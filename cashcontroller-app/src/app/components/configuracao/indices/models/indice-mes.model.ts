export class IndiceMes {
  constructor(public data: string, public valor: number) {}

  static fromDate(date: Date, valor: number): IndiceMes {
    const formatted = date.toLocaleDateString('pt-BR').split('T')[0];
    return new IndiceMes(formatted, valor);
  }
}
