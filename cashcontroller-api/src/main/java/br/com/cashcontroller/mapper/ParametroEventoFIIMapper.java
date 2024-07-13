package br.com.cashcontroller.mapper;

import br.com.cashcontroller.dto.ParametroEventoFIIAddDTO;
import br.com.cashcontroller.entity.ParametroEventoFII;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper
public interface ParametroEventoFIIMapper {
	
	ParametroEventoFIIMapper INSTANCE = Mappers.getMapper(ParametroEventoFIIMapper.class);
	
    @Mapping(source = "ativo.id", target = "ativo")
    ParametroEventoFIIAddDTO toDTO(ParametroEventoFII parametro);


    @Mapping(source = "ativo", target = "ativo.id")
    ParametroEventoFII toEntity(ParametroEventoFIIAddDTO eventoDto);



   // EventoListRendaVariavelDTO toListDTO(EventoRendaVariavel evento);
    


}
