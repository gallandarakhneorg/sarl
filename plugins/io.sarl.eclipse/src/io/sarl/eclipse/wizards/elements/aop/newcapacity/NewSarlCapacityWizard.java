/*
 * $Id$
 *
 * SARL is an general-purpose agent programming language.
 * More details on http://www.sarl.io
 *
 * Copyright (C) 2014-2015 Sebastian RODRIGUEZ, Nicolas GAUD, Stéphane GALLAND.
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

import io.sarl.eclipse.wizards.elements.AbstractNewSarlElementWizard;

import javax.inject.Inject;

import org.eclipse.xtext.ui.IImageHelper.IImageDescriptorHelper;

/**
 * Wizard for creating a new SARL capacity.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class NewSarlCapacityWizard extends AbstractNewSarlElementWizard {

	/**
	 * @param imgHelper - the helper for getting images.
	 * @param page - the page of the wizard.
	 */
	@Inject
	public NewSarlCapacityWizard(IImageDescriptorHelper imgHelper, NewSarlCapacityWizardPage page) {
		super(imgHelper, page, Messages.NewSarlCapacity_0);
	}

}