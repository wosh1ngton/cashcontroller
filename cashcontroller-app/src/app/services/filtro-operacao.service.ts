import { Injectable } from '@angular/core';
import { FilterOperacao } from '../models/filter-operacao.model';
import { DateUtil } from '../shared/util/date-util';
import { map, Observable } from 'rxjs';
import { FiltrarOperacao } from './interfaces/filtrar-operacao';

@Injectable({
  providedIn: 'root',
})
export class FiltroOperacaoService {
  filtrarPorMes($event: any, filter: FilterOperacao): void {
    filter.startDate = null;
    filter.ano = $event.ano;
    filter.mes = $event.mesInteiro;
    filter.endDate = DateUtil.getLastDayOfMonthByYear(filter.ano, filter.mes);
  }

  filtrarPorDataEspecifica(filter: FilterOperacao): void {
    if (filter.startDate != null && filter.endDate != null) {
      filter.ano = 0;
      filter.mes = 0;
    }
  }

  getMeses(service: FiltrarOperacao, ano?: number): Observable<any> {
    return service.getMesesComOperacoesPorAno(ano).pipe(
      map((meses) => {
        if (meses) {
          meses.forEach((element: any) => {
            element.mesString = element.mesString.slice(0, 3);
          });
        }
        return meses;
      })
    );
  }

  getAnos(service: FiltrarOperacao): Observable<number[]> {    
    return service.getAnosComOperacoes().pipe((res) => res);    
  }
}
