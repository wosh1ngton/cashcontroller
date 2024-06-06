import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListarRendaFixaComponent } from './listar-renda-fixa.component';

describe('ListarRendaFixaComponent', () => {
  let component: ListarRendaFixaComponent;
  let fixture: ComponentFixture<ListarRendaFixaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListarRendaFixaComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ListarRendaFixaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
