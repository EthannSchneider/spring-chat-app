import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { LoginRequestForm } from '../dto/login-request-form';
import { TokenService } from './token.service';
import { RegisterRequestForm } from '../dto/register-request-form';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(
    private http: HttpClient,
    private tokenService: TokenService
  ) {}

  login(loginRequestForm: LoginRequestForm) {
    const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
    return this.http.post(
      '/api/auth/login',
      JSON.stringify(loginRequestForm),
      { headers: headers, responseType: 'json' }
    )
  }

  register(registerRequestForm: RegisterRequestForm) {
    const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
    return this.http.post(
      '/api/auth/register',
      JSON.stringify(registerRequestForm),
      { headers: headers, responseType: 'json' }
    )
  }

  logout() {
    const token = this.tokenService.getToken();
    return this.http.get(
      '/api/auth/logout',
      { responseType: 'json', headers: new HttpHeaders().set('Authorization', token) }
    )
  }
}
