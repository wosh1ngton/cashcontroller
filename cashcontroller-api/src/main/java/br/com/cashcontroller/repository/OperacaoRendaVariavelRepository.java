package br.com.cashcontroller.repository;

import br.com.cashcontroller.dto.AtivoCarteiraDTO;
import br.com.cashcontroller.dto.AtivoDTO;
import br.com.cashcontroller.dto.MesDTO;
import br.com.cashcontroller.dto.PosicaoEncerradaDTO;
import br.com.cashcontroller.entity.OperacaoRendaVariavel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
            "SUM(CASE WHEN to.id != 2 and to.id != 5 THEN op.custoTotal WHEN to.id = 2 or to.id = 5 THEN -op.custoTotal ELSE 0 END) / SUM(CASE WHEN to.id != 2 and to.id != 5 THEN op.quantidadeNegociada WHEN to.id = 2 or to.id = 5 THEN -op.quantidadeNegociada ELSE 0 END)) " +
            "FROM OperacaoRendaVariavel op " +
            "INNER JOIN op.ativo a " +
            "INNER JOIN op.tipoOperacao to " +
            "WHERE a.id = :idAtivo AND (op.dataOperacao BETWEEN :startDate AND :dataDeCorte)")
    AtivoDTO calcularPrecoMedio(@Param("idAtivo") int idAtivo,
                                @Param("dataDeCorte") LocalDate dataDeCorte,
                                @Param("startDate") LocalDate startDate);


    @Query("SELECT new br.com.cashcontroller.dto.AtivoCarteiraDTO(" +
            "    a, " +
            "    SUM(CASE " +
            "        WHEN top.id != 2 and top.id != 5 THEN op.quantidadeNegociada " +
            "        WHEN top.id = 2 or top.id = 5 THEN -op.quantidadeNegociada " +
            "        ELSE 0 " +
            "    END) AS custodia, " +
            "    (" +
            "        SELECT SUM(CASE WHEN to.id != 2 and to.id != 5 THEN opi.custoTotal WHEN to.id = 2 or to.id= 5 THEN -opi.custoTotal ELSE 0 END) / SUM(CASE WHEN to.id != 2 and to.id != 5 THEN opi.quantidadeNegociada WHEN to.id = 2 or to.id = 5 THEN -opi.quantidadeNegociada ELSE 0 END) " +
            "        FROM OperacaoRendaVariavel opi JOIN opi.tipoOperacao to WHERE opi.ativo.id = op.ativo.id " +
            "    ) AS pm) " +
            "FROM " +
            "    OperacaoRendaVariavel op " +
            "JOIN " +
            "    op.ativo a " +
            "JOIN " +
            "   op.tipoOperacao top " +
            "GROUP BY " +
            "    a " +
            "HAVING " +
            "       SUM(CASE" +
            "       WHEN top.id != 2 and top.id != 5 THEN op.quantidadeNegociada" +
            "       WHEN top.id = 2 or top.id = 5 THEN -op.quantidadeNegociada " +
            "       ELSE 0 " +
            "       END) > 0")
    List<AtivoCarteiraDTO> listarCarteiraDeAcoes();





    @Query("SELECT distinct(YEAR(op.dataOperacao)) FROM OperacaoRendaVariavel op ")
    List<Integer> listarAnosComOperacoes();

    @Query("SELECT distinct new br.com.cashcontroller.dto.MesDTO(Month(id.dataOperacao) mes, Year(id.dataOperacao))  FROM OperacaoRendaVariavel id WHERE year(id.dataOperacao) = :ano ORDER BY  Month(id.dataOperacao) ")
    List<MesDTO> listarMesesComOperacoesPorAno(@Param("ano") int ano);


    @Query("SELECT " +
            "    SUM(CASE " +
            "        WHEN top.id != 2 and top.id != 5 THEN op.quantidadeNegociada " +
            "        WHEN top.id = 2 or top.id = 5 THEN -op.quantidadeNegociada " +
            "        ELSE 0 " +
            "    END) " +
            "FROM " +
            "    OperacaoRendaVariavel op " +
            "JOIN " +
            "    op.ativo a " +
            "JOIN " +
            "   op.tipoOperacao top WHERE a.id = :id")
    Long getCustodiaPorAtivo(@Param("id") int id);


    @Query("SELECT " +
            "    COALESCE(SUM(CASE " +
            "        WHEN top.id != 2 and top.id != 5 THEN op.quantidadeNegociada " +
            "        WHEN top.id = 2 or top.id = 5 THEN -op.quantidadeNegociada " +
            "        ELSE 0 " +
            "    END),0) " +
            "FROM " +
            "    OperacaoRendaVariavel op " +
            "JOIN " +
            "    op.ativo a " +
            "JOIN " +
            "   op.tipoOperacao top WHERE a.id = :id AND op.dataOperacao <= :dataCom")
    Long getCustodiaPorAtivo(@Param("id") int id, @Param("dataCom") LocalDate dataCom);

    @Query("SELECT new br.com.cashcontroller.dto.PosicaoEncerradaDTO(" +
            "    (SELECT MIN(op2.dataOperacao) FROM OperacaoRendaVariavel op2 WHERE op2.ativo.id = a.id) as data_inicio, " +
            "    a.nome, " +
            "    SUM(CASE " +
            "        WHEN top.id != 2 and top.id != 5 THEN op.quantidadeNegociada " +
            "        WHEN top.id = 2 or top.id = 5 THEN -op.quantidadeNegociada " +
            "        ELSE 0 " +
            "    END) AS soma, " +
            "    (SELECT MAX(op3.dataOperacao) FROM OperacaoRendaVariavel op3 WHERE op3.ativo.id = a.id) as data_encerramento, " +
            "   SUM(CASE WHEN top.id = 2 THEN op.custoTotal ELSE 0 END) AS valorInvestido, " +
            "   SUM(CASE WHEN top.id = 2 THEN op.valorTotal ELSE 0 END) AS valorVenda" +
            ") " +
            "FROM OperacaoRendaVariavel op " +
            "JOIN op.ativo a " +
            "JOIN op.tipoOperacao top " +
            "GROUP BY a " +
            "HAVING " +
            "       SUM(CASE" +
            "       WHEN top.id != 2 THEN op.quantidadeNegociada" +
            "       WHEN top.id = 2 THEN -op.quantidadeNegociada " +
            "       ELSE 0 " +
            "       END) = 0")
    List<PosicaoEncerradaDTO> listarAtivosComOperacoesFechadas();

}
