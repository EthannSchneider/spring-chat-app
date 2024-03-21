import { Component } from '@angular/core';
import { AuthService } from '../core/service/auth.service';
import { RegisterRequestForm } from '../core/dto/register-request-form';
import { TokenService } from '../core/service/token.service';
import { Regex } from '../core/utils/regex';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  registerErrorMessages: string = "";
  listFormRegister: string[][] = [
    ['text', 'username'],
    ['text', 'email'],
    ['text', 'phonenumber'],
    ['text', 'firstname'],
    ['text', 'lastname'],
    ['password', 'password'],
    ['password', 'retype_password']
  ];
  moreButtonRegister: string[][] = [
    ['/login', 'Login']
  ];

  constructor(private authService: AuthService, private tokenService: TokenService) {}

  register = (value: any) => {
    this.onSubmit(value);
  }

  onSubmit(value: any) {
    if (!(value.password && value.retype_password && value.username && value.email && value.phonenumber && value.firstname && value.lastname)) {
      return;
    }
    if (value.password !== value.retype_password) {
      this.registerErrorMessages = "passwords do not match!";
      return;
    }
    if (value.firstname.length <= 2 || value.firstname.length >= 50 || value.lastname.length <= 2 || value.lastname.length >= 50) {
      this.registerErrorMessages = "firstname and lastname must be at least 2 characters long and max 50 characters long!";
      return;
    }
    if(!Regex.emailRegex.test(value.email)) {
      this.registerErrorMessages = "invalid email!";
      return;
    }
    if(!Regex.phoneNumberRegex.test(value.phonenumber)) {
      this.registerErrorMessages = "invalid phone number!";
      return;
    }
    const registerRequestForm = new RegisterRequestForm(value.username, value.email, value.phonenumber, value.firstname, value.lastname, value.password);
    this.authService.register(registerRequestForm).subscribe({
      next: response => {
        const body = response as { [key: string]: string };
        this.tokenService.setToken(body['token']);
        window.location.href = '/';
      },
      error: err => {
        this.registerErrorMessages = "name or email already taken!";
      }
    });
  }
}
