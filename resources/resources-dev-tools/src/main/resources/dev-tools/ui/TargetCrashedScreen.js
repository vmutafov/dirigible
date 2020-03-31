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
// Copyright 2018 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import * as Common from '../common/common.js';
import {VBox} from './Widget.js';

export class TargetCrashedScreen extends VBox {
  /**
   * @param {function()} hideCallback
   */
  constructor(hideCallback) {
    super(true);
    this.registerRequiredCSS('ui/targetCrashedScreen.css');
    this.contentElement.createChild('div', 'message').textContent =
        Common.UIString.UIString('DevTools was disconnected from the page.');
    this.contentElement.createChild('div', 'message').textContent =
        Common.UIString.UIString('Once page is reloaded, DevTools will automatically reconnect.');
    this._hideCallback = hideCallback;
  }

  /**
   * @override
   */
  willHide() {
    this._hideCallback.call(null);
  }
}
