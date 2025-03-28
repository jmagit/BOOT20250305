// a = 1
// b = '2'
// d = 3

// c = +a + +b + +d

// console.log(c)
// console.log(c * 2)
// c = 11
// const kk = () => 0

// function kk() {
//     var a = 1
//     if(true) {
//         var b = 2
//     }
//     c = a + b
//     return c
// }
// c = () => 'algo'
// c()
// if(kk() === c) {
//     console.log('ok')
// } else {
//     console.log('ko')
// }
// console.log(globalThis)

// t = [10, 20, 30]
// t[7]=70
// for(let i in t) {
//     console.log(t[i])
// }
// for(let i of t) {
//     console.log(i)
// }
t = { a: 1, b: 2 }
t.c = 3
for(let i in t) {
    console.log(`${i} = ${t[i]}`)
}
// for(let i of t) {
//     console.log(i)
// }

function MiClase(elId, elNombre) {
    var obj = this
    obj.id = elId;
    obj.nombre = elNombre;
    obj.muestraId = function() {
      alert("El ID del objeto es " + obj.id);
    }
    obj.ponNombre = function(nom) {
        obj.nombre=nom.toUpperCase();
    }
  }
  let obj1 = new MiClase(1, "objeto1");
  obj1.muestraId();
  fn = obj1.muestraId
  fn()

  