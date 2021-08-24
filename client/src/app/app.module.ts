import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { BrowserModule, Title } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from "@angular/flex-layout";
import { TimeSinceModule } from '@thisissoon/angular-timesince';

import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';

import { AngularMaterialModule } from './angular-material.module';
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from '@angular/material/form-field';

import { authInterceptorProviders } from './helpers/auth.interceptor';
import { LoggedInAuthGuard } from './guards/loggedinauthguard.guard';
import { AuthGuard } from './guards/AuthGuard.guard';

import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';
import { RegisterComponent } from './components/register/register.component';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { PostComponent } from './components/post/post.component';
import { UploadComponent } from './components/upload/upload.component';
import { ConfirmationDialogComponent } from './components/dialogs/confirmation-dialog/confirmation-dialog.component';
import { FavoritesComponent } from './components/favorites/favorites.component';
import { MatTabNav } from '@angular/material/tabs';
import { UserpostsComponent } from './components/userposts/userposts.component';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { CommentsComponent } from './components/comments/comments.component';
import { RestoreComponent } from './components/restore/restore.component';

@NgModule({
  imports: [
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    FlexLayoutModule,
    AngularMaterialModule,
    TimeSinceModule,
    InfiniteScrollModule
  ],
  declarations: [
    AppComponent,
    HeaderComponent,
    RegisterComponent,
    LoginComponent,
    HomeComponent,
    PostComponent,
    UploadComponent,
    ConfirmationDialogComponent,
    FavoritesComponent,
    UserpostsComponent,
    CommentsComponent,
    RestoreComponent
  ],
  exports: [
    UploadComponent
  ],
  providers: [
    authInterceptorProviders, 
    LoggedInAuthGuard, 
    AuthGuard,
    Title,
    MatTabNav,
    { provide: MAT_FORM_FIELD_DEFAULT_OPTIONS, useValue: { appearance: 'outline' } }
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})

export class AppModule { }
