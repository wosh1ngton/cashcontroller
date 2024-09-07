import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TableCarteiraPrincipalComponent } from './table-carteira-principal.component';

describe('TableCarteiraPrincipalComponent', () => {
  let component: TableCarteiraPrincipalComponent;
  let fixture: ComponentFixture<TableCarteiraPrincipalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TableCarteiraPrincipalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TableCarteiraPrincipalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
