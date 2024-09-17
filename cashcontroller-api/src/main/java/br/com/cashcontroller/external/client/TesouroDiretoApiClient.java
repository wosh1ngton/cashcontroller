package br.com.cashcontroller.external.client;

import br.com.cashcontroller.external.dto.tesouro.TesouroDiretoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.SocketException;

@Component
public class TesouroDiretoApiClient {

    private final WebClient webClientTesouro;
    @Autowired
    public TesouroDiretoApiClient(@Qualifier("tesouroApiClient") WebClient webClientTesouro) {
        this.webClientTesouro = webClientTesouro;
    }

    @Retryable(
            value = { SocketException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public TesouroDiretoDTO getTitulosTesouroDireto() {
        return webClientTesouro.get()
                .uri("/json/br/com/b3/tesourodireto/service/api/treasurybondsinfo.json")
                .retrieve()
                .bodyToMono(TesouroDiretoDTO.class)
                .block();
    }

    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            System.out.println("Request: " + clientRequest.method() + " " + clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> System.out.println(name + ": " + value)));
            return Mono.just(clientRequest);
        });
    }

    private static ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            clientResponse.headers().asHttpHeaders().forEach((name, values) -> values.forEach(value -> System.out.println(name + ": " + value)));
            return Mono.just(clientResponse);
        });
    }

}
