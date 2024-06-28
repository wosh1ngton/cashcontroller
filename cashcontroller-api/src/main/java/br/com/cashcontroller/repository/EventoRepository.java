package br.com.cashcontroller.repository;

import br.com.cashcontroller.entity.EventoRendaVariavel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoRepository extends JpaRepository<EventoRendaVariavel, Integer> {


}
