import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CadastrarAtivoCarteiraComponent } from './cadastrar-ativo-carteira.component';

describe('CadastrarAtivoCarteiraComponent', () => {
  let component: CadastrarAtivoCarteiraComponent;
  let fixture: ComponentFixture<CadastrarAtivoCarteiraComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CadastrarAtivoCarteiraComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CadastrarAtivoCarteiraComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
