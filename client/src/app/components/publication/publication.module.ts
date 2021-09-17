import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { ContentComponent } from './content/content.component';
import { CommentsComponent } from './comments/comments.component';
import { MaterialModule } from 'src/app/material.module';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { FlexLayoutModule } from '@angular/flex-layout';
import { TimeSinceModule } from '@thisissoon/angular-timesince';
import { ReactiveFormsModule } from '@angular/forms';
import { NotFoundModule } from '../shared/notfound.module';

const routes: Routes = [
  { path: ':id', component: ContentComponent }
];

@NgModule({
  declarations: [
    ContentComponent,
    CommentsComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    InfiniteScrollModule,
    FlexLayoutModule,
    TimeSinceModule,
    ReactiveFormsModule,
    NotFoundModule,
    RouterModule.forChild(routes)
  ]
})
export class PublicationModule { }
