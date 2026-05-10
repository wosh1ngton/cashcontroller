package br.com.cashcontroller.repository;

import br.com.cashcontroller.entity.PrejuizoAcumulado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrejuizoAcumuladoRepository extends JpaRepository<PrejuizoAcumulado,Long> {

    @Query("SELECT p.prejuizoAcumulado FROM PrejuizoAcumulado p " +
            "WHERE p.anoMes = :anoMes AND p.subclasseAtivoId = :subclasseAtivoId AND p.user.id = :userId")
    Double findPrejuizoAcumulado(@Param("anoMes") String anoMes,
                                 @Param("subclasseAtivoId") Integer subclasseAtivoId,
                                 @Param("userId") Long userId);


    @Query("SELECT p FROM PrejuizoAcumulado p " +
            "WHERE p.anoMes = :anoMes AND p.subclasseAtivoId = :subclasseAtivoId AND p.user.id = :userId")
    PrejuizoAcumulado findByAnoMesAndSubclasseAtivoId(@Param("anoMes") String anoMes,
                                                       @Param("subclasseAtivoId") Integer subclasseAtivoId,
                                                       @Param("userId") Long userId);

}
