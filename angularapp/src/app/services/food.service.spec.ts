import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { FoodService } from './food.service';

describe('FoodService', () => {
  let service: FoodService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],  // Import HttpClientTestingModule for HTTP requests
    });
    service = TestBed.inject(FoodService);
  });

  fit('frontend_should_create_food_service', () => {
    expect(service).toBeTruthy();
  });
});
