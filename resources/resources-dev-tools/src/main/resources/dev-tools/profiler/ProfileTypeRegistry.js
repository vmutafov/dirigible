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
// Copyright 2014 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import {CPUProfileType} from './CPUProfileView.js';
import {SamplingHeapProfileType, SamplingNativeHeapProfileType, SamplingNativeHeapSnapshotBrowserType, SamplingNativeHeapSnapshotRendererType,} from './HeapProfileView.js';
import {HeapSnapshotProfileType, TrackingHeapSnapshotProfileType} from './HeapSnapshotView.js';

/**
 * @unrestricted
 */
export class ProfileTypeRegistry {
  constructor() {
    this.cpuProfileType = new CPUProfileType();
    this.heapSnapshotProfileType = new HeapSnapshotProfileType();
    this.samplingHeapProfileType = new SamplingHeapProfileType();
    this.samplingNativeHeapProfileType = new SamplingNativeHeapProfileType();
    this.samplingNativeHeapSnapshotBrowserType = new SamplingNativeHeapSnapshotBrowserType();
    this.samplingNativeHeapSnapshotRendererType = new SamplingNativeHeapSnapshotRendererType();
    this.trackingHeapSnapshotProfileType = new TrackingHeapSnapshotProfileType();
  }
}

export const instance = new ProfileTypeRegistry();
