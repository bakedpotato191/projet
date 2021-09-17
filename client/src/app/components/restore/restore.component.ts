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
    restoreForm!: FormGroup;
    passwordForm!: FormGroup;

    isSent: boolean = false;
    isHiden: boolean = true;
    isLoading: boolean = false;
    isRequest: boolean = false;
    isInvalid: boolean = false;
    isVisible: boolean = false;
    isUpdated: boolean = false;
    uuid!: string | null;

    constructor(
        private readonly route: ActivatedRoute,
        private readonly authService: AuthService,
        private readonly sharedService: SharedService,
        private fb: FormBuilder
    ) {}

    ngOnInit() {
        let uuid = this.route.snapshot.paramMap.get('token');

        if (uuid) {
            const json = { token: uuid };
            this.authService.verifyToken(json).subscribe({
                next: (_data) => {
                    this.isRequest = true;
                    this.init_password_form();
                },
                error: (e) => {
                    console.error(e);
                    this.isInvalid = true;
                    this.init_restore_form();
                    this.sharedService.showSnackbar(
                        'Il semble que vous ayez cliqué sur un lien de réinitialisation de mot de passe invalide. Veuillez réessayer.',
                        'Fermer',
                        0
                    );
                }
            }
            );
            this.sharedService.setTitle('Mis a jour de mot de passe');
        } else {
            this.sharedService.setTitle('Reinitialisation');
            this.init_restore_form();
        }
    }

    init_restore_form() {
        this.restoreForm = this.fb.group({
            email: [
                '',
                [Validators.required, Validators.email, Validators.maxLength(128)]
            ]
        });
    }

    init_password_form() {
        this.passwordForm = this.fb.group({
            password: [
                '',
                [Validators.required, Validators.minLength(8), Validators.maxLength(32)]
            ],
            passwordRepeat: [
                '',
                [Validators.required, Validators.minLength(8), Validators.maxLength(32)]
            ],
            token: [this.uuid]
        });
    }

    submit_email() {
        if (this.restoreForm.invalid) {
            return;
        }
        this.isLoading = true;
        this.authService.restore(this.restoreForm.value).subscribe({
        next: (_data) => this.isSent = true,
        error: (e) => {
                console.log(e);
                this.sharedService.showSnackbar(e.error, 'Dismiss', 0);
            }
        })
        .add(
            () => this.isLoading = false
        )
    }

    submit_new_password() {
        this.passwordForm.patchValue({
            token: this.uuid
        });
        this.authService.reset(this.passwordForm.value).subscribe({
        next: (_data) => this.isUpdated = true,
        error: (e) => {
                console.log(e);
                this.sharedService.showSnackbar(e.error, 'Dismiss', 0);
            }
        }
    );
    }
}
