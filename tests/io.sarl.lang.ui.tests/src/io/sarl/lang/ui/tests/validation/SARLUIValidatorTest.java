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
package io.sarl.lang.ui.tests.validation;

import io.sarl.lang.sarl.SarlPackage;
import io.sarl.lang.sarl.SarlScript;
import io.sarl.tests.api.AbstractSarlUiTest;

import org.junit.Test;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SARLUIValidatorTest extends AbstractSarlUiTest {

	/**
	 * @throws Exception
	 */
	@Test
	public void checkFileNamingConventions_validPackageName() throws Exception {
		SarlScript script = helper().sarlScript(
				helper().generateFilename(),
				"package " + helper().getDefaultTestPackage()); //$NON-NLS-1$
		validate(script).assertNoIssues();
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void checkFileNamingConventions_wrongPackageName() throws Exception {
		SarlScript script = helper().sarlScript(
				helper().generateFilename(),
				"package fake." + helper().getDefaultTestPackage()); //$NON-NLS-1$
		validate(script).assertWarning(
				SarlPackage.eINSTANCE.getSarlScript(),
				org.eclipse.xtend.core.validation.IssueCodes.WRONG_PACKAGE,
				"Expecting package definition io.sarl.tests"); //$NON-NLS-1$
	}

}
