var __reflect=this&&this.__reflect||function(e,t,r){e.__class__=t,r?r.push(t):r=[t],e.__types__=e.__types__?r.concat(e.__types__):r},__extends=this&&this.__extends||function(e,t){function r(){this.constructor=e}for(var i in t)t.hasOwnProperty(i)&&(e[i]=t[i]);e.prototype=null===t?Object.create(t):(r.prototype=t.prototype,new r)},RES;!function(e){var t=function(t){function r(){var r=t.call(this)||this;return r.resourceConfig=null,r.resourceConfig=e.configInstance,r}return __extends(r,t),r.prototype.addSubkey=function(e,t){this.resourceConfig.addSubkey(e,t)},r.prototype.loadFile=function(e,t,r){},r.prototype.getRes=function(e){},r.prototype.destroyRes=function(e){return!1},r.getStringPrefix=function(e){if(!e)return"";var t=e.indexOf(".");return-1!=t?e.substring(0,t):""},r.getStringTail=function(e){if(!e)return"";var t=e.indexOf(".");return-1!=t?e.substring(t+1):""},r}(egret.HashObject);e.AnalyzerBase=t,__reflect(t.prototype,"RES.AnalyzerBase")}(RES||(RES={}));var RES;!function(e){var t=function(t){function r(){var e=t.call(this)||this;return e.fileDic={},e.resItemDic=[],e._dataFormat=egret.HttpResponseType.ARRAY_BUFFER,e.recycler=[],e}return __extends(r,t),r.prototype.loadFile=function(t,r,i){if(this.fileDic[t.name])return void r.call(i,t);var n=this.getRequest();this.resItemDic[n.hashCode]={item:t,func:r,thisObject:i},n.open(e.$getVirtualUrl(t.url)),n.send()},r.prototype.getRequest=function(){var e=this.recycler.pop();return e||(e=new egret.HttpRequest,e.addEventListener(egret.Event.COMPLETE,this.onLoadFinish,this),e.addEventListener(egret.IOErrorEvent.IO_ERROR,this.onLoadFinish,this)),e.responseType=this._dataFormat,e},r.prototype.onLoadFinish=function(e){var t=e.target,r=this.resItemDic[t.hashCode];delete this.resItemDic[t.hashCode];var i=r.item,n=r.func;i.loaded=e.type==egret.Event.COMPLETE,i.loaded&&this.analyzeData(i,t.response),this.recycler.push(t),n.call(r.thisObject,i)},r.prototype.analyzeData=function(e,t){var r=e.name;this.fileDic[r]||""!=t&&!t||(this.fileDic[r]=t)},r.prototype.getRes=function(e){return this.fileDic[e]},r.prototype.hasRes=function(e){var t=this.getRes(e);return null!=t},r.prototype.destroyRes=function(e){return this.fileDic[e]?(this.onResourceDestroy(this.fileDic[e]),delete this.fileDic[e],!0):!1},r.prototype.onResourceDestroy=function(e){},r}(e.AnalyzerBase);e.BinAnalyzer=t,__reflect(t.prototype,"RES.BinAnalyzer")}(RES||(RES={}));var RES;!function(e){var t=function(){function e(e,t,r){this.groupName="",this.data=null,this._loaded=!1,this.name=e,this.url=t,this.type=r}return Object.defineProperty(e.prototype,"loaded",{get:function(){return this.data?this.data.loaded:this._loaded},set:function(e){this.data&&(this.data.loaded=e),this._loaded=e},enumerable:!0,configurable:!0}),e.prototype.toString=function(){return'[ResourceItem name="'+this.name+'" url="'+this.url+'" type="'+this.type+'"]'},e}();t.TYPE_XML="xml",t.TYPE_IMAGE="image",t.TYPE_BIN="bin",t.TYPE_TEXT="text",t.TYPE_JSON="json",t.TYPE_SHEET="sheet",t.TYPE_FONT="font",t.TYPE_SOUND="sound",e.ResourceItem=t,__reflect(t.prototype,"RES.ResourceItem")}(RES||(RES={}));var RES;!function(e){var t=function(t){function r(){var e=t.call(this)||this;return e.sheetMap={},e.textureMap={},e.recyclerIamge=[],e._dataFormat=egret.HttpResponseType.TEXT,e}return __extends(r,t),r.prototype.getRes=function(t){var r=this.fileDic[t];if(r||(r=this.textureMap[t]),!r){var i=e.AnalyzerBase.getStringPrefix(t);if(r=this.fileDic[i]){var n=e.AnalyzerBase.getStringTail(t);r=r.getTexture(n)}}return r},r.prototype.onLoadFinish=function(e){var t=e.target,r=this.resItemDic[t.$hashCode];delete this.resItemDic[t.hashCode];var i=r.item,n=r.func;if(i.loaded=e.type==egret.Event.COMPLETE,i.loaded)if(t instanceof egret.HttpRequest){i.loaded=!1;var o=this.analyzeConfig(i,t.response);if(o)return this.loadImage(o,r),void this.recycler.push(t)}else{var s=new egret.Texture;s._setBitmapData(t.data),this.analyzeBitmap(i,s)}t instanceof egret.HttpRequest?this.recycler.push(t):this.recyclerIamge.push(t),n.call(r.thisObject,i)},r.prototype.analyzeConfig=function(e,t){var r,i=e.name,n="";try{var o=t;r=JSON.parse(o)}catch(s){egret.$warn(1017,e.url,t)}return r&&(this.sheetMap[i]=r,n=this.getRelativePath(e.url,r.file)),n},r.prototype.analyzeBitmap=function(e,t){var r=e.name;if(!this.fileDic[r]&&t){var i=this.sheetMap[r];delete this.sheetMap[r];var n=e.data&&e.data.subkeys?"":r,o=this.parseSpriteSheet(t,i,n);this.fileDic[r]=o}},r.prototype.getRelativePath=function(e,t){e=e.split("\\").join("/");var r=e.match(/#.*|\?.*/),i="";r&&(i=r[0]);var n=e.lastIndexOf("/");return e=-1!=n?e.substring(0,n+1)+t:t,e+i},r.prototype.parseSpriteSheet=function(e,t,r){var i=t.frames;if(!i)return null;var n=new egret.SpriteSheet(e),o=this.textureMap;for(var s in i){var a=i[s],u=n.createTexture(s,a.x,a.y,a.w,a.h,a.offX,a.offY,a.sourceW,a.sourceH);if(a.scale9grid){var c=a.scale9grid,l=c.split(",");u.scale9Grid=new egret.Rectangle(parseInt(l[0]),parseInt(l[1]),parseInt(l[2]),parseInt(l[3]))}null==o[s]&&(o[s]=u,r&&this.addSubkey(s,r))}return n},r.prototype.destroyRes=function(e){var t=this.fileDic[e];if(t){delete this.fileDic[e];var r=void 0;for(var i in t._textureMap)null==r&&(r=t._textureMap[i],this.onResourceDestroy(r),r=null),delete this.textureMap[i];return t.dispose&&t.dispose(),!0}return!1},r.prototype.loadImage=function(t,r){var i=this.getImageLoader();this.resItemDic[i.hashCode]=r,i.load(e.$getVirtualUrl(t))},r.prototype.getImageLoader=function(){var e=this.recyclerIamge.pop();return e||(e=new egret.ImageLoader,e.addEventListener(egret.Event.COMPLETE,this.onLoadFinish,this),e.addEventListener(egret.IOErrorEvent.IO_ERROR,this.onLoadFinish,this)),e},r.prototype.onResourceDestroy=function(e){e&&e.dispose()},r}(e.BinAnalyzer);e.SheetAnalyzer=t,__reflect(t.prototype,"RES.SheetAnalyzer")}(RES||(RES={}));var RES;!function(e){}(RES||(RES={}));var RES;!function(e){var t=function(t){function r(){var e=t.call(this)||this;return e.fileDic={},e.resItemDic=[],e.recycler=[],e}return __extends(r,t),r.prototype.loadFile=function(t,r,i){if(this.fileDic[t.name])return void r.call(i,t);var n=this.getLoader();this.resItemDic[n.$hashCode]={item:t,func:r,thisObject:i},n.load(e.$getVirtualUrl(t.url))},r.prototype.getLoader=function(){var e=this.recycler.pop();return e||(e=new egret.ImageLoader,e.addEventListener(egret.Event.COMPLETE,this.onLoadFinish,this),e.addEventListener(egret.IOErrorEvent.IO_ERROR,this.onLoadFinish,this)),e},r.prototype.onLoadFinish=function(e){var t=e.$target,r=this.resItemDic[t.$hashCode];delete this.resItemDic[t.$hashCode];var i=r.item,n=r.func;if(i.loaded=e.$type==egret.Event.COMPLETE,i.loaded){var o=new egret.Texture;o._setBitmapData(t.data),this.analyzeData(i,o)}this.recycler.push(t),n.call(r.thisObject,i)},r.prototype.analyzeData=function(e,t){var r=e.name;if(!this.fileDic[r]&&t){this.fileDic[r]=t;var i=e.data;if(i&&i.scale9grid){var n=i.scale9grid,o=n.split(",");t.scale9Grid=new egret.Rectangle(parseInt(o[0]),parseInt(o[1]),parseInt(o[2]),parseInt(o[3]))}}},r.prototype.getRes=function(e){return this.fileDic[e]},r.prototype.hasRes=function(e){var t=this.getRes(e);return null!=t},r.prototype.destroyRes=function(e){return this.fileDic[e]?(this.onResourceDestroy(this.fileDic[e]),delete this.fileDic[e],!0):!1},r.prototype.onResourceDestroy=function(e){e.dispose()},r}(e.AnalyzerBase);e.ImageAnalyzer=t,__reflect(t.prototype,"RES.ImageAnalyzer")}(RES||(RES={}));var RES;!function(e){var t=function(e){function t(){var t=e.call(this)||this;return t._dataFormat=egret.HttpResponseType.TEXT,t}return __extends(t,e),t}(e.BinAnalyzer);e.TextAnalyzer=t,__reflect(t.prototype,"RES.TextAnalyzer")}(RES||(RES={}));var RES;!function(e){var t=function(e){function t(){var t=e.call(this)||this;return t._dataFormat=egret.HttpResponseType.TEXT,t}return __extends(t,e),t.prototype.analyzeData=function(e,t){var r=e.name;if(!this.fileDic[r]&&t)try{var i=t;this.fileDic[r]=JSON.parse(i)}catch(n){egret.$warn(1017,e.url,t)}},t}(e.BinAnalyzer);e.JsonAnalyzer=t,__reflect(t.prototype,"RES.JsonAnalyzer")}(RES||(RES={}));var RES;!function(e){var t=function(t){function r(){var e=t.call(this)||this;return e.thread=2,e.loadingCount=0,e.callBack=null,e.resInstance=null,e.groupTotalDic={},e.numLoadedDic={},e.itemListDic={},e.groupErrorDic={},e.retryTimesDic={},e.maxRetryTimes=3,e.failedList=new Array,e.priorityQueue={},e.lazyLoadList=new Array,e.analyzerDic={},e.queueIndex=0,e}return __extends(r,t),r.prototype.isGroupInLoading=function(e){return void 0!==this.itemListDic[e]},r.prototype.loadGroup=function(t,r,i){if(void 0===i&&(i=0),!this.itemListDic[r]&&r){if(!t||0==t.length){egret.$warn(3201,r);var n=new e.ResourceEvent(e.ResourceEvent.GROUP_LOAD_ERROR);return n.groupName=r,void this.dispatchEvent(n)}this.priorityQueue[i]?this.priorityQueue[i].push(r):this.priorityQueue[i]=[r],this.itemListDic[r]=t;for(var o=t.length,s=0;o>s;s++){var a=t[s];a.groupName=r}this.groupTotalDic[r]=t.length,this.numLoadedDic[r]=0,this.next()}},r.prototype.loadItem=function(e){this.lazyLoadList.push(e),e.groupName="",this.next()},r.prototype.next=function(){for(;this.loadingCount<this.thread;){var e=this.getOneResourceItem();if(!e)break;if(this.loadingCount++,e.loaded)this.onItemComplete(e);else{var t=this.resInstance.$getAnalyzerByType(e.type);t.loadFile(e,this.onItemComplete,this)}}},r.prototype.getOneResourceItem=function(){if(this.failedList.length>0)return this.failedList.shift();var e=Number.NEGATIVE_INFINITY;for(var t in this.priorityQueue)e=Math.max(e,t);var r=this.priorityQueue[e];if(!r||0==r.length)return 0==this.lazyLoadList.length?null:this.lazyLoadList.pop();for(var i,n=r.length,o=0;n>o&&(this.queueIndex>=n&&(this.queueIndex=0),i=this.itemListDic[r[this.queueIndex]],!(i.length>0));o++)this.queueIndex++;return 0==i.length?null:i.shift()},r.prototype.onItemComplete=function(t){this.loadingCount--;var r=t.groupName;if(!t.loaded){var i=this.retryTimesDic[t.name]||1;if(!(i>this.maxRetryTimes))return this.retryTimesDic[t.name]=i+1,this.failedList.push(t),void this.next();delete this.retryTimesDic[t.name],e.ResourceEvent.dispatchResourceEvent(this.resInstance,e.ResourceEvent.ITEM_LOAD_ERROR,r,t)}if(r){this.numLoadedDic[r]++;var n=this.numLoadedDic[r],o=this.groupTotalDic[r];if(t.loaded||(this.groupErrorDic[r]=!0),e.ResourceEvent.dispatchResourceEvent(this.resInstance,e.ResourceEvent.GROUP_PROGRESS,r,t,n,o),n==o){var s=this.groupErrorDic[r];this.removeGroupName(r),delete this.groupTotalDic[r],delete this.numLoadedDic[r],delete this.itemListDic[r],delete this.groupErrorDic[r],s?e.ResourceEvent.dispatchResourceEvent(this,e.ResourceEvent.GROUP_LOAD_ERROR,r):e.ResourceEvent.dispatchResourceEvent(this,e.ResourceEvent.GROUP_COMPLETE,r)}}else this.callBack.call(this.resInstance,t);this.next()},r.prototype.removeGroupName=function(e){for(var t in this.priorityQueue){for(var r=this.priorityQueue[t],i=0,n=!1,o=r.length,s=0;o>s;s++){var a=r[s];if(a==e){r.splice(i,1),n=!0;break}i++}if(n){0==r.length&&delete this.priorityQueue[t];break}}},r}(egret.EventDispatcher);e.ResourceLoader=t,__reflect(t.prototype,"RES.ResourceLoader")}(RES||(RES={}));var RES;!function(e){var t=function(e){function t(){return e.call(this)||this}return __extends(t,e),t.prototype.analyzeConfig=function(e,t){var r,i=e.name,n="";try{var o=t;r=JSON.parse(o)}catch(s){}return r?n=this.getRelativePath(e.url,r.file):(r=t,n=this.getTexturePath(e.url,r)),this.sheetMap[i]=r,n},t.prototype.analyzeBitmap=function(e,t){var r=e.name;if(!this.fileDic[r]&&t){var i=this.sheetMap[r];delete this.sheetMap[r];var n=new egret.BitmapFont(t,i);this.fileDic[r]=n}},t.prototype.getTexturePath=function(e,t){var r="",i=t.split("\n"),n=i[2],o=n.indexOf('file="');return-1!=o&&(n=n.substring(o+6),o=n.indexOf('"'),r=n.substring(0,o)),e=e.split("\\").join("/"),o=e.lastIndexOf("/"),e=-1!=o?e.substring(0,o+1)+r:r},t.prototype.onResourceDestroy=function(e){e&&e.dispose()},t}(e.SheetAnalyzer);e.FontAnalyzer=t,__reflect(t.prototype,"RES.FontAnalyzer")}(RES||(RES={}));var RES;!function(e){var t=function(){function t(){this.keyMap={},this.groupDic={},e.configInstance=this}return t.prototype.getGroupByName=function(e){var t=new Array;if(!this.groupDic[e])return t;for(var r=this.groupDic[e],i=r.length,n=0;i>n;n++){var o=r[n];t.push(this.parseResourceItem(o))}return t},t.prototype.getRawGroupByName=function(e){return this.groupDic[e]?this.groupDic[e]:[]},t.prototype.createGroup=function(e,t,r){if(void 0===r&&(r=!1),!r&&this.groupDic[e]||!t||0==t.length)return!1;for(var i=this.groupDic,n=[],o=t.length,s=0;o>s;s++){var a=t[s],u=i[a];if(u)for(var c=u.length,l=0;c>l;l++){var p=u[l];-1==n.indexOf(p)&&n.push(p)}else{var p=this.keyMap[a];p?-1==n.indexOf(p)&&n.push(p):egret.$warn(3200,a)}}return 0==n.length?!1:(this.groupDic[e]=n,!0)},t.prototype.parseConfig=function(e,t){if(e){var r=e.resources;if(r)for(var i=r.length,n=0;i>n;n++){var o=r[n],s=o.url;s&&-1==s.indexOf("://")&&(o.url=t+s),this.addItemToKeyMap(o)}var a=e.groups;if(a)for(var u=a.length,n=0;u>n;n++){for(var c=a[n],l=[],p=c.keys.split(","),h=p.length,f=0;h>f;f++){var d=p[f].trim(),o=this.keyMap[d];o&&-1==l.indexOf(o)&&l.push(o)}this.groupDic[c.name]=l}}},t.prototype.addSubkey=function(e,t){var r=this.keyMap[t];r&&!this.keyMap[e]&&(this.keyMap[e]=r)},t.prototype.addItemToKeyMap=function(e){if(this.keyMap[e.name]||(this.keyMap[e.name]=e),e.hasOwnProperty("subkeys")){var t=e.subkeys.split(",");e.subkeys=t;for(var r=t.length,i=0;r>i;i++){var n=t[i];null==this.keyMap[n]&&(this.keyMap[n]=e)}}},t.prototype.getName=function(e){var t=this.keyMap[e];return t?t.name:""},t.prototype.getType=function(e){var t=this.keyMap[e];return t?t.type:""},t.prototype.getRawResourceItem=function(e){return this.keyMap[e]},t.prototype.getResourceItem=function(e){var t=this.keyMap[e];return t?this.parseResourceItem(t):null},t.prototype.parseResourceItem=function(t){var r=new e.ResourceItem(t.name,t.url,t.type);return r.data=t,r},t}();e.ResourceConfig=t,__reflect(t.prototype,"RES.ResourceConfig")}(RES||(RES={}));var RES;!function(e){var t=function(e){function t(){var t=e.call(this)||this;return t._dataFormat=egret.HttpResponseType.TEXT,t}return __extends(t,e),t.prototype.analyzeData=function(e,t){var r=e.name;if(!this.fileDic[r]&&t)try{var i=t,n=egret.XML.parse(i);this.fileDic[r]=n}catch(o){}},t}(e.BinAnalyzer);e.XMLAnalyzer=t,__reflect(t.prototype,"RES.XMLAnalyzer")}(RES||(RES={}));var RES;!function(e){var t=function(e){function t(t,r,i){void 0===r&&(r=!1),void 0===i&&(i=!1);var n=e.call(this,t,r,i)||this;return n.itemsLoaded=0,n.itemsTotal=0,n.groupName="",n.resItem=null,n}return __extends(t,e),t.dispatchResourceEvent=function(e,r,i,n,o,s){void 0===i&&(i=""),void 0===n&&(n=null),void 0===o&&(o=0),void 0===s&&(s=0);var a=egret.Event.create(t,r);a.groupName=i,a.resItem=n,a.itemsLoaded=o,a.itemsTotal=s;var u=e.dispatchEvent(a);return egret.Event.release(a),u},t}(egret.Event);t.ITEM_LOAD_ERROR="itemLoadError",t.CONFIG_COMPLETE="configComplete",t.CONFIG_LOAD_ERROR="configLoadError",t.GROUP_PROGRESS="groupProgress",t.GROUP_COMPLETE="groupComplete",t.GROUP_LOAD_ERROR="groupLoadError",e.ResourceEvent=t,__reflect(t.prototype,"RES.ResourceEvent")}(RES||(RES={}));var RES;!function(e){var t;!function(t){var r=function(t){function r(){var e=t.call(this)||this;return e._versionInfo={},e}return __extends(r,t),r.prototype.fetchVersion=function(e){e.onSuccess(null)},r.prototype.getChangeList=function(){return[]},r.prototype.getVirtualUrl=function(t){return t.length>20&&("http://"===t.substr(0,7)||"https://"===t.substr(0,8))?t:e.getDomain()+t},r}(egret.EventDispatcher);t.Html5VersionController=r,__reflect(r.prototype,"RES.web.Html5VersionController",["RES.VersionController","RES.IVersionController"]),egret.Capabilities.runtimeType==egret.RuntimeType.WEB&&(e.VersionController=r)}(t=e.web||(e.web={}))}(RES||(RES={}));var RES;!function(e){var t;!function(t){var r=function(){function e(){this._versionInfo={},this._versionPath="",this._localFileArr=[]}return e.prototype.fetchVersion=function(e){var t=this;if(t._versionPath="all.manifest",t._versionInfo=t.getLocalData(t._versionPath),null==t._versionInfo)return void egret.callLater(function(){e.onFail(1,null)},t);var r=0,i=function(i){if(i)for(var n=0;n<i.length;n++)i[n]&&""!=i[n]&&t._localFileArr.push("resource/"+i[n]);r++,2==r&&e.onSuccess(null)};t.getList(i,"assets","resource"),t.getList(i,"update","resource")},e.prototype.getList=function(e,t,r){void 0===r&&(r="");var i=egret.PromiseObject.create();i.onSuccessFunc=function(t){e(t)},i.onErrorFunc=function(){console.error("list files error")},"assets"==t?egret_native.Game.listResource(r,i):egret_native.Game.listUpdate(r,i)},e.prototype.getChangeList=function(){var e=[],t=this._localFileArr;for(var r in this._versionInfo)t.indexOf(this.getVirtualUrl(r))<0&&e.push({url:this.getVirtualUrl(r),size:this._versionInfo[r].s});return e},e.prototype.getVirtualUrl=function(e){return this._versionInfo&&this._versionInfo[e]?"resource/"+this._versionInfo[e].v.substring(0,2)+"/"+this._versionInfo[e].v+"_"+this._versionInfo[e].s+"."+e.substring(e.lastIndexOf(".")+1):e},e.prototype.getLocalData=function(e){if(egret_native.readUpdateFileSync&&egret_native.readResourceFileSync){var t=egret_native.readUpdateFileSync(e);if(null!=t)return JSON.parse(t);if(t=egret_native.readResourceFileSync(e),null!=t)return JSON.parse(t)}return null},e}();t.NativeVersionController=r,__reflect(r.prototype,"RES.native.NativeVersionController",["RES.VersionController","RES.IVersionController"]),egret.Capabilities.runtimeType==egret.RuntimeType.NATIVE&&(e.VersionController=r)}(t=e["native"]||(e["native"]={}))}(RES||(RES={}));var RES;!function(e){var t=function(t){function r(){var e=t.call(this)||this;return e.soundDic={},e.resItemDic=[],e}return __extends(r,t),r.prototype.loadFile=function(t,r,i){if(this.soundDic[t.name])return void r.call(i,t);var n=new egret.Sound;n.addEventListener(egret.Event.COMPLETE,this.onLoadFinish,this),n.addEventListener(egret.IOErrorEvent.IO_ERROR,this.onLoadFinish,this),this.resItemDic[n.$hashCode]={item:t,func:r,thisObject:i},n.load(e.$getVirtualUrl(t.url)),t.data&&(n.type=t.data.soundType)},r.prototype.onLoadFinish=function(e){var t=e.$target;t.removeEventListener(egret.Event.COMPLETE,this.onLoadFinish,this),t.removeEventListener(egret.IOErrorEvent.IO_ERROR,this.onLoadFinish,this);var r=this.resItemDic[t.$hashCode];delete this.resItemDic[t.$hashCode];var i=r.item,n=r.func;i.loaded=e.$type==egret.Event.COMPLETE,i.loaded&&this.analyzeData(i,t),n.call(r.thisObject,i)},r.prototype.analyzeData=function(e,t){var r=e.name;!this.soundDic[r]&&t&&(this.soundDic[r]=t)},r.prototype.getRes=function(e){return this.soundDic[e]},r.prototype.hasRes=function(e){return!!this.getRes(e)},r.prototype.destroyRes=function(e){return this.soundDic[e]?(delete this.soundDic[e],!0):!1},r}(e.AnalyzerBase);e.SoundAnalyzer=t,__reflect(t.prototype,"RES.SoundAnalyzer")}(RES||(RES={}));var RES;!function(e){function t(e,t){T.registerAnalyzer(e,t)}function r(e){return T.$getAnalyzerByType(e)}function i(e){T.$registerVersionController(e)}function n(){return T.vcs}function o(e,t,r){void 0===t&&(t=""),void 0===r&&(r="json"),T.loadConfig(e,t,r)}function s(e,t){void 0===t&&(t=0),T.loadGroup(e,t)}function a(e){return T.isGroupLoaded(e)}function u(e){return T.getGroupByName(e)}function c(e,t,r){return void 0===r&&(r=!1),T.createGroup(e,t,r)}function l(e){return T.hasRes(e)}function p(e){return T.hasRawRes(e)}function h(e){T.domain=e}function f(){return T.domain}function d(e,t){void 0===t&&(t=""),T.parseConfig(e,t)}function y(e){return T.getRes(e)}function g(e,t,r){T.getResAsync(e,t,r)}function v(e,t,r,i){void 0===i&&(i=""),T.getResByUrl(e,t,r,i)}function R(e,t){return T.destroyRes(e,t)}function m(e){T.setMaxLoadingThread(e)}function E(e){T.setMaxRetryTimes(e)}function _(e,t,r,i,n){void 0===i&&(i=!1),void 0===n&&(n=0),T.addEventListener(e,t,r,i,n)}function I(e,t,r,i){void 0===i&&(i=!1),T.removeEventListener(e,t,r,i)}function L(e){return T.vcs?T.vcs.getVirtualUrl(e):e}e.registerAnalyzer=t,e.getAnalyzer=r,e.registerVersionController=i,e.getVersionController=n,e.loadConfig=o,e.loadGroup=s,e.isGroupLoaded=a,e.getGroupByName=u,e.createGroup=c,e.hasRes=l,e.hasRawRes=p,e.setDomain=h,e.getDomain=f,e.parseConfig=d,e.getRes=y,e.getResAsync=g,e.getResByUrl=v,e.destroyRes=R,e.setMaxLoadingThread=m,e.setMaxRetryTimes=E,e.addEventListener=_,e.removeEventListener=I,e.$getVirtualUrl=L;var D=function(t){function r(){var e=t.call(this)||this;return e.analyzerDic={},e.analyzerClassMap={},e.configItemList=[],e.callLaterFlag=!1,e.configComplete=!1,e.loadedGroups=[],e.groupNameList=[],e.asyncDic={},e._loadedUrlTypes={},e.domain="",e.init(),e}return __extends(r,t),r.prototype.$getAnalyzerByType=function(e){var t=this.analyzerDic[e];if(!t){var r=this.analyzerClassMap[e];if(!r)return null;t=this.analyzerDic[e]=new r}return t},r.prototype.registerAnalyzer=function(e,t){this.analyzerClassMap[e]=t},r.prototype.$registerVersionController=function(e){this.vcs=e},r.prototype.init=function(){this.vcs=new e.VersionController;var t=this.analyzerClassMap;t[e.ResourceItem.TYPE_BIN]=e.BinAnalyzer,t[e.ResourceItem.TYPE_IMAGE]=e.ImageAnalyzer,t[e.ResourceItem.TYPE_TEXT]=e.TextAnalyzer,t[e.ResourceItem.TYPE_JSON]=e.JsonAnalyzer,t[e.ResourceItem.TYPE_SHEET]=e.SheetAnalyzer,t[e.ResourceItem.TYPE_FONT]=e.FontAnalyzer,t[e.ResourceItem.TYPE_SOUND]=e.SoundAnalyzer,t[e.ResourceItem.TYPE_XML]=e.XMLAnalyzer,this.resConfig=new e.ResourceConfig,this.resLoader=new e.ResourceLoader,this.resLoader.callBack=this.onResourceItemComp,this.resLoader.resInstance=this,this.resLoader.addEventListener(e.ResourceEvent.GROUP_COMPLETE,this.onGroupComp,this),this.resLoader.addEventListener(e.ResourceEvent.GROUP_LOAD_ERROR,this.onGroupError,this)},r.prototype.loadConfig=function(e,t,r){void 0===r&&(r="json");var i={url:e,resourceRoot:t,type:r};this.configItemList.push(i),this.callLaterFlag||(egret.callLater(this.startLoadConfig,this),this.callLaterFlag=!0)},r.prototype.startLoadConfig=function(){var t=this;this.callLaterFlag=!1;var i=this.configItemList;this.configItemList=[],this.loadingConfigList=i;for(var n=i.length,o=[],s=0;n>s;s++){var a=i[s],u=new e.ResourceItem(a.url,a.url,a.type);o.push(u)}var c={onSuccess:function(e){t.resLoader.loadGroup(o,r.GROUP_CONFIG,Number.MAX_VALUE)},onFail:function(r,i){e.ResourceEvent.dispatchResourceEvent(t,e.ResourceEvent.CONFIG_LOAD_ERROR)}};this.vcs?this.vcs.fetchVersion(c):this.resLoader.loadGroup(o,r.GROUP_CONFIG,Number.MAX_VALUE)},r.prototype.isGroupLoaded=function(e){return-1!=this.loadedGroups.indexOf(e)},r.prototype.getGroupByName=function(e){return this.resConfig.getGroupByName(e)},r.prototype.loadGroup=function(t,r){if(void 0===r&&(r=0),-1!=this.loadedGroups.indexOf(t))return void e.ResourceEvent.dispatchResourceEvent(this,e.ResourceEvent.GROUP_COMPLETE,t);if(!this.resLoader.isGroupInLoading(t))if(this.configComplete){var i=this.resConfig.getGroupByName(t);this.resLoader.loadGroup(i,t,r)}else this.groupNameList.push({name:t,priority:r})},r.prototype.createGroup=function(e,t,r){if(void 0===r&&(r=!1),r){var i=this.loadedGroups.indexOf(e);-1!=i&&this.loadedGroups.splice(i,1)}return this.resConfig.createGroup(e,t,r)},r.prototype.onGroupComp=function(t){if(t.groupName==r.GROUP_CONFIG){for(var i=this.loadingConfigList.length,n=0;i>n;n++){var o=this.loadingConfigList[n],s=this.$getAnalyzerByType(o.type),a=s.getRes(o.url);s.destroyRes(o.url),this.resConfig.parseConfig(a,o.resourceRoot)}this.configComplete=!0,this.loadingConfigList=null,e.ResourceEvent.dispatchResourceEvent(this,e.ResourceEvent.CONFIG_COMPLETE),this.loadDelayGroups()}else this.loadedGroups.push(t.groupName),this.dispatchEvent(t)},r.prototype.loadDelayGroups=function(){var e=this.groupNameList;this.groupNameList=[];for(var t=e.length,r=0;t>r;r++){var i=e[r];this.loadGroup(i.name,i.priority)}},r.prototype.onGroupError=function(t){t.groupName==r.GROUP_CONFIG?(this.loadingConfigList=null,e.ResourceEvent.dispatchResourceEvent(this,e.ResourceEvent.CONFIG_LOAD_ERROR)):this.dispatchEvent(t)},r.prototype.hasRes=function(t){var r=this.resConfig.getType(t);if(""==r){var i=e.AnalyzerBase.getStringTail(t);if(r=this.resConfig.getType(i),""==r)return!1}return!0},r.prototype.parseConfig=function(e,t){this.resConfig.parseConfig(e,t),this.configComplete||this.loadingConfigList||(this.configComplete=!0,this.loadDelayGroups())},r.prototype.getRes=function(t){var r=this.resConfig.getType(t);if(""==r){var i=e.AnalyzerBase.getStringPrefix(t);if(r=this.resConfig.getType(i),""==r)return null}var n=this.$getAnalyzerByType(r);return n.getRes(t)},r.prototype.getResAsync=function(t,r,i){var n=this.resConfig.getType(t),o=this.resConfig.getName(t);if(""==n&&(o=e.AnalyzerBase.getStringPrefix(t),n=this.resConfig.getType(o),""==n))return void egret.$callAsync(r,i);var s=this.$getAnalyzerByType(n),a=s.getRes(t);if(a)return void egret.$callAsync(r,i,a,t);var u={key:t,compFunc:r,thisObject:i};if(this.asyncDic[o])this.asyncDic[o].push(u);else{this.asyncDic[o]=[u];var c=this.resConfig.getResourceItem(o);this.resLoader.loadItem(c)}},r.prototype.getResByUrl=function(t,r,i,n){if(void 0===n&&(n=""),!t)return void egret.$callAsync(r,i);n||(n=this.getTypeByUrl(t)),null!=this._loadedUrlTypes[t]&&this._loadedUrlTypes[t]!=n&&egret.$warn(3202),this._loadedUrlTypes[t]=n;var o=this.$getAnalyzerByType(n),s=t,a=o.getRes(s);if(a)return void egret.$callAsync(r,i,a,t);var u={key:s,compFunc:r,thisObject:i};if(this.asyncDic[s])this.asyncDic[s].push(u);else{this.asyncDic[s]=[u];var c=new e.ResourceItem(s,t,n);this.resLoader.loadItem(c)}},r.prototype.getTypeByUrl=function(t){var r=t.substr(t.lastIndexOf(".")+1);r&&(r=r.toLowerCase());var i;switch(r){case e.ResourceItem.TYPE_XML:case e.ResourceItem.TYPE_JSON:case e.ResourceItem.TYPE_SHEET:i=r;break;case"png":case"jpg":case"gif":case"jpeg":case"bmp":i=e.ResourceItem.TYPE_IMAGE;break;case"fnt":i=e.ResourceItem.TYPE_FONT;break;case"txt":i=e.ResourceItem.TYPE_TEXT;break;case"mp3":case"ogg":case"mpeg":case"wav":case"m4a":case"mp4":case"aiff":case"wma":case"mid":i=e.ResourceItem.TYPE_SOUND;break;default:i=e.ResourceItem.TYPE_BIN}return i},r.prototype.onResourceItemComp=function(e){var t=this.asyncDic[e.name];delete this.asyncDic[e.name];for(var r=this.$getAnalyzerByType(e.type),i=t.length,n=0;i>n;n++){var o=t[n],s=r.getRes(o.key);o.compFunc.call(o.thisObject,s,o.key)}},r.prototype.destroyRes=function(e,t){void 0===t&&(t=!0);var r=this.resConfig.getRawGroupByName(e);if(r&&r.length>0){var i=this.loadedGroups.indexOf(e);-1!=i&&this.loadedGroups.splice(i,1);for(var n=r.length,o=0;n>o;o++){var s=r[o];if(!t&&this.isResInLoadedGroup(s.name));else{s.loaded=!1;var a=this.$getAnalyzerByType(s.type);a.destroyRes(s.name),this.removeLoadedGroupsByItemName(s.name)}}return!0}var u=this.resConfig.getType(e);if(""==u){if(u=this._loadedUrlTypes[e],null==u||""==u)return!1;delete this._loadedUrlTypes[e];var c=this.$getAnalyzerByType(u);return c.destroyRes(e),!0}var s=this.resConfig.getRawResourceItem(e);s.loaded=!1;var a=this.$getAnalyzerByType(u),l=a.destroyRes(e);return this.removeLoadedGroupsByItemName(s.name),l},r.prototype.removeLoadedGroupsByItemName=function(e){for(var t=this.loadedGroups,r=t.length,i=0;r>i;i++)for(var n=this.resConfig.getRawGroupByName(t[i]),o=n.length,s=0;o>s;s++){var a=n[s];if(a.name==e){t.splice(i,1),i--,r=t.length;break}}},r.prototype.isResInLoadedGroup=function(e){for(var t=this.loadedGroups,r=t.length,i=0;r>i;i++)for(var n=this.resConfig.getRawGroupByName(t[i]),o=n.length,s=0;o>s;s++){var a=n[s];if(a.name==e)return!0}return!1},r.prototype.setMaxLoadingThread=function(e){1>e&&(e=1),this.resLoader.thread=e},r.prototype.setMaxRetryTimes=function(e){e=Math.max(e,0),this.resLoader.maxRetryTimes=e},r.prototype.hasRawRes=function(e){return null!=this.resConfig.getRawResourceItem(e)},r}(egret.EventDispatcher);D.GROUP_CONFIG="RES__CONFIG",__reflect(D.prototype,"Resource");var T=new D}(RES||(RES={}));var RES;!function(e){var t=function(t){function r(){var e=t.call(this)||this;return e.sheetMap={},e.recyclerIamge=[],e._dataFormat=egret.HttpResponseType.TEXT,e}return __extends(r,t),r.prototype.onLoadFinish=function(e){var t=e.target,r=this.resItemDic[t.$hashCode];delete this.resItemDic[t.hashCode];var i=r.item,n=r.func;if(i.loaded=e.type==egret.Event.COMPLETE,i.loaded)if(t instanceof egret.HttpRequest){i.loaded=!1;var o=this.analyzeConfig(i,t.response);if(o)return this.loadImage(o,r),void this.recycler.push(t)}else this.analyzeBitmap(i,t.data);t instanceof egret.HttpRequest?this.recycler.push(t):this.recyclerIamge.push(t),n.call(r.thisObject,i)},r.prototype.analyzeConfig=function(e,t){var r,i=e.name,n="";try{var o=t;r=JSON.parse(o)}catch(s){egret.$warn(1017,e.url,t)}if(r)if(this.sheetMap[i]=r,r.file)n=this.getRelativePath(e.url,r.file);else{var a=e.url.split("?"),u=a[0].split("/");u[u.length-1]=u[u.length-1].split(".")[0]+".png",n="";for(var c=0;c<u.length;c++)n+=u[c]+(c<u.length-1?"/":"");2==a.length&&(n+=a[2])}return n},r.prototype.analyzeBitmap=function(e,t){var r=e.name;if(!this.fileDic[r]&&t){var i=this.sheetMap[r];delete this.sheetMap[r];var n=e.data&&e.data.subkeys?"":r,o=this.parseAnimation(t,i,n);this.fileDic[r]=o}},r.prototype.getRelativePath=function(e,t){e=e.split("\\").join("/");var r=e.lastIndexOf("/");return e=-1!=r?e.substring(0,r+1)+t:t},r.prototype.parseAnimation=function(e,t,r){for(var i,n=Object.keys(t.mc),o=t.mc[n[0]].frames,s=o.length,a=[],u=0;s>u;u++){i=t.res[o[u].res];var c=new egret.Texture;c._bitmapData=e,c.$initData(i.x,i.y,i.w,i.h,o[u].x,o[u].y,o[u].sourceW,o[u].sourceH,e.width,e.height)}return a},r.prototype.destroyRes=function(e){var t=this.fileDic[e];return t?(delete this.fileDic[e],!0):!1},r.prototype.loadImage=function(t,r){var i=this.getImageLoader();this.resItemDic[i.hashCode]=r,i.load(e.$getVirtualUrl(t))},r.prototype.getImageLoader=function(){var e=this.recyclerIamge.pop();return e||(e=new egret.ImageLoader,e.addEventListener(egret.Event.COMPLETE,this.onLoadFinish,this),e.addEventListener(egret.IOErrorEvent.IO_ERROR,this.onLoadFinish,this)),e},r}(e.BinAnalyzer);e.AnimationAnalyzer=t,__reflect(t.prototype,"RES.AnimationAnalyzer")}(RES||(RES={}));var egret;!function(e){e.$locale_strings=e.$locale_strings||{},e.$locale_strings.en_US=e.$locale_strings.en_US||{};var t=e.$locale_strings.en_US;t[3200]="RES.createGroup() passed in non-existed key value in configuration: {0}",t[3201]='RES loaded non-existed or empty resource group:"{0}"',t[3202]="Do not use the different types of ways to load the same material!",t[3203]="Can't find the analyzer of the specified file type:{0}。 Please register the specified analyzer in the initialization of the project first,then start the resource loading process。"}(egret||(egret={}));var egret;!function(e){e.$locale_strings=e.$locale_strings||{},e.$locale_strings.zh_CN=e.$locale_strings.zh_CN||{};var t=e.$locale_strings.zh_CN;t[3200]="RES.createGroup()传入了配置中不存在的键值: {0}",t[3201]='RES加载了不存在或空的资源组:"{0}"',t[3202]="请不要使用不同的类型方式来加载同一个素材！",t[3203]="找不到指定文件类型的解析器:{0}。 请先在项目初始化里注册指定文件类型的解析器，再启动资源加载。"}(egret||(egret={}));