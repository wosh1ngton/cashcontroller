package br.com.cashcontroller.repository;

import br.com.cashcontroller.entity.TipoOperacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoOperacaoRepository extends JpaRepository<TipoOperacao, Integer> {

}
