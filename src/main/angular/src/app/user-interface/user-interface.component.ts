import { Component, OnInit } from '@angular/core';
import { UserService } from '../core/service/user.service';
import { AuthService } from '../core/service/auth.service';

@Component({
  selector: 'app-userpage',
  templateUrl: './user-interface.component.html',
  styleUrls: ['./user-interface.component.scss']
})
export class UserInterfaceComponent implements OnInit {
  username: string = '';
  name: string = '';
  lastname: string = '';
  isAdmin: boolean = false;

  constructor(private userService: UserService, private authService: AuthService) {}

  ngOnInit(): void {
    try {
      this.userService.getUser().subscribe({
        next: user => {
          const body = user as { [key: string]: string };
          this.username = body["username"] ?? '';
          this.name = body["firstName"] ?? '';
          this.lastname = body["lastName"] ?? '';
        },
        error: err => {
          console.log(err);
          this.logout();
        }
      });
    }catch(err) {
      console.log(err);
      this.logout();
    }
  }

  logout() {
    this.authService.logout().subscribe({
      next: () => { },
      error: err => {
        console.log(err);
      }
    });
    window.location.href = '/login';
  }
}
