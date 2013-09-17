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
package org.jooq.test._.testcases;

import static junit.framework.Assert.assertEquals;
import static org.jooq.tools.reflect.Reflect.on;

import java.sql.Date;
import java.util.List;

import org.jooq.EnumType;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;
import org.jooq.test._.converters.Boolean_10;
import org.jooq.test._.converters.Boolean_TF_LC;
import org.jooq.test._.converters.Boolean_TF_UC;
import org.jooq.test._.converters.Boolean_YES_NO_LC;
import org.jooq.test._.converters.Boolean_YES_NO_UC;
import org.jooq.test._.converters.Boolean_YN_LC;
import org.jooq.test._.converters.Boolean_YN_UC;

import org.junit.Test;

public class EnumTests<
    A    extends UpdatableRecord<A> & Record6<Integer, String, String, Date, Integer, ?>,
    AP,
    B    extends UpdatableRecord<B>,
    S    extends UpdatableRecord<S> & Record1<String>,
    B2S  extends UpdatableRecord<B2S> & Record3<String, Integer, Integer>,
    BS   extends UpdatableRecord<BS>,
    L    extends TableRecord<L> & Record2<String, String>,
    X    extends TableRecord<X>,
    DATE extends UpdatableRecord<DATE>,
    BOOL extends UpdatableRecord<BOOL>,
    D    extends UpdatableRecord<D>,
    T    extends UpdatableRecord<T>,
    U    extends TableRecord<U>,
    UU   extends UpdatableRecord<UU>,
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785> {

    public EnumTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785> delegate) {
        super(delegate);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testEnums() throws Exception {
        if (TBook_STATUS() == null) {
            log.info("SKIPPING", "enums test");
            return;
        }

        jOOQAbstractTest.reset = false;

        B book = create()
            .selectFrom(TBook())
            .where(TBook_PUBLISHED_IN().equal(1990))
            .fetchOne();
        Enum<?> value = book.getValue(TBook_STATUS());
        assertEquals("SOLD_OUT", value.name());
        assertEquals("SOLD OUT", ((EnumType) value).getLiteral());

        // Another copy of the original record
        book = create().fetchOne(TBook(), TBook_TITLE().equal("1984"));
        book.setValue((Field) TBook_STATUS(), Enum.valueOf(value.getClass(), "ON_STOCK"));
        book.store();

        book = create().fetchOne(TBook(), TBook_TITLE().equal("1984"));
        value = book.getValue(TBook_STATUS());
        assertEquals("ON_STOCK", value.name());
        assertEquals("ON STOCK", ((EnumType) value).getLiteral());
    }

    @Test
    public <R extends TableRecord<R>> void testCustomEnums() throws Exception {
        jOOQAbstractTest.reset = false;

        // This does not yet work correctly for Sybase ASE, Postgres
        // Sybase: Is casting enums to unknown enum types
        // ASE: Cannot implicitly cast '1' to 1

        // TODO [#677] [#1013] This doesn't work correctly yet for
        // Ingres, HSQLDB, H2, Derby, Sybase ASE
        // Double-check again for Postgres

        // Insertion
        // --------------------------------------------------------------------
        assertEquals(1,
        create().insertInto(TBooleans())
                .set(TBooleans_ID(), 1)
                .set(TBooleans_BOOLEAN_10(), Boolean_10.ZERO)
                .set(TBooleans_Boolean_TF_LC(), Boolean_TF_LC.FALSE)
                .set(TBooleans_Boolean_TF_UC(), Boolean_TF_UC.FALSE)
                .set(TBooleans_Boolean_YES_NO_LC(), Boolean_YES_NO_LC.no)
                .set(TBooleans_Boolean_YES_NO_UC(), Boolean_YES_NO_UC.NO)
                .set(TBooleans_Boolean_YN_LC(), Boolean_YN_LC.n)
                .set(TBooleans_Boolean_YN_UC(), Boolean_YN_UC.N)
//                .set(TBooleans_C(), false)
//                .set(TBooleans_VC(), false)
//                .set(TBooleans_N(), false)
                .execute());

        assertEquals(1,
        create().insertInto(TBooleans())
                .set(TBooleans_ID(), 2)
                .set(TBooleans_BOOLEAN_10(), Boolean_10.ONE)
                .set(TBooleans_Boolean_TF_LC(), Boolean_TF_LC.TRUE)
                .set(TBooleans_Boolean_TF_UC(), Boolean_TF_UC.TRUE)
                .set(TBooleans_Boolean_YES_NO_LC(), Boolean_YES_NO_LC.yes)
                .set(TBooleans_Boolean_YES_NO_UC(), Boolean_YES_NO_UC.YES)
                .set(TBooleans_Boolean_YN_LC(), Boolean_YN_LC.y)
                .set(TBooleans_Boolean_YN_UC(), Boolean_YN_UC.Y)
//                .set(TBooleans_C(), true)
//                .set(TBooleans_VC(), true)
//                .set(TBooleans_N(), true)
                .execute());

        // Selection
        // --------------------------------------------------------------------
        Result<?> result =
        create().selectFrom(TBooleans())
                .where(TBooleans_ID().in(1, 2))
                .and(TBooleans_BOOLEAN_10().in(Boolean_10.ONE, Boolean_10.ZERO))
                .and(TBooleans_Boolean_TF_LC().in(Boolean_TF_LC.TRUE, Boolean_TF_LC.FALSE))
                .and(TBooleans_Boolean_TF_UC().in(Boolean_TF_UC.TRUE, Boolean_TF_UC.FALSE))
                .and(TBooleans_Boolean_YES_NO_LC().in(Boolean_YES_NO_LC.yes, Boolean_YES_NO_LC.no))
                .and(TBooleans_Boolean_YES_NO_UC().in(Boolean_YES_NO_UC.YES, Boolean_YES_NO_UC.NO))
                .and(TBooleans_Boolean_YN_LC().in(Boolean_YN_LC.y, Boolean_YN_LC.n))
                .and(TBooleans_Boolean_YN_UC().in(Boolean_YN_UC.Y, Boolean_YN_UC.N))
                .orderBy(TBooleans_ID().asc())
                .fetch();

        assertEquals(1, (int) result.getValue(0, TBooleans_ID()));
        assertEquals(2, (int) result.getValue(1, TBooleans_ID()));

        assertEquals(Boolean_10.ZERO, result.getValue(0, TBooleans_BOOLEAN_10()));
        assertEquals(Boolean_10.ONE, result.getValue(1, TBooleans_BOOLEAN_10()));

        assertEquals(Boolean_TF_LC.FALSE, result.getValue(0, TBooleans_Boolean_TF_LC()));
        assertEquals(Boolean_TF_LC.TRUE, result.getValue(1, TBooleans_Boolean_TF_LC()));

        assertEquals(Boolean_TF_UC.FALSE, result.getValue(0, TBooleans_Boolean_TF_UC()));
        assertEquals(Boolean_TF_UC.TRUE, result.getValue(1, TBooleans_Boolean_TF_UC()));

        assertEquals(Boolean_YES_NO_LC.no, result.getValue(0, TBooleans_Boolean_YES_NO_LC()));
        assertEquals(Boolean_YES_NO_LC.yes, result.getValue(1, TBooleans_Boolean_YES_NO_LC()));

        assertEquals(Boolean_YES_NO_UC.NO, result.getValue(0, TBooleans_Boolean_YES_NO_UC()));
        assertEquals(Boolean_YES_NO_UC.YES, result.getValue(1, TBooleans_Boolean_YES_NO_UC()));

        assertEquals(Boolean_YN_LC.n, result.getValue(0, TBooleans_Boolean_YN_LC()));
        assertEquals(Boolean_YN_LC.y, result.getValue(1, TBooleans_Boolean_YN_LC()));

        assertEquals(Boolean_YN_UC.N, result.getValue(0, TBooleans_Boolean_YN_UC()));
        assertEquals(Boolean_YN_UC.Y, result.getValue(1, TBooleans_Boolean_YN_UC()));

//        assertFalse(result.getValue(0, TBooleans_C()));
//        assertTrue(result.getValue(1, TBooleans_C()));
//
//        assertFalse(result.getValue(0, TBooleans_VC()));
//        assertTrue(result.getValue(1, TBooleans_VC()));
//
//        assertFalse(result.getValue(0, TBooleans_N()));
//        assertTrue(result.getValue(1, TBooleans_N()));

        // Conversion to custom POJOs
        // --------------------------------------------------------------------
        if (TBooleansPojo() == null) {
            log.info("SKIPPING", "Generated POJO tests");
        }
        else {
            List<Object> b =
            create().selectFrom(TBooleans())
                    .orderBy(TBooleans_ID().asc())
                    .fetchInto(TBooleansPojo());

            assertEquals(2, b.size());
            assertEquals(1, on(b.get(0)).call("getId").get());
            assertEquals(2, on(b.get(1)).call("getId").get());

            assertEquals(Boolean_10.ZERO, on(b.get(0)).call("getOneZero").get());
            assertEquals(Boolean_10.ONE, on(b.get(1)).call("getOneZero").get());

            assertEquals(Boolean_TF_LC.FALSE, on(b.get(0)).call("getTrueFalseLc").get());
            assertEquals(Boolean_TF_LC.TRUE, on(b.get(1)).call("getTrueFalseLc").get());

            assertEquals(Boolean_TF_UC.FALSE, on(b.get(0)).call("getTrueFalseUc").get());
            assertEquals(Boolean_TF_UC.TRUE, on(b.get(1)).call("getTrueFalseUc").get());

            assertEquals(Boolean_YES_NO_LC.no, on(b.get(0)).call("getYesNoLc").get());
            assertEquals(Boolean_YES_NO_LC.yes, on(b.get(1)).call("getYesNoLc").get());

            assertEquals(Boolean_YES_NO_UC.NO, on(b.get(0)).call("getYesNoUc").get());
            assertEquals(Boolean_YES_NO_UC.YES, on(b.get(1)).call("getYesNoUc").get());

            assertEquals(Boolean_YN_LC.n, on(b.get(0)).call("getYNLc").get());
            assertEquals(Boolean_YN_LC.y, on(b.get(1)).call("getYNLc").get());

            assertEquals(Boolean_YN_UC.N, on(b.get(0)).call("getYNUc").get());
            assertEquals(Boolean_YN_UC.Y, on(b.get(1)).call("getYNUc").get());
        }
    }
}
