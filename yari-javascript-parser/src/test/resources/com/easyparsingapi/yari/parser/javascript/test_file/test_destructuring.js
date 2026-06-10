let a, b, rest;
[a, b] = [10, 20];

[a, b, ...rest] = [10, 20, 30, 40, 50];

({ a, b } = { a: 10, b: 20 });

({a, b, ...rest} = {a: 10, b: 20, c: 30, d: 40});

const x = [1, 2, 3, 4, 5];

const x = [1, 2, 3, 4, 5];
const [y, z] = x;

const [firstElement, secondElement] = list;

const foo = ['one', 'two', 'three'];

const [red, yellow, green] = foo;

let a, b;

[a, b] = [1, 2];

const foo = ['one', 'two'];

const [red, yellow, green, blue] = foo;

let a, b;

[a=5, b=7] = [1];

let a = 1;
let b = 3;

[a, b] = [b, a];

const arr = [1,2,3];
[arr[2], arr[1]] = [arr[1], arr[2]];

function f() {
  return [1, 2];
}

let a, b;
[a, b] = f();

function f() {
  return [1, 2, 3];
}

const [a, , b] = f();

const [c] = f();

[,,] = f();

const [a, ...b] = [1, 2, 3];

function parseProtocol(url) {
  const parsedURL = /^(\w+)\:\/\/([^\/]+)\/(.*)$/.exec(url);
  if (!parsedURL) {
    return false;
  }

  const [, protocol, fullhost, fullpath] = parsedURL;
  return protocol;
}

const user = {
    id: 42,
    isVerified: true
};

const {id, isVerified} = user;

console.log(id); // 42
console.log(isVerified); // true

let a, b;

({a, b} = {a: 1, b: 2});

const o = {p: 42, q: true};
const {p: foo, q: bar} = o;

const {a = 10, b = 5} = {a: 3};

const {a: aa = 10, b: bb = 5} = {a: 3};

const user = {
  id: 42,
  displayName: 'jdoe',
  fullName: {
    firstName: 'John',
    lastName: 'Doe'
  }
};

function userId({id}) {
  return id;
}

function userDisplayName({displayName: dname}) {
  return dname;
}

function whois({displayName, fullName: {firstName: name}}) {
  return `${displayName} is ${name}`;
}

console.log(whois(user));  // "jdoe is John"

function drawChart({size = 'big', coords = {x: 0, y: 0}, radius = 25} = {}) {
  console.log(size, coords, radius);
}

drawChart({
  coords: {x: 18, y: 30},
  radius: 30
});

const metadata = {
  title: 'Scratchpad',
  translations: [
    {
      locale: 'de',
      localization_tags: [],
      last_edit: '2014-04-14T08:43:37',
      url: '/de/docs/Tools/Scratchpad',
      title: 'JavaScript-Umgebung'
    }
  ],
  url: '/en-US/docs/Tools/Scratchpad'
};

let {
  title: englishTitle, // rename
  translations: [
    {
       title: localeTitle, // rename
    },
  ],
} = metadata;

const people = [
  {
    name: 'Mike Smith',
    family: {
      mother: 'Jane Smith',
      father: 'Harry Smith',
      sister: 'Samantha Smith'
    },
    age: 35
  },
  {
    name: 'Tom Jones',
    family: {
      mother: 'Norah Jones',
      father: 'Richard Jones',
      brother: 'Howard Jones'
    },
    age: 25
  }
];

for (const {name: n, family: {father: f}} of people) {
  console.log('Name: ' + n + ', Father: ' + f);
}

let key = 'z';
let {[key]: foo} = {z: 'bar'};

let {a, b, ...rest} = {a: 10, b: 20, c: 30, d: 40}

const foo = { 'fizz-buzz': true };
const { 'fizz-buzz': fizzBuzz } = foo;

const props = [
  { id: 1, name: 'Fizz'},
  { id: 2, name: 'Buzz'},
  { id: 3, name: 'FizzBuzz'}
];

const [,, { name }] = props;

let obj = {self: '123'};
obj.__proto__.prot = '456';
const {self, prot} = obj;


const obj = {
  self: "123",
  __proto__: {
    prot: "456",
  },
};
const { self, prot } = obj;

const props = [
  { id: 1, name: "Fizz" },
  { id: 2, name: "Buzz" },
  { id: 3, name: "FizzBuzz" },
];

const [, , { name }] = props;

const { a, toFixed } = 1;

const foo = { "fizz-buzz": true };
const { "fizz-buzz": fizzBuzz } = foo;

const key = "z";
const { [key]: foo } = { z: "bar" };

const people = [
  {
    name: "Mike Smith",
    family: {
      mother: "Jane Smith",
      father: "Harry Smith",
      sister: "Samantha Smith",
    },
    age: 35,
  },
  {
    name: "Tom Jones",
    family: {
      mother: "Norah Jones",
      father: "Richard Jones",
      brother: "Howard Jones",
    },
    age: 25,
  },
];

for (const {
  name: n,
  family: { father: f },
} of people) {
  console.log(`Name: ${n}, Father: ${f}`);
}


const metadata = {
  title: "Scratchpad",
  translations: [
    {
      locale: "de",
      localizationTags: [],
      lastEdit: "2014-04-14T08:43:37",
      url: "/de/docs/Tools/Scratchpad",
      title: "JavaScript-Umgebung",
    },
  ],
  url: "/en-US/docs/Tools/Scratchpad",
};

const {
  title: englishTitle, // rename
  translations: [
    {
      title: localeTitle, // rename
    },
  ],
} = metadata;


function drawChart({
  size = "big",
  coords = { x: 0, y: 0 },
  radius = 25,
} = {}) {
  console.log(size, coords, radius);
}

drawChart({
  coords: { x: 18, y: 30 },
  radius: 30,
});


function whois({ displayName, fullName: { firstName: name } }) {
  return `${displayName} is ${name}`;
}

function userDisplayName({ displayName: dname }) {
  return dname;
}

function userId({ id }) {
  return id;
}

const user = {
  id: 42,
  displayName: "jdoe",
  fullName: {
    firstName: "Jane",
    lastName: "Doe",
  },
};

const { a: aa = 10, b: bb = 5 } = { a: 3 };

const o = { p: 42, q: true };
const { p: foo, q: bar } = o;

const user = {
  id: 42,
  isVerified: true,
};

const { id, isVerified } = user;

const obj = {
  *[Symbol.iterator]() {
    for (const v of [0, 1, 2, 3]) {
      console.log(v);
      yield v;
    }
  },
};
const [a, b, ...rest] = obj; 

const obj = {
  *[Symbol.iterator]() {
    for (const v of [0, 1, 2, 3]) {
      console.log(v);
      yield v;
    }
  },
};
const [a, b] = obj; 

const [a, b] = new Map([
  [1, 2],
  [3, 4],
]);

function parseProtocol(url) {
  const parsedURL = /^(\w+):\/\/([^\/]+)\/(.*)$/.exec(url);
  if (!parsedURL) {
    return false;
  }
  console.log(parsedURL);

  const [, protocol, fullHost, fullPath] = parsedURL;
  return protocol;
}

const [a, b, ...[c, d, ...[e, f]]] = [1, 2, 3, 4, 5, 6];
console.log(a, b, c, d, e, f);

const [a, b, ...[c, d]] = [1, 2, 3, 4];
console.log(a, b, c, d);

const [a, b, ...{ length }] = [1, 2, 3];
console.log(a, b, length);

function f() {
  return [1, 2, 3];
}

const [a, , b] = f();
console.log(a); // 1
console.log(b); // 3

const [c] = f();

function f() {
  return [1, 2];
}

const [a, b] = f();

let a = 1;
let b = 3;

[a, b] = [b, a];

const arr = [1, 2, 3];
[arr[2], arr[1]] = [arr[1], arr[2]];
const foo = ["one", "two"];

const [red, yellow, green, blue] = foo;

const foo = ["one", "two", "three"];

const [red, yellow, green] = foo;

const { a, ...others } = { a: 1, b: 2, c: 3 };

const [first, ...others2] = [1, 2, 3];

const { b = console.log("hey") } = { b: 2 };

const [a = 1] = []; // a is 1
const { b = 2 } = { b: undefined }; // b is 2
const { c = 2 } = { c: null }; // c is null

const numbers = [];
const obj = { a: 1, b: 2 };
({ a: numbers[0], b: numbers[1] } = obj);

const obj = { a: 1, b: { c: 2 } };
const { a } = obj; // a is constant
let {
  b: { c: d },
} = obj; // d is re-assignable

const obj = { a: 1, b: { c: 2 } };
const {
  a,
  b: { c: d },
} = obj;

const obj = { prop1: x, prop2: y, prop3: z };
const { prop1: x, prop2: y, prop3: z } = obj;

const obj = { a, b, c };
const { a, b, c } = obj;

const arr = [1, 2, 3];
const [a, b, c] = arr;

const arr = [a, b, c];
const [a, b] = array;
const [a, , b] = array;
const [a = aDefault, b] = array;
const [a, b, ...rest] = array;
const [a, , b, ...rest] = array;
const [a, b, ...{ pop, push }] = array;
const [a, b, ...[c, d]] = array;

const { a, b } = obj;
const { a: a1, b: b1 } = obj;
const { a: a1 = aDefault, b = bDefault } = obj;
const { a, b, ...rest } = obj;
const { a: a1, b: b1, ...rest } = obj;
const { [key]: a } = obj;

let a, b, a1, b1, c, d, rest, pop, push;

[a, b] = array;
[a, , b] = array;
[a = aDefault, b] = array;
[a, b, ...rest] = array;
[a, , b, ...rest] = array;
[a, b, ...{ pop, push }] = array;
[a, b, ...[c, d]] = array;

({ a, b } = obj); // parentheses are required
({ a: a1, b: b1 } = obj);
({ a: a1 = aDefault, b = bDefault } = obj);
({ a, b, ...rest } = obj);
({ a: a1, b: b1, ...rest } = obj);

let a, b, rest;
[a, b] = [10, 20];

const [a, b] = array;
const [a, , b] = array;
const [a = aDefault, b] = array;
const [a, b, ...rest] = array;
const [a, , b, ...rest] = array;
const [a, b, ...{ pop, push }] = array;
const [a, b, ...[c, d]] = array;

const { a, b } = obj;
const { a: a1, b: b1 } = obj;
const { a: a1 = aDefault, b = bDefault } = obj;
const { a, b, ...rest } = obj;
const { a: a1, b: b1, ...rest } = obj;
const { [key]: a } = obj;

let a, b, a1, b1, c, d, rest, pop, push;
[a, b] = array;
[a, , b] = array;
[a = aDefault, b] = array;
[a, b, ...rest] = array;
[a, , b, ...rest] = array;
[a, b, ...{ pop, push }] = array;
[a, b, ...[c, d]] = array;

({ a, b } = obj); // brackets are required
({ a: a1, b: b1 } = obj);
({ a: a1 = aDefault, b = bDefault } = obj);
({ a, b, ...rest } = obj);
({ a: a1, b: b1, ...rest } = obj);

const obj = { a: 1, b: { c: 2 } };
const { a, b: { c: d } } = obj;

function userDisplayName({ displayName: dname }) {
  return dname;
}

function whois({ displayName, fullName: { firstName: name } }) {
  return `${displayName} is ${name}`;
}

function drawChart({ size = 'big', coords = { x: 0, y: 0 }, radius = 25 } = {}) {
  console.log(size, coords, radius);
}

drawChart({
  coords: { x: 18, y: 30 },
  radius: 30,
});


