/*
 * $Id$
 *
 * File is automatically generated by the Xtext language generator.
 * Do not change it.
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
package io.sarl.lang.services;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.lib.Pure;

/** Replies the default value that is associated to a {@code JvmType}.
 */
@SuppressWarnings("all")
public interface ITypeDefaultValueProvider {

	/** Replies the default value that is associated to the given type.
	 * @param type the type to consider.
	 * @return the default value
	 */
	@Pure
	Object getDefaultValue(JvmType type);

	/** Replies the default value that is associated to the given type.
	 * @param typeName the name of the type to consider.
	 * @return the default value
	 */
	@Pure
	Object getDefaultValue(String typeName);

	/** Replies the default value in Sarl syntax and that is associated to the given type.
	 * @param type the type to consider.
	 * @return the default value in Sarl syntax
	 */
	@Pure
	String getDefaultValueInSarlSyntax(JvmType type);

	/** Replies the default value in Sarl syntax and that is associated to the given type.
	 * @param typeName the name of the type to consider.
	 * @return the default value in Sarl syntax
	 */
	@Pure
	String getDefaultValueInSarlSyntax(String typeName);

	/** Replies the XExpression for the default value that is associated to the given type.
	 * @param type the type to consider.
	 * @param notifier the context from whic a type must be loaded if needed.
	 * @return the XExpression for the default value
	 */
	@Pure
	XExpression getDefaultValueXExpression(JvmType type, Notifier notifier);

	/** Replies the XExpression for the default value that is associated to the given type.
	 * @param type the type to consider.
	 * @param notifier the context from whic a type must be loaded if needed.
	 * @return the XExpression for the default value
	 */
	@Pure
	XExpression getDefaultValueXExpression(String typeName, Notifier notifier);

}
