import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventoRvFormComponent } from './evento-rv-form.component';

describe('EventoRvFormComponent', () => {
  let component: EventoRvFormComponent;
  let fixture: ComponentFixture<EventoRvFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventoRvFormComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EventoRvFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
