/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company and Eclipse Dirigible contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-FileCopyrightText: 2022 SAP SE or an SAP affiliate company and Eclipse Dirigible contributors
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.dirigible.engine.js.service;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.eclipse.dirigible.commons.api.service.AbstractRestService;
import org.eclipse.dirigible.commons.api.service.IRestService;
import org.eclipse.dirigible.commons.config.Configuration;
import org.eclipse.dirigible.engine.js.processor.JavascriptEngineProcessor;
import org.eclipse.dirigible.repository.api.RepositoryNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Front facing REST service serving the Javascript backend services.
 */
@Path("/js")
@Api(value = "JavaScript Engine", authorizations = { @Authorization(value = "basicAuth", scopes = {}) })
@ApiResponses({ @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
		@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Internal Server Error") })
public class JSWebService extends AbstractRestService implements IRestService {

	private static final Logger logger = LoggerFactory.getLogger(JSWebService.class.getCanonicalName());
	private static final String DIRIGIBLE_JS_WEB_HANDLER_CLASS_NAME_KEY = "DIRIGIBLE_JS_WEB_HANDLER_CLASS_NAME";
	private static final String HTTP_PATH_MATCHER = "/{projectName}/{projectFilePath:.*\\.js|.*\\.mjs}";
	private static final String HTTP_PATH_WITH_PARAM_MATCHER = "/{projectName}/{projectFilePath:.*\\.js|.*\\.mjs}/{projectFilePathParam}";

	@GET
	@Path(HTTP_PATH_MATCHER)
	public Response get(
			@PathParam("projectName") String projectName,
			@PathParam("projectFilePath") String projectFilePath
	) {
		return executeJavaScript(projectName, projectFilePath);
	}

	@GET
	@Path(HTTP_PATH_WITH_PARAM_MATCHER)
	public Response get(
			@PathParam("projectName") String projectName,
			@PathParam("projectFilePath") String projectFilePath,
			@PathParam("projectFilePathParam") String projectFilePathParam
	) {
		return executeJavaScript(projectName, projectFilePath, projectFilePathParam);
	}

	@POST
	@Path(HTTP_PATH_MATCHER)
	public Response post(
			@PathParam("projectName") String projectName,
			@PathParam("projectFilePath") String projectFilePath
	) {
		return executeJavaScript(projectName, projectFilePath);
	}

	@POST
	@Path(HTTP_PATH_WITH_PARAM_MATCHER)
	public Response post(
			@PathParam("projectName") String projectName,
			@PathParam("projectFilePath") String projectFilePath,
			@PathParam("projectFilePathParam") String projectFilePathParam
	) {
		return executeJavaScript(projectName, projectFilePath, projectFilePathParam);
	}

	@PUT
	@Path(HTTP_PATH_MATCHER)
	public Response put(
			@PathParam("projectName") String projectName,
			@PathParam("projectFilePath") String projectFilePath
	) {
		return executeJavaScript(projectName, projectFilePath);
	}

	@PUT
	@Path(HTTP_PATH_WITH_PARAM_MATCHER)
	public Response put(
			@PathParam("projectName") String projectName,
			@PathParam("projectFilePath") String projectFilePath,
			@PathParam("projectFilePathParam") String projectFilePathParam
	) {
		return executeJavaScript(projectName, projectFilePath, projectFilePathParam);
	}

	@PATCH
	@Path(HTTP_PATH_MATCHER)
	public Response patch(
			@PathParam("projectName") String projectName,
			@PathParam("projectFilePath") String projectFilePath
	) {
		return executeJavaScript(projectName, projectFilePath);
	}

	@PATCH
	@Path(HTTP_PATH_WITH_PARAM_MATCHER)
	public Response patch(
			@PathParam("projectName") String projectName,
			@PathParam("projectFilePath") String projectFilePath,
			@PathParam("projectFilePathParam") String projectFilePathParam
	) {
		return executeJavaScript(projectName, projectFilePath, projectFilePathParam);
	}

	@DELETE
	@Path(HTTP_PATH_MATCHER)
	public Response delete(
			@PathParam("projectName") String projectName,
			@PathParam("projectFilePath") String projectFilePath
	) {
		return executeJavaScript(projectName, projectFilePath);
	}

	@DELETE
	@Path(HTTP_PATH_WITH_PARAM_MATCHER)
	public Response delete(
			@PathParam("projectName") String projectName,
			@PathParam("projectFilePath") String projectFilePath,
			@PathParam("projectFilePathParam") String projectFilePathParam
	) {
		return executeJavaScript(projectName, projectFilePath, projectFilePathParam);
	}

	@HEAD
	@Path(HTTP_PATH_MATCHER)
	public Response head(
			@PathParam("projectName") String projectName,
			@PathParam("projectFilePath") String projectFilePath
	) {
		return executeJavaScript(projectName, projectFilePath);
	}

	@HEAD
	@Path(HTTP_PATH_WITH_PARAM_MATCHER)
	public Response head(
			@PathParam("projectName") String projectName,
			@PathParam("projectFilePath") String projectFilePath,
			@PathParam("projectFilePathParam") String projectFilePathParam
	) {
		return executeJavaScript(projectName, projectFilePath, projectFilePathParam);
	}

	private Response executeJavaScript(String projectName, String projectFilePath) {
		return executeJavaScript(projectName, projectFilePath, "");
	}

	private Response executeJavaScript(String projectName, String projectFilePath, String projectFilePathParam) {
		try {
			getJSWebHandler().handleJSRequest(projectName, projectFilePath, projectFilePathParam);
			return Response.ok().build();
		} catch (RepositoryNotFoundException e) {
			String message = e.getMessage() + ". Try to publish the service before execution.";
			throw new RepositoryNotFoundException(message, e);
		}
	}

	private JSWebHandler getJSWebHandler() {
		String jsWebHandlerClassName = Configuration.get(DIRIGIBLE_JS_WEB_HANDLER_CLASS_NAME_KEY, null);
		if (jsWebHandlerClassName == null) {
			return new DefaultJSWebHandler();
		}

		try {
			return (JSWebHandler) Class.forName(jsWebHandlerClassName).getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
			throw new RuntimeException("Could not use " + jsWebHandlerClassName, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.commons.api.service.IRestService#getType()
	 */
	@Override
	public Class<? extends IRestService> getType() {
		return JSWebService.class;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.commons.api.service.AbstractRestService#getLogger()
	 */
	@Override
	protected Logger getLogger() {
		return logger;
	}
}
