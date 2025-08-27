import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Orders } from 'src/app/models/orders.model';
import { OrderService } from 'src/app/services/order.service';

@Component({
  selector: 'app-adminvieworders',
  templateUrl: './adminvieworders.component.html',
  styleUrls: ['./adminvieworders.component.css']
})
export class AdminviewordersComponent implements OnInit, OnDestroy {

  getOrderSubscription: Subscription;
  updateOrderSubscription: Subscription;
  deleteOrderSubscription: Subscription;
  orders: Orders[] = [];
  showUserModal: boolean = false;
  selectedUser: any = null;
  isLoading: boolean = false;

  currentPage : number=0;
  totalPages : number;
  pageSize : number=2;

  constructor(private orderService: OrderService) { }
  ngOnDestroy(): void {
    if (this.getOrderSubscription) {
      this.getOrderSubscription.unsubscribe();
    }
    if (this.updateOrderSubscription) {
      this.updateOrderSubscription.unsubscribe();
    }
    if (this.deleteOrderSubscription) {
      this.deleteOrderSubscription.unsubscribe();
    }
  }

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders() {
    this.getOrderSubscription = this.orderService.getAllPaginatedOrders(this.currentPage , this.pageSize).subscribe((ordersList) => {
      this.orders = ordersList.content;
      this.totalPages=ordersList.totalPages;
      this.isLoading = true;
    },
      (error) => {
        this.isLoading = true;
      });
    this.isLoading = false;
  }

  changePage(pageNumber : number){
    this.currentPage=pageNumber;
    this.loadOrders();
  }

  changeStatus(order: Orders, status: string) {
    order.orderStatus = status;
    this.updateOrderSubscription = this.orderService.updateOrder(order.orderId, order).subscribe(
      (order) => {
        console.log("order status updated");
        console.log(order);
      },
      (error) => {
        console.log(error);
      }
    );
  }

  deleteOrder(order: Orders) {
    this.deleteOrderSubscription = this.orderService.deleteOrder(order.orderId).subscribe(
      (order) => {
        console.log("order deleted");
        console.log(order);
        this.ngOnInit();
      },
      (error) => {
        console.log(error);
      }
    );
  }



  openUserModal(order: any): void {
    this.selectedUser = order.user;
    this.showUserModal = true;
  }

  closeUserModal(): void {
    this.showUserModal = false;
  }
}

