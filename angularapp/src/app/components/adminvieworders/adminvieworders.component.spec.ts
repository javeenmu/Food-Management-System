import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminviewordersComponent } from './adminvieworders.component';

describe('AdminviewordersComponent', () => {
  let component: AdminviewordersComponent;
  let fixture: ComponentFixture<AdminviewordersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminviewordersComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminviewordersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
