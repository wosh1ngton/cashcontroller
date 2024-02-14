package br.com.cashcontroller.repository;

import br.com.cashcontroller.entity.OperacaoRendaVariavel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperacaoRendaVariavelRepository extends JpaRepository<OperacaoRendaVariavel, Integer> {
}
