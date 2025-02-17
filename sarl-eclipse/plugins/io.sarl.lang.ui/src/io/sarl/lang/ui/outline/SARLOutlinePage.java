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

package io.sarl.lang.ui.outline;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import org.eclipse.xtend.core.xtend.XtendTypeDeclaration;
import org.eclipse.xtend.ide.outline.XtendOutlinePage;
import org.eclipse.xtext.ui.editor.outline.IOutlineNode;
import org.eclipse.xtext.ui.editor.outline.impl.EStructuralFeatureNode;

/**
 * Customize the outline page.
 *
 * <p>The outline page is expanding the nodes at the startup.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("restriction")
public class SARLOutlinePage extends XtendOutlinePage {

	@Override
	protected List<IOutlineNode> getInitiallyExpandedNodes() {
		// Automatically expend the rot nodes into the outline.
		final var rootNode = getTreeProvider().createRoot(getXtextDocument());
		final var result = newArrayList(rootNode);
		for (final var firstLevelNode: rootNode.getChildren()) {
			if (firstLevelNode instanceof EStructuralFeatureNode snode) {
				final var feature = snode.getEStructuralFeature();
				if (XtendTypeDeclaration.class.isAssignableFrom(feature.getContainerClass())) {
					result.add(firstLevelNode);
				}
			}
		}
		return result;
	}

}
