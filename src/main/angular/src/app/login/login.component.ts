import { Component } from '@angular/core';
import { FormControl, UntypedFormGroup } from '@angular/forms';
import { AuthService } from '../core/service/auth.service';
import { LoginRequestForm } from '../core/dto/login-request-form';
import { TokenService } from '../core/service/token.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  listFormRegister: string[][] = [
    ['text', 'username'],
    ['password', 'password'],
  ];
  login = (value: any) =>{
    this.onSubmit(value);
  };
  moreButtonLogin: string[][] = [
    ['/register', 'Register']
  ];

  loginErrorMessages: string = "";

  constructor(private authService: AuthService, private tokenService: TokenService) {
    
  }
 
  onSubmit(value: any) {
    console.log(value);
    this.tokenService.logout();
    this.authService.login(new LoginRequestForm(value.username, value.password)).subscribe({
      next: token => {
        const body = token as { [key: string]: string };
        this.tokenService.setToken(body['token']);
        window.location.href = '/'
      },
      error: err => {
        this.loginErrorMessages = "Invalid username or password. Please try again."
      }
    });
  }
}