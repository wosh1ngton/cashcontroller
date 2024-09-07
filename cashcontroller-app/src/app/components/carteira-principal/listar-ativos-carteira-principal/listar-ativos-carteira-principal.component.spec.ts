import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListarAtivosCarteiraPrincipalComponent } from './listar-ativos-carteira-principal.component';

describe('ListarAtivosCarteiraPrincipalComponent', () => {
  let component: ListarAtivosCarteiraPrincipalComponent;
  let fixture: ComponentFixture<ListarAtivosCarteiraPrincipalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListarAtivosCarteiraPrincipalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ListarAtivosCarteiraPrincipalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
