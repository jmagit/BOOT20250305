/* eslint-disable @typescript-eslint/no-explicit-any */
import { HttpContext, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { RESTDAOService } from '../code-base';
import { AUTH_REQUIRED } from '../security';

@Injectable({
  providedIn: 'root'
})
export class PeliculasDAOService extends RESTDAOService<any, number> {
  constructor() {
    super('catalogo/peliculas/v1', { context: new HttpContext().set(AUTH_REQUIRED, true) });
  }
  page(page: number, rows: number = 20): Observable<{ page: number, pages: number, rows: number, list: any[] }> {
    return new Observable(subscriber => {
      const url = `${this.baseUrl}?page=${page}&size=${rows}&sort=title`
      this.http.get<any>(url, this.option).subscribe({
        next: data => subscriber.next({ page: data.page.number, pages: data.page.totalPages, rows: data.page.totalElements, list: data.content }),
        error: err => subscriber.error(err)
      })
    })
  }
  search(filtro: any): Observable<any[]> {
    let params = new HttpParams()
    for(const param in filtro) {
      if (filtro[param] !== undefined && filtro[param] !== null && filtro[param] !== '') {
        params = params.append(param, filtro[param])
      }
    }
    if(params.keys().length === 0) {
      return throwError(() => new HttpErrorResponse({
        status: 400,
        statusText: 'Bad Request',
        url: `${this.baseUrl}/filtro`,
        error: {
          isTrusted: true,
          title: 'Filtro vacío',
        }
      })) as Observable<any[]>
    }
    params = params.append('mode', 'short')
    return this.http.get<any[]>(`${this.baseUrl}/filtro`, Object.assign({}, this.option, { params }));
  }
  // details(id: number): Observable<any> {
  //   return this.http.get<any>(`${this.baseUrl}/${id}?mode=details`, this.option);
  // }
  clasificaciones(): Observable<any[]> {
    return this.http.get<any>(`${this.baseUrl}/clasificaciones`, this.option);
  }
  contenidos(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/contenido-adicional`, this.option);
  }
}

@Injectable({
  providedIn: 'root'
})
export class ActoresDAOService extends RESTDAOService<any, number> {
  constructor() {
    super('catalogo/actores/v1', { context: new HttpContext().set(AUTH_REQUIRED, true) });
  }
  override query(extras = {}): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}?modo=short`, Object.assign({}, this.option, extras));
  }
  page(page: number, rows: number = 20): Observable<{ page: number, pages: number, rows: number, list: any[] }> {
    return new Observable(subscriber => {
      const url = `${this.baseUrl}?page=${page}&size=${rows}&sort=firstName,lastName`
      this.http.get<any>(url, this.option).subscribe({
        next: data => subscriber.next({ page: data.page.number, pages: data.page.totalPages, rows: data.page.totalElements, list: data.content }),
        error: err => subscriber.error(err)
      })
    })
  }
  peliculas(id: number): Observable<any[]> {
    return this.http.get<any>(`${this.baseUrl}/${id}/pelis`, this.option);
  }
}

@Injectable({
  providedIn: 'root'
})
export class CategoriasDAOService extends RESTDAOService<any, number> {
  constructor() {
    super('catalogo/categorias/v1', { context: new HttpContext().set(AUTH_REQUIRED, true) });
  }
  peliculas(id: number): Observable<any[]> {
    return this.http.get<any>(`${this.baseUrl}/${id}/peliculas`, this.option);
  }
}

@Injectable({
  providedIn: 'root'
})
export class IdiomasDAOService extends RESTDAOService<any, number> {
  constructor() {
    super('catalogo/idiomas/v1', { context: new HttpContext().set(AUTH_REQUIRED, true) });
  }
}
