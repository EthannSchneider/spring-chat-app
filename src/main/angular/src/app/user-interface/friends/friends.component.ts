import { Component, OnInit } from '@angular/core';
import { UserService } from '../../core/service/user.service';

@Component({
  selector: 'app-friends',
  templateUrl: './friends.component.html',
  styleUrls: ['./friends.component.scss']
})
export class FriendsComponent implements OnInit{
  friends: any[] = [];
  friendRequests: any[] = [];
  sentFriendRequests: any[] = [];

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.userService.getFriendList().subscribe((data: any) => {
      this.friends = data;
    });

    this.userService.getPendingRequestFriendList().subscribe((data: any) => {
      this.friendRequests = data;
    });

    this.userService.getSentPendingRequestFriendList().subscribe((data: any) => {
      this.sentFriendRequests = data
    });
  }

  acceptFriendRequest(friend: any) {
    this.userService.acceptFriend(friend.user.username).subscribe(() => {
      this.ngOnInit();
    });
  }

  rejectFriendRequest(friend: any) {
    this.userService.declineFriend(friend.user.username).subscribe(() => {
      this.ngOnInit();
    });
  }

  removeFriend(friend: any) {
    this.userService.removeFriend(friend.user.username).subscribe(() => {
      this.ngOnInit();
    });
  }
}
