package br.com.cashcontroller.mapper;

import br.com.cashcontroller.dto.AtivoDTO;
import br.com.cashcontroller.dto.IndexadorDTO;
import br.com.cashcontroller.entity.Ativo;
import br.com.cashcontroller.entity.Indexador;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface IndexadorMapper {
	
	IndexadorMapper INSTANCE = Mappers.getMapper(IndexadorMapper.class);
		
	IndexadorDTO toDTO(Indexador indexador);
	Indexador toEntity(IndexadorDTO indexadorDto);
	List<IndexadorDTO> toListDTO(List<Indexador> indexadores);
	
}
