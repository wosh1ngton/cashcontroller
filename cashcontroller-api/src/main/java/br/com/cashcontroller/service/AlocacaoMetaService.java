package br.com.cashcontroller.service;

import br.com.cashcontroller.dto.AlocacaoMetaDTO;
import br.com.cashcontroller.entity.AlocacaoMeta;
import br.com.cashcontroller.entity.enums.CategoriaAlocacao;
import br.com.cashcontroller.model.User;
import br.com.cashcontroller.repository.AlocacaoMetaRepository;
import br.com.cashcontroller.security.SecurityUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AlocacaoMetaService {

    @Autowired
    private AlocacaoMetaRepository repository;

    public List<AlocacaoMetaDTO> listar() {
        Long userId = SecurityUtils.getCurrentUserId();
        Map<CategoriaAlocacao, AlocacaoMeta> existentes = new EnumMap<>(CategoriaAlocacao.class);
        repository.findAllByUser(userId).forEach(meta -> existentes.put(meta.getCategoria(), meta));

        List<AlocacaoMetaDTO> resultado = new ArrayList<>();
        for (CategoriaAlocacao categoria : CategoriaAlocacao.values()) {
            AlocacaoMeta meta = existentes.get(categoria);
            double percentual = meta != null ? meta.getPercentual() : categoria.getPercentualPadrao();
            resultado.add(new AlocacaoMetaDTO(categoria, categoria.getDescricao(), percentual));
        }
        return resultado;
    }

    public List<AlocacaoMetaDTO> salvar(List<AlocacaoMetaDTO> metas) {
        User currentUser = SecurityUtils.getCurrentUser();
        Long userId = currentUser.getId();

        Map<CategoriaAlocacao, AlocacaoMeta> existentes = new EnumMap<>(CategoriaAlocacao.class);
        repository.findAllByUser(userId).forEach(meta -> existentes.put(meta.getCategoria(), meta));

        for (AlocacaoMetaDTO dto : metas) {
            if (dto.getCategoria() == null || dto.getPercentual() == null) continue;
            AlocacaoMeta meta = existentes.get(dto.getCategoria());
            if (meta == null) {
                meta = new AlocacaoMeta();
                meta.setUser(currentUser);
                meta.setCategoria(dto.getCategoria());
            }
            meta.setPercentual(dto.getPercentual());
            repository.save(meta);
        }

        return listar();
    }

    public Map<CategoriaAlocacao, Double> obterPercentuaisPorCategoria() {
        Long userId = SecurityUtils.getCurrentUserId();
        Map<CategoriaAlocacao, Double> resultado = new EnumMap<>(CategoriaAlocacao.class);
        for (CategoriaAlocacao categoria : CategoriaAlocacao.values()) {
            resultado.put(categoria, categoria.getPercentualPadrao());
        }
        repository.findAllByUser(userId).forEach(meta ->
                resultado.put(meta.getCategoria(), meta.getPercentual()));
        return resultado;
    }
}
