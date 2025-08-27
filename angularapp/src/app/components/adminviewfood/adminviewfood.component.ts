
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';

import { Food } from 'src/app/models/food.model';
import { FoodService } from 'src/app/services/food.service';

@Component({
  selector: 'app-adminviewfood',
  templateUrl: './adminviewfood.component.html',
  styleUrls: ['./adminviewfood.component.css']
})
export class AdminviewfoodComponent implements OnInit, OnDestroy {

  getFoodSubscription:Subscription;
  deleteFoodSubscription:Subscription;
  foods: Food[] = []
  filteredFoods: Food[] = []
  searchText: string = ''
  minPrice: number = null;
  maxPrice: number = null;
  isLoading:boolean=false;
  foodRemoved : boolean = false;
  foodType: string;

  currentPage : number=0;
  totalPages : number;
  pageSize : number=2;
  // Modal control
  showDeleteModal: boolean = false;
  foodToDeleteId: number | null = null;
  constructor(private foodService: FoodService, private router: Router,private activatedRoute:ActivatedRoute) { }


  ngOnInit(): void {
    this.getAllFoods();
  }

  getAllFoods() {
    this.foodType = this.activatedRoute.snapshot.params['category'];
    if(!this.foodType){
      this.foodService.getPaginatedFoods(this.currentPage , this.pageSize).subscribe((result) => {
        this.foods = result.content;
        this.filteredFoods = result.content;
        this.totalPages=result.totalPages;
        this.isLoading=true;
        console.log(this.foods)
      },
      (error)=>{
        this.isLoading=true;
      })
      this.isLoading=false;
    }
    else{
      this.foodService.getPaginatedFoods(this.currentPage , this.pageSize).subscribe((result) => {
        this.foods = result.content;
        this.totalPages=result.totalPages;
        this.filterCategories()
      
        this.isLoading=true;
      },
      (error)=>{
        this.isLoading=true;
      })
      this.isLoading=false;
    }
    
  }

  changePage(pageNumber : number){
    this.currentPage=pageNumber;
    this.foodType="All";
    this.getAllFoods();
  }

  filterCategories(){
    console.log("added");
    this.filteredFoods = this.foods.filter(food=>food.tags.includes(this.foodType))
    console.log(this.filteredFoods);
  }

  searchFoods() {
    this.filteredFoods = this.foods.filter(food => {
      const matchesText = this.searchText === '' || food.foodName.toLowerCase().includes(this.searchText.toLowerCase());
      const matchesMin = this.minPrice === null || food.price >= this.minPrice;
      const matchesMax = this.maxPrice === null || food.price <= this.maxPrice;
      return matchesText && matchesMin && matchesMax;
    });
  }

  updateFood(id: number) {
    this.router.navigate(['/admineditfood', id]);
  }

  deleteFood(id: number) {
    this.foodToDeleteId = id;
    this.showDeleteModal = true;
  }

  closeDeleteModal() {
    this.showDeleteModal = false;
    this.foodToDeleteId = null;
  }

  confirmDelete() {
    if (this.foodToDeleteId !== null) {
      this.deleteFoodSubscription = this.foodService.deleteFood(this.foodToDeleteId).subscribe(() => {
        this.getAllFoods();
        this.closeDeleteModal();
      },(error)=>{
        if(error.error.errorMessage.includes("Out")){
          this.foodRemoved=true;
        }
      });
    }
  }

  ngOnDestroy():void{
    if(this.getFoodSubscription){
      this.getFoodSubscription.unsubscribe();
    }
    if(this.deleteFoodSubscription){
      this.deleteFoodSubscription.unsubscribe();
    }
  }
}