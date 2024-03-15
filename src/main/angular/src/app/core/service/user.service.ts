import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { LoginRequestForm } from '../dto/login-request-form';
import { NotAuthenticated } from '../exception/NotAuthenticated';
import { TokenService } from './token.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(
    private http: HttpClient,
    private tokenService: TokenService
  ) {}

  getUser() {
    if (!this.tokenService.isThereToken()){
      throw new NotAuthenticated();
    }
    const headers = new HttpHeaders().set('Authorization', this.tokenService.getToken());
    return this.http.get(
      '/api/user',
      { headers: headers, responseType: 'json' }
    );
  }
}
