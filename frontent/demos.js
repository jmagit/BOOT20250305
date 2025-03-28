// a = 1
// b = '2'
// d = 3

// c = +a + +b + +d

// console.log(c)
// console.log(c * 2)
c = 11
const kk = () => 0

function kk() {
    var a = 1
    if(true) {
        var b = 2
    }
    c = a + b
    return c
}
c = () => 'algo'
c()
if(kk() === c) {
    console.log('ok')
} else {
    console.log('ko')
}
console.log(globalThis)
