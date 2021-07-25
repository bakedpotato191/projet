import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from 'src/app/class/user';
import { UserService } from 'src/app/services/user.service';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Post } from 'src/app/class/post';
import { UploadformService } from 'src/app/services/uploadform.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  cols!: number;
  mypage!: boolean;

  gridByBreakpoint = {
    xl: 4,
    lg: 4,
    md: 3,
    sm: 2,
    xs: 1
  }

  user!: User;
  post!: Post;

  constructor(private userService: UserService,
              private uploadService: UploadformService, 
              private route: ActivatedRoute, 
              private router:Router,
              private breakpointObserver: BreakpointObserver,
              private tokenService: TokenStorageService) {

    this.breakpointObserver.observe([
      Breakpoints.XSmall,
      Breakpoints.Small,
      Breakpoints.Medium,
      Breakpoints.Large,
      Breakpoints.XLarge,
    ]).subscribe(result => {
      if (result.matches) {
        if (result.breakpoints[Breakpoints.XSmall]) {
          this.cols = this.gridByBreakpoint.xs;
        }
        if (result.breakpoints[Breakpoints.Small]) {
          this.cols = this.gridByBreakpoint.sm;
        }
        if (result.breakpoints[Breakpoints.Medium]) {
          this.cols = this.gridByBreakpoint.md;
        }
        if (result.breakpoints[Breakpoints.Large]) {
          this.cols = this.gridByBreakpoint.lg;
        }
        if (result.breakpoints[Breakpoints.XLarge]) {
          this.cols = this.gridByBreakpoint.xl;
        }
      }
    });
  }

  ngOnInit(): void {
    const username = this.route.snapshot.paramMap.get('username');
    if (username !== null) {
      this.getUserData(username);
    }
    if (username == this.tokenService.getUser().username){
      this.mypage = true;
    }
  }

  getUserData(username: String) {
    this.userService.getUser(username).subscribe(data => {
      this.user = data;
    });
  }

  openDialog(): void {
    this.uploadService.open();
  }

  openPostPage(id: number){
    this.router.navigate(['p', id]);
  }

  subscribe (){

  }

  unsubscribe(){
    
  }
}