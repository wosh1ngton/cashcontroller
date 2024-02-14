package br.com.cashcontroller.repository;

import br.com.cashcontroller.entity.OperacaoRendaFixa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperacaoRendaFixalRepository extends JpaRepository<OperacaoRendaFixa, Integer> {
}
