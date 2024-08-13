package br.com.cashcontroller.repository;

import br.com.cashcontroller.dto.AtivoCarteiraDTO;
import br.com.cashcontroller.dto.AtivoCarteiraRFDTO;
import br.com.cashcontroller.dto.AtivoDTO;
import br.com.cashcontroller.dto.MesDTO;
import br.com.cashcontroller.entity.OperacaoRendaFixa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OperacaoRendaFixaRepository extends JpaRepository<OperacaoRendaFixa, Integer> {

    String STRING_QUERY = "SUM(CASE" +
            "       WHEN top.id = 1 THEN op.quantidadeNegociada" +
            "       WHEN top.id = 2 THEN -op.quantidadeNegociada " +
            "       ELSE 0 " +
            "       END) > 0";

    @Query("SELECT DISTINCT new br.com.cashcontroller.dto.MesDTO(Month(of.dataOperacao) as mes, Year(of.dataOperacao) as ano) " +
            "FROM OperacaoRendaFixa of " +
            "WHERE Year(of.dataOperacao) = :ano"  )
    List<MesDTO> listarMesesComOperacoesPorAno(@Param("ano") int ano);


    @Query("SELECT distinct(YEAR(oprf.dataOperacao)) FROM OperacaoRendaFixa oprf ")
    List<Integer> listarAnosComOperacoes();

    @Query("SELECT " +
            "op " +
            "FROM OperacaoRendaFixa op " +
            "INNER JOIN op.ativo a " +
            "INNER JOIN a.subclasseAtivo sub " +
            "WHERE " +
            "((:endDate IS NULL) OR (:startDate IS NULL) OR (op.dataOperacao BETWEEN :startDate AND :endDate)) " +
            "AND ((:id IS NULL) OR (:id = 0) OR (sub.id = :id))" +
            "AND ((:ano IS NULL or :ano = 0) OR (:mes IS NULL  or :mes = 0) OR (YEAR(op.dataOperacao) = :ano AND MONTH(op.dataOperacao) = :mes))")
    List<OperacaoRendaFixa> findOperacoesByData(@Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate,
                                                    @Param("id") Integer id,
                                                    @Param("ano") Integer ano,
                                                    @Param("mes") Integer mes);


    @Query("SELECT new br.com.cashcontroller.dto.AtivoCarteiraRFDTO(" +
            "    a.id," +
            "    a.nome," +
            "    a.sigla, " +
            "    a.subclasseAtivo.id, " +
            "    op.taxaContratada,  " +
            "    ind.id,   " +
            " COALESCE((SELECT SUM(CASE WHEN to.id = 1 THEN opi.custoTotal WHEN to.id = 2 THEN -opi.custoTotal ELSE 0 END) " +
            "    / SUM(CASE WHEN to.id = 1 THEN opi.quantidadeNegociada WHEN to.id = 2 THEN -opi.quantidadeNegociada ELSE 0 END) " +
            "    FROM OperacaoRendaFixa opi JOIN opi.tipoOperacao to WHERE opi.ativo.id = op.ativo.id),0.0) as PM, " +
            "    SUM(CASE " +
            "        WHEN top.id = 1 THEN op.quantidadeNegociada " +
            "        WHEN top.id = 2 THEN -op.quantidadeNegociada " +
            "        ELSE 0 " +
            "    END) AS custodia) " +
            "FROM" +
            "    OperacaoRendaFixa op " +
            "JOIN " +
            "    op.ativo a " +
            "JOIN " +
            "   a.parametroRendaFixa prf " +
            "JOIN " +
            "   a.parametroRendaFixa.indexador ind  " +
            "JOIN " +
            "   op.tipoOperacao top " +
            "GROUP BY " +
            "    a.id, a.nome, a.sigla, ind.id, op.taxaContratada  " +
            "HAVING " +
            "       " + STRING_QUERY)
    List<AtivoCarteiraRFDTO> listarCarteiraRendaFixa1();



    @Query("SELECT new br.com.cashcontroller.dto.AtivoCarteiraRFDTO(" +
            "    a.id, " +
            "    a.nome, " +
            "    a.sigla, " +
            "    sub.id, " +
            "    op.taxaContratada, " +
            "    SUM(CASE WHEN top.id = 1 THEN op.quantidadeNegociada WHEN top.id = 2 THEN -op.quantidadeNegociada ELSE 0 END) AS quantidade, " +
            "    SUM(CASE WHEN top.id = 1 THEN op.custoTotal WHEN top.id = 2 THEN -op.custoTotal ELSE 0 END) / SUM(CASE WHEN top.id = 1 THEN op.quantidadeNegociada WHEN top.id = 2 THEN -op.quantidadeNegociada ELSE 0 END) AS pm, " +
            "    ind.id," +
            "    op.dataOperacao," +
            "    ind.nome," +
            "    op.id," +
            "    (SELECT PRFI.isIsento FROM ParametroRendaFixa PRFI WHERE PRFI.ativo.id = a.id) ISENTO " +
            ") " +
            "FROM OperacaoRendaFixa op " +
            "JOIN op.ativo a " +
            "JOIN a.subclasseAtivo sub " +
            "JOIN a.parametroRendaFixa prf " +
            "JOIN prf.indexador ind " +
            "JOIN op.tipoOperacao top " +
            "WHERE a.id IN ( " +
            "    SELECT aInner.id " +
            "    FROM OperacaoRendaFixa opInner " +
            "    JOIN opInner.ativo aInner " +
            "    JOIN opInner.tipoOperacao topInner " +
            "    GROUP BY aInner.id " +
            "    HAVING SUM(CASE WHEN topInner.id = 1 THEN opInner.quantidadeNegociada WHEN topInner.id = 2 THEN -opInner.quantidadeNegociada ELSE 0 END) > 0 " +
            ") " +
            "GROUP BY a.id, a.nome, a.sigla, sub.id, op.taxaContratada, ind.id, ind.nome, op.dataOperacao, op.id " +
            "HAVING SUM(CASE WHEN top.id = 1 THEN op.quantidadeNegociada WHEN top.id = 2 THEN -op.quantidadeNegociada ELSE 0 END) > 0")
    List<AtivoCarteiraRFDTO> listarCarteiraRendaFixa();



    @Query("SELECT SUM(CASE WHEN to.id = 1 THEN opi.custoTotal WHEN to.id = 2 THEN -opi.custoTotal ELSE 0 END) " +
            " /  SUM(CASE WHEN to.id = 1 THEN opi.quantidadeNegociada WHEN to.id = 2 THEN -opi.quantidadeNegociada ELSE 0 END) " +
            " FROM OperacaoRendaFixa opi JOIN opi.tipoOperacao to WHERE opi.ativo.id = :ativoId")
    Double findPrecoMedioByAtivoId(@Param("ativoId") int ativoId);


    @Query("SELECT new br.com.cashcontroller.dto.AtivoDTO( " +
            "a.id, " +
            "a.nome, " +
            "SUM(CASE WHEN to.id = 1 THEN op.custoTotal WHEN to.id = 2 THEN -op.custoTotal ELSE 0 END) / SUM(CASE WHEN to.id = 1 THEN op.quantidadeNegociada WHEN to.id = 2 THEN -op.quantidadeNegociada ELSE 0 END)) " +
            "FROM OperacaoRendaFixa op " +
            "INNER JOIN op.ativo a " +
            "INNER JOIN op.tipoOperacao to " +
            "WHERE a.id = :idAtivo AND (op.dataOperacao BETWEEN :startDate AND :dataDeCorte)")
    AtivoDTO calcularPrecoMedio(@Param("idAtivo") int idAtivo,
                                @Param("dataDeCorte") LocalDate dataDeCorte,
                                @Param("startDate") LocalDate startDate);



    @Query("SELECT op FROM OperacaoRendaFixa op " +
            "WHERE (SELECT COUNT(1) FROM OperacaoRendaFixa opi " +
            "WHERE opi.tipoOperacao.id = 2 AND opi.ativo.id = :idAtivo) = 1 " +
            "AND op.ativo.id = :idAtivo " +
            "ORDER BY op.dataOperacao ASC")
    List<OperacaoRendaFixa> listarOperacoesAtivoVendaParcial(@Param("idAtivo") int idAtivo);

    @Query("SELECT SUM(CASE WHEN top.id = 1 THEN op.quantidadeNegociada WHEN top.id = 2 THEN -op.quantidadeNegociada ELSE 0 END) AS quantidade FROM OperacaoRendaFixa op JOIN op.tipoOperacao top WHERE op.ativo.id = :idAtivo")
    double getCustodiaByIdAtivo(@Param("idAtivo") int idAtivo);
}
