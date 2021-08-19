import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'src/app/services/auth.service';
import { SharedService } from 'src/app/services/shared.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  hide = true;
  loginForm!: FormGroup;

  isLoggedIn = false;
  isLoginFailed = false;
  isLoading!: boolean;
  errorMessage = '';
  roles: string[] = [];

  constructor(private authService: AuthService, 
              private tokenStorage: TokenStorageService, 
              private sharedService: SharedService,
              private fb: FormBuilder) { }

  ngOnInit(): void {
    this.initLoginForm();
    if (this.tokenStorage.getToken()) {
      this.isLoggedIn = true;
      console.log(this.isLoggedIn);
      this.roles = this.tokenStorage.getUser().roles;
    }
  }

  initLoginForm() {
    this.loginForm = this.fb.group({
      email: ["", [Validators.required, Validators.email]],
      password: ["", Validators.required]
    });
  }

  onSubmit(): void {
    this.isLoading = true;
    this.authService.login(this.loginForm.value).subscribe(
      data => {
        this.tokenStorage.saveToken(data.accessToken);
        this.tokenStorage.saveUser(data);

        this.isLoginFailed = false;
        this.isLoggedIn = true;
        this.roles = this.tokenStorage.getUser().roles;
        this.sharedService.reloadPage();
      },
      error => {
        this.isLoginFailed = true;
        this.isLoading = false;
        if (error.status === 401) {
          return this.sharedService.showSnackbar("Invalid email or password", 'Dismiss', 7000);
        }
      }
    );
  }
}


