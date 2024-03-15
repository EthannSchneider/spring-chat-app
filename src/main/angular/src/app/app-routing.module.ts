import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserInterfaceComponent } from './user-interface/user-interface.component';
import { LoginComponent } from './login/login.component';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'user-interface' },
  { path: 'user-interface', component: UserInterfaceComponent, loadChildren: () => import("./user-interface/user-interface.module").then((m) => m.DashboardModule) },
  { path: 'login', component: LoginComponent, loadChildren: () => import("./login/login.module").then((m) => m.LoginModule) },
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule { }
