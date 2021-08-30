import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { UserService } from 'src/app/services/user.service';

import { TokenStorageService } from 'src/app/services/token-storage.service';
import { MatTabChangeEvent } from '@angular/material/tabs';
import { SharedService } from 'src/app/services/shared.service';
import { UploadComponent } from '../upload/upload.component';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { LoginComponent } from '../login/login.component';
import { FormGroup } from '@angular/forms';
import { Subscription } from 'rxjs';
import { FollowingComponent } from '../dialogs/following/following.component';
import { AvatarComponent } from '../dialogs/avatar/avatar.component';
import { UserpostsComponent } from '../userposts/userposts.component';
import { User } from 'src/app/interfaces/user';
import { Post } from 'src/app/interfaces/post';

@Component({
    selector: 'app-userpage',
    templateUrl: './userpage.component.html',
    styleUrls: ['./userpage.component.css']
})
export class UserPageComponent implements OnInit {
    @ViewChild('input') input!: ElementRef;
    @ViewChild(UserpostsComponent) userposts: any;

    isMyPage!: boolean;
    username!: string;
    isContent: boolean = false;
    user!: User;
    posts!: Post[];

    uploadRef!: MatDialogRef<UploadComponent>;
    loginRef!: MatDialogRef<LoginComponent>;
    avatarRef!: MatDialogRef<AvatarComponent>;

    avatarForm!: FormGroup;
    fileData!: Blob;
    imgFile!: string;

    subscription!: Subscription;

    isOverlayed!: boolean;

    selectedIndex!: number;

    ngOnInit(): void {
        this.activatedRoute.paramMap.subscribe((param) => {
            this.username = param.get('username')!;
            this.sharedService.setTitle('@' + this.username);
            this.get_user_data(this.username);
            this.isMyPage = this.username == this.tokenService.getUser().username;
        });
        this.activatedRoute.firstChild?.paramMap.subscribe((params) => {
            const tab = params.get('tabname');
            this.selectedIndex = tab == 'favorites' ? 1 : 0;
        });
    }

    constructor(
        private userService: UserService,
        private readonly activatedRoute: ActivatedRoute,
        private readonly router: Router,
        private readonly tokenService: TokenStorageService,
        private readonly sharedService: SharedService,
        public dialog: MatDialog
    ) {}

    get_user_data(username: string): void {
        this.userService.getUser(username).subscribe(
            (data) => {
                this.user = data;
                this.isContent = true;
            },
            (_error) => {
                this.isContent = true;
            }
        );
    }

    open_upload_dialog(): void {
        this.uploadRef = this.dialog.open(UploadComponent, {
            data: this.userposts,
            width: '500px',
            autoFocus: false
        });

        this.uploadRef.afterClosed().subscribe((result) => {
            if (result) {
                this.userposts.get_latest_post();
            }
        });
    }

    follow() {
        if (this.tokenService.getToken() == null) {
            this.dialog.open(LoginComponent);
        }
        this.user.followed = true;
        this.userService.follow(this.user.username).subscribe();
    }

    unfollow() {
        if (this.tokenService.getToken() == null) {
            return this.dialog.open(LoginComponent);
        }
        this.user.followed = false;
        return this.userService.unfollow(this.user.username).subscribe();
    }

    on_tab_change(event: MatTabChangeEvent): void {
        switch (event.index) {
            case 0:
                this.router.navigate(['profile/' + this.username]);
                break;
            case 1:
                this.router.navigate(['favorites'], { relativeTo: this.activatedRoute });
                break;
            default:
                break;
        }
    }

    on_file_change(e: any) {
        if (e.target.files && e.target.files.length) {
            this.fileData = e.target.files[0];
        }

        if (this.avatarRef !== undefined) {
            this.avatarRef.close();
        }

        this.submit_avatar();
    }

    submit_avatar() {
        var formData: any = new FormData();
        formData.append('avatar', this.fileData);
        this.isOverlayed = true;
        setTimeout(() => {
            this.userService
                .setProfilePicture(formData)
                .subscribe(
                    (data) => {
                        this.user.avatar = data.avatar;
                        this.user.has_avatar = data.has_avatar;
                    },
                    (error) => {
                        this.sharedService.showSnackbar(
                            'La demande a échoué avec le statut http ' + error.status,
                            'Dismiss',
                            7000
                        );
                    }
                )
                .add(() => {
                    this.isOverlayed = false;
                });
        }, 1000);
    }

    open_subscriptions_dialog(): void {
        this.dialog.open(FollowingComponent, {
            height: '450px',
            width: '500px',
            data: this.username,
            panelClass: ['dialog-window-style']
        });
    }

    on_avatar_click(): void {
        if (!this.isMyPage) {
            return;
        }

        if (this.user.has_avatar) {
            this.avatarRef = this.dialog.open(AvatarComponent, {
                width: '400px',
                panelClass: ['dialog-window-style'],
                data: this.input,
                autoFocus: false
            });
            this.avatarRef.afterClosed().subscribe((result) => {
                if (result === 'delete') {
                    this.isOverlayed = true;
                    setTimeout(() => {
                        this.userService
                            .deleteProfilePicture()
                            .subscribe(
                                (data) => {
                                    this.user.has_avatar = data.has_avatar;
                                    this.user.avatar = data.avatar;
                                },
                                (error) => {
                                    this.sharedService.showSnackbar(
                                        'La requete DELETE a échoué avec le code statut' +
                                            error.status,
                                        'Dismiss',
                                        7000
                                    );
                                }
                            )
                            .add(() => {
                                this.isOverlayed = false;
                            });
                    }, 1000);
                }
            });
        } else {
            this.input.nativeElement.click();
        }
    }
}
