import { NgModule } from '@angular/core';
import { LoginComponent } from './login.component';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { FormModule } from '../core/component/form/form.module';
import { CommonModule } from '@angular/common';

@NgModule({
  declarations: [ 
    LoginComponent 
  ],
  imports: [ 
    CommonModule, 
    RouterModule.forChild([{ path: "", component: LoginComponent }]), 
    FormModule
  ]
})
export class LoginModule { }
