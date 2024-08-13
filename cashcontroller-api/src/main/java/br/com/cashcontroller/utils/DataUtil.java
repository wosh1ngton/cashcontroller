package br.com.cashcontroller.utils;

import br.com.cashcontroller.external.dto.FeriadoDTO;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;


public class DataUtil {

    private static boolean isFeriado(LocalDate date, FeriadoDTO[] feriados) {
        for (FeriadoDTO feriado : feriados) {
            if (date.equals(feriado.getDate())) {
                return true;
            }
        }
        return false;
    }

    private static boolean isFeriado(LocalDate date, List<FeriadoDTO> feriados) {
        for (FeriadoDTO feriado : feriados) {
            if (date.equals(feriado.getDate())) {
                return true;
            }
        }
        return false;
    }
    public static LocalDate getDataPagamentoFII(LocalDate startDate, int diasUteis, FeriadoDTO[] feriados) {

        LocalDate resultDate = startDate;
        int addedDays = 1;

        while (addedDays < diasUteis) {
            resultDate = resultDate.plusDays(1);
            if (!(resultDate.getDayOfWeek() == DayOfWeek.SATURDAY ||
                    resultDate.getDayOfWeek() == DayOfWeek.SUNDAY ||
                    isFeriado(resultDate, feriados))) {
                addedDays++;
            }
        }

        return resultDate;
    }

    public static LocalDate getDataComFII(LocalDate startDate, int diasUteis, FeriadoDTO[] feriados) {

        if(diasUteis == -1) {
            return getUltimoDiaUtildoMes(startDate, feriados);
        }

        LocalDate resultDate = startDate;
        int addedDays = 1;

        while (addedDays < diasUteis) {
            resultDate = resultDate.plusDays(1);
            if (!(resultDate.getDayOfWeek() == DayOfWeek.SATURDAY ||
                    resultDate.getDayOfWeek() == DayOfWeek.SUNDAY ||
                    isFeriado(resultDate, feriados))) {
                addedDays++;
            }
        }

        return resultDate;
    }

    public static LocalDate getUltimoDiaUtildoMes(LocalDate startDate, FeriadoDTO[] feriados) {

        LocalDate data = startDate.minusMonths(1);
        YearMonth dataMesAno = YearMonth.of(data.getYear(), data.getMonthValue());
        LocalDate lastDay = dataMesAno.atEndOfMonth();

        while (lastDay.getDayOfWeek() == DayOfWeek.SATURDAY || lastDay.getDayOfWeek() == DayOfWeek.SUNDAY ||
                isFeriado(lastDay, feriados)) {
            lastDay = lastDay.minusDays(1);
        }

        return lastDay;
    }


    public static LocalDate getPrimeiroDiaUtildoMes(int year, int month, FeriadoDTO[] feriados) {

        LocalDate firstDay = LocalDate.of(year,month, 1);

        while (firstDay.getDayOfWeek() == DayOfWeek.SATURDAY || firstDay.getDayOfWeek() == DayOfWeek.SUNDAY ||
        isFeriado(firstDay, feriados)) {
            firstDay = firstDay.plusDays(1);
        }

        return firstDay;
    }

    public static int calculateWorkdays(LocalDate startDate, LocalDate endDate, List<FeriadoDTO> feriados) {
        int workdays = 0;
        LocalDate date = startDate;

        while (!date.isAfter(endDate)) {
            if (!isWeekend(date) && !isFeriado(date, feriados)) {
                workdays++;
            }
            date = date.plusDays(1);
        }

        return workdays;
    }

    public static int calculateWorkdays(LocalDate startDate, LocalDate endDate) {
        int workdays = 0;
        LocalDate date = startDate;

        while (!date.isAfter(endDate)) {
            if (!isWeekend(date)) {
                workdays++;
            }
            date = date.plusDays(1);
        }

        return workdays;
    }

    private static boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }


    public static YearMonth stringToYearMonth (String dateString) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate date = LocalDate.parse(dateString, formatter);
            YearMonth yearMonth = YearMonth.from(date);
            return yearMonth;
        } catch (DateTimeParseException e) {
            System.out.println("Error parsing date: " + e.getMessage());
        }
        return null;
    }
}
