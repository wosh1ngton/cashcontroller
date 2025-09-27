package br.com.cashcontroller.external.service;

import br.com.cashcontroller.external.client.BrapiApiClient;
import br.com.cashcontroller.external.dto.stock.BrapiDTO;
import br.com.cashcontroller.service.AtivoCarteiraService;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.JSONArray;
import org.json.JSONObject;
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

    public BrapiDTO getIVVB11() {
        var brapiDTO =brapiApiClient.getFiisBrapi().block();
        return brapiDTO;
    }




    public String getIbov() {
        var body = brapiApiClient.getIbov();
        JSONObject jsonObject = new JSONObject(body);
        JSONArray resultsArray = jsonObject.getJSONArray("results");
        JSONObject firstResult = resultsArray.getJSONObject(0);
        double regularMarketPrice = firstResult.getDouble("regularMarketPrice");
        return String.valueOf(regularMarketPrice);
    }
}
