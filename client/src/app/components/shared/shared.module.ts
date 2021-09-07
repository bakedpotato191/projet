import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConfirmationDialogComponent } from './dialogs/confirmation-dialog/confirmation-dialog.component';
import { AvatarComponent } from './dialogs/avatar/avatar.component';
import { MaterialModule } from 'src/app/material.module';
import { FollowingComponent } from './dialogs/following/following.component';
import { FollowerComponent } from './dialogs/follower/follower.component';


@NgModule({
  declarations: [
    FollowingComponent,
    AvatarComponent,
    ConfirmationDialogComponent,
    FollowerComponent
  ],
  imports: [
    CommonModule,
    MaterialModule
  ]
})
export class SharedModule { }
