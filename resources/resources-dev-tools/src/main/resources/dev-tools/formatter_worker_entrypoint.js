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
// Copyright 2016 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import './RuntimeInstantiator.js';
import './common/common.js';
import './platform/platform.js';
import './text_utils/text_utils-legacy.js';
import './cm_headless/cm_headless.js';
import './formatter_worker/formatter_worker.js';

import {startWorker} from './RuntimeInstantiator.js';

startWorker('formatter_worker_entrypoint');
