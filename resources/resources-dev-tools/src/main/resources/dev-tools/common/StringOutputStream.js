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
// Copyright 2015 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

/**
 * @interface
 */
export class OutputStream {
  /**
   * @param {string} data
   * @return {!Promise.<void>}
   */
  async write(data) {
  }

  /**
   * @return {!Promise.<void>}
   */
  async close() {
  }
}

/**
 * @implements {OutputStream}
 */
export class StringOutputStream {
  constructor() {
    this._data = '';
  }

  /**
   * @override
   * @param {string} chunk
   * @return {!Promise.<void>}
   */
  async write(chunk) {
    this._data += chunk;
  }

  /**
   * @override
   * @return {!Promise.<void>}
   */
  async close() {
  }

  /**
   * @return {string}
   */
  data() {
    return this._data;
  }
}
