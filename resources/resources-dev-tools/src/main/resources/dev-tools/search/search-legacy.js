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

import * as SearchModule from './search.js';

self.Search = self.Search || {};
Search = Search || {};

/**
 * @constructor
 */
Search.SearchConfig = SearchModule.SearchConfig.SearchConfig;

/**
 * @interface
 */
Search.SearchResult = SearchModule.SearchConfig.SearchResult;

/**
 * @interface
 */
Search.SearchScope = SearchModule.SearchConfig.SearchScope;

/**
 * @constructor
 */
Search.SearchResultsPane = SearchModule.SearchResultsPane.SearchResultsPane;

/**
 * @constructor
 */
Search.SearchView = SearchModule.SearchView.SearchView;
