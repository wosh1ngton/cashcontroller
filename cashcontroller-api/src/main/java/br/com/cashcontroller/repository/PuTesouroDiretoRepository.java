package br.com.cashcontroller.repository;

import br.com.cashcontroller.entity.PuTesouroDireto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PuTesouroDiretoRepository extends JpaRepository<PuTesouroDireto, Long> {

    // Busca somente os títulos específicos informados — evita full table scan na tabela inteira.
    @Query(value =
        "SELECT pu.* FROM pu_tesouro_direto pu " +
        "INNER JOIN (" +
        "    SELECT tipo_titulo, MAX(data_base) AS data_maxima " +
        "    FROM pu_tesouro_direto " +
        "    WHERE tipo_titulo IN (:titulos)" +
        "    GROUP BY tipo_titulo" +
        ") maxDates ON pu.tipo_titulo = maxDates.tipo_titulo AND pu.data_base = maxDates.data_maxima",
        nativeQuery = true)
    List<PuTesouroDireto> listarValoresMaisRecentesPorTitulos(@org.springframework.data.repository.query.Param("titulos") List<String> titulos);

    // Fallback: busca todos os títulos (uso apenas quando lista de títulos não está disponível)
    @Query(value =
        "SELECT pu.* FROM pu_tesouro_direto pu " +
        "INNER JOIN (" +
        "    SELECT tipo_titulo, MAX(data_base) AS data_maxima " +
        "    FROM pu_tesouro_direto " +
        "    GROUP BY tipo_titulo" +
        ") maxDates ON pu.tipo_titulo = maxDates.tipo_titulo AND pu.data_base = maxDates.data_maxima",
        nativeQuery = true)
    List<PuTesouroDireto> listarValoresMaisRecentesPorTitulo();
}
