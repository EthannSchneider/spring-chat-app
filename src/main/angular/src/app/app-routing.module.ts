import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserInterfaceComponent } from './user-interface/user-interface.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'user-interface' },
  { path: 'user-interface', component: UserInterfaceComponent, loadChildren: () => import("./user-interface/user-interface.module").then((m) => m.DashboardModule) },
  { path: 'login', component: LoginComponent, loadChildren: () => import("./login/login.module").then((m) => m.LoginModule) },
  { path: 'register', component: RegisterComponent, loadChildren: () => import("./register/register.module").then((m) => m.RegisterModule) },
  { path: '**', redirectTo: 'user-interface' }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule { }
