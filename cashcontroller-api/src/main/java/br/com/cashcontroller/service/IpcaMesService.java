package br.com.cashcontroller.service;

import br.com.cashcontroller.entity.IpcaMes;
import br.com.cashcontroller.repository.IpcaMesRepository;
import br.com.cashcontroller.service.interfaces.IndiceMesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IpcaMesService implements IndiceMesService<IpcaMes> {

    @Autowired
    private IpcaMesRepository repository;

    @Override
    public IpcaMes save(IpcaMes indice) {
        return repository.save(indice);
    }

    @Override
    public IpcaMes update(int id, IpcaMes updated) {
        IpcaMes existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nao encontrado"));
        existing.setData(updated.getData());
        existing.setValor(updated.getValor());
        return repository.save(existing);
    }

    @Override
    public Optional<IpcaMes> findById(int id) {
        return repository.findById(id);
    }
}
