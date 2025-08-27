import { Feedback } from './feedback.model';

describe('Feedback Model', () => {

  fit('frontend_Feedback_model_should_create_an_instance', () => {
    // Create a sample Feedback object
    const feedback: Feedback = {
      feedbackId: 101,
      userId: 456,
      feedbackText: 'Great service and support!',
      date: new Date('2024-07-10'),
      foodId: 789,
      rating: 5
    };

    expect(feedback).toBeTruthy();

    // Validate individual properties
    
    expect(feedback.feedbackText).toBe('Great service and support!');

  });

});
