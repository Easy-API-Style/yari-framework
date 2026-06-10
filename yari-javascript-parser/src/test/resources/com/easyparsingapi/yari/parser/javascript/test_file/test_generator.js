class Foo {
  *generator () {
    yield 1;
    yield 2;
    yield 3;
  }
}

const someObj = {
  *generator () {
    yield 'a';
    yield 'b';
  }
}

function* yieldAndReturn() {
  yield "Y";
  return "R";
  yield "unreachable";
}

function* logGenerator() {
  console.log(0);
  console.log(1, yield);
  console.log(2, yield);
  console.log(3, yield);
}

function* anotherGenerator(i) {
  yield i + 1;
  yield i + 2;
  yield i + 3;
}

function* generator(i) {
  yield i;
  yield* anotherGenerator(i);
  yield i + 10;
}

function* idMaker() {
  var index = 0;
  while (true)
    yield index++;
}

function* powers(n){
     for(let current =n;; current *= n){
         yield current;
     }
}

for(let power of powers(2)){
     if(power > 32) break;
     console.log(power)
}
const foo = function* () {
  yield 10;
  yield 20;
};

const obj2 = {
  g: function* () {
    let index = 0
    while (true) {
      yield index++
    }
  }
};

const obj2 = {
  * g() {
    let index = 0
    while (true) {
      yield index++
    }
  }
};

const obj3 = {
  f: async function () {
    await some_promise
  }
}

const obj3 = {
  async f() {
    await some_promise
  }
}
const obj4 = {
  f: async function* () {
    yield 1
    yield 2
    yield 3
  }
};

const obj4 = {
  async* f() {
   yield 1
   yield 2
   yield 3
  }
}
const obj = {
  a: 'foo',
  b() { return this.a }
};

const bar = {
  foo0: function() { return 0 },
  foo1() { return 1 },
  ['foo' + 2]() { return 2 }
}

function foo() {
  return 1
}
let name = 'foo'

const monObj = {
  *generator() {
    yield "a";
    yield "b";
  },
};

function* autreGenerateur(i) {
  yield i + 1;
  yield i + 2;
  yield i + 3;
}
function* generateur(i) {
  yield i;
  yield* autreGenerateur(i);
  yield i + 10;
}

function* creerID() {
  var index = 0;
  while (true) {
    yield index++;
  }
}

class Toto {
  *[Symbol.iterator]() {
    yield 1;
    yield 2;
  }
}

const monObj = {
  *[Symbol.iterator]() {
    yield "a";
    yield "b";
  },
};


