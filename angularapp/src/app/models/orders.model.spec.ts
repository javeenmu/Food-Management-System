import { orders } from './orders.model';

describe('Orders Model', () => {

  fit('frontend_Orders_model_should_create_an_instance', () => {
    // Create a sample Orders object
    const order: orders = {
      orderId: 1001,
      orderStatus: 'Delivered',
      totalAmount: 499.99,
      quantity: 2,
      userId: 456,
      foodId: 789,
      orderDate: '2024-07-10'
    };

    expect(order).toBeTruthy();

    // Validate individual properties
    expect(order.orderStatus).toBe('Delivered');
    expect(order.totalAmount).toBe(499.99);
    expect(order.quantity).toBe(2);
 
  });

});
