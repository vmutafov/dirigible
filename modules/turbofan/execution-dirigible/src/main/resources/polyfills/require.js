const DirigibleSourceProvider = Java.type("org.eclipse.dirigible.turbofan.core.dirigible.modules.DirigibleSourceProvider");
const dirigibleSourceProvider = new DirigibleSourceProvider();

var Require = (function (modulePath) {
    var _loadedModules = {};
    var _require = function (path) {
        var moduleInfo, buffered, head = '(function(exports,module,require){ ',
            code = '',
            tail = '})',
            line = null;
        moduleInfo = _loadedModules[path];
        if (moduleInfo) {
            return moduleInfo;
        }
        code = dirigibleSourceProvider.getSource(path);

        moduleInfo = {
            loaded: false,
            id: path,
            exports: {},
            require: _requireClosure()
        };
        code = head + code + tail;
        _loadedModules[path] = moduleInfo;
        var compiledWrapper = load({
            name: path,
            script: code
        });
        var parameters = [moduleInfo.exports, /* exports */ moduleInfo, /* module */ moduleInfo.require /* require */];
        compiledWrapper.apply(moduleInfo.exports, /* this */ parameters);
        moduleInfo.loaded = true;
        return moduleInfo;
    };
    var _requireClosure = function () {
        return function (path) {
            var module = _require(path);
            return module.exports;
        };
    };
    return _requireClosure();
});
globalThis.require = Require();
globalThis.dirigibleRequire = globalThis.require;