import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserviewordersComponent } from './uservieworders.component';

describe('UserviewordersComponent', () => {
  let component: UserviewordersComponent;
  let fixture: ComponentFixture<UserviewordersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserviewordersComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserviewordersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
