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

import * as TextEditor from '../text_editor/text_editor.js';  // eslint-disable-line no-unused-vars

/**
 * @implements {TextEditor.CodeMirrorTextEditor.CodeMirrorMimeMode}
 */
export class DefaultCodeMirrorMimeMode {
  /**
   * @param {!Root.Runtime.Extension} extension
   * @return {!Promise}
   * @override
   */
  async install(extension) {
    return Promise.resolve();
  }
}

CmModes.DefaultCodeMirrorMimeMode = DefaultCodeMirrorMimeMode;
