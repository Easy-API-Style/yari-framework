'use strict';var aa=typeof Object.defineProperties=="function"?Object.defineProperty:function(a,b,c){if(a==Array.prototype||a==Object.prototype)return a;a[b]=c.value;return a};
function ba(a){a=["object"==typeof globalThis&&globalThis,a,"object"==typeof window&&window,"object"==typeof self&&self,"object"==typeof global&&global];for(var b=0;b<a.length;++b){var c=a[b];if(c&&c.Math==Math)return c}throw Error("Cannot find global object");}
var ca=ba(this);function da(a,b){if(b)a:{var c=ca;a=a.split(".");for(var d=0;d<a.length-1;d++){var e=a[d];if(!(e in c))break a;c=c[e]}a=a[a.length-1];d=c[a];b=b(d);b!=d&&b!=null&&aa(c,a,{configurable:!0,writable:!0,value:b})}}
function ea(a){function b(d){return a.next(d)}
function c(d){return a.throw(d)}
return new Promise(function(d,e){function f(g){g.done?d(g.value):Promise.resolve(g.value).then(b,c).then(f,e)}
f(a.next())})}
function r(a){return ea(a())}
da("globalThis",function(a){return a||ca});
da("Symbol.dispose",function(a){return a?a:Symbol("Symbol.dispose")});
da("Object.values",function(a){return a?a:function(b){var c=[],d;for(d in b)Object.prototype.hasOwnProperty.call(b,d)&&c.push(b[d]);return c}});
da("Array.prototype.includes",function(a){return a?a:function(b,c){var d=this;d instanceof String&&(d=String(d));var e=d.length;c=c||0;for(c<0&&(c=Math.max(c+e,0));c<e;c++){var f=d[c];if(f===b||Object.is(f,b))return!0}return!1}});
da("Object.entries",function(a){return a?a:function(b){var c=[],d;for(d in b)Object.prototype.hasOwnProperty.call(b,d)&&c.push([d,b[d]]);return c}});
function fa(a,b){a instanceof String&&(a+="");var c=0,d=!1,e={next:function(){if(!d&&c<a.length){var f=c++;return{value:b(f,a[f]),done:!1}}d=!0;return{done:!0,value:void 0}}};
e[Symbol.iterator]=function(){return e};
return e}
da("Array.prototype.values",function(a){return a?a:function(){return fa(this,function(b,c){return c})}});
da("String.prototype.matchAll",function(a){return a?a:function(b){if(b instanceof RegExp&&!b.global)throw new TypeError("RegExp passed into String.prototype.matchAll() must have global tag.");var c=new RegExp(b,b instanceof RegExp?void 0:"g"),d=this,e=!1,f={next:function(){if(e)return{value:void 0,done:!0};var g=c.exec(d);if(!g)return e=!0,{value:void 0,done:!0};g[0]===""&&(c.lastIndex+=1);return{value:g,done:!1}}};
f[Symbol.iterator]=function(){return f};
return f}});
da("Promise.prototype.finally",function(a){return a?a:function(b){return this.then(function(c){return Promise.resolve(b()).then(function(){return c})},function(c){return Promise.resolve(b()).then(function(){throw c;
})})}});/*

 Copyright The Closure Library Authors.
 SPDX-License-Identifier: Apache-2.0
*/
var t=this||self;function ha(a,b){var c=v("CLOSURE_FLAGS");a=c&&c[a];return a!=null?a:b}
function v(a,b){a=a.split(".");b=b||t;for(var c=0;c<a.length;c++)if(b=b[a[c]],b==null)return null;return b}
function ja(a){var b=typeof a;b=b!="object"?b:a?Array.isArray(a)?"array":b:"null";return b=="array"||b=="object"&&typeof a.length=="number"}
function ka(a,b,c){return a.call.apply(a.bind,arguments)}
function la(a,b,c){if(!a)throw Error();if(arguments.length>2){var d=Array.prototype.slice.call(arguments,2);return function(){var e=Array.prototype.slice.call(arguments);Array.prototype.unshift.apply(e,d);return a.apply(b,e)}}return function(){return a.apply(b,arguments)}}
function ma(a,b,c){ma=Function.prototype.bind&&Function.prototype.bind.toString().indexOf("native code")!=-1?ka:la;return ma.apply(null,arguments)}
function w(a,b){a=a.split(".");for(var c=t,d;a.length&&(d=a.shift());)a.length||b===void 0?c[d]&&c[d]!==Object.prototype[d]?c=c[d]:c=c[d]={}:c[d]=b}
function na(a){return a}
function oa(a,b){function c(){}
c.prototype=b.prototype;a.Ja=b.prototype;a.prototype=new c;a.prototype.constructor=a;a.rb=function(d,e,f){for(var g=Array(arguments.length-2),h=2;h<arguments.length;h++)g[h-2]=arguments[h];return b.prototype[e].apply(d,g)}}
;function pa(a,b){if(Error.captureStackTrace)Error.captureStackTrace(this,pa);else{const c=Error().stack;c&&(this.stack=c)}a&&(this.message=String(a));b!==void 0&&(this.cause=b)}
oa(pa,Error);pa.prototype.name="CustomError";const qa=String.prototype.trim?function(a){return a.trim()}:function(a){return/^[\s\xa0]*([\s\S]*?)[\s\xa0]*$/.exec(a)[1]};/*

 Copyright Google LLC
 SPDX-License-Identifier: Apache-2.0
*/
let ra=globalThis.trustedTypes,sa;function ta(){let a=null;if(!ra)return a;try{const b=c=>c;
a=ra.createPolicy("goog#html",{createHTML:b,createScript:b,createScriptURL:b})}catch(b){}return a}
;var ua=class{constructor(a){this.h=a}toString(){return this.h+""}};function va(a,b=`unexpected value ${a}!`){throw Error(b);}
;function wa(a,b){Array.prototype.forEach.call(a,b,void 0)}
function xa(a,b){return Array.prototype.map.call(a,b,void 0)}
function ya(a,b){b=Array.prototype.indexOf.call(a,b,void 0);b>=0&&Array.prototype.splice.call(a,b,1)}
function za(a,b){for(let c=1;c<arguments.length;c++){const d=arguments[c];if(ja(d)){const e=a.length||0,f=d.length||0;a.length=e+f;for(let g=0;g<f;g++)a[e+g]=d[g]}else a.push(d)}}
;function Aa(a,b){a.__closure__error__context__984382||(a.__closure__error__context__984382={});a.__closure__error__context__984382.severity=b}
;function Ba(a){var b=v("window.location.href");a==null&&(a='Unknown Error of type "null/undefined"');if(typeof a==="string")return{message:a,name:"Unknown error",lineNumber:"Not available",fileName:b,stack:"Not available"};let c,d;var e=!1;try{c=a.lineNumber||a.line||"Not available"}catch(f){c="Not available",e=!0}try{d=a.fileName||a.filename||a.sourceURL||t.$googDebugFname||b}catch(f){d="Not available",e=!0}b=Ca(a);if(!(!e&&a.lineNumber&&a.fileName&&a.stack&&a.message&&a.name)){e=a.message;if(e==
null){if(a.constructor&&a.constructor instanceof Function){if(a.constructor.name)e=a.constructor.name;else if(e=a.constructor,Da[e])e=Da[e];else{e=String(e);if(!Da[e]){const f=/function\s+([^\(]+)/m.exec(e);Da[e]=f?f[1]:"[Anonymous]"}e=Da[e]}e='Unknown Error of type "'+e+'"'}else e="Unknown Error of unknown type";typeof a.toString==="function"&&Object.prototype.toString!==a.toString&&(e+=": "+a.toString())}return{message:e,name:a.name||"UnknownError",lineNumber:c,fileName:d,stack:b||"Not available"}}return{message:a.message,
name:a.name,lineNumber:a.lineNumber,fileName:a.fileName,stack:b}}
function Ca(a,b){b||(b={});b[Ea(a)]=!0;let c=a.stack||"";var d=a.cause;d&&!b[Ea(d)]&&(c+="\nCaused by: ",d.stack&&d.stack.indexOf(d.toString())==0||(c+=typeof d==="string"?d:d.message+"\n"),c+=Ca(d,b));a=a.errors;if(Array.isArray(a)){d=1;let e;for(e=0;e<a.length&&!(d>4);e++)b[Ea(a[e])]||(c+="\nInner error "+d++ +": ",a[e].stack&&a[e].stack.indexOf(a[e].toString())==0||(c+=typeof a[e]==="string"?a[e]:a[e].message+"\n"),c+=Ca(a[e],b));e<a.length&&(c+="\n... "+(a.length-e)+" more inner errors")}return c}
function Ea(a){let b="";typeof a.toString==="function"&&(b=""+a);return b+a.stack}
var Da={};var Fa=RegExp("^(?:([^:/?#.]+):)?(?://(?:([^\\\\/?#]*)@)?([^\\\\/?#]*?)(?::([0-9]+))?(?=[\\\\/?#]|$))?([^?#]+)?(?:\\?([^#]*))?(?:#([\\s\\S]*))?$");function Ga(a){return a?decodeURI(a):a}
function Ia(a,b,c){if(Array.isArray(b))for(let d=0;d<b.length;d++)Ia(a,String(b[d]),c);else b!=null&&c.push(a+(b===""?"":"="+encodeURIComponent(String(b))))}
function Ja(a){const b=[];for(const c in a)Ia(c,a[c],b);return b.join("&")}
;function Ka(){throw Error("Invalid UTF8");}
function La(a,b){b=String.fromCharCode.apply(null,b);return a==null?b:a+b}
let Ma=void 0,Na;const Oa=typeof TextDecoder!=="undefined";function Pa(a){t.setTimeout(()=>{throw a;},0)}
;var Qa=ha(610401301,!1),Ra=ha(748402147,!0);function Sa(){var a=t.navigator;return a&&(a=a.userAgent)?a:""}
var Ta;const Ua=t.navigator;Ta=Ua?Ua.userAgentData||null:null;function Va(a){if(!Qa||!Ta)return!1;for(let b=0;b<Ta.brands.length;b++){const {brand:c}=Ta.brands[b];if(c&&c.indexOf(a)!=-1)return!0}return!1}
function x(a){return Sa().indexOf(a)!=-1}
;function Wa(){return Qa?!!Ta&&Ta.brands.length>0:!1}
;var Xa=x("Safari")&&!((Wa()?Va("Chromium"):(x("Chrome")||x("CriOS"))&&(Wa()||!x("Edge"))||x("Silk"))||(Wa()?0:x("Coast"))||(Wa()?0:x("Opera"))||(Wa()?0:x("Edge"))||(Wa()?Va("Microsoft Edge"):x("Edg/"))||(Wa()?Va("Opera"):x("OPR"))||x("Firefox")||x("FxiOS")||x("Silk")||x("Android"))&&!(x("iPhone")&&!x("iPod")&&!x("iPad")||x("iPad")||x("iPod"));var Ya={},Za=null;function $a(a,b){b===void 0&&(b=0);ab();b=Ya[b];const c=Array(Math.floor(a.length/3)),d=b[64]||"";let e=0,f=0;for(;e<a.length-2;e+=3){var g=a[e],h=a[e+1],k=a[e+2],l=b[g>>2];g=b[(g&3)<<4|h>>4];h=b[(h&15)<<2|k>>6];k=b[k&63];c[f++]=""+l+g+h+k}l=0;k=d;switch(a.length-e){case 2:l=a[e+1],k=b[(l&15)<<2]||d;case 1:a=a[e],c[f]=""+b[a>>2]+b[(a&3)<<4|l>>4]+k+d}return c.join("")}
function bb(a){const b=a.length;let c=b*3/4;c%3?c=Math.floor(c):"=.".indexOf(a[b-1])!=-1&&(c="=.".indexOf(a[b-2])!=-1?c-2:c-1);const d=new Uint8Array(c);let e=0;cb(a,function(f){d[e++]=f});
return e!==c?d.subarray(0,e):d}
function cb(a,b){function c(e){for(;d<a.length;){const f=a.charAt(d++),g=Za[f];if(g!=null)return g;if(!/^[\s\xa0]*$/.test(f))throw Error("Unknown base64 encoding at char: "+f);}return e}
ab();let d=0;for(;;){const e=c(-1),f=c(0),g=c(64),h=c(64);if(h===64&&e===-1)break;b(e<<2|f>>4);g!=64&&(b(f<<4&240|g>>2),h!=64&&b(g<<6&192|h))}}
function ab(){if(!Za){Za={};var a="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".split(""),b=["+/=","+/","-_=","-_.","-_"];for(let c=0;c<5;c++){const d=a.concat(b[c].split(""));Ya[c]=d;for(let e=0;e<d.length;e++){const f=d[e];Za[f]===void 0&&(Za[f]=e)}}}}
;var db=typeof Uint8Array!=="undefined",eb=!(Wa()?0:x("Trident")||x("MSIE"))&&typeof btoa==="function";const fb=/[-_.]/g,gb={"-":"+",_:"/",".":"="};function hb(a){return gb[a]||""}
function ib(a){if(!eb)return bb(a);a=fb.test(a)?a.replace(fb,hb):a;a=atob(a);const b=new Uint8Array(a.length);for(let c=0;c<a.length;c++)b[c]=a.charCodeAt(c);return b}
var jb={};function kb(){return lb||(lb=new mb(null,jb))}
function nb(a){ob(jb);var b=a.h;b=b==null||db&&b!=null&&b instanceof Uint8Array?b:typeof b==="string"?ib(b):null;return b==null?b:a.h=b}
var mb=class{sizeBytes(){const a=nb(this);return a?a.length:0}constructor(a,b){ob(b);this.h=a;if(a!=null&&a.length===0)throw Error("ByteString should be constructed with non-empty values");}};let lb;function ob(a){if(a!==jb)throw Error("illegal external caller");}
;let pb=void 0;function qb(a){a=Error(a);Aa(a,"warning");return a}
function rb(a,b){if(a!=null){var c;var d=(c=pb)!=null?c:pb={};c=d[a]||0;c>=b||(d[a]=c+1,a=Error(),Aa(a,"incident"),Pa(a))}}
;var sb=typeof Symbol==="function"&&typeof Symbol()==="symbol";function tb(a,b,c=!1){return typeof Symbol==="function"&&typeof Symbol()==="symbol"?c&&Symbol.for&&a?Symbol.for(a):a!=null?Symbol(a):Symbol():b}
var ub=tb("jas",void 0,!0),vb=tb(void 0,"1oa"),wb=tb(void 0,Symbol()),xb=tb(void 0,"0ubs"),yb=tb(void 0,"0actk"),zb=tb("m_m","Db",!0);[...Object.values({Za:1,Ya:2,Xa:4,gb:8,mb:16,eb:32,Pa:64,Va:128,Ta:256,lb:512,Ua:1024,Wa:2048,fb:4096,ab:8192})];const Ab={Da:{value:0,configurable:!0,writable:!0,enumerable:!1}},Bb=Object.defineProperties,A=sb?ub:"Da";var Cb;const Db=[];B(Db,7);Cb=Object.freeze(Db);function Eb(a,b){sb||A in a||Bb(a,Ab);a[A]|=b}
function B(a,b){sb||A in a||Bb(a,Ab);a[A]=b}
;function Fb(){return typeof BigInt==="function"}
;var Gb={};function Hb(a,b){return b===void 0?a.h!==Ib&&!!(2&(a.o[A]|0)):!!(2&b)&&a.h!==Ib}
const Ib={};var Jb=Object.freeze({});function Kb(a,b,c){const d=b&128?0:-1,e=a.length;var f;if(f=!!e)f=a[e-1],f=f!=null&&typeof f==="object"&&f.constructor===Object;const g=e+(f?-1:0);for(b=b&128?1:0;b<g;b++)c(b-d,a[b]);if(f){a=a[e-1];for(const h in a)!isNaN(h)&&c(+h,a[h])}}
var Lb={};function Mb(a){a.zb=!0;return a}
;var Nb=Mb(a=>typeof a==="number"),Ob=Mb(a=>typeof a==="string"),Pb=Mb(a=>typeof a==="boolean"),Qb=Mb(a=>a!=null&&typeof a==="object"&&typeof a.then==="function");var Rb=typeof t.BigInt==="function"&&typeof t.BigInt(0)==="bigint";var Xb=Mb(a=>Rb?a>=Sb&&a<=Tb:a[0]==="-"?Ub(a,Vb):Ub(a,Wb));
const Vb=Number.MIN_SAFE_INTEGER.toString(),Sb=Rb?BigInt(Number.MIN_SAFE_INTEGER):void 0,Wb=Number.MAX_SAFE_INTEGER.toString(),Tb=Rb?BigInt(Number.MAX_SAFE_INTEGER):void 0;function Ub(a,b){if(a.length>b.length)return!1;if(a.length<b.length||a===b)return!0;for(let c=0;c<a.length;c++){const d=a[c],e=b[c];if(d>e)return!1;if(d<e)return!0}}
;const Yb=typeof Uint8Array.prototype.slice==="function";let C=0,D=0;function Zb(a){const b=a>>>0;C=b;D=(a-b)/4294967296>>>0}
function $b(a){if(a<0){Zb(0-a);const [b,c]=ac(C,D);C=b>>>0;D=c>>>0}else Zb(a)}
function bc(a,b){b>>>=0;a>>>=0;if(b<=2097151)var c=""+(4294967296*b+a);else Fb()?c=""+(BigInt(b)<<BigInt(32)|BigInt(a)):(c=(a>>>24|b<<8)&16777215,b=b>>16&65535,a=(a&16777215)+c*6777216+b*6710656,c+=b*8147497,b*=2,a>=1E7&&(c+=a/1E7>>>0,a%=1E7),c>=1E7&&(b+=c/1E7>>>0,c%=1E7),c=b+cc(c)+cc(a));return c}
function cc(a){a=String(a);return"0000000".slice(a.length)+a}
function ac(a,b){b=~b;a?a=~a+1:b+=1;return[a,b]}
;const dc=typeof BigInt==="function"?BigInt.asIntN:void 0,ec=Number.isSafeInteger,fc=Number.isFinite,hc=Math.trunc;function ic(a){return a.displayName||a.name||"unknown type name"}
const jc=/^-?([1-9][0-9]*|0)(\.[0-9]+)?$/;function kc(a){switch(typeof a){case "bigint":return!0;case "number":return fc(a);case "string":return jc.test(a);default:return!1}}
function lc(a){if(a==null)return a;if(typeof a==="string"&&a)a=+a;else if(typeof a!=="number")return;return fc(a)?a|0:void 0}
function mc(a){if(!kc(a))throw qb("int64");switch(typeof a){case "string":kc(a);var b=hc(Number(a));if(ec(b))a=String(b);else if(b=a.indexOf("."),b!==-1&&(a=a.substring(0,b)),b=a.length,!(a[0]==="-"?b<20||b===20&&a<="-9223372036854775808":b<19||b===19&&a<="9223372036854775807")){if(a.length<16)$b(Number(a));else if(Fb())a=BigInt(a),C=Number(a&BigInt(4294967295))>>>0,D=Number(a>>BigInt(32)&BigInt(4294967295));else{b=+(a[0]==="-");D=C=0;var c=a.length;for(let e=0+b,f=(c-b)%6+b;f<=c;e=f,f+=6){var d=
Number(a.slice(e,f));D*=1E6;C=C*1E6+d;C>=4294967296&&(D+=Math.trunc(C/4294967296),D>>>=0,C>>>=0)}if(b){const [e,f]=ac(C,D);C=e;D=f}}a=C;b=D;if(b&2147483648)if(Fb())a=""+(BigInt(b|0)<<BigInt(32)|BigInt(a>>>0));else{const [e,f]=ac(a,b);a="-"+bc(e,f)}else a=bc(a,b)}return a;case "bigint":b=a=dc(64,a);if(Ob(b)){if(!/^\s*(?:-?[1-9]\d*|0)?\s*$/.test(b))throw Error(String(b));}else if(Nb(b)&&!Number.isSafeInteger(b))throw Error(String(b));Rb?a=BigInt(a):a=Pb(a)?a?"1":"0":Ob(a)?a.trim()||"0":String(a);return a;
default:kc(a);a=hc(a);if(!ec(a)){$b(a);b=C;c=D;if(a=c&2147483648)b=~b+1>>>0,c=~c>>>0,b==0&&(c=c+1>>>0);d=c*4294967296+(b>>>0);b=Number.isSafeInteger(d)?d:bc(b,c);a=typeof b==="number"?a?-b:b:a?"-"+b:b}return a}}
function nc(a){if(a!=null&&typeof a!=="string")throw Error();return a}
function E(a){return a==null||typeof a==="string"?a:void 0}
function oc(a,b){if(!(a instanceof b))throw Error(`Expected instanceof ${ic(b)} but got ${a&&ic(a.constructor)}`);return a}
function pc(a,b,c){if(a!=null&&a[zb]===Gb)return a;if(Array.isArray(a)){var d=a[A]|0;c=d|c&32|c&2;c!==d&&B(a,c);return new b(a)}}
;function qc(a){return a}
;function rc(a){const b=na(wb);return b?a[b]:void 0}
function sc(a,b){for(const c in a)!isNaN(c)&&b(a,+c,a[c])}
function tc(a){const b=new uc;sc(a,(c,d,e)=>{b[d]=Array.prototype.slice.call(e)});
b.h=a.h;return b}
var uc=class{};function vc(a,b){b<100||rb(xb,1)}
;function wc(a,b,c,d){const e=d!==void 0;d=!!d;var f=na(wb),g;!e&&sb&&f&&(g=a[f])&&sc(g,vc);f=[];var h=a.length;let k;g=4294967295;let l=!1;const n=!!(b&64),p=n?b&128?0:-1:void 0;if(!(b&1||(k=h&&a[h-1],k!=null&&typeof k==="object"&&k.constructor===Object?(h--,g=h):k=void 0,!n||b&128||e))){l=!0;var q;g=((q=xc)!=null?q:qc)(g-p,p,a,k,void 0)+p}b=void 0;for(q=0;q<h;q++){let u=a[q];if(u!=null&&(u=c(u,d))!=null)if(n&&q>=g){const z=q-p;var m=void 0;((m=b)!=null?m:b={})[z]=u}else f[q]=u}if(k)for(let u in k){m=
k[u];if(m==null||(m=c(m,d))==null)continue;h=+u;let z;if(n&&!Number.isNaN(h)&&(z=h+p)<g)f[z]=m;else{let y;((y=b)!=null?y:b={})[u]=m}}b&&(l?f.push(b):f[g]=b);e&&na(wb)&&(a=rc(a))&&a instanceof uc&&(f[wb]=tc(a));return f}
function yc(a){switch(typeof a){case "number":return Number.isFinite(a)?a:""+a;case "bigint":return Xb(a)?Number(a):""+a;case "boolean":return a?1:0;case "object":if(Array.isArray(a)){var b=a[A]|0;return a.length===0&&b&1?void 0:wc(a,b,yc)}if(a!=null&&a[zb]===Gb)return zc(a);if(a instanceof mb){const e=a.h;if(e==null)a="";else if(typeof e==="string")a=e;else{if(eb){b="";for(var c=0,d=e.length-10240;c<d;)b+=String.fromCharCode.apply(null,e.subarray(c,c+=10240));b+=String.fromCharCode.apply(null,c?
e.subarray(c):e);b=btoa(b)}else b=$a(e);a=a.h=b}return a}return}return a}
let xc;function zc(a){a=a.o;return wc(a,a[A]|0,yc)}
;let Ac,Bc;function Cc(a){switch(typeof a){case "boolean":return Ac||(Ac=[0,void 0,!0]);case "number":return a>0?void 0:a===0?Bc||(Bc=[0,void 0]):[-a,void 0];case "string":return[0,a];case "object":return a}}
function Dc(a,b,c){return a=Ec(a,b[0],b[1],c?1:2)}
function Ec(a,b,c,d=0){if(a==null){var e=32;c?(a=[c],e|=128):a=[];b&&(e=e&-16760833|(b&1023)<<14)}else{if(!Array.isArray(a))throw Error("narr");e=a[A]|0;if(Ra&&1&e)throw Error("rfarr");2048&e&&!(2&e)&&Fc();if(e&256)throw Error("farr");if(e&64)return d!==0||e&2048||B(a,e|2048),a;if(c&&(e|=128,c!==a[0]))throw Error("mid");a:{c=a;e|=64;var f=c.length;if(f){var g=f-1;const k=c[g];if(k!=null&&typeof k==="object"&&k.constructor===Object){b=e&128?0:-1;g-=b;if(g>=1024)throw Error("pvtlmt");for(var h in k)f=
+h,f<g&&(c[f+b]=k[h],delete k[h]);e=e&-16760833|(g&1023)<<14;break a}}if(b){h=Math.max(b,f-(e&128?0:-1));if(h>1024)throw Error("spvt");e=e&-16760833|(h&1023)<<14}}}e|=64;d===0&&(e|=2048);B(a,e);return a}
function Fc(){if(Ra)throw Error("carr");rb(yb,5)}
;function Gc(a,b){if(typeof a!=="object")return a;if(Array.isArray(a)){var c=a[A]|0;a.length===0&&c&1?a=void 0:c&2||(!b||4096&c||16&c?a=Hc(a,c,!1,b&&!(c&16)):(Eb(a,34),c&4&&Object.freeze(a)));return a}if(a!=null&&a[zb]===Gb)return b=a.o,c=b[A]|0,Hb(a,c)?a:Ic(a,b,c)?Jc(a,b):Hc(b,c);if(a instanceof mb)return a}
function Jc(a,b,c){a=new a.constructor(b);c&&(a.h=Ib);a.i=Ib;return a}
function Hc(a,b,c,d){d!=null||(d=!!(34&b));a=wc(a,b,Gc,d);d=32;c&&(d|=2);b=b&16769217|d;B(a,b);return a}
function Kc(a){const b=a.o,c=b[A]|0;return Hb(a,c)?Ic(a,b,c)?Jc(a,b,!0):new a.constructor(Hc(b,c,!1)):a}
function Lc(a){if(a.h!==Ib)return!1;var b=a.o;b=Hc(b,b[A]|0);Eb(b,2048);a.o=b;a.h=void 0;a.i=void 0;return!0}
function Mc(a){if(!Lc(a)&&Hb(a,a.o[A]|0))throw Error();}
function Nc(a,b){b===void 0&&(b=a[A]|0);b&32&&!(b&4096)&&B(a,b|4096)}
function Ic(a,b,c){return c&2?!0:c&32&&!(c&4096)?(B(b,c|2),a.h=Ib,!0):!1}
;const Oc={};function F(a,b,c,d){Object.isExtensible(a);b=Pc(a.o,b,c);if(b!==null||d&&a.i!==Ib)return b}
function Pc(a,b,c,d){if(b===-1)return null;const e=b+(c?0:-1),f=a.length-1;let g,h;if(!(f<1+(c?0:-1))){if(e>=f)if(g=a[f],g!=null&&typeof g==="object"&&g.constructor===Object)c=g[b],h=!0;else if(e===f)c=g;else return;else c=a[e];if(d&&c!=null){d=d(c);if(d==null)return d;if(!Object.is(d,c))return h?g[b]=d:a[e]=d,d}return c}}
function Qc(a,b,c){Mc(a);const d=a.o;H(d,d[A]|0,b,c);return a}
function H(a,b,c,d,e){const f=c+(e?0:-1);var g=a.length-1;if(g>=1+(e?0:-1)&&f>=g){const h=a[g];if(h!=null&&typeof h==="object"&&h.constructor===Object)return h[c]=d,b}if(f<=g)return a[f]=d,b;if(d!==void 0){let h;g=((h=b)!=null?h:b=a[A]|0)>>14&1023||536870912;c>=g?d!=null&&(a[g+(e?0:-1)]={[c]:d}):a[f]=d}return b}
function Rc(a,b,c){a=Pc(a,b,c);return Array.isArray(a)?a:Cb}
function Sc(a,b){2&b&&(a|=2);return a|1}
function Tc(a){return!!(2&a)&&!!(4&a)||!!(256&a)}
function Uc(a){if(sb){var b;return(b=a[vb])!=null?b:a[vb]=new Map}if(vb in a)return a[vb];b=new Map;Object.defineProperty(a,vb,{value:b});return b}
function Vc(a,b,c,d){let e=a.get(d);if(e!=null)return e;e=0;for(let f=0;f<d.length;f++){const g=d[f];Pc(b,g)!=null&&(e!==0&&(c=H(b,c,e)),e=g)}a.set(d,e);return e}
function Wc(a,b,c){let d=a[A]|0;const e=d&128?Lb:void 0,f=Pc(a,c,e);let g;if(f!=null&&f[zb]===Gb){if(!Hb(f))return Lc(f),f.o;g=f.o}else Array.isArray(f)&&(g=f);if(g){const h=g[A]|0;h&2&&(g=Hc(g,h))}g=Dc(g,b,!0);g!==f&&H(a,d,c,g,e);return g}
function Xc(a,b,c,d,e){let f=!1;d=Pc(a,d,e,g=>{const h=pc(g,c,b);f=h!==g&&h!=null;return h});
if(d!=null)return f&&!Hb(d)&&Nc(a,b),d}
function Yc(a,b,c,d){let e=a.o,f=e[A]|0;b=Xc(e,f,b,c,d);if(b==null)return b;f=e[A]|0;if(!Hb(a,f)){const g=Kc(b);g!==b&&(Lc(a)&&(e=a.o,f=e[A]|0),b=g,f=H(e,f,c,b,d),Nc(e,f))}return b}
function Zc(a,b,c,d,e,f,g,h){var k=Hb(a,c);f=k?1:f;g=!!g||f===3;k=h&&!k;(f===2||k)&&Lc(a)&&(b=a.o,c=b[A]|0);h=Rc(b,e);var l=h===Cb?7:h[A]|0,n=Sc(l,c);if(a=!(4&n)){var p=h,q=c;const m=!!(2&n);m&&(q|=2);let u=!m,z=!0,y=0,G=0;for(;y<p.length;y++){const ia=pc(p[y],d,q);if(ia instanceof d){if(!m){const Ha=Hb(ia);u&&(u=!Ha);z&&(z=Ha)}p[G++]=ia}}G<y&&(p.length=G);n|=4;n=z?n&-4097:n|4096;n=u?n|8:n&-9}n!==l&&(B(h,n),2&n&&Object.freeze(h));if(k&&!(8&n||!h.length&&(f===1||(f!==4?0:2&n||!(16&n)&&32&c)))){Tc(n)&&
(h=Array.prototype.slice.call(h),n=$c(n,c),c=H(b,c,e,h));d=h;k=n;for(l=0;l<d.length;l++)p=d[l],n=Kc(p),p!==n&&(d[l]=n);k|=8;n=k=d.length?k|4096:k&-4097;B(h,n)}k=d=n;f===1||(f!==4?0:2&d||!(16&d)&&32&c)?Tc(d)||(d|=!h.length||a&&!(4096&d)||32&c&&!(4096&d||16&d)?2:256,d!==k&&B(h,d),Object.freeze(h)):(f===2&&Tc(d)&&(h=Array.prototype.slice.call(h),k=0,d=$c(d,c),c=H(b,c,e,h)),Tc(d)||(g||(d|=16),d!==k&&B(h,d)));2&d||!(4096&d||16&d)||Nc(b,c);return h}
function I(a,b,c,d){d!=null?oc(d,b):d=void 0;Qc(a,c,d);d&&!Hb(d)&&Nc(a.o);return a}
function $c(a,b){var c;2&b?c=a|2:c=a&-3;a=c;return a&-273}
function ad(a,b,c,d){var e=d;Mc(a);d=a.o;a=Zc(a,d,d[A]|0,c,b,2,!0);e=e!=null?oc(e,c):new c;a.push(e);b=c=a===Cb?7:a[A]|0;(e=Hb(e))?(c&=-9,a.length===1&&(c&=-4097)):c|=4096;c!==b&&B(a,c);e||Nc(d)}
function bd(a,b,c="",d){let e;return(e=E(F(a,b,d)))!=null?e:c}
function cd(a,b){var c=dd;const d=a.o;c=Vc(Uc(d),d,void 0,c);return E(F(a,c===b?b:-1,void 0,Oc))}
function ed(a,b,c){if(c!=null){if(typeof c!=="number")throw qb("int32");if(!fc(c))throw qb("int32");c|=0}Qc(a,b,c)}
function J(a,b,c){return Qc(a,b,nc(c))}
function fd(a,b,c,d){a:{d=nc(d);Mc(a);const g=a.o;var e=g[A]|0;if(d==null){var f=Uc(g);if(Vc(f,g,e,c)===b)f.set(c,0);else break a}else{f=g;b===0||c.includes(b);const h=Uc(f),k=Vc(h,f,e,c);k!==b&&(k&&(e=H(f,e,k)),h.set(c,b))}H(g,e,b,d)}return a}
function gd(a,b,c){if(c!=null){if(!fc(c))throw qb("enum");c|=0}return Qc(a,b,c)}
;var hd=class{constructor(a,b,c){this.buffer=a;if(c&&!b)throw Error();this.h=b}};
function id(a,b){if(typeof a==="string")return new hd(ib(a),b);if(Array.isArray(a))return new hd(new Uint8Array(a),b);if(a.constructor===Uint8Array)return new hd(a,!1);if(a.constructor===ArrayBuffer)return a=new Uint8Array(a),new hd(a,!1);if(a.constructor===mb)return b=nb(a)||new Uint8Array(0),new hd(b,!0,a);if(a instanceof Uint8Array)return a=a.constructor===Uint8Array?a:new Uint8Array(a.buffer,a.byteOffset,a.byteLength),new hd(a,!1);throw Error();}
;function jd(a){const b=a.i;let c=a.h,d=b[c++],e=d&127;if(d&128&&(d=b[c++],e|=(d&127)<<7,d&128&&(d=b[c++],e|=(d&127)<<14,d&128&&(d=b[c++],e|=(d&127)<<21,d&128&&(d=b[c++],e|=d<<28,d&128&&b[c++]&128&&b[c++]&128&&b[c++]&128&&b[c++]&128&&b[c++]&128)))))throw Error();kd(a,c);return e}
function kd(a,b){a.h=b;if(b>a.j)throw Error();}
function ld(a,b){if(b<0)throw Error();const c=a.h;b=c+b;if(b>a.j)throw Error();a.h=b;return c}
var md=class{constructor(a,b,c,d){this.i=null;this.m=!1;this.h=this.j=this.l=0;this.init(a,b,c,d)}init(a,b,c,{W:d=!1,da:e=!1}={}){this.W=d;this.da=e;a&&(a=id(a,this.da),this.i=a.buffer,this.m=a.h,this.l=b||0,this.j=c!==void 0?this.l+c:this.i.length,this.h=this.l)}clear(){this.i=null;this.m=!1;this.h=this.j=this.l=0;this.W=!1}reset(){this.h=this.l}},nd=[];function od(a,b,c,d){if(pd.length){const e=pd.pop();qd(e,d);e.h.init(a,b,c,d);return e}return new rd(a,b,c,d)}
function qd(a,{ha:b=!1}={}){a.ha=b}
function sd(a){a.h.clear();a.j=-1;a.i=-1;pd.length<100&&pd.push(a)}
function td(a){var b=a.h;if(b.h==b.j)return!1;a.l=a.h.h;var c=jd(a.h)>>>0;b=c>>>3;c&=7;if(!(c>=0&&c<=5))throw Error();if(b<1)throw Error();a.j=b;a.i=c;return!0}
function ud(a){switch(a.i){case 0:if(a.i!=0)ud(a);else a:{a=a.h;var b=a.h;const c=b+10,d=a.i;for(;b<c;)if((d[b++]&128)===0){kd(a,b);break a}throw Error();}break;case 1:a=a.h;kd(a,a.h+8);break;case 2:a.i!=2?ud(a):(b=jd(a.h)>>>0,a=a.h,kd(a,a.h+b));break;case 5:a=a.h;kd(a,a.h+4);break;case 3:b=a.j;do{if(!td(a))throw Error();if(a.i==4){if(a.j!=b)throw Error();break}ud(a)}while(1);break;default:throw Error();}}
function vd(a,b,c){const d=a.h.j;var e=jd(a.h)>>>0;e=a.h.h+e;let f=e-d;f<=0&&(a.h.j=e,c(b,a,void 0,void 0,void 0),f=e-a.h.h);if(f)throw Error();a.h.h=e;a.h.j=d}
var rd=class{constructor(a,b,c,d){if(nd.length){const e=nd.pop();e.init(a,b,c,d);a=e}else a=new md(a,b,c,d);this.h=a;this.l=this.h.h;this.i=this.j=-1;qd(this,d)}reset(){this.h.reset();this.l=this.h.h;this.i=this.j=-1}},pd=[];function wd(){const a=class{constructor(){throw Error();}};Object.setPrototypeOf(a,a.prototype);return a}
var xd=wd(),yd=wd();var K=class{constructor(a,b,c){this.o=Ec(a,b,c)}toJSON(){return zc(this)}clone(){const a=this.o,b=a[A]|0;return Ic(this,a,b)?Jc(this,a,!0):new this.constructor(Hc(a,b,!1))}};K.prototype[zb]=Gb;var zd=class{constructor(a,b){this.U=a;a=na(xd);this.h=!!a&&b===a||!1}};const Ad=new zd(function(a,b,c,d,e){if(a.i!==2)return!1;vd(a,Wc(b,d,c),e);return!0},xd),Bd=new zd(function(a,b,c,d,e){if(a.i!==2)return!1;
vd(a,Wc(b,d,c),e);return!0},xd);
var Cd=Symbol(),Dd=Symbol(),Ed=Symbol();let Fd,Gd;
function Hd(a){var b=Id,c=Jd,d=a[Cd];if(d)return d;d={};d.ta=a;d.aa=Cc(a[0]);var e=a[1];let f=1;e&&e.constructor===Object&&(d.extensions=e,e=a[++f],typeof e==="function"&&(d.Ea=!0,Fd!=null||(Fd=e),Gd!=null||(Gd=a[f+1]),e=a[f+=2]));const g={};for(;e&&Array.isArray(e)&&e.length&&typeof e[0]==="number"&&e[0]>0;){for(var h=0;h<e.length;h++)g[e[h]]=e;e=a[++f]}for(h=1;e!==void 0;){typeof e==="number"&&(h+=e,e=a[++f]);let n;var k=void 0;e instanceof zd?n=e:(n=Ad,f--);let p;if((p=n)==null?0:p.h){e=a[++f];
k=a;var l=f;typeof e==="function"&&(e=e(),k[l]=e);k=e}e=a[++f];l=h+1;typeof e==="number"&&e<0&&(l-=e,e=a[++f]);for(;h<l;h++){const q=g[h];k?c(d,h,n,k,q):b(d,h,n,q)}}return a[Cd]=d}
;function Id(a,b,c,d){const e=c.U;a[b]=d?(f,g,h)=>e(f,g,h,d):e}
function Jd(a,b,c,d,e){const f=c.U;let g,h;a[b]=(k,l,n)=>f(k,l,n,h||(h=Hd(d).aa),g||(g=Kd(d)),e)}
function Kd(a){let b=a[Dd];if(b!=null)return b;const c=Hd(a);b=c.Ea?(d,e)=>Fd(d,e,c):(d,e)=>{for(;td(e)&&e.i!=4;){var f=e.j,g=c[f];
if(g==null){var h=c.extensions;h&&(h=h[f])&&(h=Ld(h),h!=null&&(g=c[f]=h))}if(g==null||!g(e,d,f)){h=e;g=h.l;ud(h);if(h.ha)var k=void 0;else{var l=h.h.h-g;h.h.h=g;h=h.h;g=l;if(g==0)k=kb();else{var n=ld(h,g);h.W&&h.m?g=h.i.subarray(n,n+g):(h=h.i,l=n,g=n+g,g=l===g?new Uint8Array(0):Yb?h.slice(l,g):new Uint8Array(h.subarray(l,g)));k=g.length==0?kb():new mb(g,jb)}}l=h=g=void 0;n=d;k&&((g=(h=(l=n[wb])!=null?l:n[wb]=new uc)[f])!=null?g:h[f]=[]).push(k)}}if(d=rc(d))d.h=c.ta[Ed];return!0};
a[Dd]=b;a[Ed]=Md.bind(a);return b}
function Md(a,b,c,d){var e=this[Cd];const f=this[Dd],g=Dc(void 0,e.aa,!1),h=rc(a);if(h){var k=!1,l=e.extensions;if(l){e=(n,p,q)=>{if(q.length!==0)if(l[p])for(const m of q){n=od(m);try{k=!0,f(g,n)}finally{sd(n)}}else d==null||d(a,p,q)};
if(b==null)sc(h,e);else if(h!=null){const n=h[b];n&&e(h,b,n)}if(k){let n=a[A]|0;if(n&2&&n&2048&&(c==null||!c.Nb))throw Error();const p=n&128?Lb:void 0,q=(m,u)=>{if(Pc(a,m,p)!=null)switch(c==null?void 0:c.Lb){case 1:return;default:throw Error();}u!=null&&(n=H(a,n,m,u,p));delete h[m]};
b==null?Kb(g,g[A]|0,(m,u)=>{q(m,u)}):q(b,Pc(g,b,p))}}}}
function Ld(a){a=Array.isArray(a)?a[0]instanceof zd?a:[Bd,a]:[a,void 0];const b=a[0].U;if(a=a[1]){const c=Kd(a),d=Hd(a).aa;return(e,f,g)=>b(e,f,g,d,c)}return b}
;var Nd;
Nd=new zd(function(a,b,c){if(a.i!==2)return!1;var d=jd(a.h)>>>0;a=a.h;var e=ld(a,d);a=a.i;if(Oa){var f=a,g;(g=Na)||(g=Na=new TextDecoder("utf-8",{fatal:!0}));d=e+d;f=e===0&&d===f.length?f:f.subarray(e,d);try{var h=g.decode(f)}catch(l){if(Ma===void 0){try{g.decode(new Uint8Array([128]))}catch(n){}try{g.decode(new Uint8Array([97])),Ma=!0}catch(n){Ma=!1}}!Ma&&(Na=void 0);throw l;}}else{h=e;d=h+d;e=[];let l=null;let n;for(;h<d;){var k=a[h++];k<128?e.push(k):k<224?h>=d?Ka():(n=a[h++],k<194||(n&192)!==
128?(h--,Ka()):e.push((k&31)<<6|n&63)):k<240?h>=d-1?Ka():(n=a[h++],(n&192)!==128||k===224&&n<160||k===237&&n>=160||((g=a[h++])&192)!==128?(h--,Ka()):e.push((k&15)<<12|(n&63)<<6|g&63)):k<=244?h>=d-2?Ka():(n=a[h++],(n&192)!==128||(k<<28)+(n-144)>>30!==0||((g=a[h++])&192)!==128||((f=a[h++])&192)!==128?(h--,Ka()):(k=(k&7)<<18|(n&63)<<12|(g&63)<<6|f&63,k-=65536,e.push((k>>10&1023)+55296,(k&1023)+56320))):Ka();e.length>=8192&&(l=La(l,e),e.length=0)}h=La(l,e)}H(b,b[A]|0,c,h,(b[A]|0)&128?Lb:void 0);return!0},
yd);
var Od=function(a,b,c=xd){return new zd(a,c)}(function(a,b,c,d,e){if(a.i!==2)return!1;
d=Dc(void 0,d,!0);var f=b[A]|0;if(f&2)throw Error();const g=f&128?Lb:void 0;let h=Rc(b,c,g),k=h===Cb?7:h[A]|0,l=Sc(k,f);if(2&l||Tc(l)||16&l)l===k||Tc(l)||B(h,l),h=Array.prototype.slice.call(h),k=0,l=$c(l,f),H(b,f,c,h,g);l&=-13;l!==k&&B(h,l);h.push(d);vd(a,d,e);return!0},function(a,b,c,d,e){if(Array.isArray(b)){for(let l=0;l<b.length;l++){var f=e,g=a,h=g.h;
var k=b[l];k=k instanceof K?k.o:Array.isArray(k)?Dc(k,d,!1):void 0;h.call(g,c,k,f)}a=b[A]|0;a&1||B(b,a|1)}});function Pd(){}
;function Qd(a){for(const b in a)return!1;return!0}
function Rd(a){if(!a||typeof a!=="object")return a;if(typeof a.clone==="function")return a.clone();if(typeof Map!=="undefined"&&a instanceof Map)return new Map(a);if(typeof Set!=="undefined"&&a instanceof Set)return new Set(a);if(a instanceof Date)return new Date(a.getTime());const b=Array.isArray(a)?[]:typeof ArrayBuffer!=="function"||typeof ArrayBuffer.isView!=="function"||!ArrayBuffer.isView(a)||a instanceof DataView?{}:new a.constructor(a.length);for(const c in a)b[c]=Rd(a[c]);return b}
const Sd="constructor hasOwnProperty isPrototypeOf propertyIsEnumerable toLocaleString toString valueOf".split(" ");function Td(a,b){let c,d;for(let e=1;e<arguments.length;e++){d=arguments[e];for(c in d)a[c]=d[c];for(let f=0;f<Sd.length;f++)c=Sd[f],Object.prototype.hasOwnProperty.call(d,c)&&(a[c]=d[c])}}
;function Ud(a,b){this.h=a===Vd&&b||""}
Ud.prototype.toString=function(){return this.h};
var Vd={};new Ud(Vd,"");function Wd(a){if(!a)return"";if(/^about:(?:blank|srcdoc)$/.test(a))return window.origin||"";a.indexOf("blob:")===0&&(a=a.substring(5));a=a.split("#")[0].split("?")[0];a=a.toLowerCase();a.indexOf("//")==0&&(a=window.location.protocol+a);/^[\w\-]*:\/\//.test(a)||(a=window.location.href);var b=a.substring(a.indexOf("://")+3),c=b.indexOf("/");c!=-1&&(b=b.substring(0,c));c=a.substring(0,a.indexOf("://"));if(!c)throw Error("URI is missing protocol: "+a);if(c!=="http"&&c!=="https"&&c!=="chrome-extension"&&
c!=="moz-extension"&&c!=="file"&&c!=="android-app"&&c!=="chrome-search"&&c!=="chrome-untrusted"&&c!=="chrome"&&c!=="app"&&c!=="devtools")throw Error("Invalid URI scheme in origin: "+c);a="";var d=b.indexOf(":");if(d!=-1){var e=b.substring(d+1);b=b.substring(0,d);if(c==="http"&&e!=="80"||c==="https"&&e!=="443")a=":"+e}return c+"://"+b+a}
;function Xd(){function a(){e[0]=1732584193;e[1]=4023233417;e[2]=2562383102;e[3]=271733878;e[4]=3285377520;n=l=0}
function b(p){for(var q=g,m=0;m<64;m+=4)q[m/4]=p[m]<<24|p[m+1]<<16|p[m+2]<<8|p[m+3];for(m=16;m<80;m++)p=q[m-3]^q[m-8]^q[m-14]^q[m-16],q[m]=(p<<1|p>>>31)&4294967295;p=e[0];var u=e[1],z=e[2],y=e[3],G=e[4];for(m=0;m<80;m++){if(m<40)if(m<20){var ia=y^u&(z^y);var Ha=1518500249}else ia=u^z^y,Ha=1859775393;else m<60?(ia=u&z|y&(u|z),Ha=2400959708):(ia=u^z^y,Ha=3395469782);ia=((p<<5|p>>>27)&4294967295)+ia+G+Ha+q[m]&4294967295;G=y;y=z;z=(u<<30|u>>>2)&4294967295;u=p;p=ia}e[0]=e[0]+p&4294967295;e[1]=e[1]+u&4294967295;
e[2]=e[2]+z&4294967295;e[3]=e[3]+y&4294967295;e[4]=e[4]+G&4294967295}
function c(p,q){if(typeof p==="string"){p=unescape(encodeURIComponent(p));for(var m=[],u=0,z=p.length;u<z;++u)m.push(p.charCodeAt(u));p=m}q||(q=p.length);m=0;if(l==0)for(;m+64<q;)b(p.slice(m,m+64)),m+=64,n+=64;for(;m<q;)if(f[l++]=p[m++],n++,l==64)for(l=0,b(f);m+64<q;)b(p.slice(m,m+64)),m+=64,n+=64}
function d(){var p=[],q=n*8;l<56?c(h,56-l):c(h,64-(l-56));for(var m=63;m>=56;m--)f[m]=q&255,q>>>=8;b(f);for(m=q=0;m<5;m++)for(var u=24;u>=0;u-=8)p[q++]=e[m]>>u&255;return p}
for(var e=[],f=[],g=[],h=[128],k=1;k<64;++k)h[k]=0;var l,n;a();return{reset:a,update:c,digest:d,xa:function(){for(var p=d(),q="",m=0;m<p.length;m++)q+="0123456789ABCDEF".charAt(Math.floor(p[m]/16))+"0123456789ABCDEF".charAt(p[m]%16);return q}}}
;function Yd(a,b,c){var d=String(t.location.href);return d&&a&&b?[b,Zd(Wd(d),a,c||null)].join(" "):null}
function Zd(a,b,c){var d=[];let e=[];if((Array.isArray(c)?2:1)==1)return e=[b,a],wa(d,function(h){e.push(h)}),$d(e.join(" "));
const f=[],g=[];wa(c,function(h){g.push(h.key);f.push(h.value)});
c=Math.floor((new Date).getTime()/1E3);e=f.length==0?[c,b,a]:[f.join(":"),c,b,a];wa(d,function(h){e.push(h)});
a=$d(e.join(" "));a=[c,a];g.length==0||a.push(g.join(""));return a.join("_")}
function $d(a){const b=Xd();b.update(a);return b.xa().toLowerCase()}
;function ae(){this.h=document||{cookie:""}}
ae.prototype.isEnabled=function(){if(!t.navigator.cookieEnabled)return!1;if(this.h.cookie)return!0;this.set("TESTCOOKIESENABLED","1",{maxAge:60});if(this.get("TESTCOOKIESENABLED")!=="1")return!1;this.remove("TESTCOOKIESENABLED");return!0};
ae.prototype.set=function(a,b,c){let d,e,f,g=!1,h;typeof c==="object"&&(h=c.sameSite,g=c.secure||!1,f=c.domain||void 0,e=c.path||void 0,d=c.maxAge);if(/[;=\s]/.test(a))throw Error('Invalid cookie name "'+a+'"');if(/[;\r\n]/.test(b))throw Error('Invalid cookie value "'+b+'"');d===void 0&&(d=-1);this.h.cookie=a+"="+b+(f?";domain="+f:"")+(e?";path="+e:"")+(d<0?"":d==0?";expires="+(new Date(1970,1,1)).toUTCString():";expires="+(new Date(Date.now()+d*1E3)).toUTCString())+(g?";secure":"")+(h!=null?";samesite="+
h:"")};
ae.prototype.get=function(a,b){const c=a+"=",d=(this.h.cookie||"").split(";");for(let e=0,f;e<d.length;e++){f=qa(d[e]);if(f.lastIndexOf(c,0)==0)return f.slice(c.length);if(f==a)return""}return b};
ae.prototype.remove=function(a,b,c){const d=this.get(a)!==void 0;this.set(a,"",{maxAge:0,path:b,domain:c});return d};
ae.prototype.clear=function(){var a=(this.h.cookie||"").split(";");const b=[],c=[];let d,e;for(let f=0;f<a.length;f++)e=qa(a[f]),d=e.indexOf("="),d==-1?(b.push(""),c.push(e)):(b.push(e.substring(0,d)),c.push(e.substring(d+1)));for(a=b.length-1;a>=0;a--)this.remove(b[a])};function be(a,b,c,d){(a=t[a])||typeof document==="undefined"||(a=(new ae).get(b));return a?Yd(a,c,d):null}
;var ce=typeof AsyncContext!=="undefined"&&typeof AsyncContext.Snapshot==="function"?a=>a&&AsyncContext.Snapshot.wrap(a):a=>a;function de(){this.l=this.l;this.i=this.i}
de.prototype.l=!1;de.prototype.dispose=function(){this.l||(this.l=!0,this.m())};
de.prototype[Symbol.dispose]=function(){this.dispose()};
de.prototype.addOnDisposeCallback=function(a,b){this.l?b!==void 0?a.call(b):a():(this.i||(this.i=[]),b&&(a=a.bind(b)),this.i.push(a))};
de.prototype.m=function(){if(this.i)for(;this.i.length;)this.i.shift()()};function ee(a,b){a.l(b);a.i<100&&(a.i++,b.next=a.h,a.h=b)}
class fe{constructor(a,b){this.j=a;this.l=b;this.i=0;this.h=null}get(){let a;this.i>0?(this.i--,a=this.h,this.h=a.next,a.next=null):a=this.j();return a}};class ge{constructor(){this.i=this.h=null}add(a,b){const c=he.get();c.set(a,b);this.i?this.i.next=c:this.h=c;this.i=c}remove(){let a=null;this.h&&(a=this.h,this.h=this.h.next,this.h||(this.i=null),a.next=null);return a}}var he=new fe(()=>new ie,a=>a.reset());
class ie{constructor(){this.next=this.scope=this.h=null}set(a,b){this.h=a;this.scope=b;this.next=null}reset(){this.next=this.scope=this.h=null}};let je,ke=!1,le=new ge,ne=(a,b)=>{je||me();ke||(je(),ke=!0);le.add(a,b)},me=()=>{const a=Promise.resolve(void 0);
je=()=>{a.then(oe)}};
function oe(){let a;for(;a=le.remove();){try{a.h.call(a.scope)}catch(b){Pa(b)}ee(he,a)}ke=!1}
;function L(a){this.h=0;this.A=void 0;this.l=this.i=this.j=null;this.m=this.u=!1;if(a!=Pd)try{const b=this;a.call(void 0,function(c){pe(b,2,c)},function(c){pe(b,3,c)})}catch(b){pe(this,3,b)}}
function qe(){this.next=this.context=this.i=this.j=this.h=null;this.l=!1}
qe.prototype.reset=function(){this.context=this.i=this.j=this.h=null;this.l=!1};
var re=new fe(function(){return new qe},function(a){a.reset()});
function se(a,b,c){const d=re.get();d.j=a;d.i=b;d.context=c;return d}
function te(a){if(a instanceof L)return a;const b=new L(Pd);pe(b,2,a);return b}
L.prototype.then=function(a,b,c){return ue(this,ce(typeof a==="function"?a:null),ce(typeof b==="function"?b:null),c)};
L.prototype.$goog_Thenable=!0;L.prototype.B=function(a,b){return ue(this,null,ce(a),b)};
L.prototype.catch=L.prototype.B;L.prototype.cancel=function(a){if(this.h==0){const b=new ve(a);ne(function(){we(this,b)},this)}};
function we(a,b){if(a.h==0)if(a.j){var c=a.j;if(c.i){var d=0,e=null,f=null;for(let g=c.i;g&&(g.l||(d++,g.h==a&&(e=g),!(e&&d>1)));g=g.next)e||(f=g);e&&(c.h==0&&d==1?we(c,b):(f?(d=f,d.next==c.l&&(c.l=d),d.next=d.next.next):xe(c),ye(c,e,3,b)))}a.j=null}else pe(a,3,b)}
function ze(a,b){a.i||a.h!=2&&a.h!=3||Ae(a);a.l?a.l.next=b:a.i=b;a.l=b}
function ue(a,b,c,d){const e=se(null,null,null);e.h=new L(function(f,g){e.j=b?function(h){try{const k=b.call(d,h);f(k)}catch(k){g(k)}}:f;
e.i=c?function(h){try{const k=c.call(d,h);k===void 0&&h instanceof ve?g(h):f(k)}catch(k){g(k)}}:g});
e.h.j=a;ze(a,e);return e.h}
L.prototype.I=function(a){this.h=0;pe(this,2,a)};
L.prototype.K=function(a){this.h=0;pe(this,3,a)};
function pe(a,b,c){if(a.h==0){a===c&&(b=3,c=new TypeError("Promise cannot resolve to itself"));a.h=1;a:{var d=c,e=a.I,f=a.K;if(d instanceof L){ze(d,se(e||Pd,f||null,a));var g=!0}else{if(d)try{var h=!!d.$goog_Thenable}catch(k){h=!1}else h=!1;if(h)d.then(e,f,a),g=!0;else{h=typeof d;if(h=="object"&&d!=null||h=="function")try{const k=d.then;if(typeof k==="function"){Be(d,k,e,f,a);g=!0;break a}}catch(k){f.call(a,k);g=!0;break a}g=!1}}}g||(a.A=c,a.h=b,a.j=null,Ae(a),b!=3||c instanceof ve||Ce(a,c))}}
function Be(a,b,c,d,e){function f(k){h||(h=!0,d.call(e,k))}
function g(k){h||(h=!0,c.call(e,k))}
let h=!1;try{b.call(a,g,f)}catch(k){f(k)}}
function Ae(a){a.u||(a.u=!0,ne(a.D,a))}
function xe(a){let b=null;a.i&&(b=a.i,a.i=b.next,b.next=null);a.i||(a.l=null);return b}
L.prototype.D=function(){let a;for(;a=xe(this);)ye(this,a,this.h,this.A);this.u=!1};
function ye(a,b,c,d){if(c==3&&b.i&&!b.l)for(;a&&a.m;a=a.j)a.m=!1;if(b.h)b.h.j=null,De(b,c,d);else try{b.l?b.j.call(b.context):De(b,c,d)}catch(e){Ee.call(null,e)}ee(re,b)}
function De(a,b,c){b==2?a.j.call(a.context,c):a.i&&a.i.call(a.context,c)}
function Ce(a,b){a.m=!0;ne(function(){a.m&&Ee.call(null,b)})}
var Ee=Pa;function ve(a){pa.call(this,a)}
oa(ve,pa);ve.prototype.name="cancel";const Fe=self;class Ge{constructor(){this.promise=new Promise((a,b)=>{this.resolve=a;this.reject=b})}}
;function M(a){de.call(this);this.I=1;this.u=[];this.A=0;this.h=[];this.j={};this.V=!!a}
oa(M,de);M.prototype.K=function(a,b,c){let d=this.j[a];d||(d=this.j[a]=[]);const e=this.I;this.h[e]=a;this.h[e+1]=b;this.h[e+2]=c;this.I=e+3;d.push(e);return e};
M.prototype.B=function(a){const b=this.h[a];if(b){const c=this.j[b];this.A!=0?(this.u.push(a),this.h[a+1]=()=>{}):(c&&ya(c,a),delete this.h[a],delete this.h[a+1],delete this.h[a+2])}return!!b};
M.prototype.D=function(a,b){var c=this.j[a];if(c){const e=Array(arguments.length-1);var d=arguments.length;let f;for(f=1;f<d;f++)e[f-1]=arguments[f];if(this.V)for(f=0;f<c.length;f++)d=c[f],He(this.h[d+1],this.h[d+2],e);else{this.A++;try{for(f=0,d=c.length;f<d&&!this.l;f++){const g=c[f];this.h[g+1].apply(this.h[g+2],e)}}finally{if(this.A--,this.u.length>0&&this.A==0)for(;c=this.u.pop();)this.B(c)}}return f!=0}return!1};
function He(a,b,c){ne(function(){a.apply(b,c)})}
M.prototype.clear=function(a){if(a){const b=this.j[a];b&&(b.forEach(this.B,this),delete this.j[a])}else this.h.length=0,this.j={}};
M.prototype.m=function(){M.Ja.m.call(this);this.clear();this.u.length=0};/*

 (The MIT License)

 Copyright (C) 2014 by Vitaly Puzrin

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.

 -----------------------------------------------------------------------------
 Ported from zlib, which is under the following license
 https://github.com/madler/zlib/blob/master/zlib.h

 zlib.h -- interface of the 'zlib' general purpose compression library
   version 1.2.8, April 28th, 2013
   Copyright (C) 1995-2013 Jean-loup Gailly and Mark Adler
   This software is provided 'as-is', without any express or implied
   warranty.  In no event will the authors be held liable for any damages
   arising from the use of this software.
   Permission is granted to anyone to use this software for any purpose,
   including commercial applications, and to alter it and redistribute it
   freely, subject to the following restrictions:
   1. The origin of this software must not be misrepresented; you must not
      claim that you wrote the original software. If you use this software
      in a product, an acknowledgment in the product documentation would be
      appreciated but is not required.
   2. Altered source versions must be plainly marked as such, and must not be
      misrepresented as being the original software.
   3. This notice may not be removed or altered from any source distribution.
   Jean-loup Gailly        Mark Adler
   jloup@gzip.org          madler@alumni.caltech.edu
   The data format used by the zlib library is described by RFCs (Request for
   Comments) 1950 to 1952 in the files http://tools.ietf.org/html/rfc1950
   (zlib format), rfc1951 (deflate format) and rfc1952 (gzip format).
*/
let N={};var Ie=typeof Uint8Array!=="undefined"&&typeof Uint16Array!=="undefined"&&typeof Int32Array!=="undefined";N.assign=function(a){for(var b=Array.prototype.slice.call(arguments,1);b.length;){var c=b.shift();if(c){if(typeof c!=="object")throw new TypeError(c+"must be non-object");for(var d in c)Object.prototype.hasOwnProperty.call(c,d)&&(a[d]=c[d])}}return a};
N.Rb=function(a,b){if(a.length===b)return a;if(a.subarray)return a.subarray(0,b);a.length=b;return a};
var Je={sa:function(a,b,c,d,e){if(b.subarray&&a.subarray)a.set(b.subarray(c,c+d),e);else for(var f=0;f<d;f++)a[e+f]=b[c+f]},
ya:function(a){var b,c;var d=c=0;for(b=a.length;d<b;d++)c+=a[d].length;var e=new Uint8Array(c);d=c=0;for(b=a.length;d<b;d++){var f=a[d];e.set(f,c);c+=f.length}return e}},Ke={sa:function(a,b,c,d,e){for(var f=0;f<d;f++)a[e+f]=b[c+f]},
ya:function(a){return[].concat.apply([],a)}};
N.Ia=function(){Ie?(N.oa=Uint8Array,N.ma=Uint16Array,N.na=Int32Array,N.assign(N,Je)):(N.oa=Array,N.ma=Array,N.na=Array,N.assign(N,Ke))};
N.Ia();try{new Uint8Array(1)}catch(a){};function Le(a){for(var b=a.length;--b>=0;)a[b]=0}
Le(Array(576));Le(Array(60));Le(Array(512));Le(Array(256));Le(Array(29));Le(Array(30));/*

Math.uuid.js (v1.4)
http://www.broofa.com
mailto:robert@broofa.com
Copyright (c) 2010 Robert Kieffer
Dual licensed under the MIT and GPL licenses.
*/
var Me="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".split("");var Ne=class{constructor(a){this.name=a}};var Oe=new Ne("rawColdConfigGroup");var Pe=new Ne("rawHotConfigGroup");var Qe=class extends K{constructor(a){super(a)}};var Re=class extends K{constructor(a){super(a)}};var Se=class extends K{constructor(a){super(a)}};var Te=class extends K{constructor(a){super(a)}getPlayerType(){var a=F(this,36);a=a==null?a:fc(a)?a|0:void 0;return a!=null?a:0}setHomeGroupInfo(a){return I(this,Se,81,a)}clearLocationPlayabilityToken(){return Qc(this,89)}};var Ue=class extends K{constructor(a){super(a)}},Ve=[2,3,4,5,6];var We=class extends K{constructor(a){super(a)}setTrackingParams(a){if(a!=null)if(typeof a==="string")a=a?new mb(a,jb):kb();else if(a.constructor!==mb)if(db&&a!=null&&a instanceof Uint8Array)a=a.length?new mb(new Uint8Array(a),jb):kb();else throw Error();return Qc(this,1,a)}};var Xe=class extends K{constructor(a){super(a)}};var Ye=class extends K{constructor(a){super(a)}};var Ze=class extends K{constructor(a){super(a)}setToken(a){return J(this,2,a)}};var $e=class extends K{constructor(a){super(a)}setSafetyMode(a){return gd(this,5,a)}};var af=class extends K{constructor(a){super(a)}j(a){return I(this,Te,1,a)}};var bf=class extends K{constructor(a){super(a,500)}};var cf=class extends K{constructor(a){super(a)}};var df=class extends K{constructor(a){super(a)}setVideoId(a){return fd(this,1,dd,a)}getPlaylistId(){return cd(this,2)}},dd=[1,2];var ef=class extends K{constructor(a){super(a)}};var ff=new Ne("recordNotificationInteractionsEndpoint");var gf=["notification/convert_endpoint_to_url"],hf=["notification/record_interactions"],jf=["notification_registration/set_registration"];var kf=a=>self.btoa(String.fromCharCode.apply(null,Array.from(new Uint8Array(a)))).replace(/\+/g,"-").replace(/\//g,"_");var lf=["notifications_register","notifications_check_registration"];var O=class extends Error{constructor(a,...b){super(a);this.args=[...b];Object.setPrototypeOf(this,new.target.prototype)}};let mf=null;function P(a,b){const c={};c.key=a;c.value=b;return nf().then(d=>new Promise((e,f)=>{try{const g=d.transaction("swpushnotificationsstore","readwrite").objectStore("swpushnotificationsstore").put(c);g.onsuccess=()=>{e()};
g.onerror=()=>{f()}}catch(g){f(g)}}))}
function of(){return P("IndexedDBCheck","testing IndexedDB").then(()=>pf("IndexedDBCheck")).then(a=>a==="testing IndexedDB"?Promise.resolve():Promise.reject()).then(()=>!0).catch(()=>!1)}
function pf(a){const b=new O("Error accessing DB");return nf().then(c=>new Promise((d,e)=>{try{const f=c.transaction("swpushnotificationsstore").objectStore("swpushnotificationsstore").get(a);f.onsuccess=()=>{const g=f.result;d(g?g.value:null)};
f.onerror=()=>{b.params={key:a,source:"onerror"};e(b)}}catch(f){b.params={key:a,
thrownError:String(f)},e(b)}}),()=>null)}
function nf(){return mf?Promise.resolve(mf):new Promise((a,b)=>{const c=self.indexedDB.open("swpushnotificationsdb");c.onerror=b;c.onsuccess=()=>{const d=c.result;if(d.objectStoreNames.contains("swpushnotificationsstore"))mf=d,a(mf);else return self.indexedDB.deleteDatabase("swpushnotificationsdb"),nf()};
c.onupgradeneeded=qf})}
function qf(a){a=a.target.result;a.objectStoreNames.contains("swpushnotificationsstore")&&a.deleteObjectStore("swpushnotificationsstore");a.createObjectStore("swpushnotificationsstore",{keyPath:"key"})}
;const rf={WEB_UNPLUGGED:"^unplugged/",WEB_UNPLUGGED_ONBOARDING:"^unplugged/",WEB_UNPLUGGED_OPS:"^unplugged/",WEB_UNPLUGGED_PUBLIC:"^unplugged/",WEB_CREATOR:"^creator/",WEB_KIDS:"^kids/",WEB_EXPERIMENTS:"^experiments/",WEB_MUSIC:"^music/",WEB_REMIX:"^music/",WEB_MUSIC_EMBEDDED_PLAYER:"^music/",WEB_MUSIC_EMBEDDED_PLAYER:"^main_app/|^sfv/"};
function sf(a){if(a.length===1)return a[0];var b=rf.UNKNOWN_INTERFACE;if(b){b=new RegExp(b);for(var c of a)if(b.exec(c))return c}const d=[];Object.entries(rf).forEach(([e,f])=>{"UNKNOWN_INTERFACE"!==e&&d.push(f)});
c=new RegExp(d.join("|"));a.sort((e,f)=>e.length-f.length);
for(const e of a)if(!c.exec(e))return e;return a[0]}
function tf(a){return`/youtubei/v1/${sf(a)}`}
;var uf=class extends K{constructor(a){super(a)}};var vf=class extends K{constructor(a){super(a,0,"yt.sw.adr")}};const wf=t.window;let xf,yf;const zf=(wf==null?void 0:(xf=wf.yt)==null?void 0:xf.config_)||(wf==null?void 0:(yf=wf.ytcfg)==null?void 0:yf.data_)||{};w("yt.config_",zf);function Q(...a){a=arguments;a.length>1?zf[a[0]]=a[1]:a.length===1&&Object.assign(zf,a[0])}
function R(a,b){return a in zf?zf[a]:b}
;const Af=[];function Bf(a){Af.forEach(b=>b(a))}
function Cf(a){return a&&window.yterr?function(){try{return a.apply(this,arguments)}catch(b){Df(b)}}:a}
function Df(a){var b=v("yt.logging.errors.log");b?b(a,"ERROR",void 0,void 0,void 0,void 0,void 0):(b=R("ERRORS",[]),b.push([a,"ERROR",void 0,void 0,void 0,void 0,void 0]),Q("ERRORS",b));Bf(a)}
function Ef(a){var b=v("yt.logging.errors.log");b?b(a,"WARNING",void 0,void 0,void 0,void 0,void 0):(b=R("ERRORS",[]),b.push([a,"WARNING",void 0,void 0,void 0,void 0,void 0]),Q("ERRORS",b))}
;const Ff=/^[\w.]*$/,Gf={q:!0,search_query:!0};function Hf(a,b){b=a.split(b);const c={};for(let f=0,g=b.length;f<g;f++){const h=b[f].split("=");if(h.length===1&&h[0]||h.length===2)try{const k=If(h[0]||""),l=If(h[1]||"");if(k in c){const n=c[k];Array.isArray(n)?za(n,l):c[k]=[n,l]}else c[k]=l}catch(k){var d=k,e=h[0];const l=String(Hf);d.args=[{key:e,value:h[1],query:a,method:Jf===l?"unchanged":l}];Gf.hasOwnProperty(e)||Ef(d)}}return c}
const Jf=String(Hf);function Kf(a){a.charAt(0)==="?"&&(a=a.substring(1));return Hf(a,"&")}
function Lf(a,b){return Mf(a,b||{},!0)}
function Mf(a,b,c){var d=a.split("#",2);a=d[0];d=d.length>1?"#"+d[1]:"";var e=a.split("?",2);a=e[0];e=Kf(e[1]||"");for(var f in b)!c&&e!==null&&f in e||(e[f]=b[f]);b=a;a=Ja(e);a?(c=b.indexOf("#"),c<0&&(c=b.length),f=b.indexOf("?"),f<0||f>c?(f=c,e=""):e=b.substring(f+1,c),b=[b.slice(0,f),e,b.slice(c)],c=b[1],b[1]=a?c?c+"&"+a:a:c,a=b[0]+(b[1]?"?"+b[1]:"")+b[2]):a=b;return a+d}
function Nf(a){if(!b)var b=window.location.href;const c=a.match(Fa)[1]||null,d=Ga(a.match(Fa)[3]||null);c&&d?(a=a.match(Fa),b=b.match(Fa),a=a[3]==b[3]&&a[1]==b[1]&&a[4]==b[4]):a=d?Ga(b.match(Fa)[3]||null)===d&&(Number(b.match(Fa)[4]||null)||null)===(Number(a.match(Fa)[4]||null)||null):!0;return a}
function If(a){return a&&a.match(Ff)?a:decodeURIComponent(a.replace(/\+/g," "))}
;function Of(a,b){typeof a==="function"&&(a=Cf(a));return window.setTimeout(a,b)}
;var Pf="client_dev_domain client_dev_expflag client_dev_regex_map client_dev_root_url client_rollout_override expflag forcedCapability jsfeat jsmode mods".split(" "),Qf=[...Pf,"client_dev_set_cookie"];function S(a){a=Rf(a);return typeof a==="string"&&a==="false"?!1:!!a}
function T(a,b){a=Rf(a);return a===void 0&&b!==void 0?b:Number(a||0)}
function Sf(){return R("EXPERIMENTS_TOKEN","")}
function Rf(a){return R("EXPERIMENT_FLAGS",{})[a]}
function Tf(){const a=[],b=R("EXPERIMENTS_FORCED_FLAGS",{});for(var c of Object.keys(b))a.push({key:c,value:String(b[c])});c=R("EXPERIMENT_FLAGS",{});for(const d of Object.keys(c))d.startsWith("force_")&&b[d]===void 0&&a.push({key:d,value:String(c[d])});return a}
;[...Pf];let Uf=!1;function Vf(a,b){const c={method:b.method||"GET",credentials:"same-origin"};b.headers&&(c.headers=b.headers);b.priority&&(c.priority=b.priority);a=Wf(a,b);const d=Xf(a,b);d&&(c.body=d);b.withCredentials&&(c.credentials="include");const e=b.context||t;let f=!1,g;fetch(a,c).then(h=>{if(!f){f=!0;g&&window.clearTimeout(g);var k=h.ok,l=n=>{n=n||{};k?b.onSuccess&&b.onSuccess.call(e,n,h):b.onError&&b.onError.call(e,n,h);b.onFinish&&b.onFinish.call(e,n,h)};
(b.format||"JSON")==="JSON"&&(k||h.status>=400&&h.status<500)?h.json().then(l,()=>{l(null)}):l(null)}}).catch(()=>{b.onError&&b.onError.call(e,{},{})});
a=b.timeout||0;b.onFetchTimeout&&a>0&&(g=Of(()=>{f||(f=!0,window.clearTimeout(g),b.onFetchTimeout.call(b.context||t))},a))}
function Wf(a,b){b.includeDomain&&(a=document.location.protocol+"//"+document.location.hostname+(document.location.port?":"+document.location.port:"")+a);const c=R("XSRF_FIELD_NAME");if(b=b.urlParams)b[c]&&delete b[c],a=Lf(a,b);return a}
function Xf(a,b){const c=R("XSRF_FIELD_NAME"),d=R("XSRF_TOKEN");var e=b.postBody||"",f=b.postParams;const g=R("XSRF_FIELD_NAME");let h;b.headers&&(h=b.headers["Content-Type"]);b.excludeXsrf||Ga(a.match(Fa)[3]||null)&&!b.withCredentials&&Ga(a.match(Fa)[3]||null)!==document.location.hostname||b.method!=="POST"||h&&h!=="application/x-www-form-urlencoded"||b.postParams&&b.postParams[g]||(f||(f={}),f[c]=d);(S("ajax_parse_query_data_only_when_filled")&&f&&Object.keys(f).length>0||f)&&typeof e==="string"&&
(e=Kf(e),Td(e,f),e=b.postBodyFormat&&b.postBodyFormat==="JSON"?JSON.stringify(e):Ja(e));f=e||f&&!Qd(f);!Uf&&f&&b.method!=="POST"&&(Uf=!0,Df(Error("AJAX request with postData should use POST")));return e}
;const Yf=[{Z:a=>`Cannot read property '${a.key}'`,
S:{Error:[{v:/(Permission denied) to access property "([^']+)"/,groups:["reason","key"]}],TypeError:[{v:/Cannot read property '([^']+)' of (null|undefined)/,groups:["key","value"]},{v:/\u65e0\u6cd5\u83b7\u53d6\u672a\u5b9a\u4e49\u6216 (null|undefined) \u5f15\u7528\u7684\u5c5e\u6027\u201c([^\u201d]+)\u201d/,groups:["value","key"]},{v:/\uc815\uc758\ub418\uc9c0 \uc54a\uc74c \ub610\ub294 (null|undefined) \ucc38\uc870\uc778 '([^']+)' \uc18d\uc131\uc744 \uac00\uc838\uc62c \uc218 \uc5c6\uc2b5\ub2c8\ub2e4./,
groups:["value","key"]},{v:/No se puede obtener la propiedad '([^']+)' de referencia nula o sin definir/,groups:["key"]},{v:/Unable to get property '([^']+)' of (undefined or null) reference/,groups:["key","value"]},{v:/(null) is not an object \(evaluating '(?:([^.]+)\.)?([^']+)'\)/,groups:["value","base","key"]}]}},{Z:a=>`Cannot call '${a.key}'`,
S:{TypeError:[{v:/(?:([^ ]+)?\.)?([^ ]+) is not a function/,groups:["base","key"]},{v:/([^ ]+) called on (null or undefined)/,groups:["key","value"]},{v:/Object (.*) has no method '([^ ]+)'/,groups:["base","key"]},{v:/Object doesn't support property or method '([^ ]+)'/,groups:["key"]},{v:/\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u306f '([^']+)' \u30d7\u30ed\u30d1\u30c6\u30a3\u307e\u305f\u306f\u30e1\u30bd\u30c3\u30c9\u3092\u30b5\u30dd\u30fc\u30c8\u3057\u3066\u3044\u307e\u305b\u3093/,groups:["key"]},{v:/\uac1c\uccb4\uac00 '([^']+)' \uc18d\uc131\uc774\ub098 \uba54\uc11c\ub4dc\ub97c \uc9c0\uc6d0\ud558\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4./,
groups:["key"]}]}},{Z:a=>`${a.key} is not defined`,
S:{ReferenceError:[{v:/(.*) is not defined/,groups:["key"]},{v:/Can't find variable: (.*)/,groups:["key"]}]}}];var ag={G:[],F:[{callback:Zf,weight:500},{callback:$f,weight:500}]};function Zf(a){if(a.name==="JavaException")return!0;a=a.stack;return a.includes("chrome://")||a.includes("-extension://")||a.includes("webkit-masked-url://")}
function $f(a){if(!a.stack)return!0;const b=!a.stack.includes("\n");return b&&a.stack.includes("ErrorType: ")||b&&a.stack.includes("Anonymous function (Unknown script")||a.stack.toLowerCase()==="not available"||a.fileName==="user-script"||a.fileName.startsWith("user-script:")?!0:!1}
;function bg(){if(!cg){var a=cg=new dg;a.G.length=0;a.F.length=0;eg(a,ag)}return cg}
function eg(a,b){b.G&&a.G.push.apply(a.G,b.G);b.F&&a.F.push.apply(a.F,b.F)}
var dg=class{constructor(){this.F=[];this.G=[]}},cg;const fg=new M;function gg(a){const b=a.length;let c=0;const d=()=>a.charCodeAt(c++);
do{var e=hg(d);if(e===Infinity)break;const f=e>>3;switch(e&7){case 0:e=hg(d);if(f===2)return e;break;case 1:if(f===2)return;c+=8;break;case 2:e=hg(d);if(f===2)return a.substr(c,e);c+=e;break;case 5:if(f===2)return;c+=4;break;default:return}}while(c<b)}
function hg(a){let b=a(),c=b&127;if(b<128)return c;b=a();c|=(b&127)<<7;if(b<128)return c;b=a();c|=(b&127)<<14;if(b<128)return c;b=a();return b<128?c|(b&127)<<21:Infinity}
;function ig(a,b,c,d){if(a)if(Array.isArray(a)){var e=d;for(d=0;d<a.length&&!(a[d]&&(e+=jg(d,a[d],b,c),e>500));d++);d=e}else if(typeof a==="object")for(e in a){if(a[e]){var f=e;var g=a[e],h=b,k=c;f=typeof g!=="string"||f!=="clickTrackingParams"&&f!=="trackingParams"?0:(g=gg(atob(g.replace(/-/g,"+").replace(/_/g,"/"))))?jg(`${f}.ve`,g,h,k):0;d+=f;d+=jg(e,a[e],b,c);if(d>500)break}}else c[b]=kg(a),d+=c[b].length;else c[b]=kg(a),d+=c[b].length;return d}
function jg(a,b,c,d){c+=`.${a}`;a=kg(b);d[c]=a;return c.length+a.length}
function kg(a){try{return(typeof a==="string"?a:String(JSON.stringify(a))).substr(0,500)}catch(b){return`unable to serialize ${typeof a} (${b.message})`}}
;function lg(){mg.instance||(mg.instance=new mg);return mg.instance}
function ng(a,b){a={};var c=[];"USER_SESSION_ID"in zf&&c.push({key:"u",value:R("USER_SESSION_ID")});var d=Wd(t==null?void 0:t.location.href);var e=[];var f;(f=t.__SAPISID||t.__APISID||t.__3PSAPISID||t.__1PSAPISID||t.__OVERRIDE_SID)?f=!0:(typeof document!=="undefined"&&(f=new ae,f=f.get("SAPISID")||f.get("APISID")||f.get("__Secure-3PAPISID")||f.get("__Secure-1PAPISID")),f=!!f);f&&(f=(d=d.indexOf("https:")==0||d.indexOf("chrome-extension:")==0||d.indexOf("chrome-untrusted://new-tab-page")==0||d.indexOf("moz-extension:")==
0)?t.__SAPISID:t.__APISID,f||typeof document==="undefined"||(f=new ae,f=f.get(d?"SAPISID":"APISID")||f.get("__Secure-3PAPISID")),(f=f?Yd(f,d?"SAPISIDHASH":"APISIDHASH",c):null)&&e.push(f),d&&((d=be("__1PSAPISID","__Secure-1PAPISID","SAPISID1PHASH",c))&&e.push(d),(c=be("__3PSAPISID","__Secure-3PAPISID","SAPISID3PHASH",c))&&e.push(c)));if(e=e.length==0?null:e.join(" "))a.Authorization=e,e=b=b==null?void 0:b.sessionIndex,e===void 0&&(e=Number(R("SESSION_INDEX",0)),e=isNaN(e)?0:e),S("voice_search_auth_header_removal")||
(a["X-Goog-AuthUser"]=e.toString()),"INNERTUBE_HOST_OVERRIDE"in zf||(a["X-Origin"]=window.location.origin),b===void 0&&"DELEGATED_SESSION_ID"in zf&&(a["X-Goog-PageId"]=R("DELEGATED_SESSION_ID"));return a}
var mg=class{constructor(){this.Ka=!0}};var og={identityType:"UNAUTHENTICATED_IDENTITY_TYPE_UNKNOWN"};function pg(a){switch(a){case "DESKTOP":return 1;case "UNKNOWN_PLATFORM":return 0;case "TV":return 2;case "GAME_CONSOLE":return 3;case "MOBILE":return 4;case "TABLET":return 5}}
;w("ytglobal.prefsUserPrefsPrefs_",v("ytglobal.prefsUserPrefsPrefs_")||{});function qg(){if(R("DATASYNC_ID")!==void 0)return R("DATASYNC_ID");throw new O("Datasync ID not set","unknown");}
;function rg(a,b){return sg(a,0,b)}
function tg(a){const b=v("yt.scheduler.instance.addImmediateJob");b?b(a):a()}
var ug=class{h(a){sg(a,1)}};function vg(){wg.instance||(wg.instance=new wg);return wg.instance}
function sg(a,b,c){c!==void 0&&Number.isNaN(Number(c))&&(c=void 0);const d=v("yt.scheduler.instance.addJob");return d?d(a,b,c):c===void 0?(a(),NaN):Of(a,c||0)}
var wg=class extends ug{P(a){if(a===void 0||!Number.isNaN(Number(a))){var b=v("yt.scheduler.instance.cancelJob");b?b(a):window.clearTimeout(a)}}start(){const a=v("yt.scheduler.instance.start");a&&a()}},xg=vg();const yg=[];let zg,Ag=!1;function Bg(a){Ag||(zg?zg.handleError(a):(yg.push({type:"ERROR",payload:a}),yg.length>10&&yg.shift()))}
function Cg(a,b){Ag||(zg?zg.R(a,b):(yg.push({type:"EVENT",eventType:a,payload:b}),yg.length>10&&yg.shift()))}
;function Dg(a){if(a.indexOf(":")>=0)throw Error("Database name cannot contain ':'");}
function Eg(a){return a.substr(0,a.indexOf(":"))||a}
;const Fg={AUTH_INVALID:"No user identifier specified.",EXPLICIT_ABORT:"Transaction was explicitly aborted.",IDB_NOT_SUPPORTED:"IndexedDB is not supported.",MISSING_INDEX:"Index not created.",MISSING_OBJECT_STORES:"Object stores not created.",DB_DELETED_BY_MISSING_OBJECT_STORES:"Database is deleted because expected object stores were not created.",DB_REOPENED_BY_MISSING_OBJECT_STORES:"Database is reopened because expected object stores were not created.",UNKNOWN_ABORT:"Transaction was aborted for unknown reasons.",
QUOTA_EXCEEDED:"The current transaction exceeded its quota limitations.",QUOTA_MAYBE_EXCEEDED:"The current transaction may have failed because of exceeding quota limitations.",EXECUTE_TRANSACTION_ON_CLOSED_DB:"Can't start a transaction on a closed database",INCOMPATIBLE_DB_VERSION:"The binary is incompatible with the database version"},Gg={AUTH_INVALID:"ERROR",EXECUTE_TRANSACTION_ON_CLOSED_DB:"WARNING",EXPLICIT_ABORT:"IGNORED",IDB_NOT_SUPPORTED:"ERROR",MISSING_INDEX:"WARNING",MISSING_OBJECT_STORES:"ERROR",
DB_DELETED_BY_MISSING_OBJECT_STORES:"WARNING",DB_REOPENED_BY_MISSING_OBJECT_STORES:"WARNING",QUOTA_EXCEEDED:"WARNING",QUOTA_MAYBE_EXCEEDED:"WARNING",UNKNOWN_ABORT:"WARNING",INCOMPATIBLE_DB_VERSION:"WARNING"},Hg={AUTH_INVALID:!1,EXECUTE_TRANSACTION_ON_CLOSED_DB:!1,EXPLICIT_ABORT:!1,IDB_NOT_SUPPORTED:!1,MISSING_INDEX:!1,MISSING_OBJECT_STORES:!1,DB_DELETED_BY_MISSING_OBJECT_STORES:!1,DB_REOPENED_BY_MISSING_OBJECT_STORES:!1,QUOTA_EXCEEDED:!1,QUOTA_MAYBE_EXCEEDED:!0,UNKNOWN_ABORT:!0,INCOMPATIBLE_DB_VERSION:!1};
var U=class extends O{constructor(a,b={},c=Fg[a],d=Gg[a],e=Hg[a]){super(c,Object.assign({},{name:"YtIdbKnownError",isSw:self.document===void 0,isIframe:self!==self.top,type:a},b));this.type=a;this.message=c;this.level=d;this.h=e;Object.setPrototypeOf(this,U.prototype)}},Ig=class extends U{constructor(a,b){super("MISSING_OBJECT_STORES",{expectedObjectStores:b,foundObjectStores:a},Fg.MISSING_OBJECT_STORES);Object.setPrototypeOf(this,Ig.prototype)}},Jg=class extends Error{constructor(a,b){super();this.index=
a;this.objectStore=b;Object.setPrototypeOf(this,Jg.prototype)}};const Kg=["The database connection is closing","Can't start a transaction on a closed database","A mutation operation was attempted on a database that did not allow mutations"];
function Lg(a,b,c,d){b=Eg(b);let e;e=a instanceof Error?a:Error(`Unexpected error: ${a}`);if(e instanceof U)return e;a={objectStoreNames:c,dbName:b,dbVersion:d};if(e.name==="QuotaExceededError")return new U("QUOTA_EXCEEDED",a);if(Xa&&e.name==="UnknownError")return new U("QUOTA_MAYBE_EXCEEDED",a);if(e instanceof Jg)return new U("MISSING_INDEX",Object.assign({},a,{objectStore:e.objectStore,index:e.index}));if(e.name==="InvalidStateError"&&Kg.some(f=>e.message.includes(f)))return new U("EXECUTE_TRANSACTION_ON_CLOSED_DB",
a);
if(e.name==="AbortError")return new U("UNKNOWN_ABORT",a,e.message);e.args=[Object.assign({},a,{name:"IdbError",Eb:e.name})];e.level="WARNING";return e}
function Mg(a,b,c){return new U("IDB_NOT_SUPPORTED",{context:{caller:a,publicName:b,version:c,hasSucceededOnce:void 0}})}
;function Ng(a){if(!a)throw Error();throw a;}
function Og(a){return a}
var Pg=class{constructor(a){this.h=a}};function Qg(a,b,c,d,e){try{if(a.state.status!=="FULFILLED")throw Error("calling handleResolve before the promise is fulfilled.");const f=c(a.state.value);f instanceof Rg?Sg(a,b,f,d,e):d(f)}catch(f){e(f)}}
function Tg(a,b,c,d,e){try{if(a.state.status!=="REJECTED")throw Error("calling handleReject before the promise is rejected.");const f=c(a.state.reason);f instanceof Rg?Sg(a,b,f,d,e):d(f)}catch(f){e(f)}}
function Sg(a,b,c,d,e){b===c?e(new TypeError("Circular promise chain detected.")):c.then(f=>{f instanceof Rg?Sg(a,b,f,d,e):d(f)},f=>{e(f)})}
var Rg=class{constructor(a){this.state={status:"PENDING"};this.h=[];this.i=[];a=a.h;const b=d=>{if(this.state.status==="PENDING"){this.state={status:"FULFILLED",value:d};for(const e of this.h)e()}},c=d=>{if(this.state.status==="PENDING"){this.state={status:"REJECTED",
reason:d};for(const e of this.i)e()}};
try{a(b,c)}catch(d){c(d)}}static all(a){return new Rg(new Pg((b,c)=>{const d=[];let e=a.length;e===0&&b(d);for(let f=0;f<a.length;++f)Rg.resolve(a[f]).then(g=>{d[f]=g;e--;e===0&&b(d)}).catch(g=>{c(g)})}))}static resolve(a){return new Rg(new Pg((b,c)=>{a instanceof Rg?a.then(b,c):b(a)}))}static reject(a){return new Rg(new Pg((b,c)=>{c(a)}))}then(a,b){const c=a!=null?a:Og,d=b!=null?b:Ng;
return new Rg(new Pg((e,f)=>{this.state.status==="PENDING"?(this.h.push(()=>{Qg(this,this,c,e,f)}),this.i.push(()=>{Tg(this,this,d,e,f)})):this.state.status==="FULFILLED"?Qg(this,this,c,e,f):this.state.status==="REJECTED"&&Tg(this,this,d,e,f)}))}catch(a){return this.then(void 0,a)}};function Ug(a,b,c){const d=()=>{try{a.removeEventListener("success",e),a.removeEventListener("error",f)}catch(g){}},e=()=>{b(a.result);
d()},f=()=>{c(a.error);
d()};
a.addEventListener("success",e);a.addEventListener("error",f)}
function Vg(a){return new Promise((b,c)=>{Ug(a,b,c)})}
function V(a){return new Rg(new Pg((b,c)=>{Ug(a,b,c)}))}
;function Wg(a,b){return new Rg(new Pg((c,d)=>{const e=()=>{const f=a?b(a):null;f?f.then(g=>{a=g;e()},d):c()};
e()}))}
;const Xg=window;var W=Xg.ytcsi&&Xg.ytcsi.now?Xg.ytcsi.now:Xg.performance&&Xg.performance.timing&&Xg.performance.now&&Xg.performance.timing.navigationStart?()=>Xg.performance.timing.navigationStart+Xg.performance.now():()=>(new Date).getTime();function Yg(){return S("idb_immediate_commit")}
function X(a,b,c,d){return r(function*(){const e={mode:"readonly",C:!1,tag:"IDB_TRANSACTION_TAG_UNKNOWN"};typeof c==="string"?e.mode=c:Object.assign(e,c);a.transactionCount++;const f=e.C?3:1;let g=0,h;for(;!h;){g++;const p=Math.round(W());try{var k=a.h.transaction(b,e.mode),l=d,n=!!e.commit;const q=new Zg(k),m=yield $g(q,l,n),u=Math.round(W());ah(a,p,u,g,void 0,b.join(),e);return m}catch(q){l=Math.round(W());const m=Lg(q,a.h.name,b.join(),a.h.version);if(m instanceof U&&!m.h||g>=f)ah(a,p,l,g,m,b.join(),
e),h=m}}return Promise.reject(h)})}
function bh(a,b,c){a=a.h.createObjectStore(b,c);return new ch(a)}
function dh(a,b,c,d){return X(a,[b],{mode:"readwrite",C:!0,commit:Yg()},e=>{e=e.objectStore(b);return V(e.h.put(c,d))})}
function ah(a,b,c,d,e,f,g){b=c-b;e?(e instanceof U&&(e.type==="QUOTA_EXCEEDED"||e.type==="QUOTA_MAYBE_EXCEEDED")&&Cg("QUOTA_EXCEEDED",{dbName:Eg(a.h.name),objectStoreNames:f,transactionCount:a.transactionCount,transactionMode:g.mode}),e instanceof U&&e.type==="UNKNOWN_ABORT"&&(c-=a.j,c<0&&c>=2147483648&&(c=0),Cg("TRANSACTION_UNEXPECTEDLY_ABORTED",{objectStoreNames:f,transactionDuration:b,transactionCount:a.transactionCount,dbDuration:c}),a.i=!0),eh(a,!1,d,f,b,g.tag),Bg(e)):eh(a,!0,d,f,b,g.tag)}
function eh(a,b,c,d,e,f="IDB_TRANSACTION_TAG_UNKNOWN"){Cg("TRANSACTION_ENDED",{objectStoreNames:d,connectionHasUnknownAbortedTransaction:a.i,duration:e,isSuccessful:b,tryCount:c,tag:f})}
var fh=class{constructor(a,b){this.h=a;this.options=b;this.transactionCount=0;this.j=Math.round(W());this.i=!1}add(a,b,c){return X(this,[a],{mode:"readwrite",C:!0,commit:Yg()},d=>d.objectStore(a).add(b,c))}clear(a){return X(this,[a],{mode:"readwrite",
C:!0},b=>b.objectStore(a).clear())}close(){this.h.close();
let a;((a=this.options)==null?0:a.closed)&&this.options.closed()}count(a,b){return X(this,[a],{mode:"readonly",C:!0,commit:Yg()},c=>c.objectStore(a).count(b))}delete(a,b){return X(this,[a],{mode:"readwrite",
C:!0,commit:Yg()&&!(b instanceof IDBKeyRange)},c=>c.objectStore(a).delete(b))}get(a,b){return X(this,[a],{mode:"readonly",
C:!0,commit:Yg()},c=>c.objectStore(a).get(b))}getAll(a,b,c){return X(this,[a],{mode:"readonly",
C:!0},d=>d.objectStore(a).getAll(b,c))}objectStoreNames(){return Array.from(this.h.objectStoreNames)}getName(){return this.h.name}};
function gh(a,b,c){a=a.h.openCursor(b.query,b.direction);return hh(a).then(d=>Wg(d,c))}
function ih(a,b){return gh(a,{query:b},c=>c.delete().then(()=>jh(c))).then(()=>{})}
function kh(a,b,c){const d=[];return gh(a,{query:b},e=>{if(!(c!==void 0&&d.length>=c))return d.push(e.cursor.value),jh(e)}).then(()=>d)}
var ch=class{constructor(a){this.h=a}add(a,b){return V(this.h.add(a,b))}autoIncrement(){return this.h.autoIncrement}clear(){return V(this.h.clear()).then(()=>{})}count(a){return V(this.h.count(a))}delete(a){return a instanceof IDBKeyRange?ih(this,a):V(this.h.delete(a))}get(a){return V(this.h.get(a))}getAll(a,b){return"getAll"in IDBObjectStore.prototype?V(this.h.getAll(a,b)):kh(this,a,b)}index(a){try{return new lh(this.h.index(a))}catch(b){if(b instanceof Error&&b.name==="NotFoundError")throw new Jg(a,
this.h.name);
throw b;}}getName(){return this.h.name}keyPath(){return this.h.keyPath}};function $g(a,b,c){const d=new Promise((e,f)=>{try{const g=b(a);c&&a.commit();g.then(h=>{e(h)}).catch(f)}catch(g){f(g),a.abort()}});
return Promise.all([d,a.done]).then(([e])=>e)}
var Zg=class{constructor(a){this.h=a;this.j=new Map;this.i=!1;this.done=new Promise((b,c)=>{this.h.addEventListener("complete",()=>{b()});
this.h.addEventListener("error",d=>{d.currentTarget===d.target&&c(this.h.error)});
this.h.addEventListener("abort",()=>{var d=this.h.error;if(d)c(d);else if(!this.i){d=U;var e=this.h.objectStoreNames;const f=[];for(let g=0;g<e.length;g++){const h=e.item(g);if(h===null)throw Error("Invariant: item in DOMStringList is null");f.push(h)}d=new d("UNKNOWN_ABORT",{objectStoreNames:f.join(),dbName:this.h.db.name,mode:this.h.mode});c(d)}})})}abort(){this.h.abort();
this.i=!0;throw new U("EXPLICIT_ABORT");}commit(){if(!this.i){let a,b;(b=(a=this.h).commit)==null||b.call(a)}}objectStore(a){a=this.h.objectStore(a);let b=this.j.get(a);b||(b=new ch(a),this.j.set(a,b));return b}};function mh(a,b,c){const {query:d=null,direction:e="next"}=b;a=a.h.openCursor(d,e);return hh(a).then(f=>Wg(f,c))}
function nh(a,b,c){const d=[];return mh(a,{query:b},e=>{if(!(c!==void 0&&d.length>=c))return d.push(e.cursor.value),jh(e)}).then(()=>d)}
var lh=class{constructor(a){this.h=a}count(a){return V(this.h.count(a))}delete(a){return mh(this,{query:a},b=>b.delete().then(()=>jh(b)))}get(a){return V(this.h.get(a))}getAll(a,b){return"getAll"in IDBIndex.prototype?V(this.h.getAll(a,b)):nh(this,a,b)}keyPath(){return this.h.keyPath}unique(){return this.h.unique}};
function hh(a){return V(a).then(b=>b?new oh(a,b):null)}
function jh(a){a.cursor.continue(void 0);return hh(a.request)}
function ph(a){a.cursor.advance(5);return hh(a.request)}
var oh=class{constructor(a,b){this.request=a;this.cursor=b}delete(){return V(this.cursor.delete()).then(()=>{})}update(a){return V(this.cursor.update(a))}};function qh(a,b,c){return new Promise((d,e)=>{let f;f=b!==void 0?self.indexedDB.open(a,b):self.indexedDB.open(a);const g=c.va,h=c.blocking,k=c.La,l=c.upgrade,n=c.closed;let p;const q=()=>{p||(p=new fh(f.result,{closed:n}));return p};
f.addEventListener("upgradeneeded",m=>{try{if(m.newVersion===null)throw Error("Invariant: newVersion on IDbVersionChangeEvent is null");if(f.transaction===null)throw Error("Invariant: transaction on IDbOpenDbRequest is null");m.dataLoss&&m.dataLoss!=="none"&&Cg("IDB_DATA_CORRUPTED",{reason:m.dataLossMessage||"unknown reason",dbName:Eg(a)});const u=q(),z=new Zg(f.transaction);l&&l(u,y=>m.oldVersion<y&&m.newVersion>=y,z);
z.done.catch(y=>{e(y)})}catch(u){e(u)}});
f.addEventListener("success",()=>{const m=f.result;h&&m.addEventListener("versionchange",()=>{h(q())});
m.addEventListener("close",()=>{Cg("IDB_UNEXPECTEDLY_CLOSED",{dbName:Eg(a),dbVersion:m.version});k&&k()});
d(q())});
f.addEventListener("error",()=>{e(f.error)});
g&&f.addEventListener("blocked",()=>{g()})})}
function rh(a,b,c={}){return qh(a,b,c)}
function sh(a,b={}){return r(function*(){try{const c=self.indexedDB.deleteDatabase(a),d=b.va;d&&c.addEventListener("blocked",()=>{d()});
yield Vg(c)}catch(c){throw Lg(c,a,"",-1);}})}
;function th(a,b){return new U("INCOMPATIBLE_DB_VERSION",{dbName:a.name,oldVersion:a.options.version,newVersion:b})}
function uh(a,b){if(!b)throw Mg("openWithToken",Eg(a.name));return a.open()}
var vh=class{constructor(a,b){this.name=a;this.options=b;this.j=!0;this.m=this.l=0}i(a,b,c={}){return rh(a,b,c)}delete(a={}){return sh(this.name,a)}open(){if(!this.j)throw th(this);if(this.h)return this.h;let a;const b=()=>{this.h===a&&(this.h=void 0)},c={blocking:e=>{e.close()},
closed:b,La:b,upgrade:this.options.upgrade},d=()=>{const e=this;return r(function*(){var f,g=(f=Error().stack)!=null?f:"";try{const k=yield e.i(e.name,e.options.version,c);f=k;var h=e.options;const l=[];for(const n of Object.keys(h.M)){const {L:p,Jb:q=Number.MAX_VALUE}=h.M[n];!(f.h.version>=p)||f.h.version>=q||f.h.objectStoreNames.contains(n)||l.push(n)}if(l.length!==0){const n=Object.keys(e.options.M),p=k.objectStoreNames();if(e.m<T("ytidb_reopen_db_retries",0))return e.m++,k.close(),Bg(new U("DB_REOPENED_BY_MISSING_OBJECT_STORES",
{dbName:e.name,expectedObjectStores:n,foundObjectStores:p})),d();if(e.l<T("ytidb_remake_db_retries",1))return e.l++,yield e.delete(),Bg(new U("DB_DELETED_BY_MISSING_OBJECT_STORES",{dbName:e.name,expectedObjectStores:n,foundObjectStores:p})),d();throw new Ig(p,n);}return k}catch(k){if(k instanceof DOMException?k.name==="VersionError":"DOMError"in self&&k instanceof DOMError?k.name==="VersionError":k instanceof Object&&"message"in k&&k.message==="An attempt was made to open a database using a lower version than the existing version."){g=
yield e.i(e.name,void 0,Object.assign({},c,{upgrade:void 0}));h=g.h.version;if(e.options.version!==void 0&&h>e.options.version+1)throw g.close(),e.j=!1,th(e,h);return g}b();k instanceof Error&&!S("ytidb_async_stack_killswitch")&&(k.stack=`${k.stack}\n${g.substring(g.indexOf("\n")+1)}`);let l;throw Lg(k,e.name,"",(l=e.options.version)!=null?l:-1);}})};
return this.h=a=d()}};const wh=new vh("YtIdbMeta",{M:{databases:{L:1}},upgrade(a,b){b(1)&&bh(a,"databases",{keyPath:"actualName"})}});function xh(a,b){return r(function*(){return X(yield uh(wh,b),["databases"],{C:!0,mode:"readwrite"},c=>{const d=c.objectStore("databases");return d.get(a.actualName).then(e=>{if(e?a.actualName!==e.actualName||a.publicName!==e.publicName||a.userIdentifier!==e.userIdentifier:1)return V(d.h.put(a,void 0)).then(()=>{})})})})}
function yh(a,b){return r(function*(){if(a)return(yield uh(wh,b)).delete("databases",a)})}
;let zh;const Ah=new class{constructor(){}}(new class{constructor(){}});function Bh(){return r(function*(){return!0})}
function Ch(){if(zh!==void 0)return zh;Ag=!0;return zh=Bh().then(a=>{Ag=!1;return a})}
function Dh(){return v("ytglobal.idbToken_")||void 0}
function Eh(){const a=Dh();return a?Promise.resolve(a):Ch().then(b=>{(b=b?Ah:void 0)&&w("ytglobal.idbToken_",b);return b})}
;new Ge;function Fh(a){try{qg();var b=!0}catch(c){b=!1}if(!b)throw a=new U("AUTH_INVALID",{dbName:a}),Bg(a),a;b=qg();return{actualName:`${a}:${b}`,publicName:a,userIdentifier:b}}
function Gh(a,b,c,d){return r(function*(){var e,f=(e=Error().stack)!=null?e:"";e=yield Eh();if(!e)throw e=Mg("openDbImpl",a,b),S("ytidb_async_stack_killswitch")||(e.stack=`${e.stack}\n${f.substring(f.indexOf("\n")+1)}`),Bg(e),e;Dg(a);f=c?{actualName:a,publicName:a,userIdentifier:void 0}:Fh(a);try{return yield xh(f,e),yield rh(f.actualName,b,d)}catch(g){try{yield yh(f.actualName,e)}catch(h){}throw g;}})}
function Hh(a,b,c={}){return Gh(a,b,!1,c)}
function Ih(a,b,c={}){return Gh(a,b,!0,c)}
function Jh(a,b={}){return r(function*(){const c=yield Eh();if(c){Dg(a);var d=Fh(a);yield sh(d.actualName,b);yield yh(d.actualName,c)}})}
function Kh(a,b={}){return r(function*(){const c=yield Eh();c&&(Dg(a),yield sh(a,b),yield yh(a,c))})}
;function Lh(a,b){let c;return()=>{c||(c=new Mh(a,b));return c}}
var Mh=class extends vh{constructor(a,b){super(a,b);this.options=b;Dg(a)}i(a,b,c={}){return(this.options.shared?Ih:Hh)(a,b,Object.assign({},c))}delete(a={}){return(this.options.shared?Kh:Jh)(this.name,a)}};function Nh(a,b){return Lh(a,b)}
;var Oh=Nh("ytGcfConfig",{M:{coldConfigStore:{L:1},hotConfigStore:{L:1}},shared:!1,upgrade(a,b){b(1)&&(bh(a,"hotConfigStore",{keyPath:"key",autoIncrement:!0}).h.createIndex("hotTimestampIndex","timestamp",{unique:!1}),bh(a,"coldConfigStore",{keyPath:"key",autoIncrement:!0}).h.createIndex("coldTimestampIndex","timestamp",{unique:!1}))},version:1});function Ph(a){return uh(Oh(),a)}
function Qh(a,b,c){return r(function*(){const d={config:a,hashData:b,timestamp:W()},e=yield Ph(c);yield e.clear("hotConfigStore");return yield dh(e,"hotConfigStore",d)})}
function Rh(a,b,c,d){return r(function*(){const e={config:a,hashData:b,configData:c,timestamp:W()},f=yield Ph(d);yield f.clear("coldConfigStore");return yield dh(f,"coldConfigStore",e)})}
function Sh(a){return r(function*(){let b=void 0;yield X(yield Ph(a),["coldConfigStore"],{mode:"readwrite",C:!0},c=>mh(c.objectStore("coldConfigStore").index("coldTimestampIndex"),{direction:"prev"},d=>{b=d.cursor.value}));
return b})}
function Th(a){return r(function*(){let b=void 0;yield X(yield Ph(a),["hotConfigStore"],{mode:"readwrite",C:!0},c=>mh(c.objectStore("hotConfigStore").index("hotTimestampIndex"),{direction:"prev"},d=>{b=d.cursor.value}));
return b})}
;var Uh=class extends de{constructor(){super();this.j=[];this.h=[];const a=v("yt.gcf.config.hotUpdateCallbacks");a?(this.j=[...a],this.h=a):(this.h=[],w("yt.gcf.config.hotUpdateCallbacks",this.h))}m(){for(const b of this.j){var a=this.h;const c=a.indexOf(b);c>=0&&a.splice(c,1)}this.j.length=0;super.m()}};function Vh(a,b,c){return r(function*(){if(S("start_client_gcf")){c&&(a.j=c,w("yt.gcf.config.hotConfigGroup",a.j||null));a.hotHashData=b;w("yt.gcf.config.hotHashData",a.hotHashData||null);var d=Dh();if(d){if(!c){var e;c=(e=yield Th(d))==null?void 0:e.config}yield Qh(c,b,d)}if(c){d=a.i;e=c;for(const f of d.h)f(e)}}})}
function Wh(a,b,c){return r(function*(){if(S("start_client_gcf")){a.coldHashData=b;w("yt.gcf.config.coldHashData",a.coldHashData||null);const d=Dh();if(d){if(!c){let e;c=(e=yield Sh(d))==null?void 0:e.config}c&&(yield Rh(c,b,c.configData,d))}}})}
var Xh=class{constructor(){this.h=0;this.i=new Uh}};function Yh(){return"INNERTUBE_API_KEY"in zf&&"INNERTUBE_API_VERSION"in zf}
function Zh(){return{innertubeApiKey:R("INNERTUBE_API_KEY"),innertubeApiVersion:R("INNERTUBE_API_VERSION"),X:R("INNERTUBE_CONTEXT_CLIENT_CONFIG_INFO"),za:R("INNERTUBE_CONTEXT_CLIENT_NAME","WEB"),Aa:R("INNERTUBE_CONTEXT_CLIENT_NAME",1),innertubeContextClientVersion:R("INNERTUBE_CONTEXT_CLIENT_VERSION"),ja:R("INNERTUBE_CONTEXT_HL"),ia:R("INNERTUBE_CONTEXT_GL"),Ba:R("INNERTUBE_HOST_OVERRIDE")||"",Ca:!!R("INNERTUBE_USE_THIRD_PARTY_AUTH",!1),yb:!!R("INNERTUBE_OMIT_API_KEY_WHEN_AUTH_HEADER_IS_PRESENT",
!1),appInstallData:R("SERIALIZED_CLIENT_CONFIG_DATA")}}
function $h(a){const b={client:{hl:a.ja,gl:a.ia,clientName:a.za,clientVersion:a.innertubeContextClientVersion,configInfo:a.X}};navigator.userAgent&&(b.client.userAgent=String(navigator.userAgent));var c=t.devicePixelRatio;c&&c!=1&&(b.client.screenDensityFloat=String(c));c=Sf();c!==""&&(b.client.experimentsToken=c);c=Tf();c.length>0&&(b.request={internalExperimentFlags:c});ai(void 0,b);bi(a,void 0,b);S("start_client_gcf")&&ci(void 0,b);R("DELEGATED_SESSION_ID")&&!S("pageid_as_header_web")&&(b.user=
{onBehalfOfUser:R("DELEGATED_SESSION_ID")});!S("fill_delegate_context_in_gel_killswitch")&&(a=R("INNERTUBE_CONTEXT_SERIALIZED_DELEGATION_CONTEXT"))&&(b.user=Object.assign({},b.user,{serializedDelegationContext:a}));a=R("INNERTUBE_CONTEXT");var d;if(S("enable_persistent_device_token")&&(a==null?0:(d=a.client)==null?0:d.rolloutToken)){var e;b.client.rolloutToken=a==null?void 0:(e=a.client)==null?void 0:e.rolloutToken}d=Object;e=d.assign;a=b.client;var f=R("DEVICE","");c={};for(const [g,h]of Object.entries(Kf(f))){f=
g;const k=h;f==="cbrand"?c.deviceMake=k:f==="cmodel"?c.deviceModel=k:f==="cbr"?c.browserName=k:f==="cbrver"?c.browserVersion=k:f==="cos"?c.osName=k:f==="cosver"?c.osVersion=k:f==="cplatform"&&(c.platform=k)}b.client=e.call(d,a,c);return b}
function ai(a,b){const c=v("yt.embedded_player.embed_url");c&&(a?(b=Yc(a,Ye,7)||new Ye,J(b,4,c),I(a,Ye,7,b)):b&&(b.thirdParty={embedUrl:c}))}
function bi(a,b,c){if(a.appInstallData)if(b){let d;c=(d=Yc(b,Re,62))!=null?d:new Re;J(c,6,a.appInstallData);I(b,Re,62,c)}else c&&(c.client.configInfo=c.client.configInfo||{},c.client.configInfo.appInstallData=a.appInstallData)}
function di(a,b,c={}){let d={};R("EOM_VISITOR_DATA")?d={"X-Goog-EOM-Visitor-Id":R("EOM_VISITOR_DATA")}:d={"X-Goog-Visitor-Id":c.visitorData||R("VISITOR_DATA","")};if(b&&b.includes("www.youtube-nocookie.com"))return d;b=c.qb||R("AUTHORIZATION");b||(a?b=`Bearer ${v("gapi.auth.getToken")().pb}`:(a=ng(lg()),S("pageid_as_header_web")||delete a["X-Goog-PageId"],d=Object.assign({},d,a)));b&&(d.Authorization=b);return d}
function ci(a,b){if(!Xh.instance){var c=new Xh;Xh.instance=c}c=Xh.instance;var d=W()-c.h;if(c.h!==0&&d<T("send_config_hash_timer"))c=void 0;else{d=v("yt.gcf.config.coldConfigData");var e=v("yt.gcf.config.hotHashData"),f=v("yt.gcf.config.coldHashData");d&&e&&f&&(c.h=W());c={coldConfigData:d,hotHashData:e,coldHashData:f}}if(d=c)if(e=d.coldConfigData,c=d.coldHashData,d=d.hotHashData,a){var g;b=(g=Yc(a,Re,62))!=null?g:new Re;g=J(b,1,e);g=J(g,3,c);J(g,5,d);I(a,Re,62,b)}else b&&(b.client.configInfo=b.client.configInfo||
{},e&&(b.client.configInfo.coldConfigData=e),c&&(b.client.configInfo.coldHashData=c),d&&(b.client.configInfo.hotHashData=d))}
;typeof TextEncoder!=="undefined"&&new TextEncoder;function ei(a){this.version=1;this.args=a}
;function fi(){var a=gi;this.topic="screen-created";this.h=a}
fi.prototype.toString=function(){return this.topic};const hi=v("ytPubsub2Pubsub2Instance")||new M;M.prototype.subscribe=M.prototype.K;M.prototype.unsubscribeByKey=M.prototype.B;M.prototype.publish=M.prototype.D;M.prototype.clear=M.prototype.clear;w("ytPubsub2Pubsub2Instance",hi);const ii=v("ytPubsub2Pubsub2SubscribedKeys")||{};w("ytPubsub2Pubsub2SubscribedKeys",ii);const ji=v("ytPubsub2Pubsub2TopicToKeys")||{};w("ytPubsub2Pubsub2TopicToKeys",ji);const ki=v("ytPubsub2Pubsub2IsAsync")||{};w("ytPubsub2Pubsub2IsAsync",ki);
w("ytPubsub2Pubsub2SkipSubKey",null);function li(a,b){const c=mi();c&&c.publish.call(c,a.toString(),a,b)}
function ni(a){var b=oi;const c=mi();if(!c)return 0;const d=c.subscribe(b.toString(),(e,f)=>{var g=v("ytPubsub2Pubsub2SkipSubKey");g&&g==d||(g=()=>{if(ii[d])try{if(f&&b instanceof fi&&b!=e)try{var h=b.h,k=f;if(!k.args||!k.version)throw Error("yt.pubsub2.Data.deserialize(): serializedData is incomplete.");try{if(!h.la){const m=new h;h.la=m.version}var l=h.la}catch(m){}if(!l||k.version!=l)throw Error("yt.pubsub2.Data.deserialize(): serializedData version is incompatible.");try{l=Reflect;var n=l.construct;
{var p=k.args;const m=p.length;if(m>0){const u=Array(m);for(k=0;k<m;k++)u[k]=p[k];var q=u}else q=[]}f=n.call(l,h,q)}catch(m){throw m.message="yt.pubsub2.Data.deserialize(): "+m.message,m;}}catch(m){throw m.message="yt.pubsub2.pubsub2 cross-binary conversion error for "+b.toString()+": "+m.message,m;}a.call(window,f)}catch(m){Df(m)}},ki[b.toString()]?v("yt.scheduler.instance")?xg.h(g):Of(g,0):g())});
ii[d]=!0;ji[b.toString()]||(ji[b.toString()]=[]);ji[b.toString()].push(d);return d}
function pi(){var a=qi;const b=ni(function(c){a.apply(void 0,arguments);ri(b)});
return b}
function ri(a){const b=mi();b&&(typeof a==="number"&&(a=[a]),wa(a,c=>{b.unsubscribeByKey(c);delete ii[c]}))}
function mi(){return v("ytPubsub2Pubsub2Instance")}
;let si=void 0,ti=void 0;var ui={accountStateChangeSignedIn:23,accountStateChangeSignedOut:24,delayedEventMetricCaptured:11,latencyActionBaselined:6,latencyActionInfo:7,latencyActionTicked:5,offlineTransferStatusChanged:2,offlineImageDownload:335,playbackStartStateChanged:9,systemHealthCaptured:3,mangoOnboardingCompleted:10,mangoPushNotificationReceived:230,mangoUnforkDbMigrationError:121,mangoUnforkDbMigrationSummary:122,mangoUnforkDbMigrationPreunforkDbVersionNumber:133,mangoUnforkDbMigrationPhoneMetadata:134,mangoUnforkDbMigrationPhoneStorage:135,
mangoUnforkDbMigrationStep:142,mangoAsyncApiMigrationEvent:223,mangoDownloadVideoResult:224,mangoHomepageVideoCount:279,mangoHomeV3State:295,mangoImageClientCacheHitEvent:273,sdCardStatusChanged:98,framesDropped:12,thumbnailHovered:13,deviceRetentionInfoCaptured:14,thumbnailLoaded:15,backToAppEvent:318,streamingStatsCaptured:17,offlineVideoShared:19,appCrashed:20,youThere:21,offlineStateSnapshot:22,mdxSessionStarted:25,mdxSessionConnected:26,mdxSessionDisconnected:27,bedrockResourceConsumptionSnapshot:28,
nextGenWatchWatchSwiped:29,kidsAccountsSnapshot:30,zeroStepChannelCreated:31,tvhtml5SearchCompleted:32,offlineSharePairing:34,offlineShareUnlock:35,mdxRouteDistributionSnapshot:36,bedrockRepetitiveActionTimed:37,unpluggedDegradationInfo:229,uploadMp4HeaderMoved:38,uploadVideoTranscoded:39,uploadProcessorStarted:46,uploadProcessorEnded:47,uploadProcessorReady:94,uploadProcessorRequirementPending:95,uploadProcessorInterrupted:96,uploadFrontendEvent:241,assetPackDownloadStarted:41,assetPackDownloaded:42,
assetPackApplied:43,assetPackDeleted:44,appInstallAttributionEvent:459,playbackSessionStopped:45,adBlockerMessagingShown:48,distributionChannelCaptured:49,dataPlanCpidRequested:51,detailedNetworkTypeCaptured:52,sendStateUpdated:53,receiveStateUpdated:54,sendDebugStateUpdated:55,receiveDebugStateUpdated:56,kidsErrored:57,mdxMsnSessionStatsFinished:58,appSettingsCaptured:59,mdxWebSocketServerHttpError:60,mdxWebSocketServer:61,startupCrashesDetected:62,coldStartInfo:435,offlinePlaybackStarted:63,liveChatMessageSent:225,
liveChatUserPresent:434,liveChatBeingModerated:457,liveCreationCameraUpdated:64,liveCreationEncodingCaptured:65,liveCreationError:66,liveCreationHealthUpdated:67,liveCreationVideoEffectsCaptured:68,liveCreationStageOccured:75,liveCreationBroadcastScheduled:123,liveCreationArchiveReplacement:149,liveCreationCostreamingConnection:421,liveCreationStreamWebrtcStats:288,liveCreationWebrtcError:526,mdxSessionRecoveryStarted:69,mdxSessionRecoveryCompleted:70,mdxSessionRecoveryStopped:71,visualElementShown:72,
visualElementHidden:73,visualElementGestured:78,visualElementStateChanged:208,screenCreated:156,playbackAssociated:202,visualElementAttached:215,playbackContextEvent:214,cloudCastingPlaybackStarted:74,webPlayerApiCalled:76,tvhtml5AccountDialogOpened:79,foregroundHeartbeat:80,foregroundHeartbeatScreenAssociated:111,kidsOfflineSnapshot:81,mdxEncryptionSessionStatsFinished:82,playerRequestCompleted:83,liteSchedulerStatistics:84,mdxSignIn:85,spacecastMetadataLookupRequested:86,spacecastBatchLookupRequested:87,
spacecastSummaryRequested:88,spacecastPlayback:89,spacecastDiscovery:90,tvhtml5LaunchUrlComponentChanged:91,mdxBackgroundPlaybackRequestCompleted:92,mdxBrokenAdditionalDataDeviceDetected:93,tvhtml5LocalStorage:97,tvhtml5DeviceStorageStatus:147,autoCaptionsAvailable:99,playbackScrubbingEvent:339,flexyState:100,interfaceOrientationCaptured:101,mainAppBrowseFragmentCache:102,offlineCacheVerificationFailure:103,offlinePlaybackExceptionDigest:217,vrCopresenceStats:104,vrCopresenceSyncStats:130,vrCopresenceCommsStats:137,
vrCopresencePartyStats:153,vrCopresenceEmojiStats:213,vrCopresenceEvent:141,vrCopresenceFlowTransitEvent:160,vrCowatchPartyEvent:492,vrCowatchUserStartOrJoinEvent:504,vrPlaybackEvent:345,kidsAgeGateTracking:105,offlineDelayAllowedTracking:106,mainAppAutoOfflineState:107,videoAsThumbnailDownload:108,videoAsThumbnailPlayback:109,liteShowMore:110,renderingError:118,kidsProfilePinGateTracking:119,abrTrajectory:124,scrollEvent:125,streamzIncremented:126,kidsProfileSwitcherTracking:127,kidsProfileCreationTracking:129,
buyFlowStarted:136,mbsConnectionInitiated:138,mbsPlaybackInitiated:139,mbsLoadChildren:140,liteProfileFetcher:144,mdxRemoteTransaction:146,reelPlaybackError:148,reachabilityDetectionEvent:150,mobilePlaybackEvent:151,courtsidePlayerStateChanged:152,musicPersistentCacheChecked:154,musicPersistentCacheCleared:155,playbackInterrupted:157,playbackInterruptionResolved:158,fixFopFlow:159,anrDetection:161,backstagePostCreationFlowEnded:162,clientError:163,gamingAccountLinkStatusChanged:164,liteHousewarming:165,
buyFlowEvent:167,kidsParentalGateTracking:168,kidsSignedOutSettingsStatus:437,kidsSignedOutPauseHistoryFixStatus:438,tvhtml5WatchdogViolation:444,ypcUpgradeFlow:169,yongleStudy:170,ypcUpdateFlowStarted:171,ypcUpdateFlowCancelled:172,ypcUpdateFlowSucceeded:173,ypcUpdateFlowFailed:174,liteGrowthkitPromo:175,paymentFlowStarted:341,transactionFlowShowPaymentDialog:405,transactionFlowStarted:176,transactionFlowSecondaryDeviceStarted:222,transactionFlowSecondaryDeviceSignedOutStarted:383,transactionFlowCancelled:177,
transactionFlowPaymentCallBackReceived:387,transactionFlowPaymentSubmitted:460,transactionFlowPaymentSucceeded:329,transactionFlowSucceeded:178,transactionFlowFailed:179,transactionFlowPlayBillingConnectionStartEvent:428,transactionFlowSecondaryDeviceSuccess:458,transactionFlowErrorEvent:411,liteVideoQualityChanged:180,watchBreakEnablementSettingEvent:181,watchBreakFrequencySettingEvent:182,videoEffectsCameraPerformanceMetrics:183,adNotify:184,startupTelemetry:185,playbackOfflineFallbackUsed:186,
outOfMemory:187,ypcPauseFlowStarted:188,ypcPauseFlowCancelled:189,ypcPauseFlowSucceeded:190,ypcPauseFlowFailed:191,uploadFileSelected:192,ypcResumeFlowStarted:193,ypcResumeFlowCancelled:194,ypcResumeFlowSucceeded:195,ypcResumeFlowFailed:196,adsClientStateChange:197,ypcCancelFlowStarted:198,ypcCancelFlowCancelled:199,ypcCancelFlowSucceeded:200,ypcCancelFlowFailed:201,ypcCancelFlowGoToPaymentProcessor:402,ypcDeactivateFlowStarted:320,ypcRedeemFlowStarted:203,ypcRedeemFlowCancelled:204,ypcRedeemFlowSucceeded:205,
ypcRedeemFlowFailed:206,ypcFamilyCreateFlowStarted:258,ypcFamilyCreateFlowCancelled:259,ypcFamilyCreateFlowSucceeded:260,ypcFamilyCreateFlowFailed:261,ypcFamilyManageFlowStarted:262,ypcFamilyManageFlowCancelled:263,ypcFamilyManageFlowSucceeded:264,ypcFamilyManageFlowFailed:265,restoreContextEvent:207,embedsAdEvent:327,autoplayTriggered:209,clientDataErrorEvent:210,experimentalVssValidation:211,tvhtml5TriggeredEvent:212,tvhtml5FrameworksFieldTrialResult:216,tvhtml5FrameworksFieldTrialStart:220,musicOfflinePreferences:218,
watchTimeSegment:219,appWidthLayoutError:221,accountRegistryChange:226,userMentionAutoCompleteBoxEvent:227,downloadRecommendationEnablementSettingEvent:228,musicPlaybackContentModeChangeEvent:231,offlineDbOpenCompleted:232,kidsFlowEvent:233,kidsFlowCorpusSelectedEvent:234,videoEffectsEvent:235,unpluggedOpsEogAnalyticsEvent:236,playbackAudioRouteEvent:237,interactionLoggingDebugModeError:238,offlineYtbRefreshed:239,kidsFlowError:240,musicAutoplayOnLaunchAttempted:242,deviceContextActivityEvent:243,
deviceContextEvent:244,templateResolutionException:245,musicSideloadedPlaylistServiceCalled:246,embedsStorageAccessNotChecked:247,embedsHasStorageAccessResult:248,embedsItpPlayedOnReload:249,embedsRequestStorageAccessResult:250,embedsShouldRequestStorageAccessResult:251,embedsRequestStorageAccessState:256,embedsRequestStorageAccessFailedState:257,embedsItpWatchLaterResult:266,searchSuggestDecodingPayloadFailure:252,siriShortcutActivated:253,tvhtml5KeyboardPerformance:254,latencyActionSpan:255,elementsLog:267,
ytbFileOpened:268,tfliteModelError:269,apiTest:270,yongleUsbSetup:271,touStrikeInterstitialEvent:272,liteStreamToSave:274,appBundleClientEvent:275,ytbFileCreationFailed:276,adNotifyFailure:278,ytbTransferFailed:280,blockingRequestFailed:281,liteAccountSelector:282,liteAccountUiCallbacks:283,dummyPayload:284,browseResponseValidationEvent:285,entitiesError:286,musicIosBackgroundFetch:287,mdxNotificationEvent:289,layersValidationError:290,musicPwaInstalled:291,liteAccountCleanup:292,html5PlayerHealthEvent:293,
watchRestoreAttempt:294,liteAccountSignIn:296,notaireEvent:298,kidsVoiceSearchEvent:299,adNotifyFilled:300,delayedEventDropped:301,analyticsSearchEvent:302,systemDarkThemeOptOutEvent:303,flowEvent:304,networkConnectivityBaselineEvent:305,ytbFileImported:306,downloadStreamUrlExpired:307,directSignInEvent:308,lyricImpressionEvent:309,accessibilityStateEvent:310,tokenRefreshEvent:311,genericAttestationExecution:312,tvhtml5VideoSeek:313,unpluggedAutoPause:314,scrubbingEvent:315,bedtimeReminderEvent:317,
tvhtml5UnexpectedRestart:319,tvhtml5StabilityTraceEvent:478,tvhtml5OperationHealth:467,tvhtml5WatchKeyEvent:321,voiceLanguageChanged:322,tvhtml5LiveChatStatus:323,parentToolsCorpusSelectedEvent:324,offerAdsEnrollmentInitiated:325,networkQualityIntervalEvent:326,deviceStartupMetrics:328,heartbeatActionPlayerTransitioned:330,tvhtml5Lifecycle:331,heartbeatActionPlayerHalted:332,adaptiveInlineMutedSettingEvent:333,mainAppLibraryLoadingState:334,thirdPartyLogMonitoringEvent:336,appShellAssetLoadReport:337,
tvhtml5AndroidAttestation:338,tvhtml5StartupSoundEvent:340,iosBackgroundRefreshTask:342,iosBackgroundProcessingTask:343,sliEventBatch:344,postImpressionEvent:346,musicSideloadedPlaylistExport:347,idbUnexpectedlyClosed:348,voiceSearchEvent:349,mdxSessionCastEvent:350,idbQuotaExceeded:351,idbTransactionEnded:352,idbTransactionAborted:353,tvhtml5KeyboardLogging:354,idbIsSupportedCompleted:355,creatorStudioMobileEvent:356,idbDataCorrupted:357,parentToolsAppChosenEvent:358,webViewBottomSheetResized:359,
activeStateControllerScrollPerformanceSummary:360,navigatorValidation:361,mdxSessionHeartbeat:362,clientHintsPolyfillDiagnostics:363,clientHintsPolyfillEvent:364,proofOfOriginTokenError:365,kidsAddedAccountSummary:366,musicWearableDevice:367,ypcRefundFlowEvent:368,tvhtml5PlaybackMeasurementEvent:369,tvhtml5WatermarkMeasurementEvent:370,clientExpGcfPropagationEvent:371,mainAppReferrerIntent:372,leaderLockEnded:373,leaderLockAcquired:374,googleHatsEvent:375,persistentLensLaunchEvent:376,parentToolsChildWelcomeChosenEvent:378,
browseThumbnailPreloadEvent:379,finalPayload:380,mdxDialAdditionalDataUpdateEvent:381,webOrchestrationTaskLifecycleRecord:382,startupSignalEvent:384,accountError:385,gmsDeviceCheckEvent:386,accountSelectorEvent:388,accountUiCallbacks:389,mdxDialAdditionalDataProbeEvent:390,downloadsSearchIcingApiStats:391,downloadsSearchIndexUpdatedEvent:397,downloadsSearchIndexSnapshot:398,dataPushClientEvent:392,kidsCategorySelectedEvent:393,mdxDeviceManagementSnapshotEvent:394,prefetchRequested:395,prefetchableCommandExecuted:396,
gelDebuggingEvent:399,webLinkTtsPlayEnd:400,clipViewInvalid:401,persistentStorageStateChecked:403,cacheWipeoutEvent:404,playerEvent:410,sfvEffectPipelineStartedEvent:412,sfvEffectPipelinePausedEvent:429,sfvEffectPipelineEndedEvent:413,sfvEffectChosenEvent:414,sfvEffectLoadedEvent:415,sfvEffectUserInteractionEvent:465,sfvEffectFirstFrameProcessedLatencyEvent:416,sfvEffectAggregatedFramesProcessedLatencyEvent:417,sfvEffectAggregatedFramesDroppedEvent:418,sfvEffectPipelineErrorEvent:430,sfvEffectGraphFrozenEvent:419,
sfvEffectGlThreadBlockedEvent:420,mdeQosEvent:510,mdeVideoChangedEvent:442,mdePlayerPerformanceMetrics:472,mdeExporterEvent:497,genericClientExperimentEvent:423,homePreloadTaskScheduled:424,homePreloadTaskExecuted:425,homePreloadCacheHit:426,polymerPropertyChangedInObserver:427,applicationStarted:431,networkCronetRttBatch:432,networkCronetRttSummary:433,repeatChapterLoopEvent:436,seekCancellationEvent:462,lockModeTimeoutEvent:483,externalVideoShareToYoutubeAttempt:501,parentCodeEvent:502,offlineTransferStarted:4,
musicOfflineMixtapePreferencesChanged:16,mangoDailyNewVideosNotificationAttempt:40,mangoDailyNewVideosNotificationError:77,dtwsPlaybackStarted:112,dtwsTileFetchStarted:113,dtwsTileFetchCompleted:114,dtwsTileFetchStatusChanged:145,dtwsKeyframeDecoderBufferSent:115,dtwsTileUnderflowedOnNonkeyframe:116,dtwsBackfillFetchStatusChanged:143,dtwsBackfillUnderflowed:117,dtwsAdaptiveLevelChanged:128,blockingVisitorIdTimeout:277,liteSocial:18,mobileJsInvocation:297,biscottiBasedDetection:439,coWatchStateChange:440,
embedsVideoDataDidChange:441,shortsFirst:443,cruiseControlEvent:445,qoeClientLoggingContext:446,atvRecommendationJobExecuted:447,tvhtml5UserFeedback:448,producerProjectCreated:449,producerProjectOpened:450,producerProjectDeleted:451,producerProjectElementAdded:453,producerProjectElementRemoved:454,producerAppStateChange:509,producerProjectDiskInsufficientExportFailure:516,producerMediaServicesResetDetails:522,tvhtml5ShowClockEvent:455,deviceCapabilityCheckMetrics:456,youtubeClearcutEvent:461,offlineBrowseFallbackEvent:463,
getCtvTokenEvent:464,startupDroppedFramesSummary:466,screenshotEvent:468,miniAppPlayEvent:469,elementsDebugCounters:470,fontLoadEvent:471,webKillswitchReceived:473,webKillswitchExecuted:474,cameraOpenEvent:475,manualSmoothnessMeasurement:476,tvhtml5AppQualityEvent:477,polymerPropertyAccessEvent:479,miniAppSdkUsage:480,cobaltTelemetryEvent:481,crossDevicePlayback:482,channelCreatedWithObakeImage:484,channelEditedWithObakeImage:485,offlineDeleteEvent:486,crossDeviceNotificationTransfer:487,androidIntentEvent:488,
unpluggedAmbientInterludesCounterfactualEvent:489,keyPlaysPlayback:490,shortsCreationFallbackEvent:493,vssData:491,castMatch:494,miniAppPerformanceMetrics:495,userFeedbackEvent:496,kidsGuestSessionMismatch:498,musicSideloadedPlaylistMigrationEvent:499,sleepTimerSessionFinishEvent:500,watchEpPromoConflict:503,innertubeResponseCacheMetrics:505,miniAppAdEvent:506,dataPlanUpsellEvent:507,producerProjectRenamed:508,producerMediaSelectionEvent:511,embedsAutoplayStatusChanged:512,remoteConnectEvent:513,
connectedSessionMisattributionEvent:514,producerProjectElementModified:515,adsSeenClientLogging:517,producerEvent:518,tvhtml5CleanStart:519,deviceAccountMetricsEvent:520,derpLogEvent:521,playablesPortalEvent:523,ipValidationStarted:524,ipValidationReceived:525,reelsSequenceMutationEvent:527,watchZoomStateChange:528,metadataEditorEvent:529,kidsPrismaDeeplinksEvent:530};const vi=["client.name","client.version"];function wi(a){if(!a.errorMetadata||!a.errorMetadata.kvPairs)return a;a.errorMetadata.kvPairs=a.errorMetadata.kvPairs.filter(b=>b.key?vi.includes(b.key):!1);
return a}
;var xi=Nh("ServiceWorkerLogsDatabase",{M:{SWHealthLog:{L:1}},shared:!0,upgrade:(a,b)=>{b(1)&&bh(a,"SWHealthLog",{keyPath:"id",autoIncrement:!0}).h.createIndex("swHealthNewRequest",["interface","timestamp"],{unique:!1})},
version:1});function yi(a,b){return r(function*(){var c=yield uh(xi(),b),d=R("INNERTUBE_CONTEXT_CLIENT_NAME",0);const e=Object.assign({},a);e.clientError&&(e.clientError=wi(e.clientError));e.interface=d;return dh(c,"SWHealthLog",e)})}
;w("ytNetworklessLoggingInitializationOptions",t.ytNetworklessLoggingInitializationOptions||{isNwlInitialized:!1});function zi(a,b,c,d){!R("VISITOR_DATA")&&b!=="visitor_id"&&Math.random()<.01&&Ef(new O("Missing VISITOR_DATA when sending innertube request.",b,c,d));if(!a.isReady())throw a=new O("innertube xhrclient not ready",b,c,d),Df(a),a;c={headers:d.headers||{},method:"POST",postParams:c,postBody:d.postBody,postBodyFormat:d.postBodyFormat||"JSON",onTimeout:()=>{d.onTimeout()},
onFetchTimeout:d.onTimeout,onSuccess:(h,k)=>{if(d.onSuccess)d.onSuccess(k)},
onFetchSuccess:h=>{if(d.onSuccess)d.onSuccess(h)},
onProgress:h=>{if(d.onProgress)d.onProgress(h)},
onError:(h,k)=>{if(d.onError)d.onError(k)},
onFetchError:h=>{if(d.onError)d.onError(h)},
timeout:d.timeout,withCredentials:!0,compress:d.compress};c.headers["Content-Type"]||(c.headers["Content-Type"]="application/json");let e="";var f=a.config_.Ba;f&&(e=f);f=a.config_.Ca||!1;const g=di(f,e,d);Object.assign(c.headers,g);c.headers.Authorization&&!e&&f&&(c.headers["x-origin"]=window.location.origin);a=Lf(`${e}${`/${"youtubei"}/${a.config_.innertubeApiVersion}/${b}`}`,{alt:"json"});try{Vf(a,c)}catch(h){if(h.name==="InvalidAccessError")Ef(Error("An extension is blocking network request."));
else throw h;}}
var Ai=class{constructor(a){this.config_=null;a?this.config_=a:Yh()&&(this.config_=Zh())}isReady(){!this.config_&&Yh()&&(this.config_=Zh());return!!this.config_}};let Bi=0;w("ytDomDomGetNextId",v("ytDomDomGetNextId")||(()=>++Bi));w("ytEventsEventsListeners",t.ytEventsEventsListeners||{});w("ytEventsEventsCounter",t.ytEventsEventsCounter||{count:0});t.ytPubsubPubsubInstance||new M;var Ci=Symbol("injectionDeps"),Di=class{constructor(){this.name="INNERTUBE_TRANSPORT_TOKEN"}toString(){return`InjectionToken(${this.name})`}},Ei=class{constructor(){this.key=Xh}};function Fi(a){var b={ba:Gi,ka:Hi.instance};a.i.set(b.ba,b);const c=a.j.get(b.ba);if(c)try{c.Mb(a.resolve(b.ba))}catch(d){c.Ib(d)}}
function Ii(a,b,c,d=!1){if(c.indexOf(b)>-1)throw Error(`Deps cycle for: ${b}`);if(a.h.has(b))return a.h.get(b);if(!a.i.has(b)){if(d)return;throw Error(`No provider for: ${b}`);}d=a.i.get(b);c.push(b);if(d.ka!==void 0)var e=d.ka;else if(d.Na)e=d[Ci]?Ji(a,d[Ci],c):[],e=d.Na(...e);else if(d.Ma){e=d.Ma;const f=e[Ci]?Ji(a,e[Ci],c):[];e=new e(...f)}else throw Error(`Could not resolve providers for: ${b}`);c.pop();d.Sb||a.h.set(b,e);return e}
function Ji(a,b,c){return b?b.map(d=>d instanceof Ei?Ii(a,d.key,c,!0):Ii(a,d,c)):[]}
var Ki=class{constructor(){this.i=new Map;this.j=new Map;this.h=new Map}resolve(a){return a instanceof Ei?Ii(this,a.key,[],!0):Ii(this,a,[])}};let Li;function Mi(){Li||(Li=new Ki);return Li}
;let Ni=window;function Oi(){let a,b;return"h5vcc"in Ni&&((a=Ni.h5vcc.traceEvent)==null?0:a.traceBegin)&&((b=Ni.h5vcc.traceEvent)==null?0:b.traceEnd)?1:"performance"in Ni&&Ni.performance.mark&&Ni.performance.measure?2:0}
function Pi(a){const b=Oi();switch(b){case 1:Ni.h5vcc.traceEvent.traceBegin("YTLR",a);break;case 2:Ni.performance.mark(`${a}-start`);break;case 0:break;default:va(b,"unknown trace type")}}
function Qi(a){var b=Oi();switch(b){case 1:Ni.h5vcc.traceEvent.traceEnd("YTLR",a);break;case 2:b=`${a}-start`;const c=`${a}-end`;Ni.performance.mark(c);Ni.performance.measure(a,b,c);break;case 0:break;default:va(b,"unknown trace type")}}
;var Ri=S("web_enable_lifecycle_monitoring")&&Oi()!==0,Si=S("web_enable_lifecycle_monitoring");function Ti(a){let b,c;(c=(b=window).onerror)==null||c.call(b,a.message,"",0,0,a)}
;function Ui(a){let b;return(b=a.priority)!=null?b:0}
function Vi(a){var b=Array.from(a.h.keys()).sort((c,d)=>Ui(a.h[d])-Ui(a.h[c]));
for(const c of b)b=a.h[c],b.jobId===void 0||b.T||(a.scheduler.P(b.jobId),sg(b.Y,10))}
var Wi=class{constructor(a){this.scheduler=vg();this.i=new Ge;this.h=a;for(let b=0;b<this.h.length;b++){const c=this.h[b];a=()=>{c.Y();this.h[b].T=!0;this.h.every(e=>e.T===!0)&&this.i.resolve()};
const d=sg(a,Ui(c));this.h[b]=Object.assign({},c,{Y:a,jobId:d})}}cancel(){for(const a of this.h)a.jobId===void 0||a.T||this.scheduler.P(a.jobId),a.T=!0;this.i.resolve()}};function Xi(a,b,c){Si&&console.groupCollapsed&&console.groupEnd&&(console.groupCollapsed(`[${a.constructor.name}] '${a.state}' to '${b}'`),console.log("with message: ",c),console.groupEnd())}
function Yi(a,b){const c=b.filter(e=>Zi(a,e)===10),d=b.filter(e=>Zi(a,e)!==10);
return a.l.Qb?(...e)=>r(function*(){yield $i(c,...e);aj(a,d,...e)}):(...e)=>{bj(c,...e);
aj(a,d,...e)}}
function Zi(a,b){let c,d;return(d=(c=a.j)!=null?c:b.priority)!=null?d:0}
function $i(a,...b){return r(function*(){vg();for(const c of a){let d;tg(()=>{cj(c.name);const e=dj(()=>c.callback(...b));
Qb(e)?d=S("web_lifecycle_error_handling_killswitch")?e.then(()=>{ej(c.name)}):e.then(()=>{ej(c.name)},f=>{Ti(f);
ej(c.name)}):ej(c.name)});
d&&(yield d)}})}
function aj(a,b,...c){b=b.map(d=>({Y:()=>{cj(d.name);dj(()=>d.callback(...c));
ej(d.name)},
priority:Zi(a,d)}));
b.length&&(a.i=new Wi(b))}
function bj(a,...b){vg();for(const c of a)tg(()=>{cj(c.name);dj(()=>c.callback(...b));
ej(c.name)})}
function cj(a){Ri&&a&&Pi(a)}
function ej(a){Ri&&a&&Qi(a)}
var fj=class{constructor(){this.state="none";this.plugins=[];this.j=void 0;this.l={};Ri&&Pi(this.state)}get currentState(){return this.state}install(a){this.plugins.push(a);return this}transition(a,b){Ri&&Qi(this.state);var c=this.transitions.find(d=>Array.isArray(d.from)?d.from.find(e=>e===this.state&&d.to===a):d.from===this.state&&d.to===a);
if(c){this.i&&(Vi(this.i),this.i=void 0);Xi(this,a,b);this.state=a;Ri&&Pi(this.state);c=c.action.bind(this);const d=this.plugins.filter(e=>e[a]).map(e=>e[a]);
c(Yi(this,d),b)}else throw Error(`no transition specified from ${this.state} to ${a}`);}};function dj(a){if(S("web_lifecycle_error_handling_killswitch"))return a();try{return a()}catch(b){Ti(b)}}
;function gj(){hj||(hj=new ij);return hj}
var ij=class extends fj{constructor(){super();this.h=null;this.j=10;this.transitions=[{from:"none",to:"application_navigating",action:this.m},{from:"application_navigating",to:"none",action:this.u},{from:"application_navigating",to:"application_navigating",action:()=>{}},
{from:"none",to:"none",action:()=>{}}]}m(a,b){this.h=rg(()=>{this.currentState==="application_navigating"&&this.transition("none")},5E3);
a(b==null?void 0:b.event)}u(a,b){this.h&&(xg.P(this.h),this.h=null);a(b==null?void 0:b.event)}},hj;let jj=[];w("yt.logging.transport.getScrapedGelPayloads",function(){return jj});function kj(a,b){const c=lj(b);if(a.h[c])return a.h[c];const d=Object.keys(a.store)||[];if(d.length<=1&&lj(b)===d[0])return d;const e=[];for(let g=0;g<d.length;g++){const h=d[g].split("/");if(mj(b.auth,h[0])){var f=b.isJspb;mj(f===void 0?"undefined":f?"true":"false",h[1])&&mj(b.cttAuthInfo,h[2])&&(f=b.tier,f=f===void 0?"undefined":JSON.stringify(f),mj(f,h[3])&&e.push(d[g]))}}return a.h[c]=e}
function mj(a,b){return a===void 0||a==="undefined"?!0:a===b}
var nj=class{constructor(){this.store={};this.h={}}storePayload(a,b){a=lj(a);this.store[a]?this.store[a].push(b):(this.h={},this.store[a]=[b]);S("more_accurate_gel_parser")&&(b=new CustomEvent("TRANSPORTING_NEW_EVENT"),window.dispatchEvent(b));return a}smartExtractMatchingEntries(a){if(!a.keys.length)return[];const b=kj(this,a.keys.splice(0,1)[0]),c=[];for(let d=0;d<b.length;d++)this.store[b[d]]&&a.sizeLimit&&(this.store[b[d]].length<=a.sizeLimit?(c.push(...this.store[b[d]]),delete this.store[b[d]]):
c.push(...this.store[b[d]].splice(0,a.sizeLimit)));(a==null?0:a.sizeLimit)&&c.length<(a==null?void 0:a.sizeLimit)&&(a.sizeLimit-=c.length,c.push(...this.smartExtractMatchingEntries(a)));return c}extractMatchingEntries(a){a=kj(this,a);const b=[];for(let c=0;c<a.length;c++)this.store[a[c]]&&(b.push(...this.store[a[c]]),delete this.store[a[c]]);return b}getSequenceCount(a){a=kj(this,a);let b=0;for(let c=0;c<a.length;c++){let d;b+=((d=this.store[a[c]])==null?void 0:d.length)||0}return b}};
nj.prototype.getSequenceCount=nj.prototype.getSequenceCount;nj.prototype.extractMatchingEntries=nj.prototype.extractMatchingEntries;nj.prototype.smartExtractMatchingEntries=nj.prototype.smartExtractMatchingEntries;nj.prototype.storePayload=nj.prototype.storePayload;function lj(a){return[a.auth===void 0?"undefined":a.auth,a.isJspb===void 0?"undefined":a.isJspb,a.cttAuthInfo===void 0?"undefined":a.cttAuthInfo,a.tier===void 0?"undefined":a.tier].join("/")}
;function oj(a,b){if(a)return a[b.name]}
;const pj=T("initial_gel_batch_timeout",2E3),qj=T("gel_queue_timeout_max_ms",6E4),rj=T("gel_min_batch_size",5);let sj=void 0;class tj{constructor(){this.l=this.h=this.i=0;this.j=!1}}const uj=new tj,vj=new tj,wj=new tj,xj=new tj;let yj,zj=!0,Aj=1;const Bj=new Map,Cj=t.ytLoggingTransportTokensToCttTargetIds_||{},Dj=t.ytLoggingTransportTokensToJspbCttTargetIds_||{};let Ej={};function Fj(){let a=v("yt.logging.ims");a||(a=new nj,w("yt.logging.ims",a));return a}
function Gj(a,b){if(a.endpoint==="log_event"){Hj();var c=Ij(a),d=Jj(a.payload)||"";a:{if(S("enable_web_tiered_gel")){var e=ui[d||""];var f,g;if(Mi().resolve(new Ei)==null)var h=void 0;else{let k;h=(k=v("yt.gcf.config.hotConfigGroup"))!=null?k:R("RAW_HOT_CONFIG_GROUP");h=h==null?void 0:(f=h.loggingHotConfig)==null?void 0:(g=f.eventLoggingConfig)==null?void 0:g.payloadPolicies}if(f=h)for(g=0;g<f.length;g++)if(f[g].payloadNumber===e){e=f[g];break a}}e=void 0}f=200;if(e){if(e.enabled===!1&&!S("web_payload_policy_disabled_killswitch"))return;
f=Kj(e.tier);if(f===400){Lj(a,b);return}}Ej[c]=!0;c={cttAuthInfo:c,isJspb:!1,tier:f};Fj().storePayload(c,a.payload);Mj(b,c,d==="gelDebuggingEvent")}}
function Mj(a,b,c=!1){a&&(sj=new a);a=T("tvhtml5_logging_max_batch_ads_fork")||T("tvhtml5_logging_max_batch")||T("web_logging_max_batch")||100;const d=W(),e=Nj(!1,b.tier),f=e.l;c&&(e.j=!0);c=0;b&&(c=Fj().getSequenceCount(b));c>=1E3?Oj({writeThenSend:!0},!1,b.tier):c>=a?yj||(yj=Pj(()=>{Oj({writeThenSend:!0},!1,b.tier);yj=void 0},0)):d-f>=10&&(Qj(!1,b.tier),e.l=d)}
function Lj(a,b){if(a.endpoint==="log_event"){S("more_accurate_gel_parser")&&Fj().storePayload({isJspb:!1},a.payload);Hj();var c=Ij(a),d=new Map;d.set(c,[a.payload]);var e=Jj(a.payload)||"";b&&(sj=new b);return new L((f,g)=>{sj&&sj.isReady()?Rj(d,sj,f,g,{bypassNetworkless:!0},!0,e==="gelDebuggingEvent"):f()})}}
function Ij(a){var b="";if(a.dangerousLogToVisitorSession)b="visitorOnlyApprovedKey";else if(a.cttAuthInfo){b=a.cttAuthInfo;const c={};b.videoId?c.videoId=b.videoId:b.playlistId&&(c.playlistId=b.playlistId);Cj[a.cttAuthInfo.token]=c;b=a.cttAuthInfo.token}return b}
function Oj(a={},b=!1,c){new L((d,e)=>{const f=Nj(b,c),g=f.j;f.j=!1;Sj(f.i);Sj(f.h);f.h=0;sj&&sj.isReady()?c===void 0&&S("enable_web_tiered_gel")?Tj(d,e,a,b,300,g):Tj(d,e,a,b,c,g):(Qj(b,c),d())})}
function Tj(a,b,c={},d=!1,e=200,f=!1){var g=sj,h=new Map;const k=new Map,l={isJspb:d,cttAuthInfo:void 0,tier:e},n={isJspb:d,cttAuthInfo:void 0};if(d){for(const p of Object.keys(Ej))b=S("enable_web_tiered_gel")?Fj().smartExtractMatchingEntries({keys:[l,n],sizeLimit:1E3}):Fj().extractMatchingEntries({isJspb:!0,cttAuthInfo:p}),b.length>0&&h.set(p,b),(S("web_fp_via_jspb_and_json")&&c.writeThenSend||!S("web_fp_via_jspb_and_json"))&&delete Ej[p];Uj(h,g,a,c,f)}else{for(const p of Object.keys(Ej))h=S("enable_web_tiered_gel")?
Fj().smartExtractMatchingEntries({keys:[{isJspb:!1,cttAuthInfo:p,tier:e},{isJspb:!1,cttAuthInfo:p}],sizeLimit:1E3}):Fj().extractMatchingEntries({isJspb:!1,cttAuthInfo:p}),h.length>0&&k.set(p,h),(S("web_fp_via_jspb_and_json")&&c.writeThenSend||!S("web_fp_via_jspb_and_json"))&&delete Ej[p];Rj(k,g,a,b,c,!1,f)}}
function Qj(a=!1,b=200){const c=()=>{Oj({writeThenSend:!0},a,b)},d=Nj(a,b);
var e=d===xj||d===wj?5E3:qj;S("web_gel_timeout_cap")&&!d.h&&(e=Pj(()=>{c()},e),d.h=e);
Sj(d.i);e=R("LOGGING_BATCH_TIMEOUT",T("web_gel_debounce_ms",1E4));S("shorten_initial_gel_batch_timeout")&&zj&&(e=pj);e=Pj(()=>{T("gel_min_batch_size")>0?Fj().getSequenceCount({cttAuthInfo:void 0,isJspb:a,tier:b})>=rj&&c():c()},e);
d.i=e}
function Rj(a,b,c,d,e={},f,g){const h=Math.round(W());let k=a.size;const l=Vj(g);for(const [n,p]of a){a=n;g=p;const q=Rd({context:$h(b.config_||Zh())});if(!ja(g)&&!S("throw_err_when_logevent_malformed_killswitch")){d();break}q.events=g;(g=Cj[a])&&Wj(q,a,g);delete Cj[a];const m=a==="visitorOnlyApprovedKey";Xj(q,h,m);Yj(e);const u=G=>{S("start_client_gcf")&&xg.h(()=>r(function*(){yield Zj(G)}));
k--;k||c()};
let z=0;const y=()=>{z++;if(e.bypassNetworkless&&z===1)try{zi(b,l,q,ak({writeThenSend:!0},m,u,y,f)),zj=!1}catch(G){Df(G),d()}k--;k||c()};
try{zi(b,l,q,ak(e,m,u,y,f)),zj=!1}catch(G){Df(G),d()}}}
function Uj(a,b,c,d={},e){const f=Math.round(W()),g={value:a.size};var h=new Map([...a]);for(const [z]of h){var k=z,l=a.get(k);h=new ef;var n=b.config_||Zh(),p=new af,q=new Te;J(q,1,n.ja);J(q,2,n.ia);gd(q,16,n.Aa);J(q,17,n.innertubeContextClientVersion);if(n.X){var m=n.X,u=new Re;m.coldConfigData&&J(u,1,m.coldConfigData);m.appInstallData&&J(u,6,m.appInstallData);m.coldHashData&&J(u,3,m.coldHashData);m.hotHashData&&J(u,5,m.hotHashData);I(q,Re,62,u)}if((m=t.devicePixelRatio)&&m!=1){if(m!=null&&typeof m!==
"number")throw Error(`Value of float/double field must be a number, found ${typeof m}: ${m}`);Qc(q,65,m)}m=Sf();m!==""&&J(q,54,m);m=Tf();if(m.length>0){u=new Xe;for(let y=0;y<m.length;y++){const G=new Ue;J(G,1,m[y].key);fd(G,2,Ve,m[y].value);ad(u,15,Ue,G)}I(p,Xe,5,u)}ai(p);bi(n,q);S("start_client_gcf")&&ci(q);R("DELEGATED_SESSION_ID")&&!S("pageid_as_header_web")&&(n=new $e,J(n,3,R("DELEGATED_SESSION_ID")));!S("fill_delegate_context_in_gel_killswitch")&&(m=R("INNERTUBE_CONTEXT_SERIALIZED_DELEGATION_CONTEXT"))&&
(u=Yc(p,$e,3)||new $e,n=p,m=J(u,18,m),I(n,$e,3,m));n=q;m=R("DEVICE","");for(const [y,G]of Object.entries(Kf(m)))m=y,u=G,m==="cbrand"?J(n,12,u):m==="cmodel"?J(n,13,u):m==="cbr"?J(n,87,u):m==="cbrver"?J(n,88,u):m==="cos"?J(n,18,u):m==="cosver"?J(n,19,u):m==="cplatform"&&gd(n,42,pg(u));p.j(q);I(h,af,1,p);if(q=Dj[k])a:{if(cd(q,1))p=1;else if(q.getPlaylistId())p=2;else break a;I(h,df,4,q);q=Yc(h,af,1)||new af;n=Yc(q,$e,3)||new $e;m=new Ze;m.setToken(k);gd(m,1,p);ad(n,12,Ze,m);I(q,$e,3,n)}delete Dj[k];
k=k==="visitorOnlyApprovedKey";bk()||Qc(h,2,f==null?f:mc(f));!k&&(p=R("EVENT_ID"))&&(q=ck(),n=new cf,J(n,1,p),Qc(n,2,q==null?q:mc(q)),I(h,cf,5,n));Yj(d);if(S("jspb_serialize_with_worker")){ti||((p=R("WORKER_SERIALIZATION_URL"))?((p=p.privateDoNotAccessOrElseTrustedResourceUrlWrappedValue)?(sa===void 0&&(sa=ta()),p=(q=sa)?q.createScriptURL(p):p,p=new ua(p)):p=null,ti=p):ti=null);q=ti||void 0;if(!si&&q!==void 0){p=Worker;if(q instanceof ua)q=q.h;else throw Error("");si=new p(q,void 0)}if((p=si)&&d.writeThenSend){Bj.set(Aj,
{client:b,resolve:c,networklessOptions:d,isIsolated:!1,useVSSEndpoint:e,dangerousLogToVisitorSession:k,requestsOutstanding:g});a=p;b=a.postMessage;c=zc(h);b.call(a,{op:"gelBatchToSerialize",batchRequest:c,clientEvents:l,key:Aj});Aj++;break}}if(l){p=[];for(q=0;q<l.length;q++)try{p.push(new bf(l[q]))}catch(y){Df(new O("Transport failed to deserialize "+String(l[q])))}l=p}else l=[];for(const y of l)ad(h,3,bf,y);l={startTime:W(),ticks:{},infos:{}};h=JSON.stringify(zc(h));l.ticks.geljspc=W();S("log_jspb_serialize_latency")&&
Math.random()<.001&&li("meta_logging_csi_event",{timerName:"gel_jspb_serialize",Tb:l});dk(h,b,c,d,e,k,g)}}
function dk(a,b,c,d={},e,f,g={value:0}){e=Vj(e);d=ak(d,f,h=>{S("start_client_gcf")&&xg.h(()=>r(function*(){yield Zj(h)}));
g.value--;g.value||c()},()=>{g.value--;
g.value||c()},!1);
d.headers["Content-Type"]="application/json+protobuf";d.postBodyFormat="JSPB";d.postBody=a;zi(b,e,"",d);zj=!1}
function Yj(a){S("always_send_and_write")&&(a.writeThenSend=!1)}
function ak(a,b,c,d,e){a={retry:!0,onSuccess:c,onError:d,networklessOptions:a,dangerousLogToVisitorSession:b,sb:!!e,headers:{},postBodyFormat:"",postBody:"",compress:S("compress_gel")||S("compress_gel_lr")};bk()&&(a.headers["X-Goog-Request-Time"]=JSON.stringify(Math.round(W())));return a}
function Xj(a,b,c){bk()||(a.requestTimeMs=String(b));S("unsplit_gel_payloads_in_logs")&&(a.unsplitGelPayloadsInLogs=!0);!c&&(b=R("EVENT_ID"))&&(c=ck(),a.serializedClientEventId={serializedEventId:b,clientCounter:String(c)})}
function ck(){let a=R("BATCH_CLIENT_COUNTER")||0;a||(a=Math.floor(Math.random()*65535/2));a++;a>65535&&(a=1);Q("BATCH_CLIENT_COUNTER",a);return a}
function Wj(a,b,c){let d;if(c.videoId)d="VIDEO";else if(c.playlistId)d="PLAYLIST";else return;a.credentialTransferTokenTargetId=c;a.context=a.context||{};a.context.user=a.context.user||{};a.context.user.credentialTransferTokens=[{token:b,scope:d}]}
function Hj(){var a=Rf("il_payload_scraping");a=(a!==void 0?String(a):"")==="enable_il_payload_scraping";!v("yt.logging.transport.enableScrapingForTest")&&a&&(jj=[],w("yt.logging.transport.enableScrapingForTest",!0),w("yt.logging.transport.scrapedPayloadsForTesting",jj),w("yt.logging.transport.payloadToScrape","visualElementShown visualElementHidden visualElementAttached screenCreated visualElementGestured visualElementStateChanged".split(" ")),w("yt.logging.transport.getScrapedPayloadFromClientEventsFunction"),
w("yt.logging.transport.scrapeClientEvent",!0))}
function bk(){return S("use_request_time_ms_header")||S("lr_use_request_time_ms_header")}
function Pj(a,b){return S("transport_use_scheduler")===!1?Of(a,b):S("logging_avoid_blocking_during_navigation")||S("lr_logging_avoid_blocking_during_navigation")?rg(()=>{gj().currentState==="none"?a():gj().install({none:{callback:a}})},b):rg(a,b)}
function Sj(a){S("transport_use_scheduler")?xg.P(a):window.clearTimeout(a)}
function Zj(a){return r(function*(){var b,c=a==null?void 0:(b=a.responseContext)==null?void 0:b.globalConfigGroup;b=oj(c,Pe);const d=c==null?void 0:c.hotHashData,e=oj(c,Oe);c=c==null?void 0:c.coldHashData;const f=Mi().resolve(new Ei);f&&(d&&(b?yield Vh(f,d,b):yield Vh(f,d)),c&&(e?yield Wh(f,c,e):yield Wh(f,c)))})}
function Nj(a,b=200){return a?b===300?xj:vj:b===300?wj:uj}
function Jj(a){a=Object.keys(a);for(const b of a)if(ui[b])return b}
function Kj(a){switch(a){case "DELAYED_EVENT_TIER_UNSPECIFIED":return 0;case "DELAYED_EVENT_TIER_DEFAULT":return 100;case "DELAYED_EVENT_TIER_DISPATCH_TO_EMPTY":return 200;case "DELAYED_EVENT_TIER_FAST":return 300;case "DELAYED_EVENT_TIER_IMMEDIATE":return 400;default:return 200}}
function Vj(a=!1){return a&&S("vss_through_gel_video_stats")?"video_stats":"log_event"}
;const ek=t.ytLoggingGelSequenceIdObj_||{};
function fk(a,b,c,d={}){const e={},f=Math.round(d.timestamp||W());e.eventTimeMs=f<Number.MAX_SAFE_INTEGER?f:0;e[a]=b;a=v("_lact",window);a=a==null?-1:Math.max(Date.now()-a,0);e.context={lastActivityMs:String(d.timestamp||!isFinite(a)?-1:a)};d.sequenceGroup&&!S("web_gel_sequence_info_killswitch")&&(a=e.context,b=d.sequenceGroup,ek[b]=b in ek?ek[b]+1:0,a.sequence={index:ek[b],groupKey:b},d.endOfSequence&&delete ek[d.sequenceGroup]);S("web_tag_automated_log_events")&&(e.context.automatedLogEventSource=
d.automatedLogEventSource);(d.sendIsolatedPayload?Lj:Gj)({endpoint:"log_event",payload:e,cttAuthInfo:d.cttAuthInfo,dangerousLogToVisitorSession:d.dangerousLogToVisitorSession},c)}
function gk(a=!1){Oj(void 0,a)}
;let hk=[];function Y(a,b,c={}){let d=Ai;R("ytLoggingEventsDefaultDisabled",!1)&&Ai===Ai&&(d=null);fk(a,b,d,c)}
;var ik=new Set,jk=0,kk=0,lk=0,mk=[];const nk=[],ok=["PhantomJS","Googlebot","TO STOP THIS SECURITY SCAN go/scan"];function pk(a){qk(a)}
function rk(a){qk(a,"WARNING")}
function qk(a,b="ERROR"){var c={};c.name=R("INNERTUBE_CONTEXT_CLIENT_NAME",1);c.version=R("INNERTUBE_CONTEXT_CLIENT_VERSION");sk(a,c,b)}
function sk(a,b,c="ERROR"){if(a){a.hasOwnProperty("level")&&a.level&&(c=a.level);if(S("console_log_js_exceptions")){var d=[];d.push(`Name: ${a.name}`);d.push(`Message: ${a.message}`);a.hasOwnProperty("params")&&d.push(`Error Params: ${JSON.stringify(a.params)}`);a.hasOwnProperty("args")&&d.push(`Error args: ${JSON.stringify(a.args)}`);d.push(`File name: ${a.fileName}`);d.push(`Stacktrace: ${a.stack}`);window.console.log(d.join("\n"),a)}if(!(jk>=5)){d=[];for(e of nk)try{e()&&d.push(e())}catch(u){}var e=
d;e=[...mk,...e];var f=Ba(a);d=f.message||"Unknown Error";const q=f.name||"UnknownError";var g=f.stack||a.i||"Not available";if(g.startsWith(`${q}: ${d}`)){var h=g.split("\n");h.shift();g=h.join("\n")}h=f.lineNumber||"Not available";f=f.fileName||"Not available";let m=0;if(a.hasOwnProperty("args")&&a.args&&a.args.length)for(var k=0;k<a.args.length&&!(m=ig(a.args[k],`params.${k}`,b,m),m>=500);k++);else if(a.hasOwnProperty("params")&&a.params){const u=a.params;if(typeof a.params==="object")for(k in u){if(!u[k])continue;
const z=`params.${k}`,y=kg(u[k]);b[z]=y;m+=z.length+y.length;if(m>500)break}else b.params=kg(u)}if(e.length)for(k=0;k<e.length&&!(m=ig(e[k],`params.context.${k}`,b,m),m>=500);k++);navigator.vendor&&!b.hasOwnProperty("vendor")&&(b["device.vendor"]=navigator.vendor);b={message:d,name:q,lineNumber:h,fileName:f,stack:g,params:b,sampleWeight:1};k=Number(a.columnNumber);isNaN(k)||(b.lineNumber=`${b.lineNumber}:${k}`);if(a.level==="IGNORED")var l=0;else a:{a=bg();for(l of a.G)if(b.message&&b.message.match(l.Fa)){l=
l.weight;break a}for(var n of a.F)if(n.callback(b)){l=n.weight;break a}l=1}b.sampleWeight=l;l=b;for(var p of Yf)if(p.S[l.name]){n=p.S[l.name];for(const u of n)if(n=l.message.match(u.v)){l.params["params.error.original"]=n[0];a=u.groups;b={};for(k=0;k<a.length;k++)b[a[k]]=n[k+1],l.params[`params.error.${a[k]}`]=n[k+1];l.message=p.Z(b);break}}l.params||(l.params={});p=bg();l.params["params.errorServiceSignature"]=`msg=${p.G.length}&cb=${p.F.length}`;l.params["params.serviceWorker"]="true";t.document&&
t.document.querySelectorAll&&(l.params["params.fscripts"]=String(document.querySelectorAll("script:not([nonce])").length));(new Ud(Vd,"sample")).constructor!==Ud&&(l.params["params.fconst"]="true");window.yterr&&typeof window.yterr==="function"&&window.yterr(l);l.sampleWeight===0||ik.has(l.message)||tk(l,c)}}}
function tk(a,b="ERROR"){if(b==="ERROR"){fg.D("handleError",a);if(S("record_app_crashed_web")&&lk===0&&a.sampleWeight===1){lk++;var c={appCrashType:"APP_CRASH_TYPE_BREAKPAD"};S("report_client_error_with_app_crash_ks")||(c.systemHealth={crashData:{clientError:{logMessage:{message:a.message}}}});Y("appCrashed",c)}kk++}else b==="WARNING"&&fg.D("handleWarning",a);c={};b:{for(e of ok){var d=Sa();if(d&&d.toLowerCase().indexOf(e.toLowerCase())>=0){var e=!0;break b}}e=!1}if(e)c=void 0;else{d={stackTrace:a.stack};
a.fileName&&(d.filename=a.fileName);e=a.lineNumber&&a.lineNumber.split?a.lineNumber.split(":"):[];e.length!==0&&(e.length!==1||isNaN(Number(e[0]))?e.length!==2||isNaN(Number(e[0]))||isNaN(Number(e[1]))||(d.lineNumber=Number(e[0]),d.columnNumber=Number(e[1])):d.lineNumber=Number(e[0]));e={level:"ERROR_LEVEL_UNKNOWN",message:a.message,errorClassName:a.name,sampleWeight:a.sampleWeight};b==="ERROR"?e.level="ERROR_LEVEL_ERROR":b==="WARNING"&&(e.level="ERROR_LEVEL_WARNNING");d={isObfuscated:!0,browserStackInfo:d};
c.pageUrl=window.location.href;c.kvPairs=[];R("FEXP_EXPERIMENTS")&&(c.experimentIds=R("FEXP_EXPERIMENTS"));var f=R("LATEST_ECATCHER_SERVICE_TRACKING_PARAMS");const k=zf.EXPERIMENT_FLAGS;if((!k||!k.web_disable_gel_stp_ecatcher_killswitch)&&f)for(var g of Object.keys(f))c.kvPairs.push({key:g,value:String(f[g])});if(g=a.params)for(var h of Object.keys(g))c.kvPairs.push({key:`client.${h}`,value:String(g[h])});h=R("SERVER_NAME");g=R("SERVER_VERSION");h&&g&&(c.kvPairs.push({key:"server.name",value:h}),
c.kvPairs.push({key:"server.version",value:g}));c={errorMetadata:c,stackTrace:d,logMessage:e}}if(c&&(Y("clientError",c),b==="ERROR"||S("errors_flush_gel_always_killswitch")))a:{if(S("web_fp_via_jspb")){b=hk;hk=[];if(b)for(const k of b)fk(k.N,k.payload,Ai,k.options);gk(!0);if(!S("web_fp_via_jspb_and_json"))break a}gk()}try{ik.add(a.message)}catch(k){}jk++}
function uk(a,...b){a.args||(a.args=[]);Array.isArray(a.args)&&a.args.push(...b)}
;function vk(a){return r(function*(){var b=yield t.fetch(a.i);if(b.status!==200)return Promise.reject("Server error when retrieving AmbientData");b=yield b.text();if(!b.startsWith(")]}'\n"))return Promise.reject("Incorrect JSPB formatting");a:{b=JSON.parse(b.substring(5));for(let c=0;c<b.length;c++)if(b[c][0]==="yt.sw.adr"){b=new vf(b[c]);break a}b=null}return b?b:Promise.reject("AmbientData missing from response")})}
function wk(a=!1){const b=xk.instance;return r(function*(){if(a||!b.h)b.h=vk(b).then(b.j).catch(c=>{delete b.h;qk(c)});
return b.h})}
var xk=class{constructor(){this.i=yk("/sw.js_data")}j(a){const b=Yc(a,uf,2,Lb);if(b){var c=bd(b,5);c&&(t.__SAPISID=c);E(F(b,10))!=null?Q("EOM_VISITOR_DATA",bd(b,10)):E(F(b,7))!=null&&Q("VISITOR_DATA",bd(b,7));if(lc(F(b,4))!=null){c=String;let e;var d=(e=lc(F(b,4)))!=null?e:0;Q("SESSION_INDEX",c(d))}E(F(b,8))!=null&&Q("DELEGATED_SESSION_ID",bd(b,8));E(F(b,12))!=null&&Q("USER_SESSION_ID",bd(b,12));E(F(b,11))!=null&&Q("INNERTUBE_CONTEXT_SERIALIZED_DELEGATION_CONTEXT",bd(b,11))}return a}};function zk(a,b){b.encryptedTokenJarContents&&(a.h[b.encryptedTokenJarContents]=b,typeof b.expirationSeconds==="string"&&setTimeout(()=>{delete a.h[b.encryptedTokenJarContents]},Number(b.expirationSeconds)*1E3))}
var Ak=class{constructor(){this.h={}}handleResponse(a,b){if(!b)throw Error("request needs to be passed into ConsistencyService");let c,d;b=((c=b.H.context)==null?void 0:(d=c.request)==null?void 0:d.consistencyTokenJars)||[];let e;if(a=(e=a.responseContext)==null?void 0:e.consistencyTokenJar){for(const f of b)delete this.h[f.encryptedTokenJarContents];zk(this,a)}}};let Bk=Date.now().toString();function Ck(){if(window.crypto&&window.crypto.getRandomValues)try{var a=Array(16),b=new Uint8Array(16);window.crypto.getRandomValues(b);for(var c=0;c<a.length;c++)a[c]=b[c];return a}catch(d){}a=Array(16);for(b=0;b<16;b++){c=Date.now();for(let d=0;d<c%23;d++)a[b]=Math.random();a[b]=Math.floor(Math.random()*256)}if(Bk)for(b=1,c=0;c<Bk.length;c++)a[b%16]^=a[(b-1)%16]/4^Bk.charCodeAt(c),b++;return a}
;var Dk;let Ek=t.ytLoggingDocDocumentNonce_;if(!Ek){const a=Ck(),b=[];for(let c=0;c<a.length;c++)b.push("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_".charAt(a[c]&63));Ek=b.join("")}Dk=Ek;var Fk={Ra:0,Oa:1,Qa:2,bb:3,Sa:4,ob:5,cb:6,kb:7,ib:8,jb:9,nb:10,hb:11,0:"DEFAULT",1:"CHAT",2:"CONVERSATIONS",3:"MINIPLAYER",4:"DIALOG",5:"VOZ",6:"MUSIC_WATCH_TABS",7:"SHARE",8:"PUSH_NOTIFICATIONS",9:"RICH_GRID_WATCH",10:"UNPLUGGED_BROWSE",11:"PICTURE_IN_PICTURE"};let Gk=1;function Hk(a){return new Ik({trackingParams:a})}
function Jk(a,b,c,d,e,f){const g=Gk++;return new Ik({veType:a,veCounter:g,elementIndex:c,dataElement:b,youtubeData:d,jspbYoutubeData:e,loggingDirectives:f})}
var Ik=class{constructor(a){this.h=a}getAsJson(){const a={};this.h.trackingParams!==void 0?a.trackingParams=this.h.trackingParams:(a.veType=this.h.veType,this.h.veCounter!==void 0&&(a.veCounter=this.h.veCounter),this.h.elementIndex!==void 0&&(a.elementIndex=this.h.elementIndex));this.h.dataElement!==void 0&&(a.dataElement=this.h.dataElement.getAsJson());this.h.youtubeData!==void 0&&(a.youtubeData=this.h.youtubeData);this.h.isCounterfactual&&(a.isCounterfactual=!0);return a}getAsJspb(){const a=new We;
this.h.trackingParams!==void 0?a.setTrackingParams(this.h.trackingParams):(this.h.veType!==void 0&&ed(a,2,this.h.veType),this.h.veCounter!==void 0&&ed(a,6,this.h.veCounter),this.h.elementIndex!==void 0&&ed(a,3,this.h.elementIndex),this.h.isCounterfactual&&Qc(a,5,!0));if(this.h.dataElement!==void 0){var b=this.h.dataElement.getAsJspb();I(a,We,7,b)}this.h.youtubeData!==void 0&&I(a,Qe,8,this.h.jspbYoutubeData);return a}toString(){return JSON.stringify(this.getAsJson())}isClientVe(){return!this.h.trackingParams&&
!!this.h.veType}getLoggingDirectives(){return this.h.loggingDirectives}};function Kk(a=0){return R("client-screen-nonce-store",{})[a]}
function Lk(a,b=0){let c=R("client-screen-nonce-store");c||(c={},Q("client-screen-nonce-store",c));c[b]=a}
function Mk(a=0){return a===0?"ROOT_VE_TYPE":`${"ROOT_VE_TYPE"}.${a}`}
function Nk(a=0){return R(Mk(a))}
function Ok(a=0){return(a=Nk(a))?new Ik({veType:a,youtubeData:void 0,jspbYoutubeData:void 0}):null}
function Pk(){let a=R("csn-to-ctt-auth-info");a||(a={},Q("csn-to-ctt-auth-info",a));return a}
function Qk(){return Object.values(R("client-screen-nonce-store",{})).filter(a=>a!==void 0)}
function Z(a=0){a=Kk(a);if(!a&&!R("USE_CSN_FALLBACK",!0))return null;a||(a="UNDEFINED_CSN");return a?a:null}
function Rk(a){for(const b of Object.values(Fk))if(Z(b)===a)return!0;return!1}
function Sk(a,b,c){const d=Pk();(c=Z(c))&&delete d[c];b&&(d[a]=b)}
function Tk(a){return Pk()[a]}
function Uk(a,b,c=0,d){if(a!==Kk(c)||b!==R(Mk(c)))if(Sk(a,d,c),Lk(a,c),Q(Mk(c),b),b=()=>{setTimeout(()=>{a&&Y("foregroundHeartbeatScreenAssociated",{clientDocumentNonce:Dk,clientScreenNonce:a})},0)},"requestAnimationFrame"in window)try{window.requestAnimationFrame(b)}catch(e){b()}else b()}
;function Vk(){var a=R("INNERTUBE_CONTEXT");if(!a)return qk(Error("Error: No InnerTubeContext shell provided in ytconfig.")),{};a=Rd(a);S("web_no_tracking_params_in_shell_killswitch")||delete a.clickTracking;a.client||(a.client={});var b=a.client;b.utcOffsetMinutes=-Math.floor((new Date).getTimezoneOffset());var c=Sf();c?b.experimentsToken=c:delete b.experimentsToken;Ak.instance||(Ak.instance=new Ak);b=Ak.instance.h;c=[];let d=0;for(var e in b)c[d++]=b[e];a.request=Object.assign({},a.request,{consistencyTokenJars:c});
a.user=Object.assign({},a.user);if(e=R("INNERTUBE_CONTEXT_SERIALIZED_DELEGATION_CONTEXT"))a.user.serializedDelegationContext=e;return a}
;function Wk(a){var b=a;if(a=R("INNERTUBE_HOST_OVERRIDE")){a=String(a);var c=String,d=b.match(Fa);b=d[5];var e=d[6];d=d[7];let f="";b&&(f+=b);e&&(f+="?"+e);d&&(f+="#"+d);b=a+c(f)}return b}
;function Xk(a){const b={"Content-Type":"application/json"};R("EOM_VISITOR_DATA")?b["X-Goog-EOM-Visitor-Id"]=R("EOM_VISITOR_DATA"):R("VISITOR_DATA")&&(b["X-Goog-Visitor-Id"]=R("VISITOR_DATA"));b["X-Youtube-Bootstrap-Logged-In"]=R("LOGGED_IN",!1);R("DEBUG_SETTINGS_METADATA")&&(b["X-Debug-Settings-Metadata"]=R("DEBUG_SETTINGS_METADATA"));if(a!=="cors"){var c=R("INNERTUBE_CONTEXT_CLIENT_NAME");c&&(b["X-Youtube-Client-Name"]=c);(c=R("INNERTUBE_CONTEXT_CLIENT_VERSION"))&&(b["X-Youtube-Client-Version"]=
c);(c=R("CHROME_CONNECTED_HEADER"))&&(b["X-Youtube-Chrome-Connected"]=c);(c=R("DOMAIN_ADMIN_STATE"))&&(b["X-Youtube-Domain-Admin-State"]=c)}R("SEND_LAVA_HEADER_UNCONDITIONALLY",!1)?(a=R("SERIALIZED_LAVA_DEVICE_CONTEXT"))&&(b["X-YouTube-Lava-Device-Context"]=a):a!=="cors"&&R("ENABLE_LAVA_HEADER_ON_IT_EXPANSION")&&(a=R("SERIALIZED_LAVA_DEVICE_CONTEXT"))&&(b["X-YouTube-Lava-Device-Context"]=a);return b}
;var Yk=class{constructor(){this.h={}}get(a){if(Object.prototype.hasOwnProperty.call(this.h,a))return this.h[a]}set(a,b){this.h[a]=b}remove(a){delete this.h[a]}};new class{constructor(){this.mappings=new Yk}get(a){a:{var b=this.mappings.get(a.toString());switch(b.type){case "mapping":a=b.value;break a;case "factory":b=b.value();this.mappings.set(a.toString(),{type:"mapping",value:b});a=b;break a;default:a=va(b,void 0)}}return a}};var Zk=class{},$k=class extends Zk{};const al={GET_DATASYNC_IDS:function(a){return()=>new a}(class extends $k{})};class gi extends ei{constructor(a){super(arguments);this.csn=a}}const oi=new fi,bl=[];let dl=cl,el=0;const fl=new Map,gl=new Map,hl=new Map;
function il(a,b,c,d,e,f,g,h){const k=dl(),l=new Ik({veType:b,youtubeData:f,jspbYoutubeData:void 0});f=jl({},k);e&&(f.cttAuthInfo=e);e={csn:k,pageVe:l.getAsJson()};S("expectation_logging")&&h&&h.screenCreatedLoggingExpectations&&(e.screenCreatedLoggingExpectations=h.screenCreatedLoggingExpectations);c&&c.visualElement?(e.implicitGesture={parentCsn:c.clientScreenNonce,gesturedVe:c.visualElement.getAsJson()},g&&(e.implicitGesture.gestureType=g)):c&&rk(new O("newScreen() parent element does not have a VE - rootVe",
b));d&&(e.cloneCsn=d);a?fk("screenCreated",e,a,f):Y("screenCreated",e,f);li(oi,new gi(k));fl.clear();gl.clear();hl.clear();return k}
function kl(a,b,c,d,e=!1,f={}){ll(a,b,c,[d],e,f)}
function ll(a,b,c,d,e=!1,f={}){Object.assign(f,jl({cttAuthInfo:Tk(b)||void 0},b));for(const h of d){var g=h.getAsJson();(Qd(g)||!g.trackingParams&&!g.veType)&&rk(Error("Child VE logged with no data"));if(S("no_client_ve_attach_unless_shown")){const k=ml(h,b);if(g.veType&&!gl.has(k)&&!hl.has(k)&&!e){if(!S("il_attach_cache_limit")||fl.size<1E3){fl.set(k,[a,b,c,h]);return}S("il_attach_cache_limit")&&fl.size>1E3&&rk(new O("IL Attach cache exceeded limit"))}g=ml(c,b);fl.has(g)?nl(c,b):hl.set(g,!0)}}d=
d.filter(h=>{h.csn!==b?(h.csn=b,h=!0):h=!1;return h});
c={csn:b,parentVe:c.getAsJson(),childVes:xa(d,h=>h.getAsJson())};
b==="UNDEFINED_CSN"?ol("visualElementAttached",f,c):a?fk("visualElementAttached",c,a,f):Y("visualElementAttached",c,f)}
function pl(a,b,c,d,e){ql(a,b,c,e)}
function ql(a,b,c,d){rl(c,b);const e=jl({cttAuthInfo:Tk(b)||void 0},b);c={csn:b,ve:c.getAsJson(),eventType:1};d&&(c.clientData=d);b==="UNDEFINED_CSN"?ol("visualElementShown",e,c):a?fk("visualElementShown",c,a,e):Y("visualElementShown",c,e)}
function sl(a,b,c,d=!1){const e=d?16:8;d=jl({cttAuthInfo:Tk(b)||void 0,endOfSequence:d},b);c={csn:b,ve:c.getAsJson(),eventType:e};b==="UNDEFINED_CSN"?ol("visualElementHidden",d,c):a?fk("visualElementHidden",c,a,d):Y("visualElementHidden",c,d)}
function tl(a,b,c,d){var e=void 0;rl(c,b);e=e||"INTERACTION_LOGGING_GESTURE_TYPE_GENERIC_CLICK";const f=jl({cttAuthInfo:Tk(b)||void 0},b);c={csn:b,ve:c.getAsJson(),gestureType:e};d&&(c.clientData=d);b==="UNDEFINED_CSN"?ol("visualElementGestured",f,c):a?fk("visualElementGestured",c,a,f):Y("visualElementGestured",c,f)}
function cl(){let a;a=Ck();const b=[];for(let c=0;c<a.length;c++)b.push("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_".charAt(a[c]&63));return b.join("")}
function ol(a,b,c){bl.push({N:a,payload:c,Ab:void 0,options:b});el||(el=pi())}
function qi(a){if(bl){for(const b of bl)b.payload&&(b.payload.csn=a.csn,Y(b.N,b.payload,b.options));bl.length=0}el=0}
function ml(a,b){return`${a.getAsJson().veType}${a.getAsJson().veCounter}${b}`}
function rl(a,b){if(S("no_client_ve_attach_unless_shown")){var c=ml(a,b);gl.set(c,!0);nl(a,b)}}
function nl(a,b){a=ml(a,b);fl.has(a)&&(b=fl.get(a)||[],kl(b[0],b[1],b[2],b[3],!0),fl.delete(a))}
function jl(a,b){S("log_sequence_info_on_gel_web")&&(a.sequenceGroup=b);return a}
;Object.assign({auto_search:"LATENCY_ACTION_AUTO_SEARCH",ad_to_ad:"LATENCY_ACTION_AD_TO_AD",ad_to_video:"LATENCY_ACTION_AD_TO_VIDEO",app_startup:"LATENCY_ACTION_APP_STARTUP",browse:"LATENCY_ACTION_BROWSE",cast_splash:"LATENCY_ACTION_CAST_SPLASH",channel_activity:"LATENCY_ACTION_KIDS_CHANNEL_ACTIVITY",channels:"LATENCY_ACTION_CHANNELS",chips:"LATENCY_ACTION_CHIPS",commerce_transaction:"LATENCY_ACTION_COMMERCE_TRANSACTION",direct_playback:"LATENCY_ACTION_DIRECT_PLAYBACK",editor:"LATENCY_ACTION_EDITOR",
embed:"LATENCY_ACTION_EMBED",embed_no_video:"LATENCY_ACTION_EMBED_NO_VIDEO",entity_key_serialization_perf:"LATENCY_ACTION_ENTITY_KEY_SERIALIZATION_PERF",entity_key_deserialization_perf:"LATENCY_ACTION_ENTITY_KEY_DESERIALIZATION_PERF",explore:"LATENCY_ACTION_EXPLORE",favorites:"LATENCY_ACTION_FAVORITES",home:"LATENCY_ACTION_HOME",inboarding:"LATENCY_ACTION_INBOARDING",landing:"LATENCY_ACTION_LANDING",learning:"LATENCY_ACTION_LEARNING",learning_journey_browse:"LATENCY_ACTION_LEARNING_JOURNEY_BROWSE",
learning_journey_watch:"LATENCY_ACTION_LEARNING_JOURNEY_WATCH",library:"LATENCY_ACTION_LIBRARY",live:"LATENCY_ACTION_LIVE",live_pagination:"LATENCY_ACTION_LIVE_PAGINATION",management:"LATENCY_ACTION_MANAGEMENT",mini_app:"LATENCY_ACTION_MINI_APP_PLAY",notification_settings:"LATENCY_ACTION_KIDS_NOTIFICATION_SETTINGS",onboarding:"LATENCY_ACTION_ONBOARDING",parent_profile_settings:"LATENCY_ACTION_KIDS_PARENT_PROFILE_SETTINGS",parent_tools_collection:"LATENCY_ACTION_PARENT_TOOLS_COLLECTION",parent_tools_dashboard:"LATENCY_ACTION_PARENT_TOOLS_DASHBOARD",
player_att:"LATENCY_ACTION_PLAYER_ATTESTATION",prebuffer:"LATENCY_ACTION_PREBUFFER",prefetch:"LATENCY_ACTION_PREFETCH",profile_settings:"LATENCY_ACTION_KIDS_PROFILE_SETTINGS",profile_switcher:"LATENCY_ACTION_LOGIN",projects:"LATENCY_ACTION_PROJECTS",reel_watch:"LATENCY_ACTION_REEL_WATCH",results:"LATENCY_ACTION_RESULTS",red:"LATENCY_ACTION_PREMIUM_PAGE_GET_BROWSE",premium:"LATENCY_ACTION_PREMIUM_PAGE_GET_BROWSE",privacy_policy:"LATENCY_ACTION_KIDS_PRIVACY_POLICY",review:"LATENCY_ACTION_REVIEW",search_overview_answer:"LATENCY_ACTION_SEARCH_OVERVIEW_ANSWER",
search_ui:"LATENCY_ACTION_SEARCH_UI",search_suggest:"LATENCY_ACTION_SUGGEST",search_zero_state:"LATENCY_ACTION_SEARCH_ZERO_STATE",secret_code:"LATENCY_ACTION_KIDS_SECRET_CODE",seek:"LATENCY_ACTION_PLAYER_SEEK",settings:"LATENCY_ACTION_SETTINGS",store:"LATENCY_ACTION_STORE",supervision_dashboard:"LATENCY_ACTION_KIDS_SUPERVISION_DASHBOARD",tenx:"LATENCY_ACTION_TENX",video_preview:"LATENCY_ACTION_VIDEO_PREVIEW",video_to_ad:"LATENCY_ACTION_VIDEO_TO_AD",watch:"LATENCY_ACTION_WATCH",watch_it_again:"LATENCY_ACTION_KIDS_WATCH_IT_AGAIN",
"watch,watch7":"LATENCY_ACTION_WATCH","watch,watch7_html5":"LATENCY_ACTION_WATCH","watch,watch7ad":"LATENCY_ACTION_WATCH","watch,watch7ad_html5":"LATENCY_ACTION_WATCH",wn_comments:"LATENCY_ACTION_LOAD_COMMENTS",ww_rqs:"LATENCY_ACTION_WHO_IS_WATCHING",voice_assistant:"LATENCY_ACTION_VOICE_ASSISTANT",cast_load_by_entity_to_watch:"LATENCY_ACTION_CAST_LOAD_BY_ENTITY_TO_WATCH",networkless_performance:"LATENCY_ACTION_NETWORKLESS_PERFORMANCE",gel_compression:"LATENCY_ACTION_GEL_COMPRESSION",gel_jspb_serialize:"LATENCY_ACTION_GEL_JSPB_SERIALIZE",
attestation_challenge_fetch:"LATENCY_ACTION_ATTESTATION_CHALLENGE_FETCH"},{"analytics.explore":"LATENCY_ACTION_CREATOR_ANALYTICS_EXPLORE","artist.analytics":"LATENCY_ACTION_CREATOR_ARTIST_ANALYTICS","artist.events":"LATENCY_ACTION_CREATOR_ARTIST_CONCERTS","artist.presskit":"LATENCY_ACTION_CREATOR_ARTIST_PROFILE","asset.claimed_videos":"LATENCY_ACTION_CREATOR_CMS_ASSET_CLAIMED_VIDEOS","asset.composition":"LATENCY_ACTION_CREATOR_CMS_ASSET_COMPOSITION","asset.composition_ownership":"LATENCY_ACTION_CREATOR_CMS_ASSET_COMPOSITION_OWNERSHIP",
"asset.composition_policy":"LATENCY_ACTION_CREATOR_CMS_ASSET_COMPOSITION_POLICY","asset.embeds":"LATENCY_ACTION_CREATOR_CMS_ASSET_EMBEDS","asset.history":"LATENCY_ACTION_CREATOR_CMS_ASSET_HISTORY","asset.issues":"LATENCY_ACTION_CREATOR_CMS_ASSET_ISSUES","asset.licenses":"LATENCY_ACTION_CREATOR_CMS_ASSET_LICENSES","asset.metadata":"LATENCY_ACTION_CREATOR_CMS_ASSET_METADATA","asset.ownership":"LATENCY_ACTION_CREATOR_CMS_ASSET_OWNERSHIP","asset.policy":"LATENCY_ACTION_CREATOR_CMS_ASSET_POLICY","asset.references":"LATENCY_ACTION_CREATOR_CMS_ASSET_REFERENCES",
"asset.shares":"LATENCY_ACTION_CREATOR_CMS_ASSET_SHARES","asset.sound_recordings":"LATENCY_ACTION_CREATOR_CMS_ASSET_SOUND_RECORDINGS","asset_group.assets":"LATENCY_ACTION_CREATOR_CMS_ASSET_GROUP_ASSETS","asset_group.campaigns":"LATENCY_ACTION_CREATOR_CMS_ASSET_GROUP_CAMPAIGNS","asset_group.claimed_videos":"LATENCY_ACTION_CREATOR_CMS_ASSET_GROUP_CLAIMED_VIDEOS","asset_group.metadata":"LATENCY_ACTION_CREATOR_CMS_ASSET_GROUP_METADATA","song.analytics":"LATENCY_ACTION_CREATOR_SONG_ANALYTICS",creator_channel_dashboard:"LATENCY_ACTION_CREATOR_CHANNEL_DASHBOARD",
"channel.analytics":"LATENCY_ACTION_CREATOR_CHANNEL_ANALYTICS","channel.comments":"LATENCY_ACTION_CREATOR_CHANNEL_COMMENTS","channel.content":"LATENCY_ACTION_CREATOR_POST_LIST","channel.content.promotions":"LATENCY_ACTION_CREATOR_PROMOTION_LIST","channel.copyright":"LATENCY_ACTION_CREATOR_CHANNEL_COPYRIGHT","channel.editing":"LATENCY_ACTION_CREATOR_CHANNEL_EDITING","channel.monetization":"LATENCY_ACTION_CREATOR_CHANNEL_MONETIZATION","channel.music":"LATENCY_ACTION_CREATOR_CHANNEL_MUSIC","channel.music_storefront":"LATENCY_ACTION_CREATOR_CHANNEL_MUSIC_STOREFRONT",
"channel.playlists":"LATENCY_ACTION_CREATOR_CHANNEL_PLAYLISTS","channel.translations":"LATENCY_ACTION_CREATOR_CHANNEL_TRANSLATIONS","channel.videos":"LATENCY_ACTION_CREATOR_CHANNEL_VIDEOS","channel.live_streaming":"LATENCY_ACTION_CREATOR_LIVE_STREAMING","dialog.copyright_strikes":"LATENCY_ACTION_CREATOR_DIALOG_COPYRIGHT_STRIKES","dialog.video_copyright":"LATENCY_ACTION_CREATOR_DIALOG_VIDEO_COPYRIGHT","dialog.uploads":"LATENCY_ACTION_CREATOR_DIALOG_UPLOADS",owner:"LATENCY_ACTION_CREATOR_CMS_DASHBOARD",
"owner.allowlist":"LATENCY_ACTION_CREATOR_CMS_ALLOWLIST","owner.analytics":"LATENCY_ACTION_CREATOR_CMS_ANALYTICS","owner.art_tracks":"LATENCY_ACTION_CREATOR_CMS_ART_TRACKS","owner.assets":"LATENCY_ACTION_CREATOR_CMS_ASSETS","owner.asset_groups":"LATENCY_ACTION_CREATOR_CMS_ASSET_GROUPS","owner.bulk":"LATENCY_ACTION_CREATOR_CMS_BULK_HISTORY","owner.campaigns":"LATENCY_ACTION_CREATOR_CMS_CAMPAIGNS","owner.channel_invites":"LATENCY_ACTION_CREATOR_CMS_CHANNEL_INVITES","owner.channels":"LATENCY_ACTION_CREATOR_CMS_CHANNELS",
"owner.claimed_videos":"LATENCY_ACTION_CREATOR_CMS_CLAIMED_VIDEOS","owner.claims":"LATENCY_ACTION_CREATOR_CMS_MANUAL_CLAIMING","owner.claims.manual":"LATENCY_ACTION_CREATOR_CMS_MANUAL_CLAIMING","owner.delivery":"LATENCY_ACTION_CREATOR_CMS_CONTENT_DELIVERY","owner.delivery_templates":"LATENCY_ACTION_CREATOR_CMS_DELIVERY_TEMPLATES","owner.issues":"LATENCY_ACTION_CREATOR_CMS_ISSUES","owner.licenses":"LATENCY_ACTION_CREATOR_CMS_LICENSES","owner.pitch_music":"LATENCY_ACTION_CREATOR_CMS_PITCH_MUSIC","owner.policies":"LATENCY_ACTION_CREATOR_CMS_POLICIES",
"owner.releases":"LATENCY_ACTION_CREATOR_CMS_RELEASES","owner.reports":"LATENCY_ACTION_CREATOR_CMS_REPORTS","owner.videos":"LATENCY_ACTION_CREATOR_CMS_VIDEOS","playlist.videos":"LATENCY_ACTION_CREATOR_PLAYLIST_VIDEO_LIST","post.comments":"LATENCY_ACTION_CREATOR_POST_COMMENTS","post.edit":"LATENCY_ACTION_CREATOR_POST_EDIT","promotion.edit":"LATENCY_ACTION_CREATOR_PROMOTION_EDIT","video.analytics":"LATENCY_ACTION_CREATOR_VIDEO_ANALYTICS","video.claims":"LATENCY_ACTION_CREATOR_VIDEO_CLAIMS","video.comments":"LATENCY_ACTION_CREATOR_VIDEO_COMMENTS",
"video.copyright":"LATENCY_ACTION_CREATOR_VIDEO_COPYRIGHT","video.edit":"LATENCY_ACTION_CREATOR_VIDEO_EDIT","video.editor":"LATENCY_ACTION_CREATOR_VIDEO_EDITOR","video.editor_async":"LATENCY_ACTION_CREATOR_VIDEO_EDITOR_ASYNC","video.live_settings":"LATENCY_ACTION_CREATOR_VIDEO_LIVE_SETTINGS","video.live_streaming":"LATENCY_ACTION_CREATOR_VIDEO_LIVE_STREAMING","video.monetization":"LATENCY_ACTION_CREATOR_VIDEO_MONETIZATION","video.policy":"LATENCY_ACTION_CREATOR_VIDEO_POLICY","video.rights_management":"LATENCY_ACTION_CREATOR_VIDEO_RIGHTS_MANAGEMENT",
"video.translations":"LATENCY_ACTION_CREATOR_VIDEO_TRANSLATIONS"});w("ytLoggingLatencyUsageStats_",t.ytLoggingLatencyUsageStats_||{});const ul=window;class vl{constructor(){this.timing={};this.clearResourceTimings=()=>{};
this.webkitClearResourceTimings=()=>{};
this.mozClearResourceTimings=()=>{};
this.msClearResourceTimings=()=>{};
this.oClearResourceTimings=()=>{}}}
var wl=ul.performance||ul.mozPerformance||ul.msPerformance||ul.webkitPerformance||new vl;ma(wl.clearResourceTimings||wl.webkitClearResourceTimings||wl.mozClearResourceTimings||wl.msClearResourceTimings||wl.oClearResourceTimings||Pd,wl);const xl=["type.googleapis.com/youtube.api.pfiinnertube.YoutubeApiInnertube.BrowseResponse","type.googleapis.com/youtube.api.pfiinnertube.YoutubeApiInnertube.PlayerResponse"];function yl(a){var b={ub:{}},c=lg();if(Hi.instance!==void 0){const d=Hi.instance;a=[b!==d.m,a!==d.l,c!==d.j,!1,!1,!1,void 0!==d.i];if(a.some(e=>e))throw new O("InnerTubeTransportService is already initialized",a);
}else Hi.instance=new Hi(b,a,c)}
function zl(a,b){return r(function*(){var c;const d=a==null?void 0:(c=a.ea)==null?void 0:c.sessionIndex;c=yield te(ng(0,{sessionIndex:d}));return Promise.resolve(Object.assign({},Xk(b),c))})}
function Al(a,b,c,d=()=>{}){return r(function*(){var e;
if(b==null?0:(e=b.H)==null?0:e.context){e=b.H.context;for(var f of[])yield f.Hb(e)}var g;if((g=a.i)==null?0:g.Pb(b.input,b.H))return yield a.i.Cb(b.input,b.H);var h;if((g=(h=b.config)==null?void 0:h.Kb)&&a.h.has(g))var k=a.h.get(g);else{h=JSON.stringify(b.H);let q;f=(q=(k=b.O)==null?void 0:k.headers)!=null?q:{};b.O=Object.assign({},b.O,{headers:Object.assign({},f,c)});k=Object.assign({},b.O);b.O.method==="POST"&&(k=Object.assign({},k,{body:h}));k=a.l.fetch(b.input,k,b.config);g&&a.h.set(g,k)}(k=yield k)&&
S("web_streaming_player")&&Array.isArray(k)&&(k=k[0].playerResponse);var l;let n;if(k&&"error"in k&&((l=k)==null?0:(n=l.error)==null?0:n.details)){l=k.error.details;for(const q of l)(l=q["@type"])&&xl.indexOf(l)>-1&&(delete q["@type"],k=q)}g&&a.h.has(g)&&a.h.delete(g);let p;!k&&((p=a.i)==null?0:p.tb(b.input,b.H))&&(k=yield a.i.Bb(b.input,b.H));d();return k||void 0})}
function Bl(a,b,c){var d={ea:{identity:og}};let e=()=>{};
b.context||(b.context=Vk());return new L(f=>r(function*(){var g=Wk(c);g=Nf(g)?"same-origin":"cors";if(a.j.Ka){var h,k=d==null?void 0:(h=d.ea)==null?void 0:h.sessionIndex;h=ng(0,{sessionIndex:k});g=Object.assign({},Xk(g),h)}else g=yield zl(d,g);h=Wk(c);k={};S("json_condensed_response")&&(k.prettyPrint="false");h=Mf(h,k||{},!1);k={method:"POST",mode:Nf(h)?"same-origin":"cors",credentials:Nf(h)?"same-origin":"include"};var l={};const n={};for(const p of Object.keys(l))l[p]&&(n[p]=l[p]);Object.keys(n).length>
0&&(k.headers=n);f(Al(a,{input:h,O:k,H:b,config:d},g,e))}))}
var Hi=class{constructor(a,b,c){this.m=a;this.l=b;this.j=c;this.i=void 0;this.h=new Map;a.ca||(a.ca={});a.ca=Object.assign({},al,a.ca)}};var Gi=new Di;let Cl;function Dl(){if(!Cl){const a=Mi();yl({fetch:(b,c)=>te(fetch(new Request(b,c)))});
Fi(a);Cl=a.resolve(Gi)}return Cl}
;function El(a){return r(function*(){yield Fl();rk(a)})}
function Gl(a){return r(function*(){yield Fl();qk(a)})}
function Hl(a){r(function*(){var b=yield Eh();b?yield yi(a,b):(yield wk(),b={timestamp:a.timestamp},b=a.appShellAssetLoadReport?{N:"appShellAssetLoadReport",payload:a.appShellAssetLoadReport,options:b}:a.clientError?{N:"clientError",payload:a.clientError,options:b}:void 0,b&&Y(b.N,b.payload))})}
function Fl(){return r(function*(){try{yield wk()}catch(a){}})}
;var Il=Symbol("trackingData"),Jl=new WeakMap;function Kl(){Ll.instance||(Ll.instance=new Ll);return Ll.instance}
function Ml(a){const b=Nl(a);let c,d;if(S("il_use_view_model_logging_context")&&(b==null?0:(c=b.context)==null?0:(d=c.loggingContext)==null?0:d.loggingDirectives))return b.context.loggingContext.loggingDirectives.trackingParams||"";let e,f;if(b==null?0:(e=b.rendererContext)==null?0:(f=e.loggingContext)==null?0:f.loggingDirectives)return b.rendererContext.loggingContext.loggingDirectives.trackingParams||"";if(b==null?0:b.loggingDirectives)return b.loggingDirectives.trackingParams||"";let g;return((g=
a.veContainer)==null?0:g.trackingParams)?a.veContainer.trackingParams:(b==null?void 0:b.trackingParams)||""}
function Ol(a,b,c){const d=Z(c);return a.csn===null||d===a.csn||c?d:(a=new O("VisibilityLogger called before newScreen",{caller:b.tagName,previous_csn:a.csn,current_csn:d}),rk(a),null)}
function Pl(a){let b;return!((b=Nl(a))==null||!b.loggingDirectives)}
function Ql(a){a=Nl(a);return Math.floor(Number(a&&a.loggingDirectives&&a.loggingDirectives.visibility&&a.loggingDirectives.visibility.types||""))||1}
function Nl(a){let b,c=a.data||((b=a.props)==null?void 0:b.data);if(!c||a.isWebComponentWrapper){let d;c=(d=Jl.get(a))==null?void 0:d[Il]}return c}
var Ll=class{constructor(){this.l=new Set;this.i=new Set;this.h=new Map;this.client=void 0;this.csn=null}j(a){this.client=a}m(){this.clear();this.csn=Z()}clear(){this.l.clear();this.i.clear();this.h.clear();this.csn=null}A(a,b,c){var d=Ml(a),e=a.visualElement?a.visualElement:d;b=this.l.has(e);var f=this.h.get(e);this.l.add(e);this.h.set(e,!0);a.impressionLog&&!b&&a.impressionLog();if(d||a.visualElement)if(c=Ol(this,a,c)){var g=Pl(a);if(Ql(a)||g)e=a.visualElement?a.visualElement:Hk(d),d=a.interactionLoggingClientData,
g||b?Ql(a)&4?f||(a=this.client,rl(e,c),b=jl({cttAuthInfo:Tk(c)||void 0},c),f={csn:c,ve:e.getAsJson(),eventType:4},d&&(f.clientData=d),c==="UNDEFINED_CSN"?ol("visualElementShown",b,f):a?fk("visualElementShown",f,a,b):Y("visualElementShown",f,b)):Ql(a)&1&&!b&&ql(this.client,c,e,d):ql(this.client,c,e,d)}}u(a,b,c){var d=Ml(a);const e=a.visualElement?a.visualElement:d;b=this.i.has(e);const f=this.h.get(e);this.i.add(e);this.h.set(e,!1);if(f===!1)return!0;if(!d&&!a.visualElement)return!1;c=Ol(this,a,c);
if(!c||!Ql(a)&&Pl(a))return!1;d=a.visualElement?a.visualElement:Hk(d);Ql(a)&8?sl(this.client,c,d):Ql(a)&2&&!b&&(a=this.client,b=jl({cttAuthInfo:Tk(c)||void 0},c),d={csn:c,ve:d.getAsJson(),eventType:2},c==="UNDEFINED_CSN"?ol("visualElementHidden",b,d):a?fk("visualElementHidden",d,a,b):Y("visualElementHidden",d,b));return!0}};function Rl(){Sl.instance||(Sl.instance=new Sl)}
function Tl(a){Rl();Cf(Kl().A).bind(Kl())(a,void 0,8)}
function Ul(a){Rl();Cf(Kl().u).bind(Kl())(a,void 0,8)}
var Sl=class{j(a){Cf(Kl().j).bind(Kl())(a)}clear(){Cf(Kl().clear).bind(Kl())()}};function Vl(){Wl.instance||(Wl.instance=new Wl);return Wl.instance}
function Xl(a,b,c={}){a.i.add(c.layer||0);a.m=()=>{Yl(a,b,c);const d=Ok(c.layer);if(d){for(const e of a.D)Zl(a,e[0],e[1]||d,c.layer);for(const e of a.B)$l(a,e[0],e[1])}};
Z(c.layer)||a.m();if(c.ga)for(const d of c.ga)am(a,d,c.layer);else qk(Error("Delayed screen needs a data promise."))}
function Yl(a,b,c={}){var d=void 0;c.layer||(c.layer=0);d=c.Ga!==void 0?c.Ga:c.layer;const e=Z(d);d=Ok(d);let f;d&&(c.parentCsn!==void 0?f={clientScreenNonce:c.parentCsn,visualElement:d}:e&&e!=="UNDEFINED_CSN"&&(f={clientScreenNonce:e,visualElement:d}));let g;const h=R("EVENT_ID");e==="UNDEFINED_CSN"&&h&&(g={servletData:{serializedServletEventId:h}});S("combine_ve_grafts")&&e&&bm(a,e);S("no_client_ve_attach_unless_shown")&&d&&e&&nl(d,e);let k;try{k=il(a.client,b,f,c.fa,c.cttAuthInfo,g,c.xb,c.loggingExpectations)}catch(p){uk(p,
{Ob:b,rootVe:d,Gb:void 0,wb:e,Fb:f,fa:c.fa});qk(p);return}Uk(k,b,c.layer,c.cttAuthInfo);e&&e!=="UNDEFINED_CSN"&&d&&!Rk(e)&&sl(a.client,e,d,!0);a.h[a.h.length-1]&&!a.h[a.h.length-1].csn&&(a.h[a.h.length-1].csn=k||"");Rl();Cf(Kl().m).bind(Kl())();const l=Ok(c.layer);e&&e!=="UNDEFINED_CSN"&&l&&(S("web_mark_root_visible")||S("music_web_mark_root_visible"))&&Cf(pl)(void 0,k,l,void 0,void 0,void 0);a.i.delete(c.layer||0);a.m=void 0;let n;(n=a.V.get(c.layer))==null||n.forEach((p,q)=>{p?Zl(a,q,p,c.layer):
l&&Zl(a,q,l,c.layer)});
cm(a)}
function dm(a){var b=28631,c={layer:8};Cf(()=>{[28631].includes(b)||(rk(new O("createClientScreen() called with a non-page VE",b)),b=83769);c.isHistoryNavigation||a.h.push({rootVe:b,key:c.key||""});a.D=[];a.B=[];c.ga?Xl(a,b,c):Yl(a,b,c)})()}
function am(a,b,c=0){Cf(()=>{b.then(d=>{a.i.has(c)&&a.m&&a.m();const e=Z(c),f=Ok(c);if(e&&f){var g={automatedLogEventSource:3},h;(d==null?0:(h=d.response)==null?0:h.trackingParams)&&kl(a.client,e,f,Hk(d.response.trackingParams),!1,g);var k;(d==null?0:(k=d.playerResponse)==null?0:k.trackingParams)&&kl(a.client,e,f,Hk(d.playerResponse.trackingParams),!1,g)}})})()}
function Zl(a,b,c,d=0){return Cf(()=>{if(a.i.has(d))return a.D.push([b,c]),!0;const e=Z(d),f=c||Ok(d);if(e&&f){if(S("combine_ve_grafts")){const g=a.l.get(f.toString());g?g.push(b):(a.A.set(f.toString(),f),a.l.set(f.toString(),[b]));a.K||(a.K=rg(()=>{bm(a,e)},1200))}else kl(a.client,e,f,b);
return!0}return!1})()}
function em(a,b){return Cf(()=>{const c=Hk(b);Zl(a,c,void 0,8);return c})()}
function bm(a,b){if(b===void 0){const c=Qk();for(let d=0;d<c.length;d++)c[d]!==void 0&&bm(a,c[d])}else a.l.forEach((c,d)=>{(d=a.A.get(d))&&ll(a.client,b,d,c)}),a.l.clear(),a.A.clear(),a.K=void 0}
function fm(a,b,c,d=0){if(!b)return!1;d=Z(d);if(!d)return!1;tl(a.client,d,Hk(b),c);return!0}
function $l(a,b,c,d=0){const e=Z(d);b=b||Ok(d);e&&b&&(a=a.client,d=jl({cttAuthInfo:Tk(e)||void 0},e),c={csn:e,ve:b.getAsJson(),clientData:c},e==="UNDEFINED_CSN"?ol("visualElementStateChanged",d,c):a?fk("visualElementStateChanged",c,a,d):Y("visualElementStateChanged",c,d))}
function cm(a){for(var b=0;b<a.u.length;b++){var c=a.u[b];try{c()}catch(d){qk(d)}}a.u.length=0;for(b=0;b<a.I.length;b++){c=a.I[b];try{c()}catch(d){qk(d)}}}
var Wl=class{constructor(){this.D=[];this.B=[];this.h=[];this.u=[];this.I=[];this.l=new Map;this.A=new Map;this.i=new Set;this.V=new Map}j(a){this.client=a}clickCommand(a,b,c=0){return fm(this,a.clickTrackingParams,b,c)}stateChanged(a,b,c=0){this.visualElementStateChanged(Hk(a),b,c)}visualElementStateChanged(a,b,c=0){c===0&&this.i.has(c)?this.B.push([a,b]):$l(this,a,b,c)}};const gm={granted:"GRANTED",denied:"DENIED",unknown:"UNKNOWN"},hm=RegExp("^(?:[a-z]+:)?//","i");function im(a){var b=a.data;a=b.type;b=b.data;a==="notifications_register"?(P("IDToken",b),jm()):a==="notifications_check_registration"&&km(b)}
function lm(){return self.clients.matchAll({type:"window",includeUncontrolled:!0}).then(a=>{if(a)for(const b of a)b.postMessage({type:"update_unseen_notifications_count_signal"})})}
function mm(a){const b=[];a.forEach(c=>{b.push({key:c.key,value:c.value})});
return b}
function nm(a){return r(function*(){const b=mm(a.payload.chrome.extraUrlParams),c={recipientId:a.recipientId,endpoint:a.payload.chrome.endpoint,extraUrlParams:b},d=tf(gf);return om().then(e=>Bl(e,c,d).then(f=>{f.json().then(g=>g&&g.endpointUrl?pm(a,g.endpointUrl):Promise.resolve()).catch(g=>{Gl(g);
Promise.reject(g)})}))})}
function qm(a,b){var c=Z(8);if(c==null||!b)return a;a=hm.test(a)?new URL(a):new URL(a,self.registration.scope);a.searchParams.set("parentCsn",c);a.searchParams.set("parentTrackingParams",b);return a.toString()}
function pm(a,b){a.deviceId&&P("DeviceId",a.deviceId);a.timestampSec&&P("TimestampLowerBound",a.timestampSec);const c=a.payload.chrome,d=Vl();dm(d);var e;const f=(e=c.postedEndpoint)==null?void 0:e.clickTrackingParams;e=c==null?void 0:c.loggingDirectives;const g=c.title,h={body:c.body,icon:c.iconUrl,data:{nav:qm(b,e==null?void 0:e.trackingParams),id:c.notificationId,attributionTag:c.attributionTag,clickEndpoint:c.clickEndpoint,postedEndpoint:c.postedEndpoint,clickTrackingParams:f,isDismissed:!0,loggingDirectives:e},
tag:c.notificationTag||c.title+c.body+c.iconUrl,requireInteraction:!0};return self.registration.showNotification(g,h).then(()=>{var k;((k=h.data)==null?0:k.postedEndpoint)&&rm(h.data.postedEndpoint);let l;if((l=h.data)==null?0:l.loggingDirectives)k=h.data.loggingDirectives,S("enable_client_ve_spec")&&k.clientVeSpec?(k=Jk(k.clientVeSpec.uiType,void 0,k.clientVeSpec.elementIndex,k.clientVeSpec.clientYoutubeData,void 0,k),k=Zl(d,k,void 0,8)?k:null):k=k.trackingParams?em(d,k.trackingParams):null,Tl({screenLayer:8,
visualElement:k});sm(a.displayCap)}).catch(()=>{})}
function rm(a){if(!oj(a,ff))return Promise.reject();const b={serializedRecordNotificationInteractionsRequest:oj(a,ff).serializedInteractionsRequest},c=tf(hf);return om().then(d=>Bl(d,b,c)).then(d=>d)}
function sm(a){a!==-1&&self.registration.getNotifications().then(b=>{for(let d=0;d<b.length-a;d++){b[d].data.isDismissed=!1;b[d].close();let e,f;if((e=b[d].data)==null?0:(f=e.loggingDirectives)==null?0:f.trackingParams){var c=Hk(b[d].data.loggingDirectives.trackingParams);const g={screenLayer:8,visualElement:c},h=Jk(82046),k=Vl();Zl(k,h,c,8);Tl({screenLayer:8,visualElement:h});(c=Z(8))&&tl(k.client,c,h);Ul(g)}}})}
function km(a){const b=[tm(a),pf("RegistrationTimestamp").then(um),vm(),wm(),xm()];Promise.all(b).catch(()=>{P("IDToken",a);jm();return Promise.resolve()})}
function um(a){return Date.now()-(a||0)<=9E7?Promise.resolve():Promise.reject()}
function tm(a){return pf("IDToken").then(b=>a===b?Promise.resolve():Promise.reject())}
function vm(){return pf("Permission").then(a=>Notification.permission===a?Promise.resolve():Promise.reject())}
function wm(){return pf("Endpoint").then(a=>ym().then(b=>a===b?Promise.resolve():Promise.reject()))}
function xm(){return pf("application_server_key").then(a=>zm().then(b=>a===b?Promise.resolve():Promise.reject()))}
function Am(){var a=Notification.permission;if(gm[a])return gm[a]}
function jm(){P("RegistrationTimestamp",0);Promise.all([ym(),Bm(),Cm(),zm()]).then(([a,b,c,d])=>{b=b?kf(b):null;c=c?kf(c):null;d=d?$a(new Uint8Array(d),4):null;Dm(a,b,c,d)}).catch(()=>{Dm()})}
function Dm(a=null,b=null,c=null,d=null){of().then(e=>{e&&(P("Endpoint",a),P("P256dhKey",b),P("AuthKey",c),P("application_server_key",d),P("Permission",Notification.permission),Promise.all([pf("DeviceId"),pf("NotificationsDisabled")]).then(([f,g])=>{if(f!=null)var h=f;else{f=[];var k;h=h||Me.length;for(k=0;k<256;k++)f[k]=Me[0|Math.random()*h];h=f.join("")}Em(h,a!=null?a:void 0,b!=null?b:void 0,c!=null?c:void 0,d!=null?d:void 0,g!=null?g:void 0)}))})}
function Em(a,b,c,d,e,f){r(function*(){const g={notificationRegistration:{chromeRegistration:{deviceId:a,pushParams:{applicationServerKey:e,authKey:d,p256dhKey:c,browserEndpoint:b},notificationsDisabledInApp:f,permission:Am()}}},h=tf(jf);return om().then(k=>Bl(k,g,h).then(()=>{P("DeviceId",a);P("RegistrationTimestamp",Date.now());P("TimestampLowerBound",Date.now())},l=>{El(l)}))})}
function ym(){return self.registration.pushManager.getSubscription().then(a=>a?Promise.resolve(a.endpoint):Promise.resolve(null))}
function Bm(){return self.registration.pushManager.getSubscription().then(a=>a&&a.getKey?Promise.resolve(a.getKey("p256dh")):Promise.resolve(null))}
function Cm(){return self.registration.pushManager.getSubscription().then(a=>a&&a.getKey?Promise.resolve(a.getKey("auth")):Promise.resolve(null))}
function zm(){return self.registration.pushManager.getSubscription().then(a=>a?Promise.resolve(a.options.applicationServerKey):Promise.resolve(null))}
function om(){return r(function*(){try{return yield wk(!0),Dl()}catch(a){return yield El(a),Promise.reject(a)}})}
;let Fm=self.location.origin+"/";function yk(a){let b=typeof ServiceWorkerGlobalScope!=="undefined"&&self instanceof ServiceWorkerGlobalScope?Fe.registration.scope:Fm;b.endsWith("/")&&(b=b.slice(0,-1));return a==="/"?b:b+a}
;let Gm=void 0;function Hm(a){return r(function*(){Gm||(Gm=yield a.open("yt-appshell-assets"));return Gm})}
function Im(a,b){return r(function*(){const c=yield Hm(a),d=b.map(e=>Jm(c,e));
return Promise.all(d)})}
function Km(a,b){return r(function*(){let c;try{c=yield a.match(b,{cacheName:"yt-appshell-assets"})}catch(d){}return c})}
function Lm(a,b){return r(function*(){const c=yield Hm(a),d=(yield c.keys()).filter(e=>!b.includes(e.url)).map(e=>c.delete(e));
return Promise.all(d)})}
function Mm(a,b,c){return r(function*(){yield(yield Hm(a)).put(b,c)})}
function Nm(a,b){r(function*(){yield(yield Hm(a)).delete(b)})}
function Jm(a,b){return r(function*(){return(yield a.match(b))?Promise.resolve():a.add(b)})}
;var Om=Nh("yt-serviceworker-metadata",{M:{auth:{L:1},["resource-manifest-assets"]:{L:2}},shared:!0,upgrade(a,b){b(1)&&bh(a,"resource-manifest-assets");b(2)&&bh(a,"auth")},version:2});let Pm=null;function Qm(a){return uh(Om(),a)}
function Rm(){return r(function*(){const a=yield Eh();if(a)return Sm.instance||(Sm.instance=new Sm(a)),Sm.instance})}
function Tm(a,b){return r(function*(){yield X(yield Qm(a.token),["resource-manifest-assets"],"readwrite",c=>{const d=c.objectStore("resource-manifest-assets"),e=Date.now();return V(d.h.put(b,e)).then(()=>{Pm=e;let f=!0;return gh(d,{query:IDBKeyRange.bound(0,Date.now()),direction:"prev"},g=>f?(f=!1,ph(g)):d.delete(g.cursor.key).then(()=>jh(g)))})})})}
function Um(a,b){return r(function*(){let c=!1,d=0;yield X(yield Qm(a.token),["resource-manifest-assets"],"readonly",e=>gh(e.objectStore("resource-manifest-assets"),{query:IDBKeyRange.bound(0,Date.now()),direction:"prev"},f=>{if(f.cursor.value.includes(b))c=!0;else return d+=1,jh(f)}));
return c?d:-1})}
function Vm(a){return r(function*(){Pm||(yield X(yield Qm(a.token),["resource-manifest-assets"],"readonly",b=>gh(b.objectStore("resource-manifest-assets"),{query:IDBKeyRange.bound(0,Date.now()),direction:"prev"},c=>{Pm=c.cursor.key})));
return Pm})}
var Sm=class{constructor(a){this.token=a}};function Wm(){return r(function*(){const a=yield Eh();if(a)return Xm.instance||(Xm.instance=new Xm(a)),Xm.instance})}
function Ym(a,b){return r(function*(){yield dh(yield Qm(a.token),"auth",b,"shell_identifier_key")})}
function Zm(a){return r(function*(){return(yield(yield Qm(a.token)).get("auth","shell_identifier_key"))||""})}
function $m(a){return r(function*(){yield(yield Qm(a.token)).clear("auth")})}
var Xm=class{constructor(a){this.token=a}};function an(){r(function*(){const a=yield Wm();a&&(yield $m(a))})}
;var bn=class extends K{constructor(a){super(a)}hasUrl(){return E(F(this,1))!=null}};function cn(a){const b=a.o;return Zc(a,b,b[A]|0,bn,1,void 0===Jb?2:4,!1,!0)}
var dn=function(a,b){return(c,d)=>{{const f={da:!0};d&&Object.assign(f,d);c=od(c,void 0,void 0,f);try{const g=new a,h=g.o;Kd(b)(h,c);var e=g}finally{sd(c)}}return e}}(class extends K{constructor(a){super(a)}},[0,
Od,[0,Nd]]);function en(a){return r(function*(){const b=a.headers.get("X-Resource-Manifest");return b?Promise.resolve(fn(b)):Promise.reject(Error("No resource manifest header"))})}
function fn(a){return cn(dn(decodeURIComponent(a))).reduce((b,c)=>{(c=bd(c,1))&&b.push(c);return b},[])}
;function gn(a){return r(function*(){const b=yield wk();if(b&&E(F(b,3,Lb))!=null){var c=yield Wm();c&&(c=yield Zm(c),E(F(b,3,Lb,Oc))!==c&&(Nm(a.caches,a.J),an()))}})}
function hn(a){return r(function*(){let b,c;try{c=yield jn(a.h),b=yield en(c),yield Im(a.caches,b)}catch(d){return Promise.reject(d)}try{yield kn(),yield Mm(a.caches,a.J,c)}catch(d){return Promise.reject(d)}if(b)try{yield ln(a,b,a.J)}catch(d){}return Promise.resolve()})}
function jn(a){return r(function*(){try{return yield t.fetch(new Request(a))}catch(b){return Promise.reject(b)}})}
function kn(){return r(function*(){var a=yield wk();let b;a&&E(F(a,3,Lb))!=null&&(b=bd(a,3,void 0,Lb));return b?(a=yield Wm())?Promise.resolve(Ym(a,b)):Promise.reject(Error("Could not get AuthMonitor instance")):Promise.reject(Error("Could not get datasync ID"))})}
function ln(a,b,c){return r(function*(){const d=yield Rm();if(d)try{yield Tm(d,b)}catch(e){yield El(e)}b.push(c);try{yield Lm(a.caches,b)}catch(e){yield El(e)}return Promise.resolve()})}
function mn(a,b){return r(function*(){return Km(a.caches,b)})}
function nn(a){return r(function*(){return Km(a.caches,a.J)})}
var on=class{constructor(){var a=self.caches;let b;b=yk("/app_shell");S("service_worker_forward_exp_params")&&(b+=self.location.search);var c=yk("/app_shell_home");this.caches=a;this.h=b;this.J=c}initialize(){const a=this;return r(function*(){yield gn(a);return hn(a)})}};var pn=class{constructor(){const a=this;this.i=new ReadableStream({start(b){a.close=()=>void b.close();
a.h=c=>{const d=c.getReader();return d.read().then(function h({done:f,value:g}){if(f)return Promise.resolve();b.enqueue(g);return d.read().then(h)})};
a.j=()=>{const c=(new TextEncoder).encode("<script>if (window.fetchInitialData) { window.fetchInitialData(); } else { window.getInitialData = undefined; }\x3c/script>");b.enqueue(c)}}})}};function qn(a,b){return r(function*(){const c=b.request,d=yield mn(a.h,c.url);if(d)return a.i&&Hl({appShellAssetLoadReport:{assetPath:c.url,cacheHit:!0},timestamp:W()}),d;rn(a,c);return sn(b)})}
function tn(a,b){return r(function*(){const c=yield un(b);if(c.response&&(c.response.ok||c.response.type==="opaqueredirect"||c.response.status===429||c.response.status===303||c.response.status>=300&&c.response.status<400))return c.response;const d=yield nn(a.h);if(d)return vn(a),wn(d,b);xn(a);return c.response?c.response:Promise.reject(c.error)})}
function yn(a,b){b=new URL(b);if(!a.config.pa.includes(b.pathname))return!1;if(!b.search)return!0;b=new URLSearchParams(b.search);for(const c of a.config.ra){if(c.key==="*")return!0;a=b.get(c.key);if(c.value===void 0||a===c.value)if(b.delete(c.key),!b.toString())return!0}return!1}
function zn(a,b){return r(function*(){const c=yield nn(a.h);if(!c)return xn(a),sn(b);vn(a);var d;a:{if(c.headers&&(d=c.headers.get("date"))&&(d=Date.parse(d),!isNaN(d))){d=Math.round(W()-d);break a}d=-1}if(!(d>-1&&d/864E5>=7))return wn(c,b);d=yield un(b);return d.response&&d.response.ok?d.response:wn(c,b)})}
function sn(a){return Promise.resolve(a.preloadResponse).then(b=>b&&!An(b)?b:t.fetch(a.request))}
function rn(a,b){if(a.i){var c={assetPath:b.url,cacheHit:!1};Rm().then(d=>{if(d){var e=Vm(d).then(f=>{f&&(c.currentAppBundleTimestampSec=String(Math.floor(f/1E3)))});
d=Um(d,b.url).then(f=>{c.appBundleVersionDiffCount=f});
Promise.all([e,d]).catch(f=>{El(f)}).finally(()=>{Hl({appShellAssetLoadReport:c,
timestamp:W()})})}else Hl({appShellAssetLoadReport:c,
timestamp:W()})})}}
function vn(a){a.i&&Hl({appShellAssetLoadReport:{assetPath:a.h.J,cacheHit:!0},timestamp:W()})}
function xn(a){a.i&&Hl({appShellAssetLoadReport:{assetPath:a.h.J,cacheHit:!1},timestamp:W()})}
function wn(a,b){if(!S("sw_nav_preload_pbj"))return a;const c=new pn,d=c.h(a.body);Promise.resolve(b.preloadResponse).then(e=>{if(!e||!An(e))throw Error("no pbj preload response available");d.then(()=>c.h(e.body)).then(()=>void c.close())}).catch(()=>{d.then(()=>{c.j();
c.close()})});
return new Response(c.i,{status:a.status,statusText:a.statusText,headers:a.headers})}
function un(a){return r(function*(){try{return{response:yield sn(a)}}catch(b){return{error:b}}})}
function An(a){return a.headers.get("x-navigation-preload-response-type")==="pbj"}
var Jn=class{constructor(){var a=Bn;var b={wa:Cn,Ha:Dn([En,/\/signin/,/\/logout/]),pa:["/","/feed/downloads"],ra:Fn([{key:"feature",value:"ytca"}]),qa:Gn(S("kevlar_sw_app_wide_fallback")?Hn:In)};this.h=a;this.config=b;a=T("app_shell_asset_log_fraction");this.i=!0;a&&(this.i=Math.random()<a)}};const Kn=/^\/$/,In=[Kn,/^\/feed\/downloads$/],Hn=[Kn,/^\/feed\/\w*/,/^\/results$/,/^\/playlist$/,/^\/watch$/,/^\/channel\/\w*/];function Gn(a){return new RegExp(a.map(b=>b.source).join("|"))}
const Ln=/^https:\/\/([\w-]*\.)*youtube\.com.*/;Gn([Ln,/^https:\/\/([\w-]*\.)*gstatic\.com.*/]);Gn([/\.css$/,/\.js$/,/\.webm$/,/\.png$/]);function Dn(a){a=Gn(a);return new RegExp(`${Ln.source}(${a.source})`)}
const Mn=Gn([/\.css$/,/\.js$/,/\.ico$/,/\/ytmweb\/_\/js\//,/\/ytmweb\/_\/ss\//,/\/kabuki\/_\/js\//,/\/kabuki\/_\/ss\//,/\/ytmainappweb\/_\/js\//,/\/ytmainappweb\/_\/ss\//,/\/ytmusicweb\/_\/js\//,/\/ytmusicweb\/_\/ss\//,/\/music_integrations\/_\/js\//,/\/music_integrations\/_\/ss\//]),Cn=new RegExp(`${Ln.source}(${Mn.source})`),En=/purge_shell=1/;function Fn(a=[]){const b=[];for(const c of Qf)b.push({key:c});for(const c of a)b.push(c);return b}
Dn([En]);Fn();var On=class{constructor(){var a=Bn,b=Nn,c=self;if(t.URLPattern){var d=[];S("service_worker_static_routing_exclude_embed")&&d.push({condition:{urlPattern:new URLPattern({pathname:"/embed*"})},source:"network"});S("service_worker_static_routing_exclude_innertube")&&d.push({condition:{urlPattern:new URLPattern({pathname:"/youtubei/v1/*"})},source:"network"})}else d=[];this.h=c;this.i=a;this.u=b;this.B=lf;this.j=d}init(){this.h.oninstall=this.A.bind(this);this.h.onactivate=this.l.bind(this);this.h.onfetch=
this.m.bind(this);this.h.onmessage=this.D.bind(this)}A(a){this.h.skipWaiting();if(S("service_worker_static_routing_registration")&&this.j.length>0&&a.addRoutes)try{a.addRoutes(this.j)}catch(c){}const b=this.i.initialize().catch(c=>{El(c);return Promise.resolve()});
a.waitUntil(b)}l(a){const b=[this.h.clients.claim()],c=this.h.registration;c.navigationPreload&&(b.push(c.navigationPreload.enable()),S("sw_nav_preload_pbj")&&b.push(c.navigationPreload.setHeaderValue("pbj")));a.waitUntil(Promise.all(b))}m(a){const b=this;return r(function*(){var c=b.u,d=!!b.h.registration.navigationPreload;const e=a.request;if(c.config.Ha.test(e.url))xk.instance&&(delete xk.instance.h,t.__SAPISID=void 0,Q("VISITOR_DATA",void 0),Q("SESSION_INDEX",void 0),Q("DELEGATED_SESSION_ID",
void 0),Q("USER_SESSION_ID",void 0),Q("INNERTUBE_CONTEXT_SERIALIZED_DELEGATION_CONTEXT",void 0)),d=a.respondWith,c=c.h,Nm(c.caches,c.J),an(),c=sn(a),d.call(a,c);else if(c.config.wa.test(e.url))a.respondWith(qn(c,a));else if(e.mode==="navigate"){const f=new URL(e.url);c.config.qa.test(f.pathname)?a.respondWith(tn(c,a)):yn(c,e.url)?a.respondWith(zn(c,a)):d&&a.respondWith(sn(a))}})}D(a){const b=a.data;
this.B.includes(b.type)?im(a):b.type==="refresh_shell"&&hn(this.i).catch(c=>{El(c)})}};function Pn(){let a=v("ytglobal.storage_");a||(a=new Qn,w("ytglobal.storage_",a));return a}
var Qn=class{estimate(){return r(function*(){const a=navigator;let b;if((b=a.storage)==null?0:b.estimate)return a.storage.estimate();let c;if((c=a.webkitTemporaryStorage)==null?0:c.queryUsageAndQuota)return Rn()})}};
function Rn(){const a=navigator;return new Promise((b,c)=>{let d;(d=a.webkitTemporaryStorage)!=null&&d.queryUsageAndQuota?a.webkitTemporaryStorage.queryUsageAndQuota((e,f)=>{b({usage:e,quota:f})},e=>{c(e)}):c(Error("webkitTemporaryStorage is not supported."))})}
w("ytglobal.storageClass_",Qn);function Sn(a,b){Pn().estimate().then(c=>{c=Object.assign({},b,{isSw:self.document===void 0,isIframe:self!==self.top,deviceStorageUsageMbytes:Tn(c==null?void 0:c.usage),deviceStorageQuotaMbytes:Tn(c==null?void 0:c.quota)});a.h("idbQuotaExceeded",c)})}
class Un{constructor(){var a=Vn;this.handleError=Wn;this.h=a;this.i=!1;self.document===void 0||self.addEventListener("beforeunload",()=>{this.i=!0});
this.j=Math.random()<=.2}R(a,b){switch(a){case "IDB_DATA_CORRUPTED":S("idb_data_corrupted_killswitch")||this.h("idbDataCorrupted",b);break;case "IDB_UNEXPECTEDLY_CLOSED":this.h("idbUnexpectedlyClosed",b);break;case "IS_SUPPORTED_COMPLETED":S("idb_is_supported_completed_killswitch")||this.h("idbIsSupportedCompleted",b);break;case "QUOTA_EXCEEDED":Sn(this,b);break;case "TRANSACTION_ENDED":this.j&&Math.random()<=.1&&this.h("idbTransactionEnded",b);break;case "TRANSACTION_UNEXPECTEDLY_ABORTED":a=Object.assign({},
b,{hasWindowUnloaded:this.i}),this.h("idbTransactionAborted",a)}}}function Tn(a){return typeof a==="undefined"?"-1":String(Math.ceil(a/1048576))}
;eg(bg(),{G:[{Fa:/Failed to fetch/,weight:500}],F:[]});({handleError:Wn=pk,R:Vn=Y}={handleError:Gl,R:function(a,b){return r(function*(){yield Fl();Y(a,b)})}});
var Vn,Wn;for(zg=new Un;yg.length>0;){const a=yg.shift();switch(a.type){case "ERROR":zg.handleError(a.payload);break;case "EVENT":zg.R(a.eventType,a.payload)}}xk.instance=new xk;self.onnotificationclick=function(a){a.notification.close();const b=a.notification.data;b.isDismissed=!1;const c=self.clients.matchAll({type:"window",includeUncontrolled:!0});c.then(d=>{a:{var e=b.nav;for(const f of d)if(f.url===e){f.focus();break a}self.clients.openWindow(e)}});
a.waitUntil(c);a.waitUntil(rm(b.clickEndpoint))};
self.onnotificationclose=function(a){var b=a.notification.data,c;if(b==null?0:(c=b.loggingDirectives)==null?0:c.trackingParams){a=Hk(b.loggingDirectives.trackingParams);c={screenLayer:8,visualElement:a};if(b.isDismissed){b=Jk(74726);const d=Vl();Zl(d,b,a,8);Tl({screenLayer:8,visualElement:b});(a=Z(8))&&tl(d.client,a,b)}Ul(c)}};
self.onpush=function(a){a.waitUntil(pf("NotificationsDisabled").then(b=>{if(b)return Promise.resolve();if(a.data&&a.data.text().length)try{return nm(a.data.json())}catch(c){return Promise.resolve(c.message)}return Promise.resolve()}));
a.waitUntil(lm())};
self.onpushsubscriptionchange=function(){jm()};
const Bn=new on,Nn=new Jn;(new On).init();
