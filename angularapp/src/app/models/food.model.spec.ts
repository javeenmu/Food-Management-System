import { Food } from './food.model';

describe('Food Model', () => {

  fit('frontend_Food_model_should_create_an_instance', () => {
    // Create a sample Food object
    const food: Food = {
      foodId: 201,
      foodName: 'Burger',
      price: 150.75,
      stockQuantity: 50,
      photo: 'https://8080-efaadabebabddbbcaeabeadfbfbdcfeda.premiumproject.examly.io/burger.jpg',
      userId: 789
    };

    expect(food).toBeTruthy();

    // Validate individual properties
    expect(food.foodName).toBe('Burger');
   
  });

});
