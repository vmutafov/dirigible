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

// TODO(crbug.com/1029037): lazily load these files again after the
// race-condition with CodeMirror is fixed
import './clike.js';
import './coffeescript.js';
import './php.js';
import './python.js';
import './shell.js';
import './livescript.js';
import './markdown.js';
import './clojure.js';
import './jsx.js';

import * as DefaultCodeMirrorMimeMode from './DefaultCodeMirrorMimeMode.js';

export {
  DefaultCodeMirrorMimeMode,
};
