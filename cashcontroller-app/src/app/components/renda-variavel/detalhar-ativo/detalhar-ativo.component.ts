import {
  Component,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges,
} from '@angular/core';
import { PrimengModule } from 'src/app/primeng/primeng.module';
import { OperacaoRendaVariavelService } from 'src/app/services/operacao-renda-variavel.service';

@Component({
  selector: 'app-detalhar-ativo',
  standalone: true,
  imports: [PrimengModule],
  templateUrl: './detalhar-ativo.component.html',
  styleUrl: './detalhar-ativo.component.css',
})
export class DetalharAtivoComponent implements OnChanges {
  @Input() idAtivo: number = 0;
  operacoes: any;

  constructor(private operacaoService: OperacaoRendaVariavelService) {}

  ngOnChanges(changes: SimpleChanges): void {
    this.operacaoService.getOperacoesPorAtivo(this.idAtivo).subscribe((val) => {      
      this.operacoes = val;
    });
  }
}
