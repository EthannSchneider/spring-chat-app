import { NgModule } from '@angular/core';
import { RegisterComponent } from './register.component';
import { RouterModule } from '@angular/router';
import { FormModule } from '../core/component/form/form.module';
import { CommonModule } from '@angular/common';

@NgModule({
  declarations: [
    RegisterComponent
  ],
  imports: [
    RouterModule.forChild([{ path: "", component: RegisterComponent }]),
    FormModule 
  ]
})
export class RegisterModule { }
