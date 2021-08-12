import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Post } from 'src/app/class/post';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-userposts',
  templateUrl: './userposts.component.html',
  styleUrls: ['./userposts.component.css']
})
export class UserpostsComponent implements OnInit {

  posts: Post[] = [];

  constructor(private readonly router: Router,
              private readonly activatedRoute: ActivatedRoute,
              private readonly userService: UserService) { }

  ngOnInit(): void {
    const username = this.activatedRoute.snapshot.paramMap.get('username');
    if (username !== null) {
      this.getUserPosts(username);
  }
}

  openPostPage(id: number) {
    this.router.navigate(['p', id]);
  }

  getUserPosts(username: String) {
    this.userService.getUserPosts(username).subscribe(data => {
      this.posts = data;
    });
  }
}
