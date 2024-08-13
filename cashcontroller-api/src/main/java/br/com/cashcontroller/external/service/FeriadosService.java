package br.com.cashcontroller.external.service;

import br.com.cashcontroller.external.client.FeriadosApiClient;
import br.com.cashcontroller.external.dto.FeriadoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FeriadosService {

    private final FeriadosApiClient feriadosApiClient;

    @Autowired
    public FeriadosService(FeriadosApiClient feriadosApiClient) {
        this.feriadosApiClient = feriadosApiClient;
    }

    public FeriadoDTO[] getFeriados(int ano) {
        return feriadosApiClient.getFeriados(ano).block();
    }

    public List<FeriadoDTO> getFeriadosPorIntervalo(LocalDate fim) {
        LocalDate inicio = LocalDate.of(2015,1,1);
        List<FeriadoDTO> feriados = new ArrayList<>();
        for (int i = inicio.getYear(); i <= fim.getYear(); i++) {
            FeriadoDTO[] feriadosAno = getFeriados(i);
            feriados.addAll(Arrays.asList(feriadosAno));
        }
        return  feriados;

    }
}
