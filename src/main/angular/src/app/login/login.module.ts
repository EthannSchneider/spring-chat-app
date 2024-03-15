import { NgModule } from '@angular/core';
import { LoginComponent } from './login.component';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [ 
    LoginComponent 
  ],
  imports: [ 
    ReactiveFormsModule, 
    RouterModule.forChild([{ path: "", component: LoginComponent }]), 
  ]
})
export class LoginModule { }
