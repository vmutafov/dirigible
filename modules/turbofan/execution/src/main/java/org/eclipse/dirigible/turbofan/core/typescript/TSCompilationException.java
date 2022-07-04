package org.eclipse.dirigible.turbofan.core.typescript;

import java.lang.RuntimeException;

class TSCompilationException extends RuntimeException {
    public TSCompilationException(String message, Throwable cause) {
        super(message, cause);
    }
}