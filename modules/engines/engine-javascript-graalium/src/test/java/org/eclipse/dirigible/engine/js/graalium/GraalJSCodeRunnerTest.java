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
package org.eclipse.dirigible.engine.js.graalium;

import org.eclipse.dirigible.api.v3.test.AbstractApiSuiteTest;
import org.eclipse.dirigible.commons.config.StaticObjects;
import org.eclipse.dirigible.engine.js.graalium.execution.platform.polyfills.*;
import org.eclipse.dirigible.engine.js.graalium.core.polyfills.RequirePolyfill;
import org.eclipse.dirigible.repository.api.ICollection;
import org.eclipse.dirigible.repository.api.IRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.file.Path;

public class GraalJSCodeRunnerTest extends AbstractApiSuiteTest {

    private IRepository repository;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.repository = (IRepository) StaticObjects.get(StaticObjects.REPOSITORY);
    }

    @Test
    @Ignore
    public void testNewEngineWithDirigibleImports() {
        Path workingDir = Path.of("");
        GraalJSCodeRunner codeRunner = createCodeRunner(workingDir);

        Path codePath = Path.of("importDirigibleApi.mjs");
        codeRunner.run(codePath);
    }

    @Test
    @Ignore
    public void testNewEngineWithRelativeImports() {
        Path workingDir = Path.of("");
        GraalJSCodeRunner codeRunner = createCodeRunner(workingDir);

        Path codePath = Path.of("relativeImports/l12/l12.mjs");
        codeRunner.run(codePath);
    }

    private Path getOrCreateInternalFolder(String folderName) {
        ICollection folder = repository.getCollection(folderName);
        if (!folder.exists()) {
            folder.create();
        }

        String folderPathString = folder.getPath();
        String folderInternalPathString = repository.getInternalResourcePath(folderPathString);
        return Path.of(folderInternalPathString);
    }

    private GraalJSCodeRunner createCodeRunner(Path workingDir) {
        return GraalJSCodeRunner.newBuilder(workingDir, workingDir.resolve("caches"))
                .addJSPolyfill(new RequirePolyfill())
                .waitForDebugger(false)
                .build();
    }

    @After
    public void cleanUp() {
        repository.removeCollection("dependencies");
        repository.removeCollection("core-modules");
        repository.removeCollection("registry");
    }

}