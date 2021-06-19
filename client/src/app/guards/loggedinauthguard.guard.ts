import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { TokenStorageService } from '../services/token-storage.service';

@Injectable({
  providedIn: 'root'
})
export class LoggedInAuthGuard implements CanActivate {
  
  constructor(private authService: TokenStorageService, private router: Router) { }

  canActivate(): boolean {
      if (this.authService.getToken() !== null) {
        const user = this.authService.getUser();
        this.router.navigate([`/profile/${user.email}`]);
        return false
    } else {
        return true
    }
  }
}
