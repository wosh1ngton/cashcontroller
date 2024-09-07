package br.com.cashcontroller.mapper;

import br.com.cashcontroller.dto.EventoAddRendaFixaDTO;
import br.com.cashcontroller.dto.EventoListRendaFixaDTO;
import br.com.cashcontroller.entity.EventoRendaFixa;
import br.com.cashcontroller.entity.EventoRendaVariavel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper
public interface EventoRendaFixaMapper {
	
	EventoRendaFixaMapper INSTANCE = Mappers.getMapper(EventoRendaFixaMapper.class);
	
    @Mapping(source = "ativo.id", target = "ativo")
    @Mapping(source = "tipoEvento.id", target = "tipoEvento")
    EventoAddRendaFixaDTO toDTO(EventoRendaFixa evento);


    @Mapping(source = "ativo", target = "ativo.id")
    @Mapping(source = "tipoEvento", target = "tipoEvento.id")
    EventoRendaFixa toEntity(EventoAddRendaFixaDTO eventoDto);

    @Mapping(source = "ativo.subclasseAtivo", target = "ativo.subclasseAtivoDto")
    EventoListRendaFixaDTO toListDTO(EventoRendaFixa evento);


}
