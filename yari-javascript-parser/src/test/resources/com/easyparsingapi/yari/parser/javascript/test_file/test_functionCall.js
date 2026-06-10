function multiply(a, b) {
  return a * b
}
multiply(5, 2)  // 10
multiply(5)     // NaN !

function multiply(a, b) {
  b = (typeof b !== 'undefined') ?  b : 1
  return a * b
}
multiply(5, 2)  // 10
multiply(5)     // 5

function multiply(a, b = 1) {
  return a * b
}
multiply(5, 2)          // 10
multiply(5)             // 5
multiply(5, undefined)  // 5

function test(num = 1) {
  console.log(typeof num)
}
test()           // 'number' (num is set to 1)
test(undefined)  // 'number' (num is set to 1 too)

// test with other falsy values:
test('')         // 'string' (num is set to '')
test(null)       // 'object' (num is set to null)

function append(value, array = []) {
  array.push(value)
  return array
}
append(1)  // [1]
append(2)  // [2], not [1, 2]

function callSomething(thing = something()) {
  return thing
}
let numberOfTimesCalled = 0
function something() {
  numberOfTimesCalled += 1
  return numberOfTimesCalled
}
callSomething()  // 1
callSomething()  // 2


function greet(name, greeting, message = greeting + ' ' + name) {
  return [name, greeting, message]
}
greet('David', 'Hi')                     // ["David", "Hi", "Hi David"]
greet('David', 'Hi', 'Happy Birthday!')  // ["David", "Hi", "Happy Birthday!"]

function go() {
  return ':P'
}
function withDefaults(a, b = 5, c = b, d = go(), e = this,
                      f = arguments, g = this.value) {
  return [a, b, c, d, e, f, g]
}
function withoutDefaults(a, b, c, d, e, f, g) {
  switch (arguments.length) {
    case 0:
      
    case 1:
      b = 5;
    case 2:
      c = b;
    case 3:
      d = go();
    case 4:
      e = this;
    case 5:
      f = arguments;
    case 6:
      g = this.value;
    default:
  }
  return [a, b, c, d, e, f, g];
}
withDefaults.call({value: '=^_^='});
withoutDefaults.call({value: '=^_^='});

function f(x = 1, y) {
  return [x, y]
}
f()   // [1, undefined]
f(2)  // [2, undefined]

function preFilledArray([x = 1, y = 2] = []) {
  return x + y;
}
preFilledArray();       // 3
preFilledArray([]);     // 3
preFilledArray([2]);    // 4
preFilledArray([2, 3]); // 5

// Works the same for objects:
function preFilledObject({z = 3} = {}) {
  return z;
}
preFilledObject();          // 3
preFilledObject({});        // 3
preFilledObject({ z: 2 });  // 2

function config(options) {
  options.duration ??= 100;
  options.speed ??= 25;
  return options;
}
config({ duration: 125 }); // { duration: 125, speed: 25 }
config({}); // { duration: 100, speed: 25 }

map(cube, [0, 1, 2, 5, 10]);

résultat = externe()(20);







