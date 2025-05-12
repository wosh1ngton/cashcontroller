package br.com.cashcontroller.controller;

import br.com.cashcontroller.dto.AtivoCarteiraDTO;
import br.com.cashcontroller.external.dto.stock.BrapiDTO;
import br.com.cashcontroller.external.service.RendaVariavelService;
import br.com.cashcontroller.service.AtivoCarteiraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cotacao")
public class CotacaoController {

    @Autowired
    AtivoCarteiraService ativoCarteiraService;

    @GetMapping("/meus-fiis")
    public List<AtivoCarteiraDTO> getCotacaoAtualMeusFiis() {
        return this.ativoCarteiraService.getCotacaoMeusFiis();
    }

    @GetMapping("/minhas-acoes")
    public List<AtivoCarteiraDTO> getCotacaoAtualMinhasAcoes() {
        return this.ativoCarteiraService.getCotacaoMinhasAcoes();
    }
}
