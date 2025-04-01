import { HttpClient, HttpContext, HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Component } from '@angular/core';
import { Location } from '@angular/common';
import { inject, TestBed } from '@angular/core/testing';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { LoggerService } from '@my/core';
import { environment } from 'src/environments/environment';
import { AuthInterceptor, AuthService, LoginService, AuthCanActivateFn, InRoleCanActivate, AUTH_REQUIRED, AuthWithRedirectCanActivate } from './security.service';

describe('AuthService', () => {
  let service: AuthService;
  let log: LoggerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LoggerService],
    });
    service = TestBed.inject(AuthService);
    log = TestBed.inject(LoggerService);
    spyOn(log, 'log');
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('Login', () => {
    const roles = ['Usuarios', 'Administradores']
    service.login('token', 'refresh', 'usuario', roles)
    expect(service.AuthorizationHeader).toBe('token')
    expect(service.RefreshToken).toBe('refresh')
    expect(service.Name).toBe('usuario')
    expect(service.Roles.length).toEqual(2)
    expect(service.isAuthenticated).toBeTruthy();
    expect(service.isInRoles('Administradores')).toBeTruthy();
  });

  it('Logout', () => {
    service.logout()
    expect(service.isAuthenticated).toBeFalsy();
  });

});

describe('LoginService ', () => {
  const apiURL = environment.securityApiURL

  let service: LoginService;
  let auth: AuthService

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService, HttpClient],
    });
    service = TestBed.inject(LoginService);
    auth = TestBed.inject(AuthService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
  describe('login', () => {
    it('OK', inject([HttpTestingController], (httpMock: HttpTestingController) => {
      const demoUsr = { "username": "demo@example.com", "password": "P@$$w0rd" }
      const res = {
        "success": true,
        "token": "B" + "earer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c3IiOiJhZG1AZXhhbXBsZS5jb20iLCJuYW1lIjoiQWRtaW5pc3RyYWRvciIsInJvbGVzIjpbIlVzdWFyaW9zIiwiQWRtaW5pc3RyYWRvcmVzIl0sImlhdCI6MTY3MDU4NTE0MSwiZXhwIjoxNjcwNTg1NDQxLCJhdWQiOiJhdXRob3JpemF0aW9uIiwiaXNzIjoiTWljcm9zZXJ2aWNpb3NKV1QifQ.R4w4DH3HfVssI7TSO0u0z2uCu7BrhLXN5YdxEyx3uOIzhENycz0vL8B0_etz8kSz8KVWM0hOLqf0J7XOwNci1ksf4ZWenykapG-AuEkQkX2Y7ZTjECscor5dT3Cmj0swI12Yx-FL3r3OXDRppSnoOCvOE_w-ardwHt48QCU5u7YjvXjcP34bavFDjYpD7dvy5eoT-TDb0Un4XYkBVhR18u0ogH9TKoxF0lt8TSh5ckwjcZ4_KF3E4TGAIHId6UbuxUqMNTyJW0gkJR7iCQPn4Ez3osvZG4Rvj7VT_VbX_9EzTdXOJ9ZeuMpSuhk-AmFyXCeu8wcD-mU7JWn8RW2OCQ",
        "refresh": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c3IiOiJhZG1AZXhhbXBsZS5jb20iLCJpYXQiOjE2NzA1ODUxNDEsIm5iZiI6MTY3MDU4NTQ0MSwiZXhwIjoxNjcwNTg2MzQxLCJhdWQiOiJhdXRob3JpemF0aW9uIiwiaXNzIjoiTWljcm9zZXJ2aWNpb3NKV1QifQ.rh8bgIhlPKkKeiCHBvkT2qZruAvdFjldfD9PCeC4ZN0",
        "name": "Administrador",
        "roles": ["Usuarios", "Administradores"],
        "expires_in": 300
      }

      service.login(demoUsr.username, demoUsr.password).subscribe({
        next: data => {
          expect(data).toBeTruthy();
          expect(service.isAuthenticated).toBeTruthy();
          expect(service.Name).toEqual(res.name);
          expect(service.Roles.length).toEqual(2);
        },
        error: () => { fail('has executed "error" callback'); }
      });
      const req = httpMock.expectOne(apiURL + 'login');
      expect(req.request.method).toEqual('POST');
      expect(req.request.body).toEqual(demoUsr);
      req.flush(res);
      httpMock.verify();
    }));

    it('KO', inject([HttpTestingController], (httpMock: HttpTestingController) => {
      const demoUsr = { "username": "demo@example.com", "password": "P@$$w0rd" }
      const res = {
        "success": false,
      }

      service.login(demoUsr.username, demoUsr.password).subscribe({
        next: data => {
          expect(data).withContext('service result').toBeFalsy();
          expect(service.isAuthenticated).withContext('is autenticated').toBeFalsy();
        },
        error: () => { fail('has executed "error" callback'); }
      });
      const req = httpMock.expectOne(apiURL + 'login');
      expect(req.request.method).toEqual('POST');
      expect(req.request.body).toEqual(demoUsr);
      req.flush(res);
      httpMock.verify();
    }));

    it('network failure', inject([HttpTestingController], (httpMock: HttpTestingController) => {
      service.login('', '').subscribe({
        next: () => { fail('has executed "next" callback'); },
        error: data => {
          expect(data.status).withContext('service result').toBe(404);
        }
      });
      const req = httpMock.expectOne(apiURL + 'login');
      expect(req.request.method).toEqual('POST');
      req.flush('error', { status: 404, statusText: 'Not Found' });
      httpMock.verify();
    }));
  })

  it('logout', () => {
    auth.login('token', 'refresh', 'usuario', [])
    expect(service.isAuthenticated).toBeTruthy();
    service.logout()
    expect(service.isAuthenticated).toBeFalsy();
  });
  describe('refresh', () => {
    it('OK', inject([HttpTestingController], (httpMock: HttpTestingController) => {
      const res = {
        "success": true,
        "token": "B" + "earer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c3IiOiJhZG1AZXhhbXBsZS5jb20iLCJuYW1lIjoiQWRtaW5pc3RyYWRvciIsInJvbGVzIjpbIlVzdWFyaW9zIiwiQWRtaW5pc3RyYWRvcmVzIl0sImlhdCI6MTY3MDU4NTE0MSwiZXhwIjoxNjcwNTg1NDQxLCJhdWQiOiJhdXRob3JpemF0aW9uIiwiaXNzIjoiTWljcm9zZXJ2aWNpb3NKV1QifQ.R4w4DH3HfVssI7TSO0u0z2uCu7BrhLXN5YdxEyx3uOIzhENycz0vL8B0_etz8kSz8KVWM0hOLqf0J7XOwNci1ksf4ZWenykapG-AuEkQkX2Y7ZTjECscor5dT3Cmj0swI12Yx-FL3r3OXDRppSnoOCvOE_w-ardwHt48QCU5u7YjvXjcP34bavFDjYpD7dvy5eoT-TDb0Un4XYkBVhR18u0ogH9TKoxF0lt8TSh5ckwjcZ4_KF3E4TGAIHId6UbuxUqMNTyJW0gkJR7iCQPn4Ez3osvZG4Rvj7VT_VbX_9EzTdXOJ9ZeuMpSuhk-AmFyXCeu8wcD-mU7JWn8RW2OCQ",
        "refresh": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c3IiOiJhZG1AZXhhbXBsZS5jb20iLCJpYXQiOjE2NzA1ODUxNDEsIm5iZiI6MTY3MDU4NTQ0MSwiZXhwIjoxNjcwNTg2MzQxLCJhdWQiOiJhdXRob3JpemF0aW9uIiwiaXNzIjoiTWljcm9zZXJ2aWNpb3NKV1QifQ.rh8bgIhlPKkKeiCHBvkT2qZruAvdFjldfD9PCeC4ZN0",
        "name": "Administrador",
        "roles": ["Usuarios", "Administradores"],
        "expires_in": 300
      }
      const token = "1234567890"
      auth.login('token', token, 'usuario', [])
      service.refresh().subscribe({
        next: data => {
          expect(data).toBeTruthy();
          expect(service.isAuthenticated).toBeTruthy();
          expect(service.Name).toEqual(res.name);
          expect(service.Roles.length).toEqual(2);
        },
        error: () => { fail('has executed "error" callback'); }
      });
      const req = httpMock.expectOne(apiURL + 'login/refresh');
      expect(req.request.method).toEqual('POST');
      expect(req.request.body).toEqual({ token });
      req.flush(res);
      httpMock.verify();
    }));
    describe('KO', () => {
      it('invalid user', inject([HttpTestingController], (httpMock: HttpTestingController) => {
        const res = {
          "success": false,
        }
        const token = "1234567890"
        auth.login('token', token, 'usuario', [])

        service.refresh().subscribe({
          next: data => {
            expect(data).withContext('service result').toEqual([false]);
            expect(service.isAuthenticated).withContext('is autenticated').toBeFalsy();
          },
          error: () => { fail('has executed "error" callback'); }
        });
        const req = httpMock.expectOne(apiURL + 'login/refresh');
        expect(req.request.method).toEqual('POST');
        expect(req.request.body).toEqual({ token });
        req.flush(res);
        httpMock.verify();
      }));

      it('not is Autenticated', inject([HttpTestingController], () => {
        auth.logout()
        service.refresh().subscribe({
          next: data => {
            expect(data).withContext('service result').toEqual([false]);
          },
          error: () => { fail('has executed "error" callback'); }
        });
      }));

      it('network failure', inject([HttpTestingController], (httpMock: HttpTestingController) => {
        auth.login('token', '', 'usuario', [])
        service.refresh().subscribe({
          next: () => { fail('has executed "next" callback'); },
          error: data => {
            expect(data.status).withContext('service result').toBe(404);
          }
        });
        const req = httpMock.expectOne(apiURL + 'login/refresh');
        expect(req.request.method).toEqual('POST');
        req.flush('error', { status: 404, statusText: 'Not Found' });
        httpMock.verify();
      }));
    })
  });
});

describe('AuthInterceptor', () => {
  const fakeURL = 'https://localhost:80/test'
  const callback = {
    next: (data: { result: string }) => {
      expect(data.result).withContext('service result').toBe('OK');
    },
    error: () => { fail('has executed "error" callback'); }
  }
  let service: AuthInterceptor;
  let auth: AuthService
  let http: HttpClient


  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AuthInterceptor, AuthService,
        { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true, }
      ],
      imports: [HttpClientTestingModule,],
    });
    service = TestBed.inject(AuthInterceptor);
    auth = TestBed.inject(AuthService);
    http = TestBed.inject(HttpClient)
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('OK', () => {
    it('not required', inject([HttpTestingController], (httpMock: HttpTestingController) => {
      auth.login('B' + 'earer 12345', '12345', 'usuario', [])
      http.get<{ result: string }>(fakeURL).subscribe(callback);
      const req = httpMock.expectOne(fakeURL);
      expect(req.request.method).toEqual('GET');
      expect(req.request.headers.has('Authorization')).toBeFalsy();
      req.flush({ result: 'OK' });
      httpMock.verify();
    }));

    it('withCredentials', inject([HttpTestingController], (httpMock: HttpTestingController) => {
      auth.login('B' + 'earer 12345', '12345', 'usuario', [])
      http.get<{ result: string }>(fakeURL, { withCredentials: true }).subscribe(callback);
      const req = httpMock.expectOne(fakeURL);
      expect(req.request.method).toEqual('GET');
      expect(req.request.headers.has('Authorization')).toBeTruthy();
      req.flush({ result: 'OK' });
      httpMock.verify();
    }));

    it('AUTH_REQUIRED', inject([HttpTestingController], (httpMock: HttpTestingController) => {
      auth.login('B' + 'earer 12345', '12345', 'usuario', [])
      http.get<{ result: string }>(fakeURL, { context: new HttpContext().set(AUTH_REQUIRED, true) }).subscribe(callback);
      const req = httpMock.expectOne(fakeURL);
      expect(req.request.method).toEqual('GET');
      expect(req.request.headers.has('Authorization')).toBeTruthy();
      req.flush({ result: 'OK' });
      httpMock.verify();
    }));
  });
  describe('KO', () => {
    const apiURL = environment.securityApiURL + 'login/refresh'
    const errorBody = {
      "type": "ApplicationError",
      "status": 401,
      "title": "Invalid token",
      "detail": "Token expired",
      "source": "expiredAt: Fri Dec 09 2022 11:30:41 GMT+0000 (Coordinated Universal Time)"
    }
    const refreshOK = {
      "success": true,
      "token": "B" + "earer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c3IiOiJhZG1AZXhhbXBsZS5jb20iLCJuYW1lIjoiQWRtaW5pc3RyYWRvciIsInJvbGVzIjpbIlVzdWFyaW9zIiwiQWRtaW5pc3RyYWRvcmVzIl0sImlhdCI6MTY3MDU4NTE0MSwiZXhwIjoxNjcwNTg1NDQxLCJhdWQiOiJhdXRob3JpemF0aW9uIiwiaXNzIjoiTWljcm9zZXJ2aWNpb3NKV1QifQ.R4w4DH3HfVssI7TSO0u0z2uCu7BrhLXN5YdxEyx3uOIzhENycz0vL8B0_etz8kSz8KVWM0hOLqf0J7XOwNci1ksf4ZWenykapG-AuEkQkX2Y7ZTjECscor5dT3Cmj0swI12Yx-FL3r3OXDRppSnoOCvOE_w-ardwHt48QCU5u7YjvXjcP34bavFDjYpD7dvy5eoT-TDb0Un4XYkBVhR18u0ogH9TKoxF0lt8TSh5ckwjcZ4_KF3E4TGAIHId6UbuxUqMNTyJW0gkJR7iCQPn4Ez3osvZG4Rvj7VT_VbX_9EzTdXOJ9ZeuMpSuhk-AmFyXCeu8wcD-mU7JWn8RW2OCQ",
      "refresh": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c3IiOiJhZG1AZXhhbXBsZS5jb20iLCJpYXQiOjE2NzA1ODUxNDEsIm5iZiI6MTY3MDU4NTQ0MSwiZXhwIjoxNjcwNTg2MzQxLCJhdWQiOiJhdXRob3JpemF0aW9uIiwiaXNzIjoiTWljcm9zZXJ2aWNpb3NKV1QifQ.rh8bgIhlPKkKeiCHBvkT2qZruAvdFjldfD9PCeC4ZN0",
      "name": "Administrador",
      "roles": ["Usuarios", "Administradores"],
      "expires_in": 300
    }
    const refreshKO = {
      "success": false,
    }
    it('refresh success', inject([HttpTestingController], (httpMock: HttpTestingController) => {
      auth.login('B' + 'earer 12345', '12345', 'usuario', [])
      http.get<{ result: string }>(fakeURL, { withCredentials: true }).subscribe(callback);
      const reqIni = httpMock.expectOne(fakeURL);
      reqIni.flush(errorBody, { status: 401, statusText: 'Unauthorized' });
      expect(reqIni.request.headers.has('Authorization')).toBeTruthy();
      const reqRefr = httpMock.expectOne(apiURL);
      reqRefr.flush(refreshOK);
      const reReq = httpMock.expectOne(fakeURL);
      reReq.flush({ result: 'OK' });
      httpMock.verify();
    }));

    it('refresh no success', inject([HttpTestingController], (httpMock: HttpTestingController) => {
      auth.login('B' + 'earer 12345', '12345', 'usuario', [])
      http.get<{ result: string }>(fakeURL, { withCredentials: true }).subscribe({
        next: () => { fail('has executed "next" callback'); },
        error: data => {
          expect(auth.isAuthenticated).withContext('is autenticated').toBeFalsy();
          expect(data.status).withContext('service result').toBe(401);
        }
      });
      const reqIni = httpMock.expectOne(fakeURL);
      reqIni.flush(errorBody, { status: 401, statusText: 'Unauthorized' });
      expect(reqIni.request.headers.has('Authorization')).toBeTruthy();
      const refresh = httpMock.expectOne(apiURL);
      refresh.flush(refreshKO);
      const reReq = httpMock.expectOne(fakeURL);
      // expect(reReq.request.headers.has('Authorization')).withContext('2 no Authorization').toBeFalsy();
      reReq.flush({ result: 'OK' }, { status: 401, statusText: 'Unauthorized' });
      httpMock.verify();
    }));

    it('refresh error', inject([HttpTestingController], (httpMock: HttpTestingController) => {
      auth.login('B' + 'earer 12345', '12345', 'usuario', [])
      http.get<{ result: string }>(fakeURL, { withCredentials: true }).subscribe({
        next: () => { fail('has executed "next" callback'); },
        error: data => {
          expect(auth.isAuthenticated).withContext('is autenticated').toBeFalsy();
          expect(data.status).withContext('service result').toBe(401);
        }
      });
      const reqIni = httpMock.expectOne(fakeURL);
      reqIni.flush(errorBody, { status: 401, statusText: 'Unauthorized' });
      expect(reqIni.request.headers.has('Authorization')).toBeTruthy();
      const refresh = httpMock.expectOne(apiURL);
      refresh.flush({ result: 'OK' }, { status: 401, statusText: 'Unauthorized' });
      httpMock.verify();
    }));

    it('no refresh', inject([HttpTestingController], (httpMock: HttpTestingController) => {
      auth.login('B' + 'earer 12345', '12345', 'usuario', [])
      http.get<{ result: string }>(fakeURL, { withCredentials: true }).subscribe({
        next: () => { fail('has executed "next" callback'); },
        error: data => {
          expect(auth.isAuthenticated).withContext('is autenticated').toBeTruthy();
          expect(data.status).withContext('service result').toBe(404);
        }
      });
      const req = httpMock.expectOne(fakeURL);
      req.flush('', { status: 404, statusText: 'not found' });
      expect(req.request.headers.has('Authorization')).toBeTruthy();
      httpMock.verify();
    }));

  });
});

@Component({
    selector: 'app-test-home', template: `<p>Test Home</p>`,
    standalone: true
})
class TestHomeComponent { }
@Component({
    selector: 'app-test-component', template: `<p>Test Component</p>`,
    standalone: true
})
class TestComponent { }

describe('AuthCanActivateFn', () => {
  let auth: AuthService
  let router: Router
  let location: Location

  beforeEach(() => {
    TestBed.configureTestingModule({
    providers: [AuthService, Location,],
    imports: [
        RouterTestingModule.withRoutes([
            { path: '', pathMatch: 'full', component: TestHomeComponent },
            { path: 'login', component: TestComponent },
            { path: 'test', component: TestComponent, canActivate: [AuthCanActivateFn] },
            { path: 'redirect', component: TestComponent, canActivate: [AuthWithRedirectCanActivate('/login')] },
        ]),
        TestHomeComponent, TestComponent
    ]
});
    router = TestBed.inject(Router);
    auth = TestBed.inject(AuthService);
    location = TestBed.inject(Location)
  });
  it('canActivateFn', () => {
    auth.login('token', 'refresh', 'usuario', [])
    TestBed.runInInjectionContext(() => expect(AuthCanActivateFn({} as ActivatedRouteSnapshot,{} as RouterStateSnapshot)).toBeTruthy())
  });
  it('not canActivateFn', () => {
    auth.logout()
    TestBed.runInInjectionContext(() => expect(AuthCanActivateFn({} as ActivatedRouteSnapshot,{} as RouterStateSnapshot)).toBeFalsy())
  });

  it('canActivate', async () => {
    auth.login('token', 'refresh', 'usuario', [])
    expect(router.routerState.snapshot.url).toEqual('');
    expect(location.path()).toEqual('');
    const navigate = await router.navigateByUrl('/test')
    expect(navigate).toBeTruthy()
    expect(router.routerState.snapshot.url).toEqual('/test');
    expect(location.path()).toEqual('/test');
  });
  it('not canActivate', async () => {
    auth.logout()
    expect(router.routerState.snapshot.url).toEqual('');
    const navigate = await router.navigateByUrl('/test')
    expect(navigate).toBeFalsy()
    expect(router.routerState.snapshot.url).toEqual('');
  });
  it('not canActivate redirectTo', async () => {
    auth.logout()
    expect(router.routerState.snapshot.url).toEqual('');
    const navigate = await router.navigateByUrl('/redirect')
    expect(navigate).toBeFalsy()
    expect(router.routerState.snapshot.url).toEqual('/login?returnUrl=%2Fredirect');
  });
});

describe('InRoleCanActivate', () => {
  let auth: AuthService
  let router: Router

  beforeEach(() => {
    TestBed.configureTestingModule({
    providers: [AuthService],
    imports: [
        RouterTestingModule.withRoutes([
            { path: '', pathMatch: 'full', component: TestHomeComponent },
            { path: 'test', component: TestComponent, canActivate: [InRoleCanActivate('Administradores', 'ADMIN')] },
            { path: 'bad', component: TestComponent, canActivate: [InRoleCanActivate()] },
        ]),
        TestHomeComponent, TestComponent
    ]
});
    router = TestBed.inject(Router);
    auth = TestBed.inject(AuthService);
  });

  it('canActivate', async () => {
    auth.login('token', 'refresh', 'usuario', ['Usuarios', 'Administradores'])
    expect(router.routerState.snapshot.url).toEqual('');
    const navigate = await router.navigateByUrl('/test')
    expect(navigate).toBeTruthy()
    expect(router.routerState.snapshot.url).toEqual('/test');
  });
  it('canActivate: sin data', async () => {
    auth.login('token', 'refresh', 'usuario', ['Usuarios', 'Administradores'])
    expect(router.routerState.snapshot.url).toEqual('');
    const navigate = await router.navigateByUrl('/bad')
    expect(navigate).toBeFalsy()
    expect(router.routerState.snapshot.url).toEqual('');
  });
  it('not canActivate: not authenticated', async () => {
    auth.logout()
    expect(router.routerState.snapshot.url).toEqual('');
    const navigate = await router.navigateByUrl('/test')
    expect(navigate).toBeFalsy()
    expect(router.routerState.snapshot.url).toEqual('');
  });
  it('not canActivate: not roles', async () => {
    auth.login('token', 'refresh', 'usuario', ['Usuarios'])
    expect(router.routerState.snapshot.url).toEqual('');
    const navigate = await router.navigateByUrl('/test')
    expect(navigate).toBeFalsy()
    expect(router.routerState.snapshot.url).toEqual('');
  });
});

