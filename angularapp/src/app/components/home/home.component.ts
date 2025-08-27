import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, OnDestroy {

  constructor(private router:Router, private authService:AuthService) { }
  ngOnDestroy(): void {
    this.roleSubscription.unsubscribe();
  }

  userRole:string = "";
  roleSubscription:Subscription;

  ngOnInit(): void {
    this.roleSubscription = this.authService.role$.subscribe(data=>{
      this.userRole = data;
    })
  }


  orderNow(){
    if(this.userRole=="ADMIN"){
      this.router.navigate(['/adminviewfood']);
    }
    if(this.userRole == "USER"){
      this.router.navigate(['/userviewfood']);
    }
    if(!this.userRole){
      this.router.navigate(['/login']);
    }
  }

  categoryNavigate(category:string){
  
    if(this.userRole=="ADMIN"){
      this.router.navigate(['/adminviewfood',category]);
    }
    else{
      this.router.navigate(["/userviewfood",category]);
    }
  }


}
