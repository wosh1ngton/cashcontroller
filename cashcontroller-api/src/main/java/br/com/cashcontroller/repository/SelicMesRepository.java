package br.com.cashcontroller.repository;

import br.com.cashcontroller.entity.IpcaMes;
import br.com.cashcontroller.entity.SelicMes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SelicMesRepository extends JpaRepository<SelicMes, Integer> {

    public Optional<SelicMes> findByData(@Param("data") LocalDate Data);
}
