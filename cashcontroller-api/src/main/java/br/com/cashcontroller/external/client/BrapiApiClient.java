package br.com.cashcontroller.external.client;

import br.com.cashcontroller.external.dto.stock.BrapiDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class BrapiApiClient {

    @Value("${B_TOKEN}")
    private String TOKEN_BRAPI;

    private final WebClient webClientBrapi;
    @Autowired
    public BrapiApiClient(@Qualifier("brapiClient") WebClient webClientBrapi) {
        this.webClientBrapi = webClientBrapi;
    }

    public Mono<BrapiDTO> getStocksBrapi() {
        return webClientBrapi.get()
                .uri("/quote/list?type=stock&token="+TOKEN_BRAPI)
                .retrieve()
                .bodyToMono(BrapiDTO.class)
                .doOnNext(body -> log.info("Response: {}", body));
    }


    public Mono<BrapiDTO> getFiisBrapi() {
        return webClientBrapi.get()
                .uri("/quote/list?type=fund&token="+TOKEN_BRAPI)
                .retrieve()
                .bodyToMono(BrapiDTO.class);
    }

    public Mono<BrapiDTO> getIIVB11() {
        return webClientBrapi.get()
                .uri("/quote/list?search=IVVB11&token="+TOKEN_BRAPI)
                .retrieve()
                .bodyToMono(BrapiDTO.class);
    }


    public String getIbov() {
        return  webClientBrapi.get()
                .uri("/quote/^BVSP?token=" + TOKEN_BRAPI)
                .retrieve()
                .bodyToMono(String.class).block();
    }





}
