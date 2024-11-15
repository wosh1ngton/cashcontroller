package br.com.cashcontroller.repository;

import br.com.cashcontroller.entity.Aporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AporteRepository extends JpaRepository<Aporte, Integer> {


}
