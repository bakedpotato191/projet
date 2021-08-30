import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'src/app/services/auth.service';
import { SharedService } from 'src/app/services/shared.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  isHiden = true;
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
    this.sharedService.setTitle("Connexion");
    this.init_login_form();
    if (this.tokenStorage.getToken()) {
      this.isLoggedIn = true;
      this.roles = this.tokenStorage.getUser().roles;
    }
  }

  init_login_form(): void {
    this.loginForm = this.fb.group({
      email: ["", [Validators.required, Validators.email, Validators.maxLength(128)]],
      password: ["", [Validators.required, Validators.minLength(8), Validators.maxLength(32)]]
    });
  }

  submit(): void {
    if (this.loginForm.invalid) {
      return;
    }
    this.isLoading = true;
    this.authService.login(this.loginForm.value).subscribe(
      data => {
        this.tokenStorage.saveToken(data.accessToken);
        this.tokenStorage.saveUser(data);

        this.isLoginFailed = false;
        this.isLoggedIn = true;
        this.roles = this.tokenStorage.getUser().roles;
        window.location.reload();
      },
      error => {
        this.isLoginFailed = true;
        this.isLoading = false;
        if (error.status === 401) {
          this.sharedService.showSnackbar("Invalid email or password", 'Dismiss', 7000);
        }
      }
    );
  }
}


