package br.com.cashcontroller.repository;

import br.com.cashcontroller.entity.Indexador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexadorRepository extends JpaRepository<Indexador, Integer> {

}
