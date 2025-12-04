package br.com.cashcontroller.controller;

import br.com.cashcontroller.exception.PrejuizoNaoDeclaradoException;
import br.com.cashcontroller.exception.SenhaInvalidaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SenhaInvalidaException.class)
    public ResponseEntity<String> handleSenhaInvalida(SenhaInvalidaException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(PrejuizoNaoDeclaradoException.class)
    public ResponseEntity<String> handlePrejuizoException(WebClientResponseException ex) {
        return ResponseEntity.status(ex.getRawStatusCode())
                .body("Prejuízo não declarado para esse mês: " + ex.getResponseBodyAsString());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        log.error("Erro inesperado", ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro inesperado: " + ex.getMessage());

    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleWebClientResponse(WebClientResponseException ex) {
        return ResponseEntity.status(ex.getRawStatusCode())
                .body("Erro na chamada ao serviço externo: " + ex.getResponseBodyAsString());
    }


}
