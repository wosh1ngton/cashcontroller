import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListarCarteiraRendaFixaComponent } from './listar-carteira-renda-fixa.component';

describe('ListarCarteiraRendaFixaComponent', () => {
  let component: ListarCarteiraRendaFixaComponent;
  let fixture: ComponentFixture<ListarCarteiraRendaFixaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListarCarteiraRendaFixaComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ListarCarteiraRendaFixaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
