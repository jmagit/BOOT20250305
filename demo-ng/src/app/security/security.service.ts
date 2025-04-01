import { Injectable, inject } from '@angular/core';
import { Router, Route, CanActivateFn, CanActivateChildFn, CanMatchFn } from '@angular/router';
import { HttpClient, HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpContextToken } from '@angular/common/http';
import { BehaviorSubject, catchError, filter, Observable, of, switchMap, take, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';
import { EventBusService } from '../common-services';

export const AUTH_REQUIRED = new HttpContextToken<boolean>(() => false);
export const LOGIN_EVENT = 'login'
export const LOGIN_FORM_OPEN_EVENT = 'login.form.open'
export const LOGIN_FORM_CLOSE_EVENT = 'login.form.close'
export const LOGOUT_EVENT = 'logout'

@Injectable({ providedIn: 'root' })
export class AuthService {
  private isAuth = false;
  private authToken: string = '';
  private refresh: string = '';
  private name = '';
  private roles: string[] = []
  private storage: Storage;

  constructor(private eventBus: EventBusService) {
    this.storage = sessionStorage ?? localStorage;
    if (this.storage?.['AuthService']) {
      const cache = JSON.parse(this.storage['AuthService']);
      this.isAuth = cache.isAuth;
      this.authToken = cache.authToken;
      this.refresh = cache.refresh;
      this.name = cache.name;
      this.roles = cache.roles;
    }
  }

  get AuthorizationHeader() { return this.authToken; }
  get RefreshToken() { return this.refresh; }
  get isAuthenticated() { return this.isAuth; }
  get Name() { return this.name; }
  get Roles() { return Object.assign([], this.roles); }

  login(authToken: string, refresh: string, name: string, roles: string[]) {
    this.isAuth = true;
    this.authToken = authToken;
    this.refresh = refresh;
    this.name = name;
    this.roles = roles;
    if (this.storage) {
      this.storage['AuthService'] = JSON.stringify({ isAuth: this.isAuth, authToken, refresh, name, roles });
    }
    this.eventBus.emit(LOGIN_EVENT)
  }
  isInRoles(...rolesArgs: string[]) {
    if (this.isAuthenticated && this.roles.length > 0 && rolesArgs.length > 0)
      for (const role of rolesArgs)
        if (this.roles.includes(role)) return true;
    return false;
  }
  logout() {
    this.isAuth = false;
    this.authToken = '';
    this.refresh = '';
    this.name = '';
    this.roles = [];
    if (this.storage) {
      this.storage.removeItem('AuthService');
    }
    this.eventBus.emit(LOGOUT_EVENT)
  }
}

class LoginResponse {
  success = false;
  token: string = '';
  refresh: string = '';
  name: string = '';
  roles: string[] = [];
  expires_in: number = 0;
}

@Injectable({ providedIn: 'root' })
export class LoginService {
  constructor(private http: HttpClient, private auth: AuthService) { }
  get isAuthenticated() { return this.auth.isAuthenticated; }
  get Name() { return this.auth.Name; }
  get Roles() { return this.auth.Roles; }

  login(usr: string, pwd: string) {
    if (this.auth.isAuthenticated) this.auth.logout();
    return new Observable(observable =>
      this.http.post<LoginResponse>(environment.securityApiURL + 'login', { username: usr, password: pwd })
        .subscribe({
          next: data => {
            if (data.success === true) {
              this.auth.login(data.token ?? '', data.refresh ?? '', data.name ?? '', data.roles ?? []);
            }
            observable.next(this.auth.isAuthenticated);
          },
          error: err => observable.error(err)
        })
    );
  }
  refresh() {
    if (this.auth.isAuthenticated) {
      return this.http.post<LoginResponse>(environment.securityApiURL + 'login/refresh', { token: this.auth.RefreshToken })
        .pipe(
          switchMap(data => {
            if (data.success === true) {
              this.auth.login(data.token ?? '', data.refresh ?? '', data.name ?? '', data.roles ?? []);
            } else {
              this.auth.logout()
            }
            return of([data.success])
          })
        );
    }
    return of([false])
  }

  logout() {
    this.auth.logout();
  }
}

// { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true, },

/* eslint-disable @typescript-eslint/no-explicit-any */
@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private isRefreshing = false;
  private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);

  constructor(private auth: AuthService, private login: LoginService) { }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!(req.context.get(AUTH_REQUIRED) || req.withCredentials) || !this.auth.isAuthenticated) {
      return next.handle(req);
    }
    const authReq = this.addAuthorizationHeader(req)
    return next.handle(authReq).pipe(
      catchError(err => {
        if ([401, 403].includes(err.status) && err.error?.detail?.toLowerCase()?.includes('token expired')
          && !authReq.url.includes('/refresh') && this.auth.isAuthenticated) {
          return this.refreshToken(authReq, next);
        }
        return throwError(() => err);
      }))
  }
  private refreshToken(req: HttpRequest<any>, next: HttpHandler) {
    if (!this.isRefreshing) {
      this.isRefreshing = true;
      this.refreshTokenSubject.next(false);
      return this.login.refresh().pipe(
        switchMap(() => {
          this.isRefreshing = false;
          this.refreshTokenSubject.next(true);
          return next.handle(this.addAuthorizationHeader(req))
        }),
        catchError((err) => {
          this.isRefreshing = false;
          this.login.logout();
          return throwError(() => err);
        })
      )
    }
    return this.refreshTokenSubject.pipe(
      filter(isRefresh => isRefresh),
      take(1),
      switchMap(() => next.handle(this.addAuthorizationHeader(req)))
    );
  }

  private addAuthorizationHeader(req: HttpRequest<any>) {
    return req.clone(
      { headers: req.headers.set('Authorization', this.auth.AuthorizationHeader) }
    );
  }
}
/* eslint-enable @typescript-eslint/no-explicit-any */

/*
@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) { }
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    if (!this.authService.isAuthenticated && route?.data['redirectTo'])
      this.router.navigate([route?.data['redirectTo']], { queryParams: { returnUrl: state.url } });
    return this.authService.isAuthenticated;
  }
}
*/
export const AuthCanActivateFn: CanActivateFn = (_route, _state) => {
  return inject(AuthService).isAuthenticated;
}

export function AuthWithRedirectCanActivate(redirectTo: string): CanActivateFn {
  return (_route, state) => {
    const authService = inject(AuthService)
    if (!authService.isAuthenticated && redirectTo)
      inject(Router).navigate([redirectTo], { queryParams: { returnUrl: state.url } });
    return authService.isAuthenticated;
  }
}

/*
@Injectable({ providedIn: 'root' })
export class InRoleGuard implements CanActivate, CanActivateChild, CanLoad {
  constructor(private authService: AuthService, private router: Router) { }
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    return route.data['roles'] ? this.authService.isInRoles(...route.data['roles']) : false;
  }
  canActivateChild(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
    return childRoute.data['roles'] ? this.authService.isInRoles(...childRoute.data['roles']) : false;
  }
  canLoad(route: Route, segments: UrlSegment[]): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
    return route.data && route.data['roles'] ? this.authService.isInRoles(...route.data['roles']) : false;
  }
}
*/
export function InRoleCanActivate(...rolesArgs: string[]): CanActivateFn {
  return (_route, _state) => {
    return rolesArgs.length ? inject(AuthService).isInRoles(...rolesArgs) : false;
  }
}
export const InRoleCanActivateFn: CanActivateFn = (route, _state) => {
  return route.data['roles'] ? inject(AuthService).isInRoles(...route.data['roles']) : false;
}
export function InRoleCanActivateChild(...rolesArgs: string[]): CanActivateChildFn {
  return (_childRoute, _state) => {
    return rolesArgs.length ? inject(AuthService).isInRoles(...rolesArgs) : false;
  }
}
export const InRoleCanActivateChildFn: CanActivateChildFn = (childRoute, _state) => {
  return childRoute.data['roles'] ? inject(AuthService).isInRoles(...childRoute.data['roles']) : false;
}
export function InRoleCanLoad(...rolesArgs: string[]): CanMatchFn {
  return (_route, _segments) => {
    return rolesArgs.length ? inject(AuthService).isInRoles(...rolesArgs) : false;
  }
}
export const InRoleCanLoadFn: CanMatchFn = (route: Route, _segments) => {
  return route.data && route.data['roles'] ? inject(AuthService).isInRoles(...route.data['roles']) : false;
}


export class Role {
  role: string = '';
}
export class User {
  idUsuario: string = '';
  password: string = '';
  nombre: string = '';
  roles: Role[] = [];

}

@Injectable({ providedIn: 'root' })
export class RegisterUserDAO {
  private baseUrl = environment.securityApiURL + 'register ';
  private options = { withCredentials: true };

  constructor(private http: HttpClient) { }

  add(item: User) {
    return this.http.post(this.baseUrl, item);
  }

  get() {
    return this.http.get<User>(this.baseUrl, this.options);
  }
  change(item: User) {
    return this.http.put(this.baseUrl, item, this.options);
  }
}
