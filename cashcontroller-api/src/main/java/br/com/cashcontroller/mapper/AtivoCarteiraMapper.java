package br.com.cashcontroller.mapper;


import br.com.cashcontroller.dto.AtivoCarteiraAddDTO;
import br.com.cashcontroller.dto.AtivoCarteiraDTO;
import br.com.cashcontroller.dto.AtivoCarteiraRFDTO;
import br.com.cashcontroller.entity.AtivoCarteira;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AtivoCarteiraMapper  {

    AtivoCarteiraMapper INSTANCE = Mappers.getMapper(AtivoCarteiraMapper.class);

    @Mapping(target = "ativo.subclasseAtivo", ignore = true)
    AtivoCarteiraDTO toDTO(AtivoCarteira ativoCarteira);

    @Mapping(target = "ativo.parametroRendaFixa.ativo", ignore = true)
    AtivoCarteiraDTO toListDTO(AtivoCarteira ativoCarteira);

    @Mapping(source = "ativo.id", target = "ativo")
    AtivoCarteiraAddDTO toAddDTO(AtivoCarteira ativoCarteira);

    @Mapping(target = "ativo.subclasseAtivo", ignore = true)
    AtivoCarteira toEntity(AtivoCarteiraDTO ativoCarteiraDTO);

    @Mapping(source = "ativo", target = "ativo.id")
    AtivoCarteira toAddEntity(AtivoCarteiraAddDTO ativoCarteiraDTO);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AtivoCarteiraDTO dto, @MappingTarget AtivoCarteira entity);

    @Mapping(target = "idSubclasseAtivo", source = "ativo.subclasseAtivo.id")
    @Mapping(target = "idAtivo", source = "ativo.id")
    @Mapping(target = "nomeAtivo", source = "ativo.nome")
    @Mapping(target = "idIndice", source = "ativo.parametroRendaFixa.indexador.id")
    @Mapping(target = "indice", source = "ativo.parametroRendaFixa.indexador.nome")
    AtivoCarteiraRFDTO toRFDTO(AtivoCarteiraDTO ativoCarteiraDTO);

}
