package br.com.cashcontroller.service;

import br.com.cashcontroller.dto.*;
import br.com.cashcontroller.entity.EventoRendaVariavel;
import br.com.cashcontroller.entity.OperacaoRendaVariavel;
import br.com.cashcontroller.entity.TipoEvento;
import br.com.cashcontroller.mapper.EventoRendaVariavelMapper;
import br.com.cashcontroller.mapper.OperacaoRendaVariavelMapper;
import br.com.cashcontroller.repository.EventoRepository;
import br.com.cashcontroller.repository.OperacaoRendaVariavelRepository;
import br.com.cashcontroller.repository.TipoEventoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventoService {

	@Autowired
	private EventoRepository eventoRepository;

	@Autowired
	private OperacaoRendaVariavelRepository operacaoRendaVariavelRepository;


	@Autowired
	private TipoEventoRepository tipoEventoRepository;
	public EventoAddRendaVariavelDTO cadastrarEvento(EventoAddRendaVariavelDTO eventoDTO) {
		EventoRendaVariavel evento = EventoRendaVariavelMapper.INSTANCE.toEntity(eventoDTO);
		return EventoRendaVariavelMapper.INSTANCE.toDTO(eventoRepository.save(evento));
	}

	private long getCustodiaPorData(int idAtivo, LocalDate dataCom) {
		var custodia = this.operacaoRendaVariavelRepository.getCustodiaPorAtivo(idAtivo, dataCom);
		return  custodia;
	}
	
	public EventoAddRendaVariavelDTO atualizarEvento(EventoAddRendaVariavelDTO eventoDTO) {
		EventoRendaVariavel evento = new EventoRendaVariavel();
		if(eventoDTO.getId() != 0) {
			evento = EventoRendaVariavelMapper.INSTANCE.toEntity(eventoDTO);
		}
		return EventoRendaVariavelMapper.INSTANCE.toDTO(eventoRepository.save(evento));
	}

	public void excluirEvento(int id) {

		Optional<EventoRendaVariavel> evento = eventoRepository.findById(id);
		evento.ifPresent(value -> this.eventoRepository.delete(value));

	}

	public List<EventoListRendaVariavelDTO> listarEventos() {
		List<EventoRendaVariavel> eventos =  eventoRepository.findAll();
		var eventosDTO = eventos.stream().map(EventoRendaVariavelMapper.INSTANCE::toListDTO).collect(Collectors.toList());
		return eventosDTO;
	}

	public List<TipoEventoDTO> listarTipoEventos() {
		List<TipoEvento> eventos =  tipoEventoRepository.findAll();
		var eventosDTO = eventos.stream().map(evento -> new TipoEventoDTO(evento.getId(),evento.getNome())).collect(Collectors.toList());
		return eventosDTO;
	}

	public List<EventoListRendaVariavelDTO> listarEventosRendaVariavelPorData(Filter filter) {
		List<EventoRendaVariavel> eventos = eventoRepository.findEventosByData(filter.getStartDate(), filter.getEndDate(), filter.getSubclasse(), filter.getAno(), filter.getMes());

		var eventosDto = eventos.stream().map(EventoRendaVariavelMapper.INSTANCE::toListDTO).toList();
		eventosDto.stream().peek(eventoDto -> eventoDto.setValorTotal(
				getValorTotalEvento(eventoDto)
		)).collect(Collectors.toList());

		return eventosDto;

	}

	private double getValorTotalEvento(EventoListRendaVariavelDTO eventoDto) {
		var totalBruto = this.getCustodiaPorData(eventoDto.getAtivo().getId(), eventoDto.getDataCom()) * eventoDto.getValor();
		if(eventoDto.getTipoEvento().getId() == 2) {
			var totalLiquido = totalBruto - totalBruto * 0.15;
			return totalLiquido;
		} else {
			return totalBruto;
		}
	}

}
