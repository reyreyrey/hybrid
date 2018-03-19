var Long=function(){"use strict";function i(i,t,n){this.low=0|i,this.high=0|t,this.unsigned=!!n}function t(i){return(i&&i.__isLong__)===!0}function n(i,t){var n,s,r;return t?(i>>>=0,(r=i>=0&&256>i)&&(s=o[i])?s:(n=e(i,0>(0|i)?-1:0,!0),r&&(o[i]=n),n)):(i|=0,(r=i>=-128&&128>i)&&(s=u[i])?s:(n=e(i,0>i?-1:0,!1),r&&(u[i]=n),n))}function s(i,t){if(isNaN(i)||!isFinite(i))return t?m:w;if(t){if(0>i)return m;if(i>=d)return p}else{if(-v>=i)return _;if(i+1>=v)return E}return 0>i?s(-i,t).neg():e(i%l|0,i/l|0,t)}function e(t,n,s){return new i(t,n,s)}function r(i,t,n){if(0===i.length)throw Error("empty string");if("NaN"===i||"Infinity"===i||"+Infinity"===i||"-Infinity"===i)return w;if("number"==typeof t?(n=t,t=!1):t=!!t,n=n||10,2>n||n>36)throw RangeError("radix");var e;if((e=i.indexOf("-"))>0)throw Error("interior hyphen");if(0===e)return r(i.substring(1),t,n).neg();for(var h=s(g(n,8)),u=w,o=0;o<i.length;o+=8){var f=Math.min(8,i.length-o),a=parseInt(i.substring(o,o+f),n);if(8>f){var l=s(g(n,f));u=u.mul(l).add(s(a))}else u=u.mul(h),u=u.add(s(a))}return u.unsigned=t,u}function h(t){return t instanceof i?t:"number"==typeof t?s(t):"string"==typeof t?r(t):e(t.low,t.high,t.unsigned)}i.__isLong__,Object.defineProperty(i.prototype,"__isLong__",{value:!0,enumerable:!1,configurable:!1}),i.isLong=t;var u={},o={};i.fromInt=n,i.fromNumber=s,i.fromBits=e;var g=Math.pow;i.fromString=r,i.fromValue=h;var f=65536,a=1<<24,l=f*f,d=l*l,v=d/2,c=n(a),w=n(0);i.ZERO=w;var m=n(0,!0);i.UZERO=m;var N=n(1);i.ONE=N;var b=n(1,!0);i.UONE=b;var q=n(-1);i.NEG_ONE=q;var E=e(-1,2147483647,!1);i.MAX_VALUE=E;var p=e(-1,-1,!0);i.MAX_UNSIGNED_VALUE=p;var _=e(0,-2147483648,!1);i.MIN_VALUE=_;var L=i.prototype;return L.toInt=function(){return this.unsigned?this.low>>>0:this.low},L.toNumber=function(){return this.unsigned?(this.high>>>0)*l+(this.low>>>0):this.high*l+(this.low>>>0)},L.toString=function(i){if(i=i||10,2>i||i>36)throw RangeError("radix");if(this.isZero())return"0";if(this.isNegative()){if(this.eq(_)){var t=s(i),n=this.div(t),e=n.mul(t).sub(this);return n.toString(i)+e.toInt().toString(i)}return"-"+this.neg().toString(i)}for(var r=s(g(i,6),this.unsigned),h=this,u="";;){var o=h.div(r),f=h.sub(o.mul(r)).toInt()>>>0,a=f.toString(i);if(h=o,h.isZero())return a+u;for(;a.length<6;)a="0"+a;u=""+a+u}},L.getHighBits=function(){return this.high},L.getHighBitsUnsigned=function(){return this.high>>>0},L.getLowBits=function(){return this.low},L.getLowBitsUnsigned=function(){return this.low>>>0},L.getNumBitsAbs=function(){if(this.isNegative())return this.eq(_)?64:this.neg().getNumBitsAbs();for(var i=0!=this.high?this.high:this.low,t=31;t>0&&0==(i&1<<t);t--);return 0!=this.high?t+33:t+1},L.isZero=function(){return 0===this.high&&0===this.low},L.isNegative=function(){return!this.unsigned&&this.high<0},L.isPositive=function(){return this.unsigned||this.high>=0},L.isOdd=function(){return 1===(1&this.low)},L.isEven=function(){return 0===(1&this.low)},L.equals=function(i){return t(i)||(i=h(i)),this.unsigned!==i.unsigned&&this.high>>>31===1&&i.high>>>31===1?!1:this.high===i.high&&this.low===i.low},L.eq=L.equals,L.notEquals=function(i){return!this.eq(i)},L.neq=L.notEquals,L.lessThan=function(i){return this.comp(i)<0},L.lt=L.lessThan,L.lessThanOrEqual=function(i){return this.comp(i)<=0},L.lte=L.lessThanOrEqual,L.greaterThan=function(i){return this.comp(i)>0},L.gt=L.greaterThan,L.greaterThanOrEqual=function(i){return this.comp(i)>=0},L.gte=L.greaterThanOrEqual,L.compare=function(i){if(t(i)||(i=h(i)),this.eq(i))return 0;var n=this.isNegative(),s=i.isNegative();return n&&!s?-1:!n&&s?1:this.unsigned?i.high>>>0>this.high>>>0||i.high===this.high&&i.low>>>0>this.low>>>0?-1:1:this.sub(i).isNegative()?-1:1},L.comp=L.compare,L.negate=function(){return!this.unsigned&&this.eq(_)?_:this.not().add(N)},L.neg=L.negate,L.add=function(i){t(i)||(i=h(i));var n=this.high>>>16,s=65535&this.high,r=this.low>>>16,u=65535&this.low,o=i.high>>>16,g=65535&i.high,f=i.low>>>16,a=65535&i.low,l=0,d=0,v=0,c=0;return c+=u+a,v+=c>>>16,c&=65535,v+=r+f,d+=v>>>16,v&=65535,d+=s+g,l+=d>>>16,d&=65535,l+=n+o,l&=65535,e(v<<16|c,l<<16|d,this.unsigned)},L.subtract=function(i){return t(i)||(i=h(i)),this.add(i.neg())},L.sub=L.subtract,L.multiply=function(i){if(this.isZero())return w;if(t(i)||(i=h(i)),i.isZero())return w;if(this.eq(_))return i.isOdd()?_:w;if(i.eq(_))return this.isOdd()?_:w;if(this.isNegative())return i.isNegative()?this.neg().mul(i.neg()):this.neg().mul(i).neg();if(i.isNegative())return this.mul(i.neg()).neg();if(this.lt(c)&&i.lt(c))return s(this.toNumber()*i.toNumber(),this.unsigned);var n=this.high>>>16,r=65535&this.high,u=this.low>>>16,o=65535&this.low,g=i.high>>>16,f=65535&i.high,a=i.low>>>16,l=65535&i.low,d=0,v=0,m=0,N=0;return N+=o*l,m+=N>>>16,N&=65535,m+=u*l,v+=m>>>16,m&=65535,m+=o*a,v+=m>>>16,m&=65535,v+=r*l,d+=v>>>16,v&=65535,v+=u*a,d+=v>>>16,v&=65535,v+=o*f,d+=v>>>16,v&=65535,d+=n*l+r*a+u*f+o*g,d&=65535,e(m<<16|N,d<<16|v,this.unsigned)},L.mul=L.multiply,L.divide=function(i){if(t(i)||(i=h(i)),i.isZero())throw Error("division by zero");if(this.isZero())return this.unsigned?m:w;var n,e,r;if(this.unsigned)i.unsigned||(i=i.toUnsigned());else{if(this.eq(_)){if(i.eq(N)||i.eq(q))return _;if(i.eq(_))return N;var u=this.shr(1);return n=u.div(i).shl(1),n.eq(w)?i.isNegative()?N:q:(e=this.sub(i.mul(n)),r=n.add(e.div(i)))}if(i.eq(_))return this.unsigned?m:w;if(this.isNegative())return i.isNegative()?this.neg().div(i.neg()):this.neg().div(i).neg();if(i.isNegative())return this.div(i.neg()).neg()}if(this.unsigned){if(i.gt(this))return m;if(i.gt(this.shru(1)))return b;r=m}else r=w;for(e=this;e.gte(i);){n=Math.max(1,Math.floor(e.toNumber()/i.toNumber()));for(var o=Math.ceil(Math.log(n)/Math.LN2),f=48>=o?1:g(2,o-48),a=s(n),l=a.mul(i);l.isNegative()||l.gt(e);)n-=f,a=s(n,this.unsigned),l=a.mul(i);a.isZero()&&(a=N),r=r.add(a),e=e.sub(l)}return r},L.div=L.divide,L.modulo=function(i){return t(i)||(i=h(i)),this.sub(this.div(i).mul(i))},L.mod=L.modulo,L.not=function(){return e(~this.low,~this.high,this.unsigned)},L.and=function(i){return t(i)||(i=h(i)),e(this.low&i.low,this.high&i.high,this.unsigned)},L.or=function(i){return t(i)||(i=h(i)),e(this.low|i.low,this.high|i.high,this.unsigned)},L.xor=function(i){return t(i)||(i=h(i)),e(this.low^i.low,this.high^i.high,this.unsigned)},L.shiftLeft=function(i){return t(i)&&(i=i.toInt()),0===(i&=63)?this:32>i?e(this.low<<i,this.high<<i|this.low>>>32-i,this.unsigned):e(0,this.low<<i-32,this.unsigned)},L.shl=L.shiftLeft,L.shiftRight=function(i){return t(i)&&(i=i.toInt()),0===(i&=63)?this:32>i?e(this.low>>>i|this.high<<32-i,this.high>>i,this.unsigned):e(this.high>>i-32,this.high>=0?0:-1,this.unsigned)},L.shr=L.shiftRight,L.shiftRightUnsigned=function(i){if(t(i)&&(i=i.toInt()),i&=63,0===i)return this;var n=this.high;if(32>i){var s=this.low;return e(s>>>i|n<<32-i,n>>>i,this.unsigned)}return 32===i?e(n,0,this.unsigned):e(n>>>i-32,0,this.unsigned)},L.shru=L.shiftRightUnsigned,L.toSigned=function(){return this.unsigned?e(this.low,this.high,!1):this},L.toUnsigned=function(){return this.unsigned?this:e(this.low,this.high,!0)},i}();egret.registerClass(Long,"Long");