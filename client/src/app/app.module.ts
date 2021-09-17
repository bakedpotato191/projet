import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app.routes';
import { BrowserModule, Title } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from "@angular/flex-layout";
import { TimeSinceModule } from '@thisissoon/angular-timesince';

import { HttpClientModule } from '@angular/common/http';

import { MaterialModule } from './material.module';

import { authInterceptorProviders } from './helpers/auth.interceptor';
import { LoggedInAuthGuard } from './guards/loggedinauthguard.guard';
import { AuthGuard } from './guards/AuthGuard.guard';

import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';
import { HomepageComponent } from './components/homepage/homepage.component';
import { UserpageModule } from './components/userpage/userpage.module';
import { NotFoundModule } from './components/shared/notfound.module';
import { RouterModule } from '@angular/router';
import { RestoreModule } from './components/restore/restore.module';
import { LoginModule } from './components/login/login.module';
import { RegisterModule } from './components/register/register.module';
import { ExploreModule } from './components/explore/explore.module';

@NgModule({
  imports: [
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    FlexLayoutModule,
    MaterialModule,
    TimeSinceModule,
    UserpageModule,
    NotFoundModule,
    RestoreModule,
    LoginModule,
    RegisterModule,
    ExploreModule
  ],
  declarations: [
    AppComponent,
    HeaderComponent,
    HomepageComponent,
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
