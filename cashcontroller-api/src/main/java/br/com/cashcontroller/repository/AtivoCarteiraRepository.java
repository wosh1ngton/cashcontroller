package br.com.cashcontroller.repository;

import br.com.cashcontroller.dto.PatrimonioCategoriaDTO;
import br.com.cashcontroller.dto.ProventosMesDTO;
import br.com.cashcontroller.dto.TopPagadoraProventosDTO;
import br.com.cashcontroller.entity.Ativo;
import br.com.cashcontroller.entity.AtivoCarteira;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface AtivoCarteiraRepository extends JpaRepository<AtivoCarteira, Integer> {

    @Query("SELECT ac FROM AtivoCarteira ac WHERE ac.ativo.id = :idAtivo")
    Optional<AtivoCarteira> findByIdAtivo(@Param("idAtivo") int idAtivo);

    @Query("SELECT " +
            "new br.com.cashcontroller.dto.PatrimonioCategoriaDTO(" +
            "sub.id, " +
            "SUM(ac.valorMercado))" +
            "FROM AtivoCarteira ac " +
            "INNER JOIN ac.ativo a " +
            "INNER JOIN a.subclasseAtivo sub " +
            "GROUP BY " +
            "sub.id ")
    List<PatrimonioCategoriaDTO> getPatrimonioPorCategoria();


    @Query("SELECT " +
            "new br.com.cashcontroller.dto.ProventosMesDTO( " +
            "concat(year(ev.dataPagamento), '-', lpad((CAST(month(ev.dataPagamento) AS STRING)), 2, '0')), " +
            "SUM(CASE WHEN te.id = 2 THEN ev.valor - (ev.valor * 0.15) ELSE ev.valor END * " +
            "COALESCE((SELECT SUM(CASE WHEN top.id != 2 and top.id != 5 and top.id != 6 THEN op.quantidadeNegociada " +
            "WHEN top.id = 2 or top.id = 5 THEN -op.quantidadeNegociada ELSE 0 END)  " +
            "FROM OperacaoRendaVariavel op JOIN op.tipoOperacao top " +
            "WHERE op.ativo.id = ev.ativo.id AND op.dataOperacao <= ev.dataCom), 0 ))," +
            "ev.ativo.subclasseAtivo.id " +
            ") " +
            "FROM EventoRendaVariavel ev JOIN ev.tipoEvento te " +
            "GROUP BY concat(year(ev.dataPagamento), '-', lpad((CAST(month(ev.dataPagamento) AS STRING)), 2, '0'))," +
            "ev.ativo.subclasseAtivo.id " +
            "ORDER BY concat(year(ev.dataPagamento), '-', lpad((CAST(month(ev.dataPagamento) AS STRING)), 2, '0'))")
    List<ProventosMesDTO> listarProventosAnoMes();


    @Query("SELECT " +
            "new br.com.cashcontroller.dto.TopPagadoraProventosDTO( " +
            "a.sigla, " +
            "a.subclasseAtivo.id, " +
            "SUM(CASE WHEN te.id = 2 THEN ev.valor - (ev.valor * 0.15) ELSE ev.valor END * " +
            "COALESCE((SELECT SUM(CASE WHEN top.id != 2 and top.id != 5 and top.id != 6 THEN op.quantidadeNegociada " +
            "WHEN top.id = 2 or top.id = 5 THEN -op.quantidadeNegociada ELSE 0 END)  " +
            "FROM OperacaoRendaVariavel op JOIN op.tipoOperacao top " +
            "WHERE op.ativo.id = ev.ativo.id AND op.dataOperacao <= ev.dataCom), 0 )) " +
            ") " +
            "FROM EventoRendaVariavel ev JOIN ev.tipoEvento te " +
            "JOIN ev.ativo a " +
            "GROUP BY a.sigla, a.subclasseAtivo.id")
    List<TopPagadoraProventosDTO> listarTopPagadoras();

    @Query("SELECT " +
            "a FROM AtivoCarteira ac " +
            "JOIN ac.ativo a " +
            "JOIN a.subclasseAtivo sub " +
            "WHERE " +
            "ac.custodia > 0 AND " +
            "sub.id = 1")
    List<Ativo> findAllFiis();

}
