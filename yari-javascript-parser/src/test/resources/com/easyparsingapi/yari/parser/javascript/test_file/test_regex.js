new RegExp("ab+c", "i"); // constructeur
new RegExp(/ab+c/, "i"); // notation littérale dans un constructeur

var re = /\w+/;
var re = new RegExp("\\w+");

let re = /(\w+)\s(\w+)/;    

let lignes = texte.split(/\r\n|\r|\n/);

s.match(/voici.*ligne/);
s.match(/voici[^]*ligne/);

let regex = /toto/y;

let regex = /toto/yug;

re = /\d/y;

let regex = /[\u0400-\u04FF]+/g;

console.log(/[^.]+/.exec(url)[0].substr(7)); 