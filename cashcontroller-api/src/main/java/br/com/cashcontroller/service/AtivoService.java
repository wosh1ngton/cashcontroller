package br.com.cashcontroller.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.cashcontroller.dto.AtivoAddDTO;
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

	public AtivoAddDTO cadastrarAtivo(AtivoAddDTO ativoAddDto) {

		Ativo ativo = AtivoMapper.INSTANCE.toAddEntity(ativoAddDto);
		if (ativo.getParametroRendaFixa() != null) {
			ativo.getParametroRendaFixa().setAtivo(ativo);
		}
		var entitySaved = ativoRepository.saveAndFlush(ativo);
		return AtivoMapper.INSTANCE.toAddDTO(entitySaved);
	}
	
	public AtivoAddDTO atualizarAtivo(AtivoAddDTO ativoAddDto) {
		Ativo ativo = new Ativo();
		if(ativoAddDto.getId() != 0) {
			ativo = AtivoMapper.INSTANCE.toAddEntity(ativoAddDto);
		}
		return AtivoMapper.INSTANCE.toAddDTO(ativoRepository.save(ativo));
	}

	public void excluirAtivo(int id) {

		Optional<Ativo> ativo = ativoRepository.findById(id);
        ativo.ifPresent(value -> this.ativoRepository.delete(value));

	}

	public AtivoDTO findById(int id) {

		Optional<Ativo> ativo = ativoRepository.findById(id);
		return AtivoMapper.INSTANCE.toDTO(ativo.get());

	}
	
	public List<AtivoDTO> listarAtivos() {
		List<Ativo> ativos =  ativoRepository.findAll();
		return ativos.stream().map(AtivoMapper.INSTANCE::toDTO).collect(Collectors.toList());
	}

	public List<AtivoDTO> listarAtivosPorClasse(int id) {
		List<Ativo> ativos =  ativoRepository.findByClasseAtivo(id);
		return ativos.stream().map(AtivoMapper.INSTANCE::toDTO).collect(Collectors.toList());
	}

	public List<AtivoDTO> listarAtivosPorSubClasse(int id) {
		List<Ativo> ativos =  ativoRepository.findBySubClasseAtivo(id);
		return ativos.stream().map(AtivoMapper.INSTANCE::toDTO).collect(Collectors.toList());
	}
	
	public List<SubclasseAtivoDTO> listarSubclasseAtivos() {
		List<SubclasseAtivo> subclasseAtivos =  subclasseRepository.findAll();
		return SubclasseAtivoMapper.INSTANCE.toListDTO(subclasseAtivos);
	}
	
}
