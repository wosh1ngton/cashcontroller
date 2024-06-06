import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListarRendaVariavelComponent } from './listar-operacoes.component';

describe('ListarRendaVariavelComponent', () => {
  let component: ListarRendaVariavelComponent;
  let fixture: ComponentFixture<ListarRendaVariavelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListarRendaVariavelComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ListarRendaVariavelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
