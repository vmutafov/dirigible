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

import * as HeapSnapshotModel from '../heap_snapshot_model/heap_snapshot_model.js';  // eslint-disable-line no-unused-vars

/**
 * @interface
 */
export class ChildrenProvider {
  dispose() {
  }

  /**
   * @param {number} snapshotObjectId
   * @return {!Promise<number>}
   */
  nodePosition(snapshotObjectId) {
  }

  /**
   * @return {!Promise<boolean>}
   */
  isEmpty() {
  }

  /**
   * @param {number} startPosition
   * @param {number} endPosition
   * @return {!Promise<!HeapSnapshotModel.HeapSnapshotModel.ItemsRange>}
   */
  serializeItemsRange(startPosition, endPosition) {
  }

  /**
   * @param {!HeapSnapshotModel.HeapSnapshotModel.ComparatorConfig} comparator
   * @return {!Promise<?>}
   */
  sortAndRewind(comparator) {
  }
}
