/*
 * $Id$
 *
 * SARL is an general-purpose agent programming language.
 * More details on http://www.sarl.io
 *
 * Copyright (C) 2014-2024 SARL.io, the Original Authors and Main Authors
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

package io.sarl.lang.validation.subvalidators;

import java.lang.ref.WeakReference;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.eclipse.xtext.validation.AbstractDeclarativeValidator;

import io.sarl.lang.validation.ISARLValidator;

/**
 * A abstract implementation of {@link AbstractDeclarativeValidator} that provides utility functions.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 0.14
 */
public abstract class AbstractSARLSubValidatorWithParentLink extends AbstractSARLSubValidator {

	@Inject
	private Provider<ISARLValidator> parentValidatorProvider;

	private WeakReference<ISARLValidator> parentValidator;

	/** Replies the parent validator if defined.
	 *
	 * @return parent validator.
	 */
	public ISARLValidator getParentValidator() {
		var ref = this.parentValidator == null ? null : this.parentValidator.get();
		if (ref == null) {
			ref = this.parentValidatorProvider.get();
			this.parentValidator = new WeakReference<>(ref);
		}
		return ref;
	}

}