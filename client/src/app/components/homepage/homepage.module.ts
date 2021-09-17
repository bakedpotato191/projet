import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomepageComponent } from './homepage.component';
import { RouterModule, Routes } from '@angular/router';
import { MaterialModule } from 'src/app/material.module';
import { FlexLayoutModule } from '@angular/flex-layout';
import { LoggedInAuthGuard } from 'src/app/guards/loggedinauthguard.guard';

const routes: Routes = [
  { path: '', component: HomepageComponent, canActivate: [LoggedInAuthGuard] }
];

@NgModule({
  declarations: [HomepageComponent],
  imports: [
    CommonModule,
    MaterialModule,
    FlexLayoutModule,
    RouterModule.forChild(routes)
  ],
  exports: [HomepageComponent]
})
export class HomepageModule { }
