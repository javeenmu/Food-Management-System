import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserviewfoodComponent } from './userviewfood.component';

describe('UserviewfoodComponent', () => {
  let component: UserviewfoodComponent;
  let fixture: ComponentFixture<UserviewfoodComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserviewfoodComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserviewfoodComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
