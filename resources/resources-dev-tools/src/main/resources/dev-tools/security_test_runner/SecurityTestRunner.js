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
// Copyright 2017 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

/**
 * @fileoverview using private properties isn't a Closure violation in tests.
 * @suppress {accessControls}
 */

SecurityTestRunner.dumpSecurityPanelSidebarOrigins = function() {
  for (const key in Security.SecurityPanelSidebarTree.OriginGroup) {
    const originGroup = Security.SecurityPanelSidebarTree.OriginGroup[key];
    const element = Security.SecurityPanel._instance()._sidebarTree._originGroups.get(originGroup);

    if (element.hidden) {
      continue;
    }

    TestRunner.addResult('Group: ' + element.title);
    const originTitles = element.childrenListElement.getElementsByTagName('span');

    for (const originTitle of originTitles) {
      if (originTitle.className !== 'tree-element-title') {
        TestRunner.dumpDeepInnerHTML(originTitle);
      }
    }
  }
};

SecurityTestRunner.dispatchRequestFinished = function(request) {
  TestRunner.networkManager.dispatchEventToListeners(SDK.NetworkManager.Events.RequestFinished, request);
};
