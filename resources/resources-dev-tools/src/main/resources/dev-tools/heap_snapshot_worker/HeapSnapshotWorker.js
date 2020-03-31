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
import {HeapSnapshotWorkerDispatcher} from './HeapSnapshotWorkerDispatcher.js';

function postMessageWrapper(message) {
  postMessage(message);
}

const dispatcher = new HeapSnapshotWorkerDispatcher(self, postMessageWrapper);

/**
 * @param {function(!Event)} listener
 * @suppressGlobalPropertiesCheck
 */
function installMessageEventListener(listener) {
  self.addEventListener('message', listener, false);
}

installMessageEventListener(dispatcher.dispatchMessage.bind(dispatcher));
