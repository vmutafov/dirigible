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
globalThis.global = globalThis;
globalThis.window = globalThis;

const jasmine_boot = require('jasmine/jasmine-boot');
globalThis.boot();

describe("A suite is just a function", function () {
    console.log("!!!!! VM: in describe");
    var a;

    it("and so is a spec", function () {
        console.log("!!!!! VM: in it");
        a = true;
        expect(a).toBe(true);

        console.log("!!!!! VM: in it end");
    });
});

const testsPromise = env.execute();
const res = await testsPromise;

const a = 5;
//
//for (var propertyName in jasmine) {
//    exports[propertyName] = jasmine[propertyName];
//}