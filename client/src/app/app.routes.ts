import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
    { path: '', pathMatch: 'full', loadChildren: () => import('./components/homepage/homepage.module').then(m => m.HomepageModule) },
    { path: 'signup', loadChildren: () => import('./components/register/register.module').then(m => m.RegisterModule) },
    { path: 'login', loadChildren: () => import('./components/login/login.module').then(m => m.LoginModule)},
    { path: 'explore', loadChildren: () => import('./components/explore/explore.module').then(m => m.ExploreModule) },
    { path: 'password_reset', loadChildren: () => import('./components/restore/restore.module').then(m => m.RestoreModule) },
    { path: 'publication', loadChildren: () => import('./components/publication/publication.module').then(m => m.PublicationModule) },
    { path: 'profile', loadChildren: () => import('./components/userpage/userpage.module').then(m => m.UserpageModule)},
    { path: '**', pathMatch: 'full', loadChildren: () => import('./components/shared/notfound.module').then(m => m.NotFoundModule) }
  ];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }