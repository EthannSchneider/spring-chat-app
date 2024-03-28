import { NgModule } from '@angular/core';
import { UserInterfaceComponent } from './user-interface.component';
import { DashboardRoutingModule } from './user-interface-routing.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { CommonModule } from '@angular/common';
import {MatIconModule} from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatDialogModule} from '@angular/material/dialog';
import {MatSelectModule} from '@angular/material/select';
import { FriendsComponent } from './friends/friends.component';
import { ConversationsComponent } from './conversations/conversations.component';
import { UserComponent } from './conversations/user/user.component';

@NgModule({
  declarations: [
    UserInterfaceComponent,
    DashboardComponent,
    FriendsComponent,
    ConversationsComponent,
    UserComponent
  ],
  imports: [
    DashboardRoutingModule,
    CommonModule,
    MatIconModule,
    FormsModule, 
    MatFormFieldModule, 
    MatInputModule,
    MatButtonModule, 
    MatDividerModule,
    FormsModule,
    MatCheckboxModule,
    MatDialogModule,
    MatFormFieldModule, 
    MatInputModule, 
    MatSelectModule,
    ReactiveFormsModule
  ],
})
export class DashboardModule { }
