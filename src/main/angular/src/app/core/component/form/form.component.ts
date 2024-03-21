import { Component, Input, OnInit, input } from '@angular/core';
import { FormControl, UntypedFormGroup } from '@angular/forms';

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrl: './form.component.scss'
})
export class FormComponent implements OnInit {
  @Input() errorMessages: string = ''
  @Input() title: string = '';
  @Input() listForm: string[][] = [];
  @Input() callback: Function = (value: any) => {};
  @Input() moreButton:  string[][] = [];
  forms: UntypedFormGroup = new UntypedFormGroup({});

  ngOnInit() {
    this.listForm.forEach((item: string[]) => {
      this.forms.addControl(item[1], new FormControl(''));
    });
  }

  redirect(url: string) {
    window.location.href = url
  }

  onSubmit() {
    this.callback.call(this, this.forms.value);
  }
}