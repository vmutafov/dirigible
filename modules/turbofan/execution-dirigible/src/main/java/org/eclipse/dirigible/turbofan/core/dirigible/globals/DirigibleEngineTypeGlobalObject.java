package org.eclipse.dirigible.turbofan.core.dirigible.globals;

import org.eclipse.dirigible.turbofan.core.graal.globals.JSGlobalObject;

public class DirigibleEngineTypeGlobalObject implements JSGlobalObject {
    @Override
    public String getName() {
        return "__engine";
    }

    @Override
    public Object getValue() {
        return "graalium";
    }
}
