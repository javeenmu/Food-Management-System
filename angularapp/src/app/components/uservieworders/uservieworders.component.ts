import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Food } from 'src/app/models/food.model';
import { Orders } from 'src/app/models/orders.model';
import { FoodService } from 'src/app/services/food.service';
import { OrderService } from 'src/app/services/order.service';

@Component({
  selector: 'app-uservieworders',
  templateUrl: './uservieworders.component.html',
  styleUrls: ['./uservieworders.component.css']
})
export class UserviewordersComponent implements OnInit, OnDestroy {

  getFoodSubscription: Subscription;
  getOrderSubscription: Subscription;
  deleteOrderSubscription: Subscription;
  showPopup: boolean = false;
  orderToDelete: number | null = null;
  orders: Orders[] = [];
  foods: Food[] = [];
  userId: number = 0;
  isLoading: boolean = false;

  currentPage : number=0;
  totalPages : number;
  pageSize : number=2;

  constructor(private orderService: OrderService, private foodService: FoodService, private router: Router) { }

  ngOnDestroy(): void {
    if (this.deleteOrderSubscription) {
      this.deleteOrderSubscription.unsubscribe();
    }
    if (this.getFoodSubscription) {
      this.getFoodSubscription.unsubscribe();
    }
    if (this.getOrderSubscription) {
      this.getOrderSubscription.unsubscribe();
    }
  }
  loadFoods() {
    this.getFoodSubscription = this.foodService.getAllFoods().subscribe((foodList) => {
      this.foods = foodList;
      this.isLoading = true;
    },
      (error) => {
        this.isLoading = true;
      })
    this.isLoading = false;
  }
  loadOrders() {
    this.getOrderSubscription = this.orderService.getAllPaginatedOrdersByUserId(this.userId,this.currentPage,this.pageSize).subscribe((ordersByUser) => {
      console.log(ordersByUser.content);
      this.orders = ordersByUser.content;
      this.totalPages=ordersByUser.totalPages;
      this.isLoading = true;
    },
      (error) => {
        this.isLoading = true;
      })
    this.isLoading = false;
  }
  feedback(orderId: number) {
    console.log(orderId)
    this.router.navigate(['useraddfeedback', orderId])

  }
  ngOnInit(): void {
    this.userId = +sessionStorage.getItem('userId');

    this.loadFoods();
    this.loadOrders();
  }
  confirmDelete(orderId: number): void {
    this.orderToDelete = orderId;
    this.showPopup = true;
  }

  deleteOrder(): void {
    this.deleteOrderSubscription = this.orderService.deleteOrder(this.orderToDelete).subscribe((result) => {
      console.log(result);
      this.ngOnInit();
      this.showPopup = false;
    }, (error) => {
      console.log(error);
    })
  }

  cancelDelete(): void {
    this.orderToDelete = null;
    this.showPopup = false;
  }

  changePage(pageNumber : number){
    this.currentPage=pageNumber;
    this.loadOrders();
  }
}


