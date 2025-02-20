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
package org.eclipse.dirigible.database.sql.builders.sequence;

import org.eclipse.dirigible.database.sql.ISqlDialect;
import org.eclipse.dirigible.database.sql.builders.AbstractQuerySqlBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Next Value Sequence Builder.
 */
public class NextValueSequenceBuilder extends AbstractQuerySqlBuilder {

	private static final Logger logger = LoggerFactory.getLogger(NextValueSequenceBuilder.class);

	private String sequence = null;

	/**
	 * Instantiates a new next value sequence builder.
	 *
	 * @param dialect
	 *            the dialect
	 * @param sequence
	 *            the sequence
	 */
	public NextValueSequenceBuilder(ISqlDialect dialect, String sequence) {
		super(dialect);
		this.sequence = sequence;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.database.sql.ISqlBuilder#generate()
	 */
	@Override
	public String generate() {
		StringBuilder sql = new StringBuilder();

		// SELECT
		generateSelect(sql);

		// NEXTVAL
		generateNextValue(sql);

		String generated = sql.toString();

		logger.trace("generated: " + generated);

		return generated;
	}

	/**
	 * Generate select.
	 *
	 * @param sql
	 *            the sql
	 */
	protected void generateSelect(StringBuilder sql) {
		sql.append(KEYWORD_SELECT);
	}

	/**
	 * Generate next value.
	 *
	 * @param sql
	 *            the sql
	 */
	protected void generateNextValue(StringBuilder sql) {
		String sequenceName = (isCaseSensitive()) ? encapsulate(this.getSequence()) : this.getSequence();
		sql.append(SPACE).append(KEYWORD_NEXT_VALUE_FOR).append(SPACE).append(sequenceName);
	}

	/**
	 * Gets the sequence.
	 *
	 * @return the sequence
	 */
	public String getSequence() {
		return sequence;
	}

}
