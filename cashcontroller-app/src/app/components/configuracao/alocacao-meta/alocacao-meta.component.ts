import { Component, OnInit } from '@angular/core';
import { MessageService } from 'primeng/api';
import { AlocacaoMeta } from 'src/app/models/alocacao-meta.model';
import { AlocacaoMetaService } from 'src/app/services/alocacao-meta.service';

@Component({
  selector: 'app-alocacao-meta',
  templateUrl: './alocacao-meta.component.html',
  styleUrl: './alocacao-meta.component.css',
})
export class AlocacaoMetaComponent implements OnInit {

  metas: AlocacaoMeta[] = [];
  carregando = false;
  salvando = false;

  constructor(
    private alocacaoMetaService: AlocacaoMetaService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.carregar();
  }

  carregar() {
    this.carregando = true;
    this.alocacaoMetaService.listar().subscribe({
      next: (res) => {
        this.metas = res;
        this.carregando = false;
      },
      error: () => {
        this.carregando = false;
      }
    });
  }

  get total(): number {
    return this.metas.reduce((acc, meta) => acc + (Number(meta.percentual) || 0), 0);
  }

  get totalValido(): boolean {
    return Math.abs(this.total - 100) < 0.01;
  }

  salvar() {
    if (!this.totalValido) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Soma inválida',
        detail: `O total dos percentuais deve ser 100% (atual: ${this.total.toFixed(2)}%)`
      });
      return;
    }

    this.salvando = true;
    this.alocacaoMetaService.salvar(this.metas).subscribe({
      next: (res) => {
        this.metas = res;
        this.salvando = false;
        this.messageService.add({
          severity: 'success',
          summary: 'Sucesso',
          detail: 'Percentuais atualizados',
        });
      },
      error: (e) => {
        this.salvando = false;
        this.messageService.add({
          severity: 'error',
          summary: 'Erro',
          detail: e?.error?.message || e?.error || 'Falha ao salvar',
        });
      }
    });
  }
}
