import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule, Title } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from "@angular/flex-layout";
import { TimeSinceModule } from '@thisissoon/angular-timesince';

import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';

import { MaterialModule } from './material.module';

import { authInterceptorProviders } from './helpers/auth.interceptor';
import { LoggedInAuthGuard } from './guards/loggedinauthguard.guard';
import { AuthGuard } from './guards/AuthGuard.guard';

import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';
import { RegisterComponent } from './components/register/register.component';
import { LoginComponent } from './components/login/login.component';
import { PostComponent } from './components/post/post.component';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { CommentsComponent } from './components/comments/comments.component';
import { RestoreComponent } from './components/restore/restore.component';
import { ExploreComponent } from './components/explore/explore.component';
import { HomepageComponent } from './components/homepage/homepage.component';
import { SharedModule } from './components/shared/shared.module';
import { UserpageModule } from './components/userpage/userpage.module';

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
    InfiniteScrollModule,
    SharedModule,
    UserpageModule
  ],
  declarations: [
    AppComponent,
    HeaderComponent,
    RegisterComponent,
    LoginComponent,
    PostComponent,
    CommentsComponent,
    RestoreComponent,
    ExploreComponent,
    HomepageComponent,
    PostComponent
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
