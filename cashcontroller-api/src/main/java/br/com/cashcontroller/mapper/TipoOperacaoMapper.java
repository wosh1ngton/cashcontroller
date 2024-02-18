package br.com.cashcontroller.mapper;

import br.com.cashcontroller.dto.AtivoDTO;
import br.com.cashcontroller.entity.Ativo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import br.com.cashcontroller.dto.TipoOperacaoDTO;
import br.com.cashcontroller.entity.TipoOperacao;

import java.util.List;

@Mapper
public interface TipoOperacaoMapper {
	
	TipoOperacaoMapper INSTANCE = Mappers.getMapper(TipoOperacaoMapper.class);	
		
	TipoOperacaoDTO toDTO(TipoOperacao tipoOperacao);
	TipoOperacao toEntity(TipoOperacaoDTO tipoOperacaoDto);

	List<TipoOperacaoDTO> toListDTO(List<TipoOperacao> tiposOperacao);
	
	
}
