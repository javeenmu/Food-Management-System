import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AdminaddfoodComponent } from './components/adminaddfood/adminaddfood.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { UseraddfeedbackComponent } from './components/useraddfeedback/useraddfeedback.component';
import { UsermakeorderComponent } from './components/usermakeorder/usermakeorder.component';
import { LoginComponent } from './components/login/login.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { UsernavComponent } from './components/usernav/usernav.component';
import { UserviewfeedbackComponent } from './components/userviewfeedback/userviewfeedback.component';
import { UserviewfoodComponent } from './components/userviewfood/userviewfood.component';
import { UserviewordersComponent } from './components/uservieworders/uservieworders.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { AdminnavComponent } from './components/adminnav/adminnav.component';
import { AdminviewordersComponent } from './components/adminvieworders/adminvieworders.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AdminviewfoodComponent } from './components/adminviewfood/adminviewfood.component';
import { AdminviewfeedbackComponent } from './components/adminviewfeedback/adminviewfeedback.component';
import { AdminorderschartComponent } from './components/adminorderschart/adminorderschart.component';
import { ProfileComponent } from './components/profile/profile.component';
import { ChartsModule } from 'ng2-charts';
@NgModule({
  declarations: [
    AppComponent,
    AdminaddfoodComponent,
    AdminnavComponent,
    AdminviewfoodComponent,
    AdminorderschartComponent,
    AdminviewordersComponent,
    RegistrationComponent,
    UseraddfeedbackComponent,
    UsermakeorderComponent,
    LoginComponent,
    NavbarComponent,
    UsernavComponent,
    UserviewfeedbackComponent,
    UserviewfoodComponent,
    UserviewordersComponent,
    AdminviewfeedbackComponent,
    AdminorderschartComponent,
    ProfileComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    ChartsModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
