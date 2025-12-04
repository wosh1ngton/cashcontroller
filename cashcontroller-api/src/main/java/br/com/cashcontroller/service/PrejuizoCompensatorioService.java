package br.com.cashcontroller.service;


import br.com.cashcontroller.dto.IrpfMesDTO;
import br.com.cashcontroller.dto.PrejuizoAcumuladoDTO;
import br.com.cashcontroller.dto.enums.SubclasseAtivoEnum;
import br.com.cashcontroller.entity.PrejuizoAcumulado;
import br.com.cashcontroller.mapper.PrejuizoAcumuladoMapper;
import br.com.cashcontroller.repository.PrejuizoAcumuladoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
public class PrejuizoCompensatorioService {

    @Autowired
    PrejuizoAcumuladoRepository repository;

    public Double getPrejuizoMesAnterior(String anoMes, Integer subclasseAtivoId) {
        return repository.findPrejuizoAcumulado(anoMes, subclasseAtivoId);
    }

    public PrejuizoAcumulado getPrejuizoByAnoMesCategoria(String anoMes, Integer subclasseAtivoId) {
        return repository.findByAnoMesAndSubclasseAtivoId(anoMes, subclasseAtivoId);
    }

    public void atualizaPrejuizoAcumulado(String anoMesAnterior, String anoMesAtual, Integer subclasseAtivoId,
                                          Double resultadoMes, Double vendasMes) {



        Double valorPrejuizoAcumulado = repository.findPrejuizoAcumulado(anoMesAnterior, subclasseAtivoId);
        PrejuizoAcumuladoDTO prejuizoAcumuladoDTO = new PrejuizoAcumuladoDTO();

        setPrejuizoAcumulado(resultadoMes, prejuizoAcumuladoDTO, valorPrejuizoAcumulado, vendasMes, subclasseAtivoId);
        prejuizoAcumuladoDTO.setSubclasseAtivoId(subclasseAtivoId);
        prejuizoAcumuladoDTO.setAnoMes(anoMesAtual);

        prejuizoAcumuladoDTO.setLucroMes(resultadoMes);
        getLucroTributavel(resultadoMes, vendasMes, prejuizoAcumuladoDTO, subclasseAtivoId);
        prejuizoAcumuladoDTO.setVendasMes(vendasMes);

        var prejuizoExistente = getPrejuizoByAnoMesCategoria(anoMesAtual, subclasseAtivoId);
        if(prejuizoExistente != null) {
            repository.delete(prejuizoExistente);
        };
        PrejuizoAcumulado prejuizoAcumulado = PrejuizoAcumuladoMapper.INSTANCE.toEntity(prejuizoAcumuladoDTO);
        repository.save(prejuizoAcumulado);


    }

    private static void setPrejuizoAcumulado(Double resultadoMes, PrejuizoAcumuladoDTO prejuizoAcumuladoDTO, Double valorPrejuizoAcumulado, Double vendas, Integer subclasseAtivoId) {
        if(resultadoMes < 0) {
            prejuizoAcumuladoDTO.setPrejuizoAcumulado(valorPrejuizoAcumulado + Math.abs(resultadoMes));
        } else if((resultadoMes > 0 && vendas > 20000 && subclasseAtivoId.equals(SubclasseAtivoEnum.ACAO.getId())) || resultadoMes > 0 && subclasseAtivoId.equals(SubclasseAtivoEnum.FII.getId())) {
            prejuizoAcumuladoDTO.setPrejuizoAcumulado(valorPrejuizoAcumulado - resultadoMes);
        } else {
            prejuizoAcumuladoDTO.setPrejuizoAcumulado(valorPrejuizoAcumulado);
        }
    }






    private static void getLucroTributavel(Double resultadoMes, Double vendasMes, PrejuizoAcumuladoDTO prejuizoAcumuladoDTO, Integer subclasseAtivoId) {
        if((vendasMes > 20000 && resultadoMes > 0 && subclasseAtivoId == SubclasseAtivoEnum.ACAO.getId()) ||
            resultadoMes > 0 && subclasseAtivoId == SubclasseAtivoEnum.FII.getId()) {
            prejuizoAcumuladoDTO.setLucroTributavel(resultadoMes);
        } else {
            prejuizoAcumuladoDTO.setLucroTributavel(0d);
        }
    }


}
