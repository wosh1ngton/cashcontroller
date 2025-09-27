package br.com.cashcontroller.service;

import br.com.cashcontroller.entity.IndiceMesBase;
import br.com.cashcontroller.entity.IpcaMes;
import br.com.cashcontroller.entity.SelicMes;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IndiceMesFacadeService {
    @Autowired
    private IpcaMesService ipcaService;

    @Autowired
    private SelicMesService selicService;

    public IndiceMesBase save(String tipo, IndiceMesBase dto) {
        switch (tipo.toLowerCase()) {
            case "ipca":
                return ipcaService.save((IpcaMes) dto);
            case "selic":
                return selicService.save((SelicMes) dto);
            default:
                throw new RuntimeException("Tipo desconhecido: " + tipo);
        }
    }

    @SneakyThrows
    public IndiceMesBase update(String tipo, int id, IndiceMesBase dto) {
        switch (tipo.toLowerCase()) {
            case "ipca":
                return ipcaService.update(id, (IpcaMes) dto);
            case "selic":
                return selicService.update(id, (SelicMes) dto);
            default:
                throw new IllegalAccessException("Tipo desconhecido" + tipo);
        }
    }
}
