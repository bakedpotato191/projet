import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

/* Routing */
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

/* Material Modules */
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AngularMaterialModule } from './angular-material.module';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatDialogModule } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';

/* Forms */
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

/* Angular Flex Layout */
import { FlexLayoutModule } from "@angular/flex-layout";

/* Components */
import { HeaderComponent } from './components/header/header.component';
import { RegisterComponent } from './components/register/register.component';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { PostComponent } from './components/post/post.component';
import { UploadComponent } from './components/upload/upload.component';

/* Helpers */
import { authInterceptorProviders } from './helpers/auth.interceptor';

/* Guards */
import { LoggedInAuthGuard } from './guards/loggedinauthguard.guard';
import { AuthGuard } from './guards/AuthGuard.guard';

/* ngx infinite scroll */
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { UploadformService } from './services/uploadform.service';

import { NgTimePastPipeModule } from 'ng-time-past-pipe';

@NgModule({
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    AngularMaterialModule,
    FormsModule,
    FlexLayoutModule,
    MatCardModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    InfiniteScrollModule,
    NgTimePastPipeModule
  ],
  declarations: [
    AppComponent,
    HeaderComponent,
    RegisterComponent,
    LoginComponent,
    HomeComponent,
    PostComponent,
    UploadComponent
  ],
  exports: [
    UploadComponent
  ],
  entryComponents: [UploadComponent],
  providers: [authInterceptorProviders, LoggedInAuthGuard, AuthGuard, UploadformService],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})

export class AppModule { }
