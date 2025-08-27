import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminaddfoodComponent } from './adminaddfood.component';

describe('AdminaddfoodComponent', () => {
  let component: AdminaddfoodComponent;
  let fixture: ComponentFixture<AdminaddfoodComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminaddfoodComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminaddfoodComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
