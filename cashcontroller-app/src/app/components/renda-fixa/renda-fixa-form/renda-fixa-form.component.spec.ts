import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RendaFixaFormComponent } from './renda-fixa-form.component';

describe('RendaFixaFormComponent', () => {
  let component: RendaFixaFormComponent;
  let fixture: ComponentFixture<RendaFixaFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RendaFixaFormComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RendaFixaFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
