package br.com.cashcontroller.repository;

import br.com.cashcontroller.entity.PuTesouroDireto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PuTesouroDiretoRepository extends JpaRepository<PuTesouroDireto, Long> {

    @Query("SELECT pu FROM PuTesouroDireto pu " +
            "INNER JOIN (SELECT puSub.tipoTitulo as titulo, MAX(puSub.dataBase) as dataMaxima " +
            "           FROM PuTesouroDireto puSub GROUP BY puSub.tipoTitulo) maxDates " +
            "ON pu.tipoTitulo = maxDates.titulo AND pu.dataBase = maxDates.dataMaxima")
    List<PuTesouroDireto> listarValoresMaisRecentesPorTitulo();
}
