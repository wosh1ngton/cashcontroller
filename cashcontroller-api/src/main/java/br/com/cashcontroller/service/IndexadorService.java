package br.com.cashcontroller.service;

import br.com.cashcontroller.dto.IndexadorDTO;
import br.com.cashcontroller.entity.IpcaMes;
import br.com.cashcontroller.entity.SelicMes;
import br.com.cashcontroller.mapper.IndexadorMapper;
import br.com.cashcontroller.repository.IndexadorRepository;
import br.com.cashcontroller.repository.IpcaMesRepository;
import br.com.cashcontroller.repository.SelicMesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class IndexadorService {

    @Autowired
    IndexadorRepository repository;

    @Autowired
    IpcaMesRepository ipcaMesRepository;
    @Autowired
    SelicMesRepository selicMesRepository;

    public List<IndexadorDTO> listarIndexadores() {
        return IndexadorMapper.INSTANCE.toListDTO(repository.findAll());
    }

    public List<IpcaMes> cadastrarIpcaMesEmLote(List<IpcaMes> ipcas) {
        try {
            return ipcaMesRepository.saveAll(ipcas);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<SelicMes> cadastrarSelicMesEmLote(List<SelicMes> selics) {
        try {
            return selicMesRepository.saveAll(selics);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
