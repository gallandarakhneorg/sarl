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

package io.sarl.eclipse.wizards.elements.aop.newcapacity;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.swt.widgets.Shell;

import io.sarl.eclipse.wizards.elements.AbstractSuperTypeSelectionDialog;
import io.sarl.eclipse.wizards.elements.SarlSpecificTypeSelectionExtension;
import io.sarl.lang.core.Capacity;

/** Dialog box for selecting a SARL capacity type.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SuperCapacitySelectionDialog extends AbstractSuperTypeSelectionDialog<NewSarlCapacityWizardPage> {

	/**
	 * Creates new instance.
	 *
	 * @param parent shell to parent the dialog on.
	 * @param context context used to execute long-running operations associated with this dialog.
	 * @param project the java project which will be considered when searching for interfaces.
	 * @param wizardPage the wizard page.
	 * @param extension the wizard type selection extension.
	 */
	public SuperCapacitySelectionDialog(Shell parent, IRunnableContext context, IJavaProject project,
			NewSarlCapacityWizardPage wizardPage, SarlSpecificTypeSelectionExtension extension) {
		super(parent, context, wizardPage,
				createSearchScope(project, Capacity.class, true),
				IJavaSearchConstants.INTERFACE, extension);
	}

	@Override
	protected List<String> saveWizardPage(NewSarlCapacityWizardPage wizardPage) {
		return Collections.unmodifiableList(wizardPage.getSuperInterfaces());
	}

	@Override
	protected void restoreWizardPage(NewSarlCapacityWizardPage wizardPage, List<String> savedContent) {
		wizardPage.setSuperInterfaces(savedContent, true);
	}

	@Override
	protected boolean addTypeToWizardPage(NewSarlCapacityWizardPage wizardPage, String qualifiedName) {
		return wizardPage.addSuperInterface(qualifiedName);
	}

	@Override
	protected int getSuperTypeCount(NewSarlCapacityWizardPage wizardPage) {
		return wizardPage.getSuperInterfaces().size();
	}

}
