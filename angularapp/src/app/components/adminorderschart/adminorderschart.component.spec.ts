import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminorderschartComponent } from './adminorderschart.component';

describe('AdminorderschartComponent', () => {
  let component: AdminorderschartComponent;
  let fixture: ComponentFixture<AdminorderschartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminorderschartComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminorderschartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
