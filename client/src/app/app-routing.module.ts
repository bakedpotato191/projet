import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { PostComponent } from './components/post/post.component';
import { RegisterComponent } from './components/register/register.component';
import { RestoreComponent } from './components/restore/restore.component';
import { LoggedInAuthGuard } from './guards/loggedinauthguard.guard';
import { HomepageComponent } from './components/homepage/homepage.component';
import { ExploreComponent } from './components/explore/explore.component';
import { AuthGuard } from './guards/AuthGuard.guard';

const routes: Routes = [
  { path: '', pathMatch: 'full', component:HomepageComponent, canActivate:[LoggedInAuthGuard] },
  { path: 'signup', component: RegisterComponent, canActivate:[LoggedInAuthGuard] },
  { path: 'login', component: LoginComponent, canActivate:[LoggedInAuthGuard] },
  { path: 'explore', component: ExploreComponent, canActivate: [AuthGuard] },
  { path: 'password_reset', redirectTo: 'password_reset/', pathMatch: 'full' },
  { path: 'password_reset/:token', component: RestoreComponent, canActivate:[LoggedInAuthGuard] },
  { path: 'p/:id', component: PostComponent },
  { path: 'profile', loadChildren: () => import('./components/userpage/userpage.module').then(m => m.UserpageModule)},
  { path: 'p/:id', component: PostComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
