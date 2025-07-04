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

package io.sarl.docs.generator.tests.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import com.google.common.io.Resources;
import com.google.inject.Injector;
import org.arakhne.afc.vmutil.FileSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import io.sarl.docs.generator.parser.SarlDocumentationParser;
import io.sarl.docs.generator.parser.SarlDocumentationParser.ParsingException;
import io.sarl.docs.generator.parser.SarlDocumentationParser.Tag;
import io.sarl.docs.generator.parser.ValidationComponentData;
import io.sarl.lang.SARLStandaloneSetup;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 0.6
 */
@SuppressWarnings("all")
@DisplayName("SarlDocumentationParser")
@org.junit.jupiter.api.Tag("maven")
@org.junit.jupiter.api.Tag("unit")
public class SarlDocumentationParserTest {

	private static File file(String basename) {
		URL url = Resources.getResource(SarlDocumentationParserTest.class, basename);
		assumeTrue(url != null);
		File file = FileSystem.convertURLToFile(url);
		assumeTrue(file != null);
		return file;
	}

	@Nested
	@DisplayName("Generation of documentation")
	public class TransformTest {

		private SarlDocumentationParser parser;

		@BeforeEach
		public void setUp() {
			Injector injector = SARLStandaloneSetup.doSetup();
			this.parser = injector.getInstance(SarlDocumentationParser.class);
			this.parser.setBlockCodeTemplate(SarlDocumentationParser.getBasicCodeBlockFormatter());
		}

		@Test
		@DisplayName("Missing file")
		public void noFile() throws Exception {
			try {
				File file = new File("nofile.txt");
				this.parser.transform(file);
				fail("expecting exception " + ParsingException.class);
			} catch (ParsingException ex) {
				//
			}
		}
	
		@Test
		@DisplayName("Include")
		public void includer01() throws Exception {
			File file = file("includer.txt");
			String value = this.parser.transform(file);
			assertEquals(
					"this is a fake text done for testing. this is a fake text done for testing. this is a fake text done for testing.\n"
					+ "\nthis is a fake text done for testing. this is a fake text done for testing. this is a fake text done for "
					+ "testing. this is a fake text done for testing.\n\nthis is a fake text done for testing. "
					+ "I'm included.this is a fake text done for testing. this is a fake text done for testing. this is a "
					+ "fake text done for testing. this is a fake text done\nfor testing. this is a fake text done for "
					+ "testing. this is a fake text done for testing. this is a fake text done\nfor testing. this is a "
					+ "fake text done for testing. this is a fake text done for testing.",
					value);
		}
	
		@Test
		@DisplayName("Variable with predefinition")
		public void saver01() throws Exception {
			File file = file("saverpredefinition.txt");
			String value = this.parser.transform(file);
			assertEquals("this is a fake text done for testing. this is a fake text done for testing. this is a fake text done "
					+ "for testing.\n\nthis is a fake text done for testing. this is a fake text done for testing. "
					+ "this is a fake text done for testing. this is a fake text done for testing.\n\nthis is a fake text done "
					+ "for testing. `this is`this is a fake text done for testing. this is a fake text done for testing. "
					+ "this is a fake text done for testing. this is a fake text done \nfor testing. this is a fake text done "
					+ "for testing. this is a fake text done for testing. this is a fake text done\nfor testing. this is a "
					+ "fake text done for testing. this is a fake text done for testing.",
					value);
		}
	
		@Test
		@DisplayName("Variable with postdefinition")
		public void saver02() throws Exception {
			File file = file("saverpostdefinition.txt");
			String value = this.parser.transform(file);
			assertEquals("this is a fake text done for testing. this is a fake text done for testing. this is a fake text "
					+ "done for testing.\n\nthis is a fake text done for testing. `this is a fake text`this is a fake text done for "
					+ "testing. this is a fake text done for testing. this is a fake text done for testing.\n\nthis "
					+ "is a fake text done for testing. this is a fake text done for testing. this is a "
					+ "fake text done for testing. this is a fake text done for testing. this is a fake text done \nfor "
					+ "testing. this is a fake text done for testing. this is a fake text done for testing. this is a "
					+ "fake text done\nfor testing. this is a fake text done for testing. this is a fake text done "
					+ "for testing.",
					value);
		}
	
		@Test
		@DisplayName("Failing variable")
		public void saver03() throws Exception {
			try {
				File file = file("savernodefinition.txt");
				this.parser.transform(file);
				fail("expecting exception " + ParsingException.class);
			} catch (ParsingException ex) {
				//
			}
		}
	
		@Test
		@DisplayName("Success")
		public void success01() throws Exception {
			File file = file("success.txt");
			String value = this.parser.transform(file);
			assertEquals("this is a fake text done for testing. this is a fake text done for testing. this is a fake "
					+ "text done for testing.\n\nthis is a fake text done for testing. this is a fake text done for "
					+ "testing. this is a fake text done for testing. this is a fake text done for testing.\n\n"
					+ "\n\nthis is a fake text done for testing. this is a fake text done for testing. this "
					+ "is a fake text done for testing. this is a fake text done for testing. this is a "
					+ "fake text done \nfor testing. this is a fake text done for testing. this is a fake text "
					+ "done for testing. this is a fake text done\nfor testing. this is a fake text done for "
					+ "testing. this is a fake text done for testing.",
					value);
		}
	
		@Test
		@DisplayName("Failure")
		public void failure01() throws Exception {
			File file = file("failure.txt");
			String value = this.parser.transform(file);
			assertEquals("this is a fake text done for testing. this is a fake text done for testing. this is a fake "
					+ "text done for testing.\n\nthis is a fake text done for testing. this is a fake text done for "
					+ "testing. this is a fake text done for testing. this is a fake text done for testing.\n\n"
					+ "\n\nthis is a fake text done for testing. this is a fake text done for testing. this "
					+ "is a fake text done for testing. this is a fake text done for testing. this is a "
					+ "fake text done \nfor testing. this is a fake text done for testing. this is a fake text "
					+ "done for testing. this is a fake text done\nfor testing. this is a fake text done for "
					+ "testing. this is a fake text done for testing.",
					value);
		}
	
		@Test
		@DisplayName("Outline")
		public void outline01() throws Exception {
			File file = file("outline.txt");
			String value = this.parser.transform(file);
			assertEquals("this is a fake text done for testing. this is a fake text done for testing. this is a fake "
					+ "text done for testing.\n\nthis is a fake text done for testing. this is a fake text done for "
					+ "testing. this is a fake text done for testing. this is a fake text done for testing.\n\n[::Outline::]"
					+ "\n\nthis is a fake text done for testing. this is a fake text done for testing. this "
					+ "is a fake text done for testing. this is a fake text done for testing. this is a "
					+ "fake text done \nfor testing. this is a fake text done for testing. this is a fake text "
					+ "done for testing. this is a fake text done\nfor testing. this is a fake text done for "
					+ "testing. this is a fake text done for testing.",
					value);
		}
	
		@Test
		@DisplayName("Parameter delimiters")
		public void parameterDelimiters01() throws Exception {
			File file = file("parameterdelimiters.txt");
			String value = this.parser.transform(file);
			assertEquals("this is a fake text done for testing. this is a fake text done for testing. this is a fake text "
					+ "done for testing.\n\nthis is a fake text done for testing. this is a fake text done for "
					+ "testing. this is a fake text\ndone for testing. this is a fake text "
					+ "done for testing.\n\n`this`\n`testing`\n`is`\n`done`",
					value);
		}
	
		@Test
		@DisplayName("[:var]() in variable")
		public void saveInSave01() throws Exception {
			File file = file("saveinsave.txt");
			String value = this.parser.transform(file);
			assertEquals("this is a fake text done for testing. this is a fake text done for testing. this is a fake "
					+ "text done for testing.\n\nthis is a fake text done for testing. this is a fake text done for "
					+ "testing. this is a fake text done for testing. this is a fake text done for testing.\n\n"
					+ "`this is a fake text`\n`fake`",
					value);
		}
	
		@Test
		@DisplayName("[:var]() in Success")
		public void saveInSuccess01() throws Exception {
			File file = file("saveinsuccess.txt");
			String value = this.parser.transform(file);
			assertEquals("this is a fake text done for testing. this is a fake text done for testing. this is a fake "
					+ "text done for testing.\n\n\n\nthis is a fake text done for testing. this is a fake text done for "
					+ "testing. this is a fake text done for testing. this is a fake text done for testing.\n\n"
					+ "`hidden`",
					value);
		}
	
		@Test
		@DisplayName("Fact")
		public void fact01() throws Exception {
			File file = file("fact.txt");
			String value = this.parser.transform(file);
			assertEquals("this is a fake text done for testing. this is a fake text done for testing. this is a fake "
					+ "text done for testing.\n\nthis is a fake text done for testing. this is a fake text done for "
					+ "testing. this is a fake text done for testing. this is a fake text done for testing.\n\n\n\nthis "
					+ "is a fake text done for testing. this is a fake text done for testing. this is a fake text done "
					+ "for testing. this is a fake text done for testing. this is a fake text done \nfor testing. this "
					+ "is a fake text done for testing. this is a fake text done for testing. this is a fake text "
					+ "done\nfor testing. this is a fake text done for testing. this is a fake text done for testing.",
					value);
		}

		@Test
		@DisplayName("On/Off in Success")
		public void onOff01() throws Exception {
			File file = file("onOffInSuccess.txt");
			String value = this.parser.transform(file);
			assertEquals("this is a fake text done for testing. this is a fake text done for testing. this is a fake "
					+ "text done for testing.\n\nthis is a fake text done for testing. this is a fake text done for "
					+ "testing. this is a fake text done for testing. this is a fake text done for testing.\n\n"
					+ "\tof code\n\n\nthis is a fake text done for testing. this is a fake text done for "
					+ "testing. this is a fake text done for testing. this is a fake text done for testing. this is "
					+ "a fake text done \nfor testing. this is a fake text done for testing. this is a fake text "
					+ "done for testing. this is a fake text done\nfor testing. this is a fake text done for testing. "
					+ "this is a fake text done for testing.",
					value);
		}

		@Test
		@DisplayName("On/Off in Failure")
		public void onOff02() throws Exception {
			File file = file("onOffInFailure.txt");
			String value = this.parser.transform(file);
			assertEquals("this is a fake text done for testing. this is a fake text done for testing. this is a fake "
					+ "text done for testing.\n\nthis is a fake text done for testing. this is a fake text done for "
					+ "testing. this is a fake text done for testing. this is a fake text done for testing.\n\n"
					+ "\tof code\n\n\nthis is a fake text done for testing. this is a fake text done for "
					+ "testing. this is a fake text done for testing. this is a fake text done for testing. this is "
					+ "a fake text done \nfor testing. this is a fake text done for testing. this is a fake text "
					+ "done for testing. this is a fake text done\nfor testing. this is a fake text done for testing. "
					+ "this is a fake text done for testing.",
					value);
		}

		@Test
		@DisplayName("HTML <a>")
		public void htmlHyperref00() throws Exception {
			final var props = new Properties();
			props.setProperty("some.url", "https://somewhere.com");
			this.parser.addHighPropertyProvider(props);
			
			File file = file("htmlhyperref.txt");
			String value = this.parser.transform(file);
			assertEquals("<a href=\"https://somewhere.com/path/to/page\">XXX</a>",
					value);
		}

		@Test
		@DisplayName("HTML <a> with variable")
		public void htmlHyperref01() throws Exception {
			final var props = new Properties();
			props.setProperty("some.url", "https://somewhere.com");
			this.parser.addHighPropertyProvider(props);
			
			File file = file("htmlhyperref2.txt");
			String value = this.parser.transform(file);
			assertEquals("<a href=\"https://somewhere.com/path/to/page\">XXX</a>\n"
				+ "[:some.url!]/path/to/page",
				value);
		}

	}

	@Nested
	@DisplayName("Validation of documentation")
	public class ValidationTest {

		private SarlDocumentationParser parser;

		@BeforeEach
		public void setUp() {
			Injector injector = SARLStandaloneSetup.doSetup();
			this.parser = injector.getInstance(SarlDocumentationParser.class);
			this.parser.setBlockCodeTemplate(SarlDocumentationParser.getBasicCodeBlockFormatter());
		}

		@Test
		@DisplayName("Success")
		public void success01() {
			File file = file("success2.txt");
			this.parser.extractValidationComponents(file, (components) -> {
				assertEquals(1, components.size());
				Tag key = components.keySet().iterator().next();
				assertEquals(Tag.SUCCESS, key);
				List<ValidationComponentData> values = components.get(key);
				assertNotNull(values);
				assertEquals(1, values.size());
				assertEquals("package io.sarl.docs.tests\n"
						+ "		import io.sarl.api.core.Initialize\n"
						+ "		import io.sarl.api.core.Logging\n"
						+ "		agent MyAgent {\n"
						+ "			uses Logging\n"
						+ "			on Initialize {\n"
						+ "				info(\"Hello\")\n"
						+ "			}\n"
						+ "		}",
						values.get(0).code);
			});
		}

		@Test
		@DisplayName("Failure")
		public void failure01() {
			File file = file("failure2.txt");
			this.parser.extractValidationComponents(file, (components) -> {
				assertEquals(1, components.size());
				Tag key = components.keySet().iterator().next();
				assertEquals(Tag.FAILURE, key);
				List<ValidationComponentData> values = components.get(key);
				assertNotNull(values);
				assertEquals(1, values.size());
				assertEquals("package io.sarl.docs.tests\n"
						+ "		agent MyAgent {\n"
						+ "			uses Logging\n"
						+ "			on Initialize {\n"
						+ "				info(\"Hello\")\n"
						+ "			}\n"
						+ "		}",
						values.get(0).code);
			});
		}

		@Test
		@DisplayName("Fact")
		public void fact01() {
			File file = file("fact.txt");
			this.parser.extractValidationComponents(file, (components) -> {
				assertEquals(1, components.size());
				Tag key = components.keySet().iterator().next();
				assertEquals(Tag.FACT, key);
				List<ValidationComponentData> values = components.get(key);
				assertNotNull(values);
				assertEquals(1, values.size());
				assertEquals("typeof(Integer)",
						values.get(0).code);
			});
		}

		@Test
		@DisplayName("Success and Failure")
		public void successFailureFact01() {
			File file = file("multipleblocks.txt");
			this.parser.extractValidationComponents(file, (components) -> {
				assertEquals(3, components.size());
				List<ValidationComponentData> values = components.get(Tag.FACT);
				assertNotNull(values);
				assertEquals(1, values.size());
				assertEquals("true",values.get(0).code);

				values = components.get(Tag.FAILURE);
				assertNotNull(values);
				assertEquals(1, values.size());
				assertEquals("package io.sarl.docs.tests\n"
						+ "		agent MyAgent {\n"
						+ "			uses Logging\n"
						+ "			on Initialize {\n"
						+ "				info(\"Hello\")\n"
						+ "			}\n"
						+ "		}",
						values.get(0).code);


				values = components.get(Tag.SUCCESS);
				assertNotNull(values);
				assertEquals(2, values.size());
				assertEquals("package io.sarl.docs.tests\n"
						+ "		import io.sarl.api.core.Initialize\n"
						+ "		import io.sarl.api.core.Logging\n"
						+ "		agent MyAgent {\n"
						+ "			uses Logging\n"
						+ "			on Initialize {\n"
						+ "				info(\"Hello\")\n"
						+ "			}\n"
						+ "		}",
						values.get(0).code);
				assertEquals("agent MyAgent {}",
						values.get(1).code);
			});
		}

	}

}
