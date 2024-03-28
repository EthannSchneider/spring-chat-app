import { Injectable } from "@angular/core";

@Injectable({
providedIn: 'root'
})
export class TokenService {
    getToken() {
        return localStorage.getItem('token') ?? '';
    }

    isThereToken() {
        return this.getToken().includes('Bearer ');
    }

    logout() {
        localStorage.removeItem('token');
    }

    setToken(bearer_token: string) {
        localStorage.setItem('token', 'Bearer ' + bearer_token);
    }
}
