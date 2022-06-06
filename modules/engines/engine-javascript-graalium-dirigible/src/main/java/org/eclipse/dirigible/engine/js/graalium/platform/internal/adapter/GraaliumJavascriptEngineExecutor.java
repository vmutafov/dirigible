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
package org.eclipse.dirigible.engine.js.graalium.platform.internal.adapter;

import org.eclipse.dirigible.commons.api.scripting.ScriptingException;
import org.eclipse.dirigible.commons.config.StaticObjects;
import org.eclipse.dirigible.engine.api.script.AbstractScriptExecutor;
import org.eclipse.dirigible.engine.js.graalium.CodeRunner;
import org.eclipse.dirigible.engine.js.graalium.GraalJSCodeRunner;
import org.eclipse.dirigible.engine.js.graalium.core.GraalJSSourceCreator;
import org.eclipse.dirigible.engine.js.graalium.core.polyfills.RequirePolyfill;
import org.eclipse.dirigible.engine.js.graalium.platform.internal.modules.DirigibleSourceProvider;
import org.eclipse.dirigible.engine.js.graalium.platform.internal.polyfills.DirigibleContextGlobalObject;
import org.eclipse.dirigible.engine.js.graalium.platform.internal.polyfills.DirigibleEngineTypeGlobalObject;
import org.eclipse.dirigible.repository.api.IRepository;
import org.graalvm.polyglot.Source;

import java.nio.file.Path;
import java.util.Map;

public class GraaliumJavascriptEngineExecutor extends AbstractScriptExecutor {

    private static final IRepository REPOSITORY = (IRepository) StaticObjects.get(StaticObjects.REPOSITORY);
    private final DirigibleSourceProvider dirigibleSourceProvider = new DirigibleSourceProvider();

    @Override
    public Object executeServiceModule(String module, Map<Object, Object> executionContext) throws ScriptingException {
        Source moduleSource = loadModuleSource(module);
        ExecutableFile executableFile = createExecutableFile(module);
        try (CodeRunner codeRunner = createJSCodeRunner(executableFile.getWorkingDirectoryPath(), executionContext)) {
            return codeRunner.run(moduleSource).as(Object.class);
        }
    }

    @Override
    public Object evalModule(String module, Map<Object, Object> executionContext) throws ScriptingException {
        Source moduleSource = loadModuleSource(module);
        ExecutableFile executableFile = createExecutableFile(module);
        try (CodeRunner codeRunner = createJSCodeRunner(executableFile.getWorkingDirectoryPath(), executionContext)) {
            return codeRunner.run(moduleSource).as(Object.class);
        }
    }

    @Override
    public Object executeMethodFromModule(String module, String memberClass, String memberClassMethod, Map<Object, Object> executionContext) {
        Source moduleSource = loadModuleSource(module);
        ExecutableFile executableFile = createExecutableFile(module);
        try (CodeRunner codeRunner = createJSCodeRunner(executableFile.getWorkingDirectoryPath(), executionContext)) {
            return codeRunner.run(moduleSource).as(Object.class);
        }
    }

    private static ExecutableFile createExecutableFile(String module) {
        Path modulePath = Path.of(module);
        Path repositoryRootPath = Path.of(REPOSITORY.getRepositoryPath());
        Path projectDirectoryPath = repositoryRootPath.resolve(modulePath);

        Path executableFilePath = Path.of("/");
        for (int partIndex = 1; partIndex < modulePath.getNameCount(); partIndex += 1) {
            executableFilePath = executableFilePath.resolve(modulePath.getName(partIndex));
        }

        return new ExecutableFile(projectDirectoryPath, executableFilePath);
    }

    private static class ExecutableFile {
        private final Path workingDirectoryPath;
        private final Path executableFilePath;

        private ExecutableFile(Path workingDirectoryPath, Path executableFilePath) {
            this.workingDirectoryPath = workingDirectoryPath;
            this.executableFilePath = executableFilePath;
        }

        public Path getWorkingDirectoryPath() {
            return workingDirectoryPath;
        }

        public Path getExecutableFilePath() {
            return executableFilePath;
        }
    }

    private Source loadModuleSource(String module) {
        String moduleSourceString = dirigibleSourceProvider.getSource(module);
        return GraalJSSourceCreator.createSource(moduleSourceString, module);
    }

    @Override
    public Object executeServiceCode(String code, Map<Object, Object> executionContext) throws ScriptingException {
        Source source = GraalJSSourceCreator.createSource(code, "Unknown");
        try (CodeRunner codeRunner = createJSCodeRunner(Path.of(REPOSITORY.getRepositoryPath()), executionContext)) {
            return codeRunner.run(source).as(Object.class);
        }
    }

    @Override
    public Object evalCode(String code, Map<Object, Object> executionContext) throws ScriptingException {
        Source source = GraalJSSourceCreator.createSource(code, "Unknown");
        try (CodeRunner codeRunner = createJSCodeRunner(Path.of(REPOSITORY.getRepositoryPath()), executionContext)) {
            return codeRunner.run(source).as(Object.class);
        }
    }

    private static CodeRunner createJSCodeRunner(Path workingDirectoryPath, Map<Object, Object> executionContext) {
        return GraalJSCodeRunner.newBuilder(workingDirectoryPath, workingDirectoryPath.resolve("caches"))
                .addJSPolyfill(new RequirePolyfill())
                .addGlobalObject(new DirigibleContextGlobalObject(executionContext))
                .addGlobalObject(new DirigibleEngineTypeGlobalObject())
                .waitForDebugger(false)
                .build();
    }

    @Override
    public String getType() {
        return "graalium";
    }

    @Override
    public String getName() {
        return "Graalium JavaScript Engine";
    }
}
