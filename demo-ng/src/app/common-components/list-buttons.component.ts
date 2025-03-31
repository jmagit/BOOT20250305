/* eslint-disable @angular-eslint/no-input-rename */
import { Component, EventEmitter, input, Output } from '@angular/core';

@Component({
    selector: 'app-list-buttons',
    template: `
    <div class="btn-group" role="group">
      @if (hasView) {
        <button class="btn btn-info" (click)="view.emit(null)" title="Ver"><i class="fas fa-eye"></i></button>
      }
      @if (hasEdit) {
        <button class="btn btn-success" (click)="edit.emit(null)" title="Editar"><i class="fas fa-pen"></i></button>
      }
      @if (hasDelete) {
        <button class="btn btn-danger" (click)="confirmDelete()" title="Borrar"><i class="far fa-trash-alt"></i></button>
      }
    </div>
  `,
    imports: []
})
export class ListButtonsComponent {
  readonly canView = input(true, { alias: "can-view" });
  readonly canEdit = input(true, { alias: "can-edit" });
  readonly canDelete = input(true, { alias: "can-delete" });
  readonly confirmDeleteMsg = input('', { alias: "confirm-delete-message" });
  @Output() view: EventEmitter<null> = new EventEmitter<null>();
  @Output() edit: EventEmitter<null> = new EventEmitter<null>();
  @Output() delete: EventEmitter<null> = new EventEmitter<null>();

  get hasView(): boolean { return this.canView() && this.view.observed; }
  get hasEdit(): boolean { return this.canEdit() && this.edit.observed; }
  get hasDelete(): boolean { return this.canDelete() && this.delete.observed; }

  confirmDelete() {
    const confirmDeleteMsg = this.confirmDeleteMsg();
    if(!confirmDeleteMsg || window.confirm(confirmDeleteMsg)) {
      this.delete.emit(null)
    }
  }
}
