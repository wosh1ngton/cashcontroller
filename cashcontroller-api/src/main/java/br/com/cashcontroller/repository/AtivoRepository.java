package br.com.cashcontroller.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.cashcontroller.entity.Ativo;

import java.util.List;
import java.util.Optional;

@Repository
public interface AtivoRepository extends JpaRepository<Ativo, Integer> {

    @Query("SELECT a FROM Ativo a LEFT JOIN a.parametroRendaFixa prf " +
            "WHERE a.user IS NULL OR a.user.id = :userId")
    List<Ativo> findAllVisibleToUser(@Param("userId") Long userId);

    @Query("SELECT a FROM Ativo a LEFT JOIN a.parametroRendaFixa prf " +
            "WHERE a.id = :id AND (a.user IS NULL OR a.user.id = :userId)")
    Optional<Ativo> findByIdVisibleToUser(@Param("id") Integer id, @Param("userId") Long userId);

    @Query("SELECT a FROM Ativo a INNER JOIN a.subclasseAtivo sa " +
            "INNER JOIN sa.classeAtivo ca " +
            "LEFT JOIN a.parametroRendaFixa prf " +
            "WHERE ca.id = :id AND (a.user IS NULL OR a.user.id = :userId)")
    List<Ativo> findByClasseAtivo(@Param("id") Integer id, @Param("userId") Long userId);


    @Query("SELECT a FROM Ativo a INNER JOIN a.subclasseAtivo sa " +
            "LEFT JOIN a.parametroRendaFixa prf " +
            "WHERE (sa.id = :id OR (:id = 3 AND sa.id IN (4, 5))) " +
            "AND (a.user IS NULL OR a.user.id = :userId)")
    List<Ativo> findBySubClasseAtivo(@Param("id") Integer id, @Param("userId") Long userId);

    @Query("SELECT a.logo FROM Ativo a WHERE a.sigla = :sigla")
    Optional<String> findUrlLogoBySigla(@Param("sigla") String sigla);


}
