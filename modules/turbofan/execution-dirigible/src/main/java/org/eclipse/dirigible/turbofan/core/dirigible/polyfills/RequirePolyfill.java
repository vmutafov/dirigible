package org.eclipse.dirigible.turbofan.core.dirigible.polyfills;

import org.eclipse.dirigible.turbofan.core.graal.polyfills.JSPolyfill;

public class RequirePolyfill implements JSPolyfill {
    private static final String POLYFILL_PATH_IN_RESOURCES = "/polyfills/require.js";

    public RequirePolyfill() {
    }

    public String getSource() {
        return this.getPolyfillFromResources("/polyfills/require.js");
    }

    public String getFileName() {
        return "/polyfills/require.js";
    }
}

