import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeadComponent } from './head/head.component';
import { PublicationsComponent } from './publications/publications.component';
import { FavoritesComponent } from './favorites/favorites.component';
import { RouterModule, Routes } from '@angular/router';
import { MaterialModule } from 'src/app/material.module';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { FlexLayoutModule } from '@angular/flex-layout';
import { UploadComponent } from './upload/upload.component';
import { ReactiveFormsModule } from '@angular/forms';

const routes: Routes = [
  { path: ':username', component: HeadComponent,
  children: [
      { path:':tabname', component: FavoritesComponent }
    ] 
  }
];

@NgModule({
  declarations: [
    HeadComponent,
    PublicationsComponent,
    FavoritesComponent,
    UploadComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    InfiniteScrollModule,
    FlexLayoutModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes)
  ],
  exports: [RouterModule],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class UserpageModule { }
