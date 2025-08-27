import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { OrderService } from './order.service';

describe('OrderService', () => {
  let service: OrderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],  // Import HttpClientTestingModule for HTTP requests
    });
    service = TestBed.inject(OrderService);
  });

  fit('frontend_should_create_order_service', () => {
    expect(service).toBeTruthy();
  });
});
