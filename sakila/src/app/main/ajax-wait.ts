import { Component, Injectable, inject, signal, computed } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpHandlerFn } from '@angular/common/http';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class AjaxWaitService {
  private cont = signal<number>(0);
  public readonly Visible = computed(() => this.cont() > 0);
  public readonly Oculto = computed(() => !this.Visible());

  public Mostrar(): void { this.cont.update(old => old + 1) }
  public Ocultar(): void { this.cont.update(old => old - 1) }
}

// { provide: HTTP_INTERCEPTORS, useClass: AjaxWaitInterceptor, multi: true, },
// withInterceptorsFromDi()
@Injectable({ providedIn: 'root' })
export class AjaxWaitInterceptor implements HttpInterceptor {
  constructor(private srv: AjaxWaitService) { }
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    this.srv.Mostrar();
    return next.handle(req)
      .pipe(
        finalize(() => this.srv.Ocultar())
      );
  }
}
// versión standalone
// withInterceptors([ ajaxWaitInterceptor ])
export function ajaxWaitInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> {
  const srv: AjaxWaitService = inject(AjaxWaitService);
  srv.Mostrar();
  return next(req).pipe(finalize(() => srv.Ocultar()));
}

@Component({
    selector: 'app-ajax-wait',
    template: `
  <div [hidden]="Oculto()">
    <div class="ajax-wait"></div>
    <!-- <img src="images/loading.gif" alt="Esperando ..."> -->
    <div class="loader"></div>
  </div>`,
    styles: [`
    .ajax-wait {
      position: fixed;
      background-color: black;
      left: 0;
      top: 0;
      width: 100vw;
      height: 100vh;
      opacity: 0.3;
      z-index:100;
    }
    img {
      position: fixed;
      left: 45%;
      top: 45%;
      width: 10%;
      height: auto;
      opacity: 1;
      z-index:101;
    }
    .loader {
        border: 16px dotted #1a93fd;
        /*border-top: 16px solid #abdeff;*/
        border-radius: 50%;
        animation: spin 5s linear infinite;
        position: fixed;
        left: 45%;
        top: 45%;
        width: 80px;
        height: 80px;
        z-index:101;
        opacity: 1;
      }

      @keyframes spin {
        0% { transform: rotate(0deg); }
        100% { transform: rotate(360deg); }
      }
  `],
    standalone: true,
})
export class AjaxWaitComponent {
  constructor(private srv: AjaxWaitService) { }

  public get Visible() { return this.srv.Visible }
  public get Oculto() { return this.srv.Oculto; }
}
