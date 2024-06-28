package br.com.cashcontroller.mapper;

import br.com.cashcontroller.dto.EventoAddRendaVariavelDTO;
import br.com.cashcontroller.dto.EventoListRendaVariavelDTO;
import br.com.cashcontroller.entity.EventoRendaVariavel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper
public interface EventoRendaVariavelMapper {
	
	EventoRendaVariavelMapper INSTANCE = Mappers.getMapper(EventoRendaVariavelMapper.class);
	
    @Mapping(source = "ativo.id", target = "ativo")
    @Mapping(source = "tipoEvento.id", target = "tipoEvento")
    EventoAddRendaVariavelDTO toDTO(EventoRendaVariavel evento);


    @Mapping(source = "ativo", target = "ativo.id")
    @Mapping(source = "tipoEvento", target = "tipoEvento.id")
    EventoRendaVariavel toEntity(EventoAddRendaVariavelDTO eventoDto);


    EventoListRendaVariavelDTO toListDTO(EventoRendaVariavel evento);
    


}
