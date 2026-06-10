let customer = {
  name: "Carl",
  details: { age: 82 }
};
const customerCity = customer?.city ?? "Unknown city";
console.log(customerCity); // Unknown city

let customer = {
  name: "Carl",
  details: {
    age: 82,
    location: "Paradise Falls" // detailed address is unknown
  }
};
let customerCity = customer.details?.address?.city;
let customerName = customer.name?.getName?.(); // method does not exist, customerName is undefined

let potentiallyNullObj = null;
let x = 0;
let prop = potentiallyNullObj?.[x++];
console.log(x); // 0 as x was not incremented

let myMap = new Map();
myMap.set("foo", {name: "baz", desc: "inga"});
let nameBar = myMap.get("bar")?.name;

new URL(import.meta.url).searchParams.get('someURLInfo'); // 5

