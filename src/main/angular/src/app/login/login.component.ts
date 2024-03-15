import { Component } from '@angular/core';
import { FormControl, UntypedFormGroup } from '@angular/forms';
import { AuthService } from '../core/service/auth.service';
import { LoginRequestForm } from '../core/dto/login-request-form';
import { TokenService } from '../core/service/token.service';
import { timeInterval, timeout } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  loginErrorMessages: string = ''
  loginForm: UntypedFormGroup = new UntypedFormGroup({
    username: new FormControl(''),
    password: new FormControl(''),
  });

  constructor(private authService: AuthService, private tokenService: TokenService) {
    
  }

  onSubmit() {
    this.tokenService.logout();
    this.authService.login(new LoginRequestForm(this.loginForm.value.username, this.loginForm.value.password)).subscribe({
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