/* eslint-disable @angular-eslint/directive-selector */
import { Directive, ElementRef, forwardRef, Input, OnChanges, SimpleChanges } from '@angular/core';
import { ValidatorFn, AbstractControl, NG_VALIDATORS, Validator, ValidationErrors, NgModel } from '@angular/forms';
import isIBAN from 'validator/lib/isIBAN';
// npm i validator
// npm i @types/validator -D

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

export function lowercaseValidator(): ValidatorFn {
  return (control: AbstractControl): Record<string, string> | null => {
    if (!control.value) { return null; }
    return control.value === control.value.toLowercase() ? null : { lowercase: 'Tiene que estar en mayúsculas' }
  };
}
@Directive({
  selector: '[lowercase][formControlName],[lowercase][formControl],[lowercase][ngModel]',
  providers: [{ provide: NG_VALIDATORS, useExisting: LowercaseValidator, multi: true }]
})
export class LowercaseValidator implements Validator {
  validate(control: AbstractControl): ValidationErrors | null {
    return lowercaseValidator()(control);
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

export function notblankValidator(control: AbstractControl): Record<string, string> | null {
  return (control.value ?? '').toString().trim() === '' ? { notblank: 'Requerido, no debe estar en blanco' } : null
}

@Directive({
  selector: '[notblank][formControlName],[notblank][formControl],[notblank][ngModel]',
  providers: [{ provide: NG_VALIDATORS, useExisting: NotblankValidator, multi: true }],
  standalone: true
})
export class NotblankValidator implements Validator {
  validate(control: AbstractControl): ValidationErrors | null {
    return notblankValidator(control);
  }
}

// https://es.iban.com/estructura

export function ibanValidator(control: AbstractControl): Record<string, string> | null {
  if (!control.value) { return null; }
  return isIBAN(control.value.toString()) ? null : { iban: 'No es un IBAN valido' }
}
@Directive({
  selector: '[iban][formControlName],[iban][formControl],[iban][ngModel]',
  providers: [{ provide: NG_VALIDATORS, useExisting: IbanValidator, multi: true }]
})
export class IbanValidator implements Validator {
  validate(control: AbstractControl): ValidationErrors | null {
    return ibanValidator(control);
  }
}

export function equalsToValidator(cntrlBind?: AbstractControl): ValidatorFn {
  let subscribe: boolean = false;
  return (control: AbstractControl): ValidationErrors | null => {
    if (!subscribe && cntrlBind) {
      subscribe = true;
      cntrlBind.valueChanges.subscribe(() => {  control.updateValueAndValidity();  });
    }
    return (!cntrlBind || control.value !== cntrlBind.value) ? { 'equalsTo': `${control.value} distinto de ${cntrlBind?.value}` } : null;
  }
}
@Directive({
  selector: '[equalsTo]', providers: [{ provide: NG_VALIDATORS, useExisting: forwardRef(() => EqualsToValidator), multi: true }],
})
export class EqualsToValidator implements Validator, OnChanges {
  @Input({alias: 'equalsTo', required: true }) cntrlBind?: NgModel
  private validator: ValidatorFn = () => null
  ngOnChanges(_changes: SimpleChanges): void { this.validator = equalsToValidator(this.cntrlBind?.control) }
  validate(control: AbstractControl): ValidationErrors | null {  return this.validator(control) }
}

export const MIS_VALIDADORES = [NIFNIEValidator, TypeValidator, UppercaseValidator, LowercaseValidator,
  NotblankValidator, EqualsToValidator, IbanValidator,]
