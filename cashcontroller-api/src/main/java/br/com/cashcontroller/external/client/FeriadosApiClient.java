package br.com.cashcontroller.external.client;

import br.com.cashcontroller.external.dto.FeriadoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class FeriadosApiClient {
    private final WebClient webClient;

    @Autowired
    public FeriadosApiClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<FeriadoDTO[]> getFeriados(int ano) {
        return webClient.get()
                .uri("/api/feriados/v1/{ano}", ano)
                .retrieve()
                .bodyToMono(FeriadoDTO[].class);
    }
}
