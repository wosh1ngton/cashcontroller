package br.com.cashcontroller.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.cashcontroller.dto.OperacaoRendaVariavelSaveDTO;
import br.com.cashcontroller.entity.Ativo;
import br.com.cashcontroller.entity.TipoOperacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import br.com.cashcontroller.dto.OperacaoRendaVariavelDTO;
import br.com.cashcontroller.entity.OperacaoRendaVariavel;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface OperacaoRendaVariavelMapper {
	
	OperacaoRendaVariavelMapper INSTANCE = Mappers.getMapper(OperacaoRendaVariavelMapper.class);
	
	@Mapping(source = "ativo.id", target = "ativoDto")
	@Mapping(source = "tipoOperacao.id", target = "tipoOperacaoDto")
	OperacaoRendaVariavelSaveDTO toSaveDTO(OperacaoRendaVariavel operacaoRendaVariavel);
	
	@Mapping(source = "ativoDto", target = "ativo.id")
	@Mapping(source = "tipoOperacaoDto", target = "tipoOperacao.id")
	OperacaoRendaVariavel toSaveEntity(OperacaoRendaVariavelSaveDTO operacaoRendaVariavelSaveDTO);


	@Mapping(target = "ativoDto", source = "ativo")
	@Mapping(target = "tipoOperacaoDto", source = "tipoOperacao")
	OperacaoRendaVariavelDTO toDTO(OperacaoRendaVariavel operacaoRendaVariavel);

	@Mapping(target = "ativo", source = "ativoDto")
	@Mapping(target = "tipoOperacao", source = "tipoOperacaoDto")
	OperacaoRendaVariavel toEntity(OperacaoRendaVariavelDTO operacaoRendaVariavelDTO);

	@Mapping(source = "ativoDto.id", target = "ativoDto")
	@Mapping(source = "tipoOperacaoDto.id", target = "tipoOperacaoDto")
	OperacaoRendaVariavelSaveDTO FromDTOtoSaveDTO(OperacaoRendaVariavelDTO operacaoRendaVariavelDTO);
	
	List<OperacaoRendaVariavelDTO> toListDTO(List<OperacaoRendaVariavel> operacoesEntity);
	List<OperacaoRendaVariavel> toListEntity(List<OperacaoRendaVariavelDTO> operacoesDTO);


//	@Named("mapToAtivo")
//	default Ativo mapToAtivo(Map<Integer, String> ativoDto) {
//		if (ativoDto == null) {
//			return null;
//		}
//		Ativo ativo = new Ativo();
//		// Logic to map from Map<Long, String> to Ativo
//		// You can use your custom logic here to map Long to id and String to nome
//		// Example:
//		ativo.setId(ativoDto.keySet().iterator().next());
//		ativo.setNome(ativoDto.values().iterator().next());
//		return ativo;
//	}

//	@Named("mapToAtivoDto")
//	default Map<Integer, String>  mapToAtivoDto(Ativo ativo) {
//		if (ativo == null) {
//			return null;
//		}
//		Map<Integer, String> ativoDto = new HashMap<Integer,String>();
//		// Logic to map from Map<Long, String> to Ativo
//		// You can use your custom logic here to map Long to id and String to nome
//		// Example:
//		ativoDto.put(ativo.getId(), ativo.getSigla());
//
//		return ativoDto;
//	}

}
