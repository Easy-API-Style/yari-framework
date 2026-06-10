console.log(2 + 2);

console.log(2 + true);

console.log('hello ' + 'everyone');

console.log(2001 + ': A Space Odyssey');

1 + 2; // 3

true + 1; // 2

false + false; // 0

'foo' + 'bar'; // "foobar"

5 + 'foo'; // "5foo"

'foo' + false; // "foofalse"

foo = 'foo'
bar = 5
baz = true

bar += 2; // 7

baz += 1; // 2

baz += false; // 1

bar += 'foo'; // "5foo"

foo += false; // "foofalse"

foo += 'bar'; // "foobar"

x = 5

x = y;     // x is 10
x = y = z; // x, y and z are all 25

5 & 2; 
a &= 2;
~0;  // -1
~-1; // 0
~1;  // -2
14 | 9;
a |= 2; // 7
14 ^ 9;

let a = 5;      // 00000000000000000000000000000101
a ^= 3;         // 00000000000000000000000000000011
console.log(a); // 00000000000000000000000000000110
// 6
let b = 5;      // 00000000000000000000000000000101
b ^= 0;         // 00000000000000000000000000000000
console.log(b); // 00000000000000000000000000000101
// 5

typeof Foo;             // returns "function"
typeof class {};        // returns "function"
Foo instanceof Object;   // true
Foo instanceof Function; // true

let x = 3;
const y = x--;

console.log(`x:${x}, y:${y}`);

let a = 3;
const b = --a;

console.log(`a:${a}, b:${b}`);

const Employee = {
  firstname: 'John',
  lastname: 'Doe'
};

console.log(Employee.firstname);
delete Employee.firstname;
console.log(Employee.firstname);

var Employee = {
  age: 28,
  name: 'abc',
  designation: 'developer'
}
console.log(delete Employee.name);   // returns true
console.log(delete Employee.age);    // returns true
console.log(delete Employee.salary); // returns true

var Employee = {};
Object.defineProperty(Employee, 'name', {configurable: false});
console.log(delete Employee.name);  // returns false

v = 2.0 / 0;     // Infinity
v = 2.0 / 0.0;   // Infinity, because 0.0 === 0
v = 2.0 / -0.0;  // -Infinity

bar /= 2     // 2.5
bar /= 2;     // 1.25
bar /= 0;     // Infinity
bar /= 'foo'; // NaN
"1" ==  1;            // true
1 == "1";             // true
0 == false;           // true
0 == null;            // false
0 == undefined;       // false
0 == !!null;          // true, look at Logical NOT operator
0 == !!undefined;     // true, look at Logical NOT operator
null == undefined;    // true
const number1 = new Number(3);
const number2 = new Number(3);
number1 == 3;         // true
number1 == number2;   // false

const object1 = {"key": "value"};
const object2 = {"key": "value"};
object1 == object2; // false
object2 == object2; // true

const string1 = "hello";
const string2 = String("hello");
const string3 = new String("hello");
const string4 = new String("hello");
console.log(string1 == string2); // true
console.log(string1 == string3); // true
console.log(string2 == string3); // true
console.log(string3 == string4); // false
console.log(string4 == string4); // true

const d = new Date('December 17, 1995 03:24:00');
const s = d.toString(); // for example: "Sun Dec 17 1995 03:24:00 GMT-0800 (Pacific Standard Time)"
console.log(d == s);    //true

2 ** 3;   // 8
3 ** 2;   // 9
3 ** 2.5; // 15.588457268119896
10 ** -1; // 0.1
NaN ** 2; // NaN
(-2) ** 2; // 4
2 ** 3 ** 2;   // 512
2 ** (3 ** 2); // 512
(2 ** 3) ** 2; // 64
-(2 ** 2); // -4

bar **= 2     // 25
bar **= 'foo' // NaN

console.log("5" > 3);          // true
console.log("3" > 3);          // false
console.log("3" > 5);          // false
console.log("hello" > 5);      // false
console.log(5 > "hello");      // false
console.log("5" > 3n);         // true
console.log("3" > 5n);         // false
console.log(true > false);     // true
console.log(false > true);     // false
console.log(true > 0);         // true
console.log(true > 1);         // false
console.log(null > 0);         // false
console.log(1 > null);         // true
console.log(undefined > 3);    // false
console.log(3 > undefined);    // false
console.log(3 > NaN);          // false
console.log(NaN > 3);          // false

console.log("5" >= 3);       // true
console.log("3" >= 3);       // true
console.log("3" >= 5);       // false
console.log("hello" >= 5);   // false
console.log(5 >= "hello");   // false

var a = 1;
var b = 2;
var c = 3;
a + b * c;     // 7
a + (b * c);   // 7
(a + b) * c;   // 9
a * c + b * c // 9;
a() * (b() + c());

// Arrays
let trees = ['redwood', 'bay', 'cedar', 'oak', 'maple']
0 in trees;        // returns true
3 in trees;        // returns true
6 in trees;        // returns false
'bay' in trees;    // returns false (you must specify the index number, not the value at that index)
'length' in trees; // returns true (length is an Array property)
Symbol.iterator in trees; // returns true (arrays are iterable, works only in ES2015+)
// Predefined objects
'PI' in Math;          // returns true
// Custom objects
let mycar = {make: 'Honda', model: 'Accord', year: 1998};
'make' in mycar;  // returns true
'model' in mycar; // returns true

"1" !=  1;            // false
1 != "1";             // false
0 != false;           // false
0 != null;            // true
0 != undefined;       // true
0 != !!null;          // false, look at Logical NOT operator
0 != !!undefined;     // false, look at Logical NOT operator
null != undefined;    // false
const number1 = new Number(3);
const number2 = new Number(3);
number1 != 3;         // false
number1 != number2;   // true

function Shape() {
}
function Rectangle() {
  Shape.call(this); // call super constructor.
}
Rectangle.prototype = Object.create(Shape.prototype);
Rectangle.prototype.constructor = Rectangle;
let rect = new Rectangle();
rect instanceof Object;    // true
rect instanceof Shape;     // true
rect instanceof Rectangle; // true
rect instanceof String;    // false
let literalObject     = {};
let nullObject  = Object.create(null);
nullObject.name = "My object";
literalObject    instanceof Object;   // true, every object literal has Object.prototype as prototype
({})             instanceof Object;   // true, same case as above
nullObject       instanceof Object;   // false, prototype is end of prototype chain (null)

9 << 3; // 72
a <<= 2; // 20

console.log("5" < 3);          // false
console.log("3" < 3);          // false
console.log("3" < 5);          // true
console.log("hello" < 5);      // false
console.log(5 < "hello");      // false
console.log("5" < 3n);         // false
console.log("3" < 5n);         // true

a1 = true  && true;       // t && t returns true
a2 = true  && false;      // t && f returns false
a3 = false && true;       // f && t returns false
a4 = false && (3 == 4);   // f && f returns false
a5 = 'Cat' && 'Dog';      // t && t returns "Dog"
a6 = false && 'Cat';      // f && t returns false
a7 = 'Cat' && false;     // t && f returns false
a8 = ''    && false;     // f && f returns ""
a9 = false && '';         // f && f returns false

let x = 0;
let y = 1;
x &&= 0; // 0
x &&= 1; // 0
y &&= 1; // 1
y &&= 0; // 0

function config(options) {
  options.duration ??= 100;
  options.speed ??= 25;
  return options;
}
config({ duration: 125 }); // { duration: 125, speed: 25 }
config({}); // { duration: 100, speed: 25 }

const nullValue = null;
const emptyText = ""; // falsy
const someNumber = 42;
const valA = nullValue ?? "default for A";
const valB = emptyText ?? "default for B";
const valC = someNumber ?? 0;
console.log(valA); // "default for A"
console.log(valB); // "" (as the empty string is not null or undefined)
console.log(valC); // 42

 13 % 5;  //  3
 1 % -2; //  1
 1 % 2;  //  1
 2 % 3;  //  2
5.5 % 2; // 1.5
x %= y; // x = x % y

 9 >> 2; //  2
-9 >> 2; // -3

let a = 5; //   (00000000000000000000000000000101)
a >>= 2;   // 1 (00000000000000000000000000000001)
b >>= 2;  // -2 (-00000000000000000000000000000010)

 9 >>> 2; // 2
-9 >>> 2; // 1073741821

console.log("hello" !== "hello");   // false
console.log("hello" !== "hola");    // true
console.log(3 !== 3);               // false
console.log(3 !== 4);               // true
console.log(true !== true);         // false
console.log(true !== false);        // true
console.log(null !== null);         // false

let a = 5; //   (00000000000000000000000000000101)
a >>>= 2;  // 1 (00000000000000000000000000000001)
let b = -5; // (-00000000000000000000000000000101)
b >>>= 2;   // 1073741822 (00111111111111111111111111111110)


