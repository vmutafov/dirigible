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
package org.eclipse.dirigible.repository.api;

import java.nio.charset.Charset;

/**
 * This interface represents a Repository. It allows for querying, modifying and
 * navigating through collections and resources.
 */
public interface IRepository
		extends IRepositoryReader, IRepositoryWriter, IRepositoryImporter, IRepositoryExporter, IRepositorySearch {

	/** The Constant SEPARATOR. */
	public static final String SEPARATOR = IRepositoryStructure.SEPARATOR;

	/** The Constant UTF8. */
	public static final Charset UTF8 = Charset.forName("UTF-8");

	/** The Constant DIRIGIBLE_REPOSITORY_PROVIDER. */
	public static final String DIRIGIBLE_REPOSITORY_PROVIDER = "DIRIGIBLE_REPOSITORY_PROVIDER"; //$NON-NLS-1$

	/** The Constant DIRIGIBLE_REPOSITORY_PROVIDER_LOCAL. */
	public static final String DIRIGIBLE_REPOSITORY_PROVIDER_LOCAL = "local"; //$NON-NLS-1$

	/** The Constant DIRIGIBLE_REPOSITORY_PROVIDER_DATABASE. */
	public static final String DIRIGIBLE_REPOSITORY_PROVIDER_DATABASE = "database"; //$NON-NLS-1$
	
	/** The Constant DIRIGIBLE_REPOSITORY_CACHE_ENABLED. */
	public static final String DIRIGIBLE_REPOSITORY_CACHE_ENABLED = "DIRIGIBLE_REPOSITORY_CACHE_ENABLED"; //$NON-NLS-1$

	/** The Constant DIRIGIBLE_REPOSITORY_CACHE_TIME_LIMIT_IN_MINUTES. */
	public static final String DIRIGIBLE_REPOSITORY_CACHE_TIME_LIMIT_IN_MINUTES = "DIRIGIBLE_REPOSITORY_CACHE_TIME_LIMIT_IN_MINUTES"; //$NON-NLS-1$

	/** The Constant DIRIGIBLE_REPOSITORY_CACHE_SIZE_LIMIT_IN_MEGABYTES. */
	public static final String DIRIGIBLE_REPOSITORY_CACHE_SIZE_LIMIT_IN_MEGABYTES = "DIRIGIBLE_REPOSITORY_CACHE_SIZE_LIMIT_IN_MEGABYTES"; //$NON-NLS-1$

	/** Gets the internal named parameter value*/
	public String getParameter(String key);
	
	/** Gets the most recent time when the repository gets changed */
	public long getLastModified();

	default String getInternalResourcePath(String resourcePath) {
		throw new RepositoryException("Operation not supported for repository type: " + this.getClass().getName());
	}

	default String getRepositoryPath() {
		throw new RepositoryException("Operation not supported for repository type: " + this.getClass().getName());
	}
}
