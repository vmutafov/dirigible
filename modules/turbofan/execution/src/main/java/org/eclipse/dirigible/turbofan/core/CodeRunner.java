package org.eclipse.dirigible.turbofan.core;

import java.nio.file.Path;

public interface CodeRunner<TSource, TResult> extends AutoCloseable {
    TResult run(Path codeFilePath);

    TResult run(TSource codeSource);

    @Override
    void close();
}
