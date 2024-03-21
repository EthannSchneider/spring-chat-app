import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { UserInterfaceComponent } from './user-interface.component';
import { FriendsComponent } from './friends/friends.component';

const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full'},
  { path: '', component: UserInterfaceComponent},
  { path: 'dashboard', component: DashboardComponent },
  { path: 'friends', component: FriendsComponent },
];

@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ],
})
export class DashboardRoutingModule { }
