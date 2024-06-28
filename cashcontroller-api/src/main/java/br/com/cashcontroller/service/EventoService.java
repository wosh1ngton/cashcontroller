package br.com.cashcontroller.service;

import br.com.cashcontroller.dto.EventoAddRendaVariavelDTO;
import br.com.cashcontroller.dto.EventoListRendaVariavelDTO;
import br.com.cashcontroller.dto.TipoEventoDTO;
import br.com.cashcontroller.entity.EventoRendaVariavel;
import br.com.cashcontroller.entity.TipoEvento;
import br.com.cashcontroller.mapper.EventoRendaVariavelMapper;
import br.com.cashcontroller.repository.EventoRepository;
import br.com.cashcontroller.repository.TipoEventoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventoService {

	@Autowired
	private EventoRepository eventoRepository;


	@Autowired
	private TipoEventoRepository tipoEventoRepository;
	public EventoAddRendaVariavelDTO cadastrarEvento(EventoAddRendaVariavelDTO eventoDTO) {
		EventoRendaVariavel evento = EventoRendaVariavelMapper.INSTANCE.toEntity(eventoDTO);
		return EventoRendaVariavelMapper.INSTANCE.toDTO(eventoRepository.save(evento));
	}
	
//	public AtivoDTO atualizarAtivo(AtivoDTO ativoDto) {
//		Ativo ativo = new Ativo();
//		if(ativoDto.getId() != 0) {
//			ativo = AtivoMapper.INSTANCE.toEntity(ativoDto);
//		}
//		return AtivoMapper.INSTANCE.toDTO(ativoRepository.save(ativo));
//	}
//
//	public void excluirAtivo(int id) {
//
//		Optional<Ativo> ativo = ativoRepository.findById(id);
//        ativo.ifPresent(value -> this.ativoRepository.delete(value));
//
//	}
//
	public List<EventoListRendaVariavelDTO> listarEventos() {
		List<EventoRendaVariavel> eventos =  eventoRepository.findAll();
		var eventosDTO = eventos.stream().map(EventoRendaVariavelMapper.INSTANCE::toListDTO).collect(Collectors.toList());
		return eventosDTO;
	}
//
//	public List<AtivoDTO> listarAtivosPorClasse(int id) {
//		List<Ativo> ativos =  ativoRepository.findByClasseAtivo(id);
//		return AtivoMapper.INSTANCE.toListDTO(ativos);
//	}
//
	public List<TipoEventoDTO> listarTipoEventos() {
		List<TipoEvento> eventos =  tipoEventoRepository.findAll();
		var eventosDTO = eventos.stream().map(evento -> new TipoEventoDTO(evento.getId(),evento.getNome())).collect(Collectors.toList());
		return eventosDTO;
	}
	
}
