package br.com.cashcontroller.repository;

import br.com.cashcontroller.entity.AlocacaoMeta;
import br.com.cashcontroller.entity.enums.CategoriaAlocacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlocacaoMetaRepository extends JpaRepository<AlocacaoMeta, Long> {

    @Query("SELECT a FROM AlocacaoMeta a WHERE a.user.id = :userId")
    List<AlocacaoMeta> findAllByUser(@Param("userId") Long userId);

    @Query("SELECT a FROM AlocacaoMeta a WHERE a.user.id = :userId AND a.categoria = :categoria")
    Optional<AlocacaoMeta> findByUserAndCategoria(@Param("userId") Long userId,
                                                  @Param("categoria") CategoriaAlocacao categoria);
}
