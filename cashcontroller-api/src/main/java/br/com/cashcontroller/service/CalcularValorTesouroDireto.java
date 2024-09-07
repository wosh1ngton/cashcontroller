package br.com.cashcontroller.service;

import br.com.cashcontroller.dto.AtivoCarteiraDTO;
import br.com.cashcontroller.dto.AtivoCarteiraRFDTO;
import br.com.cashcontroller.external.dto.tesouro.TituloTesouroDTO;

import java.util.List;
import java.util.Optional;

public final class CalcularValorTesouroDireto {

    public static void calcularValorDeMercadoTesouroDireto(AtivoCarteiraDTO ativoCarteiraRFDTO, List<TituloTesouroDTO> titulos) {
        Optional<TituloTesouroDTO> titulo = titulos.stream()
                .filter(val -> val.getNm().equals(ativoCarteiraRFDTO.getAtivo().getNome()))
                .findFirst();
        titulo.ifPresent(tituloTesouroDTO -> {
            ativoCarteiraRFDTO.setCotacao(tituloTesouroDTO.getUntrRedVal());
            ativoCarteiraRFDTO.setValorMercado(tituloTesouroDTO.getUntrRedVal() * ativoCarteiraRFDTO.getCustodia());

        });
    }
}
