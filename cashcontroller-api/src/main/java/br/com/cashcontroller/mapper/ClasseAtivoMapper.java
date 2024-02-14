package br.com.cashcontroller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import br.com.cashcontroller.dto.ClasseAtivoDTO;
import br.com.cashcontroller.entity.ClasseAtivo;

@Mapper
public interface ClasseAtivoMapper {
	
	ClasseAtivoMapper INSTANCE = Mappers.getMapper(ClasseAtivoMapper.class);	
	
	ClasseAtivoDTO toDTO(ClasseAtivo classeAtivo);
	ClasseAtivo toEntity(ClasseAtivoDTO classeAtivoDto);
}
