package br.com.cashcontroller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import br.com.cashcontroller.dto.TipoOperacaoDTO;
import br.com.cashcontroller.entity.TipoOperacao;

@Mapper
public interface TipoOperacaoMapper {
	
	TipoOperacaoMapper INSTANCE = Mappers.getMapper(TipoOperacaoMapper.class);	
		
	TipoOperacaoDTO toDTO(TipoOperacao tipoOperacao);
	TipoOperacao toEntity(TipoOperacaoDTO tipoOperacaoDto);
	
	
}
