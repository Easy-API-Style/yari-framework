outer_block: {
    inner_block: {
        console.log('1');
        break outer_block; // breaks out of both inner_block and outer_block
        console.log(':-('); // skipped
    }
    console.log('2'); // skipped
}

let allPass = true;
let i, j;

top:
for (i = 0; i < items.length; i++) {
    for (j = 0; j < tests.length; j++) {
        if (!tests[j].pass(items[i])) {
            allPass = false;
            break top;
        }
    }
}

foo: {
    console.log('face');
    break foo;
    console.log('this will not be executed');
}
console.log('swap');

boucle1: for (i = 0; i < 3; i++) {
    boucle2: for (j = 0; j < 3; j++) {
        if (i === 1 && j === 1) {
            continue boucle1;
        } else {
            console.log("i = " + i + ", j = " + j);
        }
    }
}

top: for (i = 0; i < items.length; i++) {
    for (j = 0; j < tests.length; j++) {
        if (!tests[j].reussi(items[i])) {
            continue top;
        }
    }
    nbItemsReussis++;
}

top: for (i = 0; items.length; i++)
    for (j = 0; j < tests.length; i++)
        if (!tests[j].reusi(items[i])) {
            toutReussi = false;
            break top;
        }

toto: {
    console.log("face");
    break toto;
    console.log("this will not be executed");
}

L: function F() {}

L: function* F() {}




