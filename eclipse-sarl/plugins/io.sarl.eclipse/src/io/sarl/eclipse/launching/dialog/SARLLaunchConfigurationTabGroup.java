/*
 * $Id$
 *
 * SARL is an general-purpose agent programming language.
 * More details on http://www.sarl.io
 *
 * Copyright (C) 2014-2016 the original authors or authors.
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

package io.sarl.eclipse.launching.dialog;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.EnvironmentTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaClasspathTab;

/**
 * Tab group object for SARL Launch Config type.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SARLLaunchConfigurationTabGroup extends AbstractLaunchConfigurationTabGroup {

	/** Construct a group of tabs for being used in launch config.
	 */
	public SARLLaunchConfigurationTabGroup() {
		//
	}

	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		final ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
			new SARLMainLaunchConfigurationTab(),
			new SARLArgumentsTab(),
			new SARLRuntimeEnvironmentTab(),
			new JavaClasspathTab(),
			new EnvironmentTab(),
			new CommonTab(),
		};
		setTabs(tabs);
	}
}
