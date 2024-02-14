package br.com.cashcontroller.mapper;

import br.com.cashcontroller.dto.IndexadorDTO;
import br.com.cashcontroller.entity.Indexador;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IndexadorMapper {
	
	IndexadorMapper INSTANCE = Mappers.getMapper(IndexadorMapper.class);
		
	IndexadorDTO toDTO(Indexador indexador);
	Indexador toEntity(IndexadorDTO indexadorDto);
	
	
}
