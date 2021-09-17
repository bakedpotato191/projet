import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ExploreComponent } from './explore.component';
import { RouterModule, Routes } from '@angular/router';
import { MaterialModule } from 'src/app/material.module';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { AuthGuard } from 'src/app/guards/AuthGuard.guard';
import { TimeSinceModule } from '@thisissoon/angular-timesince';

const routes: Routes = [
  { path: '', component: ExploreComponent, canActivate:[AuthGuard] }
];

@NgModule({
  declarations: [ExploreComponent],
  imports: [
    CommonModule,
    MaterialModule,
    InfiniteScrollModule,
    TimeSinceModule,
    RouterModule.forChild(routes)
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ExploreModule { }
