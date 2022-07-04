package org.eclipse.dirigible.turbofan.web.quarkus.app;

import io.vertx.core.http.HttpMethod;
import org.graalvm.polyglot.Value;

public class ApplicationRouteHandler {
    private final Value routeHandler;

    private final HttpMethod routeHttpMethod;

    public ApplicationRouteHandler(Value routeHandler) {
        this(routeHandler, null);
    }

    public ApplicationRouteHandler(Value routeHandler, HttpMethod routeHttpMethod) {
        this.routeHandler = routeHandler;
        this.routeHttpMethod = routeHttpMethod;
    }

    public Value getRouteHandler() {
        return routeHandler;
    }

    public HttpMethod getRouteHttpMethod() {
        return routeHttpMethod;
    }

}
