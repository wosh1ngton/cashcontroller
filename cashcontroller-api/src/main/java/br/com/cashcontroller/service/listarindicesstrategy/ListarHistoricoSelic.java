package br.com.cashcontroller.service.listarindicesstrategy;

import br.com.cashcontroller.dto.IndiceDTO;
import br.com.cashcontroller.entity.enums.IndiceEnum;
import br.com.cashcontroller.repository.IpcaMesRepository;
import br.com.cashcontroller.repository.SelicMesRepository;
import br.com.cashcontroller.service.interfaces.ListarIndice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class ListarHistoricoSelic implements ListarIndice {

    @Autowired
    SelicMesRepository selicMesRepository;

    @Override
    public IndiceEnum getIndiceStrategy() {
        return IndiceEnum.SELIC;
    }

    @Override
    public List<IndiceDTO> listarHistoricodoIndice() {
        var ipcas = selicMesRepository.findAll();
        List<IndiceDTO> historicoIpca = new ArrayList<IndiceDTO>();
        ipcas.forEach(ipca -> {
            IndiceDTO indiceDTO = new IndiceDTO();
            indiceDTO.setData(ipca.getData());
            indiceDTO.setValor(ipca.getValor());
            indiceDTO.setTipo("SELIC");
            indiceDTO.setId(ipca.getId());
            historicoIpca.add(indiceDTO);
        });

        return historicoIpca;
    }
}
