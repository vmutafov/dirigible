package org.eclipse.dirigible.turbofan.core.graal.modules;

import java.nio.file.Path;

public interface ModuleResolver {

    boolean isResolvable(String moduleToResolve);

    Path resolve(String moduleToResolve);
}
