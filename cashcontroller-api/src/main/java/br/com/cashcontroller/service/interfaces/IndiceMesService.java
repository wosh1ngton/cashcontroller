package br.com.cashcontroller.service.interfaces;

import br.com.cashcontroller.entity.IndiceMesBase;

import java.util.Optional;

public interface IndiceMesService<T extends IndiceMesBase> {
    T save(T indice);
    T update(int id, T indice);
    Optional<T> findById(int id);
}
