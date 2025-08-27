import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from 'src/app/models/user.model';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  registerForm: FormGroup;
  showSuccessModal: boolean = false;
  errorMessage:string='';

  constructor(
    private authService: AuthService,
    private fb: FormBuilder,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.pattern("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@$!%*?&])[A-Za-z0-9@$!%*?&]{6,}$")]],
      confirmPassword: ['', Validators.required],
      username: ['', Validators.required],
      mobileNumber: ['', [Validators.required, Validators.pattern("^[0-9]{10}$")]],
      termsAndCondition: ['', [Validators.requiredTrue]],
      passwordQuestion:['', [Validators.required]]
    }, {
      validators: this.passwordMatchValidator
    });
  }

  passwordMatchValidator(form: FormGroup) {
    const password = form.get('password')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { passwordMismatch: true };
  }

  registerUser() {
    if (this.registerForm.valid) {
      const user: User = {
        ...this.registerForm.value,
        userRole: "USER"
      };
      console.log(user);
      this.authService.register(user).subscribe(result => {
        this.showSuccessModal = true;
        this.errorMessage=''
      }, (error)=>{
        this.errorMessage = error?.error?.errorMessage || 'Failed to register. Username already exists.';
          this.showSuccessModal = false;
      });
    }
  }

  closeModal() {
    this.showSuccessModal = false;
    this.router.navigate(['/login']);
  }
}