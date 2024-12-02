package br.com.cashcontroller.external.service;

import br.com.cashcontroller.entity.SelicMes;
import br.com.cashcontroller.external.client.BacenApiClient;
import br.com.cashcontroller.external.dto.selic.IPCAMesDTO;
import br.com.cashcontroller.external.dto.selic.SelicMesDTO;
import br.com.cashcontroller.repository.SelicMesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.DoubleStream;

@RequiredArgsConstructor
@Service
public class IndicesService {
    private final BacenApiClient bacenApiClient;
    @Autowired
    SelicMesRepository selicMesRepository;

    public SelicMesDTO[] getAllSelicMes() {
        return this.bacenApiClient.getSelicMes().block();
    }
    public SelicMesDTO[] getSelicDia(String dataInicio, String dataFim) {
        return this.bacenApiClient.getSelicDia(dataInicio, dataFim).block();
    }
    public IPCAMesDTO[] getAllIPCAMes() {
        return this.bacenApiClient.getIpcaMes().block();
    }

    public void saveSelicMesAtual() {
        var inicioMes = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataInicioFormatada = formatter.format(inicioMes);
        String dataFimFormatada = formatter.format(LocalDate.now());
        Double accSelicMesAtual = Arrays.stream(getSelicDia(dataInicioFormatada, dataFimFormatada)).mapToDouble(val -> val.getValor()).sum();
        Optional<SelicMes> selicMes = selicMesRepository.findByData(inicioMes);
        if(selicMes.isPresent()) {
            selicMes.get().setValor(accSelicMesAtual);
            selicMesRepository.save(selicMes.get());
        } else {
            SelicMes novaSelic = new SelicMes();
            novaSelic.setValor(accSelicMesAtual);
            novaSelic.setData(inicioMes);
            selicMesRepository.save(novaSelic);
        }
    }

}
