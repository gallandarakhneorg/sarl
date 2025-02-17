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

package io.sarl.lang.ui.quickfix.acceptors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.text.correction.IProposalRelevance;
import org.eclipse.xtend.core.validation.IssueCodes;
import org.eclipse.xtend.core.xtend.XtendTypeDeclaration;
import org.eclipse.xtend.ide.codebuilder.XtendMethodBuilder;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmParameterizedTypeReference;
import org.eclipse.xtext.common.types.JvmTypeParameter;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xtext.xbase.typesystem.references.LightweightTypeReference;
import org.eclipse.xtext.xbase.typesystem.util.CommonTypeComputationServices;
import org.eclipse.xtext.xbase.ui.document.DocumentSourceAppender.Factory.OptionalParameters;

import io.sarl.lang.core.annotation.FiredEvent;
import io.sarl.lang.core.annotation.SyntheticMember;
import io.sarl.lang.jvmmodel.SarlJvmModelAssociations;
import io.sarl.lang.sarl.actionprototype.IActionPrototypeProvider;
import io.sarl.lang.typesystem.SARLAnnotationUtil;
import io.sarl.lang.ui.codebuilder.SarlMethodBuilder;
import io.sarl.lang.ui.codebuilder.SarlParameterBuilder;
import io.sarl.lang.ui.quickfix.SARLQuickfixProvider;
import io.sarl.lang.util.Utils;

/**
 * Add missed methods.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("restriction")
public final class MissedMethodAddModification extends SARLSemanticModification {

	private static final String GENERATED_NAME = "Generated"; //$NON-NLS-1$

	@Inject
	private SarlJvmModelAssociations associations;

	private final String[] operationUris;

	@Inject
	private Provider<XtendMethodBuilder> methodBuilder;

	@Inject
	private CommonTypeComputationServices services;

	@Inject
	private IActionPrototypeProvider actionPrototypeProvider;

	@Inject
	private SARLAnnotationUtil annotationUtils;

	private MissedMethodAddModification(String[] operationUris) {
		this.operationUris = operationUris;
	}

	/** Create the quick fix if needed.
	 *
	 * <p>User data contains the name of the expected type.
	 *
	 * @param provider the quick fix provider.
	 * @param issue the issue to fix.
	 * @param acceptor the quick fix acceptor.
	 * @param label the label of the quick fix.
	 * @param operationUris the URIs of the missed operations.
	 */
	public static void accept(SARLQuickfixProvider provider, Issue issue, IssueResolutionAcceptor acceptor,
			String label, String[] operationUris) {
		if (operationUris.length > 0) {
			final var modification = new MissedMethodAddModification(operationUris);
			provider.getInjector().injectMembers(modification);
			modification.setIssue(issue);
			modification.setTools(provider);
			acceptor.accept(
					issue,
					label,
					label,
					JavaPluginImages.IMG_CORRECTION_ADD,
					modification,
					IProposalRelevance.ADD_UNIMPLEMENTED_METHODS);
		}
	}

	@Override
	public void apply(EObject element, IModificationContext context) throws Exception {
		final var container = (XtendTypeDeclaration) element;
		final var document = context.getXtextDocument();
		final var tools = getTools();

		final var declaringType = (JvmDeclaredType) this.associations.getPrimaryJvmElement(container);

		final var insertOffset = tools.getInsertOffset(container);
		final var length = tools.getSpaceSize(document, insertOffset);
		final var options = new OptionalParameters();
		options.isJava = false;
		options.ensureEmptyLinesAround = false;
		options.baseIndentationLevel = 1;
		final var appendable = tools.getAppendableFactory().create(document,
				(XtextResource) container.eResource(), insertOffset, length, options);
		// Compute the type parameters' mapping
		final var typeParameterMap = buildTypeParameterMapping(container);

		for (final var operation : tools.getJvmOperationsFromURIs(container, this.operationUris)) {
			if (this.annotationUtils.findAnnotation(operation, SyntheticMember.class.getName()) == null
					&& !isGeneratedOperation(operation)) {

				appendable.newLine().newLine();

				final var builder = this.methodBuilder.get();
				final var sarlBuilder = builder instanceof SarlMethodBuilder cvalue ? cvalue : null;

				builder.setContext(declaringType);
				builder.setOwner(declaringType);
				builder.setMethodName(operation.getSimpleName());

				builder.setStaticFlag(false);
				builder.setOverrideFlag(!getTools().isIgnorable(IssueCodes.MISSING_OVERRIDE));
				builder.setAbstractFlag(false);

				builder.setBodyGenerator(null);

				builder.setVisibility(operation.getVisibility());
				builder.setTypeParameters(cloneTypeParameters(operation, declaringType));

				final var qualifiedActionName = this.actionPrototypeProvider.createQualifiedActionName(
						declaringType,
						operation.getSimpleName());
				final var prototype = this.actionPrototypeProvider.createPrototypeFromJvmModel(
						// TODO More general context?
						this.actionPrototypeProvider.createContext(),
						qualifiedActionName,
						operation.isVarArgs(),
						operation.getParameters());
				final var formalParameters = prototype.getFormalParameters();

				var i = 0;
				for (final var parameter : operation.getParameters()) {
					final var paramBuilder = (SarlParameterBuilder) builder.newParameterBuilder();
					paramBuilder.setName(parameter.getSimpleName());
					paramBuilder.setType(cloneTypeReference(parameter.getParameterType(), typeParameterMap));
					if (formalParameters.hasFormalParameterDefaultValue(i)) {
						final var defaultValue = formalParameters.getFormalParameterDefaultValueString(i);
						if (defaultValue != null) {
							paramBuilder.setDefaultValue(defaultValue);
						}
					}
					++i;
				}
				builder.setVarArgsFlag(operation.isVarArgs());

				builder.setReturnType(cloneTypeReference(operation.getReturnType(), typeParameterMap));

				builder.setExceptions(cloneTypeReferences(operation.getExceptions(), typeParameterMap));

				if (sarlBuilder != null) {
					final var firedEvents = this.annotationUtils.findAnnotation(operation, FiredEvent.class.getName());
					if (firedEvents != null) {
						final var events = this.annotationUtils.findTypeValues(firedEvents);
						if (events != null) {
							sarlBuilder.setFires(cloneTypeReferences(events, typeParameterMap));
						}
					}
				}

				builder.build(appendable);
			}
		}
		appendable.newLine().decreaseIndentation().newLine();

		appendable.commitChanges();
	}

	/** Replies if the given method is marked has automatically generated by the SARL compiler.
	 *
	 * @param method the method to check.
	 * @return {@code true} if the method is annoted with SyntheticMember; {@code false}
	 *     otherwise.
	 */
	public static boolean isGeneratedOperation(JvmOperation method) {
		for (final var annotation : method.getAnnotations()) {
			if (Objects.equals(SyntheticMember.class.getName(), annotation.getAnnotation().getIdentifier())) {
				return true;
			}
			final var simpleName = annotation.getAnnotation().getSimpleName();
			if (Objects.equals(GENERATED_NAME, simpleName)) {
				return true;
			}
		}
		return false;
	}

	private static JvmTypeReference mapToTypeParameter(JvmTypeReference type, Map<String, JvmTypeReference> map) {
		if (type != null && type.getType() instanceof JvmTypeParameter) {
			// The type is a type parameter of the super type.
			var ref = map.get(typeParameterId(type));
			while (ref != null) {
				final var ref2 = map.get(typeParameterId(ref));
				if (ref2 == null) {
					return ref;
				}
				ref = ref2;
			}
		}
		return type;
	}

	private Map<String, JvmTypeReference> buildTypeParameterMapping(XtendTypeDeclaration container) {
		final var map = new TreeMap<String, JvmTypeReference>();
		final var obj = this.associations.getPrimaryJvmElement(container);
		if (obj instanceof JvmGenericType genType) {
			final var encounteredTypes = new TreeSet<String>();

			final var expectedReferences = new LinkedList<JvmGenericType>();
			expectedReferences.add(genType);

			final var objectId = Object.class.getName();

			while (!expectedReferences.isEmpty()) {
				final var type = expectedReferences.removeFirst();
				if (encounteredTypes.add(type.getIdentifier())) {

					for (final var superType : type.getSuperTypes()) {
						if (!objectId.equals(superType.getIdentifier()) && superType instanceof JvmParameterizedTypeReference parametizedReference) {
							if (!parametizedReference.getArguments().isEmpty()) {
								final var genericSuperType = (JvmGenericType) parametizedReference.getType();
								var i = 0;
								for (final var refInstance : parametizedReference.getArguments()) {
									final var typeParameter = genericSuperType.getTypeParameters().get(i);
									map.put(
											typeParameterId(typeParameter, genericSuperType.getQualifiedName()),
											refInstance);
									++i;
								}
								expectedReferences.add((JvmGenericType) parametizedReference.getType());
							}
						}
					}

				}
			}
		}
		return map;
	}

	private static String typeParameterId(JvmIdentifiableElement parameter, String containerId) {
		return containerId + "$" + parameter.getIdentifier(); //$NON-NLS-1$
	}

	private static String typeParameterId(JvmTypeReference type) {
		final var realType = type.getType();
		if (realType instanceof JvmTypeParameter) {
			final var container = EcoreUtil2.getContainerOfType(realType, JvmDeclaredType.class);
			if (container != null) {
				return container.getQualifiedName() + "$" + realType.getIdentifier(); //$NON-NLS-1$
			}
		}
		return type.getQualifiedName();
	}

	/** Clone the given types by applying the type parameter mapping when necessary.
	 *
	 * @param types the types to clone.
	 * @param typeParameterMap the type parameter mapping.
	 * @return the clones.
	 */
	private List<LightweightTypeReference> cloneTypeReferences(List<JvmTypeReference> types,
			Map<String, JvmTypeReference> typeParameterMap) {
		final var newList = new ArrayList<LightweightTypeReference>(types.size());
		for (final var type : types) {
			newList.add(cloneTypeReference(type, typeParameterMap));
		}
		return newList;
	}

	private LightweightTypeReference cloneTypeReference(JvmTypeReference type,
			Map<String, JvmTypeReference> typeParameterMap) {
		final var mappedReference = mapToTypeParameter(type, typeParameterMap);
		return Utils.toLightweightTypeReference(mappedReference, this.services);
	}

	private List<JvmTypeParameter> cloneTypeParameters(JvmOperation fromOperation, JvmDeclaredType declaringType) {
		final var tools = getTools();
		final var builder1 = tools.getJvmTypeParameterBuilder();
		final var builder2 = tools.getJvmTypeBuilder();
		final var builder3 = tools.getTypeServices().getTypeReferences();
		final var builder4 = tools.getTypeServices().getTypesFactory();
		final var outParameters = new ArrayList<JvmTypeParameter>();
		// Get the type parameter mapping that is a consequence of the super type extension within the container.
		final var superTypeParameterMapping = new HashMap<String, JvmTypeReference>();
		Utils.getSuperTypeParameterMap(declaringType, superTypeParameterMapping);
		Utils.copyTypeParametersFromJvmOperation(
				fromOperation.getTypeParameters(),
				outParameters,
				superTypeParameterMapping,
				builder1, builder2, builder3, builder4);
		return outParameters;
	}

}
