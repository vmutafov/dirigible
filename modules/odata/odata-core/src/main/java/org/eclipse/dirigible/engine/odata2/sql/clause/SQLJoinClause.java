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
package org.eclipse.dirigible.engine.odata2.sql.clause;

import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.edm.EdmStructuralType;
import org.apache.olingo.odata2.api.uri.KeyPredicate;
import org.eclipse.dirigible.commons.config.Configuration;
import org.eclipse.dirigible.engine.odata2.sql.api.OData2Exception;
import org.eclipse.dirigible.engine.odata2.sql.api.SQLClause;
import org.eclipse.dirigible.engine.odata2.sql.builder.SQLContext;
import org.eclipse.dirigible.engine.odata2.sql.builder.SQLSelectBuilder;
import org.eclipse.dirigible.engine.odata2.sql.builder.SQLUtils;

import java.util.Collections;
import java.util.List;

import static org.apache.olingo.odata2.api.commons.HttpStatusCodes.INTERNAL_SERVER_ERROR;
import static org.eclipse.dirigible.engine.odata2.sql.utils.OData2Utils.fqn;
import static org.eclipse.dirigible.engine.odata2.sql.utils.OData2Utils.isPropertyParameter;

public final class SQLJoinClause implements SQLClause {

    private static final List<KeyPredicate> NO_PREDICATES_USED = Collections.emptyList();
    private static final String DOUBLE_QUOTES = "\"";

    private final EdmStructuralType start;
    private final EdmStructuralType target;
    private final String startFqn;
    private final String targetFqn;
    private final JoinType joinType;

    /**
     * The left join should be used for expands - we would like to get the
     * properties of an entity even if the expand (lik) or complex type is
     * empty.
     */
    public enum JoinType {
        LEFT
    }

    private final SQLSelectBuilder query;
    private List<KeyPredicate> keyPredicates = NO_PREDICATES_USED;

    public SQLJoinClause(final SQLSelectBuilder query, final EdmStructuralType start, final EdmStructuralType target) {
        this.start = start;
        this.target = target;
        this.query = query;
        this.startFqn = fqn(start);
        this.targetFqn = fqn(target);
        this.joinType = JoinType.LEFT;
    }

    public String evaluate(SQLContext context) throws EdmException {
        if (isEmpty()) {
            return "";
        }

        StringBuilder join = new StringBuilder();

        boolean hasFirstJsonMappingTable = query.hasSQLMappingTablePresent(start, target);
        boolean hasSecondJsonMappingTable = query.hasSQLMappingTablePresent(target, start);

        if (hasFirstJsonMappingTable && hasSecondJsonMappingTable) {

            buildMappingTableJoin(join);

        } else if (!hasFirstJsonMappingTable && !hasSecondJsonMappingTable) {

            buildJoinWithoutMappingTable(join);

        } else {

            throw new IllegalArgumentException("Missing manyToManyMappingTable definition in the following json file: " +
                    "" + (hasFirstJsonMappingTable ? target.getName() : start.getName()) +
                    ". Both json files need to point to the mapping table");
        }

        return join.toString();
    }

    private void buildMappingTableJoin(StringBuilder join) throws EdmException {

        validateMappingTable();

        List<String> firstJoinColumns = getTargetJoinKeyForEntityType(target, start);
        String firstJoinLeftTableAlias = query.getSQLTableAlias(target);
        String firstJoinRightTable = query.getSQLMappingTableName(start, target).get(0);
        String firstJoinRightTableAlias = query.getSQLTableAliasForManyToManyMappingTable(firstJoinRightTable);
        List<String> firstJoinTargetKeys = query.getSQLMappingTableJoinColumn(target, start);

        buildJoinClause(firstJoinColumns, firstJoinLeftTableAlias, firstJoinRightTable, firstJoinRightTableAlias,
                firstJoinTargetKeys, join);

        // Only needed when adding a second join statement
        join.append(" ");

        List<String> secondJoinColumns = query.getSQLMappingTableJoinColumn(start, target);
        String secondJsonMappingTable = query.getSQLMappingTableName(target, start).get(0);
        String secondJoinLeftTableAlias = query.getSQLTableAliasForManyToManyMappingTable(secondJsonMappingTable);
        String secondJoinRightTable = query.getSQLTableName(start);
        String secondJoinRightTableAlias = query.getSQLTableAlias(start);
        List<String> secondJoinTargetKeys = getTargetJoinKeyForEntityType(start, target);

        buildJoinClause(secondJoinColumns, secondJoinLeftTableAlias, secondJoinRightTable, secondJoinRightTableAlias,
                secondJoinTargetKeys, join);
    }

    private void buildJoinWithoutMappingTable(StringBuilder join) throws EdmException {
        List<String> joinColumns = query.getSQLJoinTableName(target, start);
        String leftTableAlias = query.getSQLTableAlias(target);
        String rightTable = query.getSQLTableName(start);
        String rightTableAlias = query.getSQLTableAlias(start);
        List<String> targetKeys = getTargetJoinKeyForEntityType(start, target);

        buildJoinClause(joinColumns, leftTableAlias, rightTable, rightTableAlias, targetKeys, join);
    }

    private void buildJoinClause(List<String> joinColumns, String leftTableAlias, String rightTable,
                                 String rightTableAlias, List<String> targetKeys, StringBuilder join) throws EdmException {
        boolean caseSensitive = Boolean.parseBoolean(Configuration.get("DIRIGIBLE_DATABASE_NAMES_CASE_SENSITIVE", "false"));

        join.append(joinType.toString());

        join.append(" JOIN ");
        join.append(getValue(caseSensitive, rightTable));
        join.append(" AS ");
        join.append(getValue(caseSensitive, rightTableAlias));
        join.append(" ON ");

        for (int i = 0; i < targetKeys.size(); i++) {
            join.append(getValue(caseSensitive, rightTableAlias));
            join.append(".");
            join.append(getValue(caseSensitive, targetKeys.get(i)));
            join.append(" = ");
            join.append(getValue(caseSensitive, leftTableAlias));
            join.append(".");
            join.append(getValue(caseSensitive, joinColumns.get(i)));
            if (i < targetKeys.size() - 1) join.append(" AND ");
        }
    }

    private void validateMappingTable() throws EdmException {
        String firstJsonMappingTable = query.getSQLMappingTableName(start, target).get(0);
        String secondJsonMappingTable = query.getSQLMappingTableName(target, start).get(0);

        if (!firstJsonMappingTable.equals(secondJsonMappingTable)) {
            throw new IllegalArgumentException("OData manyToManyMappingTable name is different in both json files: " +
                    "" + (target.getName()) + " and " + (start.getName()) +
                    ". Both json files need to point to the same mapping table");
        }
    }

    private String getValue(boolean caseSensitive, String value) {
        return caseSensitive ? surroundWithDoubleQuotes(value) : value;
    }

    String surroundWithDoubleQuotes(String value) {
        if (value.startsWith(DOUBLE_QUOTES) && value.endsWith(DOUBLE_QUOTES)) {
            return value;
        }

        return DOUBLE_QUOTES + value + DOUBLE_QUOTES;
    }

    @Override
    public boolean isEmpty() {
        return !needsJoinQuery(start, keyPredicates, target);
    }


    @Override
    public String toString() {
        return "SQLJoin [startFqn=" + startFqn + ", targetFqn=" + targetFqn + ", joinType=" + joinType + "]";
    }

    // This Method is for internal use ONLY !!! Do NEVER use it !!!
    public SQLSelectBuilder with(List<KeyPredicate> keyPredicates) throws EdmException {
        if (this.keyPredicates != NO_PREDICATES_USED) {
            throw new OData2Exception("A where clause for the key predicates of this join expression is already added!",
                    INTERNAL_SERVER_ERROR);
        }

        this.keyPredicates = keyPredicates;
        SQLWhereClause where = SQLUtils.whereClauseFromKeyPredicates(query, start, keyPredicates);
        query.and(where);
        return query;
    }

    private List<String> getTargetJoinKeyForEntityType(EdmStructuralType start, EdmStructuralType end) throws EdmException {
        return query.getSQLJoinTableName(start, end);
    }

    private boolean needsJoinQuery(EdmStructuralType start, List<KeyPredicate> startPredicates, EdmStructuralType target) {
        try {
            if (start.getName().equals(target.getName())) {
                return false;
            } else {
                if (startPredicates == NO_PREDICATES_USED) {
                    return true;
                } else {
                    return ((startPredicates != null && !startPredicates.isEmpty()) && hasKeyPredicatesNonParameterProperty(start, startPredicates)) ? true : false;
                }
            }
        } catch (EdmException e) {
            throw new RuntimeException(e);//should never happen
        }
    }

    private boolean hasKeyPredicatesNonParameterProperty(EdmStructuralType start, List<KeyPredicate> keyPredicates) throws EdmException {
        for (KeyPredicate keyPredicate : keyPredicates) {
            if (!isPropertyParameter(keyPredicate.getProperty(), this.query, start)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((startFqn == null) ? 0 : startFqn.hashCode());
        result = prime * result + ((targetFqn == null) ? 0 : targetFqn.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SQLJoinClause other = (SQLJoinClause) obj;
        if (startFqn == null) {
            if (other.startFqn != null)
                return false;
        } else if (!startFqn.equals(other.startFqn))
            return false;
        if (targetFqn == null) {
            if (other.targetFqn != null)
                return false;
        } else if (!targetFqn.equals(other.targetFqn))
            return false;
        return true;
    }

}
