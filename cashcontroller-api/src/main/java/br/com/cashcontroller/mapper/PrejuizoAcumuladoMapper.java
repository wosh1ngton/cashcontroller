package br.com.cashcontroller.mapper;

import br.com.cashcontroller.dto.PrejuizoAcumuladoDTO;
import br.com.cashcontroller.entity.PrejuizoAcumulado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PrejuizoAcumuladoMapper {

    PrejuizoAcumuladoMapper INSTANCE = Mappers.getMapper(PrejuizoAcumuladoMapper.class);

    @Mapping(target = "user", ignore = true)
    PrejuizoAcumulado toEntity(PrejuizoAcumuladoDTO dto);
}
