package br.com.cashcontroller.repository;

import br.com.cashcontroller.entity.Ativo;
import br.com.cashcontroller.entity.IpcaMes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IpcaMesRepository extends JpaRepository<IpcaMes, Integer> {

}
