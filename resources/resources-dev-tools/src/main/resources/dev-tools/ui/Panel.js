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
import {SearchableView} from './SearchableView.js';  // eslint-disable-line no-unused-vars
import {SplitWidget} from './SplitWidget.js';
import {VBox} from './Widget.js';

/**
 * @unrestricted
 */
export class Panel extends VBox {
  /**
   * @param {string} name
   */
  constructor(name) {
    super();

    this.element.classList.add('panel');
    this.element.setAttribute('aria-label', name);
    this.element.classList.add(name);
    this._panelName = name;

    // For testing.
    UI.panels[name] = this;
  }

  get name() {
    return this._panelName;
  }

  /**
   * @return {?SearchableView}
   */
  searchableView() {
    return null;
  }

  /**
   * @override
   * @return {!Array.<!Element>}
   */
  elementsToRestoreScrollPositionsFor() {
    return [];
  }
}

/**
 * @unrestricted
 */
export class PanelWithSidebar extends Panel {
  /**
   * @param {string} name
   * @param {number=} defaultWidth
   */
  constructor(name, defaultWidth) {
    super(name);

    this._panelSplitWidget = new SplitWidget(true, false, this._panelName + 'PanelSplitViewState', defaultWidth || 200);
    this._panelSplitWidget.show(this.element);

    this._mainWidget = new VBox();
    this._panelSplitWidget.setMainWidget(this._mainWidget);

    this._sidebarWidget = new VBox();
    this._sidebarWidget.setMinimumSize(100, 25);
    this._panelSplitWidget.setSidebarWidget(this._sidebarWidget);

    this._sidebarWidget.element.classList.add('panel-sidebar');
  }

  /**
   * @return {!Element}
   */
  panelSidebarElement() {
    return this._sidebarWidget.element;
  }

  /**
   * @return {!Element}
   */
  mainElement() {
    return this._mainWidget.element;
  }

  /**
   * @return {!SplitWidget}
   */
  splitWidget() {
    return this._panelSplitWidget;
  }
}
