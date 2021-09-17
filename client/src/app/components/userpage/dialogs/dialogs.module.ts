import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConfirmationDialogComponent } from './confirmation/confirmation-dialog.component';
import { AvatarComponent } from './avatar/avatar.component';
import { MaterialModule } from 'src/app/material.module';
import { FollowingComponent } from './followings/following.component';
import { FollowerComponent } from './followers/follower.component';
import { NotFoundModule } from '../../shared/notfound.module';

@NgModule({
  declarations: [
    FollowingComponent,
    AvatarComponent,
    ConfirmationDialogComponent,
    FollowerComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    NotFoundModule
  ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DialogsModule { }
