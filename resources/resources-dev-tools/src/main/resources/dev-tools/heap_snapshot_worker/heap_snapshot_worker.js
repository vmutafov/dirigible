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

import './AllocationProfile.js';
import './HeapSnapshot.js';
import './HeapSnapshotLoader.js';
import './HeapSnapshotWorkerDispatcher.js';
import './HeapSnapshotWorker.js';

import * as AllocationProfile from './AllocationProfile.js';
import * as HeapSnapshot from './HeapSnapshot.js';
import * as HeapSnapshotLoader from './HeapSnapshotLoader.js';
import * as HeapSnapshotWorkerDispatcher from './HeapSnapshotWorkerDispatcher.js';

export {
  AllocationProfile,
  HeapSnapshot,
  HeapSnapshotLoader,
  HeapSnapshotWorkerDispatcher,
};
