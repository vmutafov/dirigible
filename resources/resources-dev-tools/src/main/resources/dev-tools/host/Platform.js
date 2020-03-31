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
import {InspectorFrontendHostInstance} from './InspectorFrontendHost.js';

/** @type {string} */
let _platform;

/**
 * @return {string}
 */
export function platform() {
  if (!_platform) {
    _platform = InspectorFrontendHostInstance.platform();
  }
  return _platform;
}

/** @type {boolean} */
let _isMac;

/**
 * @return {boolean}
 */
export function isMac() {
  if (typeof _isMac === 'undefined') {
    _isMac = platform() === 'mac';
  }

  return _isMac;
}

/** @type {boolean} */
let _isWin;

/**
 * @return {boolean}
 */
export function isWin() {
  if (typeof _isWin === 'undefined') {
    _isWin = platform() === 'windows';
  }

  return _isWin;
}

/** @type {boolean} */
let _isCustomDevtoolsFrontend;

/**
 * @return {boolean}
 */
export function isCustomDevtoolsFrontend() {
  if (typeof _isCustomDevtoolsFrontend === 'undefined') {
    _isCustomDevtoolsFrontend = window.location.toString().startsWith('devtools://devtools/custom/');
  }
  return _isCustomDevtoolsFrontend;
}

/** @type {string} */
let _fontFamily;

/**
 * @return {string}
 */
export function fontFamily() {
  if (_fontFamily) {
    return _fontFamily;
  }
  switch (platform()) {
    case 'linux':
      _fontFamily = 'Roboto, Ubuntu, Arial, sans-serif';
      break;
    case 'mac':
      _fontFamily = '\'Lucida Grande\', sans-serif';
      break;
    case 'windows':
      _fontFamily = '\'Segoe UI\', Tahoma, sans-serif';
      break;
  }
  return _fontFamily;
}
