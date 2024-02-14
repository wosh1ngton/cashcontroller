package br.com.cashcontroller.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.cashcontroller.entity.Ativo;
import br.com.cashcontroller.entity.SubclasseAtivo;
				
@Repository
public interface SubclasseRepository extends JpaRepository<SubclasseAtivo, Integer> {

	@Query("SELECT s FROM SubclasseAtivo s JOIN FETCH s.classeAtivo")
	List<SubclasseAtivo> findAllClasseAtivo();
}
