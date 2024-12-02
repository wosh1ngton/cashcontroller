package br.com.cashcontroller.external.client;

import br.com.cashcontroller.dto.MesDTO;
import br.com.cashcontroller.external.dto.selic.IPCAMesDTO;
import br.com.cashcontroller.external.dto.selic.SelicMesDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Component
public class BacenApiClient {

    private final WebClient webClientBacen;
    @Autowired
    public BacenApiClient(@Qualifier("bcApiClient") WebClient webClientBacen) {
        this.webClientBacen = webClientBacen;
    }

    public Mono<SelicMesDTO[]> getSelicMes() {
        return webClientBacen.get()
                .uri("/dados/serie/bcdata.sgs.4390/dados?formato=json")
                .retrieve()
                .bodyToMono(SelicMesDTO[].class);
    }

    public Mono<SelicMesDTO[]> getSelicDia(String dataInicial, String dataFinal) {
        return webClientBacen.get()
                .uri(uriBuilder -> uriBuilder
                                .path("/dados/serie/bcdata.sgs.11/dados")
                                .queryParam("formato", "json")
                                .queryParam("dataInicial", dataInicial)
                                .queryParam("dataFinal", dataFinal)
                        .build())
                .retrieve()
                .bodyToMono(SelicMesDTO[].class);
    }

    public Mono<IPCAMesDTO[]> getIpcaMes() {
        return webClientBacen.get()
                .uri("/dados/serie/bcdata.sgs.433/dados?formato=json")
                .retrieve()
                .bodyToMono(IPCAMesDTO[].class);
    }

}
