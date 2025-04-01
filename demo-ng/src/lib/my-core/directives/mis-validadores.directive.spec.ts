import { FormControl, FormsModule } from '@angular/forms';
import { ibanValidator, isNotBlankValidator, NIFNIEValidator, nifnieValidator, uppercaseValidator, UppercaseValidator } from './mis-validadores.directive'
import { Component, ViewChild } from '@angular/core';
import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

describe('Mis validadores', () => {
  describe('isNotBlankValidator', () => {
    describe('OK', () => {
      it('Una cadena', () => {
        const valor = new FormControl('valor')

        const result = isNotBlankValidator(valor)

        expect(result).toBeNull()
      });

      it('Con un 0', () => {
        expect(isNotBlankValidator(new FormControl(0))).toBeNull()
      });

    });
    describe('KO', () => {
      it('Solo blancos', () => {
        const valor = new FormControl('    ')

        let result = isNotBlankValidator(valor)

        expect(result).withContext('Solo blancos').not.toBeNull()
        expect(result?.['isNotBlank']).toBeDefined()
        expect(result?.['isNotBlank']).toBe('No puede estar vacío')
      });
      it('cadena vacía', () => {
        const valor = new FormControl('')

        const result = isNotBlankValidator(valor)

        expect(result).not.toBeNull()
        expect(result?.['isNotBlank']).toBeDefined()
        expect(result?.['isNotBlank']).toBe('No puede estar vacío')
      });

      ['', '    ', null, undefined].forEach(item => {
        it(`con valor "${item}"`, () => {
          const valor = new FormControl(item)

          const result = isNotBlankValidator(valor)

          expect(result).not.toBeNull()
          expect(result?.['isNotBlank']).toBeDefined()
          expect(result?.['isNotBlank']).toBe('No puede estar vacío')
        });
      });
    });
  });
});

class Calculadora {
  suma(a: number, b: number) { return a + b }
  divide(a: number, b: number) {
    if (b === 0)
      throw new Error('Divide by 0')
    return a / b
  }
}

describe('Otros ejemplos', () => {
  function divide(a: number, b: number) {
    if (b === 0)
      throw new Error('Divide by 0')
    return a / b
  }

  describe('mock', () => {
    let calc: Calculadora;

    function depediente(a: number, b: number) { return calc.suma(a,b) * 2 }

    beforeEach(() => {
      calc = new Calculadora()
    })
    it('real', () => {
      expect(calc.suma(2,2)).toBe(4)
    })
    it('dependencia con mock', () => {
      let dep = spyOn(calc, 'suma').and.returnValue(3)
      expect(depediente(2,2)).toBe(6)
      expect(dep).toHaveBeenCalledWith(2,2)
    })
    it('dependencia real', () => {
      expect(depediente(1,2)).toBe(6)
    })
    it('siempre pasa', () => {
      let cantidad = 0
      let total = 100
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      let media = total / cantidad
      // expect(0.1 + 0.2).toBe(0.3)
      // expect(1 - 0.9).toBe(0.1)
      expect((0.1 + 0.2) + (1 - 0.9)).toBe(0.4)
    })
  });
  describe('kk', () => {
    describe('OK', () => {
      it('Tengo que probar esto también');

      it('Periódico', () => {
        expect(divide(1, 3)).toBe(0.3333333333333333)
        expect(divide(1, 3)).toBeCloseTo(0.3333, 4)
      });

      it('Divide por 0', () => {
        expect(1 / 0).toBePositiveInfinity()
        try {
          // eslint-disable-next-line @typescript-eslint/no-unused-vars
          const result = divide(1, 0)
          fail('No se lanza la excepcion')
        } catch (error) {
        }
        //expect(divide(1, 0)).toThrowError('Divide by 0')
        expect(() => divide(1, 0)).toThrow()
      });
    });
    describe('KO', () => {
      it('se ha quedado a medias', () => {
        const valor = new FormControl('')

        const result = isNotBlankValidator(valor)

        expect(result).not.toBeNull()
        pending('me falta mirar ...')
      });

      xit('falla siempre', () => {
        fail('fallo forzado')
      });

      ['', '    ', null, undefined].forEach(item => {
        it(`con valor "${item}"`, () => {
          const valor = new FormControl(item)

          const result = isNotBlankValidator(valor)

          expect(result).not.toBeNull()
          expect(result?.['isNotBlank']).toBeDefined()
          expect(result?.['isNotBlank']).toBe('No puede estar vacío')
        });
      });
    });
  });
});

describe('nifnieValidator', () => {
  const esNIFNIE = nifnieValidator()
  const control = new FormControl('input');

  describe('NIFNIE OK', () => {
    ['12345678z', '12345678Z', '1234S', '4g', 'X1234567L', 'Z1234567R', null, ''].forEach(caso => {
      it(`NIFNIE: ${caso}`, () => {
        control.setValue(caso);
        expect(esNIFNIE(control)).toBeNull()
      })
    })
  });

  describe('NIFNIE KO', () => {
    ['1234J', '12345678', 'Z', 'Z12345678', 'Y1234567L'].forEach(caso => {
      it(`NIFNIE: ${caso}`, () => {
        control.setValue(caso);
        expect(esNIFNIE(control)).not.toBeNull()
      })
    })
  });
  it('NIFNIEValidator', () => {
    const directive = new NIFNIEValidator();
    control.setValue(null);
    expect(directive.validate(control)).toBeNull();
  })
});

@Component({
  template: `<input type="text" [(ngModel)]="valor" #myInput="ngModel" nifnie >`,
})
class nifnieValidatorHostComponent {
  @ViewChild('myInput') control?: FormControl
  valor = '';
}

describe('NIFNIEValidator', () => {
  let component: nifnieValidatorHostComponent;
  let fixture: ComponentFixture<nifnieValidatorHostComponent>;
  // const control = new FormControl('input');

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [nifnieValidatorHostComponent, NIFNIEValidator],
      imports: [FormsModule,]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(nifnieValidatorHostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it(`OK`, waitForAsync(() => {
    const valor = '12345678z'
    component.valor = valor
    fixture.detectChanges()
    fixture.whenStable().then(() => {
      expect(component.control).withContext('control').toBeDefined()
      expect(component.control?.value).withContext('value').toBe(valor)
      expect(component.control?.valid).withContext('valid').toBeTrue()
      expect(component.control?.errors).withContext('errors').toBeNull()
    })
  }))

  it(`KO`, waitForAsync(async () => {
    const valor = '12345678'
    component.valor = valor
    fixture.detectChanges()
    await fixture.whenStable()
    expect(component.control).withContext('control').toBeDefined()
    expect(component.control?.value).withContext('value').toBe(valor)
    expect(component.control?.invalid).withContext('invalid').toBeTrue()
    expect(component.control?.errors).withContext('errors').toBeDefined()
    expect(component.control?.errors?.['nifnie']).withContext('nifnie').toBeDefined()
  }))
});

describe('uppercaseValidator', () => {
  const control = new FormControl('input');
  describe('Uppercase OK', () => {
    ['12345678', 'CASA', null].forEach(caso => {
      it(`Uppercase: ${caso}`, () => {
        control.setValue(caso);
        expect(uppercaseValidator()(control)).toBeNull()
      })
    })
  });

  describe('Uppercase KO', () => {
    ['Algo', '12345678z', 'casa'].forEach(caso => {
      it(`Uppercase: ${caso}`, () => {
        control.setValue(caso);
        expect(uppercaseValidator()(control)).not.toBeNull()
      })
    })
  });

  it('UppercaseValidator', () => {
    const directive = new UppercaseValidator();
    control.setValue(null);
    expect(directive.validate(control)).toBeNull();
  })
});


describe('ibanValidator', () => {
  const control = new FormControl('input');
  const validator = ibanValidator
  describe('OK', () => {
    ['ES7921000813610123456789', 'BE71096123456769', null, ''].forEach(caso => {
      it(`Caso: ${caso}`, () => {
        control.setValue(caso);
        expect(validator(control)).toBeNull()
      })
    })
  });

  describe('KO', () => {
    ['ES7921000813610123456788', 'BE7921000813610123456789', '  '].forEach(caso => {
      it(`Caso: ${caso}`, () => {
        control.setValue(caso);
        expect(validator(control)).not.toBeNull()
      })
    })
  });
});

