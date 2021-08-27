import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FavoritesComponent } from './components/favorites/favorites.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { PostComponent } from './components/post/post.component';
import { RegisterComponent } from './components/register/register.component';
import { RestoreComponent } from './components/restore/restore.component';
import { LoggedInAuthGuard } from './guards/loggedinauthguard.guard';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'login' },
  { path: 'signup', component: RegisterComponent, canActivate:[LoggedInAuthGuard] },
  { path: 'login', component: LoginComponent, canActivate:[LoggedInAuthGuard] },
  { path: 'password_reset', redirectTo: 'password_reset/', pathMatch: 'full'},
  { path: 'password_reset/:token', component: RestoreComponent },
  { path: 'profile', redirectTo:'/login', pathMatch:'full' },
  { path: 'profile/:username', component:HomeComponent,
  children: [
      { path:':tabname', component: FavoritesComponent }
    ] 
  },
  { path: 'p/:id', component: PostComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
