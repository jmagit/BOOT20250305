import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'exec',
    standalone: true
})
export class ExecPipe implements PipeTransform {
  // eslint-disable-next-line @typescript-eslint/no-unsafe-function-type, @typescript-eslint/no-explicit-any
  transform(fn: Function, ...args: any[]): any {
    return fn(...args);
  }
}

@Pipe({
    name: 'toComaDecimal',
    standalone: true
})
export class ToComaDecimalPipe implements PipeTransform {
  transform(value: number | string): string {
    if (typeof (value) === 'number') {
      value = value.toString();
    }
    if (typeof (value) === 'string') {
      return value.replace(/\./g, ',');
    }
    return value;
  }
}

export const PIPES_NUMERICOS = [ ToComaDecimalPipe, ExecPipe, ]
