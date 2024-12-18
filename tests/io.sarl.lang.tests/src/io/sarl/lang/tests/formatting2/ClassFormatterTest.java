/*
 * Copyright (C) 2014-2016 the original authors or authors.
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
package io.sarl.lang.tests.formatting2;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import com.google.inject.Inject;
import junit.framework.TestSuite;
import org.eclipse.xtext.junit4.formatter.FormatterTestRequest;
import org.eclipse.xtext.junit4.formatter.FormatterTester;
import org.eclipse.xtext.util.Strings;
import org.eclipse.xtext.xbase.lib.Procedures;
import org.junit.Test;
import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.junit.runners.model.InitializationError;

import io.sarl.tests.api.AbstractSarlTest;

/** Tests for formatting classes.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@RunWith(Suite.class)
@SuiteClasses({
	ClassFormatterTest.FormatterAPITest.class,
})
@SuppressWarnings("all")
public class ClassFormatterTest {

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class FormatterAPITest extends AbstractFormatterTest {
	
	@Test
	public void empty() throws Exception {
		String source = "class  EntityX{}";
		String expected = multilineString(
				"class EntityX {",
				"}",
				"");
		assertFormatted(source, expected);
	}

	@Test
	public void modifiers() throws Exception {
		String source = "public    static    class EntityX{}";
		String expected = multilineString(
				"public static class EntityX {",
				"}",
				"");
		assertFormatted(source, expected);
	}

	@Test
	public void twoAnnotations() throws Exception {
		String source = "@Pure@Beta    class EntityX{}";
		String expected = multilineString(
				"@Pure @Beta class EntityX {",
				"}",
				"");
		assertFormatted(source, expected);
	}

	@Test
	public void threeAnnotations() throws Exception {
		String source = "@Pure@Beta\n@Hello    class EntityX{}";
		String expected = multilineString(
				"@Pure @Beta",
				"@Hello class EntityX {",
				"}",
				"");
		assertFormatted(source, expected);
	}

	@Test
	public void annotationValue() throws Exception {
		String source = "@SuppressWarnings(        value= \"name\"   )class EntityX{}";
		String expected = multilineString(
				"@SuppressWarnings(value = \"name\") class EntityX {",
				"}",
				"");
		assertFormatted(source, expected);
	}

	@Test
	public void annotationImplicitValue() throws Exception {
		String source = "@SuppressWarnings(   \"name\"   )class EntityX{}";
		String expected = multilineString(
				"@SuppressWarnings(\"name\") class EntityX {",
				"}",
				"");
		assertFormatted(source, expected);
	}

	@Test
	public void typeParameter() throws Exception {
		String source = "class EntityX    <  T   > {}";
		String expected = multilineString(
				"class EntityX<T> {",
				"}",
				"");
		assertFormatted(source, expected);
	}

	@Test
	public void typeParameters() throws Exception {
		String source = "class EntityX    <  T , TT   extends T  > {}";
		String expected = multilineString(
				"class EntityX<T, TT extends T> {",
				"}",
				"");
		assertFormatted(source, expected);
	}
	
	@Test
	public void extend() throws Exception {
		String source = "class EntityX    extends    EntityY {}";
		String expected = multilineString(
				"class EntityX extends EntityY {",
				"}",
				"");
		assertFormatted(source, expected);
	}

	@Test
	public void extendGeneric() throws Exception {
		String source = "class EntityX    extends    EntityY <   Boolean > {}";
		String expected = multilineString(
				"class EntityX extends EntityY<Boolean> {",
				"}",
				"");
		assertFormatted(source, expected);
	}

	@Test
	public void genericExtendGeneric() throws Exception {
		String source = "class EntityX  < T   >    extends    EntityY <   T > {}";
		String expected = multilineString(
				"class EntityX<T> extends EntityY<T> {",
				"}",
				"");
		assertFormatted(source, expected);
	}

	@Test
	public void oneImplement() throws Exception {
		String source = "class EntityX    implements    EntityY {}";
		String expected = multilineString(
				"class EntityX implements EntityY {",
				"}",
				"");
		assertFormatted(source, expected);
	}

	@Test
	public void twoImplements() throws Exception {
		String source = "class EntityX    implements    EntityY  ,    EntityZ {}";
		String expected = multilineString(
				"class EntityX implements EntityY, EntityZ {",
				"}",
				"");
		assertFormatted(source, expected);
	}

	@Test
	public void oneImplementGeneric() throws Exception {
		String source = "class EntityX    implements    EntityY <   Boolean  > {}";
		String expected = multilineString(
				"class EntityX implements EntityY<Boolean> {",
				"}",
				"");
		assertFormatted(source, expected);
	}

	@Test
	public void twoImplementsGeneric() throws Exception {
		String source = "class EntityX    implements    EntityY  < Boolean  > ,    EntityZ {}";
		String expected = multilineString(
				"class EntityX implements EntityY<Boolean>, EntityZ {",
				"}",
				"");
		assertFormatted(source, expected);
	}

	@Test
	public void memberIndent() throws Exception {
		String source = "class EntityX{var x:int}";
		String expected = multilineString(
				"class EntityX {",
				"	var x : int",
				"}",
				"");
		assertFormatted(source, expected);
	}

	@Test
	public void mlStandardComment1() throws Exception {
		String source = "/*Hello world.\n* That's the second line.\n*/class A{}";
		String expected = multilineString(
				"/* Hello world.",
				" * That's the second line.",
				" */",
				"class A {",
				"}",
				"");
		assertFormatted(source, expected);
	}

	@Test
	public void mlStandardComment2() throws Exception {
		String source = "/*Hello world.\nThat's the second line.*/class A{}";
		String expected = multilineString(
				"/* Hello world.",
				" * That's the second line.",
				" */",
				"class A {",
				"}",
				"");
		assertFormatted(source, expected);
	}

	@Test
	public void mlStandardComment3() throws Exception {
		String source = "/*     Hello world.     */class A{}";
		String expected = multilineString(
				"/* Hello world.",
				" */",
				"class A {",
				"}",
				"");
		assertFormatted(source, expected);
	}

	@Test
	public void mlStandardComment4() throws Exception {
		String source = "/*     Hello world.     */class A{/*Second comment*/}";
		String expected = multilineString(
				"/* Hello world.",
				" */",
				"class A {",
				"\t/* Second comment",
				"\t */",
				"}",
				"");
		assertFormatted(source, expected);
	}

	@Test
	public void mlJavaComment() throws Exception {
		String source = "/**Hello world.\nThat's the second line.*/class A{}";
		String expected = multilineString(
				"/** Hello world.",
				" * That's the second line.",
				" */",
				"class A {",
				"}",
				"");
		assertFormatted(source, expected);
	}

	@Test
	public void slComment() throws Exception {
		String source = "\n//Hello world.\nclass A{}";
		String expected = multilineString(
				"// Hello world.",
				"class A {",
				"}",
				"");
		assertFormatted(source, expected);
	}

}
	
}