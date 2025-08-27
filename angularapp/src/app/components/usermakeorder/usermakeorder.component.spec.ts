import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsermakeorderComponent } from './usermakeorder.component';

describe('UsermakeorderComponent', () => {
  let component: UsermakeorderComponent;
  let fixture: ComponentFixture<UsermakeorderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UsermakeorderComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UsermakeorderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
