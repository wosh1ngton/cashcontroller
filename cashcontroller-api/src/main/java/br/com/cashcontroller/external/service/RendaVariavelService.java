package br.com.cashcontroller.external.service;

import br.com.cashcontroller.external.client.BrapiApiClient;
import br.com.cashcontroller.external.dto.stock.BrapiDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RendaVariavelService {

    private final BrapiApiClient brapiApiClient;

    @Autowired
    public RendaVariavelService(BrapiApiClient brapiApiClient) {
        this.brapiApiClient = brapiApiClient;
    }

    public BrapiDTO getStocksBrapi() {
        var brapiDTO =brapiApiClient.getStocksBrapi().block();
        return brapiDTO;
    }

    public BrapiDTO getFiisBrapi() {
        var brapiDTO =brapiApiClient.getFiisBrapi().block();
        return brapiDTO;
    }
}
