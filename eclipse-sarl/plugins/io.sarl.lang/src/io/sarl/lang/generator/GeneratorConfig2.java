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

package io.sarl.lang.generator;

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;


/** Configuration for the SARL generator.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 0.4
 * @see Inline
 */
public class GeneratorConfig2 {

	/**
	 * Whether <code>@Inline</code> shall be generated.
	 */
	private boolean generateInlineAnnotation = true;

	/**
	 * Whether constant expression interpreter shall be called for generated <code>@Inline</code>.
	 */
	private boolean useExpressionInterpreterForInlineAnnotation = true;

	/** Replies if the <code>@Inline</code> shall be generated.
	 *
	 * @return <code>true</code> if annotation shall be generated.
	 */
	@Pure
	public boolean isGenerateInlineAnnotation() {
		return this.generateInlineAnnotation;
	}

	/** Set if the <code>@Inline</code> shall be generated.
	 *
	 * @param generateInlineAnnotation <code>true</code> if annotation shall be generated.
	 */
	public void setGenerateInlineAnnotation(final boolean generateInlineAnnotation) {
		this.generateInlineAnnotation = generateInlineAnnotation;
	}

	/** Replies if constant expression interpreter shall be called for generated <code>@Inline</code>.
	 *
	 * @return <code>true</code> if annotation shall be generated.
	 */
	@Pure
	public boolean isUseExpressionInterpreterForInlineAnnotation() {
		return this.useExpressionInterpreterForInlineAnnotation;
	}

	/** Set if the constant expression interpreter shall be called for generated <code>@Inline</code>.
	 *
	 * @param generateInlineAnnotation <code>true</code> if annotation shall be generated.
	 */
	public void setUseExpressionInterpreterForInlineAnnotation(final boolean generateInlineAnnotation) {
		this.useExpressionInterpreterForInlineAnnotation = generateInlineAnnotation;
	}

}
