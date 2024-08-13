package br.com.cashcontroller.repository;

import br.com.cashcontroller.entity.IpcaMes;
import br.com.cashcontroller.entity.SelicMes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelicMesRepository extends JpaRepository<SelicMes, Integer> {

}
