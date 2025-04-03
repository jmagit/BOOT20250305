/* eslint-disable @typescript-eslint/no-explicit-any */
import { Component, Injectable, OnInit } from '@angular/core';

import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { NotificationService } from '../common-services';
import { PeliculasListBodyComponent } from '../peliculas';
import { FormsModule } from '@angular/forms';
import { ErrorMessagePipe, PastOrPresentValidator } from '@my/core';

@Injectable({
  providedIn: 'root'
})
export class NovedadesDAOService {
  protected baseUrl = environment.apiURL + 'catalogo/novedades/v1';
  constructor(protected http: HttpClient) { }

  query(fecha: string): Observable<any[]> {
    return this.http.get<any>(`${this.baseUrl}?fecha=${encodeURIComponent(fecha)}`);
  }
}

@Component({
  selector: 'app-novedades',
  standalone: true,
  imports: [PeliculasListBodyComponent, FormsModule, PastOrPresentValidator, ErrorMessagePipe],
  templateUrl: './novedades.component.html',
  styleUrls: ['./novedades.component.css']
})
export class NovedadesComponent implements OnInit {
  novedades: any = {}
  fecha = '2021-01-01'
  constructor(private dao: NovedadesDAOService, private notify: NotificationService) {
    const hoy = new Date();
    this.fecha = (new Date(hoy.getTime() - 1000 * 60 * 60 * 24 * 30)).toISOString().substring(0, 10)
  }

  ngOnInit(): void {
    this.cargar()
  }

  cargar() {
    this.dao.query(`${this.fecha} 00:00:00`).subscribe({
      next: data => {
        this.novedades = data;
      },
      error: err => this.handleError(err)
    });

  }
  handleError(err: HttpErrorResponse) {
    let msg = ''
    switch (err.status) {
      case 0: msg = err.message; break;
      case 404: msg = `ERROR: ${err.status} ${err.statusText}`; break;
      default:
        msg = `ERROR: ${err.status} ${err.statusText}.${err.error?.['title'] ? ` Detalles: ${err.error['title']}` : ''}`
        break;
    }
    this.notify.add(msg)
  }
  imageErrorHandler(event: Event) {
    (event.target as HTMLImageElement).src = '/images/photo-not-found.svg'
  }
}
