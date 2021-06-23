import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { PostComponent } from './components/post/post.component';
import { RegisterComponent } from './components/register/register.component';
import { AuthGuard } from './guards/AuthGuard.guard';
import { LoggedInAuthGuard } from './guards/loggedinauthguard.guard';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'login' },
  { path: 'signup', component: RegisterComponent, canActivate:[LoggedInAuthGuard] },
  { path: 'login', component: LoginComponent, canActivate:[LoggedInAuthGuard] },
  { path: 'profile/:username', component:HomeComponent, canActivate: [AuthGuard] },
  { path: 'p/:id', component: PostComponent, canActivate: [AuthGuard] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
