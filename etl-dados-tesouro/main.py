import pandas as pd
import mysql.connector
from mysql.connector import Error
import schedule
import time
import logging
from datetime import datetime
import os
from dotenv import load_dotenv

load_dotenv()

logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler('tesouro_direto_import.log'),
        logging.StreamHandler()
    ]
)

class TesouroDiretoImporter:
    def __init__(self):
        self.csv_url = "https://www.tesourotransparente.gov.br/ckan/dataset/df56aa42-484a-4a59-8184-7676580c81e3/resource/796d2059-14e9-44e3-80c9-2d9e30b405c1/download/precotaxatesourodireto.csv"
        
        self.column_mapping = {
            "Tipo Titulo": "TIPO_TITULO",
            "Data Vencimento": "DATA_VENCIMENTO", 
            "Data Base": "DATA_BASE",
            "Taxa Compra Manha": "TAXA_COMPRA_MANHA",
            "Taxa Venda Manha": "TAXA_VENDA_MANHA",
            "PU Compra Manha": "PU_COMPRA_MANHA",
            "PU Base Manha": "PU_BASE_MANHA"
        }
        self.db_config = {
            'host': os.getenv('DB_SERVER'),
            'database': os.getenv('DB_NAME'),
            'user': os.getenv('DB_USER'),
            'password': os.getenv('DB_PASSWORD'),
            'port': int(os.getenv('DB_PORT'))
        }

    def create_connection(self):
        """Cria conexão com a base de dados"""
        try:
            connection = mysql.connector.connect(**self.db_config)
            logging.info("Database connection established successfully")
            return connection
        except Error as e:
            logging.error(f"Error connecting to MySQL: {e}")
            return None
    
    def concatenate_year_to_titulo(self, tipo_titulo, data_vencimento):
        """Concatenate year from data_vencimento to tipo_titulo"""
        try:
            if pd.isna(data_vencimento) or pd.isna(tipo_titulo):
                return tipo_titulo
            
            # Extract year from data_vencimento
            if isinstance(data_vencimento, (pd.Timestamp, datetime)):
                year = data_vencimento.year
            else:
                # If it's a string or date object
                year = pd.to_datetime(data_vencimento).year
            
            # Remove any existing year in the title to avoid duplicates
            import re
            # Remove years that are already in the title (4-digit numbers)
            tipo_titulo_clean = re.sub(r'\s\d{4}$', '', str(tipo_titulo))
            
            # Concatenate the year
            return f"{tipo_titulo_clean} {year}"
            
        except Exception as e:
            logging.warning(f"Error concatenating year to titulo: {e}")
            return tipo_titulo
            
    def concatenate_year_to_titulo(self, tipo_titulo, data_vencimento):
        """Concatenate year from data_vencimento to tipo_titulo"""
        try:
            if pd.isna(data_vencimento) or pd.isna(tipo_titulo):
                return tipo_titulo
            
            # Extract year from data_vencimento
            if isinstance(data_vencimento, (pd.Timestamp, datetime)):
                year = data_vencimento.year
            else:
                # If it's a string or date object
                year = pd.to_datetime(data_vencimento).year
            
            # Remove any existing year in the title to avoid duplicates
            import re
            # Remove years that are already in the title (4-digit numbers)
            tipo_titulo_clean = re.sub(r'\s\d{4}$', '', str(tipo_titulo))
            
            # Concatenate the year
            return f"{tipo_titulo_clean} {year}"
            
        except Exception as e:
            logging.warning(f"Error concatenating year to titulo: {e}")
            return tipo_titulo
        
    def download_and_process_csv(self):
        """Download CSV and process the data"""
        try:
            logging.info("Baixando CSV...")
            
            # Read CSV directly from URL
            df = pd.read_csv(self.csv_url, sep=';', decimal=',', thousands='.')
            
            logging.info(f"Original CSV columns: {df.columns.tolist()}")
            logging.info(f"Original CSV shape: {df.shape}")
            
            # Select only the required columns (check for existence first)
            required_columns = list(self.column_mapping.keys())
            available_columns = [col for col in required_columns if col in df.columns]
            
            if len(available_columns) != len(required_columns):
                missing_columns = set(required_columns) - set(available_columns)
                logging.warning(f"Missing columns in CSV: {missing_columns}")
            
            df = df[available_columns]
            
            # Rename columns to UPPER_CASE format
            df = df.rename(columns=self.column_mapping)
            
            # Convert date columns to proper format
            date_columns = ['DATA_VENCIMENTO', 'DATA_BASE']
            for col in date_columns:
                if col in df.columns:
                    try:
                        df[col] = pd.to_datetime(df[col], format='%d/%m/%Y', errors='coerce').dt.date
                    except Exception as e:
                        logging.warning(f"Error converting date column {col}: {e}")
                        df[col] = pd.to_datetime(df[col], errors='coerce').dt.date
            
            # Convert numeric columns
            numeric_columns = ['TAXA_COMPRA_MANHA', 'TAXA_VENDA_MANHA', 'PU_COMPRA_MANHA', 'PU_BASE_MANHA']
            for col in numeric_columns:
                if col in df.columns:
                    df[col] = pd.to_numeric(df[col], errors='coerce')
            
            # Remove rows with missing essential data
            essential_columns = ['TIPO_TITULO', 'DATA_BASE']
            df = df.dropna(subset=essential_columns)
            
            # Apply the year concatenation to TIPO_TITULO
            logging.info("Concatenating years to TIPO_TITULO...")
            df['TIPO_TITULO'] = df.apply(
                lambda row: self.concatenate_year_to_titulo(
                    row['TIPO_TITULO'], 
                    row['DATA_VENCIMENTO']
                ), 
                axis=1
            )
            
            logging.info(f"Processed {len(df)} rows from CSV")
            logging.info(f"Sample data after year concatenation:\n{df.head()}")
            
            # Show some examples of the transformation
            sample_titles = df[['TIPO_TITULO', 'DATA_VENCIMENTO']].head(10)
            for _, row in sample_titles.iterrows():
                logging.info(f"Example: {row['TIPO_TITULO']} (Vencimento: {row['DATA_VENCIMENTO']})")
            
            return df
            
        except Exception as e:
            logging.error(f"Error downloading/processing CSV: {e}")
            return None

    def save_to_database(self, df):
        """Save processed data to MySQL database"""
        if df is None or df.empty:
            logging.warning("No data to save")
            return False
        
        connection = self.create_connection()
        if connection is None:
            return False
        
        cursor = None
        try:
            cursor = connection.cursor()
            
            # Check if data for today already exists to avoid duplicates
            today = datetime.now().date()
            check_query = """
                SELECT COUNT(*) FROM pu_tesouro_direto 
                WHERE DATE(DATA_IMPORTACAO) = %s
            """
            cursor.execute(check_query, (today,))
            existing_count = cursor.fetchone()[0]
            
            if existing_count > 0:
                logging.info(f"Data for today ({today}) already exists. Skipping import.")
                return True
            
            # Insert new data
            insert_query = """
                INSERT INTO pu_tesouro_direto 
                (TIPO_TITULO, DATA_VENCIMENTO, DATA_BASE, TAXA_COMPRA_MANHA, 
                 TAXA_VENDA_MANHA, PU_COMPRA_MANHA, PU_BASE_MANHA)
                VALUES (%s, %s, %s, %s, %s, %s, %s)
            """
            
            # Convert DataFrame to list of tuples for batch insert
            data_tuples = []
            for _, row in df.iterrows():
                tuple_data = (
                    row.get('TIPO_TITULO'),
                    row.get('DATA_VENCIMENTO'),
                    row.get('DATA_BASE'),
                    row.get('TAXA_COMPRA_MANHA'),
                    row.get('TAXA_VENDA_MANHA'),
                    row.get('PU_COMPRA_MANHA'),
                    row.get('PU_BASE_MANHA')
                )
                data_tuples.append(tuple_data)
            
            cursor.executemany(insert_query, data_tuples)
            connection.commit()
            
            logging.info(f"Successfully inserted {cursor.rowcount} rows into database")
            return True
            
        except Error as e:
            logging.error(f"Error inserting data into MySQL: {e}")
            connection.rollback()
            return False
        finally:
            if cursor:
                cursor.close()
            if connection:
                connection.close()

    def run_import_job(self):
        """Main function to run the import job"""
        logging.info("Starting Tesouro Direto import job...")
        
        # Download and process CSV
        df = self.download_and_process_csv()
        
        if df is not None:
            # Save to database
            success = self.save_to_database(df)
            if success:
                logging.info("Import job completed successfully")
            else:
                logging.error("Import job failed")
        else:
            logging.error("Failed to download or process CSV data")

def main():
    # Create importer instance
    importer = TesouroDiretoImporter()
    
    # Test the import immediately
    importer.run_import_job()
    
    # Schedule daily job at 7:00 AM
    schedule.every().day.at("07:00").do(importer.run_import_job)
    
    logging.info("Scheduler started. Job will run daily at 7:00 AM")
    
    # Keep the script running
    try:
        while True:
            schedule.run_pending()
            time.sleep(60)  # Check every minute
    except KeyboardInterrupt:
        logging.info("Script stopped by user")

if __name__ == "__main__":
    main()