import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Food } from 'src/app/models/food.model';
import { FoodService } from 'src/app/services/food.service';

@Component({
  selector: 'app-userviewfood',
  templateUrl: './userviewfood.component.html',
  styleUrls: ['./userviewfood.component.css']
})
export class UserviewfoodComponent implements OnInit{
  constructor(private foodService:FoodService, private router:Router, private activatedRoute:ActivatedRoute) { }
  
  foods: Food[] = [];
  filteredFoods: Food[] = [];
  searchText: string = ''
  minPrice: number = null;
  maxPrice: number = null;
  isLoading: boolean = false;
  foodType: string;

  getFoodSubscription:Subscription;
  currentPage: number = 0;
  pageSize: number = 2;
  totalPages: number;
  getAllFoods() {

    this.foodType = this.activatedRoute.snapshot.params['category'];
    if(!this.foodType){
     this.getFoodSubscription =  this.foodService.getPaginatedFoods(this.currentPage , this.pageSize).subscribe((result) => {
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
      this.getFoodSubscription = this.foodService.getPaginatedFoods(this.currentPage , this.pageSize).subscribe((result) => {
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


  order(food: Food) {
    this.router.navigate(['/usermakeorder', food.foodId]);
  }

  filterCategories(){
    this.filteredFoods = this.foods.filter(food=>food.tags.includes(this.foodType))
  }

  searchFoods(event?:Event) {
    if(!event){

      this.filteredFoods = this.foods.filter(food => {
        const matchesText = this.searchText === '' || food.foodName.toLowerCase().includes(this.searchText.toLowerCase());
        const matchesMin = this.minPrice === null || food.price >= this.minPrice;
        const matchesMax = this.maxPrice === null || food.price <= this.maxPrice;
        return matchesText && matchesMin && matchesMax;
      });
    }
    else {
      let tag = (event.target as HTMLInputElement).value
      if (tag == "All") {
        this.filteredFoods = this.foods
      }
      else {
        if (tag == "Veg") {
          this.filteredFoods = this.foods.filter(food => food.tags.includes(tag) && !food.tags.includes("Non-Veg"))
        }
        else {
          this.filteredFoods = this.foods.filter(food => food.tags.includes(tag))
        }

      }
    }


  }

  ngOnInit(): void {
    this.getAllFoods();
  }

  ngOnDestroy(): void {
    if(this.getFoodSubscription){
      this.getFoodSubscription.unsubscribe();
    }
  }


}
