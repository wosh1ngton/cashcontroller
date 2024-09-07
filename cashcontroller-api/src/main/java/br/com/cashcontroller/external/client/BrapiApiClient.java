package br.com.cashcontroller.external.client;

import br.com.cashcontroller.external.dto.stock.BrapiDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class BrapiApiClient {

    final String TOKEN_BRAPI = "hQTbQHiTVUDV9ohDHfCErU";

    private final WebClient webClientBrapi;
    @Autowired
    public BrapiApiClient(@Qualifier("brapiClient") WebClient webClientBrapi) {
        this.webClientBrapi = webClientBrapi;
    }

    public Mono<BrapiDTO> getStocksBrapi() {
        return webClientBrapi.get()
                .uri("/quote/list?type=stock&token="+TOKEN_BRAPI)
                .retrieve()
                .bodyToMono(BrapiDTO.class);
    }


    public Mono<BrapiDTO> getFiisBrapi() {
        return webClientBrapi.get()
                .uri("/quote/list?type=fund&token="+TOKEN_BRAPI)
                .retrieve()
                .bodyToMono(BrapiDTO.class);
    }





}
