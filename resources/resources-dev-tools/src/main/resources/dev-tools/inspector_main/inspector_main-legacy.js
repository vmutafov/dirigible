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

import * as InspectorMainModule from './inspector_main.js';

self.InspectorMain = self.InspectorMain || {};
InspectorMain = InspectorMain || {};

/**
 * @constructor
 */
InspectorMain.InspectorMain = InspectorMainModule.InspectorMain.InspectorMainImpl;

/**
 * @constructor
 */
InspectorMain.ReloadActionDelegate = InspectorMainModule.InspectorMain.ReloadActionDelegate;

/**
 * @constructor
 */
InspectorMain.FocusDebuggeeActionDelegate = InspectorMainModule.InspectorMain.FocusDebuggeeActionDelegate;

/**
 * @constructor
 */
InspectorMain.NodeIndicator = InspectorMainModule.InspectorMain.NodeIndicator;

/**
 * @constructor
 */
InspectorMain.SourcesPanelIndicator = InspectorMainModule.InspectorMain.SourcesPanelIndicator;

/**
 * @constructor
 */
InspectorMain.BackendSettingsSync = InspectorMainModule.InspectorMain.BackendSettingsSync;

/**
 * @constructor
 */
InspectorMain.RenderingOptionsView = InspectorMainModule.RenderingOptions.RenderingOptionsView;
