import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-formularios',
  imports: [FormsModule],
  templateUrl: './formularios.component.html',
  styleUrl: './formularios.component.css'
})
export class FormulariosComponent {
  public elemento: any = { }
  public modo: 'add' | 'edit' = 'add'

  add() {
    this.elemento = {}
    this.modo = 'add'
  }
  edit(key: number) {
    this.elemento = { id: key, nombre: 'Pepito', apellidos: 'Grillo', correo: 'pgrillo@example.com', fAlta: '2025-01-01', edad: 99, nif: '12345678z', activo: true}
    this.modo = 'edit'
  }

  cancel() {
    this.elemento = {}
  }
  send() {
    switch(this.modo) {
      case 'add':
        alert(`POST ${JSON.stringify(this.elemento)}`)
        this.cancel()
        break
      case 'edit':
        alert(`PUT ${JSON.stringify(this.elemento)}`)
        this.cancel()
        break
    }
  }
}
