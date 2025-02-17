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

package io.sarl.eclipse.pythongenerator.configuration;

import com.google.inject.Singleton;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.xtext.ui.editor.preferences.IPreferenceStoreAccess;
import org.eclipse.xtext.ui.editor.preferences.IPreferenceStoreInitializer;

import io.sarl.eclipse.pythongenerator.PyGeneratorUiPlugin;
import io.sarl.lang.pythongenerator.PyGeneratorPlugin;
import io.sarl.lang.pythongenerator.generator.PyInitializers;
import io.sarl.lang.ui.extralanguage.preferences.ExtraLanguagePreferenceAccess;


/** Initializer for the generator's preferences.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 0.6
 */
@Singleton
public class PyPreferenceInitializer implements IPreferenceStoreInitializer {

	@Override
	public void initialize(IPreferenceStoreAccess access) {
		final var store = access.getWritablePreferenceStore();
		initializeGeneralOptions(store);
		initializeTypeConversion(store);
		initializeFeatureNameConversion(store);
	}

	private static void initializeTypeConversion(IPreferenceStore store) {
		final var tcInitializer = PyInitializers.getTypeConverterInitializer(PyGeneratorUiPlugin.getDefault());
		final var preferenceValue = ExtraLanguagePreferenceAccess.toConverterPreferenceValue(tcInitializer);
		final var key = ExtraLanguagePreferenceAccess.getPrefixedKey(PyGeneratorPlugin.PREFERENCE_ID,
				ExtraLanguagePreferenceAccess.TYPE_CONVERSION_PROPERTY);
		store.setDefault(key, preferenceValue);
		store.setToDefault(key);
	}

	private static void initializeFeatureNameConversion(IPreferenceStore store) {
		final var fnInitializer = PyInitializers.getFeatureNameConverterInitializer(PyGeneratorUiPlugin.getDefault());
		final var preferenceValue = ExtraLanguagePreferenceAccess.toConverterPreferenceValue(fnInitializer);
		final var key = ExtraLanguagePreferenceAccess.getPrefixedKey(PyGeneratorPlugin.PREFERENCE_ID,
				ExtraLanguagePreferenceAccess.FEATURE_NAME_CONVERSION_PROPERTY);
		store.setDefault(key, preferenceValue);
		store.setToDefault(key);
	}

	private static void initializeGeneralOptions(IPreferenceStore store) {
		String key;

		key = ExtraLanguagePreferenceAccess.getPrefixedKey(PyGeneratorPlugin.PREFERENCE_ID,
				ExtraLanguagePreferenceAccess.ENABLED_PROPERTY);
		store.setDefault(key, false);
		store.setToDefault(key);

		key = ExtraLanguagePreferenceAccess.getPrefixedKey(PyGeneratorPlugin.PREFERENCE_ID,
				PyPreferenceAccess.JYTHON_COMPLIANCE_PROPERTY);
		store.setDefault(key, true);
		store.setToDefault(key);
	}

}
