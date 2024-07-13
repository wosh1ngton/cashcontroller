package br.com.cashcontroller.external.service;

import br.com.cashcontroller.external.client.FeriadosApiClient;
import br.com.cashcontroller.external.dto.FeriadoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeriadosService {

    private final FeriadosApiClient feriadosApiClient;

    @Autowired
    public FeriadosService(FeriadosApiClient feriadosApiClient) {
        this.feriadosApiClient = feriadosApiClient;
    }

    public FeriadoDTO[] getFeriados(int ano) {
        return feriadosApiClient.getFeriados(ano).block();
    }
}
