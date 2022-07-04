package org.eclipse.dirigible.turbofan.web.quarkus;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.vertx.mutiny.ext.web.Router;
import org.eclipse.dirigible.turbofan.web.quarkus.app.ApplicationBootstrap;

import javax.inject.Inject;
import java.nio.file.Path;

@QuarkusMain
public class Main implements QuarkusApplication {

    @Inject
    Router router;

    public static void main(String... args) {
        Quarkus.run(Main.class, args);
    }

    @Override
    public int run(String... args) {
        var cwd = Path.of(args[0]);
        var appEntry = Path.of(args[1]);
        ApplicationBootstrap.forCurrentThread(cwd, appEntry).registerJSRoutes(router);
        Quarkus.waitForExit();
        return 0;
    }
}
