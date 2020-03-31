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
import * as Common from '../common/common.js';  // eslint-disable-line no-unused-vars
import * as TextUtils from '../text_utils/text_utils.js';

/**
 * @implements {TextUtils.ContentProvider.ContentProvider}
 * @unrestricted
 */
export class CompilerSourceMappingContentProvider {
  /**
   * @param {string} sourceURL
   * @param {!Common.ResourceType.ResourceType} contentType
   */
  constructor(sourceURL, contentType) {
    this._sourceURL = sourceURL;
    this._contentType = contentType;
  }

  /**
   * @override
   * @return {string}
   */
  contentURL() {
    return this._sourceURL;
  }

  /**
   * @override
   * @return {!Common.ResourceType.ResourceType}
   */
  contentType() {
    return this._contentType;
  }

  /**
   * @override
   * @return {!Promise<boolean>}
   */
  contentEncoded() {
    return Promise.resolve(false);
  }

  /**
   * @override
   * @return {!Promise<!TextUtils.ContentProvider.DeferredContent>}
   */
  requestContent() {
    return new Promise(resolve => {
      self.SDK.multitargetNetworkManager.loadResource(
          this._sourceURL, (success, _headers, content, errorDescription) => {
            if (!success) {
              const error = ls`Could not load content for ${this._sourceURL} (${errorDescription.message})`;
              console.error(error);
              resolve({error, isEncoded: false});
            } else {
              resolve({content, isEncoded: false});
            }
          });
    });
  }

  /**
   * @override
   * @param {string} query
   * @param {boolean} caseSensitive
   * @param {boolean} isRegex
   * @return {!Promise<!Array<!TextUtils.ContentProvider.SearchMatch>>}
   */
  async searchInContent(query, caseSensitive, isRegex) {
    const {content} = await this.requestContent();
    if (typeof content !== 'string') {
      return [];
    }
    return TextUtils.TextUtils.performSearchInContent(content, query, caseSensitive, isRegex);
  }
}
