package org.eclipse.dirigible.turbofan.web.dirigible;

import org.eclipse.dirigible.commons.config.Configuration;
import org.eclipse.dirigible.turbofan.core.CodeRunner;
import org.eclipse.dirigible.turbofan.core.javascript.GraalJSCodeRunner;
import org.eclipse.dirigible.turbofan.core.dirigible.modules.DirigibleModuleResolver;
import org.eclipse.dirigible.turbofan.core.dirigible.globals.DirigibleContextGlobalObject;
import org.eclipse.dirigible.turbofan.core.dirigible.globals.DirigibleEngineTypeGlobalObject;
import org.eclipse.dirigible.turbofan.core.dirigible.polyfills.RequirePolyfill;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import java.nio.file.Path;
import java.util.HashMap;

public class CodeRunnerFactory {

    private CodeRunnerFactory() {
    }

    public static CodeRunner<Source, Value> createDirigibleJSCodeRunner(Path workingDirectoryPath) {
        var cachePath = workingDirectoryPath.resolve("caches");
        var coreModulesESMProxiesCachePath = cachePath.resolve("core-modules-proxies-cache");

        return GraalJSCodeRunner.newBuilder(workingDirectoryPath, cachePath)
                .addJSPolyfill(new RequirePolyfill())
                .addGlobalObject(new DirigibleContextGlobalObject(new HashMap<>()))
                .addGlobalObject(new DirigibleEngineTypeGlobalObject())
                .addModuleResolver(new DirigibleModuleResolver(coreModulesESMProxiesCachePath))
                .waitForDebugger(CodeRunnerFactory::shouldEnableDebug)
                .build();
    }

    private static boolean shouldEnableDebug() {
        return Configuration.get("DIRIGIBLE_TURBOFAN_ENABLE_DEBUG", Boolean.FALSE.toString()).equals(Boolean.TRUE.toString());
    }
}
