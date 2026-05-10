package br.com.cashcontroller.repository;

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
import java.util.Optional;

@Repository
public interface OperacaoRendaFixaRepository extends JpaRepository<OperacaoRendaFixa, Integer> {

    String STRING_QUERY = "SUM(CASE" +
            "       WHEN top.id = 1 THEN op.quantidadeNegociada" +
            "       WHEN top.id = 2 THEN -op.quantidadeNegociada " +
            "       ELSE 0 " +
            "       END) > 0";

    @Query("SELECT op FROM OperacaoRendaFixa op WHERE op.user.id = :userId")
    List<OperacaoRendaFixa> findAllByUser(@Param("userId") Long userId);

    @Query("SELECT op FROM OperacaoRendaFixa op WHERE op.id = :id AND op.user.id = :userId")
    Optional<OperacaoRendaFixa> findByIdAndUser(@Param("id") Integer id, @Param("userId") Long userId);

    @Query("SELECT DISTINCT new br.com.cashcontroller.dto.MesDTO(Month(of.dataOperacao) as mes, Year(of.dataOperacao) as ano) " +
            "FROM OperacaoRendaFixa of " +
            "WHERE Year(of.dataOperacao) = :ano AND of.user.id = :userId"  )
    List<MesDTO> listarMesesComOperacoesPorAno(@Param("ano") int ano, @Param("userId") Long userId);


    @Query("SELECT distinct(YEAR(oprf.dataOperacao)) FROM OperacaoRendaFixa oprf WHERE oprf.user.id = :userId")
    List<Integer> listarAnosComOperacoes(@Param("userId") Long userId);

    @Query("SELECT " +
            "op " +
            "FROM OperacaoRendaFixa op " +
            "INNER JOIN op.ativo a " +
            "INNER JOIN a.subclasseAtivo sub " +
            "WHERE " +
            "op.user.id = :userId " +
            "AND ((:endDate IS NULL) OR (:startDate IS NULL) OR (op.dataOperacao BETWEEN :startDate AND :endDate)) " +
            "AND ((:id IS NULL) OR (:id = 0) OR (sub.id = :id))" +
            "AND ((:ano IS NULL or :ano = 0) OR (:mes IS NULL  or :mes = 0) OR (YEAR(op.dataOperacao) = :ano AND MONTH(op.dataOperacao) = :mes))")
    List<OperacaoRendaFixa> findOperacoesByData(@Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate,
                                                    @Param("id") Integer id,
                                                    @Param("ano") Integer ano,
                                                    @Param("mes") Integer mes,
                                                    @Param("userId") Long userId);


    @Query("SELECT new br.com.cashcontroller.dto.AtivoCarteiraRFDTO(" +
            "    a.id," +
            "    a.nome," +
            "    a.sigla, " +
            "    a.subclasseAtivo.id, " +
            "    op.taxaContratada,  " +
            "    ind.id,   " +
            " COALESCE((SELECT SUM(CASE WHEN to.id = 1 THEN opi.custoTotal WHEN to.id = 2 THEN -opi.custoTotal ELSE 0 END) " +
            "    / SUM(CASE WHEN to.id = 1 THEN opi.quantidadeNegociada WHEN to.id = 2 THEN -opi.quantidadeNegociada ELSE 0 END) " +
            "    FROM OperacaoRendaFixa opi JOIN opi.tipoOperacao to WHERE opi.ativo.id = op.ativo.id AND opi.user.id = :userId),0.0) as PM, " +
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
            "WHERE op.user.id = :userId " +
            "GROUP BY " +
            "    a.id, a.nome, a.sigla, ind.id, op.taxaContratada  " +
            "HAVING " +
            "       " + STRING_QUERY)
    List<AtivoCarteiraRFDTO> listarCarteiraRendaFixa1(@Param("userId") Long userId);



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
            "WHERE op.user.id = :userId " +
            "AND a.id IN ( " +
            "    SELECT aInner.id " +
            "    FROM OperacaoRendaFixa opInner " +
            "    JOIN opInner.ativo aInner " +
            "    JOIN opInner.tipoOperacao topInner " +
            "    WHERE opInner.user.id = :userId " +
            "    GROUP BY aInner.id " +
            "    HAVING SUM(CASE WHEN topInner.id = 1 THEN opInner.quantidadeNegociada WHEN topInner.id = 2 THEN -opInner.quantidadeNegociada ELSE 0 END) > 0 " +
            ") " +
            "GROUP BY a.id, a.nome, a.sigla, sub.id, op.taxaContratada, ind.id, ind.nome, op.dataOperacao, op.id " +
            "HAVING SUM(CASE WHEN top.id = 1 THEN op.quantidadeNegociada WHEN top.id = 2 THEN -op.quantidadeNegociada ELSE 0 END) > 0")
    List<AtivoCarteiraRFDTO> listarOperacoesAtivosCustodiados(@Param("userId") Long userId);



    @Query("SELECT SUM(CASE WHEN to.id = 1 THEN opi.custoTotal WHEN to.id = 2 THEN -opi.custoTotal ELSE 0 END) " +
            " /  SUM(CASE WHEN to.id = 1 THEN opi.quantidadeNegociada WHEN to.id = 2 THEN -opi.quantidadeNegociada ELSE 0 END) " +
            " FROM OperacaoRendaFixa opi JOIN opi.tipoOperacao to WHERE opi.ativo.id = :ativoId AND opi.user.id = :userId")
    Double findPrecoMedioByAtivoId(@Param("ativoId") int ativoId, @Param("userId") Long userId);


    @Query("SELECT new br.com.cashcontroller.dto.AtivoDTO( " +
            "a.id, " +
            "a.nome, " +
            "SUM(CASE WHEN to.id = 1 THEN op.custoTotal WHEN to.id = 2 THEN -op.custoTotal ELSE 0 END) / SUM(CASE WHEN to.id = 1 THEN op.quantidadeNegociada WHEN to.id = 2 THEN -op.quantidadeNegociada ELSE 0 END)) " +
            "FROM OperacaoRendaFixa op " +
            "INNER JOIN op.ativo a " +
            "INNER JOIN op.tipoOperacao to " +
            "WHERE a.id = :idAtivo AND (op.dataOperacao BETWEEN :startDate AND :dataDeCorte) AND op.user.id = :userId")
    AtivoDTO calcularPrecoMedio(@Param("idAtivo") int idAtivo,
                                @Param("dataDeCorte") LocalDate dataDeCorte,
                                @Param("startDate") LocalDate startDate,
                                @Param("userId") Long userId);



    @Query("SELECT op FROM OperacaoRendaFixa op " +
            "WHERE (SELECT COUNT(1) FROM OperacaoRendaFixa opi " +
            "WHERE opi.tipoOperacao.id = 2 AND opi.ativo.id = :idAtivo AND opi.user.id = :userId) = 1 " +
            "AND op.ativo.id = :idAtivo " +
            "AND op.user.id = :userId " +
            "ORDER BY op.dataOperacao ASC")
    List<OperacaoRendaFixa> listarOperacoesAtivoVendaParcial(@Param("idAtivo") int idAtivo, @Param("userId") Long userId);

    @Query("SELECT SUM(CASE WHEN top.id = 1 THEN op.quantidadeNegociada WHEN top.id = 2 THEN -op.quantidadeNegociada ELSE 0 END) AS quantidade FROM OperacaoRendaFixa op JOIN op.tipoOperacao top WHERE op.ativo.id = :idAtivo AND op.user.id = :userId")
    double getCustodiaByIdAtivo(@Param("idAtivo") int idAtivo, @Param("userId") Long userId);

    @Query("SELECT new br.com.cashcontroller.dto.AtivoCarteiraRFDTO(" +
            "oprf.dataOperacao, " +
            "prf.isIsento " +
            ") FROM OperacaoRendaFixa oprf " +
            "JOIN oprf.ativo a " +
            "JOIN a.parametroRendaFixa prf WHERE a.id = :id AND oprf.user.id = :userId")
    Optional<AtivoCarteiraRFDTO> findAtivoCarteiraRendaFixaById(@Param("id") Integer id, @Param("userId") Long userId);

    @Query("SELECT new br.com.cashcontroller.dto.AtivoCarteiraRFDTO(" +
            "a.id, " +
            "MIN(oprf.dataOperacao), " +
            "prf.isIsento" +
            ") FROM OperacaoRendaFixa oprf " +
            "JOIN oprf.ativo a " +
            "JOIN a.parametroRendaFixa prf " +
            "WHERE a.id IN :ids AND oprf.user.id = :userId " +
            "GROUP BY a.id, prf.isIsento")
    List<AtivoCarteiraRFDTO> findAtivoParamsRendaFixaByAtivoIds(@Param("ids") List<Integer> ids, @Param("userId") Long userId);
}
