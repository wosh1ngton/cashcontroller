package br.com.cashcontroller.repository;

import br.com.cashcontroller.entity.EventoRendaVariavel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<EventoRendaVariavel, Integer> {

    @Query("SELECT " +
            "op " +
            "FROM EventoRendaVariavel op " +
            "INNER JOIN op.ativo a " +
            "INNER JOIN a.subclasseAtivo sub " +
            "WHERE " +
            "((:endDate IS NULL) OR (:startDate IS NULL) OR (op.dataPagamento BETWEEN :startDate AND :endDate)) " +
            "AND ((:id IS NULL) OR (:id = 0) OR (sub.id = :id))" +
            "AND ((:ano IS NULL or :ano = 0) OR (:mes IS NULL  or :mes = 0) OR (YEAR(op.dataPagamento) = :ano AND MONTH(op.dataPagamento) = :mes))")
    List<EventoRendaVariavel> findEventosByData(@Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate,
                                                    @Param("id") Integer id,
                                                    @Param("ano") Integer ano,
                                                    @Param("mes") Integer mes);



    @Query("SELECT " +
            "ev " +
            "FROM EventoRendaVariavel ev " +
            "INNER JOIN ev.ativo a " +
            "INNER JOIN a.subclasseAtivo sub " +
            "INNER JOIN ev.tipoEvento te " +
            "WHERE " +
            "a.id = :ativoId")
    List<EventoRendaVariavel> findEventosByAtivo(@Param("ativoId") Integer ativoId);



}
