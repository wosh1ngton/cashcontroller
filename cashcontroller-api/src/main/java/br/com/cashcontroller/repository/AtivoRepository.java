package br.com.cashcontroller.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cashcontroller.entity.Ativo;

@Repository
public interface AtivoRepository extends JpaRepository<Ativo, Integer> {

}
