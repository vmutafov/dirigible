package org.eclipse.dirigible.engine.js.graalium.platform.internal.modules;

import org.apache.commons.io.IOUtils;
import org.eclipse.dirigible.commons.config.StaticObjects;
import org.eclipse.dirigible.engine.api.script.AbstractScriptExecutor;
import org.eclipse.dirigible.repository.api.IRepository;
import org.eclipse.dirigible.repository.api.IResource;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class DirigibleSourceProvider {
    private static final IRepository REPOSITORY = (IRepository) StaticObjects.get(StaticObjects.REPOSITORY);

    public String getSource(String projectName, String projectFilePath) {
        Path repositoryFilePath = Path.of(projectName, projectFilePath);
        String repositoryFilePathString = withRepositoryPrefix(repositoryFilePath.toString());

        byte[] maybeContentFromRepository = tryGetFromRepository(repositoryFilePathString);
        if (maybeContentFromRepository != null) {
            return new String(maybeContentFromRepository, StandardCharsets.UTF_8);
        }

        byte[] maybeContentFromClassLoader = tryGetFromClassLoader(repositoryFilePathString);
        if (maybeContentFromClassLoader != null) {
            return new String(maybeContentFromClassLoader, StandardCharsets.UTF_8);
        }

        return null;
    }

    private static String withRepositoryPrefix(String input) {
        return Character.toString(input.charAt(0)).equals(IRepository.SEPARATOR) ? "" : IRepository.SEPARATOR + input;
    }

    @Nullable
    private static byte[] tryGetFromRepository(String repositoryFilePathString) {
        IResource resource = REPOSITORY.getResource(repositoryFilePathString);
        if (!resource.exists()) {
            return null;
        }
        return resource.getContent();
    }

    @Nullable
    private static byte[] tryGetFromClassLoader(String repositoryFilePathString) {
        try {
            try (InputStream bundled = AbstractScriptExecutor.class.getResourceAsStream("/META-INF/dirigible" + repositoryFilePathString)) {
                byte[] content = null;
                if (bundled != null) {
                    content = IOUtils.toByteArray(bundled);
                    REPOSITORY.createResource(repositoryFilePathString, content);
                }
                return content;
            }
        } catch (IOException e) {
            return null;
        }
    }
}
