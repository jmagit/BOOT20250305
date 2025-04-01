import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { LoginComponent, LoginFormComponent } from './login.component';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { NotificationService, WindowService } from 'src/app/common-services';
import { LoginService } from '../security.service';
import { AuthService } from '..';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { LoggerService } from '@my/core';
import { FormsModule } from '@angular/forms';
import { environment } from 'src/environments/environment';

describe('LoginComponent', () => {
  const apiURL = environment.securityApiURL
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let loginSrv: LoginService

  beforeEach(async() => {
    spyOn(console, 'warn')
    TestBed.configureTestingModule({
        providers: [LoginService, AuthService, NotificationService, LoggerService,],
        imports: [HttpClientTestingModule, RouterTestingModule, FormsModule, LoginComponent],
        schemas: [NO_ERRORS_SCHEMA]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    loginSrv = TestBed.inject(LoginService)
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it('autenticated', waitForAsync(() => {
    const res = {
      "success": true,
      "token": "B" + "earer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c3IiOiJhZG1AZXhhbXBsZS5jb20iLCJuYW1lIjoiQWRtaW5pc3RyYWRvciIsInJvbGVzIjpbIlVzdWFyaW9zIiwiQWRtaW5pc3RyYWRvcmVzIl0sImlhdCI6MTY3MDU4NTE0MSwiZXhwIjoxNjcwNTg1NDQxLCJhdWQiOiJhdXRob3JpemF0aW9uIiwiaXNzIjoiTWljcm9zZXJ2aWNpb3NKV1QifQ.R4w4DH3HfVssI7TSO0u0z2uCu7BrhLXN5YdxEyx3uOIzhENycz0vL8B0_etz8kSz8KVWM0hOLqf0J7XOwNci1ksf4ZWenykapG-AuEkQkX2Y7ZTjECscor5dT3Cmj0swI12Yx-FL3r3OXDRppSnoOCvOE_w-ardwHt48QCU5u7YjvXjcP34bavFDjYpD7dvy5eoT-TDb0Un4XYkBVhR18u0ogH9TKoxF0lt8TSh5ckwjcZ4_KF3E4TGAIHId6UbuxUqMNTyJW0gkJR7iCQPn4Ez3osvZG4Rvj7VT_VbX_9EzTdXOJ9ZeuMpSuhk-AmFyXCeu8wcD-mU7JWn8RW2OCQ",
      "refresh": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c3IiOiJhZG1AZXhhbXBsZS5jb20iLCJpYXQiOjE2NzA1ODUxNDEsIm5iZiI6MTY3MDU4NTQ0MSwiZXhwIjoxNjcwNTg2MzQxLCJhdWQiOiJhdXRob3JpemF0aW9uIiwiaXNzIjoiTWljcm9zZXJ2aWNpb3NKV1QifQ.rh8bgIhlPKkKeiCHBvkT2qZruAvdFjldfD9PCeC4ZN0",
      "name": "Administrador",
      "roles": ["Usuarios", "Administradores"],
      "expires_in": 300
    }
    const httpMock = TestBed.inject(HttpTestingController);
    spyOn(component, 'reloadPage')
    loginSrv.logout()
    component.txtUsuario = 'demo@example.com'
    component.txtPassword = 'P@$$w0rd'
    component.logInOut()
    fixture.detectChanges();

    const req = httpMock.expectOne(apiURL + 'login');
    req.flush(res);
    httpMock.verify();

    expect(component.loginSrv.isAuthenticated).withContext('is autenticated').toBeTruthy();

  }));
  it('not autenticated', waitForAsync(() => {
    const res = { "success": false }
    const httpMock = TestBed.inject(HttpTestingController);
    const notify = TestBed.inject(NotificationService);
    spyOn(notify, 'add')
    spyOn(component, 'reloadPage')
    loginSrv.logout()
    component.txtUsuario = 'demo@example.com'
    component.txtPassword = 'P@$$w0rd'
    component.logInOut()
    fixture.detectChanges();

    const req = httpMock.expectOne(apiURL + 'login');
    req.flush(res);
    httpMock.verify();

    expect(component.loginSrv.isAuthenticated).withContext('is autenticated').toBeFalsy();
    expect(notify.add).withContext('notify error').toHaveBeenCalled()
    expect(notify.add).withContext('error message').toHaveBeenCalledWith('Usuario o contraseña invalida.');

  }));
  it('logout', waitForAsync(() => {
    const auth = TestBed.inject(AuthService);
    auth.login('B' + 'earer 12345', '12345', 'usuario', [])
    component.logInOut()
    fixture.detectChanges();
    expect(component.loginSrv.isAuthenticated).withContext('is autenticated').toBeFalsy();
  }));
});

describe('LoginFormComponent', () => {
  const apiURL = environment.securityApiURL
  let component: LoginFormComponent;
  let fixture: ComponentFixture<LoginFormComponent>;
  let loginSrv: LoginService

  beforeEach(async () => {
    await TestBed.configureTestingModule({
    providers: [LoginService, AuthService, NotificationService, LoggerService, WindowService],
    imports: [HttpClientTestingModule, RouterTestingModule, FormsModule, LoginFormComponent],
    schemas: [NO_ERRORS_SCHEMA]
})
    .compileComponents();

    fixture = TestBed.createComponent(LoginFormComponent);
    component = fixture.componentInstance;
    loginSrv = TestBed.inject(LoginService)
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it('autenticated', waitForAsync(() => {
    const res = {
      "success": true,
      "token": "B" + "earer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c3IiOiJhZG1AZXhhbXBsZS5jb20iLCJuYW1lIjoiQWRtaW5pc3RyYWRvciIsInJvbGVzIjpbIlVzdWFyaW9zIiwiQWRtaW5pc3RyYWRvcmVzIl0sImlhdCI6MTY3MDU4NTE0MSwiZXhwIjoxNjcwNTg1NDQxLCJhdWQiOiJhdXRob3JpemF0aW9uIiwiaXNzIjoiTWljcm9zZXJ2aWNpb3NKV1QifQ.R4w4DH3HfVssI7TSO0u0z2uCu7BrhLXN5YdxEyx3uOIzhENycz0vL8B0_etz8kSz8KVWM0hOLqf0J7XOwNci1ksf4ZWenykapG-AuEkQkX2Y7ZTjECscor5dT3Cmj0swI12Yx-FL3r3OXDRppSnoOCvOE_w-ardwHt48QCU5u7YjvXjcP34bavFDjYpD7dvy5eoT-TDb0Un4XYkBVhR18u0ogH9TKoxF0lt8TSh5ckwjcZ4_KF3E4TGAIHId6UbuxUqMNTyJW0gkJR7iCQPn4Ez3osvZG4Rvj7VT_VbX_9EzTdXOJ9ZeuMpSuhk-AmFyXCeu8wcD-mU7JWn8RW2OCQ",
      "refresh": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c3IiOiJhZG1AZXhhbXBsZS5jb20iLCJpYXQiOjE2NzA1ODUxNDEsIm5iZiI6MTY3MDU4NTQ0MSwiZXhwIjoxNjcwNTg2MzQxLCJhdWQiOiJhdXRob3JpemF0aW9uIiwiaXNzIjoiTWljcm9zZXJ2aWNpb3NKV1QifQ.rh8bgIhlPKkKeiCHBvkT2qZruAvdFjldfD9PCeC4ZN0",
      "name": "Administrador",
      "roles": ["Usuarios", "Administradores"],
      "expires_in": 300
    }
    const httpMock = TestBed.inject(HttpTestingController);
    spyOn(component, 'reloadPage')
    loginSrv.logout()
    component.txtUsuario = 'demo@example.com'
    component.txtPassword = 'P@$$w0rd'
    component.logInOut()
    fixture.detectChanges();

    const req = httpMock.expectOne(apiURL + 'login');
    req.flush(res);
    httpMock.verify();

    expect(component.loginSrv.isAuthenticated).withContext('is autenticated').toBeTruthy();

  }));
  it('not autenticated', waitForAsync(() => {
    const res = { "success": false }
    const httpMock = TestBed.inject(HttpTestingController);
    spyOn(component, 'reloadPage')
    loginSrv.logout()
    component.txtUsuario = 'demo@example.com'
    component.txtPassword = 'P@$$w0rd'
    component.logInOut()
    fixture.detectChanges();

    const req = httpMock.expectOne(apiURL + 'login');
    req.flush(res);
    httpMock.verify();

    expect(component.loginSrv.isAuthenticated).withContext('is autenticated').toBeFalsy();
    expect(component.errorMessage).withContext('error message').toBe('Usuario o contraseña invalida.');

  }));
});
