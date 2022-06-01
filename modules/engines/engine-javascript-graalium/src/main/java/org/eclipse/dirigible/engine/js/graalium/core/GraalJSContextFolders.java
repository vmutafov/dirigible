/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company and Eclipse Dirigible contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-FileCopyrightText: 2022 SAP SE or an SAP affiliate company and Eclipse Dirigible contributors
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.dirigible.engine.js.graalium.core;

import java.nio.file.Path;

public class GraalJSContextFolders {

    private final Path workingDirectoryPath;
    private final Path coreModulesProxiesCachePath;
    private final Path dependenciesCachePath;

    public GraalJSContextFolders(Path workingDirectoryPath, Path coreModulesProxiesCachePath, Path dependenciesCachePath) {
        this.workingDirectoryPath = workingDirectoryPath;
        this.coreModulesProxiesCachePath = coreModulesProxiesCachePath;
        this.dependenciesCachePath = dependenciesCachePath;
    }

    public Path getWorkingDirectoryPath() {
        return workingDirectoryPath;
    }

    public Path getCoreModulesProxiesCachePath() {
        return coreModulesProxiesCachePath;
    }

    public Path getDependenciesCachePath() {
        return dependenciesCachePath;
    }
}
