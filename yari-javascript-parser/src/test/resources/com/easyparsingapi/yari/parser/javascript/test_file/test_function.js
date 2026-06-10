function resolveAfter2Seconds() {
  return new Promise(resolve => {
    setTimeout(() => {
      resolve('resolved');
    }, 2000);
  });
}

async function asyncCall() {
  console.log('calling');
  const result = await resolveAfter2Seconds();
  console.log(result);
  // expected output: "resolved"
}

asyncCall();

function resolveAfter2Seconds() {
  console.log("starting slow promise")
  return new Promise(resolve => {
    setTimeout(function() {
      resolve("slow")
      console.log("slow promise is done")
    }, 2000)
  })
}

function resolveAfter1Second() {
  console.log("starting fast promise")
  return new Promise(resolve => {
    setTimeout(function() {
      resolve("fast")
      console.log("fast promise is done")
    }, 1000)
  })
}

async function sequentialStart() {
  console.log('==SEQUENTIAL START==')

  // 1. Execution gets here almost instantly
  const slow = await resolveAfter2Seconds()
  console.log(slow) // 2. this runs 2 seconds after 1.

  const fast = await resolveAfter1Second()
  console.log(fast) // 3. this runs 3 seconds after 1.
}

async function concurrentStart() {
  console.log('==CONCURRENT START with await==');
  const slow = resolveAfter2Seconds() // starts timer immediately
  const fast = resolveAfter1Second() // starts timer immediately

  // 1. Execution gets here almost instantly
  console.log(await slow) // 2. this runs 2 seconds after 1.
  console.log(await fast) // 3. this runs 2 seconds after 1., immediately after 2., since fast is already resolved
}

function concurrentPromise() {
  console.log('==CONCURRENT START with Promise.all==')
  return Promise.all([resolveAfter2Seconds(), resolveAfter1Second()]).then((messages) => {
    console.log(messages[0]) // slow
    console.log(messages[1]) // fast
  })
}

async function parallel() {
  console.log('==PARALLEL with await Promise.all==')

  // Start 2 "jobs" in parallel and wait for both of them to complete
  await Promise.all([
      (async()=>console.log(await resolveAfter2Seconds()))(),
      (async()=>console.log(await resolveAfter1Second()))()
  ])
}

sequentialStart() // after 2 seconds, logs "slow", then after 1 more second, "fast"

// wait above to finish
setTimeout(concurrentStart, 4000) // after 2 seconds, logs "slow" and then "fast"

// wait again
setTimeout(concurrentPromise, 7000) // same as concurrentStart

// wait again
setTimeout(parallel, 10000) // truly parallel: after 1 second, logs "fast", then after 1 more second, "slow"

function getProcessedData(url) {
  return downloadData(url) // returns a promise
    .catch(e => {
      return downloadFallbackData(url)  // returns a promise
    })
    .then(v => {
      return processDataInWorker(v)  // returns a promise
    })
}

async function getProcessedData(url) {
  let v
  try {
    v = await downloadData(url)
  } catch(e) {
    v = await downloadFallbackData(url)
  }
  return processDataInWorker(v)
}

async function getProcessedData(url) {
  const v = await downloadData(url).catch(e => {
    return downloadFallbackData(url)
  })
  return processDataInWorker(v)
}

var hoisted = "foo" in this;
console.log(`'foo' name ${hoisted ? "is" : "is not"} hoisted. typeof foo is ${typeof foo}`);
if (false) {
  function foo(){ return 1; }
}

function calc_sales(units_a, units_b, units_c) {
   return units_a * 79 + units_b * 129 + units_c * 699;
}

function testBreak(x) {
  var i = 0;

  while (i < 6) {
    if (i == 3) {
      break;
    }
    i += 1;
  }

  return i * x;
}

function resolveAfter2Seconds(x) {
  return new Promise(resolve => {
    setTimeout(() => {
      resolve(x);
    }, 2000);
  });
};

const add = async function(x) { // async function expression assigned to a variable
  let a = await resolveAfter2Seconds(20);
  let b = await resolveAfter2Seconds(30);
  return x + a + b;
};

add(10).then(v => {
  console.log(v);  // prints 60 after 4 seconds.
});

(async function(x) { // async function expression used as an IIFE
  let p_a = resolveAfter2Seconds(20);
  let p_b = resolveAfter2Seconds(30);
  return x + await p_a + await p_b;
})(10).then(v => {
  console.log(v);  // prints 60 after 2 seconds.
});

let math = {
  'factit': function factorial(n) {
    console.log(n)
    if (n <= 1) {
      return 1;
    }
    return n * factorial(n - 1);
  }
};
math.factit(3) //3;2;1;

button.addEventListener('click', function(event) {
    console.log('button is clicked!')
})

var créerAnimal = function (nom) {
  var sexe;

  return {
    setNom: function (nouveauNom) {
      nom = nouveauNom;
    },

    getNom: function () {
      return nom;
    },

    getSexe: function () {
      return sexe;
    },

    setSexe: function (nouveauSexe) {
      if (
        typeof nouveauSexe == "string" &&
        (nouveauSexe.toLowerCase() == "mâle" ||
          nouveauSexe.toLowerCase() == "femelle")
      ) {
        sexe = nouveauSexe;
      }
    },
  };
};

var getCode = (function () {
  var codeAPI = "0]Eal(eh&2"; // Un code qu'on ne souhaite pas diffuser ni modifier

  return function () {
    return codeAPI;
  };
})();

function multiplier(a, b) {
  b = typeof b !== "undefined" ? b : 1;

  return a * b;
}

function multiplier(facteur, ...lesArgs) {
  return lesArgs.map((x) => facteur * x);
}

function Personne() {
  this.âge = 0;

  setInterval(function grandir() {
    this.âge++;
  }, 1000);
}

function Personne() {
  this.âge = 0;

  setInterval(() => {
    this.âge++; // this fait référence à l'objet personne
  }, 1000);
}

function multiplier(a, b = 1) {
  return a * b;
}

function monConcat(séparateur) {
  var result = ""; // on initialise la liste
  var i;
  // on parcourt les arguments
  for (i = 1; i < arguments.length; i++) {
    result += arguments[i] + séparateur;
  }
  return result;
}

var getCode = (function () {
  var codeAPI = "0]Eal(eh&2"; // Un code qu'on ne souhaite pas diffuser ni modifier

  return function () {
    return codeAPI;
  };
})();

var animal = function (nom) {
  var getNom = function () {
    return nom; 
  };
  return getNom; 
};

function A(x) {
  function B(y) {
    function C(z) {
      console.log(x + y + z);
    }
    C(3);
  }
  B(2);
}

function ajouteCarrés(a, b) {
  function carré(x) {
    return x * x;
  }
  return carré(a) + carré(b);
}

function toto(i) {
  if (i < 0) return;
  console.log("début : " + i);
  toto(i - 1);
  console.log("fin : " + i);
}