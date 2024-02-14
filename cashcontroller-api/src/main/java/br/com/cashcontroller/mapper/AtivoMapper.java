package br.com.cashcontroller.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import br.com.cashcontroller.dto.AtivoDTO;
import br.com.cashcontroller.entity.Ativo;
import br.com.cashcontroller.entity.SubclasseAtivo;


@Mapper
public interface AtivoMapper {
	
	AtivoMapper INSTANCE = Mappers.getMapper(AtivoMapper.class);
	
	@Mapping(source = "subclasseAtivo", target = "subclasseAtivoDto")
    AtivoDTO toDTO(Ativo ativo);
	
	@Mapping(source = "subclasseAtivoDto", target = "subclasseAtivo")
    Ativo toEntity(AtivoDTO ativoDto);
    
    List<AtivoDTO> toListDTO(List<Ativo> ativos);

}
