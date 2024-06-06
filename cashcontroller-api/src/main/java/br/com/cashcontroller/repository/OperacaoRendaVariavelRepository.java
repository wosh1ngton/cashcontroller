package br.com.cashcontroller.repository;

import br.com.cashcontroller.dto.AtivoCarteiraDTO;
import br.com.cashcontroller.dto.AtivoDTO;
import br.com.cashcontroller.dto.MesDTO;
import br.com.cashcontroller.entity.OperacaoRendaVariavel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OperacaoRendaVariavelRepository extends JpaRepository<OperacaoRendaVariavel, Integer> {

    @Query("SELECT " +
                "op " +
            "FROM OperacaoRendaVariavel op " +
            "INNER JOIN op.ativo a " +
            "INNER JOIN a.subclasseAtivo sub " +
            "WHERE " +
            "((:endDate IS NULL) OR (:startDate IS NULL) OR (op.dataOperacao BETWEEN :startDate AND :endDate)) " +
            "AND ((:id IS NULL) OR (:id = 0) OR (sub.id = :id))" +
            "AND ((:ano IS NULL or :ano = 0) OR (:mes IS NULL  or :mes = 0) OR (YEAR(op.dataOperacao) = :ano AND MONTH(op.dataOperacao) = :mes))")
    List<OperacaoRendaVariavel> findOperacoesByData(@Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate,
                                                    @Param("id") Integer id,
                                                    @Param("ano") Integer ano,
                                                    @Param("mes") Integer mes);




    @Query("SELECT new br.com.cashcontroller.dto.AtivoDTO( " +
            "a.id, " +
            "a.nome, " +
            "sum(op.valorTotal) / sum(op.quantidadeNegociada)) " +
            "FROM OperacaoRendaVariavel op " +
            "INNER JOIN op.ativo a " +
            "INNER JOIN op.tipoOperacao to " +
            "WHERE a.id = :idAtivo AND (op.dataOperacao BETWEEN :startDate AND :dataDeCorte) AND to.id = 1")
    AtivoDTO calcularPrecoMedio(@Param("idAtivo") Integer idAtivo,
                                @Param("dataDeCorte") LocalDate dataDeCorte,
                                @Param("startDate") LocalDate startDate);


    @Query("SELECT new br.com.cashcontroller.dto.AtivoCarteiraDTO(" +
            "    a, " +
            "    SUM(op.quantidadeNegociada), " +
            "    (" +
            "        SELECT SUM(opi.valorTotal) / SUM(opi.quantidadeNegociada) " +
            "        FROM OperacaoRendaVariavel opi JOIN opi.tipoOperacao to" +
            "        WHERE to.id = 1 AND opi.ativo.id = op.ativo.id " +
            "    ) AS pm) " +
            "FROM " +
            "    OperacaoRendaVariavel op " +
            "JOIN " +
            "    op.ativo a " +
            "GROUP BY " +
            "    a")
    List<AtivoCarteiraDTO> listarCarteiraDeAcoes();

    @Query("SELECT distinct(YEAR(op.dataOperacao)) FROM OperacaoRendaVariavel op ")
    List<Integer> listarAnosComOperacoes();

    @Query("SELECT distinct new br.com.cashcontroller.dto.MesDTO(Month(id.dataOperacao) mes, Year(id.dataOperacao))  FROM OperacaoRendaVariavel id WHERE year(id.dataOperacao) = :ano ORDER BY  Month(id.dataOperacao) ")
    List<MesDTO> listarMesesComOperacoesPorAno(@Param("ano") int ano);
}
