import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RendaVariavelFormComponent } from './renda-variavel-form.component';

describe('RendaVariavelFormComponent', () => {
  let component: RendaVariavelFormComponent;
  let fixture: ComponentFixture<RendaVariavelFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RendaVariavelFormComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RendaVariavelFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
