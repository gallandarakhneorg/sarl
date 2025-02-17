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
package io.sarl.lang.codebuilder.appenders;

import io.sarl.lang.codebuilder.builders.ISarlEnumLiteralBuilder;
import io.sarl.lang.codebuilder.builders.ISarlEnumerationBuilder;
import io.sarl.lang.sarl.SarlEnumeration;
import io.sarl.lang.sarl.SarlScript;
import java.io.IOException;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend.core.xtend.XtendTypeDeclaration;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.access.IJvmTypeProvider;
import org.eclipse.xtext.xbase.compiler.ISourceAppender;
import org.eclipse.xtext.xbase.lib.Pure;

/** Source adapter of a Sarl SarlEnumeration.
	 * @see TopElementBuilderFragment.java : appendTo : 354
 */
@SuppressWarnings("all")
public class SarlEnumerationSourceAppender extends AbstractSourceAppender implements ISarlEnumerationBuilder {

	private final ISarlEnumerationBuilder builder;

	public SarlEnumerationSourceAppender(ISarlEnumerationBuilder builder) {
		this.builder = builder;
	}

	/** Fill the given receiver with the serialization of the element that is associated to this appender.
	 *
	 * @param appender the receiver of the source code.
	 * @throws IOException if there is error during the serialization.
	 * @see AbstractSubCodeBuilderFragment.java : appendTo : 549
	 */
	public void build(ISourceAppender appender) throws IOException {
		build(this.builder.getSarlEnumeration(), appender);
	}

	/** Find the reference to the type with the given name.
	 * @param typeName the fully qualified name of the type
	 * @return the type reference.
	 * @see TopElementBuilderFragment.java : appendTo : 1426
	 */
	public JvmTypeReference newTypeRef(String typeName) {
		return this.builder.newTypeRef(typeName);
	}

	/** Find the reference to the type with the given name.
	 * @param context the context for the type reference use
	 * @param typeName the fully qualified name of the type
	 * @return the type reference.
	 * @see TopElementBuilderFragment.java : appendTo : 1454
	 */
	public JvmTypeReference newTypeRef(Notifier context, String typeName) {
		return this.builder.newTypeRef(context, typeName);
	}

	/** Find the reference to the type with the given type parameters.
	 * @param type the type to reference
	 * @param args the type parameters to add to the to reference to the given type
	 * @return the type reference.
	 * @see TopElementBuilderFragment.java : appendTo : 1484
	 */
	public JvmTypeReference newTypeRef(JvmType type, JvmTypeReference... args) {
		return this.builder.newTypeRef(type, args);
	}

	/** Find the reference to the type with the given type parameters.
	 * @param type the type to reference
	 * @param args the type parameters to add to the to reference to the given type
	 * @return the type reference.
	 * @see TopElementBuilderFragment.java : appendTo : 1516
	 */
	public JvmTypeReference newTypeRef(Class type, JvmTypeReference... args) {
		return this.builder.newTypeRef(type, args);
	}

	/** Find the reference to the type with the given type parameters.
	 * @param context the context in which the type is defined
	 * @param type the type to reference
	 * @param args the type parameters to add to the to reference to the given type
	 * @return the type reference.
	 * @see TopElementBuilderFragment.java : appendTo : 1550
	 */
	public JvmTypeReference newTypeRef(Notifier context, Class type, JvmTypeReference... args) {
		return this.builder.newTypeRef(context, type, args);
	}

	public IJvmTypeProvider getTypeResolutionContext() {
		return this.builder.getTypeResolutionContext();
	}

	/** Dispose the resource.
	 * @see TopElementBuilderFragment.java : appendTo : 1613
	 */
	public void dispose() {
		this.builder.dispose();
	}

	/**
	 * @see TopElementBuilderFragment.java : appendTo : 1625
	 */
	@Override
	@Pure
	public String toString() {
		return this.builder.toString();
	}

	/** Initialize the Ecore element when inside a script.
	 * @param script the SARL script in which this SarlEnumeration is added.
	 * @param name the simple name of the SarlEnumeration.
	 * @param context the context in which the resolution of types must be done.
	 * @see TopElementBuilderFragment.java : appendTo : 1662
	 */
	public void eInit(SarlScript script, String name, IJvmTypeProvider context) {
		this.builder.eInit(script, name, context);
	}

	/** Initialize the Ecore element when inner type declaration.
	 * @param container the Ecore type that must contain this new SarlEnumeration.
	 * @param name the simple name of the SarlEnumeration.
	 * @param context the context in which the resolution of types must be done.
	 * @see TopElementBuilderFragment.java : appendTo : 1741
	 */
	public void eInit(XtendTypeDeclaration container, String name, IJvmTypeProvider context) {
		this.builder.eInit(container, name, context);
	}

	/** Replies the generated SarlEnumeration.
	 * @see TopElementBuilderFragment.java : appendTo : 1798
	 */
	@Pure
	public SarlEnumeration getSarlEnumeration() {
		return this.builder.getSarlEnumeration();
	}

	/** Replies the reference to the generated SarlAgent.
	 * @since 0.15
	 * @see TopElementBuilderFragment.java : appendTo : 1838
	 */
	@Pure
	public JvmTypeReference getSarlEnumerationReference() {
		return this.builder.getSarlEnumerationReference();
	}

	/** Replies the JVM declared type for this generated SarlEnumeration.
	 * @return the type, never {@code null}.
	 * @since 0.15
	 * @see TopElementBuilderFragment.java : appendTo : 1893
	 */
	@Pure
	public JvmDeclaredType getJvmDeclaredType() {
		return this.builder.getJvmDeclaredType();
	}

	/** Replies the resource to which the SarlEnumeration is attached.
	 * @see TopElementBuilderFragment.java : appendTo : 1928
	 */
	@Pure
	public Resource eResource() {
		return getSarlEnumeration().eResource();
	}

	/** Change the documentation of the element.
	 *
	 * <p>The documentation will be displayed just before the element.
	 *
	 * @param doc the documentation.
	 * @return {@code this}.
	 * @see AbstractSubCodeBuilderFragment.java : appendTo : 602
	 */
	public ISarlEnumerationBuilder setDocumentation(String doc) {
		this.builder.setDocumentation(doc);
		return this;
	}

	/** Add a modifier.
	 * @param modifier the modifier to add.
	 * @return {@code this}.
	 * @see TopElementBuilderFragment.java : appendTo : 2492
	 */
	public ISarlEnumerationBuilder addModifier(String modifier) {
		this.builder.addModifier(modifier);
		return this;
	}

	/** Create a SarlEnumLiteral.
	 * @param name the name of the SarlEnumLiteral.
	 * @return the builder.
	 * @see TopElementBuilderFragment.java : appendTo : 564
	 */
	public ISarlEnumLiteralBuilder addSarlEnumLiteral(String name) {
		return this.builder.addSarlEnumLiteral(name);
	}

}

