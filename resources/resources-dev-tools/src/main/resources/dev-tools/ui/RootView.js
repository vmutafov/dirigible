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
// Copyright 2014 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import {VBox} from './Widget.js';
import {ZoomManager} from './ZoomManager.js';

/**
 * @unrestricted
 */
export class RootView extends VBox {
  constructor() {
    super();
    this.markAsRoot();
    this.element.classList.add('root-view');
    this.registerRequiredCSS('ui/rootView.css');
    this.element.setAttribute('spellcheck', false);
  }

  /**
   * @param {!Document} document
   */
  attachToDocument(document) {
    document.defaultView.addEventListener('resize', this.doResize.bind(this), false);
    this._window = document.defaultView;
    this.doResize();
    this.show(/** @type {!Element} */ (document.body));
  }

  /**
   * @override
   */
  doResize() {
    if (this._window) {
      const size = this.constraints().minimum;
      const zoom = ZoomManager.instance().zoomFactor();
      const right = Math.min(0, this._window.innerWidth - size.width / zoom);
      this.element.style.marginRight = right + 'px';
      const bottom = Math.min(0, this._window.innerHeight - size.height / zoom);
      this.element.style.marginBottom = bottom + 'px';
    }
    super.doResize();
  }
}
