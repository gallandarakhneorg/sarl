/*
 * $Id$
 *
 * SARL is an general-purpose agent programming language.
 * More details on http://www.sarl.io
 *
 * Copyright (C) 2014-2025 SARL.io, the Original Authors and Main Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.sarl.lang.core.tests.scoping.extensions.numbers.comparison.doubleprimitive;

import static io.sarl.lang.core.scoping.extensions.numbers.comparison.PrimitiveDoubleComparisonExtensions.operator_equals;
import static io.sarl.lang.core.scoping.extensions.numbers.comparison.PrimitiveDoubleComparisonExtensions.operator_greaterEqualsThan;
import static io.sarl.lang.core.scoping.extensions.numbers.comparison.PrimitiveDoubleComparisonExtensions.operator_greaterThan;
import static io.sarl.lang.core.scoping.extensions.numbers.comparison.PrimitiveDoubleComparisonExtensions.operator_lessEqualsThan;
import static io.sarl.lang.core.scoping.extensions.numbers.comparison.PrimitiveDoubleComparisonExtensions.operator_lessThan;
import static io.sarl.lang.core.scoping.extensions.numbers.comparison.PrimitiveDoubleComparisonExtensions.operator_notEquals;
import static io.sarl.lang.core.scoping.extensions.numbers.comparison.PrimitiveDoubleComparisonExtensions.operator_spaceship;
import static io.sarl.tests.api.tools.TestAssertions.assertStrictlyNegative;
import static io.sarl.tests.api.tools.TestAssertions.assertStrictlyPositive;
import static io.sarl.tests.api.tools.TestAssertions.assertZero;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.util.concurrent.AtomicDouble;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see "https://github.com/eclipse/xtext-extras/issues/186"
 */
@SuppressWarnings("all")
@DisplayName("Calling double comparison operators")
@Tag("core")
@Tag("unit")
public class CodeTest {

	private static double left = 4;

	private static double left2 = 2;

	private static int right = 3;

	private static int right2 = 5;

	@Test
	public void operator_greaterEqualsThan_double_Number() {
		assertTrue(operator_greaterEqualsThan(left, new AtomicDouble(right)));
		assertFalse(operator_greaterEqualsThan(left2, new AtomicDouble(right)));
		assertTrue(operator_greaterEqualsThan(left, new AtomicDouble(left)));
	}

	@Test
	public void operator_lessEqualsThan_double_Number() {
		assertFalse(operator_lessEqualsThan(left, new AtomicDouble(right)));
		assertTrue(operator_lessEqualsThan(left2, new AtomicDouble(right)));
		assertTrue(operator_lessEqualsThan(left, new AtomicDouble(left)));
	}

	@Test
	public void operator_greaterThan_double_Number() {
		assertTrue(operator_greaterThan(left, new AtomicDouble(right)));
		assertFalse(operator_greaterThan(left2, new AtomicDouble(right)));
		assertFalse(operator_greaterThan(left, new AtomicDouble(left)));
	}

	@Test
	public void operator_lessThan_double_Number() {
		assertFalse(operator_lessThan(left, new AtomicDouble(right)));
		assertTrue(operator_lessThan(left2, new AtomicDouble(right)));
		assertFalse(operator_lessThan(left, new AtomicDouble(left)));
	}

	@Test
	public void operator_equals_double_Number() {
		assertFalse(operator_equals(left, new AtomicDouble(right)));
		assertFalse(operator_equals(left2, new AtomicDouble(right)));
		assertTrue(operator_equals(left, new AtomicDouble(left)));
	}

	@Test
	public void operator_notEquals_double_Number() {
		assertTrue(operator_notEquals(left, new AtomicDouble(right)));
		assertTrue(operator_notEquals(left2, new AtomicDouble(right)));
		assertFalse(operator_notEquals(left, new AtomicDouble(left)));
	}

	@Test
	public void operator_spaceship_double_byte() {
		assertStrictlyPositive(operator_spaceship(left, (byte) right));
		assertStrictlyNegative(operator_spaceship(left, (byte) right2));
		assertZero(operator_spaceship(left, (byte) left));
	}

	@Test
	public void operator_spaceship_double_short() {
		assertStrictlyPositive(operator_spaceship(left, (short) right));
		assertStrictlyNegative(operator_spaceship(left, (short) right2));
		assertZero(operator_spaceship(left, (short) left));
	}

	@Test
	public void operator_spaceship_double_int() {
		assertStrictlyPositive(operator_spaceship(left, (int) right));
		assertStrictlyNegative(operator_spaceship(left, (int) right2));
		assertZero(operator_spaceship(left, (int) left));
	}

	@Test
	public void operator_spaceship_double_long() {
		assertStrictlyPositive(operator_spaceship(left, (long) right));
		assertStrictlyNegative(operator_spaceship(left, (long) right2));
		assertZero(operator_spaceship(left, (long) left));
	}

	@Test
	public void operator_spaceship_double_float() {
		assertStrictlyPositive(operator_spaceship(left, (float) right));
		assertStrictlyNegative(operator_spaceship(left, (float) right2));
		assertZero(operator_spaceship(left, (float) left));
	}

	@Test
	public void operator_spaceship_double_double() {
		assertStrictlyPositive(operator_spaceship(left, (double) right));
		assertStrictlyNegative(operator_spaceship(left, (double) right2));
		assertZero(operator_spaceship(left, (double) left));
	}

	@Test
	public void operator_spaceship_double_Byte() {
		assertStrictlyPositive(operator_spaceship(left, Byte.valueOf((byte) right)));
		assertStrictlyNegative(operator_spaceship(left, Byte.valueOf((byte) right2)));
		assertZero(operator_spaceship(left, Byte.valueOf((byte) left)));
	}

	@Test
	public void operator_spaceship_double_Short() {
		assertStrictlyPositive(operator_spaceship(left, Short.valueOf((short) right)));
		assertStrictlyNegative(operator_spaceship(left, Short.valueOf((short) right2)));
		assertZero(operator_spaceship(left, Short.valueOf((short) left)));
	}

	@Test
	public void operator_spaceship_double_Integer() {
		assertStrictlyPositive(operator_spaceship(left, Integer.valueOf(right)));
		assertStrictlyNegative(operator_spaceship(left, Integer.valueOf(right2)));
		assertZero(operator_spaceship(left, Integer.valueOf((int) left)));
	}

	@Test
	public void operator_spaceship_double_Long() {
		assertStrictlyPositive(operator_spaceship(left, Long.valueOf(right)));
		assertStrictlyNegative(operator_spaceship(left, Long.valueOf(right2)));
		assertZero(operator_spaceship(left, Long.valueOf((long) left)));
	}

	@Test
	public void operator_spaceship_double_Float() {
		assertStrictlyPositive(operator_spaceship(left, Float.valueOf(right)));
		assertStrictlyNegative(operator_spaceship(left, Float.valueOf(right2)));
		assertZero(operator_spaceship(left, Float.valueOf((float) left)));
	}

	@Test
	public void operator_spaceship_double_Double() {
		assertStrictlyPositive(operator_spaceship(left, Double.valueOf(right)));
		assertStrictlyNegative(operator_spaceship(left, Double.valueOf(right2)));
		assertZero(operator_spaceship(left, Double.valueOf(left)));
	}

	@Test
	public void operator_spaceship_double_AtomicInteger() {
		assertStrictlyPositive(operator_spaceship(left, new AtomicInteger(right)));
		assertStrictlyNegative(operator_spaceship(left, new AtomicInteger(right2)));
		assertZero(operator_spaceship(left, new AtomicInteger((int) left)));
	}

	@Test
	public void operator_spaceship_double_AtomicLong() {
		assertStrictlyPositive(operator_spaceship(left, new AtomicLong(right)));
		assertStrictlyNegative(operator_spaceship(left, new AtomicLong(right2)));
		assertZero(operator_spaceship(left, new AtomicLong((long) left)));
	}

}
