package br.com.cashcontroller.service;

import br.com.cashcontroller.dto.IndexadorDTO;
import br.com.cashcontroller.mapper.IndexadorMapper;
import br.com.cashcontroller.repository.IndexadorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class IndexadorService {

    @Autowired
    IndexadorRepository repository;

    public List<IndexadorDTO> listarIndexadores() {
        return IndexadorMapper.INSTANCE.toListDTO(repository.findAll());
    }
}
