import { NgClass } from '@angular/common';
import { Component } from '@angular/core';
import { NotificationService } from 'src/app/common-services';

@Component({
  selector: 'app-notification-modal',
  imports: [ NgClass ],
  templateUrl: './notification-modal.component.html',
  styleUrl: './notification-modal.component.css'
})
export class NotificationModalComponent {
  constructor(private vm: NotificationService) { }
  public get VM() { return this.vm; }
}
