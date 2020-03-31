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

import * as BrowserDebuggerModule from './browser_debugger.js';

self.BrowserDebugger = self.BrowserDebugger || {};
BrowserDebugger = BrowserDebugger || {};

/**
 * @constructor
 */
BrowserDebugger.DOMBreakpointsSidebarPane = BrowserDebuggerModule.DOMBreakpointsSidebarPane.DOMBreakpointsSidebarPane;

BrowserDebugger.DOMBreakpointsSidebarPane.BreakpointTypeLabels =
    BrowserDebuggerModule.DOMBreakpointsSidebarPane.BreakpointTypeLabels;

BrowserDebugger.DOMBreakpointsSidebarPane.ContextMenuProvider =
    BrowserDebuggerModule.DOMBreakpointsSidebarPane.ContextMenuProvider;

/**
 * @constructor
 */
BrowserDebugger.EventListenerBreakpointsSidebarPane =
    BrowserDebuggerModule.EventListenerBreakpointsSidebarPane.EventListenerBreakpointsSidebarPane;

/**
 * @constructor
 */
BrowserDebugger.ObjectEventListenersSidebarPane =
    BrowserDebuggerModule.ObjectEventListenersSidebarPane.ObjectEventListenersSidebarPane;

BrowserDebugger.ObjectEventListenersSidebarPane._objectGroupName =
    BrowserDebuggerModule.ObjectEventListenersSidebarPane.objectGroupName;

/**
 * @constructor
 */
BrowserDebugger.XHRBreakpointsSidebarPane = BrowserDebuggerModule.XHRBreakpointsSidebarPane.XHRBreakpointsSidebarPane;
