import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-restore',
  templateUrl: './restore.component.html',
  styleUrls: ['./restore.component.css']
})
export class RestoreComponent implements OnInit {
  
  hide = true;
  isLoading = false;
  restoreForm!: FormGroup;

  constructor(private fb: FormBuilder) { }

  ngOnInit(): void {
    this.initLoginForm();
  }

  onSubmit() {};

  initLoginForm() {
    this.restoreForm = this.fb.group({
      email: ["", [Validators.required, Validators.email]],
    });
  }

}
