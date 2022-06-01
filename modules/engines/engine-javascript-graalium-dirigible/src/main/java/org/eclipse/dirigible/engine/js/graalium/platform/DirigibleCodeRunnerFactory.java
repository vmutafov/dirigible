package org.eclipse.dirigible.engine.js.graalium.platform;

import org.eclipse.dirigible.engine.js.graalium.CodeRunner;
import org.eclipse.dirigible.engine.js.graalium.GraalJSCodeRunner;
import org.eclipse.dirigible.engine.js.graalium.core.polyfills.RequirePolyfill;
import org.eclipse.dirigible.engine.js.graalium.platform.internal.polyfills.DirigibleContextGlobalObject;
import org.eclipse.dirigible.engine.js.graalium.platform.internal.polyfills.DirigibleEngineTypeGlobalObject;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class DirigibleCodeRunnerFactory {

    private DirigibleCodeRunnerFactory() {
    }

    public static CodeRunner createDirigibleJSCodeRunner(Path workingDirectoryPath) {
        return createDirigibleJSCodeRunner(workingDirectoryPath, new HashMap<>());
    }

    public static CodeRunner createDirigibleJSCodeRunner(Path workingDirectoryPath, Map<Object, Object> executionContext) {
        return GraalJSCodeRunner.newBuilder(workingDirectoryPath, workingDirectoryPath.resolve("caches"))
                .addJSPolyfill(new RequirePolyfill())
                .addGlobalObject(new DirigibleContextGlobalObject(executionContext))
                .addGlobalObject(new DirigibleEngineTypeGlobalObject())
                .waitForDebugger(false)
                .build();
    }
}
