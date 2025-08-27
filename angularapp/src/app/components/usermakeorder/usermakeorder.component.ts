import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Food } from 'src/app/models/food.model';
import { Orders } from 'src/app/models/orders.model';
import { FoodService } from 'src/app/services/food.service';
import { OrderService } from 'src/app/services/order.service';

@Component({
  selector: 'app-usermakeorder',
  templateUrl: './usermakeorder.component.html',
  styleUrls: ['./usermakeorder.component.css']
})
export class UsermakeorderComponent implements OnInit ,OnDestroy{
  foodId: number = null;
  foodToOrder: Food = {};
  quantity: number = 1;
  totalPrice: number = 0;
  orderDate: String = (new Date()).toDateString();
  newOrder: Orders = { orderStatus: "Pending" };
  getFoodSubscription:Subscription;
  getOrderSubscription:Subscription;

  @ViewChild("errorMessage") errorMessage: ElementRef;

  showOrderSuccessModal: boolean = false;

  constructor(
    private orderService: OrderService,
    private aRoute: ActivatedRoute,
    private foodService: FoodService,
    private router: Router
  ) { }
  
  ngOnInit(): void {
    this.foodId = +this.aRoute.snapshot.paramMap.get('id');
    this.getFoodSubscription= this.foodService.getFoodById(this.foodId).subscribe((food) => {
      this.foodToOrder = food;
      this.totalPrice = this.foodToOrder.price;
    });
  }

  cancelOrder() {
    this.router.navigate(['/userviewfood']);
  }
  
  confirmOrder() {
    this.newOrder.quantity = this.quantity;
    this.newOrder.totalAmount = this.totalPrice;
    this.newOrder.food = this.foodToOrder;
    
   this.getOrderSubscription= this.orderService.placeOrder(this.newOrder).subscribe(
      (order) => {
        this.showOrderSuccessModal = true;
      },
      (error) => {
        console.log(error);
      }
      );
    }
    
    closeModal() {
      this.showOrderSuccessModal = false;
      this.router.navigate(['/uservieworders']);
    }
    
    calculateTotalPrice() {
      if (this.quantity <= 0) {
        this.totalPrice = 0;
        this.errorMessage.nativeElement.innerHTML = "Enter valid quantity";
      } else if (this.quantity > this.foodToOrder.stockQuantity) {
        this.errorMessage.nativeElement.innerHTML = "Exceeds Stock limit";
      } else {
        this.totalPrice = this.quantity * this.foodToOrder.price;
        this.errorMessage.nativeElement.innerHTML = "";
      }
    }
    ngOnDestroy(): void {
      if(this.getFoodSubscription){
      
      this.getFoodSubscription.unsubscribe()
      
      }
      if(this.getOrderSubscription){
      this.getOrderSubscription.unsubscribe()
      }
    }
}