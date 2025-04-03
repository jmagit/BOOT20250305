/* eslint-disable @angular-eslint/directive-selector */
import { Directive, ElementRef, forwardRef } from '@angular/core';
import { ValidatorFn, AbstractControl, NG_VALIDATORS, Validator, ValidationErrors } from '@angular/forms';

export function nifnieValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) { return null; }
    const err = { nifnie: { invalidFormat: true, invalidChar: true, message: 'NIF o NIE invalido' } };
    if (/^((\d{1,8})|([X-Z]\d{7}))[TRWAGMYFPDXBNJZSQVHLCKE]$/.test(control.value.toUpperCase())) {
      const charsValue: Record<string, string> = { X: '0', Y: '1', Z: '2', };
      const numberValue = +((control.value as string).slice(0, -1).replace(/[X,Y,Z]/g, char => charsValue[char]));
      err.nifnie.invalidFormat = false;
      return control.value.toUpperCase().endsWith('TRWAGMYFPDXBNJZSQVHLCKE'.charAt(numberValue % 23)) ? null : err;
    } else { return err; }
  };
}
@Directive({
  selector: '[nifnie][formControlName],[nifnie][formControl],[nifnie][ngModel]',
  standalone: true,
  providers: [{ provide: NG_VALIDATORS, useExisting: NIFNIEValidator, multi: true }]
})
export class NIFNIEValidator implements Validator {
  validate(control: AbstractControl): ValidationErrors | null {
    return nifnieValidator()(control);
  }
}

export function uppercaseValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) { return null; }
    return control.value === control.value.toUpperCase() ? null : { uppercase: 'Tiene que estar en mayúsculas' }
  };
}
@Directive({
  selector: '[uppercase][formControlName],[uppercase][formControl],[uppercase][ngModel]',
  standalone: true,
  providers: [{ provide: NG_VALIDATORS, useExisting: UppercaseValidator, multi: true }]
})
export class UppercaseValidator implements Validator {
  validate(control: AbstractControl): ValidationErrors | null {
    return uppercaseValidator()(control);
  }
}

@Directive({
  selector: '[type][formControlName],[type][formControl],[type][ngModel]',
  standalone: true,
  providers: [
      { provide: NG_VALIDATORS, useExisting: forwardRef(() => TypeValidator), multi: true }
  ]
})
export class TypeValidator implements Validator {
  constructor(private elem: ElementRef) { }
  validate(control: AbstractControl): ValidationErrors | null {
      const valor = control.value;
      if (valor) {
        const dom = this.elem.nativeElement;
        if (dom.validity) { // dom.checkValidity();
          return (dom.validity.typeMismatch || dom.validity.stepMismatch) ? { 'type': dom.validationMessage } : null;
        }
      }
      return null;
  }
}

export function notblankValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    return !control.value || control.value.toString().trim() === '' ? { notblank: 'Requerido, no debe estar en blanco' } : null
  };
}

@Directive({
  selector: '[notblank][formControlName],[notblank][formControl],[notblank][ngModel]',
  providers: [{ provide: NG_VALIDATORS, useExisting: NotblankValidator, multi: true }],
  standalone: true
})
export class NotblankValidator implements Validator {
  validate(control: AbstractControl): ValidationErrors | null {
    return notblankValidator()(control);
  }
}

export const MIS_VALIDADORES = [NIFNIEValidator, TypeValidator, UppercaseValidator, NotblankValidator]
