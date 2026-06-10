const MY_FAV = 7;
MY_FAV = 20;
var MY_FAV = 20;
let MY_FAV = 20;

if (MY_FAV === 7) {
  let MY_FAV = 20;
  var MY_FAV = 20;
}
const MY_OBJECT = {'key': 'value'};
MY_OBJECT = {'OTHER_KEY': 'value'};
MY_OBJECT.key = 'otherValue'; // Use Object.freeze() to make object immutable
const MY_ARRAY = [];
MY_ARRAY.push('A'); // ["A"]
MY_ARRAY = ['B'];

let i = 0
let a = {
  ['foo' + ++i]: i,
  ['foo' + ++i]: i,
  ['foo' + ++i]: i
}

const items = ["A","B","C"];
const obj = {
[items]: "Hello"
}

let param = 'size'
let config = {
  [param]: 12,
  ['mobile' + param.charAt(0).toUpperCase() + param.slice(1)]: 4
}

const object = {};
object.$1 = 'foo';



