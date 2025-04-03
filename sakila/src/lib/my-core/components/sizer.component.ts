/* eslint-disable @angular-eslint/component-selector */
import { Component, input, model } from '@angular/core';

@Component({
  selector: 'my-sizer',
  standalone: true,
  template: `
    <div [style.font-size.px]="size()">
      <button (click)="dec()">-</button>
      <output [style.margin-left.px]="5" [style.margin-right.px]="5">FontSize: {{size()}}px</output>
      <button (click)="inc()">+</button>
    </div>
  `,
  host: {
    'role': 'slider',
    '[attr.aria-valuenow]': 'size()',
    'attr.aria-valuemin': '8',
    'attr.aria-valuemax': '40',
    'tabIndex': '0',
    '(keydown)': 'handleKeyDown($event)',
    'style': 'display: inline-block;',
  }
})
export class SizerComponent {
  readonly step = input(1, {transform: (value: number):number => Math.abs(value) ?? 1});
  readonly size = model<number>(12);

  dec() : void { this.resize(-this.step()); }
  inc() : void { this.resize(+this.step()); }

  resize(delta: number) : void { this.size.update(value => Math.min(40, Math.max(8, value + delta)));  }

  handleKeyDown(ev: KeyboardEvent) {
    switch (ev.key) {
      case '-': case 'ArrowLeft': case 'ArrowDown': ev.preventDefault(); this.dec();  break;
      case '+': case 'ArrowRight': case 'ArrowUp': ev.preventDefault(); this.inc();  break;
   }
  }
}
