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

package io.sarl.lang.jvmmodel.fragments;

/** Factory of JVM type for the 1-to-many generation.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 0.15
 * @see JvmGenericTypeProvider
 */
@FunctionalInterface
public interface JvmGenericTypeFactory {
	
	/** Create a generic type that was created at the given slot.
	 *
	 * @param index the slot index of the generic type.
	 * @param name the name of the generic type. It must not be empty.
	 * @return {@code this}.
	 */
	JvmGenericTypeFactory createReceiver(int index, String name);

}
