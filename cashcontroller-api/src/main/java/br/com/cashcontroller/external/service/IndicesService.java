package br.com.cashcontroller.external.service;

import br.com.cashcontroller.external.client.BacenApiClient;
import br.com.cashcontroller.external.dto.selic.IPCAMesDTO;
import br.com.cashcontroller.external.dto.selic.SelicMesDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class IndicesService {
    private final BacenApiClient bacenApiClient;

    public SelicMesDTO[] getAllSelicMes() {
        return this.bacenApiClient.getSelicMes().block();
    }
    public IPCAMesDTO[] getAllIPCAMes() {
        return this.bacenApiClient.getIpcaMes().block();
    }

}
