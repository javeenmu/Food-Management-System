import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap, switchMap, map } from 'rxjs/operators';

import { User } from '../models/user.model';
import { Login } from '../models/login.model';
import { API_URL } from '../app.constant';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private roleSubject = new BehaviorSubject<string | null>(null);
  private userIdSubject = new BehaviorSubject<string | null>(null);
  private usernameSubject = new BehaviorSubject<string | null>(null);
  private userEmailSubject = new BehaviorSubject<string | null>(null);
  private userMobileNumber = new BehaviorSubject<string | null>(null);

  role$ = this.roleSubject.asObservable();
  userId$ = this.userIdSubject.asObservable();
  usernameSubject$=this.usernameSubject.asObservable();
  userEmailSubject$=this.userEmailSubject.asObservable();
  userMobileNumber$=this.userMobileNumber.asObservable();
  user: User = {};



  constructor(private client: HttpClient, private router: Router) {
    const storedRole = sessionStorage.getItem('role');
    const storedUserId = sessionStorage.getItem('userId');
    if (storedRole) this.roleSubject.next(storedRole);
    if (storedUserId) this.userIdSubject.next(storedUserId);
  }

  register(user: User): Observable<any> {
    console.log("Register Works");
    return this.client.post(`${API_URL}/register`, user);
  }

  login(login: Login): Observable<void> {
    return this.client.post<{ token: string }>(`${API_URL}/login`, login).pipe(
      tap(response => {
        if (response?.token) {
          sessionStorage.setItem('token', response.token);
        }
      }),
      switchMap(() => this.getUserByEmail(login)),
      tap((user: User) => {
        this.user = user;
        sessionStorage.setItem('role', user.userRole);
        sessionStorage.setItem('userId', user.userId?.toString());
        sessionStorage.setItem('email',user.email);
        this.roleSubject.next(user.userRole);
        this.userIdSubject.next(user.userId?.toString());
        this.usernameSubject.next(user.username);
        this.userEmailSubject.next(user.email);
        this.userMobileNumber.next(user.mobileNumber);
      }),
      map(() => { }) // return void
    );
  }

  getUserByEmail(login: Login): Observable<User> {
    return this.client.get<User>(`${API_URL}/email/${login.email}`);
  }

  getUserByEmaill(email:string): Observable<User> {
    return this.client.get<User>(`${API_URL}/email/${email}`);
  }

  logout(): void {
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('role');
    sessionStorage.removeItem('userId');
    this.roleSubject.next(null);
    this.userIdSubject.next(null);

    this.router.navigate(['/home']);
  }
  updateProfile(userId: number, user: User): Observable<User> {
    return this.client.put<User>(`${API_URL}/user/${userId}`, user);
  }

  updatePassword(user:User):Observable<User>{
    return this.client.put<User>(`${API_URL}/user/password/${user.userId}`, user);
  }

  isLoggedIn(): boolean {
    return !!sessionStorage.getItem('token');
  }
}