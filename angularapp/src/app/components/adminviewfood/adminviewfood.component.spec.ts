import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminviewfoodComponent } from './adminviewfood.component';

describe('AdminviewfoodComponent', () => {
  let component: AdminviewfoodComponent;
  let fixture: ComponentFixture<AdminviewfoodComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminviewfoodComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminviewfoodComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
