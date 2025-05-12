package br.com.cashcontroller.service.listarindicesstrategy;

import br.com.cashcontroller.dto.IndiceDTO;
import br.com.cashcontroller.entity.enums.IndiceEnum;
import br.com.cashcontroller.service.interfaces.ListarIndice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarHistoricoIndiceService  {

    private final ListarHistoricoIpca strategyIpca;
    private final ListarHistoricoSelic strategySelic;

    public ListarHistoricoIndiceService(ListarHistoricoIpca strategyIpca, ListarHistoricoSelic strategySelic) {
        this.strategyIpca = strategyIpca;
        this.strategySelic = strategySelic;
    }

    public List<IndiceDTO> listarHistorico(String indice) {

        if(indice.equals(IndiceEnum.IPCA.toString())) {
            return strategyIpca.listarHistoricodoIndice();

        } else if(indice.equals(IndiceEnum.SELIC.toString())) {
            return strategySelic.listarHistoricodoIndice();

        } else {
            throw new RuntimeException("estrategia nao encontrada");
        }

    }
}
