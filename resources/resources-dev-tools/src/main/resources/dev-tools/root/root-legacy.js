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
// Copyright 2020 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import * as RootModule from './root.js';

self.Root = self.Root || {};
Root = Root || {};

/** @constructor */
Root.Runtime = RootModule.Runtime.Runtime;

// This must be constructed after the query parameters have been parsed.
Root.Runtime.experiments = RootModule.Runtime.experiments;

Root.Runtime.queryParam = RootModule.Runtime.Runtime.queryParam;

/** @type {!RootModule.Runtime.Runtime} */
Root.runtime;

Root.Runtime.loadResourcePromise = RootModule.Runtime.loadResourcePromise;

/** @constructor */
Root.Runtime.Extension = RootModule.Runtime.Extension;

/** @constructor */
Root.Runtime.Module = RootModule.Runtime.Module;
