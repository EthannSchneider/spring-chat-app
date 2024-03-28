import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { UserInterfaceComponent } from './user-interface.component';
import { FriendsComponent } from './friends/friends.component';
import { ConversationsComponent } from './conversations/conversations.component';
import { UserComponent } from './conversations/user/user.component';

const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full'},
  { path: '', component: UserInterfaceComponent},
  { path: 'dashboard', component: DashboardComponent },
  { path: 'friends', component: FriendsComponent },
  { path: 'conversations', component: ConversationsComponent },
  { path: 'conversations/:username', component: UserComponent },
];

@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ],
})
export class DashboardRoutingModule { }
