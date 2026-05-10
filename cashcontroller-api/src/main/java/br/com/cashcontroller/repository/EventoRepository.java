package br.com.cashcontroller.repository;

import br.com.cashcontroller.entity.EventoRendaVariavel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventoRepository extends JpaRepository<EventoRendaVariavel, Integer> {

    @Query("SELECT op FROM EventoRendaVariavel op WHERE op.user.id = :userId")
    List<EventoRendaVariavel> findAllByUser(@Param("userId") Long userId);

    @Query("SELECT op FROM EventoRendaVariavel op WHERE op.id = :id AND op.user.id = :userId")
    Optional<EventoRendaVariavel> findByIdAndUser(@Param("id") Integer id, @Param("userId") Long userId);

    @Query("SELECT " +
            "op " +
            "FROM EventoRendaVariavel op " +
            "INNER JOIN op.ativo a " +
            "INNER JOIN a.subclasseAtivo sub " +
            "WHERE " +
            "op.user.id = :userId " +
            "AND ((:endDate IS NULL) OR (:startDate IS NULL) OR (op.dataPagamento BETWEEN :startDate AND :endDate)) " +
            "AND ((:id IS NULL) OR (:id = 0) OR (sub.id = :id)) " +
            "AND ((:ativo IS NULL) OR (:ativo = 0) OR (a.id = :ativo)) " +
            "AND ((:ano IS NULL or :ano = 0) OR (:mes IS NULL  or :mes = 0) OR (YEAR(op.dataPagamento) = :ano AND MONTH(op.dataPagamento) = :mes))")
    List<EventoRendaVariavel> findEventosByData(@Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate,
                                                    @Param("id") Integer id,
                                                    @Param("ano") Integer ano,
                                                    @Param("mes") Integer mes,
                                                    @Param("ativo") Integer ativo,
                                                    @Param("userId") Long userId);



    @Query("SELECT " +
            "ev " +
            "FROM EventoRendaVariavel ev " +
            "INNER JOIN ev.ativo a " +
            "INNER JOIN a.subclasseAtivo sub " +
            "INNER JOIN ev.tipoEvento te " +
            "WHERE " +
            "a.id = :ativoId " +
            "AND ev.user.id = :userId")
    List<EventoRendaVariavel> findEventosByAtivo(@Param("ativoId") Integer ativoId, @Param("userId") Long userId);

    @Query("SELECT ev FROM EventoRendaVariavel ev JOIN FETCH ev.ativo JOIN FETCH ev.tipoEvento WHERE ev.ativo.id IN :ids AND ev.user.id = :userId ORDER BY ev.dataPagamento ASC")
    List<EventoRendaVariavel> findByAtivoIdIn(@Param("ids") List<Integer> ids, @Param("userId") Long userId);

}
