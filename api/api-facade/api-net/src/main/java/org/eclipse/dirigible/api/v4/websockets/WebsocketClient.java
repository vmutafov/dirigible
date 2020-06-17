/**
 * Copyright (c) 2010-2020 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *   SAP - initial API and implementation
 */
package org.eclipse.dirigible.api.v4.websockets;

import java.util.HashMap;
import java.util.Map;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.eclipse.dirigible.commons.api.scripting.ScriptingException;
import org.eclipse.dirigible.engine.api.script.ScriptEngineExecutorsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ClientEndpoint
public class WebsocketClient {
	
	private static Logger logger = LoggerFactory.getLogger(WebsocketClient.class);
	
	private String handler;
	
	private String engine;
	
	private Session session;
	
	public String getHandler() {
		return handler;
	}
	
	public String getEngine() {
		return engine;
	}
	
	public Session getSession() {
		return session;
	}
	
	public WebsocketClient(String handler, String engine) {
		this.handler = handler;
		this.engine = engine;
	}
	
    @OnOpen
    public void onOpen(Session session) throws ScriptingException {
    	this.session = session;
    	WebsocketsFacade.CLIENTS.add(this);
    	Map<Object, Object> context = new HashMap<>();
    	context.put("METHOD", "onopen");
    	ScriptEngineExecutorsManager.executeServiceModule(this.engine, this.handler, context);
    }

    @OnMessage
    public void processMessage(String message) throws ScriptingException {
    	Map<Object, Object> context = new HashMap<>();
    	context.put("MESSAGE", message);
    	context.put("METHOD", "onmessage");
    	ScriptEngineExecutorsManager.executeServiceModule(this.engine, this.handler, context);
    }

    @OnError
    public void processError(Throwable t) throws ScriptingException {
    	logger.error(t.getMessage(), t);
    	Map<Object, Object> context = new HashMap<>();
    	context.put("ERROR", t.getMessage());
    	context.put("METHOD", "onerror");
    	ScriptEngineExecutorsManager.executeServiceModule(this.engine, this.handler, context);
    }
    
    @OnClose
    public void onClose(Session session) throws ScriptingException {
    	WebsocketsFacade.CLIENTS.remove(this);
    	Map<Object, Object> context = new HashMap<>();
    	context.put("METHOD", "onclose");
    	ScriptEngineExecutorsManager.executeServiceModule(this.engine, this.handler, context);
    }

}
