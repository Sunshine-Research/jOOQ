/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/eula
 */

package org.jooq.impl;

import org.jooq.BindContext;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Schema;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
class Dual extends AbstractTable<Record> {

    private static final long serialVersionUID = -7492790780048090156L;

    Dual() {
        super("dual", (Schema) null);
    }

    @Override
    public final Class<? extends Record> getRecordType() {
        return RecordImpl.class;
    }

    @Override
    public final Table<Record> as(String alias) {
        return new TableAlias<Record>(this, alias);
    }

    @Override
    public final Table<Record> as(String alias, String... fieldAliases) {
        return new TableAlias<Record>(this, alias, fieldAliases);
    }

    @Override
    public final void toSQL(RenderContext context) {
        switch (context.configuration().dialect().family()) {
            /* [com] */
            case ASE:
            case SQLSERVER:
            /* [/com] */
            case POSTGRES:
            case SQLITE:
                break;

            case FIREBIRD:
                context.literal("RDB$DATABASE");
                break;

            case HSQLDB:
                context.literal("INFORMATION_SCHEMA")
                       .sql(".")
                       .literal("SYSTEM_USERS");
                break;

            case CUBRID:
                context.literal("db_root");
                break;

            // These dialects don't have a DUAL table. But simulation is needed
            // for queries like SELECT 1 WHERE 1 = 1
            /* [com] */
            case INGRES:
                context.keyword("(select 1 as dual) as dual");
                break;

            case DB2:
                context.literal("SYSIBM")
                       .sql(".")
                       .literal("DUAL");
                break;

            case SYBASE:
                context.literal("SYS")
                       .sql(".")
                       .literal("DUMMY");
                break;

            /* [/com] */
            case DERBY:
                context.literal("SYSIBM")
                       .sql(".")
                       .literal("SYSDUMMY1");
                break;

            default:
                context.keyword("dual");
                break;
        }
    }

    @Override
    public final void bind(BindContext context) {}

    @Override
    final Fields<Record> fields0() {
        return new Fields<Record>();
    }
}
