import * as moment from "moment";

Date.prototype.toJSON = function () {
    return moment(this).format('YYYY-MM-DDTHH:mm:ss.SSS');
};

export class DateUtil {

    static dataBr = {
        firstDayOfWeek: 1,
        dayNames: ['Domingo', 'Segunda-Feira', 'Terça-Feira', 'Quarta-Feira', 'Quinta-Feira', 'Sexta-Feira', 'Sábado'],
        dayNamesShort: ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb'],
        dayNamesMin: ['D', 'S', 'T', 'Q', 'Q', 'S', 'S'],
        monthNames: ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'],
        monthNamesShort: ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez'],
        today: 'Hoje',
        clear: 'Limpar'
    };

    static getMonthNumber(date: Date) {
        const month = date.getMonth() + 1;
        return month;
    }

    static getFirstDayOfMonth(date: Date): Date {
        const firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
        return firstDay;
    }

    static getLastDayOfMonthByYear(year: number, month: number): Date {
        const nextMonth = new Date(year, month, 1);
        nextMonth.setDate(nextMonth.getDate() - 1);
        return nextMonth;
    }

    static getLastDayOfMonth(date: Date): Date {
        const lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0);
        return lastDay;
      }
}