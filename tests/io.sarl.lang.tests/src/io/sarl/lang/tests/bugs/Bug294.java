/*
 * Copyright 2014-2015 Sebastian RODRIGUEZ, Nicolas GAUD, Stéphane GALLAND.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.sarl.lang.tests.bugs;

import static io.sarl.tests.api.AbstractSarlTest.assertContains;
import static io.sarl.tests.api.AbstractSarlTest.multilineString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import io.sarl.lang.SARLVersion;
import io.sarl.lang.actionprototype.ActionPrototype;
import io.sarl.tests.api.AbstractSarlTest;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.xtext.util.IAcceptor;
import org.eclipse.xtext.xbase.compiler.CompilationTestHelper;
import org.eclipse.xtext.xbase.compiler.CompilationTestHelper.Result;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.google.inject.Inject;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@RunWith(Suite.class)
@SuiteClasses({
	Bug294.CompilerTests.class,
})
@SuppressWarnings("all")
public class Bug294 {

	private static final String capacityCode = multilineString(
			"capacity PhysicEnvironment {",
			"	def influenceKinematic(linearInfluence : String = null, angularInfluence : float = 0f, otherInfluences : Object*)",
			"	def influenceSteering(linearInfluence : String = null, angularInfluence : float = 0f, otherInfluences : Object*)",
			"}");

	private static final String skillCode = multilineString(
			"skill StandardPhysicEnvironment implements PhysicEnvironment {",
			"	def influenceKinematic(linearInfluence : String = null, angularInfluence : float = 0f, otherInfluences : Object*) {",
			"	}",
			"	def influenceSteering(linearInfluence : String = null, angularInfluence : float = 0f, otherInfluences : Object*) {",
			"	}",
			"}");

	private static void assertActionKeys(Iterable<ActionPrototype> actual, String... expected) {
		assertNotNull(actual);
		Collection<Object> la = new ArrayList<>();
		for (ActionPrototype key : actual) {
			la.add(key.toString());
		}
		assertContains(la, expected);
	}

	/**
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class CompilerTests extends AbstractSarlTest {

		@Inject
		private CompilationTestHelper compiler;

		@Test
		public void testCompiler() throws Exception {
			final String expectedPhysicEnvironment = multilineString(
					"import io.sarl.lang.annotation.DefaultValue;",
					"import io.sarl.lang.annotation.DefaultValueSource;",
					"import io.sarl.lang.annotation.DefaultValueUse;",
					"import io.sarl.lang.annotation.SarlSourceCode;",
					"import io.sarl.lang.annotation.SarlSpecification;",
					"import io.sarl.lang.annotation.SyntheticMember;",
					"import io.sarl.lang.core.Capacity;",
					"",
					"@SarlSpecification(\"" + SARLVersion.SPECIFICATION_RELEASE_VERSION_STRING + "\")",
					"@SuppressWarnings(\"all\")",
					"public interface PhysicEnvironment extends Capacity {",
					"  @DefaultValueSource",
					"  public abstract void influenceKinematic(@DefaultValue(\"PhysicEnvironment#INFLUENCEKINEMATIC_0\") final String linearInfluence, @DefaultValue(\"PhysicEnvironment#INFLUENCEKINEMATIC_1\") final float angularInfluence, final Object... otherInfluences);",
					"  ",
					"  /**",
					"   * Default value for the parameter linearInfluence",
					"   */",
					"  @SyntheticMember",
					"  @SarlSourceCode(\"null\")",
					"  public final static String $DEFAULT_VALUE$INFLUENCEKINEMATIC_0 = null;",
					"  ",
					"  /**",
					"   * Default value for the parameter angularInfluence",
					"   */",
					"  @SyntheticMember",
					"  @SarlSourceCode(\"0f\")",
					"  public final static float $DEFAULT_VALUE$INFLUENCEKINEMATIC_1 = 0f;",
					"  ",
					"  @DefaultValueSource",
					"  public abstract void influenceSteering(@DefaultValue(\"PhysicEnvironment#INFLUENCESTEERING_0\") final String linearInfluence, @DefaultValue(\"PhysicEnvironment#INFLUENCESTEERING_1\") final float angularInfluence, final Object... otherInfluences);",
					"  ",
					"  /**",
					"   * Default value for the parameter linearInfluence",
					"   */",
					"  @SyntheticMember",
					"  @SarlSourceCode(\"null\")",
					"  public final static String $DEFAULT_VALUE$INFLUENCESTEERING_0 = null;",
					"  ",
					"  /**",
					"   * Default value for the parameter angularInfluence",
					"   */",
					"  @SyntheticMember",
					"  @SarlSourceCode(\"0f\")",
					"  public final static float $DEFAULT_VALUE$INFLUENCESTEERING_1 = 0f;",
					"  ",
					"  @DefaultValueUse(\"java.lang.String,float,java.lang.Object*\")",
					"  @SyntheticMember",
					"  public abstract void influenceKinematic(final Object... otherInfluences);",
					"  ",
					"  @DefaultValueUse(\"java.lang.String,float,java.lang.Object*\")",
					"  @SyntheticMember",
					"  public abstract void influenceKinematic(final float angularInfluence, final Object... otherInfluences);",
					"  ",
					"  @DefaultValueUse(\"java.lang.String,float,java.lang.Object*\")",
					"  @SyntheticMember",
					"  public abstract void influenceKinematic(final String linearInfluence, final Object... otherInfluences);",
					"  ",
					"  @DefaultValueUse(\"java.lang.String,float,java.lang.Object*\")",
					"  @SyntheticMember",
					"  public abstract void influenceSteering(final Object... otherInfluences);",
					"  ",
					"  @DefaultValueUse(\"java.lang.String,float,java.lang.Object*\")",
					"  @SyntheticMember",
					"  public abstract void influenceSteering(final float angularInfluence, final Object... otherInfluences);",
					"  ",
					"  @DefaultValueUse(\"java.lang.String,float,java.lang.Object*\")",
					"  @SyntheticMember",
					"  public abstract void influenceSteering(final String linearInfluence, final Object... otherInfluences);",
					"}",
					"");
			final String expectedStandardPhysicEnvironment = multilineString(
					"import io.sarl.lang.annotation.DefaultValue;",
					"import io.sarl.lang.annotation.DefaultValueSource;",
					"import io.sarl.lang.annotation.DefaultValueUse;",
					"import io.sarl.lang.annotation.SarlSourceCode;",
					"import io.sarl.lang.annotation.SarlSpecification;",
					"import io.sarl.lang.annotation.SyntheticMember;",
					"import io.sarl.lang.core.Agent;",
					"import io.sarl.lang.core.Skill;",
					"",
					"@SarlSpecification(\"" + SARLVersion.SPECIFICATION_RELEASE_VERSION_STRING + "\")",
					"@SuppressWarnings(\"all\")",
					"public class StandardPhysicEnvironment extends Skill implements PhysicEnvironment {",
					"  @DefaultValueSource",
					"  public void influenceKinematic(@DefaultValue(\"StandardPhysicEnvironment#INFLUENCEKINEMATIC_0\") final String linearInfluence, @DefaultValue(\"StandardPhysicEnvironment#INFLUENCEKINEMATIC_1\") final float angularInfluence, final Object... otherInfluences) {",
					"  }",
					"  ",
					"  /**",
					"   * Default value for the parameter linearInfluence",
					"   */",
					"  @SyntheticMember",
					"  @SarlSourceCode(\"null\")",
					"  private final static String $DEFAULT_VALUE$INFLUENCEKINEMATIC_0 = null;",
					"  ",
					"  /**",
					"   * Default value for the parameter angularInfluence",
					"   */",
					"  @SyntheticMember",
					"  @SarlSourceCode(\"0f\")",
					"  private final static float $DEFAULT_VALUE$INFLUENCEKINEMATIC_1 = 0f;",
					"  ",
					"  @DefaultValueSource",
					"  public void influenceSteering(@DefaultValue(\"StandardPhysicEnvironment#INFLUENCESTEERING_0\") final String linearInfluence, @DefaultValue(\"StandardPhysicEnvironment#INFLUENCESTEERING_1\") final float angularInfluence, final Object... otherInfluences) {",
					"  }",
					"  ",
					"  /**",
					"   * Default value for the parameter linearInfluence",
					"   */",
					"  @SyntheticMember",
					"  @SarlSourceCode(\"null\")",
					"  private final static String $DEFAULT_VALUE$INFLUENCESTEERING_0 = null;",
					"  ",
					"  /**",
					"   * Default value for the parameter angularInfluence",
					"   */",
					"  @SyntheticMember",
					"  @SarlSourceCode(\"0f\")",
					"  private final static float $DEFAULT_VALUE$INFLUENCESTEERING_1 = 0f;",
					"  ",
					"  @DefaultValueUse(\"java.lang.String,float,java.lang.Object*\")",
					"  @SyntheticMember",
					"  public final void influenceKinematic(final Object... otherInfluences) {",
					"    influenceKinematic($DEFAULT_VALUE$INFLUENCEKINEMATIC_0, $DEFAULT_VALUE$INFLUENCEKINEMATIC_1, otherInfluences);",
					"  }",
					"  ",
					"  @DefaultValueUse(\"java.lang.String,float,java.lang.Object*\")",
					"  @SyntheticMember",
					"  public final void influenceKinematic(final float angularInfluence, final Object... otherInfluences) {",
					"    influenceKinematic($DEFAULT_VALUE$INFLUENCEKINEMATIC_0, angularInfluence, otherInfluences);",
					"  }",
					"  ",
					"  @DefaultValueUse(\"java.lang.String,float,java.lang.Object*\")",
					"  @SyntheticMember",
					"  public final void influenceKinematic(final String linearInfluence, final Object... otherInfluences) {",
					"    influenceKinematic(linearInfluence, $DEFAULT_VALUE$INFLUENCEKINEMATIC_1, otherInfluences);",
					"  }",
					"  ",
					"  @DefaultValueUse(\"java.lang.String,float,java.lang.Object*\")",
					"  @SyntheticMember",
					"  public final void influenceSteering(final Object... otherInfluences) {",
					"    influenceSteering($DEFAULT_VALUE$INFLUENCESTEERING_0, $DEFAULT_VALUE$INFLUENCESTEERING_1, otherInfluences);",
					"  }",
					"  ",
					"  @DefaultValueUse(\"java.lang.String,float,java.lang.Object*\")",
					"  @SyntheticMember",
					"  public final void influenceSteering(final float angularInfluence, final Object... otherInfluences) {",
					"    influenceSteering($DEFAULT_VALUE$INFLUENCESTEERING_0, angularInfluence, otherInfluences);",
					"  }",
					"  ",
					"  @DefaultValueUse(\"java.lang.String,float,java.lang.Object*\")",
					"  @SyntheticMember",
					"  public final void influenceSteering(final String linearInfluence, final Object... otherInfluences) {",
					"    influenceSteering(linearInfluence, $DEFAULT_VALUE$INFLUENCESTEERING_1, otherInfluences);",
					"  }",
					"  ",
					"  /**",
					"   * Construct a skill. The owning agent is unknown.",
					"   */",
					"  @SyntheticMember",
					"  public StandardPhysicEnvironment() {",
					"    super();",
					"  }",
					"  ",
					"  /**",
					"   * Construct a skill.",
					"   * @param owner - agent that is owning this skill.",
					"   */",
					"  @SyntheticMember",
					"  public StandardPhysicEnvironment(final Agent owner) {",
					"    super(owner);",
					"  }",
					"}",
					"");

			this.compiler.compile(multilineString(capacityCode, skillCode), (r) -> {
					assertEquals(expectedPhysicEnvironment, r.getGeneratedCode("PhysicEnvironment"));
					assertEquals(expectedStandardPhysicEnvironment, r.getGeneratedCode("StandardPhysicEnvironment"));
				});
		}

	}

}
