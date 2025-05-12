package br.com.cashcontroller.service.interfaces;

import br.com.cashcontroller.dto.IndiceDTO;
import br.com.cashcontroller.entity.enums.IndiceEnum;

import java.util.List;

public interface ListarIndice {

    IndiceEnum getIndiceStrategy();
    List<IndiceDTO> listarHistoricodoIndice();
}
