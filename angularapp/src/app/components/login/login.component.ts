import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from 'src/app/models/user.model';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  forgotPassword:boolean=false;
  email:string='';
  securityAnswer:string='';
  errorMessage:string='';
  errorFlag:string='';
  user:User={};
  newPassword:string='';
  loginForm: FormGroup;
  loginError: boolean = false;

  constructor(
    private authService: AuthService,
    private fb: FormBuilder,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
    });

    this.loginForm.valueChanges.subscribe(() => {
      this.loginError = false;
    });
  }

  login() {
    if (this.loginForm.invalid) return;

    const credentials = this.loginForm.value;

    this.authService.login(credentials).subscribe({
      next: () => {
        this.loginError = false;
        this.router.navigate(['/home']);
      },
      error: () => {
        this.loginError = true;
      }
    });
  }

  forgotPasswordEnable(){
    this.forgotPassword=true;
  }

  resetPassword(){
    this.authService.getUserByEmaill(this.email).subscribe((result)=>{
      this.user= result;
      if(this.user.passwordQuestion==this.securityAnswer){
        this.user.password=this.newPassword
        this.authService.updatePassword(this.user).subscribe((result)=>{
          alert("Password changes successfully")
          this.forgotPassword=false;
          this.loginError=false;
        }, (error)=>{
          this.loginError=true;
        })
      }
      else{
        alert("Your answer is wrong")
      }
    })
    
  }

}