import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { LoggerService } from '@my/core';
import { NotificationService, NavigationService } from '../common-services';
import { AuthService } from '../security';
import { ModoCRUD } from './tipos';
import { RESTDAOService } from './rest-dao-service.class';

export abstract class ViewModelService<T, K> {
  protected modo: ModoCRUD = 'list';
  protected listado: T[] = [];
  protected elemento: T | null = null;
  protected readonly initElemento: T;
  protected idOriginal: K | null = null;

  constructor(protected dao: RESTDAOService<T, K>, initElemento: T, public notify: NotificationService, protected out: LoggerService,
    public auth: AuthService, protected router: Router, protected navigation: NavigationService) {
    this.initElemento = initElemento
  }

  public get Modo(): ModoCRUD { return this.modo; }
  public get Listado(): T[] { return this.listado; }
  public get Elemento(): T { return this.elemento ?? {} as T; }

  public list(): void {
    this.dao.query().subscribe({
      next: data => {
        this.listado = data;
        this.modo = 'list';
      },
      error: err => this.handleError(err)
    });
  }
  public add(): void {
    this.cargaListas()
    this.elemento = {...this.initElemento};
    this.modo = 'add';
  }
  public edit(key: K): void {
    this.cargaListas()
    this.dao.get(key).subscribe({
      next: data => {
        this.elemento = data;
        this.idOriginal = key;
        this.modo = 'edit';
      },
      error: err => this.handleError(err)
    });
  }
  public view(key: K): void {
    this.dao.get(key).subscribe({
      next: data => {
        this.elemento = data;
        this.modo = 'view';
      },
      error: err => this.handleError(err)
    });
  }
  public delete(key: K, nextFn = this.cancel): void {
    if (!window.confirm('¿Seguro?')) { return; }

    this.dao.remove(key).subscribe({
      next: () => {
        nextFn.apply(this)
      },
      error: err => this.handleError(err)
    });
  }

  clear() {
    this.elemento = null;
    this.idOriginal = null;
    this.listado = [];
  }

  public cancel(): void {
    this.elemento = null;
    this.idOriginal = null;
    this.navigation.back()
  }
  public send(): void {
    switch (this.modo) {
      case 'add':
        if (this.elemento)
          this.dao.add(this.elemento).subscribe({
            next: () => this.cancel(),
            error: err => this.handleError(err)
          });
        break;
      case 'edit':
        if (this.idOriginal && this.elemento)
          this.dao.change(this.idOriginal, this.elemento).subscribe({
            next: () => this.cancel(),
            error: err => this.handleError(err)
          });
        break;
      case 'view':
        this.cancel();
        break;
    }
  }

  handleError(err: HttpErrorResponse) {
    let msg = ''
    switch (err.status) {
      case 0: msg = err.message; break;
      case 404: msg = `ERROR: ${err.status} ${err.statusText}`; break;
      default:
        msg = err.error?.['detail']??err.error?.['title']??''
        msg = `ERROR: ${err.status} ${err.statusText}.${msg ? ` Detalles: ${msg}` : ''}`
        if(err.error?.['errors']) {
          for(const cmp in err.error?.['errors'] )
            msg += ` ${cmp}: ${err.error?.['errors'][cmp]}.`
        }
        break;
    }
    this.notify.add(msg)
  }
  pageChange(page: number = 0) {
    this.router.navigate([], { queryParams: { page } })
  }
  imageErrorHandler(event: Event) {
    (event.target as HTMLImageElement).src = '/images/photo-not-found.svg'
  }

  protected cargaListas() {
    // Para sobre escribir
  }
}
