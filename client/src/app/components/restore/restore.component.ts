import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-restore',
  templateUrl: './restore.component.html',
  styleUrls: ['./restore.component.css']
})
export class RestoreComponent implements OnInit {
  
  hide = true;
  isLoading = false;
  restoreForm!: FormGroup;
  sent = false;

  constructor(
              private authService: AuthService,
              private fb: FormBuilder) { }

  ngOnInit(): void {
    this.initLoginForm();
  }

  onSubmit(): void {
    this.authService.restore(this.restoreForm.value).subscribe(
      data => {
        this.sent = true;
      }
    );
  };

  initLoginForm() {
    this.restoreForm = this.fb.group({
      email: ["", [Validators.required, Validators.email]],
    });
  }

}
