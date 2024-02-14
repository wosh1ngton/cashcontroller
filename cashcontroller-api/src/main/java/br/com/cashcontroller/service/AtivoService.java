package br.com.cashcontroller.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cashcontroller.dto.AtivoDTO;
import br.com.cashcontroller.dto.SubclasseAtivoDTO;
import br.com.cashcontroller.entity.Ativo;
import br.com.cashcontroller.entity.SubclasseAtivo;
import br.com.cashcontroller.mapper.AtivoMapper;
import br.com.cashcontroller.mapper.SubclasseAtivoMapper;
import br.com.cashcontroller.repository.AtivoRepository;
import br.com.cashcontroller.repository.SubclasseRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AtivoService {

	@Autowired
	private AtivoRepository ativoRepository;
	
	@Autowired
	private SubclasseRepository subclasseRepository;	

	public AtivoDTO cadastrarAtivo(AtivoDTO ativoDto) {		
		Ativo ativo = AtivoMapper.INSTANCE.toEntity(ativoDto);
		return AtivoMapper.INSTANCE.toDTO(ativoRepository.save(ativo));
	}
	
	public AtivoDTO atualizarAtivo(AtivoDTO ativoDto) {
		Ativo ativo = new Ativo();
		if(ativoDto.getId() != 0) {
			ativo = AtivoMapper.INSTANCE.toEntity(ativoDto);
		}
		return AtivoMapper.INSTANCE.toDTO(ativoRepository.save(ativo));
	}

	public void excluirAtivo(int id) {

		Optional<Ativo> ativo = ativoRepository.findById(id);
        ativo.ifPresent(value -> this.ativoRepository.delete(value));

	}
	
	public List<AtivoDTO> listarAtivos() {
		List<Ativo> ativos =  ativoRepository.findAll();
		return AtivoMapper.INSTANCE.toListDTO(ativos);
	}
	
	public List<SubclasseAtivoDTO> listarSubclasseAtivos() {
		List<SubclasseAtivo> subclasseAtivos =  subclasseRepository.findAll();
		return SubclasseAtivoMapper.INSTANCE.toListDTO(subclasseAtivos);
	}
	
}
