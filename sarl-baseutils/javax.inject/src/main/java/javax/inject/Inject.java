/*
 * Copyright (C) 2009 The JSR-330 Expert Group
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

package javax.inject;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;

/**
 * Identifies injectable constructors, methods, and fields. May apply to static
 * as well as instance members. An injectable member may have any access
 * modifier (private, package-private, protected, public). Constructors are
 * injected first, followed by fields, and then methods. Fields and methods
 * in superclasses are injected before those in subclasses. Ordering of
 * injection among fields and among methods in the same class is not specified.
 *
 * <p>Injectable constructors are annotated with {@code @Inject} and accept
 * zero or more dependencies as arguments. {@code @Inject} can apply to at most
 * one constructor per class.
 *
 * <blockquote style="padding-left: 2em; text-indent: -2em;">@Inject
 *       <i>ConstructorModifiers<sub>opt</sub></i>
 *       <i>SimpleTypeName</i>(<i>FormalParameterList<sub>opt</sub></i>)
 *       <i>Throws<sub>opt</sub></i>
 *       <i>ConstructorBody</i></blockquote>
 *
 * <p>{@code @Inject} is optional for public, no-argument constructors when no
 * other constructors are present. This enables injectors to invoke default
 * constructors.
 *
 * <blockquote style="padding-left: 2em; text-indent: -2em;">
 *       {@literal @}Inject<sub><i>opt</i></sub>
 *       <i>Annotations<sub>opt</sub></i>
 *       public
 *       <i>SimpleTypeName</i>()
 *       <i>Throws<sub>opt</sub></i>
 *       <i>ConstructorBody</i></blockquote>
 *
 * <p>Injectable fields:
 * <ul>
 *   <li>are annotated with {@code @Inject}.
 *   <li>are not final.
 *   <li>may have any otherwise valid name.</li></ul>
 *
 * <blockquote style="padding-left: 2em; text-indent: -2em;">@Inject
 *       <i>FieldModifiers<sub>opt</sub></i>
 *       <i>Type</i>
 *       <i>VariableDeclarators</i>;</blockquote>
 *
 * <p>Injectable methods:
 * <ul>
 *   <li>are annotated with {@code @Inject}.</li>
 *   <li>are not abstract.</li>
 *   <li>do not declare type parameters of their own.</li>
 *   <li>may return a result</li>
 *   <li>may have any otherwise valid name.</li>
 *   <li>accept zero or more dependencies as arguments.</li></ul>
 *
 * <blockquote style="padding-left: 2em; text-indent: -2em;">@Inject
 *       <i>MethodModifiers<sub>opt</sub></i>
 *       <i>ResultType</i>
 *       <i>Identifier</i>(<i>FormalParameterList<sub>opt</sub></i>)
 *       <i>Throws<sub>opt</sub></i>
 *       <i>MethodBody</i></blockquote>
 *
 * <p>The injector ignores the result of an injected method, but
 * non-{@code void} return types are allowed to support use of the method in
 * other contexts (builder-style method chaining, for example).
 *
 * <p>Examples:
 *
 * <pre>
 *   public class Car {
 *     // Injectable constructor
 *     &#064;Inject public Car(Engine engine) { ... }
 *
 *     // Injectable field
 *     &#064;Inject private Provider&lt;Seat> seatProvider;
 *
 *     // Injectable package-private method
 *     &#064;Inject void install(Windshield windshield, Trunk trunk) { ... }
 *   }</pre>
 *
 * <p>A method annotated with {@code @Inject} that overrides another method
 * annotated with {@code @Inject} will only be injected once per injection
 * request per instance. A method with <i>no</i> {@code @Inject} annotation
 * that overrides a method annotated with {@code @Inject} will not be
 * injected.
 *
 * <p>Injection of members annotated with {@code @Inject} is required. While an
 * injectable member may use any accessibility modifier (including
 * {@code private}), platform or injector limitations (like security
 * restrictions or lack of reflection support) might preclude injection
 * of non-public members.
 *
 * <h2>Qualifiers</h2>
 *
 * <p>A {@linkplain Qualifier qualifier} may annotate an injectable field
 * or parameter and, combined with the type, identify the implementation to
 * inject. Qualifiers are optional, and when used with {@code @Inject} in
 * injector-independent classes, no more than one qualifier should annotate a
 * single field or parameter. The qualifiers are bold in the following example:
 *
 * <pre>
 *   public class Car {
 *     &#064;Inject private <b>@Leather</b> Provider&lt;Seat> seatProvider;
 *
 *     &#064;Inject void install(<b>@Tinted</b> Windshield windshield,
 *         <b>@Big</b> Trunk trunk) { ... }
 *   }</pre>
 *
 * <p>If one injectable method overrides another, the overriding method's
 * parameters do not automatically inherit qualifiers from the overridden
 * method's parameters.
 *
 * <h2>Injectable Values</h2>
 *
 * <p>For a given type T and optional qualifier, an injector must be able to
 * inject a user-specified class that:
 *
 * <ol type="a">
 *   <li>is assignment compatible with T and</li>
 *   <li>has an injectable constructor.</li>
 * </ol>
 *
 * <p>For example, the user might use external configuration to pick an
 * implementation of T. Beyond that, which values are injected depend upon the
 * injector implementation and its configuration.
 *
 * <h2>Circular Dependencies</h2>
 *
 * <p>Detecting and resolving circular dependencies is left as an exercise for
 * the injector implementation. Circular dependencies between two constructors
 * is an obvious problem, but you can also have a circular dependency between
 * injectable fields or methods:
 *
 * <pre>
 *   class A {
 *     &#064;Inject B b;
 *   }
 *   class B {
 *     &#064;Inject A a;
 *   }</pre>
 *
 * <p>When constructing an instance of {@code A}, a naive injector
 * implementation might go into an infinite loop constructing an instance of
 * {@code B} to set on {@code A}, a second instance of {@code A} to set on
 * {@code B}, a second instance of {@code B} to set on the second instance of
 * {@code A}, and so on.
 *
 * <p>A conservative injector might detect the circular dependency at build
 * time and generate an error, at which point the programmer could break the
 * circular dependency by injecting {@link Provider Provider&lt;A>} or {@code
 * Provider<B>} instead of {@code A} or {@code B} respectively. Calling {@link
 * Provider#get() get()} on the provider directly from the constructor or
 * method it was injected into defeats the provider's ability to break up
 * circular dependencies. In the case of method or field injection, scoping
 * one of the dependencies (using {@linkplain Singleton singleton scope}, for
 * example) may also enable a valid circular relationship.
 *
 * @see javax.inject.Qualifier @Qualifier
 * @see javax.inject.Provider
 * @deprecated Replaced by the {@code jakarta.inject.Inject}.
 */
@Target({ METHOD, CONSTRUCTOR, FIELD })
@Retention(RUNTIME)
@Documented
@Deprecated(since = "0.15", forRemoval = true)
public @interface Inject {
	//
}
