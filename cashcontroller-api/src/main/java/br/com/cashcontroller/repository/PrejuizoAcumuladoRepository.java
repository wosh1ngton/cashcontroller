package br.com.cashcontroller.repository;

import br.com.cashcontroller.entity.PrejuizoAcumulado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface PrejuizoAcumuladoRepository extends JpaRepository<PrejuizoAcumulado,Long> {

    @Query("SELECT p.prejuizoAcumulado FROM PrejuizoAcumulado p " +
            "WHERE p.anoMes = :anoMes AND p.subclasseAtivoId = :subclasseAtivoId")
    Double findPrejuizoAcumulado(String anoMes, Integer subclasseAtivoId);


    PrejuizoAcumulado findByAnoMesAndSubclasseAtivoId(String anoMes, Integer subclasseAtivoId);

}
