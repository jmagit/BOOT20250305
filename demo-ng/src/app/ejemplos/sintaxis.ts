/* eslint-disable @typescript-eslint/no-unused-vars */
/* eslint-disable prefer-const */
let a : number = 3
let b = '€2'
// b=2

let c = a + b

class Persona {
  readonly MAYORIA_EDAD = 18;
  constructor(private id: number, private nombre: string, private apellidos: string, protected edad: number) { }
  public get Id(): number { return this.Id }
    public get Nombre(): string {
        return this.nombre.toUpperCase();
    }
    public set Nombre(valor: string) {
      if (this.nombre === valor) return;
      if (!valor)
        throw new RangeError('null value');
      this.nombre = valor;
      // this.onPropertyChange('Nombre');
    }
    public getNombreCompleto(): string { return `${this.nombre} ${this.apellidos}`}
    public esMayorDeEdad(): boolean { return this.edad >= this.MAYORIA_EDAD }
    public cumpleAños(): void { if(!this.esMayorDeEdad()) this.edad++ }
}

let o = new Persona(1,'Pepito','Grillo',99);
let nom = o?.Nombre?.toUpperCase()

enum Direction { Up = 1, Down, Left, Right, }
