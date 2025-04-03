/* eslint-disable @typescript-eslint/no-explicit-any */
import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Observable, of, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';

export abstract class RESTDAOService<T, K> {
  protected baseUrl = environment.apiURL;
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

export class DAOServiceMock<T, K> extends RESTDAOService<T, K> {
  private readonly pk: string
  private readonly listado: T[]
  constructor(listado: T[]) {
    super('')
    this.listado = listado.map(item => ({ ...item }))
    this.pk = Object.keys(this.listado[0] as Record<string, any>)[0]
  }
  override query(): Observable<T[]> {
    return of(this.listado);
  }
  override get(id: K): Observable<T> {
    if (+id < 0) return this.unknownError(id)
    const index = this.findIndex(id)
    if (index < 0)
      return this.notFound(id)
    return of(this.listado[index]);
  }
  override add(item: T): Observable<T> {
    const id = (item as Record<string, any>)[this.pk]
    if (+id < 0) return this.unknownError(id)
    this.listado.push(item)
    return of(item);
  }
  override change(id: K, item: T): Observable<T> {
    if (+id < 0) return this.unknownError(id)
    const index = this.findIndex(id)
    if (index < 0)
      return this.notFound(id)
    this.listado[index] = item;
    return of(item);
  }
  override remove(id: K): Observable<T> {
    if (+id < 0) return this.unknownError(id)
    const index = this.findIndex(id)
    if (index < 0)
      return this.notFound(id)
    const item = this.listado[index];
    this.listado.splice(index, 1)
    return of(item);
  }
  page(page: number, _rows: number = 20): Observable<{ page: number, pages: number, rows: number, list: any[] }> {
    return of({ page, pages: 1, rows: this.listado.length, list: this.listado });
  }

  private findIndex(id: K) {
    return this.listado.findIndex(item => (item as Record<string, any>)[this.pk] == id)
  }

  private unknownError(id: K) {
    return throwError(() => new HttpErrorResponse({
      status: 0,
      statusText: 'Not Found',
      url: `${this.baseUrl}/${id}`,
      error: {
        isTrusted: true
      }
    })) as Observable<T>
  }
  private notFound(id: K) {
    return throwError(() => new HttpResponse({
      status: 404,
      statusText: 'Not Found',
      url: `${this.baseUrl}/${id}`,
      body: {
        "type": "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.4",
        "title": "Not Found",
        "status": 404,
        "instance": `${this.baseUrl}/${id}`
      }
    })) as Observable<T>
  }
}

