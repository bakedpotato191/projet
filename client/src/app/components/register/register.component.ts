import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { AuthService } from 'src/app/services/auth.service';
import { SnackBarService } from 'src/app/services/snackbar.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit {
  registrationForm!: FormGroup;
  isHidden: boolean = true;
  isSuccessful: boolean = false;
  isSignUpFailed: boolean = false;
  errorMessage: string = '';

  constructor(
    private authService: AuthService,
    private sbService: SnackBarService,
    private readonly titleService: Title,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.titleService.setTitle('Inscription');
    this.init_signup_form();
  }

  init_signup_form() {
    this.registrationForm = this.fb.group({
      nom: [
        '',
        Validators.required,
        Validators.minLength(2),
        Validators.maxLength(16),
      ],
      prenom: [
        '',
        Validators.required,
        Validators.minLength(2),
        Validators.maxLength(16),
      ],
      username: [
        '',
        Validators.required,
        Validators.minLength(4),
        Validators.maxLength(20),
      ],
      email: [
        '',
        [Validators.required, Validators.email, Validators.maxLength(128)],
      ],
      password: [
        '',
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(32),
      ],
    });
  }

  submit() {
    if (this.registrationForm.invalid) {
      return;
    }

    this.authService.register(this.registrationForm.value).subscribe({
      next: (_data) => {
        this.isSuccessful = true;
        this.isSignUpFailed = false;
      },
      error: (e) => {
        console.error(e);
        if (e.status === 409) {
          this.sbService.showSnackbar(
            "Le nom d'utilisateur est déjà utilisé",
            'Dismiss',
            7000
          );
          this.registrationForm.controls['username'].setErrors({
            incorrect: true,
          });
        }
        this.isSignUpFailed = true;
      },
    });
  }
}
