package br.com.cashcontroller.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import br.com.cashcontroller.dto.SubclasseAtivoDTO;
import br.com.cashcontroller.entity.SubclasseAtivo;

@Mapper
public interface SubclasseAtivoMapper {
	
	SubclasseAtivoMapper INSTANCE = Mappers.getMapper(SubclasseAtivoMapper.class);
	

	SubclasseAtivoDTO toDTO(SubclasseAtivo subclasseAtivo);
	

	SubclasseAtivo toEntity(SubclasseAtivoDTO subclasseAtivoDto);
	
	List<SubclasseAtivoDTO> toListDTO(List<SubclasseAtivo> subclasseAtivos);
}
