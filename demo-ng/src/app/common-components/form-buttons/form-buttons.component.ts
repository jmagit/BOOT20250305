/* eslint-disable @typescript-eslint/no-inferrable-types */
/* eslint-disable @angular-eslint/no-input-rename */
import { Component, EventEmitter, input, Output } from '@angular/core';

@Component({
    selector: 'app-form-buttons',
    templateUrl: './form-buttons.component.html',
    styleUrls: ['./form-buttons.component.css'],
    imports: []
})
export class FormButtonsComponent {
  readonly sendDisabled = input<boolean | null>(false, { alias: "send-disabled" });
  readonly sendText = input<string>('enviar', { alias: "send-text" });
  readonly abandonText = input<string>('volver', { alias: "abandon-text" });
  readonly deleteText = input<string>('borrar', { alias: "delete-text" });
  readonly deleteVisible = input<boolean>(true, { alias: "delete-visible" });
  @Output() send: EventEmitter<null> = new EventEmitter<null>();
  @Output() abandon: EventEmitter<null> = new EventEmitter<null>();
  @Output() delete: EventEmitter<null> = new EventEmitter<null>();

  get hasSend(): boolean { return this.send.observed; }
  get hasAbandon(): boolean { return this.abandon.observed; }
  get hasDelete(): boolean { return this.delete.observed && this.deleteVisible(); }
}

