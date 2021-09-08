import { Component } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: [
    './app.component.css']
})
export class AppComponent {
  
  isHidden: boolean = true;

  title: string = 'Infinity';

  constructor (private router: Router) {
    this.router.events.subscribe((event: any) => {
      if (event instanceof NavigationEnd) {
        if (event.url === '/login' || event.url === '/signup' || event.url === '/') {
          this.isHidden= true;
        } else {
          this.isHidden= false;
        }
      }
    });
  }
}
