import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Feedback } from 'src/app/models/feedback.model';
import { Food } from 'src/app/models/food.model';
import { Orders } from 'src/app/models/orders.model';
import { FeedbackService } from 'src/app/services/feedback.service';
import { FoodService } from 'src/app/services/food.service';
import { OrderService } from 'src/app/services/order.service';

@Component({
  selector: 'app-useraddfeedback',
  templateUrl: './useraddfeedback.component.html',
  styleUrls: ['./useraddfeedback.component.css']
})
export class UseraddfeedbackComponent implements OnInit,OnDestroy {

  feedback: Feedback = {
    feedbackText: '',
    rating: 0,
    date: new Date(),
    userId: 0,
  };

  errorMessage: string = '';
  successMessage: string = '';
  
  feedbackForm: FormGroup;
  submitted = false;
  stars = [1, 2, 3, 4, 5];
  foodItems: Food[] = [];
  order: Orders = {};
  food: Food;
  foodName: string = '';
  feedbackDate: Date = new Date();
  getOrderSubscription:Subscription;
  getOrderIdSubscription:Subscription;
  getFeedbackSubscription:Subscription;
  getFoodSubsciption:Subscription;
  constructor(
    private feedbackService: FeedbackService,
    private router: Router,
    private aroute: ActivatedRoute,
    private fb: FormBuilder,
    private foodService: FoodService,
    private orderService: OrderService
  ) { }
  
  ngOnInit(): void {
    this.feedbackForm = this.fb.group({
      feedback: ['', [Validators.required,Validators.maxLength(400)]],
      rating: [0, [Validators.required, Validators.min(1),]]
    });
    
    this.getOrderIdSubscription= this.aroute.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        const id = +idParam;
        this.getOrderSubscription=this.orderService.getOrdersById(id).subscribe(order => {
          this.order = order;
          if (order?.food?.foodId) {
            this.getFoodSubsciption=this.foodService.getFoodById(order.food.foodId).subscribe(food => {
              this.food = food;
              this.foodName = food.foodName;
            });
          } else {
            this.errorMessage = 'Invalid order or missing food ID.';
          }
        }, err => {
          this.errorMessage = 'Unable to fetch order details.';
        });
      } else {
        this.errorMessage = 'Order ID is missing in the parameters.';
      }
    });
  }

  setNumRating(value: number): void {
    this.feedbackForm.patchValue({ rating: value });
  }

  setRating(star: number) {
    this.feedbackForm.controls['rating'].setValue(star);
  }
  
  onSubmit(): void {
    this.submitted = true;
    
    if (this.feedbackForm.valid) {
      const formValue = this.feedbackForm.value;
      this.feedback.feedbackText = formValue.feedback;
      this.feedback.rating = formValue.rating;
      this.feedback.date = this.feedbackDate;
      this.feedback.userId = +sessionStorage.getItem('userId');
      
      
      this.feedback.food = this.food;
      
      this.getFeedbackSubscription=this.feedbackService.sendFeedback(this.feedback).subscribe({
        next: () => {
          this.successMessage = 'Feedback added successfully';
          this.errorMessage = '';
          this.feedbackForm.reset();
          this.submitted = false;
        },
        error: (error) => {
          this.errorMessage = error?.error?.errorMessage || 'Failed to submit feedback.';
          this.successMessage = '';
        }
        
      });
    }
  }
  
  // Optional: dismiss modals
  closeError(): void {
    this.errorMessage = '';
  }
  
  closeSuccess(): void {
    this.successMessage = '';
    this.router.navigate(['/userviewfeedback']);
  }
  ngOnDestroy(): void {
    if(this.getOrderSubscription)
      this.getOrderSubscription.unsubscribe();
    if(this.getFeedbackSubscription)
    this.getFeedbackSubscription.unsubscribe();
  if(this.getOrderIdSubscription)
      this.getOrderIdSubscription.unsubscribe();
  }
}