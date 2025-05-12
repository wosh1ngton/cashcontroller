import { Component, OnInit } from '@angular/core';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { CadastrarIndiceComponent } from './cadastrar-indice/cadastrar-indice.component';
import { ActivatedRoute, Router } from '@angular/router';
import { IndexadorService } from 'src/app/services/indexador.service';
import { Indice } from 'src/app/models/indice.model';

@Component({
  selector: 'app-indices',
  templateUrl: './indices.component.html',
  styleUrl: './indices.component.css',
  providers: [DialogService],
})
export class IndicesComponent implements OnInit {
  
  cadastroIndiceRef: DynamicDialogRef | undefined;
  indice: any = ({} = '');
  indices: Indice[] = [];
  constructor(
    private dialogService: DialogService,
    private route: ActivatedRoute,
    private router: Router,
    private indiceService: IndexadorService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((param) => {
      this.indice = param;
      this.listarIndices();
    });
    this.listarIndices();
  }

  cols = [    
    { field: 'data', header: 'Data' },
    { field: 'valor', header: 'Valor' },
  ];

  cadastrarIndice() {
    this.cadastroIndiceRef = this.dialogService.open(CadastrarIndiceComponent, {
      header: 'Cadastrar Índice',
      width: '50%',   
      contentStyle: { overflow: 'auto' },
      baseZIndex: 10000,
      maximizable: true,
    });

    this.cadastroIndiceRef.onClose.subscribe((result: any) => {
      if (result) {
        this.listarIndices();
      }
    });
  }

  editar(data: any) {
    this.cadastroIndiceRef = this.dialogService.open(CadastrarIndiceComponent, {
      header: 'Editar Índice',
      width: '50%',     
      contentStyle: { overflow: 'auto' },
      baseZIndex: 10000,
      maximizable: true,
      data: {indice: data, isEdicao: true},
    });

    this.cadastroIndiceRef.onClose.subscribe((result: any) => {
      if (result) {
        console.log('teste', result);
      }
    });  
  }

  excluir(item: any) {

  }

  listarIndices() {
    this.indiceService.listarHistorico(this.indice.id).subscribe((result) => {
      this.indices = result.sort((a, b) => {
        const dateA = new Date(a.data);
        const dateB = new Date(b.data);
        return dateB.getTime() - dateA.getTime();
      });
    });
  }
}
