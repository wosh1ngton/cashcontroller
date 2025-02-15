package br.com.cashcontroller.external.service;

import br.com.cashcontroller.external.client.TesouroDiretoApiClient;
import br.com.cashcontroller.external.dto.tesouro.TesouroDiretoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TesouroService {

    private final TesouroDiretoApiClient tesouroDiretoApiClient;

    @Autowired
    public TesouroService(TesouroDiretoApiClient tesouroDiretoApiClient) {
        this.tesouroDiretoApiClient = tesouroDiretoApiClient;
    }

    public TesouroDiretoDTO getTitulos() {
        return tesouroDiretoApiClient.getTitulosTesouroDireto2().block();
    }
}
