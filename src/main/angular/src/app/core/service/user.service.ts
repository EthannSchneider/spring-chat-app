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

  getUserByUsername(username: string) {
    const headers = new HttpHeaders().set('Authorization', this.tokenService.getToken());
    return this.http.get(
      `/api/user/${username}`,
      { headers: headers, responseType: 'json' }
    );
  }

  getFriendList() {
    const headers = new HttpHeaders().set('Authorization', this.tokenService.getToken());
    return this.http.get(
      '/api/user/friends',
      { headers: headers, responseType: 'json' }
    );
  }

  getPendingRequestFriendList() {
    const headers = new HttpHeaders().set('Authorization', this.tokenService.getToken());
    return this.http.get(
      '/api/user/friends/requests',
      { headers: headers, responseType: 'json' }
    );
  }

  getSentPendingRequestFriendList() {
    const headers = new HttpHeaders().set('Authorization', this.tokenService.getToken());
    return this.http.get(
      '/api/user/friends/requests/sent',
      { headers: headers, responseType: 'json' }
    );
  }

  addFriend(username: string) {
    const headers = new HttpHeaders().set('Authorization', this.tokenService.getToken());
    return this.http.get(
      `/api/user/${username}/friends`,
      { headers: headers, responseType: 'json' }
    );
  }

  acceptFriend(username: string) {
    const headers = new HttpHeaders().set('Authorization', this.tokenService.getToken());
    return this.http.get(
      `/api/user/${username}/friends/accept`,
      { headers: headers, responseType: 'json' }
    );
  }

  declineFriend(username: string) {
    const headers = new HttpHeaders().set('Authorization', this.tokenService.getToken());
    return this.http.get(
      `/api/user/${username}/friends/decline`,
      { headers: headers, responseType: 'json' }
    );
  }

  removeFriend(username: string) {
    const headers = new HttpHeaders().set('Authorization', this.tokenService.getToken());
    return this.http.delete(
      `/api/user/${username}/friends/remove`,
      { headers: headers, responseType: 'json' }
    );
  }
}
