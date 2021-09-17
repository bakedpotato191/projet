import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RestoreComponent } from './restore.component';
import { RouterModule, Routes } from '@angular/router';
import { MaterialModule } from 'src/app/material.module';
import { ReactiveFormsModule } from '@angular/forms';
import { LoggedInAuthGuard } from 'src/app/guards/loggedinauthguard.guard';

const routes: Routes = [
  { path: '', component: RestoreComponent, canActivate:[LoggedInAuthGuard] }
];

@NgModule({
  declarations: [RestoreComponent],
  imports: [
    CommonModule,
    MaterialModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes)
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RestoreModule { }
