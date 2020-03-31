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

import {Widget} from '../Widget.js';
import {XWidget} from '../XWidget.js';

/**
 * @param {!Event} event
 */
export function focusChanged(event) {
  const document = event.target && event.target.ownerDocument;
  const element = document ? document.deepActiveElement() : null;
  Widget.focusWidgetForNode(element);
  XWidget.focusWidgetForNode(element);
  if (!UI._keyboardFocus) {
    return;
  }

  UI.markAsFocusedByKeyboard(element);
}
