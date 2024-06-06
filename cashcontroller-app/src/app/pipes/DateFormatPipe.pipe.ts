import { DatePipe } from "@angular/common";
import { Pipe, PipeTransform } from "@angular/core";

@Pipe({
    name: 'dateFormat',
    standalone:true
})
export class DateFormatPipe implements PipeTransform {


    transform(value: any, format: string = 'mediumDate') {
        const datePipe = new DatePipe('pt');
        return datePipe.transform(value, format);
    }
}