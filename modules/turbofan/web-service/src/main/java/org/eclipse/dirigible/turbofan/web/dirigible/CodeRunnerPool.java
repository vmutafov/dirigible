package org.eclipse.dirigible.turbofan.web.dirigible;

import org.eclipse.dirigible.turbofan.core.CodeRunner;
import org.eclipse.dirigible.commons.config.StaticObjects;
import org.eclipse.dirigible.repository.api.IRepository;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import stormpot.*;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class CodeRunnerPool {
    private static final CodeRunnerAllocator allocator = new CodeRunnerAllocator();
    private static final Pool<CodeRunnerPoolable> pool = Pool.from(allocator).build();

    public static CodeRunnerPoolable borrow() throws InterruptedException {
        var timeout = new Timeout(10, TimeUnit.MINUTES);
        return pool.claim(timeout);
    }

    public static class CodeRunnerAllocator implements Allocator<CodeRunnerPoolable> {

        @Override
        public CodeRunnerPoolable allocate(Slot slot) {
            var codeRunner = createJavaScriptCodeRunner();
            return new CodeRunnerPoolable(slot, codeRunner);
        }

        private static CodeRunner<Source, Value> createJavaScriptCodeRunner() {
            var repository = (IRepository) StaticObjects.get(StaticObjects.REPOSITORY);
            var repositoryRootPath = Path.of(repository.getRepositoryPath());
            var registryPath = repositoryRootPath.resolve("registry/public");

            return CodeRunnerFactory.createDirigibleJSCodeRunner(registryPath);
        }

        @Override
        public void deallocate(CodeRunnerPoolable codeRunnerPoolable) {
            codeRunnerPoolable.getCodeRunner().close();
        }
    }

    public static class CodeRunnerPoolable implements Poolable {

        private final Slot slot;
        private final CodeRunner<Source, Value> codeRunner;

        public CodeRunnerPoolable(Slot slot, CodeRunner<Source, Value> codeRunner) {
            this.slot = slot;
            this.codeRunner = codeRunner;
        }

        public CodeRunner<Source, Value> getCodeRunner() {
            return codeRunner;
        }

        @Override
        public void release() {
            slot.release(this);
        }
    }
}
