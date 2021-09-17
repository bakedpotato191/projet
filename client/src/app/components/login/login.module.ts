import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './login.component';
import { RouterModule, Routes } from '@angular/router';
import { MaterialModule } from 'src/app/material.module';
import { ReactiveFormsModule } from '@angular/forms';
import { LoggedInAuthGuard } from 'src/app/guards/loggedinauthguard.guard';

const routes: Routes = [
  { path: '', component: LoginComponent, canActivate:[LoggedInAuthGuard] }
];

@NgModule({
  declarations: [LoginComponent],
  imports: [
    CommonModule,
    MaterialModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes)
  ],

})
export class LoginModule { }
