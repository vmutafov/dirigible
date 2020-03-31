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
/**
 * @unrestricted
 */
export class WorkerWrapper {
  /**
   * @param {string} appName
   */
  constructor(appName) {
    const url = appName + '.js' + location.search;

    /** @type {!Promise<!Worker>} */
    this._workerPromise = new Promise(fulfill => {
      const worker = new Worker(url, {type: 'module'});
      worker.onmessage = event => {
        console.assert(event.data === 'workerReady');
        worker.onmessage = null;
        fulfill(worker);
      };
    });
  }

  /**
   * @param {*} message
   */
  postMessage(message) {
    this._workerPromise.then(worker => {
      if (!this._disposed) {
        worker.postMessage(message);
      }
    });
  }

  dispose() {
    this._disposed = true;
    this._workerPromise.then(worker => worker.terminate());
  }

  terminate() {
    this.dispose();
  }

  /**
   * @param {?function(!MessageEvent):void} listener
   */
  set onmessage(listener) {
    this._workerPromise.then(worker => worker.onmessage = listener);
  }

  /**
   * @param {?function(!Event):void} listener
   */
  set onerror(listener) {
    this._workerPromise.then(worker => worker.onerror = listener);
  }
}
