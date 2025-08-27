import { User } from './user.model';

describe('User Model', () => {

  fit('frontend_User_model_should_create_an_instance', () => {
    // Create a sample User object
    const user: User = {
      email: 'testuser@example.com',
      password: 'testpassword',
      username: 'testuser',
      mobileNumber: '1234567890',
      userRole: 'admin'
    };

    expect(user).toBeTruthy();

    expect(user.userId).toBeUndefined();
    expect(user.email).toBe('testuser@example.com');
  });

});
