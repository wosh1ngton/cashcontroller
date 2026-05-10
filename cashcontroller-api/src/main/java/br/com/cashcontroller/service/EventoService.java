package br.com.cashcontroller.service;

import br.com.cashcontroller.dto.*;
import br.com.cashcontroller.dto.enums.TipoEventoEnum;
import br.com.cashcontroller.entity.*;
import br.com.cashcontroller.external.service.FeriadosService;
import br.com.cashcontroller.mapper.EventoRendaFixaMapper;
import br.com.cashcontroller.mapper.EventoRendaVariavelMapper;
import br.com.cashcontroller.mapper.ParametroEventoFIIMapper;
import br.com.cashcontroller.model.User;
import br.com.cashcontroller.repository.*;
import br.com.cashcontroller.security.SecurityUtils;
import br.com.cashcontroller.service.util.CalculaImpostoService;
import br.com.cashcontroller.utils.DataUtil;
import br.com.cashcontroller.utils.Taxa;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
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
			evento.setUser(SecurityUtils.getCurrentUser());
			eventoRendaFixaRepository.save(evento);
	}
	public void cadastrarEvento(EventoAddRendaVariavelDTO eventoDTO, String periodosDeRecorrencia) {

		if(Objects.equals(periodosDeRecorrencia, "0")) {
			EventoRendaVariavel evento = EventoRendaVariavelMapper.INSTANCE.toEntity(eventoDTO);
			evento.setUser(SecurityUtils.getCurrentUser());
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
		User currentUser = SecurityUtils.getCurrentUser();
		var eventos = eventosRecorrentes.stream()
				.map(EventoRendaVariavelMapper.INSTANCE::toEntity)
				.peek(ev -> ev.setUser(currentUser))
				.collect(Collectors.toList());
		eventoRepository.saveAll(eventos);
	}



	private long getCustodiaPorData(int idAtivo, LocalDate dataCom) {
		var custodia = this.operacaoRendaVariavelRepository.getCustodiaPorAtivo(idAtivo, dataCom, SecurityUtils.getCurrentUserId());
		return  custodia;
	}

	public EventoAddRendaVariavelDTO atualizarEvento(EventoAddRendaVariavelDTO eventoDTO) {
		EventoRendaVariavel evento = new EventoRendaVariavel();
		if(eventoDTO.getId() != 0) {
			User user = eventoRepository.findByIdAndUser(eventoDTO.getId(), SecurityUtils.getCurrentUserId())
					.map(EventoRendaVariavel::getUser)
					.orElseGet(SecurityUtils::getCurrentUser);
			evento = EventoRendaVariavelMapper.INSTANCE.toEntity(eventoDTO);
			evento.setUser(user);
		} else {
			evento.setUser(SecurityUtils.getCurrentUser());
		}
		return EventoRendaVariavelMapper.INSTANCE.toDTO(eventoRepository.save(evento));
	}

	public EventoAddRendaFixaDTO atualizarEventoRendaFixa(EventoAddRendaFixaDTO eventoDTO) {
		EventoRendaFixa evento = new EventoRendaFixa();
		if(eventoDTO.getId() != 0) {
			User user = eventoRendaFixaRepository.findByIdAndUser(eventoDTO.getId(), SecurityUtils.getCurrentUserId())
					.map(EventoRendaFixa::getUser)
					.orElseGet(SecurityUtils::getCurrentUser);
			evento = EventoRendaFixaMapper.INSTANCE.toEntity(eventoDTO);
			evento.setUser(user);
		} else {
			evento.setUser(SecurityUtils.getCurrentUser());
		}
		return EventoRendaFixaMapper.INSTANCE.toDTO(eventoRendaFixaRepository.save(evento));
	}

	public void excluirEvento(int id) {

		Optional<EventoRendaVariavel> evento = eventoRepository.findByIdAndUser(id, SecurityUtils.getCurrentUserId());
		evento.ifPresent(value -> this.eventoRepository.delete(value));

	}

	public void excluirEventoRendaFixa(int id) {

		Optional<EventoRendaFixa> evento = eventoRendaFixaRepository.findByIdAndUser(id, SecurityUtils.getCurrentUserId());
		evento.ifPresent(value -> this.eventoRendaFixaRepository.delete(value));

	}

	public List<EventoListRendaVariavelDTO> listarEventos() {
		List<EventoRendaVariavel> eventos =  eventoRepository.findAllByUser(SecurityUtils.getCurrentUserId());
		var eventosDTO = eventos.stream().map(EventoRendaVariavelMapper.INSTANCE::toListDTO).collect(Collectors.toList());
		return eventosDTO;
	}

	public List<TipoEventoDTO> listarTipoEventos() {
		List<TipoEvento> tipoEventos =  tipoEventoRepository.findAll();
		return tipoEventos.stream().map(evento -> new TipoEventoDTO(evento.getId(),evento.getNome())).collect(Collectors.toList());
	}

	public List<EventoListRendaVariavelDTO> listarEventosRendaVariavelPorData(Filter filter) {

		List<EventoRendaVariavel> eventos = eventoRepository.findEventosByData(filter.getStartDate(), filter.getEndDate(), filter.getSubclasse(), filter.getAno(), filter.getMes(), filter.getAtivo(), SecurityUtils.getCurrentUserId());
		return eventos.stream().map(EventoRendaVariavelMapper.INSTANCE::toListDTO)
				.peek(eventoDto -> eventoDto.setValorTotal(
				getValorLiquidoEvento(eventoDto)
				)).toList();

	}

	public List<EventoListRendaFixaDTO> listarEventosRendaFixaPorData(Filter filter) {

		List<EventoRendaFixa> eventos = eventoRendaFixaRepository.findEventosByData(filter.getStartDate(), filter.getEndDate(), filter.getSubclasse(), filter.getAno(), filter.getMes(), SecurityUtils.getCurrentUserId());

		return eventos.stream()
					.map(EventoRendaFixaMapper.INSTANCE::toListDTO)
					.peek(eventoDto -> {
						Optional<AtivoCarteiraRFDTO> ativo = operacaoRendaFixaRepository.findAtivoCarteiraRendaFixaById(eventoDto.getAtivo().getId(), SecurityUtils.getCurrentUserId());
						if(ativo.isPresent()) {
							eventoDto.setValorTotal(calculaImpostoService.getValorLiquidoImpostoEvento(eventoDto.getValor(), ativo.get().getIsIsento(), ativo.get().getDataOperacao()));
						}
					}).toList();

	}



	public double getTotalProventosPorAtivo(int ativoId) {
		return  eventoRepository.findEventosByAtivo(ativoId, SecurityUtils.getCurrentUserId())
				.stream()
					.map(EventoRendaVariavelMapper.INSTANCE::toListDTO)
					.mapToDouble(this::getValorLiquidoEvento)
					.sum();
	}

	public double getTotalProventosPorAtivoRendaFixa(int ativoId) {

		var eventosDto = eventoRendaFixaRepository.findEventosByAtivo(ativoId, SecurityUtils.getCurrentUserId())
				.stream().map(EventoRendaFixaMapper.INSTANCE::toListDTO).toList();

		return eventosDto.stream()
				.mapToDouble(evento -> {
					Optional<AtivoCarteiraRFDTO> operacao = operacaoRendaFixaRepository.findAtivoCarteiraRendaFixaById(evento.getAtivo().getId(), SecurityUtils.getCurrentUserId());
					evento.setValorTotal(this.calculaImpostoService.getValorLiquidoImpostoEvento(evento.getValor(), operacao.get().getIsIsento(), operacao.get().getDataOperacao()));
					return evento.getValorTotal();
				})
				.sum();
	}

	public Map<Integer, Double> getTotalProventosRendaFixaBatch(List<Integer> ativoIds) {
		if (ativoIds.isEmpty()) return Collections.emptyMap();
		long t0 = System.currentTimeMillis();

		long tQuery1 = System.currentTimeMillis();
		Long userId = SecurityUtils.getCurrentUserId();
		List<EventoRendaFixa> allEvents = eventoRendaFixaRepository.findEventosByAtivoIn(ativoIds, userId);
		log.info("[PERF]     findEventosByAtivoIn: {}ms ({} eventos)", System.currentTimeMillis() - tQuery1, allEvents.size());

		long tQuery2 = System.currentTimeMillis();
		List<AtivoCarteiraRFDTO> allParams = operacaoRendaFixaRepository.findAtivoParamsRendaFixaByAtivoIds(ativoIds, userId);
		log.info("[PERF]     findAtivoParamsRendaFixaByAtivoIds: {}ms ({} params)", System.currentTimeMillis() - tQuery2, allParams.size());

		Map<Integer, AtivoCarteiraRFDTO> paramsMap = allParams.stream()
				.collect(Collectors.toMap(AtivoCarteiraRFDTO::getIdAtivo, p -> p, (a, b) -> a));

		var result = allEvents.stream()
				.collect(Collectors.groupingBy(ev -> ev.getAtivo().getId()))
				.entrySet().stream()
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						entry -> {
							AtivoCarteiraRFDTO param = paramsMap.get(entry.getKey());
							if (param == null) return 0.0;
							return entry.getValue().stream()
									.map(EventoRendaFixaMapper.INSTANCE::toListDTO)
									.mapToDouble(dto -> calculaImpostoService.getValorLiquidoImpostoEvento(
											dto.getValor(), param.getIsIsento(), param.getDataOperacao()))
									.sum();
						}
				));
		log.info("[PERF]     getTotalProventosRendaFixaBatch total: {}ms", System.currentTimeMillis() - t0);
		return result;
	}


	private double getValorLiquidoEvento(EventoListRendaVariavelDTO eventoDto) {
		var totalBruto = getValorBrutoEvento(eventoDto);
		if(eventoDto.getTipoEvento().getId() == TipoEventoEnum.JSCP.getId()) {
			double aliquota = Taxa.getAliquotaJSCP(eventoDto.getDataCom());
            return totalBruto - totalBruto * aliquota;
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
