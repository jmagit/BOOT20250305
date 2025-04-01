import { Component, OnDestroy, OnInit } from '@angular/core';
import { LOGIN_FORM_CLOSE_EVENT, LOGIN_FORM_OPEN_EVENT, LoginService } from '../security.service';
import { ActivatedRoute, Router } from '@angular/router';
import { EventBusService, NotificationService } from 'src/app/common-services';
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';

export class BaseComponent {
  txtUsuario = 'adm@example.com';
  txtPassword = 'P@$$w0rd';

  constructor(public loginSrv: LoginService, private notify: NotificationService,
    private route: ActivatedRoute, private router: Router, protected eventBus: EventBusService) { }
  logInOut() {
    if (this.loginSrv.isAuthenticated) {
      this.loginSrv.logout();
      this.reloadPage()
    } else {
      this.loginSrv.login(this.txtUsuario, this.txtPassword).subscribe({
        next: data => {
          if (data) {
            if (this.route.snapshot.queryParams['returnUrl']) {
              this.router.navigateByUrl(this.route.snapshot.queryParams['returnUrl']);
              return
            }
            this.reloadPage()
          } else {
            this.notificaError('Usuario o contraseña invalida.')
          }
        },
        error: err => { this.notificaError(err.message); }
      });
    }
  }

  protected notificaError(error: string) {
    this.notify.add(error);
  }

  registrar() {
    this.router.navigateByUrl('/registro');
  }

  reloadPage(): void {
    this.router.navigateByUrl(this.router.url, {onSameUrlNavigation: 'reload'})
  }
}

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css'],
    imports: [FormsModule]
})
export class LoginComponent extends BaseComponent implements OnDestroy {
  private login$: Subscription;
  private logout$: Subscription;
  visible = true
  constructor(loginSrv: LoginService, notify: NotificationService, route: ActivatedRoute,
    router: Router, eventBus: EventBusService) {
    super(loginSrv, notify, route, router, eventBus)
    this.login$ = this.eventBus.on(LOGIN_FORM_OPEN_EVENT, () => {
      this.visible = false
    })
    this.logout$ = this.eventBus.on(LOGIN_FORM_CLOSE_EVENT, () => {
      this.visible = true
    })
  }
  ngOnDestroy(): void {
    this.login$.unsubscribe()
    this.logout$.unsubscribe()
  }
}

@Component({
    selector: 'app-login-form',
    templateUrl: './login-form.component.html',
    styleUrls: ['./login.component.css'],
    imports: [FormsModule]
})
export class LoginFormComponent extends BaseComponent implements OnInit, OnDestroy {
  errorMessage = '';
  constructor(loginSrv: LoginService, notify: NotificationService, route: ActivatedRoute,
    router: Router, eventBus: EventBusService) {
    super(loginSrv, notify, route, router, eventBus)
  }
  ngOnInit(): void {
    this.eventBus.emit(LOGIN_FORM_OPEN_EVENT);
  }
  ngOnDestroy(): void {
    this.eventBus.emit(LOGIN_FORM_CLOSE_EVENT);
  }

  protected override notificaError(error: string) {
    this.errorMessage = error ?? 'Usuario o contraseña invalida.';
  }
}
