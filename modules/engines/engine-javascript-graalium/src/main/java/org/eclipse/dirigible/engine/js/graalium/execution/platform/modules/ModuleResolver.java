package org.eclipse.dirigible.engine.js.graalium.execution.platform.modules;

import java.nio.file.Path;

public interface ModuleResolver {

    boolean isResolvable(String moduleToResolve);

    Path resolve(String moduleToResolve);
}
