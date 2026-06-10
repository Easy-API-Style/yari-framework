var age = 26;
var beverage = (age >= 21) ? "Beer" : "Juice";

let greeting = person => {
    let name = person ? person.name : `stranger`
    return `Howdy, ${name}`
}

console.log(greeting({ name: `Alice` }));  // "Howdy, Alice"
console.log(greeting(null));             // "Howdy, stranger"

let trees = ['redwood', 'bay', 'cedar', 'oak', 'maple']
0 in trees        // returns true
3 in trees        // returns true
6 in trees        // returns false
'bay' in trees    // returns false (you must specify the index number, not the value at that index)
'length' in trees // returns true (length is an Array property)
Symbol.iterator in trees // returns true (arrays are iterable, works only in ES2015+)
'PI' in Math          // returns true
let mycar = { make: 'Honda', model: 'Accord', year: 1998 }
'make' in mycar  // returns true
'model' in mycar // returns true
'toString' in {}  // returns true

"1" != 1;            // false
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

"Le prix est : " + (estMembre ? "15 €" : "30 €");

var elvisLives = Math.PI > 4 ? "Yep" : "Nope";

var premierControle = false,
    secondControle = false,
    acces = premierControle
        ? "Accès refusé"
        : secondControle
            ? "Accès refusé"
            : "Accès autorisé";

var stop = false,
    age = 16;

age > 18 ? location.assign("continue.html") : (stop = true);

var stop = false,
    age = 23;

age > 18
    ? (console.log("OK, accès autorisé."), location.assign("continue.html"))
    : ((stop = true), console.log("Accès refusé !"));

var url =
    age > 18
        ? (console.log("Accès autorisé."),
            // console.log renvoie "undefined", mais cela importe peu car
            // ce n'est pas le dernier élément de l'expression
            "continue.html") // la valeur à affecter si âge > 18
        : (console.log("Accès refusé !"),
            // etc.
            "stop.html"); // la valeur à affecter si âge <= 18


var func1 = function() {
    if (condition1) { return valeur1 }
    else if (condition2) { return valeur2 }
    else if (condition3) { return valeur3 }
    else { return value4 }
}

var func2 = function() {
    return condition1 ? valeur1
        : condition2 ? valeur2
            : condition3 ? valeur3
                : valeur4
}

