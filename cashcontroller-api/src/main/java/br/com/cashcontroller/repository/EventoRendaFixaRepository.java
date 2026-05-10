package br.com.cashcontroller.repository;

import br.com.cashcontroller.entity.Ativo;
import br.com.cashcontroller.entity.EventoRendaFixa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventoRendaFixaRepository extends JpaRepository<EventoRendaFixa, Integer> {

    @Query("SELECT op FROM EventoRendaFixa op WHERE op.user.id = :userId")
    List<EventoRendaFixa> findAllByUser(@Param("userId") Long userId);

    @Query("SELECT op FROM EventoRendaFixa op WHERE op.id = :id AND op.user.id = :userId")
    Optional<EventoRendaFixa> findByIdAndUser(@Param("id") Integer id, @Param("userId") Long userId);

    @Query("SELECT " +
            "op " +
            "FROM EventoRendaFixa op " +
            "INNER JOIN op.ativo a " +
            "INNER JOIN a.subclasseAtivo sub " +
            "WHERE " +
            "op.user.id = :userId " +
            "AND ((:endDate IS NULL) OR (:startDate IS NULL) OR (op.dataPagamento BETWEEN :startDate AND :endDate)) " +
            "AND ((:id IS NULL) OR (:id = 0) OR (sub.id = :id))" +
            "AND ((:ano IS NULL or :ano = 0) OR (:mes IS NULL  or :mes = 0) OR (YEAR(op.dataPagamento) = :ano AND MONTH(op.dataPagamento) = :mes))")
    List<EventoRendaFixa> findEventosByData(@Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate,
                                                    @Param("id") Integer id,
                                                    @Param("ano") Integer ano,
                                                    @Param("mes") Integer mes,
                                                    @Param("userId") Long userId);



    @Query("SELECT " +
            "ev " +
            "FROM EventoRendaFixa ev " +
            "INNER JOIN ev.ativo a " +
            "INNER JOIN a.subclasseAtivo sub " +
            "INNER JOIN ev.tipoEvento te " +
            "WHERE " +
            "a.id = :ativoId " +
            "AND ev.user.id = :userId")
    List<EventoRendaFixa> findEventosByAtivo(@Param("ativoId") Integer ativoId, @Param("userId") Long userId);

    @Query("SELECT ev FROM EventoRendaFixa ev WHERE ev.ativo = :ativo AND ev.user.id = :userId")
    List<EventoRendaFixa> findByAtivo(@Param("ativo") Ativo ativo, @Param("userId") Long userId);

    @Query("SELECT ev FROM EventoRendaFixa ev " +
            "JOIN FETCH ev.ativo a " +
            "JOIN FETCH ev.tipoEvento " +
            "WHERE a.id IN :ativoIds AND ev.user.id = :userId")
    List<EventoRendaFixa> findEventosByAtivoIn(@Param("ativoIds") List<Integer> ativoIds, @Param("userId") Long userId);


}
