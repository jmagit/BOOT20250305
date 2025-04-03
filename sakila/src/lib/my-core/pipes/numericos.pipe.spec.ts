import { ToComaDecimalPipe } from './numericos.pipe';

describe('ToComaDecimalPipe', () => {
  it('create an instance', () => {
    const pipe = new ToComaDecimalPipe();
    expect(pipe).toBeTruthy();
  });
  it('Con punto decimal', () => {
    const pipe = new ToComaDecimalPipe();
    expect(pipe.transform('12.3')).toBe('12,3');
  });
  it('Sin punto decimal', () => {
    const pipe = new ToComaDecimalPipe();
    expect(pipe.transform('123')).toBe('123');
  });
  it('Con coma decimal', () => {
    const pipe = new ToComaDecimalPipe();
    expect(pipe.transform('12,3')).toBe('12,3');
  });
  it('No numérico', () => {
    const pipe = new ToComaDecimalPipe();
    expect(pipe.transform('Abc')).toBe('Abc');
  });
  it('Numérico', () => {
    const pipe = new ToComaDecimalPipe();
    expect(pipe.transform(1.5)).toBe('1,5');
  });
});
