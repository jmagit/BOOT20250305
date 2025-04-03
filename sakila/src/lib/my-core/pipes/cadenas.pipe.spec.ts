import { CapitalizePipe, ElipsisPipe, ErrorMessagePipe, StripTagsPipe } from './cadenas.pipe';

describe('ElipsisPipe', () => {
  let pipe = new ElipsisPipe();
  beforeAll(() => {
    pipe = new ElipsisPipe();
  })
  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });
  [
    { input: '1234567890', param: 4, output: '123\u2026' },
    { input: '1234', param: 4, output: '1234' },
    { input: '12345', param: 4, output: '123\u2026' },
    { input: '1234567890', param: -1, output: '1234567890' },
    { input: '', param: 0, output: '' },
    { input: '', param: 10, output: '' },
  ].forEach(caso => {
    it(`OK: '${caso.input}' (${caso.param})  -> '${caso.output}'`, () =>
      expect(pipe.transform(caso.input, caso.param)).toBe(caso.output));
  });

});

describe('CapitalizePipe', () => {
  let pipe = new CapitalizePipe();
  beforeAll(() => {
    pipe = new CapitalizePipe();
  })
  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });
  [
    { input: 'hola mundo', output: 'Hola mundo' },
    { input: 'hola Mundo', output: 'Hola mundo' },
    { input: 'HOLA MUNDO', output: 'Hola mundo' },
  ].forEach(caso => {
    it(`OK: '${caso.input}' -> '${caso.output}'`, () =>
      expect(pipe.transform(caso.input)).toBe(caso.output));
  });

});

describe('StripTagsPipe', () => {
  let pipe = new StripTagsPipe();
  beforeAll(() => {
    pipe = new StripTagsPipe();
  })
  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });
  [
    { input: 'Hola mundo', output: 'Hola mundo' },
    { input: '<h1>Hola mundo</h1>', output: 'Hola mundo' },
    { input: 'Hola <b>mundo</b>', output: 'Hola mundo' },
    { input: '', output: '' },
  ].forEach(caso => {
    it(`OK: '${caso.input}' -> '${caso.output}'`, () =>
      expect(pipe.transform(caso.input)).toBe(caso.output));
  });
  [
    { input: 'Hola bebe', output: 'Hola bebe' },
    { input: 'Hola <b>mundo</b>', output: 'Hola <b>mundo</b>' },
    { input: '<h1><i>Hola</i> <script>a todo el </script><b>mundo</b></h1>', output: '<i>Hola</i> a todo el <b>mundo</b>' },
    { input: '', output: '' },
  ].forEach(caso => {
    it(`Negrita y cursivas permitidas: '${caso.input}' -> '${caso.output}'`, () =>
      expect(pipe.transform(caso.input, 'i', 'b')).toBe(caso.output));
  });

});

describe('ErrorMessagePipe', () => {
  let pipe = new ErrorMessagePipe();
  beforeAll(() => {
    pipe = new ErrorMessagePipe();
  })
  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });
  it('Sin errores', () => {
    expect(pipe.transform(null)).toBe('')
  });

  it('Con errores', () => {
    const errors = {
      required: true,
      minlength: { requiredLength: 10 },
      maxlength: { requiredLength: 10 },
      pattern: true,
      email: true,
      min: { min: 10 },
      max: { max: 10 },
      simple: 'Mensaje simple',
      simpleConPunto: 'Mensaje simple.',
      complejo: { message : 'Mensaje contenido'},
      complejoConPunto: { message : 'Mensaje contenido.'}
    }
    expect(pipe.transform(errors)).toBe('Es obligatorio. Como mínimo debe tener 10 caracteres. Como máximo debe tener 10 caracteres. El formato no es correcto. El formato del correo electrónico no es correcto. El valor debe ser mayor o igual a 10. El valor debe ser inferior o igual a 10. Mensaje simple. Mensaje simple. Mensaje contenido. Mensaje contenido.');
  });
  it('Con patternMsg', () => {
    const errors = {
      pattern: true,
    }
    const patternMsg="Hola mundo"
    expect(pipe.transform(errors, patternMsg)).toBe(`${patternMsg}.`);
  });

});
