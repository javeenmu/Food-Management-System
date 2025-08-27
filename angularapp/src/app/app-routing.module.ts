import { NgModule } from '@angular/core';
import { Routes, RouterModule, ExtraOptions } from '@angular/router';
import { RegistrationComponent } from './components/registration/registration.component';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { AdminorderschartComponent } from './components/adminorderschart/adminorderschart.component';
import { AdminviewfeedbackComponent } from './components/adminviewfeedback/adminviewfeedback.component';
import { AdminviewfoodComponent } from './components/adminviewfood/adminviewfood.component';
import { AdminviewordersComponent } from './components/adminvieworders/adminvieworders.component';
import { UseraddfeedbackComponent } from './components/useraddfeedback/useraddfeedback.component';
import { AdminnavComponent } from './components/adminnav/adminnav.component';
import { UserviewfoodComponent } from './components/userviewfood/userviewfood.component';
import { AdminaddfoodComponent } from './components/adminaddfood/adminaddfood.component';
import { UsermakeorderComponent } from './components/usermakeorder/usermakeorder.component';
import { UserviewordersComponent } from './components/uservieworders/uservieworders.component';
import { UserviewfeedbackComponent } from './components/userviewfeedback/userviewfeedback.component';
import { ErrorComponent } from './components/error/error.component';
import { AuthGuard } from './components/authguard/auth.guard';
import { ProfileComponent } from './components/profile/profile.component';

const routes: Routes = [
  {path:"register", component:RegistrationComponent},
  {path:"login", component:LoginComponent},
  {path:"home", component:HomeComponent},
  {path:"adminorderschart", component:AdminorderschartComponent , canActivate : [AuthGuard], data : {'role':'ADMIN'} },
  {path:"adminaddfood", component:AdminaddfoodComponent,canActivate : [AuthGuard], data : {'role':'ADMIN'} },
  {path:"adminviewfeedback", component:AdminviewfeedbackComponent , canActivate : [AuthGuard], data : {'role':'ADMIN'} },
  {path:"adminviewfood", component:AdminviewfoodComponent , canActivate : [AuthGuard], data : {'role':'ADMIN'} },
  {path:"adminvieworders", component:AdminviewordersComponent , canActivate : [AuthGuard], data : {'role':'ADMIN'} },
  {path:"userviewfood", component:UserviewfoodComponent , canActivate : [AuthGuard], data : {'role':'USER'} },
  {path:"userviewfood/:category", component:UserviewfoodComponent , canActivate : [AuthGuard], data : {'role':'USER'} },
  {path:"adminviewfood/:category", component:AdminviewfoodComponent , canActivate : [AuthGuard], data : {'role':'ADMIN'} },
  {path:"useraddfeedback",component:UseraddfeedbackComponent , canActivate : [AuthGuard], data : {'role':'USER'}},
  {path:"adminnav", component:AdminnavComponent},
  {path:"useraddfeedback/:id",component:UseraddfeedbackComponent , canActivate : [AuthGuard], data : {'role':'USER'}},
  {path:"uservieworders" , component:UserviewordersComponent , canActivate : [AuthGuard], data : {'role':'USER'}},
  {path:"usermakeorder/:id", component:UsermakeorderComponent , canActivate : [AuthGuard], data : {'role':'USER'}},
  {path:"userviewfeedback", component:UserviewfeedbackComponent , canActivate : [AuthGuard], data : {'role':'USER'}},
  {path:"adminsorderschart",component:AdminorderschartComponent, canActivate : [AuthGuard], data : {'role':'ADMIN'}},
  {path:"error", component:ErrorComponent},
  {path: "profile", component:ProfileComponent},
  {path:"admineditfood/:id", component:AdminaddfoodComponent},
  {path:"", redirectTo:"home", pathMatch:"full"}
];


const routerOptions: ExtraOptions = {
  scrollPositionRestoration: 'enabled', // restores scroll position
  anchorScrolling: 'enabled',           // enables anchor scrolling
  scrollOffset: [0, 64],                // optional: offset for fixed headers
};

@NgModule({
  imports: [RouterModule.forRoot(routes, routerOptions)],
  exports: [RouterModule]
})
export class AppRoutingModule { }