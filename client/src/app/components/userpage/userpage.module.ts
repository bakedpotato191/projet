import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { UserpageComponent } from './userpage.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MaterialModule } from 'src/app/material.module';
import { UploadComponent } from './dialogs/upload/upload.component';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { ReactiveFormsModule } from '@angular/forms';
import { DialogsModule } from './dialogs/dialogs.module';
import { FavoritesModule } from './favorites/favorites.module';
import { NotFoundModule } from './../shared/notfound.module';
import { PublicationsModule } from './publications/publications.module';

const routes: Routes = [
  { path: ':username', component: UserpageComponent,
  children: [
      { path: 'favorites', loadChildren: () => import('./favorites/favorites.module').then((m) => m.FavoritesModule) }
    ] 
  }
];

@NgModule({
  declarations: [
    UserpageComponent,
    UploadComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    InfiniteScrollModule,
    FlexLayoutModule,
    ReactiveFormsModule,
    DialogsModule,
    FavoritesModule,
    PublicationsModule,
    NotFoundModule,
    RouterModule.forChild(routes)
  ],
  exports: [RouterModule]
})
export class UserpageModule { }
