import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  constructor () { }

  ngOnInit(): void {
  }

  registerForm = new FormGroup(
    {
      firstname: new FormControl(""),
      lastname: new FormControl(""),
      email: new FormControl(""),
      mobile: new FormControl(""),
      gender: new FormControl(""),
      pwd: new FormControl(""),
      rpwd: new FormControl("")
    }
  ); // It will represents group of elements

  registerSubmitted() {
    console.log(this.registerForm.value);
  }


}
