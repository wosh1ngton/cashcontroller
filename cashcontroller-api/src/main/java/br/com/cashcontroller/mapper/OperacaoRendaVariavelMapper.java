package br.com.cashcontroller.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import br.com.cashcontroller.dto.OperacaoRendaVariavelDTO;
import br.com.cashcontroller.entity.OperacaoRendaVariavel;

@Mapper
public interface OperacaoRendaVariavelMapper {
	
	OperacaoRendaVariavelMapper INSTANCE = Mappers.getMapper(OperacaoRendaVariavelMapper.class);
	
	@Mapping(source = "ativo", target = "ativoDto")
	@Mapping(source = "tipoOperacao", target = "tipoOperacaoDto")	
	OperacaoRendaVariavelDTO toDTO(OperacaoRendaVariavel operacaoRendaVariavel);
	
	@Mapping(source = "ativoDto", target = "ativo")
	@Mapping(source = "tipoOperacaoDto", target = "tipoOperacao")	
	OperacaoRendaVariavel toEntity(OperacaoRendaVariavelDTO operacaoRendaVariavelDto);
	
	List<OperacaoRendaVariavelDTO> toListDTO(List<OperacaoRendaVariavel> subclasseAtivos);
	List<OperacaoRendaVariavel> toListEntity(List<OperacaoRendaVariavelDTO> operacoesRendaVariavel);
}
