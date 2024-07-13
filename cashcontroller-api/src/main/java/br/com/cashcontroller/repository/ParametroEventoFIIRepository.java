package br.com.cashcontroller.repository;

import br.com.cashcontroller.entity.ParametroEventoFII;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParametroEventoFIIRepository extends JpaRepository<ParametroEventoFII, Integer> {


    @Query("SELECT pf FROM ParametroEventoFII pf WHERE pf.ativo.id = :idAtivo")
    ParametroEventoFII getParametrosFiiPorAtivo(@Param("idAtivo") int idAtivo);



}
