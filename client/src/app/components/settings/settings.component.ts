import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  editForm!: FormGroup;

  constructor(private fb: FormBuilder) { }

  ngOnInit(): void {
    this.init_edit_form();
  }

  init_edit_form(): void {
    this.editForm = this.fb.group({
      prenom: ["", [Validators.required, Validators.minLength(2)]],
      nom: ["", [Validators.required, Validators.minLength(2)]]
    });
  }

}
