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
// Copyright 2020 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import * as ProtocolMonitorModule from './protocol_monitor.js';

self.ProtocolMonitor = self.ProtocolMonitor || {};
ProtocolMonitor = ProtocolMonitor || {};

/**
 * @constructor
 */
ProtocolMonitor.ProtocolMonitor = ProtocolMonitorModule.ProtocolMonitor.ProtocolMonitorImpl;

/**
 * @constructor
 */
ProtocolMonitor.ProtocolMonitor.InfoWidget = ProtocolMonitorModule.ProtocolMonitor.InfoWidget;

/**
 * @constructor
 */
ProtocolMonitor.ProtocolMonitor.ProtocolNode = ProtocolMonitorModule.ProtocolMonitor.ProtocolNode;
