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

import * as InspectorBackendCommands from '../generated/InspectorBackendCommands.js';
import * as InspectorBackend from './InspectorBackend.js';
import * as NodeURL from './NodeURL.js';

export {
  InspectorBackend,
  NodeURL,
};

// Create the global here because registering commands will involve putting
// items onto the global.
// @ts-ignore Global namespace instantiation
self.Protocol = self.Protocol || {};

// FIXME: This instance of InspectorBackend should not be a side effect of importing this module.
InspectorBackendCommands.registerCommands(InspectorBackend.inspectorBackend);

export const Protocol = self.Protocol;
