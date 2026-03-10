-- Índice para otimizar a query "maior data por título" na tabela pu_tesouro_direto.
-- Sem este índice, o GROUP BY + MAX(data_base) faz full table scan.
-- Com o índice, o banco resolve o MAX usando index range scan por tipo_titulo.
--
-- Executar uma única vez no banco de dados:
CREATE INDEX IF NOT EXISTS idx_pu_tesouro_tipo_data
    ON pu_tesouro_direto (TIPO_TITULO, DATA_BASE);
