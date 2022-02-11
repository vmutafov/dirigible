package org.eclipse.dirigible.engine.js.graalvm.execution;

import java.nio.file.Path;

public interface CodeRunner {
    void run(Path codeFilePath);
}
