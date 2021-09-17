import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { PublicationsComponent } from './publications.component';
import { MaterialModule } from 'src/app/material.module';
import { FlexLayoutModule } from '@angular/flex-layout';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';

const routes: Routes = [ 
  { path: '', component: PublicationsComponent } 
];

@NgModule({
  declarations: [
    PublicationsComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    FlexLayoutModule,
    InfiniteScrollModule,
    RouterModule.forChild(routes)
  ],
  exports: [
    PublicationsComponent
  ]
})
export class PublicationsModule { }
