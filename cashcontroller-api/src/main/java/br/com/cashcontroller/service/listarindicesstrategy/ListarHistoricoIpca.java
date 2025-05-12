package br.com.cashcontroller.service.listarindicesstrategy;

import br.com.cashcontroller.dto.IndiceDTO;
import br.com.cashcontroller.entity.IpcaMes;
import br.com.cashcontroller.entity.enums.IndiceEnum;
import br.com.cashcontroller.repository.IpcaMesRepository;
import br.com.cashcontroller.service.interfaces.ListarIndice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class ListarHistoricoIpca implements ListarIndice {

    @Autowired
    IpcaMesRepository ipcaMesRepository;
    @Override
    public IndiceEnum getIndiceStrategy() {
        return IndiceEnum.IPCA;
    }

    @Override
    public List<IndiceDTO> listarHistoricodoIndice() {
        var ipcas = ipcaMesRepository.findAll();
        List<IndiceDTO> historicoIpca = new ArrayList<IndiceDTO>();
        ipcas.stream().forEach(ipca -> {
            IndiceDTO indiceDTO = new IndiceDTO();
            indiceDTO.setData(ipca.getData());
            indiceDTO.setValor(ipca.getValor());
            indiceDTO.setTipo("IPCA");
            indiceDTO.setId(ipca.getId());
            historicoIpca.add(indiceDTO);
        });

        return historicoIpca;
    }
}
