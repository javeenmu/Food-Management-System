import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from 'src/app/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private authService : AuthService , private router : Router){}
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      let expectedRole = route.data['role']; 
      let userRole = "";
      this.authService.role$.subscribe((role)=>{
        userRole=role;
      })
      if(this.authService.isLoggedIn() && userRole==expectedRole)
        return true;
      else
      this.router.navigate(['/error']);
  }
  
}
