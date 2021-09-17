import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app.routes';
import { BrowserModule, Title } from '@angular/platform-browser';

import { HttpClientModule } from '@angular/common/http';

import { authInterceptorProviders } from './helpers/auth.interceptor';
import { LoggedInAuthGuard } from './guards/loggedinauthguard.guard';
import { AuthGuard } from './guards/AuthGuard.guard';

import { AppComponent } from './app.component';
import { UserpageModule } from './components/userpage/userpage.module';
import { NotFoundModule } from './components/shared/notfound.module';
import { RouterModule } from '@angular/router';
import { RestoreModule } from './components/restore/restore.module';
import { LoginModule } from './components/login/login.module';
import { RegisterModule } from './components/register/register.module';
import { ExploreModule } from './components/explore/explore.module';
import { HeaderModule } from './components/header/header.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@NgModule({
  imports: [
    AppRoutingModule,
    HttpClientModule,
    BrowserModule,
    BrowserAnimationsModule,
    UserpageModule,
    NotFoundModule,
    RestoreModule,
    LoginModule,
    RegisterModule,
    ExploreModule,
    HeaderModule
  ],
  declarations: [
    AppComponent
  ],
  exports: [
    RouterModule
  ],
  providers: [
    authInterceptorProviders, 
    LoggedInAuthGuard, 
    AuthGuard,
    Title
  ],
  bootstrap: [AppComponent]
})

export class AppModule { }
