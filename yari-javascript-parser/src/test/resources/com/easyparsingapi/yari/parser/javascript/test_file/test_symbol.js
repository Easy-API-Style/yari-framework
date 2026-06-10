const LIMIT = 3;

const asyncIterable = {
  [Symbol.asyncIterator]() {
    let i = 0;
    return {
      next() {
        const done = i === LIMIT;
        const value = done ? undefined : i++;
        return Promise.resolve({ value, done });
      },
      return() {
        return { done: true };
      }
    };
  }
};

(async () => {
  for await (const num of asyncIterable) {
    console.log(num);
  }
})();

const myAsyncIterable = new Object();
myAsyncIterable[Symbol.asyncIterator] = async function*() {
    yield "coucou";
    yield "l'itération";
    yield "asynchrone !";
};

(async () => {
    for await (const x of myAsyncIterable) {
        console.log(x);
    }
})();

