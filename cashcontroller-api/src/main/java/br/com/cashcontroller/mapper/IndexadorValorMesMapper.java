package br.com.cashcontroller.mapper;

import br.com.cashcontroller.dto.IndiceDTO;
import br.com.cashcontroller.entity.IpcaMes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IndexadorValorMesMapper {

    IndexadorValorMesMapper INSTANCE = Mappers.getMapper(IndexadorValorMesMapper.class);
    @Mapping(target = "tipo", ignore = true)
    IndiceDTO fromIPCAtoDTO(IpcaMes ipcaMes);

    @Mapping(target = "dataYearMonth", ignore = true)
    IpcaMes fromIPCAtoEntity(IndiceDTO dto);
}
