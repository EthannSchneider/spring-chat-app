import { Component, Input, OnInit } from '@angular/core';
import { UserService } from '../../core/service/user.service';
import { ConversationService } from '../../core/service/conversation.service';

@Component({
  selector: 'app-conversations',
  templateUrl: './conversations.component.html',
  styleUrl: './conversations.component.scss'
})
export class ConversationsComponent implements OnInit {
[x: string]: any;
  conversations: string[] = [];
  conversationsLastMessage: Map<string, any> = new Map<string, any>();
  conversationsUser: Map<string, any> = new Map<string, any>();
  search: string = "";
  constructor(private userService: UserService, private conversationService: ConversationService) { }

  ngOnInit(): void {
    this.conversations = this.conversationService.getConversation();
    this.conversations.forEach((username: string) => {
      this.getLastMessage(username);
      this.getUser(username);
    });
  }

  getLastMessage(username: string) {
    this.userService.getMessages(username, 0).subscribe({
      next: (messages: any) => {
        this.conversationsLastMessage.set(username, messages[0]);
      },
      error: (error) => {
        console.log(error);
      }
    });
  }

  getUser(username: string) {
    this.userService.getUserByUsername(username).subscribe({
      next: (user: any) => {
        this.conversationsUser.set(username, user);
      },
      error: (error) => {
        console.log(error);
      }
    });
  }

  createConversation() {
    document.location.href = "/user-interface/conversations/" + this.search
  }

  convertTimestampToDate(date: string) {
    const monthname: string[] = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
    let dates = new Date(date);
    return dates.getDay() + " " + monthname[dates.getMonth() - 1] + " " + dates.getFullYear();
  }

  convertTimestampToTime(date: string) {
    let dates = new Date(date);
    return dates.getHours() + ":" + dates.getMinutes();
  }
}
