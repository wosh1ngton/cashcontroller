package br.com.cashcontroller.repository;

import br.com.cashcontroller.dto.AtivoCarteiraRFDTO;
import br.com.cashcontroller.entity.OperacaoRendaFixa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.cashcontroller.entity.Ativo;

import java.util.List;

@Repository
public interface AtivoRepository extends JpaRepository<Ativo, Integer> {

    @Query("SELECT a FROM Ativo a INNER JOIN a.subclasseAtivo sa " +
            "INNER JOIN sa.classeAtivo ca " +
            "LEFT JOIN a.parametroRendaFixa prf " +
            "WHERE ca.id = :id")
    List<Ativo> findByClasseAtivo(@Param("id") Integer id);


    @Query("SELECT a FROM Ativo a INNER JOIN a.subclasseAtivo sa " +
            "LEFT JOIN a.parametroRendaFixa prf " +
            "WHERE sa.id = :id OR (:id = 3 AND sa.id IN (4, 5))")
    List<Ativo> findBySubClasseAtivo(@Param("id") Integer id);


}
