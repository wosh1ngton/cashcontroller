package br.com.cashcontroller.service;

import br.com.cashcontroller.entity.SelicMes;
import br.com.cashcontroller.repository.SelicMesRepository;
import br.com.cashcontroller.service.interfaces.IndiceMesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SelicMesService implements IndiceMesService<SelicMes> {

    @Autowired
    private SelicMesRepository repository;

    @Override
    public SelicMes save(SelicMes indice) {
        return repository.save(indice);
    }

    @Override
    public SelicMes update(int id, SelicMes indice) {
        SelicMes existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nao encontrado"));
        existing.setData(indice.getData());
        existing.setValor(indice.getValor());
        return repository.save(existing);
    }

    @Override
    public Optional<SelicMes> findById(int id) {
        return repository.findById(id);
    }
}
