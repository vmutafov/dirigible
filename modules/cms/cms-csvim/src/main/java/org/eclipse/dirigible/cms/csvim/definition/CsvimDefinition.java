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
package org.eclipse.dirigible.cms.csvim.definition;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.eclipse.dirigible.commons.api.artefacts.IArtefactDefinition;
import org.eclipse.dirigible.commons.api.helpers.GsonHelper;

/**
 * The CsvimDefinition Entity.
 */
@Table(name = "DIRIGIBLE_CSVIM")
public class CsvimDefinition implements IArtefactDefinition {

	@Id
	@Column(name = "CSVIM_LOCATION", columnDefinition = "VARCHAR", nullable = false, length = 255)
	private String location;
	
	@Column(name = "CSVIM_HASH", columnDefinition = "VARCHAR", nullable = false, length = 32)
	private String hash;

	@Column(name = "CSVIM_CREATED_BY", columnDefinition = "VARCHAR", nullable = false, length = 128)
	private String createdBy;

	@Column(name = "CSVIM_CREATED_AT", columnDefinition = "TIMESTAMP", nullable = false)
	private Timestamp createdAt;
	
	private List<CsvFileDefinition> csvFileDefinitions;

	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Sets the location.
	 *
	 * @param location
	 *            the new location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * @param hash the hash to set
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}

	/**
	 * Gets the created by.
	 *
	 * @return the created by
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * Sets the created by.
	 *
	 * @param createdBy
	 *            the new created by
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * Gets the created at.
	 *
	 * @return the created at
	 */
	public Timestamp getCreatedAt() {
		if (createdAt == null) {
			return null;
		}
		return new Timestamp(createdAt.getTime());
	}

	/**
	 * Sets the created at.
	 *
	 * @param createdAt
	 *            the new created at
	 */
	public void setCreatedAt(Timestamp createdAt) {
		if (createdAt == null) {
			this.createdAt = null;
			return;
		}
		this.createdAt = new Timestamp(createdAt.getTime());
	}
	
	/**
	 * Gets the CSV files
	 * 
	 * @return the list of CSV files
	 */
	public List<CsvFileDefinition> getCsvFileDefinitions() {
		return csvFileDefinitions;
	}
	
	/**
	 * Sets the CSV files
	 * 
	 * @param csvFileDefinitions the list of CSV files
	 */
	public void setCsvFileDefinitions(List<CsvFileDefinition> csvFileDefinitions) {
		this.csvFileDefinitions = csvFileDefinitions;
	}

	/**
	 * Creates ExtensionPointDefinition from JSON.
	 *
	 * @param json
	 *            the JSON
	 * @return the extension point definition
	 */
	public static CsvimDefinition fromJson(String json) {
		return GsonHelper.GSON.fromJson(json, CsvimDefinition.class);
	}

	/**
	 * Converts ExtensionPointDefinition to JSON.
	 *
	 * @return the JSON
	 */
	public String toJson() {
		return GsonHelper.GSON.toJson(this, CsvimDefinition.class);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return toJson();
	}

	@Override
	public String getArtefactName() {
		return getLocation();
	}

	@Override
	public String getArtefactLocation() {
		return getLocation();
	}

	@Override
	public int hashCode() {
		return Objects.hash(hash, location);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CsvimDefinition other = (CsvimDefinition) obj;
		return Objects.equals(hash, other.hash) && Objects.equals(location, other.location);
	}
	
	

}
