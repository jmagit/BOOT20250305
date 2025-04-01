import { JsonPipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ErrorMessagePipe, NIFNIEValidator, TypeValidator, UppercaseValidator } from '@my/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Injectable } from '@angular/core';
import { NotificationService } from 'src/app/common-services';

export abstract class RESTDAOService<T, K> {
  protected baseUrl = environment.apiUrl;
  protected http = inject(HttpClient)

  constructor(entidad: string, protected option = {}) {
    this.baseUrl += entidad;
  }
  query(extras = {}): Observable<T[]> {
    return this.http.get<T[]>(this.baseUrl, Object.assign({}, this.option, extras));
  }
  get(id: K, extras = {}): Observable<T> {
    return this.http.get<T>(`${this.baseUrl}/${id}`, Object.assign({}, this.option, extras));
  }
  add(item: T, extras = {}): Observable<T> {
    return this.http.post<T>(this.baseUrl, item, Object.assign({}, this.option, extras));
  }
  change(id: K, item: T, extras = {}): Observable<T> {
    return this.http.put<T>(`${this.baseUrl}/${id}`, item, Object.assign({}, this.option, extras));
  }
  remove(id: K, extras = {}): Observable<T> {
    return this.http.delete<T>(`${this.baseUrl}/${id}`, Object.assign({}, this.option, extras));
  }
}


@Injectable({providedIn: 'root'})
export class PersonasDaoService extends RESTDAOService<any, number> {
  constructor() {
    super('personas')
  }

}
@Component({
  selector: 'app-formularios',
  imports: [FormsModule, ErrorMessagePipe, NIFNIEValidator, TypeValidator, UppercaseValidator, JsonPipe ],
  templateUrl: './formularios.component.html',
  styleUrl: './formularios.component.css'
})
export class FormulariosComponent {
  public elemento: any = { }
  public modo: 'add' | 'edit' = 'add'

  constructor(private dao: PersonasDaoService, private notify: NotificationService) {}

  add() {
    this.elemento = {}
    this.modo = 'add'
  }
  edit(key: number) {
    // this.elemento = { id: key, nombre: 'Pepito', apellidos: 'Grillo', correo: 'pgrillo@example.com', fAlta: '2025-01-01', edad: 99, nif: '12345678z', activo: true}
    // this.modo = 'edit'
    this.dao.get(key).subscribe({
      next: datos => {
        this.elemento = datos
        this.modo = 'edit'
      },
      error: err => this.notify.add(JSON.stringify(err))
    })
  }

  cancel() {
    this.elemento = {}
  }
  send() {
    switch(this.modo) {
      case 'add':
         this.dao.add(this.elemento).subscribe({
          next: () => this.cancel(),
          error: err => this.notify.add(JSON.stringify(err))
        })
        //  alert(`POST ${JSON.stringify(this.elemento)}`)
        // this.cancel()
        break
      case 'edit':
        this.dao.change(this.elemento.id, this.elemento).subscribe({
          next: () => this.cancel(),
          error: err => this.notify.add(JSON.stringify(err))
        })
        // alert(`PUT ${JSON.stringify(this.elemento)}`)
        // this.cancel()
        break
    }
  }
}
