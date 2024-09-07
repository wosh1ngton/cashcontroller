package br.com.cashcontroller.mapper;

import br.com.cashcontroller.dto.AtivoCarteiraDTO;
import br.com.cashcontroller.entity.AtivoCarteira;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MapperIntegrationTest {

    @Test
    public void givenSourceToDestination_whenMaps_thenCorrect() {
        AtivoCarteira ativoCarteira = new AtivoCarteira();
        ativoCarteira.setId(1);
        ativoCarteira.setCustodia(20);

        AtivoCarteiraDTO ativoCarteiraDTO = AtivoCarteiraMapper.INSTANCE.toDTO(ativoCarteira);

        assertEquals(ativoCarteira.getId(), ativoCarteiraDTO.getId());
        assertEquals(ativoCarteira.getCustodia(), ativoCarteiraDTO.getCustodia());
    }
}
