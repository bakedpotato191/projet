import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomepageComponent } from './components/homepage/homepage.component';
import { RestoreComponent } from './components/restore/restore.component';
import { LoggedInAuthGuard } from './guards/loggedinauthguard.guard';

const routes: Routes = [
    { path: '', pathMatch: 'full', component:HomepageComponent, canActivate:[LoggedInAuthGuard] },
    { path: 'signup', loadChildren: () => import('./components/register/register.module').then(m => m.RegisterModule) },
    { path: 'login', loadChildren: () => import('./components/login/login.module').then(m => m.LoginModule)},
    { path: 'explore', loadChildren: () => import('./components/explore/explore.module').then(m => m.ExploreModule) },
    { path: 'password_reset', redirectTo: 'password_reset/', pathMatch: 'full' },
    { path: 'password_reset/:token', component: RestoreComponent, canActivate:[LoggedInAuthGuard] },
    { path: 'publication', loadChildren: () => import('./components/publication/publication.module').then(m => m.PublicationModule) },
    { path: 'profile', loadChildren: () => import('./components/userpage/userpage.module').then(m => m.UserpageModule)},
    { path: '**', pathMatch: 'full', loadChildren: () => import('./components/shared/notfound.module').then(m => m.NotFoundModule) }
  ];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }