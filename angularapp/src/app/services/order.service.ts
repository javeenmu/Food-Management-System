import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Orders } from '../models/orders.model';
import { Observable } from 'rxjs';
import { API_URL } from '../app.constant';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  constructor(private client:HttpClient) { }
  
  placeOrder(order:Orders):Observable<Orders>{
    return this.client.post(`${API_URL}/orders`,order);
  }

  getAllOrders():Observable<any>{
    return this.client.get(`${API_URL}/orders/all`);
  }

  //Pagination 
  getAllPaginatedOrders(page : number , size: number):Observable<any>{
    return this.client.get(`${API_URL}/orders?page=${page}&size=${size}`);
  }

  getOrdersById(orderId:number):Observable<Orders>{
    return this.client.get(`${API_URL}/orders/${orderId}`)
  }
  
  getAllOrdersByUserId(userId:number):Observable<any>{
    return this.client.get(`${API_URL}/orders/user/${userId}`)
  }

  //Pagination
  getAllPaginatedOrdersByUserId(userId:number,page : number , size: number):Observable<any>{
    return this.client.get(`${API_URL}/orders/user/${userId}?page=${page}&size=${size}`)
  }

  updateOrder(orderId:number,order:Orders):Observable<Orders>{
    return this.client.put(`${API_URL}/orders/${orderId}`,order)
  }

  deleteOrder(orderId: number):Observable<void>{
    return this.client.delete<void>(`${API_URL}/orders/${orderId}`)
  }
}
