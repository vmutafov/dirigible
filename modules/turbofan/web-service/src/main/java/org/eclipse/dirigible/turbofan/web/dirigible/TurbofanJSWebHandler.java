package org.eclipse.dirigible.turbofan.web.dirigible;

import org.eclipse.dirigible.engine.js.service.JSWebHandler;
import org.eclipse.dirigible.repository.api.IRepositoryStructure;
import org.eclipse.dirigible.turbofan.core.CodeRunner;
import org.eclipse.dirigible.turbofan.core.dirigible.modules.DirigibleSourceProvider;
import org.eclipse.dirigible.api.v3.http.HttpRequestFacade;
import org.eclipse.dirigible.commons.config.StaticObjects;
import org.eclipse.dirigible.repository.api.IRepository;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import java.io.IOException;
import java.nio.file.Path;

public class TurbofanJSWebHandler implements JSWebHandler {

    private final DirigibleSourceProvider dirigibleSourceProvider = new DirigibleSourceProvider();

    @Override
    public void handleJSRequest(String projectName, String projectFilePath, String projectFilePathParam) {
        try {
            if (HttpRequestFacade.isValid()) {
                HttpRequestFacade.setAttribute(HttpRequestFacade.ATTRIBUTE_REST_RESOURCE_PATH, projectFilePathParam);
            }

            String maybeJSCode = dirigibleSourceProvider.getSource(projectName, projectFilePath);
            if (maybeJSCode == null) {
                throw new IOException("JS source for project name '" + projectName + "' and file name '" + projectFilePath + " could not be found");
            }

            String jsCodePath = dirigibleSourceProvider.getAbsoluteSourcePath(projectName, projectFilePath).toString();
            createCodeRunner().run(Path.of(jsCodePath));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static CodeRunner<Source, Value> createCodeRunner() {
        var repository = (IRepository) StaticObjects.get(StaticObjects.REPOSITORY);
        var repositoryRootPath = Path.of(repository.getRepositoryPath());
        var registryPath = repositoryRootPath.resolve(IRepositoryStructure.KEYWORD_REGISTRY + IRepositoryStructure.SEPARATOR + IRepositoryStructure.KEYWORD_PUBLIC);

        return CodeRunnerFactory.createDirigibleJSCodeRunner(registryPath);
    }
}
