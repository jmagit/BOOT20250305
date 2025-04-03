/* eslint-disable @angular-eslint/directive-selector */
import { Directive, Input } from '@angular/core';
import { AbstractControl, NG_VALIDATORS, ValidationErrors, Validator, ValidatorFn } from '@angular/forms';

export type Comparator<T> = (a: T, b: T) => boolean;
const hoy = () => (new Date()).toISOString().substring(0, 10)

export function DateValidation(limite: string, compara: Comparator<number>): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) { return null; }
    const fechaLimite = (new Date(limite)).valueOf();
    const fechaIntroducida = (new Date(control.value)).valueOf();
    if (isNaN(fechaLimite))
      throw new Error('No es una fecha correcta')
    return !isNaN(fechaIntroducida) && compara(fechaIntroducida, fechaLimite) ? null : { date: `Invalid date` }
  };
}

@Directive({
  selector: '[before][formControlName],[before][formControl],[before][ngModel]',
  standalone: true,
  providers: [{ provide: NG_VALIDATORS, useExisting: BeforeValidator, multi: true }]
})
export class BeforeValidator implements Validator {
  @Input() before = ''

  validate(control: AbstractControl): ValidationErrors | null {
    return DateValidation(this.before, (a, b) => a < b)(control)?
      { before: `Tiene que ser anterior al ${(new Date(this.before)).toLocaleDateString()}` } : null;
  }
}
@Directive({
  selector: '[beforeOrEquals][formControlName],[beforeOrEquals][formControl],[beforeOrEquals][ngModel]',
  standalone: true,
  providers: [{ provide: NG_VALIDATORS, useExisting: BeforeOrEqualsValidator, multi: true }]
})
export class BeforeOrEqualsValidator implements Validator {
  @Input() beforeOrEquals = ''

  validate(control: AbstractControl): ValidationErrors | null {
    return DateValidation(this.beforeOrEquals, (a, b) => a <= b)(control)?
      { beforeOrEquals: `Tiene que ser anterior o igual al ${(new Date(this.beforeOrEquals)).toLocaleDateString()}` } : null;
  }
}

@Directive({
  selector: '[after][formControlName],[after][formControl],[after][ngModel]',
  standalone: true,
  providers: [{ provide: NG_VALIDATORS, useExisting: AfterValidator, multi: true }]
})
export class AfterValidator implements Validator {
  @Input() after = ''

  validate(control: AbstractControl): ValidationErrors | null {
    return DateValidation(this.after, (a, b) => a > b)(control)?
    { after: `Tiene que ser posterior al ${(new Date(this.after)).toLocaleDateString()}` } : null;

  }
}
@Directive({
  selector: '[afterOrEquals][formControlName],[afterOrEquals][formControl],[afterOrEquals][ngModel]',
  standalone: true,
  providers: [{ provide: NG_VALIDATORS, useExisting: AfterOrEqualsValidator, multi: true }]
})
export class AfterOrEqualsValidator implements Validator {
  @Input() afterOrEquals = ''

  validate(control: AbstractControl): ValidationErrors | null {
    return DateValidation(this.afterOrEquals, (a, b) => a >= b)(control)?
      { afterOrEquals: `Tiene que ser posterior o igual al ${(new Date(this.afterOrEquals)).toLocaleDateString()}` } : null;
  }
}

@Directive({
  selector: '[past][formControlName],[past][formControl],[past][ngModel]',
  standalone: true,
  providers: [{ provide: NG_VALIDATORS, useExisting: PastValidator, multi: true }]
})
export class PastValidator implements Validator {
  validate(control: AbstractControl): ValidationErrors | null {
    return DateValidation(hoy(), (a, b) => a < b)(control) ?
      { past: `Tiene que ser anterior a hoy` } : null;
  }
}

@Directive({
  selector: '[pastOrPresent][formControlName],[pastOrPresent][formControl],[pastOrPresent][ngModel]',
  standalone: true,
  providers: [{ provide: NG_VALIDATORS, useExisting: PastOrPresentValidator, multi: true }]
})
export class PastOrPresentValidator implements Validator {
  validate(control: AbstractControl): ValidationErrors | null {
    return DateValidation(hoy(), (a, b) => a <= b)(control) ?
      { pastOrPresent: `Tiene que ser anterior o igual a hoy` } : null;
  }
}

@Directive({
  selector: '[future][formControlName],[future][formControl],[future][ngModel]',
  standalone: true,
  providers: [{ provide: NG_VALIDATORS, useExisting: FutureValidator, multi: true }]
})
export class FutureValidator implements Validator {
  validate(control: AbstractControl): ValidationErrors | null {
    return DateValidation(hoy(), (a, b) => a > b)(control) ?
      { future: `Tiene que ser posterior a hoy` } : null;
  }
}

@Directive({
  selector: '[futureOrPresent][formControlName],[futureOrPresent][formControl],[futureOrPresent][ngModel]',
  standalone: true,
  providers: [{ provide: NG_VALIDATORS, useExisting: FutureOrPresentValidator, multi: true }]
})
export class FutureOrPresentValidator implements Validator {
  validate(control: AbstractControl): ValidationErrors | null {
    return DateValidation(hoy(), (a, b) => a >= b)(control) ?
      { futureOrPresent: `Tiene que ser posterior o igual a hoy` } : null;
  }
}

export const VALIDATORS_DATES = [BeforeValidator, BeforeOrEqualsValidator, AfterValidator, AfterOrEqualsValidator,
  PastValidator, PastOrPresentValidator, FutureValidator, FutureOrPresentValidator]
