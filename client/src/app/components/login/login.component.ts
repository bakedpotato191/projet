import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { AuthService } from 'src/app/services/auth.service';
import { SnackBarService } from 'src/app/services/snackbar.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  isHiden = true;
  loginForm!: FormGroup;
  isLoggedIn = false;
  isLoading!: boolean;
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private tokenStorage: TokenStorageService,
    private sbService: SnackBarService,
    private readonly titleService: Title,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.titleService.setTitle('Connexion');
    this.init_login_form();
  }

  init_login_form(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email, Validators.maxLength(128)]],
      password: ['', [ Validators.required, Validators.minLength(8), Validators.maxLength(32) ]],
    });
  }

  submit() {
    if (this.loginForm.invalid) {
      return;
    }
    this.isLoading = true;
    this.authService.login(this.loginForm.value).subscribe({
      next: (data) => {
        this.tokenStorage.saveToken(data.accessToken);
        this.tokenStorage.saveUser(data);
        this.isLoggedIn = true;
        window.location.reload();
      },
      error: (e) => {
        console.error(e);
        if (e.status === 401) {
          this.sbService.showSnackbar(
            'Email et/ou mot de passe incorrect(s)',
            'Dismiss',
            7000
          );
        }
      },
    }).add(() => this.isLoading = false);
  }
}
