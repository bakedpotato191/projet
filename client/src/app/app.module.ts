import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { BrowserModule, Title } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from "@angular/flex-layout";
import { TimeSinceModule } from '@thisissoon/angular-timesince';

import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';

import { AngularMaterialModule } from './angular-material.module';

import { authInterceptorProviders } from './helpers/auth.interceptor';
import { LoggedInAuthGuard } from './guards/loggedinauthguard.guard';
import { AuthGuard } from './guards/AuthGuard.guard';

import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';
import { RegisterComponent } from './components/register/register.component';
import { LoginComponent } from './components/login/login.component';
import { PostComponent } from './components/post/post.component';
import { UploadComponent } from './components/upload/upload.component';
import { ConfirmationDialogComponent } from './components/dialogs/confirmation-dialog/confirmation-dialog.component';
import { FavoritesComponent } from './components/favorites/favorites.component';
import { UserpostsComponent } from './components/userposts/userposts.component';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { CommentsComponent } from './components/comments/comments.component';
import { RestoreComponent } from './components/restore/restore.component';
import { FollowingComponent } from './components/dialogs/following/following.component';
import { AvatarComponent } from './components/dialogs/avatar/avatar.component';
import { ExploreComponent } from './components/explore/explore.component';
import { HomepageComponent } from './components/homepage/homepage.component';
import { UserPageComponent } from './components/userpage/userpage.component';

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
    UserPageComponent,
    PostComponent,
    UploadComponent,
    ConfirmationDialogComponent,
    FavoritesComponent,
    UserpostsComponent,
    CommentsComponent,
    RestoreComponent,
    FollowingComponent,
    AvatarComponent,
    ExploreComponent,
    HomepageComponent
  ],
  exports: [
    UploadComponent
  ],
  providers: [
    authInterceptorProviders, 
    LoggedInAuthGuard, 
    AuthGuard,
    Title
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})

export class AppModule { }
