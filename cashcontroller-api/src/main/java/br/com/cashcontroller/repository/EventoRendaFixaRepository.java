package br.com.cashcontroller.repository;

import br.com.cashcontroller.entity.EventoRendaFixa;
import br.com.cashcontroller.entity.EventoRendaVariavel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventoRendaFixaRepository extends JpaRepository<EventoRendaFixa, Integer> {

    @Query("SELECT " +
            "op " +
            "FROM EventoRendaFixa op " +
            "INNER JOIN op.ativo a " +
            "INNER JOIN a.subclasseAtivo sub " +
            "WHERE " +
            "((:endDate IS NULL) OR (:startDate IS NULL) OR (op.dataPagamento BETWEEN :startDate AND :endDate)) " +
            "AND ((:id IS NULL) OR (:id = 0) OR (sub.id = :id))" +
            "AND ((:ano IS NULL or :ano = 0) OR (:mes IS NULL  or :mes = 0) OR (YEAR(op.dataPagamento) = :ano AND MONTH(op.dataPagamento) = :mes))")
    List<EventoRendaFixa> findEventosByData(@Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate,
                                                    @Param("id") Integer id,
                                                    @Param("ano") Integer ano,
                                                    @Param("mes") Integer mes);



    @Query("SELECT " +
            "ev " +
            "FROM EventoRendaFixa ev " +
            "INNER JOIN ev.ativo a " +
            "INNER JOIN a.subclasseAtivo sub " +
            "INNER JOIN ev.tipoEvento te " +
            "WHERE " +
            "a.id = :ativoId")
    List<EventoRendaFixa> findEventosByAtivo(@Param("ativoId") Integer ativoId);





}
