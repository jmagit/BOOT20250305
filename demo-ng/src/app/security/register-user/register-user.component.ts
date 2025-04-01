import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators, FormArray, ValidatorFn, AbstractControl, ValidationErrors, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { User, RegisterUserDAO, LoginService } from '../security.service';
import { Router } from '@angular/router';
import { NotificationService, NotificationType } from 'src/app/common-services';
import { ErrorMessagePipe, LoggerService } from '@my/core';

@Component({
    selector: 'app-register-user',
    templateUrl: './register-user.component.html',
    styleUrls: ['./register-user.component.css'],
    imports: [FormsModule, ReactiveFormsModule]
})
export class RegisterUserComponent implements OnInit {
  public miForm: FormGroup = new FormGroup({});
  private model: User = new User();
  private pipe = new ErrorMessagePipe();

  constructor(private dao: RegisterUserDAO, private notify: NotificationService,
    private out: LoggerService, private router: Router, private login: LoginService) { }

  passwordMatchValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => control?.get('passwordValue')?.value === control?.get('passwordConfirm')?.value
      ? null : { 'mismatch': 'Son distintos' };
  }

  ngOnInit() {
    // const fa = new FormArray([]);
    // this.model.roles.forEach(r => fa.push(
    //   new FormGroup({ role: new FormControl(r.role , Validators.required) })
    // ));
    this.miForm = new FormGroup({
      idUsuario: new FormControl('', [Validators.required, Validators.minLength(2), Validators.maxLength(100), Validators.email,]),
      nombre: new FormControl('', [Validators.required, Validators.minLength(2), Validators.maxLength(20)]),
      password: new FormGroup({
        passwordValue: new FormControl('', [Validators.required, Validators.pattern(/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\W).{8,}$/)]),
        passwordConfirm: new FormControl('', Validators.required),
      }, this.passwordMatchValidator()),
      roles: new FormArray([])
    });
    // for (const name in this.miForm.controls) {
    //   if (this.miForm.controls[name] instanceof FormControl) {
    //     this.miForm.controls[name].valueChanges.subscribe(
    //       data => { this.formatErrorMessage(this.miForm.controls[name] as FormControl); }
    //     );
    //     // this.formatErrorMessage(this.miForm.controls[name] as FormControl);
    //     this.miForm.controls[name].setValue(this.miForm.controls[name].value)
    //   }
    // }
  }
  public getErrorMessage(name: string): string {
    const cntr = this.miForm.get(name)
    let msg = '';
    if (cntr)
      msg = this.pipe.transform(cntr.errors)
    return msg;
  }
  private formatErrorMessage(cntr: FormControl): void {
    cntr.setErrors(Object.assign({}, cntr.errors, { 'customMsg': this.pipe.transform(cntr.errors) }));
  }
  addRole(): void {
    (this.miForm.get('roles') as FormArray).push(
      new FormGroup({ role: new FormControl('Usuarios', Validators.required) })
    );
  }
  deleteRole(ind: number): void {
    (this.miForm.get('roles') as FormArray).removeAt(ind);
  }
  send(): void {
    const data = this.miForm.value;
    this.model = ({
      idUsuario: data.idUsuario,
      password: data.password.passwordValue,
      nombre: data.nombre,
      roles: data.roles
    } as User);
    this.dao.add(this.model).subscribe({
      next: () => {
        this.login.login(data.idUsuario, data.password.passwordValue).subscribe({
          next: datos => {
            if (datos) {
              this.notify.add('Usuario registrado', NotificationType.log);
              this.router.navigateByUrl('/');
            } else {
              this.notify.add('Error en el registro.');
            }
          },
          error: err => { this.notify.add(err.error.detail || err.message); }
        });
      },
      error: err => { this.notify.add(err.error.detail || err.message); }
    });
  }
}
