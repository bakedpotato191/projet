import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { SharedService } from 'src/app/services/shared.service';

@Component({
  selector: 'app-restore',
  templateUrl: './restore.component.html',
  styleUrls: ['./restore.component.css']
})
export class RestoreComponent implements OnInit {
  
  hide = true;
  restoreForm!: FormGroup;
  passwordForm!: FormGroup;
  sent = false;
  isLoading = false;
  isRequest = false;
  isInvalid = false;
  isVisible = false;
  updated = false;
  uuid!:string | null;

  constructor(private readonly route: ActivatedRoute,
              private readonly authService: AuthService,
              private readonly sharedService: SharedService,
              private fb: FormBuilder) { }

  ngOnInit(): void {
    this.uuid = this.route.snapshot.paramMap.get('token');
    
    if (this.uuid) {
      const json = { "token": this.uuid };
      this.authService.verifyToken(json).subscribe(
        _data => {
          this.isRequest = true;
          this.initPasswordsForm();
        },
        _error => {
          this.isInvalid = true;
          this.initLoginForm();
          return this.sharedService.showSnackbar("It looks like you clicked on an invalid password reset link. Please try again.", 'Dismiss', 0);
        }
      )
    }
    else {
      this.initLoginForm();
    }

  };

  initLoginForm() {
    this.restoreForm = this.fb.group({
      email: ["", [Validators.required, Validators.email, Validators.maxLength(128)]],
    });
  };

  initPasswordsForm() {
    this.passwordForm = this.fb.group({
      password: ["", [Validators.required, Validators.minLength(8), Validators.maxLength(32)]],
      passwordRepeat: ["", [Validators.required, Validators.minLength(8), Validators.maxLength(32)]],
      token: [this.uuid]
    })
  };

  submitEmail(): void {
    if (this.restoreForm.invalid) {
      return;
    }
    this.isLoading = true;
    setTimeout(() => {
      this.authService.restore(this.restoreForm.value).subscribe(
        _data=> {
          this.sent = true;
          this.isLoading = false;
        },
        error=> {
          return this.sharedService.showSnackbar(error.error, 'Dismiss', 0);
        }

      );
    }, 1000);

  };

  submitPasswords() {
    this.passwordForm.patchValue({
      token: this.uuid
    })
    this.authService.reset(this.passwordForm.value).subscribe(
      _data=> {
        this.updated = true;
      },
      error => {
        return this.sharedService.showSnackbar(error.error, 'Dismiss', 0);
      }
    )
  }
}
