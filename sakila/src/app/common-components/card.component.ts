import { Component, Input } from '@angular/core';

@Component({
    selector: 'app-card',
    template: `
    <div class="card" style="width: 18rem;">
      <div class="card-body">
        <h5 class="card-title">{{ header }}</h5>
        <div class="card-text"><ng-content><i>(sin contenido)</i></ng-content></div>
      </div>
    </div>
  `,
    standalone: true,
})
export class CardComponent {
    @Input({required: true}) header?: string;
}
