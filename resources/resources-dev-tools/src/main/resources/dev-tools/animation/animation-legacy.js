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

import * as AnimationModule from './animation.js';

self.Animation = self.Animation || {};
Animation = Animation || {};

/**
 * @constructor
 */
Animation.AnimationModel = AnimationModule.AnimationModel.AnimationModel;

/** @enum {symbol} */
Animation.AnimationModel.Events = AnimationModule.AnimationModel.Events;

/**
 * @constructor
 */
Animation.AnimationModel.Animation = AnimationModule.AnimationModel.AnimationImpl;

/**
 * @constructor
 */
Animation.AnimationModel.AnimationGroup = AnimationModule.AnimationModel.AnimationGroup;

/**
 * @constructor
 */
Animation.AnimationModel.ScreenshotCapture = AnimationModule.AnimationModel.ScreenshotCapture;

/**
 * @implements {SDK.SDKModelObserver<!Animation.AnimationModel>}
 * @constructor
 * @unrestricted
 */
Animation.AnimationTimeline = AnimationModule.AnimationTimeline.AnimationTimeline;

/**
 * @constructor
 */
Animation.AnimationUI = AnimationModule.AnimationUI.AnimationUI;

/**
 * @enum {string}
 */
Animation.AnimationUI.Events = AnimationModule.AnimationUI.Events;
