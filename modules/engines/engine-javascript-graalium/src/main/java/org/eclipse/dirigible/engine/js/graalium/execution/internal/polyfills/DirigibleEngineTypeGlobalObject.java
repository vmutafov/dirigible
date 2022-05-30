package org.eclipse.dirigible.engine.js.graalium.execution.internal.polyfills;

import org.eclipse.dirigible.engine.js.graalium.execution.platform.polyfills.JSGlobalObject;

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
