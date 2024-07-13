package br.com.cashcontroller.service;

import br.com.cashcontroller.dto.*;
import br.com.cashcontroller.entity.EventoRendaVariavel;
import br.com.cashcontroller.entity.ParametroEventoFII;
import br.com.cashcontroller.entity.TipoEvento;
import br.com.cashcontroller.external.dto.FeriadoDTO;
import br.com.cashcontroller.external.service.FeriadosService;
import br.com.cashcontroller.mapper.EventoRendaVariavelMapper;
import br.com.cashcontroller.mapper.ParametroEventoFIIMapper;
import br.com.cashcontroller.repository.EventoRepository;
import br.com.cashcontroller.repository.OperacaoRendaVariavelRepository;
import br.com.cashcontroller.repository.ParametroEventoFIIRepository;
import br.com.cashcontroller.repository.TipoEventoRepository;
import br.com.cashcontroller.utils.DataUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventoService {
	@Autowired
	private EventoRepository eventoRepository;
	@Autowired
	private FeriadosService feriadosService;
	@Autowired
	private ParametroEventoFIIRepository parametroEventoFIIRepository;
	@Autowired
	private OperacaoRendaVariavelRepository operacaoRendaVariavelRepository;
	@Autowired
	private TipoEventoRepository tipoEventoRepository;
	public void cadastrarEvento(EventoAddRendaVariavelDTO eventoDTO, String periodosDeRecorrencia) {

		if(Objects.equals(periodosDeRecorrencia, "0")) {
			EventoRendaVariavel evento = EventoRendaVariavelMapper.INSTANCE.toEntity(eventoDTO);
			eventoRepository.save(evento);
		} else {
			cadastrarBlocoDeEventos(eventoDTO, periodosDeRecorrencia);
		}

	}

	private void cadastrarBlocoDeEventos(EventoAddRendaVariavelDTO eventoDTO, String periodosDeRecorrencia) {

		var params = getParametrosFIIPorAtivo(eventoDTO.getAtivo());
		var mesEvento = eventoDTO.getDataPagamento();
		var feriados = this.feriadosService.getFeriados(mesEvento.getYear());

		List<EventoAddRendaVariavelDTO> eventosRecorrentes = new ArrayList<>();

		for (int i = 0; i < Integer.valueOf(periodosDeRecorrencia) ; i++) {

			EventoAddRendaVariavelDTO eventoDtoAdicional = new EventoAddRendaVariavelDTO();
			eventoDtoAdicional.setAtivo(eventoDTO.getAtivo());
			eventoDtoAdicional.setTipoEvento(eventoDTO.getTipoEvento());
			eventoDtoAdicional.setValor(eventoDTO.getValor());
			var anoTemp = mesEvento.getYear();

			var primeiroDiaUtilMes = DataUtil.getPrimeiroDiaUtildoMes(mesEvento.getYear(),mesEvento.getMonthValue(), feriados);
			mesEvento = mesEvento.plusMonths(1);

			if(anoTemp != mesEvento.getYear()) {
				feriados = this.feriadosService.getFeriados(mesEvento.getYear());
			}
			eventoDtoAdicional.setDataCom(DataUtil.getDataComFII(primeiroDiaUtilMes, params.getDiaUtilDtCom(), feriados));
			eventoDtoAdicional.setDataPagamento(DataUtil.getDataPagamentoFII(primeiroDiaUtilMes, params.getDiaUtilDtPagamento(), feriados));
			eventosRecorrentes.add(eventoDtoAdicional);
		}
		var eventos = eventosRecorrentes.stream().map(EventoRendaVariavelMapper.INSTANCE::toEntity).collect(Collectors.toList());
		eventoRepository.saveAll(eventos);
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

	public double getTotalProventosPorAtivo(int ativoId) {
		var eventos = eventoRepository.findEventosByAtivo(ativoId);
		var eventosDto = eventos.stream().map(EventoRendaVariavelMapper.INSTANCE::toListDTO).toList();
		return eventosDto.stream()
				.mapToDouble(this::getValorTotalEvento)
				.sum();
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

	public ParametroEventoFIIAddDTO cadastrarParametro(ParametroEventoFIIAddDTO parametroDto) {

		ParametroEventoFII parametro = ParametroEventoFIIMapper.INSTANCE.toEntity(parametroDto);
		return ParametroEventoFIIMapper.INSTANCE.toDTO(parametroEventoFIIRepository.save(parametro));
	}

	private ParametroEventoFII getParametrosFIIPorAtivo(int idAtivo) {
		var parametros = parametroEventoFIIRepository.getParametrosFiiPorAtivo(idAtivo);
		return parametros;
	}

}
