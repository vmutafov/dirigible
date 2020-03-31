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
// Copyright 2019 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import * as WorkspaceModule from './workspace.js';

self.Workspace = self.Workspace || {};
Workspace = Workspace || {};

/** @constructor */
Workspace.FileManager = WorkspaceModule.FileManager.FileManager;

/** @constructor */
Workspace.UISourceCode = WorkspaceModule.UISourceCode.UISourceCode;

/** @enum {symbol} */
Workspace.UISourceCode.Events = WorkspaceModule.UISourceCode.Events;

/** @constructor */
Workspace.UISourceCode.Message = WorkspaceModule.UISourceCode.Message;

/** @constructor */
Workspace.UILocation = WorkspaceModule.UISourceCode.UILocation;

/** @constructor */
Workspace.UISourceCodeMetadata = WorkspaceModule.UISourceCode.UISourceCodeMetadata;

/** @constructor */
Workspace.Workspace = WorkspaceModule.Workspace.WorkspaceImpl;

/** @enum {symbol} */
Workspace.Workspace.Events = WorkspaceModule.Workspace.Events;

/** @interface */
Workspace.Project = WorkspaceModule.Workspace.Project;

/** @enum {string} */
Workspace.projectTypes = WorkspaceModule.Workspace.projectTypes;

/** @constructor */
Workspace.ProjectStore = WorkspaceModule.Workspace.ProjectStore;

/**
 * @type {?WorkspaceModule.FileManager.FileManager}
 */
self.Workspace.fileManager;

/**
 * @type {!WorkspaceModule.Workspace.WorkspaceImpl}
 */
self.Workspace.workspace;
