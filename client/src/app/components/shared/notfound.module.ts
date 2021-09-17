import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotfoundComponent } from './notfound/notfound.component';
import { RouterModule } from '@angular/router';
import { FlexLayoutModule } from '@angular/flex-layout';

@NgModule({
  declarations: [
    NotfoundComponent
  ],
  imports: [
    CommonModule,
    FlexLayoutModule,
    RouterModule.forChild([
      {
          path: '',
          component: NotfoundComponent
      }
  ])
  ],
  exports: [
    RouterModule
  ]
})
export class NotFoundModule { }
