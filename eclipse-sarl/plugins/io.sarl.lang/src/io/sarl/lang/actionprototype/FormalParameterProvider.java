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

package io.sarl.lang.actionprototype;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.xbase.XExpression;



/** An object able to provide the name and the type of a formal parameter.
*
* @author $Author: sgalland$
* @version $FullVersion$
* @mavengroupid $GroupId$
* @mavenartifactid $ArtifactId$
*/
public interface FormalParameterProvider {

	/** Replies the number of formal parameters.
	 *
	 * @return the number of formal parameters.
	 */
	int getFormalParameterCount();

	/** Replies the name of the formal parameter at the given position.
	 *
	 * @param position - the position of the formal parameter.
	 * @return the name of the formal parameter.
	 */
	String getFormalParameterName(int position);

	/** Replies the type of the formal parameter at the given position.
	 *
	 * @param position - the position of the formal parameter.
	 * @param isVarargs - indicates if the parameter should be considered as a vararg parameter.
	 * @return the type of the formal parameter.
	 */
	String getFormalParameterType(int position, boolean isVarargs);

	/** Replies the type of the formal parameter at the given position.
	 *
	 * @param position - the position of the formal parameter.
	 * @param isVarargs - indicates if the parameter should be considered as a vararg parameter.
	 * @return the type of the formal parameter.
	 */
	JvmTypeReference getFormalParameterTypeReference(int position, boolean isVarargs);

	/** Replies if the formal parameter at the given position has a default value.
	 *
	 * @param position - the position of the formal parameter.
	 * @return <code>true</code> if the parameter has a default value, <code>false</code> if not.
	 */
	boolean hasFormalParameterDefaultValue(int position);

	/** Replies the default value of the formal parameter at the given position.
	 *
	 * @param position - the position of the formal parameter.
	 * @return the default value, or <code>null</code> if none.
	 */
	XExpression getFormalParameterDefaultValue(int position);

	/** Replies the formal parameter at the given position.
	 *
	 * @param position - the position of the formal parameter.
	 * @return the formal parameter
	 */
	EObject getFormalParameter(int position);

}
