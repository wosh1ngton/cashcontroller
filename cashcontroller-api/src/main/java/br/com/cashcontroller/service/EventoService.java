package br.com.cashcontroller.service;

import br.com.cashcontroller.dto.*;
import br.com.cashcontroller.dto.enums.TipoEventoEnum;
import br.com.cashcontroller.entity.*;
import br.com.cashcontroller.external.service.FeriadosService;
import br.com.cashcontroller.mapper.EventoRendaFixaMapper;
import br.com.cashcontroller.mapper.EventoRendaVariavelMapper;
import br.com.cashcontroller.mapper.ParametroEventoFIIMapper;
import br.com.cashcontroller.repository.*;
import br.com.cashcontroller.service.util.CalculaImpostoService;
import br.com.cashcontroller.utils.DataUtil;
import br.com.cashcontroller.utils.Taxa;
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
	private CalculaImpostoService calculaImpostoService;
	@Autowired
	private EventoRendaFixaRepository eventoRendaFixaRepository;
	@Autowired
	private FeriadosService feriadosService;
	@Autowired
	private ParametroEventoFIIRepository parametroEventoFIIRepository;
	@Autowired
	private OperacaoRendaVariavelRepository operacaoRendaVariavelRepository;
	@Autowired
	private TipoEventoRepository tipoEventoRepository;

	@Autowired
	private OperacaoRendaFixaRepository operacaoRendaFixaRepository;

	@Autowired
	private AtivoRepository ativoRepository;

	public void cadastrarEvento(EventoAddRendaFixaDTO eventoDTO) {
			EventoRendaFixa evento = EventoRendaFixaMapper.INSTANCE.toEntity(eventoDTO);
			eventoRendaFixaRepository.save(evento);
	}
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

	public EventoAddRendaFixaDTO atualizarEventoRendaFixa(EventoAddRendaFixaDTO eventoDTO) {
		EventoRendaFixa evento = new EventoRendaFixa();
		if(eventoDTO.getId() != 0) {
			evento = EventoRendaFixaMapper.INSTANCE.toEntity(eventoDTO);
		}
		return EventoRendaFixaMapper.INSTANCE.toDTO(eventoRendaFixaRepository.save(evento));
	}

	public void excluirEvento(int id) {

		Optional<EventoRendaVariavel> evento = eventoRepository.findById(id);
		evento.ifPresent(value -> this.eventoRepository.delete(value));

	}

	public void excluirEventoRendaFixa(int id) {

		Optional<EventoRendaFixa> evento = eventoRendaFixaRepository.findById(id);
		evento.ifPresent(value -> this.eventoRendaFixaRepository.delete(value));

	}

	public List<EventoListRendaVariavelDTO> listarEventos() {
		List<EventoRendaVariavel> eventos =  eventoRepository.findAll();
		var eventosDTO = eventos.stream().map(EventoRendaVariavelMapper.INSTANCE::toListDTO).collect(Collectors.toList());
		return eventosDTO;
	}

	public List<TipoEventoDTO> listarTipoEventos() {
		List<TipoEvento> tipoEventos =  tipoEventoRepository.findAll();
		return tipoEventos.stream().map(evento -> new TipoEventoDTO(evento.getId(),evento.getNome())).collect(Collectors.toList());
	}

	public List<EventoListRendaVariavelDTO> listarEventosRendaVariavelPorData(Filter filter) {

		List<EventoRendaVariavel> eventos = eventoRepository.findEventosByData(filter.getStartDate(), filter.getEndDate(), filter.getSubclasse(), filter.getAno(), filter.getMes(), filter.getAtivo());
		return eventos.stream().map(EventoRendaVariavelMapper.INSTANCE::toListDTO)
				.peek(eventoDto -> eventoDto.setValorTotal(
				getValorLiquidoEvento(eventoDto)
				)).toList();

	}

	public List<EventoListRendaFixaDTO> listarEventosRendaFixaPorData(Filter filter) {

		List<EventoRendaFixa> eventos = eventoRendaFixaRepository.findEventosByData(filter.getStartDate(), filter.getEndDate(), filter.getSubclasse(), filter.getAno(), filter.getMes());

		return eventos.stream()
					.map(EventoRendaFixaMapper.INSTANCE::toListDTO)
					.peek(eventoDto -> {
						Optional<AtivoCarteiraRFDTO> ativo = operacaoRendaFixaRepository.findAtivoCarteiraRendaFixaById(eventoDto.getAtivo().getId());
						if(ativo.isPresent()) {
							eventoDto.setValorTotal(calculaImpostoService.getValorLiquidoImpostoEvento(eventoDto.getValor(), ativo.get().getIsIsento(), ativo.get().getDataOperacao()));
						}
					}).toList();

	}



	public double getTotalProventosPorAtivo(int ativoId) {
		return  eventoRepository.findEventosByAtivo(ativoId)
				.stream()
					.map(EventoRendaVariavelMapper.INSTANCE::toListDTO)
					.mapToDouble(this::getValorLiquidoEvento)
					.sum();
	}

	public double getTotalProventosPorAtivoRendaFixa(int ativoId) {

		var eventosDto = eventoRendaFixaRepository.findEventosByAtivo(ativoId)
				.stream().map(EventoRendaFixaMapper.INSTANCE::toListDTO).toList();

		return eventosDto.stream()
				.mapToDouble(evento -> {
					Optional<AtivoCarteiraRFDTO> operacao = operacaoRendaFixaRepository.findAtivoCarteiraRendaFixaById(evento.getAtivo().getId());
					evento.setValorTotal(this.calculaImpostoService.getValorLiquidoImpostoEvento(evento.getValor(), operacao.get().getIsIsento(), operacao.get().getDataOperacao()));
					return evento.getValorTotal();
				})
				.sum();
	}


	private double getValorLiquidoEvento(EventoListRendaVariavelDTO eventoDto) {
		var totalBruto = getValorBrutoEvento(eventoDto);
		if(eventoDto.getTipoEvento().getId() == TipoEventoEnum.JSCP.getId()) {
            return totalBruto - totalBruto * Taxa.ALIQUOTA_JSCP;
		} else {
			return totalBruto;
		}
	}

	private double getValorBrutoEvento(EventoListRendaVariavelDTO eventoDto) {
		var custodia = this.getCustodiaPorData(eventoDto.getAtivo().getId(), eventoDto.getDataCom());
		return custodia * eventoDto.getValor();
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
