package br.com.cashcontroller.service;

import br.com.cashcontroller.dto.IndexadorDTO;
import br.com.cashcontroller.dto.IndiceDTO;
import br.com.cashcontroller.entity.IpcaMes;
import br.com.cashcontroller.entity.SelicMes;
import br.com.cashcontroller.mapper.IndexadorMapper;
import br.com.cashcontroller.mapper.IndexadorValorMesMapper;
import br.com.cashcontroller.repository.IndexadorRepository;
import br.com.cashcontroller.repository.IpcaMesRepository;
import br.com.cashcontroller.repository.SelicMesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    public IndiceDTO cadastrarIpcaMes(IndiceDTO indiceDTO) {

            IpcaMes ipcaMes = new IpcaMes();
            if(indiceDTO.getTipo().equals("IPCA")) {
                ipcaMes.setData(indiceDTO.getData());
                ipcaMes.setValor(indiceDTO.getValor());
                try {
                    ipcaMes = ipcaMesRepository.save(ipcaMes);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
            return IndexadorValorMesMapper.INSTANCE.fromIPCAtoDTO(ipcaMes);
    }

    public void editarIPCAMes(IndiceDTO indiceDTO) {
        if(Objects.nonNull(indiceDTO.getId()) ) {
            var indice = ipcaMesRepository.findById(indiceDTO.getId());
            if(indice.isPresent()){
                indice.get().setValor(indiceDTO.getValor());
                ipcaMesRepository.saveAndFlush(indice.get());
            }

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
