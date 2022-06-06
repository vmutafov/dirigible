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
package org.eclipse.dirigible.engine.js.graalium.platform;

import org.eclipse.dirigible.commons.config.StaticObjects;
import org.eclipse.dirigible.engine.js.graalium.CodeRunner;
import org.eclipse.dirigible.repository.local.LocalRepository;
import org.junit.Test;

import java.nio.file.Path;

import static org.junit.Assert.*;

public class DirigibleCodeRunnerFactoryTest {


    static {
        LocalRepository repo = new LocalRepository();
        StaticObjects.set(StaticObjects.REPOSITORY, repo);
    }

    @Test
    public void test() {
        Path jasmineDirPath = Path.of("/Users/c5326377/work/dirigible/dirigible/ext/ext-modules/ext-jasmine/src/main/resources/META-INF/dirigible/jasmine");
        Path jasminePath = Path.of("jasmine.mjs");
        CodeRunner codeRunner = DirigibleCodeRunnerFactory.createDirigibleJSCodeRunner(jasmineDirPath);
        codeRunner.run(jasminePath);
        int a = 5;
    }

}