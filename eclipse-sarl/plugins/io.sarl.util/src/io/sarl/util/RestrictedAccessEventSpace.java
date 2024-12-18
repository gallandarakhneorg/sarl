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

package io.sarl.util;

import java.security.Principal;

import io.sarl.lang.core.Address;
import io.sarl.lang.core.EventListener;
import io.sarl.lang.core.EventSpace;



/**
 * Event driven interaction space where agent are register and unregister themselves
 * according to a access restriction.
 * Agents should only register once in this type of space.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface RestrictedAccessEventSpace extends EventSpace {

	/**
	 * Registers the entity inside this space.
	 * After registering a new agent, the Space should emit a MemberJoined
	 * event where the source is the address of the newly registered agent.
	 *
	 * <p>If the agent is already registered the address is return, but the listener is not replaced.
	 *
	 * @param entity - the event listener to register.
	 * @param requester - object that is requesting the access.
	 * @return the entity's address in this space
	 * @fires ParticipantRegistered
	 */
	Address register(EventListener entity, Principal requester);

	/**
	 * Registers the entity inside this space.
	 * After registering a new agent, the Space should emit a MemberJoined
	 * event where the source is the address of the newly registered agent.
	 *
	 * <p>If the agent is already registered the address is return, but the listener is not replaced.
	 *
	 * @param <P> - type of the entity to register.
	 * @param entity - object that is requesting the access.
	 * @return the entity's address in this space
	 * @fires ParticipantRegistered
	 */
	<P extends EventListener & Principal> Address register(P entity);

	/**
	 * Unregisters the entity inside this space.
	 * Before unregistering an agent, the Space should emit a MemberLeft
	 * event where the source is the address of the unregistered agent.
	 *
	 * @param entity - the event listener to unregister.
	 * @return the former entity's address
	 * @fires ParticipantUnregistered
	 */
	Address unregister(EventListener entity);

}
