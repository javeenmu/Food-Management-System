import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Food } from '../models/food.model';
import { API_URL } from '../app.constant';

@Injectable({
  providedIn: 'root'
})
export class FoodService {

  constructor(private client:HttpClient) { }

  getAllFoods():Observable<any>{
    return this.client.get(`${API_URL}/food`);
  }
  
  getPaginatedFoods(page: number, size: number): Observable<any> {
    return this.client.get(`${API_URL}/food?page=${page}&size=${size}`);
  }


  getFoodById(foodId:number):Observable<Food>{
    return this.client.get(`${API_URL}/food/${foodId}`);
  }

  addFood(foodItem:Food):Observable<Food>{
    return this.client.post(`${API_URL}/food`,foodItem);
  }

  updateFood(foodId:number,foodItem:Food):Observable<Food>{
    return this.client.put(`${API_URL}/food/${foodId}`,foodItem);
  }
   
  deleteFood(foodId:number):Observable<any>{
    return this.client.delete(`${API_URL}/food/${foodId}`);
  }




}
