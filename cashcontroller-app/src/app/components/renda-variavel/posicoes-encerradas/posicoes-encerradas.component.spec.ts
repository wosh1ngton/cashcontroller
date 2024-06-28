import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PosicoesEncerradasComponent } from './posicoes-encerradas.component';

describe('PosicoesEncerradasComponent', () => {
  let component: PosicoesEncerradasComponent;
  let fixture: ComponentFixture<PosicoesEncerradasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PosicoesEncerradasComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PosicoesEncerradasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
