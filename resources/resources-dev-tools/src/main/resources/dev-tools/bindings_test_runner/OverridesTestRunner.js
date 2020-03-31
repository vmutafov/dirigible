/*
 * Copyright (c) 2010-2020 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   SAP - initial API and implementation
 */
// Copyright 2017 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

/**
 * @param {string} folderPath
 * @return {!{isolatedFileSystem: !Persistence.IsolatedFileSystem, project: !Workspace.Project, testFileSystem: !BindingsTestRunner.TestFileSystem}}
 */
BindingsTestRunner.createOverrideProject = async function(folderPath) {
  const testFileSystem = new BindingsTestRunner.TestFileSystem(folderPath);
  const isolatedFileSystem = await testFileSystem.reportCreatedPromise('overrides');
  isolatedFileSystem._type = 'overrides';
  const project =
      self.Workspace.workspace.project(Persistence.FileSystemWorkspaceBinding.projectId(isolatedFileSystem.path()));
  console.assert(project);
  return {isolatedFileSystem, project, testFileSystem};
};

/**
 * @param {boolean} enabled
 */
BindingsTestRunner.setOverridesEnabled = function(enabled) {
  self.Common.settings.moduleSetting('persistenceNetworkOverridesEnabled').set(enabled);
};
