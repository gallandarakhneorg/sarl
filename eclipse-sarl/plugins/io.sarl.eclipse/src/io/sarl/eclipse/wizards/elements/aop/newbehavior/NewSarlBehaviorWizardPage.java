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

package io.sarl.eclipse.wizards.elements.aop.newbehavior;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import io.sarl.eclipse.SARLEclipseConfig;
import io.sarl.eclipse.SARLEclipsePlugin;
import io.sarl.eclipse.wizards.elements.AbstractNewSarlElementWizardPage;
import io.sarl.eclipse.wizards.elements.AbstractSuperTypeSelectionDialog;
import io.sarl.eclipse.wizards.elements.SarlSpecificTypeSelectionExtension;
import io.sarl.lang.actionprototype.ActionParameterTypes;
import io.sarl.lang.actionprototype.ActionPrototype;
import io.sarl.lang.codebuilder.builders.ISarlBehaviorBuilder;
import io.sarl.lang.codebuilder.builders.IScriptBuilder;
import io.sarl.lang.core.Behavior;

/**
 * Wizard page for creating a new SARL behavior.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class NewSarlBehaviorWizardPage extends AbstractNewSarlElementWizardPage {

	/** Construct a wizard page.
	 */
	public NewSarlBehaviorWizardPage() {
		super(CLASS_TYPE, Messages.NewSarlBehavior_0);
		setTitle(Messages.NewSarlBehavior_0);
		setDescription(Messages.NewSarlBehaviorWizardPage_0);
		setImageDescriptor(SARLEclipsePlugin.getDefault().getImageDescriptor(SARLEclipseConfig.NEW_BEHAVIOR_WIZARD_DIALOG_IMAGE));
	}

	@Override
	public void createPageControls(Composite parent) {
		createSuperClassControls(parent, COLUMNS);
		createSeparator(parent, COLUMNS);
		createMethodStubControls(parent, COLUMNS, true, true);
	}

	@Override
	protected void doStatusUpdate() {
		final IStatus[] status = new IStatus[] {
			this.fContainerStatus,
			this.fPackageStatus,
			this.fTypeNameStatus,
			this.fSuperClassStatus,
		};
		updateStatus(status);
	}

	@Override
	protected void getTypeContent(Resource ecoreResource, String typeComment) throws CoreException {
		final IScriptBuilder scriptBuilder = this.codeBuilderFactory.createScript(
				getPackageFragment().getElementName(), ecoreResource);
		final ISarlBehaviorBuilder behavior = scriptBuilder.addSarlBehavior(getTypeName());
		behavior.setExtends(getSuperClass());
		behavior.setDocumentation(typeComment);

		final Map<ActionPrototype, IMethod> operationsToImplement;
		final Map<ActionParameterTypes, IMethod> constructors;

		final String superClass = getSuperClass();
		if (Strings.isNullOrEmpty(superClass) || !isCreateConstructors()) {
			constructors = null;
		} else {
			constructors = Maps.newTreeMap((Comparator<ActionParameterTypes>) null);
		}

		if (isCreateInherited()) {
			operationsToImplement = Maps.newTreeMap((Comparator<ActionPrototype>) null);
		} else {
			operationsToImplement = null;
		}

		this.jdt2sarl.populateInheritanceContext(
				this.jdt2sarl.toTypeFinder(getJavaProject()),
				// Discarding final operation.
				null,
				// Discarding overridable operation.
				null,
				// Discarding inherited fields,
				null,
				operationsToImplement,
				constructors,
				getSuperClass(),
				Collections.<String>emptyList());

		if (constructors != null) {
			this.jdt2sarl.createStandardConstructors(behavior, constructors.values(), behavior.getSarlBehavior());
		}

		if (operationsToImplement != null) {
			this.jdt2sarl.createActions(behavior, operationsToImplement.values());
		}

		scriptBuilder.finalizeScript();
	}

	@Override
	protected String getExistingElementErrorMessage() {
		return Messages.NewSarlBehaviorWizardPage_1;
	}

	@Override
	protected String getInvalidSubtypeErrorMessage() {
		return Messages.NewSarlBehaviorWizardPage_2;
	}

	@Override
	protected IType getRootSuperType() throws JavaModelException {
		return getJavaProject().findType(Behavior.class.getName());
	}

	@Override
	protected AbstractSuperTypeSelectionDialog<?> createSuperClassSelectionDialog(Shell parent,
			IRunnableContext context, IJavaProject project, SarlSpecificTypeSelectionExtension extension) {
		return new SuperBehaviorSelectionDialog(parent, context, project, this, extension);
	}

}
