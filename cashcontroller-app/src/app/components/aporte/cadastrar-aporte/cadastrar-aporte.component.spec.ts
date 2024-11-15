import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CadastrarAporteComponent } from './cadastrar-aporte.component';

describe('CadastrarAporteComponent', () => {
  let component: CadastrarAporteComponent;
  let fixture: ComponentFixture<CadastrarAporteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CadastrarAporteComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CadastrarAporteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
