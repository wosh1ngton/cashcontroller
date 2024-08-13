package br.com.cashcontroller.external.client;

import br.com.cashcontroller.dto.MesDTO;
import br.com.cashcontroller.external.dto.FeriadoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class FeriadosApiClient {
    private final WebClient webClientBrasilApi;

    @Autowired
    public FeriadosApiClient(@Qualifier("brasilApiClient") WebClient webClientBrasilApi) {
        this.webClientBrasilApi = webClientBrasilApi;
    }


    public Mono<FeriadoDTO[]> getFeriados(int ano) {
        return webClientBrasilApi.get()
                .uri("/api/feriados/v1/{ano}", ano)
                .retrieve()
                .bodyToMono(FeriadoDTO[].class);
    }



}
