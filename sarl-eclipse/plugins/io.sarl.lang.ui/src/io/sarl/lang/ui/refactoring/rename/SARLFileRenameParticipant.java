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

package io.sarl.lang.ui.refactoring.rename;

import static java.util.Collections.singletonList;

import java.util.List;
import java.util.Objects;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend.core.xtend.XtendTypeDeclaration;
import org.eclipse.xtext.Constants;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.refactoring.IChangeRedirector;
import org.eclipse.xtext.ui.refactoring.impl.AbstractProcessorBasedRenameParticipant;
import org.eclipse.xtext.ui.refactoring.ui.IRenameContextFactory;
import org.eclipse.xtext.ui.refactoring.ui.IRenameElementContext;
import org.eclipse.xtext.ui.resource.IResourceSetProvider;

/** Participant to the SARL file renaming mechanism.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("restriction")
public class SARLFileRenameParticipant extends AbstractProcessorBasedRenameParticipant {

	@Inject
	private IResourceSetProvider resourceSetProvider;

	@Inject
	private IRenameContextFactory renameContextFactory;

	private final String fileExtension;

	/** Construct the participant.
	 *
	 * @param fileExtension the file extension.
	 */
	@Inject
	public SARLFileRenameParticipant(@Named(Constants.FILE_EXTENSIONS) String fileExtension) {
		this.fileExtension = fileExtension;
	}

	@Override
	protected List<? extends IRenameElementContext> createRenameElementContexts(Object element) {
		if (!getArguments().getUpdateReferences()) {
			return super.createRenameElementContexts(element);
		}
		assert element instanceof IFile;
		final var file = (IFile) element;
		final var filePath = file.getFullPath();
		if (Objects.equals(filePath.getFileExtension(), this.fileExtension)) {
			final var typeName = filePath.removeFileExtension().lastSegment();
			final var newPath = filePath.removeLastSegments(1).append(
					getNewName()).addFileExtension(this.fileExtension);
			final var resourceSet = this.resourceSetProvider.get(file.getProject());
			final var resourceURI = URI.createPlatformResourceURI(filePath.toString(), true);
			final var resource = resourceSet.getResource(resourceURI, true);
			if (resource != null && !resource.getContents().isEmpty()) {
				for (final var type : EcoreUtil2.eAllOfType(resource.getContents().get(0),
						XtendTypeDeclaration.class)) {
					if (Objects.equals(typeName, type.getName())) {
						final var renameElementContext =
								this.renameContextFactory.createRenameElementContext(
										type, null, null, (XtextResource) resource);
						if (renameElementContext instanceof IChangeRedirector.Aware cvalue) {
							cvalue.setChangeRedirector(
								source -> Objects.equals(source, filePath) ? newPath : filePath);
						}
						return singletonList(renameElementContext);
					}
				}
			}
		}
		return super.createRenameElementContexts(element);
	}

	@Override
	protected String getNewName() {
		return trimFileExtension(super.getNewName());
	}

	/** Remove the file extension.
	 *
	 * @param fileName the file name.
	 * @return the file name without the extension.
	 */
	protected static String trimFileExtension(String fileName) {
		if (fileName.lastIndexOf('.') == -1) {
			return fileName;
		}
		return fileName.substring(0, fileName.lastIndexOf('.'));
	}

	@Override
	protected List<EObject> getRenamedElementsOrProxies(EObject originalTarget) {
		return singletonList(originalTarget);
	}

}
