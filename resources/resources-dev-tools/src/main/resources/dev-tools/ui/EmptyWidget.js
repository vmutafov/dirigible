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
import {VBox} from './Widget.js';
import {XLink} from './XLink.js';

/**
 * @unrestricted
 */
export class EmptyWidget extends VBox {
  /**
   * @param {string} text
   */
  constructor(text) {
    super();
    this.registerRequiredCSS('ui/emptyWidget.css');
    this.element.classList.add('empty-view-scroller');
    this._contentElement = this.element.createChild('div', 'empty-view');
    this._textElement = this._contentElement.createChild('div', 'empty-bold-text');
    this._textElement.textContent = text;
  }

  /**
   * @return {!Element}
   */
  appendParagraph() {
    return this._contentElement.createChild('p');
  }

  /**
   * @param {string} link
   * @return {!Node}
   */
  appendLink(link) {
    return this._contentElement.appendChild(XLink.create(link, 'Learn more'));
  }

  /**
   * @param {string} text
   */
  set text(text) {
    this._textElement.textContent = text;
  }
}
