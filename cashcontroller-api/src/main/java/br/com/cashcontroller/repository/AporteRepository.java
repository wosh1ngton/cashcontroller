package br.com.cashcontroller.repository;

import br.com.cashcontroller.entity.Aporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AporteRepository extends JpaRepository<Aporte, Integer> {

    @Query("SELECT a FROM Aporte a WHERE a.user.id = :userId")
    List<Aporte> findAllByUser(@Param("userId") Long userId);

    @Query("SELECT a FROM Aporte a WHERE a.id = :id AND a.user.id = :userId")
    Optional<Aporte> findByIdAndUser(@Param("id") Integer id, @Param("userId") Long userId);

}
