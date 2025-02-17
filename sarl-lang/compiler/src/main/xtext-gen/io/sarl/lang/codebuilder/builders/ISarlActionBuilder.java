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
package io.sarl.lang.codebuilder.builders;

import io.sarl.lang.sarl.SarlAction;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.access.IJvmTypeProvider;
import org.eclipse.xtext.xbase.lib.Pure;

/** Builder of a Sarl SarlAction.
	 * @see AbstractMemberBuilderFragment.java : appendTo : 117
 */
@SuppressWarnings("all")
public interface ISarlActionBuilder {

	/** Find the reference to the type with the given name.
	 * @param typeName the fully qualified name of the type
	 * @return the type reference.
	 * @see AbstractMemberBuilderFragment.java : appendTo : 406
	 */
	JvmTypeReference newTypeRef(String typeName);

	/** Find the reference to the type with the given name.
	 * @param context the context for the type reference use
	 * @param typeName the fully qualified name of the type
	 * @return the type reference.
	 * @see AbstractMemberBuilderFragment.java : appendTo : 434
	 */
	JvmTypeReference newTypeRef(Notifier context, String typeName);

	/** Find the reference to the type and type parameters.
	 * @param type the type to reference
	 * @param args the type arguments to put in the reference to the given type
	 * @return the type reference.
	 * @see AbstractMemberBuilderFragment.java : appendTo : 464
	 */
	JvmTypeReference newTypeRef(JvmType type, JvmTypeReference... args);

	/** Find the reference to the type and type parameters.
	 * @param type the type to reference
	 * @param args the type arguments to put in the reference to the given type
	 * @return the type reference.
	 * @see AbstractMemberBuilderFragment.java : appendTo : 496
	 */
	JvmTypeReference newTypeRef(Class type, JvmTypeReference... args);

	/** Find the reference to the type and type parameters.
	 * @param context the context in which the type is defined
	 * @param type the type to reference
	 * @param args the type arguments to put in the reference to the given type
	 * @return the type reference.
	 * @see AbstractMemberBuilderFragment.java : appendTo : 530
	 */
	JvmTypeReference newTypeRef(Notifier context, Class type, JvmTypeReference... args);

	/** Dispose the resource.
	 * @see AbstractMemberBuilderFragment.java : appendTo : 560
	 */
	void dispose();

	/** Replies the context for type resolution.
	 * @return the context or {@code null} if the Ecore object is the context.
	 * @see AbstractMemberBuilderFragment.java : appendTo : 585
	 */
	IJvmTypeProvider getTypeResolutionContext();

	/** Initialize the Ecore element.
	 * @param container the container of the SarlAction.
	 * @param name the name of the SarlAction.
	 * @param modifier the major/default modifier to be associated to the member.
	 * @param context the context in which type resolution must be applied.
	 * @see AbstractMemberBuilderFragment.java : appendTo : 632
	 */
	void eInit(EObject container, String name, String modifier, IJvmTypeProvider context);

	/** Replies the generated element.
	 * @see AbstractMemberBuilderFragment.java : appendTo : 962
	 */
	@Pure
	SarlAction getSarlAction();

	/** Replies the resource.
	 * @see AbstractMemberBuilderFragment.java : appendTo : 996
	 */
	@Pure
	Resource eResource();

	/** Change the documentation of the element.
	 *
	 * <p>The documentation will be displayed just before the element.
	 *
	 * @param doc the documentation.
	 * @return {@code this}.
	 * @see AbstractSubCodeBuilderFragment.java : appendTo : 602
	 */
	ISarlActionBuilder setDocumentation(String doc);

	/** Add a formal parameter.
	 * @param name the name of the formal parameter.
	 * @see AbstractMemberBuilderFragment.java : appendTo : 1114
	 */
	IFormalParameterBuilder addParameter(String name);

	/** Add a throwable exception.
	 * @param type the fully qualified name of the exception.
	 * @return {@code this}
	 * @see AbstractMemberBuilderFragment.java : appendTo : 1158
	 */
	ISarlActionBuilder addException(String type);

	/** Add a throwable exception.
	 * @param type the exception.
	 * @return {@code this}
	 * @see AbstractMemberBuilderFragment.java : appendTo : 1201
	 */
	ISarlActionBuilder addException(JvmTypeReference type);

	/** Add a fired event.
	 * @param type the fully qualified name of the event.
	 * @return {@code this}
	 * @see AbstractMemberBuilderFragment.java : appendTo : 1246
	 */
	ISarlActionBuilder addFiredEvent(String type);

	/** Add a fired event.
	 * @param type the event.
	 * @see AbstractMemberBuilderFragment.java : appendTo : 1287
	 */
	void addFiredEvent(JvmTypeReference type);

	/** Change the return type.
	 @param type the return type of the member.
	 * @return {@code this}
	 * @see AbstractMemberBuilderFragment.java : appendTo : 1329
	 */
	ISarlActionBuilder setReturnType(String type);

	/** Change the return type.
	 @param type the return type of the member.
	 * @return {@code this}
	 * @see AbstractMemberBuilderFragment.java : appendTo : 1385
	 */
	ISarlActionBuilder setReturnType(JvmTypeReference type);

	/** Create the block of code.
	 * @return the block builder.
	 * @see AbstractMemberBuilderFragment.java : appendTo : 1445
	 */
	IBlockExpressionBuilder getExpression();

	/** Add an annotation.
	 * @param type the qualified name of the annotation
	 * @return {@code this}
	 * @see AbstractMemberBuilderFragment.java : appendTo : 1508
	 */
	ISarlActionBuilder addAnnotation(String type);

	/** Add an annotation.
	 * @param type the annotation type
	 * @return {@code this}
	 * @see AbstractMemberBuilderFragment.java : appendTo : 1552
	 */
	ISarlActionBuilder addAnnotation(JvmTypeReference type);

	/** Add a modifier.
	 * @param modifier the modifier to add.
	 * @return {@code this}
	 * @see AbstractMemberBuilderFragment.java : appendTo : 1602
	 */
	ISarlActionBuilder addModifier(String modifier);

	/** Add a type parameter.
	 * @param name the simple name of the type parameter.
	 * @return the builder of type parameter.
	 * @see AbstractMemberBuilderFragment.java : appendTo : 1683
	 */
	ITypeParameterBuilder addTypeParameter(String name);

}

