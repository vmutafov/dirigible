/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company and Eclipse Dirigible contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-FileCopyrightText: 2022 SAP SE or an SAP affiliate company and Eclipse Dirigible contributors
 * SPDX-License-Identifier: EPL-2.0
 */
const DirigibleSourceProvider = Java.type("org.eclipse.dirigible.engine.js.graalium.platform.internal.modules.DirigibleSourceProvider");
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
        if (path === 'jasmine/jasmine-4.1.1') {
            path = '/Users/c5326377/work/dirigible/dirigible/ext/ext-modules/ext-jasmine/src/main/resources/META-INF/dirigible/jasmine/jasmine-4.1.1.js';
        } else if (path === 'jasmine/jasmine-boot') {
            path = '/Users/c5326377/work/dirigible/dirigible/ext/ext-modules/ext-jasmine/src/main/resources/META-INF/dirigible/jasmine/jasmine-boot.js';
        }


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
