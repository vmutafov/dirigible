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
package org.eclipse.dirigible.components.data.structures.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.eclipse.dirigible.components.data.structures.domain.Table;
import org.eclipse.dirigible.components.data.structures.domain.TableColumn;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackages = { "org.eclipse.dirigible.components" })
@EntityScan("org.eclipse.dirigible.components")
@Transactional
public class TableRepositoryTest {
	
	@Autowired
	private TableRepository tableRepository;
	
	@Autowired
	private TableColumnRepository tableColumnRepository;
	
	@Autowired
	EntityManager entityManager;
	
	@BeforeEach
    public void setup() throws Exception {

    	// create test Tables
		createTable(tableRepository, tableColumnRepository, "/a/b/c/t1.table", "t1", "description", "");
		createTable(tableRepository, tableColumnRepository, "/a/b/c/t2.table", "t2", "description", "");
		createTable(tableRepository, tableColumnRepository, "/a/b/c/t3.table", "t3", "description", "");
		createTable(tableRepository, tableColumnRepository, "/a/b/c/t4.table", "t4", "description", "");
		createTable(tableRepository, tableColumnRepository, "/a/b/c/t5.table", "t5", "description", "");
    }
	
	@AfterEach
    public void cleanup() throws Exception {
		
		// delete test Tables
		tableRepository.findAll().stream().forEach(e -> tableRepository.delete(e));
    }
	

	@Test
    public void getOne() {
		Long id = tableRepository.findAll().get(0).getId();
		Optional<Table> optional = tableRepository.findById(id);
		Table table = optional.isPresent() ? optional.get() : null;
        assertNotNull(table);
        assertNotNull(table.getLocation());
        assertNotNull(table.getCreatedBy());
        assertEquals("SYSTEM", table.getCreatedBy());
        assertNotNull(table.getCreatedAt());
        assertNotNull(table.getColumns());
        assertNotNull(table.getColumns().get(0));
        assertEquals(table.getName()  + "_1", table.getColumns().get(0).getName());
        assertNotNull(table.getIndexes());
        assertNotNull(table.getIndexes().get(0));
        assertEquals(table.getName()  + "_1", table.getIndexes().get(0).getName());
//        assertEquals("table:/a/b/c/t1.table:t1", table.getKey());
    }
	
	@Test
    public void getReferenceUsingEntityManager() {
		Long id = tableRepository.findAll().get(0).getId();
		Table extension = entityManager.getReference(Table.class, id);
        assertNotNull(extension);
        assertNotNull(extension.getLocation());
    }
	
	public static Table createTable(TableRepository tableRepository, TableColumnRepository tableColumnRepository, String location, String name, String description, String dependencies) {
		Table table = new Table(location, name, description, dependencies);
		table.addColumn(name + "_1", "VARCHAR", 20, true, false, "", 0, false);
		table.addColumn(name + "_2", "VARCHAR", 20, true, false, "", 0, false);
		table.addIndex(name + "_1", "", true, name + "_1");
		tableRepository.save(table);
		return table;
	}
	
	@SpringBootApplication
	static class TestConfiguration {
	}
	
}
