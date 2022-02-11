package org.eclipse.dirigible.engine.js.graalvm.execution.js.modules;

import org.apache.commons.io.IOUtils;
import org.eclipse.dirigible.commons.config.StaticObjects;
import org.eclipse.dirigible.engine.api.script.AbstractScriptExecutor;
import org.eclipse.dirigible.repository.api.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DirigibleCoreModuleProvider {
    private IRepository repository = (IRepository) StaticObjects.get(StaticObjects.REPOSITORY);

    private static Map<String, byte[]> PREDELIVERED = Collections.synchronizedMap(new HashMap<String, byte[]>());

    public byte[] getResourceContent(String root, String module, String extension) throws RepositoryException {
        if ((module == null) || "".equals(module.trim())) {
            throw new RepositoryException("Module name cannot be empty or null.");
        }
        if (module.trim().endsWith(IRepositoryStructure.SEPARATOR)) {
            throw new RepositoryException("Module name cannot point to a collection.");
        }
        String repositoryPath = createResourcePath(root, module, extension);
        final IResource resource = repository.getResource(repositoryPath);
        if (resource.exists()) {
            return resource.getContent();
        }

        // try from the classloader
        try {
            String prefix = Character.toString(module.charAt(0)).equals(IRepository.SEPARATOR) ? "" : IRepository.SEPARATOR;
            String location = prefix + module + (extension != null ? extension : "");
            byte[] content = PREDELIVERED.get(location);
            if (content != null) {
                return content;
            }
            InputStream bundled = AbstractScriptExecutor.class.getResourceAsStream("/META-INF/dirigible" + location);
            try {
                if (bundled != null) {
                    content = IOUtils.toByteArray(bundled);
                    PREDELIVERED.put(location, content);
                    return content;
                }
            } finally {
                if (bundled != null) {
                    bundled.close();
                }
            }
        } catch (IOException e) {
            throw new RepositoryException(e);
        }

        final String logMsg = String.format("There is no resource at the specified path: %s", repositoryPath);
        throw new RepositoryNotFoundException(logMsg);
    }

    private String createResourcePath(String root, String module, String extension) {
        StringBuilder buff = new StringBuilder().append(root);
        if (!Character.toString(module.charAt(0)).equals(IRepository.SEPARATOR)) {
            buff.append(IRepository.SEPARATOR);
        }
        buff.append(module);
        if (extension != null) {
            buff.append(extension);
        }
        String resourcePath = buff.toString();
        return resourcePath;
    }

    public String loadSource(String module) throws IOException {

        if (module == null) {
            throw new IOException("Module location cannot be null");
        }

        byte[] sourceCode = getResourceContent(IRepositoryStructure.PATH_REGISTRY_PUBLIC, module, ".js");
        return new String(sourceCode, StandardCharsets.UTF_8);
    }
}
