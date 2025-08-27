import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { AppRoutingModule } from "src/app/app-routing.module";
import { User } from 'src/app/models/user.model';

@Component({
  selector: 'app-adminnav',
  templateUrl: './adminnav.component.html',
  styleUrls: ['./adminnav.component.css']
})
export class AdminnavComponent implements OnInit {

  constructor(private authService:AuthService) { }

  ngOnInit(): void {
    this.username = this.authService.user.username;
  }


  username:string = "";

  menuOpen = false;
  dropdownOpen = false;

  toggleMenu() {
    this.menuOpen = !this.menuOpen;
    this.dropdownOpen = false;
  }

  closeMenu() {
    this.menuOpen = false;
    this.dropdownOpen = false;
  }

  toggleDropdown(event: Event) {
    event.preventDefault(); // prevent link jumping
    this.dropdownOpen = !this.dropdownOpen;
  }

  logout() {
    this.authService.logout();
    console.log('Admin logged out');
  }

}
