/* eslint-disable @angular-eslint/component-selector */
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'my-sizer',
  standalone: true,
  template: `
    <div [style.font-size.px]="size">
      <button (click)="dec()">-</button>
      <output>FontSize: {{size}}px</output>
      <button (click)="inc()">+</button>
    </div>
  `,
  host: { 'role': 'slider', '[attr.aria-valuenow]': 'size' }
})
export class SizerComponent {
  @Input()  size: number | string = 12;
  @Output() sizeChange = new EventEmitter<number>();

  dec() : void { this.resize(-1); }
  inc() : void { this.resize(+1); }

  resize(delta: number) : void {
    this.size = Math.min(40, Math.max(8, +this.size + delta));
    this.sizeChange.emit(this.size);
  }
}
