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

import * as CustomPreviewComponent from './CustomPreviewComponent.js';
import * as JavaScriptAutocomplete from './JavaScriptAutocomplete.js';
import * as JavaScriptREPL from './JavaScriptREPL.js';
import * as ObjectPopoverHelper from './ObjectPopoverHelper.js';
import * as ObjectPropertiesSection from './ObjectPropertiesSection.js';
import * as RemoteObjectPreviewFormatter from './RemoteObjectPreviewFormatter.js';

export {
  CustomPreviewComponent,
  JavaScriptAutocomplete,
  JavaScriptREPL,
  ObjectPopoverHelper,
  ObjectPropertiesSection,
  RemoteObjectPreviewFormatter,
};

export const javaScriptAutocomplete = new JavaScriptAutocomplete.JavaScriptAutocomplete();
