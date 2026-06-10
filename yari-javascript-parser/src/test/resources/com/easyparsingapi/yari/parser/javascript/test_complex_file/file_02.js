
            (function () {
                try {
                    (function setConstant(source, args) {
    var flag = 'done';
    var uniqueIdentifier =
      source.uniqueId +
      source.name +
      '_' +
      (Array.isArray(args) ? args.join('_') : '');
    if (source.uniqueId) {
      if (Window.prototype.toString[uniqueIdentifier] === flag) {
        return;
      }
    }
    function setConstant(source, property, value) {
      var stack =
        arguments.length > 3 && arguments[3] !== undefined ? arguments[3] : '';
      var valueWrapper =
        arguments.length > 4 && arguments[4] !== undefined ? arguments[4] : '';
      var setProxyTrap =
        arguments.length > 5 && arguments[5] !== undefined
          ? arguments[5]
          : false;
      var uboAliases = [
        'set-constant.js',
        'ubo-set-constant.js',
        'set.js',
        'ubo-set.js',
        'ubo-set-constant',
        'ubo-set',
      ];
      if (uboAliases.includes(source.name)) {
        if (stack.length !== 1 && !getNumberFromString(stack)) {
          valueWrapper = stack;
        }
        stack = undefined;
      }
      if (!property || !matchStackTrace(stack, new Error().stack)) {
        return;
      }
      var isProxyTrapSet = false;
      var emptyArr = noopArray();
      var emptyObj = noopObject();
      var constantValue;
      if (value === 'undefined') {
        constantValue = undefined;
      } else if (value === 'false') {
        constantValue = false;
      } else if (value === 'true') {
        constantValue = true;
      } else if (value === 'null') {
        constantValue = null;
      } else if (value === 'emptyArr') {
        constantValue = emptyArr;
      } else if (value === 'emptyObj') {
        constantValue = emptyObj;
      } else if (value === 'noopFunc') {
        constantValue = noopFunc;
      } else if (value === 'noopCallbackFunc') {
        constantValue = noopCallbackFunc;
      } else if (value === 'trueFunc') {
        constantValue = trueFunc;
      } else if (value === 'falseFunc') {
        constantValue = falseFunc;
      } else if (value === 'throwFunc') {
        constantValue = throwFunc;
      } else if (value === 'noopPromiseResolve') {
        constantValue = noopPromiseResolve;
      } else if (value === 'noopPromiseReject') {
        constantValue = noopPromiseReject;
      } else if (/^\d+$/.test(value)) {
        constantValue = parseFloat(value);
        if (nativeIsNaN(constantValue)) {
          return;
        }
        if (Math.abs(constantValue) > 32767) {
          return;
        }
      } else if (value === '-1') {
        constantValue = -1;
      } else if (value === '') {
        constantValue = '';
      } else if (value === 'yes') {
        constantValue = 'yes';
      } else if (value === 'no') {
        constantValue = 'no';
      } else {
        return;
      }
      var valueWrapperNames = [
        'asFunction',
        'asCallback',
        'asResolved',
        'asRejected',
      ];
      if (valueWrapperNames.includes(valueWrapper)) {
        var valueWrappersMap = {
          asFunction(v) {
            return function () {
              return v;
            };
          },
          asCallback(v) {
            return function () {
              return function () {
                return v;
              };
            };
          },
          asResolved(v) {
            return Promise.resolve(v);
          },
          asRejected(v) {
            return Promise.reject(v);
          },
        };
        constantValue = valueWrappersMap[valueWrapper](constantValue);
      }
      var canceled = false;
      var mustCancel = function mustCancel(value) {
        if (canceled) {
          return canceled;
        }
        canceled =
          value !== undefined &&
          constantValue !== undefined &&
          typeof value !== typeof constantValue &&
          value !== null;
        return canceled;
      };
      var trapProp = function trapProp(base, prop, configurable, handler) {
        if (!handler.init(base[prop])) {
          return false;
        }
        var origDescriptor = Object.getOwnPropertyDescriptor(base, prop);
        var prevSetter;
        if (origDescriptor instanceof Object) {
          if (!origDescriptor.configurable) {
            var message = `Property '${prop}' is not configurable`;
            logMessage(source, message);
            return false;
          }
          if (base[prop]) {
            base[prop] = constantValue;
          }
          if (origDescriptor.set instanceof Function) {
            prevSetter = origDescriptor.set;
          }
        }
        Object.defineProperty(base, prop, {
          configurable: configurable,
          get() {
            return handler.get();
          },
          set(a) {
            if (prevSetter !== undefined) {
              prevSetter(a);
            }
            if (a instanceof Object) {
              var propertiesToCheck = property.split('.').slice(1);
              if (setProxyTrap && !isProxyTrapSet) {
                isProxyTrapSet = true;
                a = new Proxy(a, {
                  get: function get(target, propertyKey, val) {
                    propertiesToCheck.reduce(function (
                      object,
                      currentProp,
                      index,
                      array
                    ) {
                      var currentObj =
                        object === null || object === void 0
                          ? void 0
                          : object[currentProp];
                      if (
                        index === array.length - 1 &&
                        currentObj !== constantValue
                      ) {
                        object[currentProp] = constantValue;
                      }
                      return currentObj || object;
                    },
                    target);
                    return Reflect.get(target, propertyKey, val);
                  },
                });
              }
            }
            handler.set(a);
          },
        });
        return true;
      };
      var _setChainPropAccess = function setChainPropAccess(owner, property) {
        var chainInfo = getPropertyInChain(owner, property);
        var { base: base } = chainInfo;
        var { prop: prop, chain: chain } = chainInfo;
        var inChainPropHandler = {
          factValue: undefined,
          init(a) {
            this.factValue = a;
            return true;
          },
          get() {
            return this.factValue;
          },
          set(a) {
            if (this.factValue === a) {
              return;
            }
            this.factValue = a;
            if (a instanceof Object) {
              _setChainPropAccess(a, chain);
            }
          },
        };
        var endPropHandler = {
          init(a) {
            if (mustCancel(a)) {
              return false;
            }
            return true;
          },
          get() {
            return constantValue;
          },
          set(a) {
            if (!mustCancel(a)) {
              return;
            }
            constantValue = a;
          },
        };
        if (!chain) {
          var isTrapped = trapProp(base, prop, false, endPropHandler);
          if (isTrapped) {
            hit(source);
          }
          return;
        }
        if (base !== undefined && base[prop] === null) {
          trapProp(base, prop, true, inChainPropHandler);
          return;
        }
        if (
          (base instanceof Object || typeof base === 'object') &&
          isEmptyObject(base)
        ) {
          trapProp(base, prop, true, inChainPropHandler);
        }
        var propValue = owner[prop];
        if (
          propValue instanceof Object ||
          (typeof propValue === 'object' && propValue !== null)
        ) {
          _setChainPropAccess(propValue, chain);
        }
        trapProp(base, prop, true, inChainPropHandler);
      };
      _setChainPropAccess(window, property);
    }
    function hit(source) {
      var ADGUARD_PREFIX = '[AdGuard]';
      if (!source.verbose) {
        return;
      }
      try {
        var trace = console.trace.bind(console);
        var label = `${ADGUARD_PREFIX} `;
        if (source.engine === 'corelibs') {
          label += source.ruleText;
        } else {
          if (source.domainName) {
            label += `${source.domainName}`;
          }
          if (source.args) {
            label += `#%#//scriptlet('${source.name}', '${source.args.join(
              "', '"
            )}')`;
          } else {
            label += `#%#//scriptlet('${source.name}')`;
          }
        }
        if (trace) {
          trace(label);
        }
      } catch (e) {}
      if (typeof window.__debug === 'function') {
        window.__debug(source);
      }
    }
    function logMessage(source, message) {
      var forced =
        arguments.length > 2 && arguments[2] !== undefined
          ? arguments[2]
          : false;
      var convertMessageToString =
        arguments.length > 3 && arguments[3] !== undefined
          ? arguments[3]
          : true;
      var { name: name, verbose: verbose } = source;
      if (!forced && !verbose) {
        return;
      }
      var nativeConsole = console.log;
      if (!convertMessageToString) {
        nativeConsole(`${name}:`, message);
        return;
      }
      nativeConsole(`${name}: ${message}`);
    }
    function getNumberFromString(rawString) {
      var parsedDelay = parseInt(rawString, 10);
      var validDelay = nativeIsNaN(parsedDelay) ? null : parsedDelay;
      return validDelay;
    }
    function noopArray() {
      return [];
    }
    function noopObject() {
      return {};
    }
    function noopFunc() {}
    function noopCallbackFunc() {
      return noopFunc;
    }
    function trueFunc() {
      return true;
    }
    function falseFunc() {
      return false;
    }
    function throwFunc() {
      throw new Error();
    }
    function noopPromiseReject() {
      return Promise.reject();
    }
    function noopPromiseResolve() {
      var responseBody =
        arguments.length > 0 && arguments[0] !== undefined
          ? arguments[0]
          : '{}';
      var responseUrl =
        arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : '';
      var responseType =
        arguments.length > 2 && arguments[2] !== undefined
          ? arguments[2]
          : 'basic';
      if (typeof Response === 'undefined') {
        return;
      }
      var response = new Response(responseBody, {
        status: 200,
        statusText: 'OK',
      });
      if (responseType === 'opaque') {
        Object.defineProperties(response, {
          body: {
            value: null,
          },
          status: {
            value: 0,
          },
          ok: {
            value: false,
          },
          statusText: {
            value: '',
          },
          url: {
            value: '',
          },
          type: {
            value: responseType,
          },
        });
      } else {
        Object.defineProperties(response, {
          url: {
            value: responseUrl,
          },
          type: {
            value: responseType,
          },
        });
      }
      return Promise.resolve(response);
    }
    function getPropertyInChain(base, chain) {
      var pos = chain.indexOf('.');
      if (pos === -1) {
        return {
          base: base,
          prop: chain,
        };
      }
      var prop = chain.slice(0, pos);
      if (base === null) {
        return {
          base: base,
          prop: prop,
          chain: chain,
        };
      }
      var nextBase = base[prop];
      chain = chain.slice(pos + 1);
      if (
        (base instanceof Object || typeof base === 'object') &&
        isEmptyObject(base)
      ) {
        return {
          base: base,
          prop: prop,
          chain: chain,
        };
      }
      if (nextBase === null) {
        return {
          base: base,
          prop: prop,
          chain: chain,
        };
      }
      if (nextBase !== undefined) {
        return getPropertyInChain(nextBase, chain);
      }
      Object.defineProperty(base, prop, {
        configurable: true,
      });
      return {
        base: base,
        prop: prop,
        chain: chain,
      };
    }
    function matchStackTrace(stackMatch, stackTrace) {
      if (!stackMatch || stackMatch === '') {
        return true;
      }
      var regExpValues = backupRegExpValues();
      if (shouldAbortInlineOrInjectedScript(stackMatch, stackTrace)) {
        if (regExpValues.length && regExpValues[0] !== RegExp.$1) {
          restoreRegExpValues(regExpValues);
        }
        return true;
      }
      var stackRegexp = toRegExp(stackMatch);
      var refinedStackTrace = stackTrace
        .split('\n')
        .slice(2)
        .map(function (line) {
          return line.trim();
        })
        .join('\n');
      if (regExpValues.length && regExpValues[0] !== RegExp.$1) {
        restoreRegExpValues(regExpValues);
      }
      return getNativeRegexpTest().call(stackRegexp, refinedStackTrace);
    }
    function nativeIsNaN(num) {
      var native = Number.isNaN || window.isNaN;
      return native(num);
    }
    function isEmptyObject(obj) {
      return Object.keys(obj).length === 0 && !obj.prototype;
    }
    function shouldAbortInlineOrInjectedScript(stackMatch, stackTrace) {
      var INLINE_SCRIPT_STRING = 'inlineScript';
      var INJECTED_SCRIPT_STRING = 'injectedScript';
      var INJECTED_SCRIPT_MARKER = '<anonymous>';
      var isInlineScript = function isInlineScript(match) {
        return match.includes(INLINE_SCRIPT_STRING);
      };
      var isInjectedScript = function isInjectedScript(match) {
        return match.includes(INJECTED_SCRIPT_STRING);
      };
      if (!(isInlineScript(stackMatch) || isInjectedScript(stackMatch))) {
        return false;
      }
      var documentURL = window.location.href;
      var pos = documentURL.indexOf('#');
      if (pos !== -1) {
        documentURL = documentURL.slice(0, pos);
      }
      var stackSteps = stackTrace
        .split('\n')
        .slice(2)
        .map(function (line) {
          return line.trim();
        });
      var stackLines = stackSteps.map(function (line) {
        var stack;
        var getStackTraceValues = /(.*?@)?(\S+)(:\d+)(:\d+)\)?$/.exec(line);
        if (getStackTraceValues) {
          var _stackURL, _stackURL2;
          var stackURL = getStackTraceValues[2];
          var stackLine = getStackTraceValues[3];
          var stackCol = getStackTraceValues[4];
          if (
            (_stackURL = stackURL) !== null &&
            _stackURL !== void 0 &&
            _stackURL.startsWith('(')
          ) {
            stackURL = stackURL.slice(1);
          }
          if (
            (_stackURL2 = stackURL) !== null &&
            _stackURL2 !== void 0 &&
            _stackURL2.startsWith(INJECTED_SCRIPT_MARKER)
          ) {
            var _stackFunction;
            stackURL = INJECTED_SCRIPT_STRING;
            var stackFunction =
              getStackTraceValues[1] !== undefined
                ? getStackTraceValues[1].slice(0, -1)
                : line.slice(0, getStackTraceValues.index).trim();
            if (
              (_stackFunction = stackFunction) !== null &&
              _stackFunction !== void 0 &&
              _stackFunction.startsWith('at')
            ) {
              stackFunction = stackFunction.slice(2).trim();
            }
            stack =
              `${stackFunction} ${stackURL}${stackLine}${stackCol}`.trim();
          } else if (stackURL === documentURL) {
            stack = `${INLINE_SCRIPT_STRING}${stackLine}${stackCol}`.trim();
          } else {
            stack = `${stackURL}${stackLine}${stackCol}`.trim();
          }
        } else {
          stack = line;
        }
        return stack;
      });
      if (stackLines) {
        for (var index = 0; index < stackLines.length; index += 1) {
          if (
            isInlineScript(stackMatch) &&
            stackLines[index].startsWith(INLINE_SCRIPT_STRING) &&
            stackLines[index].match(toRegExp(stackMatch))
          ) {
            return true;
          }
          if (
            isInjectedScript(stackMatch) &&
            stackLines[index].startsWith(INJECTED_SCRIPT_STRING) &&
            stackLines[index].match(toRegExp(stackMatch))
          ) {
            return true;
          }
        }
      }
      return false;
    }
    function getNativeRegexpTest() {
      var descriptor = Object.getOwnPropertyDescriptor(
        RegExp.prototype,
        'test'
      );
      var nativeRegexTest =
        descriptor === null || descriptor === void 0
          ? void 0
          : descriptor.value;
      if (descriptor && typeof descriptor.value === 'function') {
        return nativeRegexTest;
      }
      throw new Error('RegExp.prototype.test is not a function');
    }
    function toRegExp(rawInput) {
      var input = rawInput || '';
      var DEFAULT_VALUE = '.?';
      var FORWARD_SLASH = '/';
      if (input === '') {
        return new RegExp(DEFAULT_VALUE);
      }
      var delimiterIndex = input.lastIndexOf(FORWARD_SLASH);
      var flagsPart = input.substring(delimiterIndex + 1);
      var regExpPart = input.substring(0, delimiterIndex + 1);
      var isValidRegExpFlag = function isValidRegExpFlag(flag) {
        if (!flag) {
          return false;
        }
        try {
          new RegExp('', flag);
          return true;
        } catch (ex) {
          return false;
        }
      };
      var getRegExpFlags = function getRegExpFlags(regExpStr, flagsStr) {
        if (
          regExpStr.startsWith(FORWARD_SLASH) &&
          regExpStr.endsWith(FORWARD_SLASH) &&
          !regExpStr.endsWith('\\/') &&
          isValidRegExpFlag(flagsStr)
        ) {
          return flagsStr;
        }
        return '';
      };
      var flags = getRegExpFlags(regExpPart, flagsPart);
      if (
        (input.startsWith(FORWARD_SLASH) && input.endsWith(FORWARD_SLASH)) ||
        flags
      ) {
        var regExpInput = flags ? regExpPart : input;
        return new RegExp(regExpInput.slice(1, -1), flags);
      }
      var escaped = input
        .replace(/\\'/g, "'")
        .replace(/\\"/g, '"')
        .replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
      return new RegExp(escaped);
    }
    function backupRegExpValues() {
      try {
        var arrayOfRegexpValues = [];
        for (var index = 1; index < 10; index += 1) {
          var value = `$${index}`;
          if (!RegExp[value]) {
            break;
          }
          arrayOfRegexpValues.push(RegExp[value]);
        }
        return arrayOfRegexpValues;
      } catch (error) {
        return [];
      }
    }
    function restoreRegExpValues(array) {
      if (!array.length) {
        return;
      }
      try {
        var stringPattern = '';
        if (array.length === 1) {
          stringPattern = `(${array[0]})`;
        } else {
          stringPattern = array.reduce(function (
            accumulator,
            currentValue,
            currentIndex
          ) {
            if (currentIndex === 1) {
              return `(${accumulator}),(${currentValue})`;
            }
            return `${accumulator},(${currentValue})`;
          });
        }
        var regExpGroup = new RegExp(stringPattern);
        array.toString().replace(regExpGroup, '');
      } catch (error) {
        var message = `Failed to restore RegExp values: ${error}`;
        console.log(message);
      }
    }
    var updatedArgs = args ? [].concat(source).concat(args) : [source];
    try {
      setConstant.apply(this, updatedArgs);
      if (source.uniqueId) {
        Object.defineProperty(Window.prototype.toString, uniqueIdentifier, {
          value: flag,
          enumerable: false,
          writable: false,
          configurable: false,
        });
      }
    } catch (e) {
      console.log(e);
    }
  })({"name":"set-constant","args":["navigator.getBattery","noopPromiseResolve"],"ruleText":"#%#//scriptlet('set-constant', 'navigator.getBattery', 'noopPromiseResolve')"}, ["navigator.getBattery","noopPromiseResolve"]);
(function noTopics(source, args) {
    var flag = 'done';
    var uniqueIdentifier =
      source.uniqueId +
      source.name +
      '_' +
      (Array.isArray(args) ? args.join('_') : '');
    if (source.uniqueId) {
      if (Window.prototype.toString[uniqueIdentifier] === flag) {
        return;
      }
    }
    function noTopics(source) {
      var TOPICS_PROPERTY_NAME = 'browsingTopics';
      if (Document instanceof Object === false) {
        return;
      }
      if (
        !Object.prototype.hasOwnProperty.call(
          Document.prototype,
          TOPICS_PROPERTY_NAME
        ) ||
        Document.prototype[TOPICS_PROPERTY_NAME] instanceof Function === false
      ) {
        return;
      }
      Document.prototype[TOPICS_PROPERTY_NAME] = function () {
        return noopPromiseResolve('[]');
      };
      hit(source);
    }
    function hit(source) {
      var ADGUARD_PREFIX = '[AdGuard]';
      if (!source.verbose) {
        return;
      }
      try {
        var trace = console.trace.bind(console);
        var label = `${ADGUARD_PREFIX} `;
        if (source.engine === 'corelibs') {
          label += source.ruleText;
        } else {
          if (source.domainName) {
            label += `${source.domainName}`;
          }
          if (source.args) {
            label += `#%#//scriptlet('${source.name}', '${source.args.join(
              "', '"
            )}')`;
          } else {
            label += `#%#//scriptlet('${source.name}')`;
          }
        }
        if (trace) {
          trace(label);
        }
      } catch (e) {}
      if (typeof window.__debug === 'function') {
        window.__debug(source);
      }
    }
    function noopPromiseResolve() {
      var responseBody =
        arguments.length > 0 && arguments[0] !== undefined
          ? arguments[0]
          : '{}';
      var responseUrl =
        arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : '';
      var responseType =
        arguments.length > 2 && arguments[2] !== undefined
          ? arguments[2]
          : 'basic';
      if (typeof Response === 'undefined') {
        return;
      }
      var response = new Response(responseBody, {
        status: 200,
        statusText: 'OK',
      });
      if (responseType === 'opaque') {
        Object.defineProperties(response, {
          body: {
            value: null,
          },
          status: {
            value: 0,
          },
          ok: {
            value: false,
          },
          statusText: {
            value: '',
          },
          url: {
            value: '',
          },
          type: {
            value: responseType,
          },
        });
      } else {
        Object.defineProperties(response, {
          url: {
            value: responseUrl,
          },
          type: {
            value: responseType,
          },
        });
      }
      return Promise.resolve(response);
    }
    var updatedArgs = args ? [].concat(source).concat(args) : [source];
    try {
      noTopics.apply(this, updatedArgs);
      if (source.uniqueId) {
        Object.defineProperty(Window.prototype.toString, uniqueIdentifier, {
          value: flag,
          enumerable: false,
          writable: false,
          configurable: false,
        });
      }
    } catch (e) {
      console.log(e);
    }
  })({"name":"no-topics","args":[],"ruleText":"#%#//scriptlet('no-topics')"}, []);
(function setConstant(source, args) {
    var flag = 'done';
    var uniqueIdentifier =
      source.uniqueId +
      source.name +
      '_' +
      (Array.isArray(args) ? args.join('_') : '');
    if (source.uniqueId) {
      if (Window.prototype.toString[uniqueIdentifier] === flag) {
        return;
      }
    }
    function setConstant(source, property, value) {
      var stack =
        arguments.length > 3 && arguments[3] !== undefined ? arguments[3] : '';
      var valueWrapper =
        arguments.length > 4 && arguments[4] !== undefined ? arguments[4] : '';
      var setProxyTrap =
        arguments.length > 5 && arguments[5] !== undefined
          ? arguments[5]
          : false;
      var uboAliases = [
        'set-constant.js',
        'ubo-set-constant.js',
        'set.js',
        'ubo-set.js',
        'ubo-set-constant',
        'ubo-set',
      ];
      if (uboAliases.includes(source.name)) {
        if (stack.length !== 1 && !getNumberFromString(stack)) {
          valueWrapper = stack;
        }
        stack = undefined;
      }
      if (!property || !matchStackTrace(stack, new Error().stack)) {
        return;
      }
      var isProxyTrapSet = false;
      var emptyArr = noopArray();
      var emptyObj = noopObject();
      var constantValue;
      if (value === 'undefined') {
        constantValue = undefined;
      } else if (value === 'false') {
        constantValue = false;
      } else if (value === 'true') {
        constantValue = true;
      } else if (value === 'null') {
        constantValue = null;
      } else if (value === 'emptyArr') {
        constantValue = emptyArr;
      } else if (value === 'emptyObj') {
        constantValue = emptyObj;
      } else if (value === 'noopFunc') {
        constantValue = noopFunc;
      } else if (value === 'noopCallbackFunc') {
        constantValue = noopCallbackFunc;
      } else if (value === 'trueFunc') {
        constantValue = trueFunc;
      } else if (value === 'falseFunc') {
        constantValue = falseFunc;
      } else if (value === 'throwFunc') {
        constantValue = throwFunc;
      } else if (value === 'noopPromiseResolve') {
        constantValue = noopPromiseResolve;
      } else if (value === 'noopPromiseReject') {
        constantValue = noopPromiseReject;
      } else if (/^\d+$/.test(value)) {
        constantValue = parseFloat(value);
        if (nativeIsNaN(constantValue)) {
          return;
        }
        if (Math.abs(constantValue) > 32767) {
          return;
        }
      } else if (value === '-1') {
        constantValue = -1;
      } else if (value === '') {
        constantValue = '';
      } else if (value === 'yes') {
        constantValue = 'yes';
      } else if (value === 'no') {
        constantValue = 'no';
      } else {
        return;
      }
      var valueWrapperNames = [
        'asFunction',
        'asCallback',
        'asResolved',
        'asRejected',
      ];
      if (valueWrapperNames.includes(valueWrapper)) {
        var valueWrappersMap = {
          asFunction(v) {
            return function () {
              return v;
            };
          },
          asCallback(v) {
            return function () {
              return function () {
                return v;
              };
            };
          },
          asResolved(v) {
            return Promise.resolve(v);
          },
          asRejected(v) {
            return Promise.reject(v);
          },
        };
        constantValue = valueWrappersMap[valueWrapper](constantValue);
      }
      var canceled = false;
      var mustCancel = function mustCancel(value) {
        if (canceled) {
          return canceled;
        }
        canceled =
          value !== undefined &&
          constantValue !== undefined &&
          typeof value !== typeof constantValue &&
          value !== null;
        return canceled;
      };
      var trapProp = function trapProp(base, prop, configurable, handler) {
        if (!handler.init(base[prop])) {
          return false;
        }
        var origDescriptor = Object.getOwnPropertyDescriptor(base, prop);
        var prevSetter;
        if (origDescriptor instanceof Object) {
          if (!origDescriptor.configurable) {
            var message = `Property '${prop}' is not configurable`;
            logMessage(source, message);
            return false;
          }
          if (base[prop]) {
            base[prop] = constantValue;
          }
          if (origDescriptor.set instanceof Function) {
            prevSetter = origDescriptor.set;
          }
        }
        Object.defineProperty(base, prop, {
          configurable: configurable,
          get() {
            return handler.get();
          },
          set(a) {
            if (prevSetter !== undefined) {
              prevSetter(a);
            }
            if (a instanceof Object) {
              var propertiesToCheck = property.split('.').slice(1);
              if (setProxyTrap && !isProxyTrapSet) {
                isProxyTrapSet = true;
                a = new Proxy(a, {
                  get: function get(target, propertyKey, val) {
                    propertiesToCheck.reduce(function (
                      object,
                      currentProp,
                      index,
                      array
                    ) {
                      var currentObj =
                        object === null || object === void 0
                          ? void 0
                          : object[currentProp];
                      if (
                        index === array.length - 1 &&
                        currentObj !== constantValue
                      ) {
                        object[currentProp] = constantValue;
                      }
                      return currentObj || object;
                    },
                    target);
                    return Reflect.get(target, propertyKey, val);
                  },
                });
              }
            }
            handler.set(a);
          },
        });
        return true;
      };
      var _setChainPropAccess = function setChainPropAccess(owner, property) {
        var chainInfo = getPropertyInChain(owner, property);
        var { base: base } = chainInfo;
        var { prop: prop, chain: chain } = chainInfo;
        var inChainPropHandler = {
          factValue: undefined,
          init(a) {
            this.factValue = a;
            return true;
          },
          get() {
            return this.factValue;
          },
          set(a) {
            if (this.factValue === a) {
              return;
            }
            this.factValue = a;
            if (a instanceof Object) {
              _setChainPropAccess(a, chain);
            }
          },
        };
        var endPropHandler = {
          init(a) {
            if (mustCancel(a)) {
              return false;
            }
            return true;
          },
          get() {
            return constantValue;
          },
          set(a) {
            if (!mustCancel(a)) {
              return;
            }
            constantValue = a;
          },
        };
        if (!chain) {
          var isTrapped = trapProp(base, prop, false, endPropHandler);
          if (isTrapped) {
            hit(source);
          }
          return;
        }
        if (base !== undefined && base[prop] === null) {
          trapProp(base, prop, true, inChainPropHandler);
          return;
        }
        if (
          (base instanceof Object || typeof base === 'object') &&
          isEmptyObject(base)
        ) {
          trapProp(base, prop, true, inChainPropHandler);
        }
        var propValue = owner[prop];
        if (
          propValue instanceof Object ||
          (typeof propValue === 'object' && propValue !== null)
        ) {
          _setChainPropAccess(propValue, chain);
        }
        trapProp(base, prop, true, inChainPropHandler);
      };
      _setChainPropAccess(window, property);
    }
    function hit(source) {
      var ADGUARD_PREFIX = '[AdGuard]';
      if (!source.verbose) {
        return;
      }
      try {
        var trace = console.trace.bind(console);
        var label = `${ADGUARD_PREFIX} `;
        if (source.engine === 'corelibs') {
          label += source.ruleText;
        } else {
          if (source.domainName) {
            label += `${source.domainName}`;
          }
          if (source.args) {
            label += `#%#//scriptlet('${source.name}', '${source.args.join(
              "', '"
            )}')`;
          } else {
            label += `#%#//scriptlet('${source.name}')`;
          }
        }
        if (trace) {
          trace(label);
        }
      } catch (e) {}
      if (typeof window.__debug === 'function') {
        window.__debug(source);
      }
    }
    function logMessage(source, message) {
      var forced =
        arguments.length > 2 && arguments[2] !== undefined
          ? arguments[2]
          : false;
      var convertMessageToString =
        arguments.length > 3 && arguments[3] !== undefined
          ? arguments[3]
          : true;
      var { name: name, verbose: verbose } = source;
      if (!forced && !verbose) {
        return;
      }
      var nativeConsole = console.log;
      if (!convertMessageToString) {
        nativeConsole(`${name}:`, message);
        return;
      }
      nativeConsole(`${name}: ${message}`);
    }
    function getNumberFromString(rawString) {
      var parsedDelay = parseInt(rawString, 10);
      var validDelay = nativeIsNaN(parsedDelay) ? null : parsedDelay;
      return validDelay;
    }
    function noopArray() {
      return [];
    }
    function noopObject() {
      return {};
    }
    function noopFunc() {}
    function noopCallbackFunc() {
      return noopFunc;
    }
    function trueFunc() {
      return true;
    }
    function falseFunc() {
      return false;
    }
    function throwFunc() {
      throw new Error();
    }
    function noopPromiseReject() {
      return Promise.reject();
    }
    function noopPromiseResolve() {
      var responseBody =
        arguments.length > 0 && arguments[0] !== undefined
          ? arguments[0]
          : '{}';
      var responseUrl =
        arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : '';
      var responseType =
        arguments.length > 2 && arguments[2] !== undefined
          ? arguments[2]
          : 'basic';
      if (typeof Response === 'undefined') {
        return;
      }
      var response = new Response(responseBody, {
        status: 200,
        statusText: 'OK',
      });
      if (responseType === 'opaque') {
        Object.defineProperties(response, {
          body: {
            value: null,
          },
          status: {
            value: 0,
          },
          ok: {
            value: false,
          },
          statusText: {
            value: '',
          },
          url: {
            value: '',
          },
          type: {
            value: responseType,
          },
        });
      } else {
        Object.defineProperties(response, {
          url: {
            value: responseUrl,
          },
          type: {
            value: responseType,
          },
        });
      }
      return Promise.resolve(response);
    }
    function getPropertyInChain(base, chain) {
      var pos = chain.indexOf('.');
      if (pos === -1) {
        return {
          base: base,
          prop: chain,
        };
      }
      var prop = chain.slice(0, pos);
      if (base === null) {
        return {
          base: base,
          prop: prop,
          chain: chain,
        };
      }
      var nextBase = base[prop];
      chain = chain.slice(pos + 1);
      if (
        (base instanceof Object || typeof base === 'object') &&
        isEmptyObject(base)
      ) {
        return {
          base: base,
          prop: prop,
          chain: chain,
        };
      }
      if (nextBase === null) {
        return {
          base: base,
          prop: prop,
          chain: chain,
        };
      }
      if (nextBase !== undefined) {
        return getPropertyInChain(nextBase, chain);
      }
      Object.defineProperty(base, prop, {
        configurable: true,
      });
      return {
        base: base,
        prop: prop,
        chain: chain,
      };
    }
    function matchStackTrace(stackMatch, stackTrace) {
      if (!stackMatch || stackMatch === '') {
        return true;
      }
      var regExpValues = backupRegExpValues();
      if (shouldAbortInlineOrInjectedScript(stackMatch, stackTrace)) {
        if (regExpValues.length && regExpValues[0] !== RegExp.$1) {
          restoreRegExpValues(regExpValues);
        }
        return true;
      }
      var stackRegexp = toRegExp(stackMatch);
      var refinedStackTrace = stackTrace
        .split('\n')
        .slice(2)
        .map(function (line) {
          return line.trim();
        })
        .join('\n');
      if (regExpValues.length && regExpValues[0] !== RegExp.$1) {
        restoreRegExpValues(regExpValues);
      }
      return getNativeRegexpTest().call(stackRegexp, refinedStackTrace);
    }
    function nativeIsNaN(num) {
      var native = Number.isNaN || window.isNaN;
      return native(num);
    }
    function isEmptyObject(obj) {
      return Object.keys(obj).length === 0 && !obj.prototype;
    }
    function shouldAbortInlineOrInjectedScript(stackMatch, stackTrace) {
      var INLINE_SCRIPT_STRING = 'inlineScript';
      var INJECTED_SCRIPT_STRING = 'injectedScript';
      var INJECTED_SCRIPT_MARKER = '<anonymous>';
      var isInlineScript = function isInlineScript(match) {
        return match.includes(INLINE_SCRIPT_STRING);
      };
      var isInjectedScript = function isInjectedScript(match) {
        return match.includes(INJECTED_SCRIPT_STRING);
      };
      if (!(isInlineScript(stackMatch) || isInjectedScript(stackMatch))) {
        return false;
      }
      var documentURL = window.location.href;
      var pos = documentURL.indexOf('#');
      if (pos !== -1) {
        documentURL = documentURL.slice(0, pos);
      }
      var stackSteps = stackTrace
        .split('\n')
        .slice(2)
        .map(function (line) {
          return line.trim();
        });
      var stackLines = stackSteps.map(function (line) {
        var stack;
        var getStackTraceValues = /(.*?@)?(\S+)(:\d+)(:\d+)\)?$/.exec(line);
        if (getStackTraceValues) {
          var _stackURL, _stackURL2;
          var stackURL = getStackTraceValues[2];
          var stackLine = getStackTraceValues[3];
          var stackCol = getStackTraceValues[4];
          if (
            (_stackURL = stackURL) !== null &&
            _stackURL !== void 0 &&
            _stackURL.startsWith('(')
          ) {
            stackURL = stackURL.slice(1);
          }
          if (
            (_stackURL2 = stackURL) !== null &&
            _stackURL2 !== void 0 &&
            _stackURL2.startsWith(INJECTED_SCRIPT_MARKER)
          ) {
            var _stackFunction;
            stackURL = INJECTED_SCRIPT_STRING;
            var stackFunction =
              getStackTraceValues[1] !== undefined
                ? getStackTraceValues[1].slice(0, -1)
                : line.slice(0, getStackTraceValues.index).trim();
            if (
              (_stackFunction = stackFunction) !== null &&
              _stackFunction !== void 0 &&
              _stackFunction.startsWith('at')
            ) {
              stackFunction = stackFunction.slice(2).trim();
            }
            stack =
              `${stackFunction} ${stackURL}${stackLine}${stackCol}`.trim();
          } else if (stackURL === documentURL) {
            stack = `${INLINE_SCRIPT_STRING}${stackLine}${stackCol}`.trim();
          } else {
            stack = `${stackURL}${stackLine}${stackCol}`.trim();
          }
        } else {
          stack = line;
        }
        return stack;
      });
      if (stackLines) {
        for (var index = 0; index < stackLines.length; index += 1) {
          if (
            isInlineScript(stackMatch) &&
            stackLines[index].startsWith(INLINE_SCRIPT_STRING) &&
            stackLines[index].match(toRegExp(stackMatch))
          ) {
            return true;
          }
          if (
            isInjectedScript(stackMatch) &&
            stackLines[index].startsWith(INJECTED_SCRIPT_STRING) &&
            stackLines[index].match(toRegExp(stackMatch))
          ) {
            return true;
          }
        }
      }
      return false;
    }
    function getNativeRegexpTest() {
      var descriptor = Object.getOwnPropertyDescriptor(
        RegExp.prototype,
        'test'
      );
      var nativeRegexTest =
        descriptor === null || descriptor === void 0
          ? void 0
          : descriptor.value;
      if (descriptor && typeof descriptor.value === 'function') {
        return nativeRegexTest;
      }
      throw new Error('RegExp.prototype.test is not a function');
    }
    function toRegExp(rawInput) {
      var input = rawInput || '';
      var DEFAULT_VALUE = '.?';
      var FORWARD_SLASH = '/';
      if (input === '') {
        return new RegExp(DEFAULT_VALUE);
      }
      var delimiterIndex = input.lastIndexOf(FORWARD_SLASH);
      var flagsPart = input.substring(delimiterIndex + 1);
      var regExpPart = input.substring(0, delimiterIndex + 1);
      var isValidRegExpFlag = function isValidRegExpFlag(flag) {
        if (!flag) {
          return false;
        }
        try {
          new RegExp('', flag);
          return true;
        } catch (ex) {
          return false;
        }
      };
      var getRegExpFlags = function getRegExpFlags(regExpStr, flagsStr) {
        if (
          regExpStr.startsWith(FORWARD_SLASH) &&
          regExpStr.endsWith(FORWARD_SLASH) &&
          !regExpStr.endsWith('\\/') &&
          isValidRegExpFlag(flagsStr)
        ) {
          return flagsStr;
        }
        return '';
      };
      var flags = getRegExpFlags(regExpPart, flagsPart);
      if (
        (input.startsWith(FORWARD_SLASH) && input.endsWith(FORWARD_SLASH)) ||
        flags
      ) {
        var regExpInput = flags ? regExpPart : input;
        return new RegExp(regExpInput.slice(1, -1), flags);
      }
      var escaped = input
        .replace(/\\'/g, "'")
        .replace(/\\"/g, '"')
        .replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
      return new RegExp(escaped);
    }
    function backupRegExpValues() {
      try {
        var arrayOfRegexpValues = [];
        for (var index = 1; index < 10; index += 1) {
          var value = `$${index}`;
          if (!RegExp[value]) {
            break;
          }
          arrayOfRegexpValues.push(RegExp[value]);
        }
        return arrayOfRegexpValues;
      } catch (error) {
        return [];
      }
    }
    function restoreRegExpValues(array) {
      if (!array.length) {
        return;
      }
      try {
        var stringPattern = '';
        if (array.length === 1) {
          stringPattern = `(${array[0]})`;
        } else {
          stringPattern = array.reduce(function (
            accumulator,
            currentValue,
            currentIndex
          ) {
            if (currentIndex === 1) {
              return `(${accumulator}),(${currentValue})`;
            }
            return `${accumulator},(${currentValue})`;
          });
        }
        var regExpGroup = new RegExp(stringPattern);
        array.toString().replace(regExpGroup, '');
      } catch (error) {
        var message = `Failed to restore RegExp values: ${error}`;
        console.log(message);
      }
    }
    var updatedArgs = args ? [].concat(source).concat(args) : [source];
    try {
      setConstant.apply(this, updatedArgs);
      if (source.uniqueId) {
        Object.defineProperty(Window.prototype.toString, uniqueIdentifier, {
          value: flag,
          enumerable: false,
          writable: false,
          configurable: false,
        });
      }
    } catch (e) {
      console.log(e);
    }
  })({"name":"set-constant","args":["navigator.privateAttribution","undefined"],"ruleText":"#%#//scriptlet('set-constant', 'navigator.privateAttribution', 'undefined')"}, ["navigator.privateAttribution","undefined"]);
(function noProtectedAudience(source, args) {
    var flag = 'done';
    var uniqueIdentifier =
      source.uniqueId +
      source.name +
      '_' +
      (Array.isArray(args) ? args.join('_') : '');
    if (source.uniqueId) {
      if (Window.prototype.toString[uniqueIdentifier] === flag) {
        return;
      }
    }
    function noProtectedAudience(source) {
      if (Document instanceof Object === false) {
        return;
      }
      var protectedAudienceMethods = {
        joinAdInterestGroup: noopResolveVoid,
        runAdAuction: noopResolveNull,
        leaveAdInterestGroup: noopResolveVoid,
        clearOriginJoinedAdInterestGroups: noopResolveVoid,
        createAuctionNonce: noopStr,
        updateAdInterestGroups: noopFunc,
      };
      for (
        var _i = 0, _Object$keys = Object.keys(protectedAudienceMethods);
        _i < _Object$keys.length;
        _i++
      ) {
        var key = _Object$keys[_i];
        var methodName = key;
        var prototype = Navigator.prototype;
        if (
          !Object.prototype.hasOwnProperty.call(prototype, methodName) ||
          prototype[methodName] instanceof Function === false
        ) {
          continue;
        }
        prototype[methodName] = protectedAudienceMethods[methodName];
      }
      hit(source);
    }
    function hit(source) {
      var ADGUARD_PREFIX = '[AdGuard]';
      if (!source.verbose) {
        return;
      }
      try {
        var trace = console.trace.bind(console);
        var label = `${ADGUARD_PREFIX} `;
        if (source.engine === 'corelibs') {
          label += source.ruleText;
        } else {
          if (source.domainName) {
            label += `${source.domainName}`;
          }
          if (source.args) {
            label += `#%#//scriptlet('${source.name}', '${source.args.join(
              "', '"
            )}')`;
          } else {
            label += `#%#//scriptlet('${source.name}')`;
          }
        }
        if (trace) {
          trace(label);
        }
      } catch (e) {}
      if (typeof window.__debug === 'function') {
        window.__debug(source);
      }
    }
    function noopStr() {
      return '';
    }
    function noopFunc() {}
    function noopResolveVoid() {
      return Promise.resolve(undefined);
    }
    function noopResolveNull() {
      return Promise.resolve(null);
    }
    var updatedArgs = args ? [].concat(source).concat(args) : [source];
    try {
      noProtectedAudience.apply(this, updatedArgs);
      if (source.uniqueId) {
        Object.defineProperty(Window.prototype.toString, uniqueIdentifier, {
          value: flag,
          enumerable: false,
          writable: false,
          configurable: false,
        });
      }
    } catch (e) {
      console.log(e);
    }
  })({"name":"no-protected-audience","args":[],"ruleText":"#%#//scriptlet('no-protected-audience')"}, []);
                } catch (ex) {
                    console.error('Error executing js: ' + ex);
                }
            })();
            