import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'src/app/services/auth.service';
import { SharedService } from 'src/app/services/shared.service';

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
    registrationForm!: FormGroup;

    isHidden: boolean = true;
    isSuccessful: boolean = false;
    isSignUpFailed: boolean = false;
    errorMessage: string = '';

    constructor(
        private authService: AuthService,
        private sharedService: SharedService,
        private fb: FormBuilder
    ) {}

    ngOnInit(): void {
        this.sharedService.setTitle('Inscription');
        this.init_signup_form();
    }

    init_signup_form() {
        this.registrationForm = this.fb.group({
            nom: ['', Validators.required],
            prenom: ['', Validators.required],
            username: ['', Validators.required],
            email: ['', [Validators.required, Validators.email]],
            password: ['', Validators.required]
        });
    }

    submit(): void {
        if (this.registrationForm.invalid) {
            return;
        }

        this.authService.register(this.registrationForm.value).subscribe(
            (data) => {
                this.isSuccessful = true;
                this.isSignUpFailed = false;
            },
            (error) => {
                if (error.status === 409) {
                    this.sharedService.showSnackbar(
                        JSON.stringify('Username is already in use'),
                        'Dismiss',
                        7000
                    );
                    this.registrationForm.controls['username'].setErrors({
                        incorrect: true
                    });
                }
                this.isSignUpFailed = true;
            }
        );
    }
}
