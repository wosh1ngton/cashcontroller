import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListarAportesComponent } from './listar-aportes.component';

describe('ListarAportesComponent', () => {
  let component: ListarAportesComponent;
  let fixture: ComponentFixture<ListarAportesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListarAportesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ListarAportesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
