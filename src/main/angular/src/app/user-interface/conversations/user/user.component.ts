import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ConversationService } from '../../../core/service/conversation.service';
import { UserService } from '../../../core/service/user.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrl: './user.component.scss'
})
export class UserComponent {
  username: string = '';
  yourUsername: string = '';
  user: any = {};
  conversation: any = [];
  newMessage: string = '';
  intervalInfo: any;

  constructor(private route: ActivatedRoute, private conversationService: ConversationService, private userService: UserService) { }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.username = params['username'];
      this.userService.getUserByUsername(this.username).subscribe({
        next: user => {
          this.user = user;
          if (!this.conversationService.getConversation().includes(this.username)) {
            this.conversationService.addConversation(this.username);
          }
          this.userService.getMessages(this.username, 0).subscribe({
            next: conversation => {
              this.conversation = conversation;
              setTimeout( () => { this.scrollend(); }, 10 );
            },
            error: err => {
              this.error()
            }
          });
          this.userService.getUser().subscribe({
            next: user => {
              const body = user as { [key: string]: string };
              this.yourUsername = body['username'];
            },
            error: err => {
              this.error()
            }
          });
        },
        error: err => {
          this.error();
        }
      });
    });
    this.intervalInfo = setInterval(() => {
      this.userService.getMessages(this.username, 0).subscribe({
        next: conversations => {
          const conversationsItem = conversations as [any];
          if(conversationsItem.length <= 0) {
            return;
          }
          if (this.conversation[0].messageUUID != conversationsItem[0].messageUUID) {
            this.conversation = conversations;
            this.scrollend();
          }
        },
        error: err => {
          this.error();
        }
      });
    }, 3000);
  }

  sendMessage() {
    if (this.newMessage === '') {
      return;
    }
    this.userService.sendMessage(this.username, this.newMessage).subscribe({
      next: message => {
        this.conversation.splice(0, 0, message);
        this.newMessage = '';
        setTimeout( () => { this.scrollend(); }, 10 );
      },
      error: err => {
        this.error()
      }
    });
  }

  scrollend() {
    let message = document.getElementById("message") ?? { scrollTop: 0, scrollHeight: 0 };
    message.scrollTop = message.scrollHeight;
  }

  error() {
    if (this.conversationService.getConversation().includes(this.username)) {
      this.conversationService.removeConversation(this.username);
    }

    document.location.href = '/user-interface/conversations';
  }
}
