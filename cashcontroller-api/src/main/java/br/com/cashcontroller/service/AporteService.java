package br.com.cashcontroller.service;

import br.com.cashcontroller.dto.AporteDTO;
import br.com.cashcontroller.entity.Aporte;
import br.com.cashcontroller.mapper.AporteMapper;
import br.com.cashcontroller.repository.AporteRepository;
import br.com.cashcontroller.security.SecurityUtils;
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
        aporte.setUser(SecurityUtils.getCurrentUser());
        var aporteSalvo =  aporteRepository.save(aporte);
        return aporteSalvo;
    }

    public List<Aporte> listarAportes() {
        return aporteRepository.findAllByUser(SecurityUtils.getCurrentUserId())
                .stream().sorted(Comparator.comparing(Aporte::getDataAporte)
                        .reversed())
                .toList();
    }

    public void excluirAporte(Integer id) {
        var entity = aporteRepository.findByIdAndUser(id, SecurityUtils.getCurrentUserId());
        if(entity.isPresent()) {
            aporteRepository.delete(entity.get());
        } else {
            throw new RuntimeException("aporte.nao.encontrado");
        }

    }

    public AporteDTO editarAporte(AporteDTO aporteDTO) {

        Aporte aporte = aporteRepository.findByIdAndUser(aporteDTO.getId(), SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new RuntimeException("aporte.nao.encontrado"));
        aporte.setDataAporte(aporteDTO.getDataAporte());
        aporte.setValorAporte(aporteDTO.getValorAporte());
        var aporteSalvo = aporteRepository.save(aporte);
        var aporteDto = AporteMapper.INSTANCE.toDTO(aporteSalvo);

        return aporteDto;
    }
}
