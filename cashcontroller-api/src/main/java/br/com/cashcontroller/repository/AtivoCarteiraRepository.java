package br.com.cashcontroller.repository;

import br.com.cashcontroller.entity.AtivoCarteira;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AtivoCarteiraRepository extends JpaRepository<AtivoCarteira, Integer> {

    @Query("SELECT ac FROM AtivoCarteira ac WHERE ac.ativo.id = :idAtivo")
    Optional<AtivoCarteira> findByIdAtivo(@Param("idAtivo") int idAtivo);
}
