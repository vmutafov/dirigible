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

import * as TextUtilsModule from './text_utils.js';

self.TextUtils = self.TextUtils || {};
TextUtils = TextUtils || {};

/**
 * @interface
 */
TextUtils.ContentProvider = TextUtilsModule.ContentProvider.ContentProvider;

/**
 * @constructor
 */
TextUtils.ContentProvider.SearchMatch = TextUtilsModule.ContentProvider.SearchMatch;
TextUtils.ContentProvider.contentAsDataURL = TextUtilsModule.ContentProvider.contentAsDataURL;
TextUtils.StaticContentProvider = TextUtilsModule.StaticContentProvider.StaticContentProvider;


/** @constructor */
TextUtils.Text = TextUtilsModule.Text.Text;

/** @constructor */
TextUtils.TextCursor = TextUtilsModule.TextCursor.TextCursor;

/** @constructor */
TextUtils.TextRange = TextUtilsModule.TextRange.TextRange;

/** @constructor */
TextUtils.SourceRange = TextUtilsModule.TextRange.SourceRange;

/** @constructor */
TextUtils.SourceEdit = TextUtilsModule.TextRange.SourceEdit;

TextUtils.TextUtils = TextUtilsModule.TextUtils.Utils;

/** @constructor */
TextUtils.FilterParser = TextUtilsModule.TextUtils.FilterParser;

/** @constructor */
TextUtils.BalancedJSONTokenizer = TextUtilsModule.TextUtils.BalancedJSONTokenizer;

/** @interface */
TextUtils.TokenizerFactory = TextUtilsModule.TextUtils.TokenizerFactory;

TextUtils.isMinified = TextUtilsModule.TextUtils.isMinified;
