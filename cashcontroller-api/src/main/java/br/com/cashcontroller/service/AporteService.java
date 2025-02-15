package br.com.cashcontroller.service;

import br.com.cashcontroller.dto.AporteDTO;
import br.com.cashcontroller.entity.Aporte;
import br.com.cashcontroller.mapper.AporteMapper;
import br.com.cashcontroller.repository.AporteRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class AporteService {

    @Autowired
    AporteRepository aporteRepository;

    public Aporte cadastrarAporte(Aporte aporte) {
        var aporteSalvo =  aporteRepository.save(aporte);
        return aporteSalvo;
    }

    public List<Aporte> listarAportes() {
        return aporteRepository.findAll()
                .stream().sorted(Comparator.comparing(Aporte::getDataAporte)
                        .reversed())
                .toList();
    }

    public void excluirAporte(Integer id) {
        var entity = aporteRepository.findById(id);
        if(entity.isPresent()) {
            aporteRepository.delete(entity.get());
        } else {
            throw new RuntimeException("aporte.nao.encontrado");
        }

    }

    public AporteDTO editarAporte(AporteDTO aporteDTO) {

        Aporte aporte = aporteRepository.getReferenceById(aporteDTO.getId());
        aporte.setDataAporte(aporteDTO.getDataAporte());
        aporte.setValorAporte(aporteDTO.getValorAporte());
        var aporteSalvo = aporteRepository.save(aporte);
        var aporteDto = AporteMapper.INSTANCE.toDTO(aporteSalvo);

        return aporteDto;
    }
}
