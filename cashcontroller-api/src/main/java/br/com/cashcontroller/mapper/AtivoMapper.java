package br.com.cashcontroller.mapper;

import java.util.List;

import br.com.cashcontroller.dto.AtivoAddDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import br.com.cashcontroller.dto.AtivoDTO;
import br.com.cashcontroller.entity.Ativo;
import br.com.cashcontroller.entity.SubclasseAtivo;


@Mapper
public interface AtivoMapper {
	
	AtivoMapper INSTANCE = Mappers.getMapper(AtivoMapper.class);


    @Mapping(source = "parametroRendaFixa", target = "parametroRendaFixaDto")
	@Mapping(source = "subclasseAtivo", target = "subclasseAtivoDto")
    AtivoDTO toDTO(Ativo ativo);

    @Mapping(target = "parametroRendaFixaDto", source = "parametroRendaFixa")
    @Mapping(target = "subclasseAtivo", source = "subclasseAtivo.id")
    AtivoAddDTO toAddDTO(Ativo ativo);

    @Mapping(source = "parametroRendaFixaDto", target = "parametroRendaFixa")
	@Mapping(source = "subclasseAtivoDto", target = "subclasseAtivo")
    Ativo toEntity(AtivoDTO ativoDto);

    @Mapping(source = "parametroRendaFixaDto.isIsento", target = "parametroRendaFixa.isIsento")
    @Mapping(source = "subclasseAtivo", target = "subclasseAtivo.id")
    @Mapping(source = "parametroRendaFixaDto", target = "parametroRendaFixa")
    Ativo toAddEntity(AtivoAddDTO ativoAddDto);

}
