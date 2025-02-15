package br.com.cashcontroller.mapper;

import br.com.cashcontroller.dto.AporteDTO;
import br.com.cashcontroller.entity.Aporte;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AporteMapper {
	
	AporteMapper INSTANCE = Mappers.getMapper(AporteMapper.class);
		
	AporteDTO toDTO(Aporte entity);
	Aporte toEntity(AporteDTO dto);
	
}
