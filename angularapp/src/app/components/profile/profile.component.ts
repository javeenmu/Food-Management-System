import { Component, OnInit } from '@angular/core';
import { Login } from 'src/app/models/login.model';
import { User } from 'src/app/models/user.model';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {


  user: User = {}

  editableUser: User = { ...this.user };
  showModal: boolean = false;
  emailErrorMessage: string = ''
  usernameErrorMessage: string = ''
  login:Login={
    email:sessionStorage.getItem('email')
  }
  constructor(private authService: AuthService) { }

  ngOnInit(): void {

    this.authService.getUserByEmail(this.login).subscribe((result)=>{
      this.user=result;
    })
  }

  openModal(): void {

    this.editableUser = { ...this.user };
    this.showModal = true;
  }

  closeModal(): void {
    this.emailErrorMessage = ''
    this.usernameErrorMessage = ''
    this.showModal = false;
  }

  updateProfile(): void {

    this.authService.updateProfile(+sessionStorage.getItem('userId'), this.editableUser).subscribe({
      next: (updatedUser) => {

        this.user = updatedUser;
        this.closeModal();


      },
      error: (err) => {
        console.log(err)
        if (err.error.errorMessage.includes("Email")) {
          this.emailErrorMessage = err.error.errorMessage;
        }
        else {
          this.usernameErrorMessage = err.error.errorMessage;
        }

      }
    });
  }
}