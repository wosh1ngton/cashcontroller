package br.com.cashcontroller.mapper;

import br.com.cashcontroller.dto.OperacaoRendaFixaDTO;
import br.com.cashcontroller.dto.OperacaoRendaVariavelDTO;
import br.com.cashcontroller.entity.OperacaoRendaFixa;
import br.com.cashcontroller.entity.OperacaoRendaVariavel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OperacaoRendaFixaMapper {
	
	OperacaoRendaFixaMapper INSTANCE = Mappers.getMapper(OperacaoRendaFixaMapper.class);
	
	@Mapping(source = "ativo", target = "ativoDto")	
	@Mapping(source = "tipoOperacao", target = "tipoOperacaoDto")
	@Mapping(source = "indexador", target = "indexadorDto")
	OperacaoRendaFixaDTO toDTO(OperacaoRendaFixa operacaoRendaFixa);
	
	@Mapping(source = "ativoDto", target = "ativo")
	@Mapping(source = "tipoOperacaoDto", target = "tipoOperacao")
	@Mapping(source = "indexadorDto", target = "indexador")
	OperacaoRendaFixa toEntity(OperacaoRendaFixaDTO operacaoRendaFixaDto);
	
	List<OperacaoRendaVariavelDTO> toListDTO(List<OperacaoRendaVariavel> subclasseAtivos);
	List<OperacaoRendaVariavel> toListEntity(List<OperacaoRendaVariavelDTO> operacoesRendaVariavel);
}
