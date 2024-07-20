package br.com.cashcontroller.mapper;

import java.util.List;

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


    @Mapping(target = "subclasseAtivo", ignore = true)
	@Mapping(source = "subclasseAtivo", target = "subclasseAtivoDto")
    AtivoDTO toDTO(Ativo ativo);

    @Mapping(target = "subclasseAtivoDto", ignore = true)
    @Mapping(target = "subclasseAtivo", source = "subclasseAtivo.id")
    AtivoDTO toAddDTO(Ativo ativo);

	@Mapping(source = "subclasseAtivoDto", target = "subclasseAtivo")
    Ativo toEntity(AtivoDTO ativoDto);

    @Mapping(source = "subclasseAtivo", target = "subclasseAtivo.id")
    Ativo toAddEntity(AtivoDTO ativoDto);

//    List<AtivoDTO> toListDTO(List<Ativo> ativos);

}
