package org.eclipse.dirigible.engine.js.graalvm.execution.js;

import org.eclipse.dirigible.engine.js.graalvm.callbacks.Require;
import org.eclipse.dirigible.engine.js.graalvm.execution.CodeRunner;
import org.eclipse.dirigible.engine.js.graalvm.execution.js.modules.DirigibleCoreModuleProvider;
import org.eclipse.dirigible.engine.js.graalvm.execution.js.platform.GraalJSContextCreator;
import org.eclipse.dirigible.engine.js.graalvm.execution.js.platform.GraalJSEngineCreator;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class GraalJSCodeRunner implements CodeRunner {

    private final Path currentWorkingDirectoryPath;
    private final GraalJSCodeRunListener codeRunListener;
    private final Engine graalEngine;
    private final Context graalContext;
    private final DirigibleCoreModuleProvider dirigibleCoreModuleProvider;

    public GraalJSCodeRunner(Path currentWorkingDirectoryPath, Boolean isDebugMode) {
        this(currentWorkingDirectoryPath, new GraalJSCodeRunListener(), isDebugMode);
    }

    public GraalJSCodeRunner(Path currentWorkingDirectoryPath, GraalJSCodeRunListener codeRunListener, Boolean isDebugMode) {
        this.codeRunListener = codeRunListener;
        this.currentWorkingDirectoryPath = currentWorkingDirectoryPath;
        graalEngine = isDebugMode ? GraalJSEngineCreator.getOrCreateDebuggableEngine() : GraalJSEngineCreator.getOrCreateEngine();
        graalContext = GraalJSContextCreator.createContext(graalEngine, currentWorkingDirectoryPath);
        dirigibleCoreModuleProvider = new DirigibleCoreModuleProvider();
        addRequirePolyfill();
    }

    private void addRequirePolyfill() {
        try {
            graalContext.getBindings("js").putMember("SourceProvider", dirigibleCoreModuleProvider);

            Source requireSource = Source
                    .newBuilder("js", Require.CODE, "require.js")
                    .internal(true)
                    .cached(true)
                    .build();

            Source dirigibleRequireSource = Source
                    .newBuilder("js", Require.DIRIGIBLE_REQUIRE_CODE, "require.js")
                    .internal(true)
                    .cached(true)
                    .build();

            graalContext.eval(requireSource);
            graalContext.eval(dirigibleRequireSource); // alias of Require.CODE
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run(Path codeFilePath) {
        Source codeSource = getSource(codeFilePath);
        Value result = graalContext.eval(codeSource);
        System.out.println(1);
    }

    private Source getSource(Path codeFilePath) {
        try {
            Path relativeCodeFilePath = currentWorkingDirectoryPath.resolve(codeFilePath);
            File codeFile = relativeCodeFilePath.toFile();
            return Source.newBuilder("js", codeFile)
                    .cached(true)
                    .encoding(StandardCharsets.UTF_8)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
