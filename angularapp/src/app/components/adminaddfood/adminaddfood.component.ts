import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Food } from 'src/app/models/food.model';
import { FoodService } from 'src/app/services/food.service';

@Component({
  selector: 'app-adminaddfood',
  templateUrl: './adminaddfood.component.html',
  styleUrls: ['./adminaddfood.component.css']
})
export class AdminaddfoodComponent implements OnInit, OnDestroy {
  
  addFoodSubscription:Subscription;
  updateFoodSubscription:Subscription;
  foodTags: string[] = ["Veg","Non-Veg","Breakfast","Lunch","Dinner","Snack","Dessert","Spicy","Non-Spicy","Indian","Chinese","Continental"];
  editFlag:boolean=false;
  foodId:number=null;
  photo:string=''
  addFoodForm:FormGroup
  food:Food= this.initialize();
  showSuccessModal:boolean=false;
  imagePreview:SafeUrl|null = null;
  errorMessage:string;
  selectedTags: string[] = [];



  constructor(private formBuilder:FormBuilder, private foodService:FoodService, private sanitizer:DomSanitizer, private activedRoute:ActivatedRoute, private router:Router) { }
  

  initialize():Food{
    return {foodName:'',price:null, stockQuantity:null, photo:''}
  }

  ngOnInit(): void {
    this.addFoodForm = this.formBuilder.group({
      foodName:['', [Validators.required]],
      price:[null, [Validators.required, Validators.min(0)]],
      stockQuantity:[null, [Validators.required, Validators.min(0)]]
    })
    this.foodId = +this.activedRoute.snapshot.params['id']
    this.foodService.getFoodById(this.foodId).subscribe((result)=>{
      this.food=result;
      this.imagePreview = result.photo;
      this.photo= result.photo;
      this.editFlag=true;
      this.selectedTags =result.tags.split(" ")
    })
    if(!this.foodId){
      this.food = this.initialize();
      this.editFlag=false;
    }
    this.showSuccessModal=false;
  }

  cancel(){
    this.editFlag=false;
    this.router.navigate(['/adminviewfood'])
  }
  onSubmit(): void {
    if(!this.editFlag){
      if (this.addFoodForm.valid) {
        const formValues = this.addFoodForm.value;
    
        this.food = {
          foodName: formValues.foodName,
          price: formValues.price,
          stockQuantity: formValues.stockQuantity,
          photo: this.photo,
          tags:this.selectedTags.join(" ") + " "
        };
        console.log(this.food)
  
        this.addFoodSubscription = this.foodService.addFood(this.food).subscribe((value)=>{
          console.log('Food item added:', this.food);
          this.showSuccessModal = true;
          this.imagePreview=null;
          this.food.photo = '';
          this.food=this.initialize();
          this.errorMessage=''
          this.selectedTags=[]
          this.photo=null
          this.addFoodForm.reset();
        }, (error)=>{
          this.errorMessage = error?.error?.errorMessage || 'Failed to add food item.';
          this.showSuccessModal = false;
        })
      } else {
        console.log('Form is invalid');
        
      }
    }
    else{
      if (this.addFoodForm.valid) {
        const formValues = this.addFoodForm.value;
    
        this.food = {
          foodName: formValues.foodName,
          price: formValues.price,
          stockQuantity: formValues.stockQuantity,
          photo: this.photo,
          tags:this.selectedTags.join(" ") + " "
        };
  
        this.updateFoodSubscription = this.foodService.updateFood(this.foodId,this.food).subscribe((value)=>{
          console.log('Food item added:', this.food);
          this.showSuccessModal = true;
          // Reset form and image
          this.addFoodForm.reset();
          // this.imagePreview = null;
          this.food.photo = '';
          this.editFlag=false;
          this.selectedTags=[]
          this.router.navigate(['/adminviewfood'])
        })
      } else {
        console.log('Form is invalid');
      }
    }
    
  }

  toggleTag(tag: string) {
    const index = this.selectedTags.indexOf(tag);
    if (index > -1) {
      this.selectedTags.splice(index, 1); // Remove tag
    } else {
      this.selectedTags.push(tag); // Add tag
    }
  }

  onImagePicked(event:Event):void{
    const input = event.target as HTMLInputElement;
    if(input.files && input.files.length>0){
      const file = input.files[0];
      // if(!file.type.startsWith('image/')){

      // }
      const reader = new FileReader();
      reader.onload = () =>{
        const base64String = reader.result as string;
        this.photo = base64String;
        console.log(this.photo);
        this.imagePreview = this.sanitizer.bypassSecurityTrustUrl(base64String);
      }
      reader.readAsDataURL(file);
    }
  }

  closeModal(){
    this.showSuccessModal=false;
  }

  ngOnDestroy(): void {
    if(this.addFoodSubscription){
      console.log("Unsubscribing add Food Subscription");
      this.addFoodSubscription.unsubscribe();
    }
    if(this.updateFoodSubscription){
      console.log("Unsubscribing update food subscription");
      this.updateFoodSubscription.unsubscribe();
    }
  }
  



}