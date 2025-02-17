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

package io.sarl.eclipse.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.internal.core.ClasspathEntry;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.internal.core.util.Util;
import org.eclipse.jdt.internal.launching.RuntimeClasspathEntry;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;
import org.osgi.framework.wiring.BundleWiring;

import io.sarl.eclipse.SARLEclipsePlugin;
import io.sarl.eclipse.util.Utilities.BundleURLMappings;
import io.sarl.eclipse.util.classpath.JavaClasspathParser;
import io.sarl.lang.SARLConfig;

/** Utilities around bundles. It should be replaced
 * by the OSGi, Eclipse and Xtext API.
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("restriction")
public final class BundleUtil {

	/** OS-independent paths of the bin folders.
	 */
	public static final String[] BIN_FOLDERS = {
		SARLConfig.FOLDER_BIN,
		"bin", //$NON-NLS-1$
	};

	/** OS-independent paths of the source folders.
	 */
	public static final String[] SRC_FOLDERS = {
		SARLConfig.FOLDER_SOURCE_JAVA,
		SARLConfig.FOLDER_SOURCE_SARL,
		"src", //$NON-NLS-1$
	};

	private static final String SOURCE_SUFIX = ".source"; //$NON-NLS-1$

	private static final String JAVADOC_SUFIX = ".javadoc"; //$NON-NLS-1$

	private static final String JAR_EXTENSION = "jar"; //$NON-NLS-1$

	private static final String DOT_JAR_EXTENSION = "." + JAR_EXTENSION; //$NON-NLS-1$

	private static final String ROOT_NAME = "/"; //$NON-NLS-1$

	private static final String DEFAULT_PATH_TO_CLASSES_IN_MAVEN_PROJECT = "target/classes"; //$NON-NLS-1$

	private BundleUtil() {
		//
	}

	/** Create an instance of a set of bundle dependencies.
	 *
	 * @return the set.
	 * @since 0.13
	 */
	protected static Set<BundleDependency> createBundleDependencySet() {
		return new TreeSet<>(new Comparator<BundleDependency>() {
			@Override
			public int compare(BundleDependency o1, BundleDependency o2) {
				return o1.getBundle().getSymbolicName().compareTo(o2.getBundle().getSymbolicName());
			}
		});
	}
	
	/** Replies the source location for the given bundle.
	 *
	 * <p>The source location is usually the root folder where the source code of the bundle is located.
	 *
	 * <p>We can't use P2Utils and we can't use SimpleConfiguratorManipulator because of
	 * API breakage between 3.5 and 4.2.
	 * So we do a bit EDV (Computer data processing) ;-)
	 *
	 * @param bundle the bundle for which the source location must be computed.
	 * @param bundleLocation the location of the bundle, as replied by {@link #getBundlePath(Bundle)}.
	 * @return the path to the source folder of the bundle, or {@code null} if undefined.
	 * @see #getBundlePath(Bundle)
	 */
	public static IPath getSourceBundlePath(Bundle bundle, IPath bundleLocation) {
		IPath sourcesPath = null;
		// Not an essential functionality, make it robust
		try {
			final var srcFolderPath = getSourceRootProjectFolderPath(bundle);
			if (srcFolderPath == null) {
				//common case, jar file.
				final var bundlesParentFolder = bundleLocation.removeLastSegments(1);
				final var binaryJarName = bundleLocation.lastSegment();
				final var symbolicName = bundle.getSymbolicName();
				final var sourceJarName = binaryJarName.replace(symbolicName,
						symbolicName.concat(SOURCE_SUFIX));
				final var potentialSourceJar = bundlesParentFolder.append(sourceJarName);
				if (potentialSourceJar.toFile().exists()) {
					sourcesPath = potentialSourceJar;
				}
			} else {
				sourcesPath = srcFolderPath;
			}
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}

		return sourcesPath;
	}

	private static IPath getBinFolderPath(Bundle bundle) {
		for (final var binFolder : BIN_FOLDERS) {
			final var binFolderURL = FileLocator.find(bundle, Path.fromPortableString(binFolder), null);
			if (binFolderURL != null) {
				try {
					final var binFolderFileURL = FileLocator.toFileURL(binFolderURL);
					return new Path(binFolderFileURL.getPath()).makeAbsolute();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return null;
	}

	private static IPath getSourceRootProjectFolderPath(Bundle bundle) {
		for (final var srcFolder : SRC_FOLDERS) {
			final var relPath = Path.fromPortableString(srcFolder);
			final var srcFolderURL = FileLocator.find(bundle, relPath, null);
			if (srcFolderURL != null) {
				try {
					final var srcFolderFileURL = FileLocator.toFileURL(srcFolderURL);
					var absPath = new Path(srcFolderFileURL.getPath()).makeAbsolute();
					absPath = absPath.removeLastSegments(relPath.segmentCount());
					return absPath;
				} catch (IOException e) {
					//
				}
			}
		}
		return null;
	}

	/** Replies the path of the binary files of the given bundle.
	 *
	 * @param bundle the bundle for which the path must be retreived.
	 * @return the path to the binaries of the bundle.
	 * @see #getSourceBundlePath(Bundle, IPath)
	 */
	public static IPath getBundlePath(Bundle bundle) {
		var path = getBinFolderPath(bundle);
		if (path == null) {
			// common jar file case, no bin folder
			final var opt = FileLocator.getBundleFileLocation(bundle);
			if (opt.isPresent()) {
				path = new Path(opt.get().getAbsolutePath());
			}
		}
		return path;
	}

	/** Replies the javadoc location for the given bundle.
	 *
	 * <p>We can't use P2Utils and we can't use SimpleConfiguratorManipulator because of
	 * API breakage between 3.5 and 4.2.
	 * So we do a bit EDV (Computer data processing) ;-)
	 *
	 * @param bundle the bundle for which the javadoc location must be computed.
	 * @param bundleLocation the location of the bundle, as replied by {@link #getBundlePath(Bundle)}.
	 * @return the path to the javadoc folder of the bundle, or {@code null} if undefined.
	 * @see #getBundlePath(Bundle)
	 */
	public static IPath getJavadocBundlePath(Bundle bundle, IPath bundleLocation) {
		IPath sourcesPath = null;
		// Not an essential functionality, make it robust
		try {
			final var srcFolderPath = getSourceRootProjectFolderPath(bundle);
			if (srcFolderPath == null) {
				//common case, jar file.
				final var bundlesParentFolder = bundleLocation.removeLastSegments(1);
				final var binaryJarName = bundleLocation.lastSegment();
				final var symbolicName = bundle.getSymbolicName();
				final var sourceJarName = binaryJarName.replace(symbolicName,
						symbolicName.concat(JAVADOC_SUFIX));
				final var potentialSourceJar = bundlesParentFolder.append(sourceJarName);
				if (potentialSourceJar.toFile().exists()) {
					sourcesPath = potentialSourceJar;
				}
			} else {
				sourcesPath = srcFolderPath;
			}
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
		return sourcesPath;
	}

	/** Replies the dependencies for the given bundle.
	 *
	 * @param bundle the bundle.
	 * @return the bundle dependencies.
	 */
	public static IBundleDependencies resolveBundleDependencies(Bundle bundle) {
		return resolveBundleDependencies(bundle, (BundleURLMappings) null);
	}

	/** Replies the dependencies for the given bundle.
	 *
	 * @param bundle the bundle.
	 * @param javadocURLs the mapping from bundles to the corresponding Javadoc URLs.
	 * @return the bundle dependencies.
	 */
	public static IBundleDependencies resolveBundleDependencies(Bundle bundle, BundleURLMappings javadocURLs) {
		final var docMapping = javadocURLs == null ? new Utilities.SARLBundleJavadocURLMappings() : javadocURLs;
		return new BundleDependencies(bundle, null, null, docMapping);
	}

	/** Replies the dependencies for the given bundle.
	 *
	 * @param bundle the bundle.
	 * @param directDependencies the list of the bundle symbolic names that are the direct dependencies of the bundle to
	 *      be considered. If the given bundle has other dependencies in its Manifest, they will be ignored if they
	 *      are not in this parameter.
	 * @return the bundle dependencies.
	 * @since 0.13
	 */
	public static IBundleDependencies resolveBundleDependenciesWithFilter(Bundle bundle, String... directDependencies) {
		return resolveBundleDependenciesWithFilter(bundle, (BundleURLMappings) null, directDependencies);
	}

	/** Replies the dependencies for the given bundle.
	 *
	 * @param bundle the bundle.
	 * @param javadocURLs the mapping from bundles to the corresponding Javadoc URLs.
	 * @param directDependencies the list of the bundle symbolic names that are the direct dependencies of the bundle to
	 *      be considered. If the given bundle has other dependencies in its Manifest, they will be ignored if they
	 *      are not in this parameter.
	 * @return the bundle dependencies.
	 * @since 0.13
	 */
	public static IBundleDependencies resolveBundleDependenciesWithFilter(Bundle bundle, BundleURLMappings javadocURLs, String... directDependencies) {
		final var docMapping = javadocURLs == null ? new Utilities.SARLBundleJavadocURLMappings() : javadocURLs;
		final var deps = directDependencies == null || directDependencies.length == 0 ? null : Arrays.asList(directDependencies);
		return new BundleDependencies(bundle, deps, null, docMapping);
	}

	/** Replies the dependencies for the given bundle.
	 *
	 * @param bundle the bundle.
	 * @param extraDependencies the list of the bundle symbolic names that are in the dependencies of the bundle to
	 *      be considered. There bundles, and their transitive dependencies, are automatically added in the dependencies of the bundle. 
	 * @return the bundle dependencies.
	 * @since 0.13
	 */
	public static IBundleDependencies resolveBundleDependenciesWithExtras(Bundle bundle, String... extraDependencies) {
		return resolveBundleDependenciesWithExtras(bundle, (BundleURLMappings) null, extraDependencies);
	}

	/** Replies the dependencies for the given bundle.
	 *
	 * @param bundle the bundle.
	 * @param javadocURLs the mapping from bundles to the corresponding Javadoc URLs.
	 * @param extraDependencies the list of the bundle symbolic names that are in the dependencies of the bundle to
	 *      be considered. There bundles, and their transitive dependencies, are automatically added in the dependencies of the bundle. 
	 * @return the bundle dependencies.
	 * @since 0.13
	 */
	public static IBundleDependencies resolveBundleDependenciesWithExtras(Bundle bundle, BundleURLMappings javadocURLs, String... extraDependencies) {
		final var docMapping = javadocURLs == null ? new Utilities.SARLBundleJavadocURLMappings() : javadocURLs;
		final var deps = extraDependencies == null || extraDependencies.length == 0 ? null : Arrays.asList(extraDependencies);
		return new BundleDependencies(bundle, null, deps, docMapping);
	}

	/** Container of bundle dependencies. This class is an iterable on the symbolic names of the dependency bundles.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class BundleDependency {

		private final Bundle bundle;

		private final IClasspathEntry classpathEntry;

		private final boolean isFragment;

		private IRuntimeClasspathEntry runtimeClasspathEntry;

		/** Constructor.
		 *
		 * @param bundle the bundle.
		 * @param classPathEntry the classpath entry for the bundle.
		 * @param isFragment indicates if the dependency is a fragment or not.
		 */
		BundleDependency(Bundle bundle, IClasspathEntry classPathEntry, boolean isFragment) {
			assert bundle != null;
			assert classPathEntry != null;
			this.bundle = bundle;
			this.classpathEntry = classPathEntry;
			this.isFragment = isFragment;
		}

		/** Replies the bundle.
		 *
		 * @return the bundle.
		 */
		public Bundle getBundle() {
			return this.bundle;
		}

		/** The class path entry for the bundle.
		 *
		 * @return the classpath entry.
		 */
		public IClasspathEntry getClassPathEntry() {
			return this.classpathEntry;
		}

		/** The runtime class path entry for the bundle.
		 *
		 * @return the runtime classpath entry.
		 */
		public IRuntimeClasspathEntry getRuntimeClassPathEntry() {
			if (this.classpathEntry != null && this.runtimeClasspathEntry == null) {
				this.runtimeClasspathEntry = new RuntimeClasspathEntry(this.classpathEntry);
			}
			return this.runtimeClasspathEntry;
		}

		/** Replies if the dependency is a fragment bundle.
		 *
		 * @return {@code true} if the bundle is a fragment.
		 */
		public boolean isFragment() {
			return this.isFragment;
		}

		@Override
		public String toString() {
			return this.bundle.getSymbolicName();
		}

	}

	/** Container of bundle dependencies. This class is an iterable on the symbolic names of the dependency bundles.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public interface IBundleDependencies {

		/** Replies the name of the bundle that depends on the current dependencies.
		 *
		 * @return the bundle name.
		 */
		String getBundleSymbolicName();

		/** Replies the detected binary path for the bundle.
		 *
		 * @return the output folder.
		 */
		IPath getBundleBinaryPath();

		/** Replies the version of the bundle that is considered for computing the dependencies.
		 *
		 * @return the bundle version.
		 */
		Version getBundleVersion();

		/** Replies the symbolic names of the direct dependencies of the bundle (no transitivity).
		 * The bundle itself is included in the replied list if it is not a directory.
		 *
		 * @return the symbolic names of the bundle dependencies.
		 */
		Iterable<String> getDirectSymbolicNames();

		/** Replies the classpath entries of the bundle dependencies (no transitivity).
		 * The bundle itself is included in the replied list if it is not a directory.
		 *
		 * @return the classpath entries of the bundle dependencies.
		 */
		Iterable<IClasspathEntry> getDirectClasspathEntries();

		/** Replies the runtime classpath entries of the bundle dependencies (no transitivity).
		 * The bundle itself is included in the replied list if it is not a directory.
		 *
		 * @return the runtime classpath entries of the bundle dependencies.
		 */
		Iterable<IRuntimeClasspathEntry> getDirectRuntimeClasspathEntries();

		/** Replies the dependencies of the bundle.
		 * The bundle itself is included in the replied list if it is not a directory (no transitivity).
		 *
		 * @return the bundle dependencies, or {@code null} if the dependencies cannot be computed.
		 */
		Set<BundleDependency> getDirectDependencies();

		/** Replies the symbolic names of the bundle dependencies (transitivity).
		 * The bundle itself is included in the replied list if it is not a directory.
		 *
		 * @param includeFragments indicates if bundle fragments should be replied also.
		 * @return the symbolic names of the bundle dependencies.
		 */
		Iterable<String> getTransitiveSymbolicNames(boolean includeFragments);

		/** Replies the classpath entries of the bundle dependencies (transitivity).
		 * The bundle itself is included in the replied list if it is not a directory.
		 *
		 * @param includeFragments indicates if bundle fragments should be replied also.
		 * @return the classpath entries of the bundle dependencies.
		 */
		Iterable<IClasspathEntry> getTransitiveClasspathEntries(boolean includeFragments);

		/** Replies the runtime classpath entries of the bundle dependencies (transitivity).
		 * The bundle itself is included in the replied list if it is not a directory.
		 *
		 * @param includeFragments indicates if bundle fragments should be replied also.
		 * @return the runtime classpath entries of the bundle dependencies.
		 */
		Iterable<IRuntimeClasspathEntry> getTransitiveRuntimeClasspathEntries(boolean includeFragments);

		/** Replies the dependencies of the bundle (transitivity).
		 * The bundle itself is included in the replied list if it is not a directory.
		 *
		 * @param includeFragments indicates if bundle fragments should be replied also.
		 * @return the bundle dependencies, or {@code null} if the dependencies cannot be computed.
		 */
		Iterable<BundleDependency> getTransitiveDependencies(boolean includeFragments);

	}

	/** Definition of a set of dependencies.
	 *
	 * <p>The set entries are sorted in the insertion order.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class DependencyDefinition {

		private Version version;

		private final Set<BundleDependency> dependencies = createBundleDependencySet();

		private final Set<String> dependencyIds = new TreeSet<>();

		DependencyDefinition(Version version, Collection<BundleDependency> dependencies) {
			this.version = version;
			addDependencies(dependencies);
		}

		/** Change the version of the dependency.
		 *
		 * @param version the new version. if {@code null}, the version does not change.
		 */
		public void setVersion(Version version) {
			if (version != null) {
				this.version = version;
			}
		}

		/** Add the given dependencies.
		 *
		 * @param dependencies the dependencies.
		 */
		public void addDependencies(Collection<BundleDependency> dependencies) {
			for (final var dependency : dependencies) {
				if (this.dependencyIds.add(dependency.getBundle().getSymbolicName())) {
					this.dependencies.add(dependency);
				}
			}
		}

		/** Replies the version of the bundle.
		 *
		 * @return the version.
		 */
		public Version getVersion() {
			return this.version;
		}

		/** Replies the dependencies.
		 *
		 * @return the dependencies.
		 */
		public Set<BundleDependency> getDependencies() {
			return Collections.unmodifiableSet(this.dependencies);
		}

		@Override
		public String toString() {
			final var buf = new StringBuilder();
			buf.append("version="); //$NON-NLS-1$
			buf.append(this.version);
			buf.append("; dependencies="); //$NON-NLS-1$
			buf.append(this.dependencies.toString());
			return buf.toString();
		}

	}

	/** Container of bundle dependencies. This class is an iterable on the symbolic names of the dependency bundles.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class BundleDependencies implements IBundleDependencies {

		private final Bundle bundle;

		private final Collection<String> directDependencies;

		private final Collection<String> extraDependencies;

		private final BundleURLMappings javadocURLs;

		private IPath binaryBundlePath;

		private Map<Bundle, DependencyDefinition> bundleDependencies;

		/** Constructor.
		 *
		 * @param bundle the bundle.
		 * @param directDependencies the list of the bundle symbolic names that are the direct dependencies of the bundle to
		 *      be considered. If the given bundle has other dependencies in its Manifest, they will be ignored if they
		 *      are not in this parameter.
		 * @param extraDependencies the list of the bundle symbolic names that are in the dependencies of the bundle to
		 *      be considered. There bundles, and their transitive dependencies, are automatically added in the dependencies of the bundle. 
		 * @param javadocURLs the mapping from bundles to the corresponding Javadoc URLs.
		 */
		BundleDependencies(Bundle bundle, Collection<String> directDependencies, Collection<String> extraDependencies, BundleURLMappings javadocURLs) {
			assert bundle != null;
			this.bundle = bundle;
			this.directDependencies = directDependencies;
			this.extraDependencies = extraDependencies;
			this.javadocURLs = javadocURLs;
		}

		private Map<Bundle, DependencyDefinition> getBundleDependencies() {
			if (this.bundleDependencies == null) {
				this.bundleDependencies = new TreeMap<>(new Comparator<Bundle>() {
					@Override
					public int compare(Bundle o1, Bundle o2) {
						return o1.getSymbolicName().compareTo(o2.getSymbolicName());
					}
				});
			}
			return this.bundleDependencies;
		}

		private DependencyDefinition getBundleDependencies(Bundle bundle) {
			final var repository = getBundleDependencies();
			synchronized (repository) {
				return repository.get(bundle);
			}
		}

		private void setBundleDependencies(Bundle bundle, Set<BundleDependency> cpEntries, boolean overwrite) {
			final var repository = getBundleDependencies();
			synchronized (repository) {
				if (overwrite || !repository.containsKey(bundle)) {
					repository.put(bundle, new DependencyDefinition(bundle.getVersion(), cpEntries));
				} else {
					final var dependencySet = repository.get(bundle);
					assert dependencySet != null;
					dependencySet.setVersion(bundle.getVersion());
					dependencySet.addDependencies(cpEntries);
				}
			}
		}

		private void addToBundleDependencies(Bundle bundle, BundleDependency dependency) {
			final var repository = getBundleDependencies();
			synchronized (repository) {
				final var dependencySet = repository.get(bundle);
				if (dependencySet == null) {
					repository.put(bundle, new DependencyDefinition(bundle.getVersion(), Collections.singletonList(dependency)));
				} else {
					dependencySet.addDependencies(Collections.singletonList(dependency));
				}
			}
		}

		@Override
		public String toString() {
			final var buf = new StringBuilder();
			final var dependencies = getDependencyDefinition();
			if (dependencies != null) {
				toDependencyTree(
						buf,
						new String(), new String(),
						this.bundle,
						false,
						dependencies.getDependencies());
			}
			return buf.toString();
		}

		private void toDependencyTree(StringBuilder builder, String indent1, String indent2, Bundle current,
				boolean isFragment, Collection<BundleDependency> dependencies) {
			builder.append(indent1);
			builder.append(current.getSymbolicName());
			if (isFragment) {
				builder.append(" (fragment)"); //$NON-NLS-1$
			}
			builder.append("\n"); //$NON-NLS-1$
			for (final var dependency : dependencies) {
				if (!Objects.equals(current.getSymbolicName(), dependency.getBundle().getSymbolicName())) {
					final var subdependencies = getBundleDependencies(dependency.getBundle());
					if (subdependencies != null) {
						toDependencyTree(
								builder,
								indent2 + "|- ", //$NON-NLS-1$
								indent2 + "   ", //$NON-NLS-1$
								dependency.getBundle(),
								dependency.isFragment(),
								subdependencies.getDependencies());
					}
				}
			}
		}

		@Override
		public IPath getBundleBinaryPath() {
			if (this.binaryBundlePath == null) {
				getDependencyDefinition();
			}
			return this.binaryBundlePath;
		}

		@Override
		public Iterable<String> getDirectSymbolicNames() {
			return () -> new SymbolicNameIterator(getDirectDependencies());
		}

		@Override
		public Iterable<IClasspathEntry> getDirectClasspathEntries() {
			return () -> new ClasspathEntryIterator(getDirectDependencies());
		}

		@Override
		public Iterable<IRuntimeClasspathEntry> getDirectRuntimeClasspathEntries() {
			return () -> new RuntimeClasspathEntryIterator(getDirectDependencies());
		}

		@Override
		public Iterable<String> getTransitiveSymbolicNames(boolean includeFragments) {
			return () -> new SymbolicNameIterator(getTransitiveDependencies(includeFragments));
		}

		@Override
		public Iterable<IClasspathEntry> getTransitiveClasspathEntries(boolean includeFragments) {
			return () -> new ClasspathEntryIterator(getTransitiveDependencies(includeFragments));
		}

		@Override
		public Iterable<IRuntimeClasspathEntry> getTransitiveRuntimeClasspathEntries(boolean includeFragments) {
			return () -> new RuntimeClasspathEntryIterator(getTransitiveDependencies(includeFragments));
		}

		private DependencyDefinition getDependencyDefinition() {
			var dependencies = getBundleDependencies(this.bundle);
			if (dependencies == null) {
				final var bundlePath = BundleUtil.getBundlePath(this.bundle);
				if (bundlePath.toFile().isDirectory()) {
					// we have a directory, we assume we are in debug mode of the
					// product
					final var bundleSourcePath = BundleUtil.getSourceBundlePath(this.bundle, bundlePath);

					// Default value of the output folder for our project but will be
					// overload later in we find a .classpath precising the output
					// folder
					var outputFolder = bundleSourcePath;
					if (!bundlePath.equals(bundleSourcePath)) {
						outputFolder = Path.fromPortableString(bundleSourcePath.toPortableString().concat(DEFAULT_PATH_TO_CLASSES_IN_MAVEN_PROJECT));
					}
					URL bundleURL = null;
					try {
						bundleURL = new URI("file://" + bundleSourcePath.toPortableString()).toURL(); //$NON-NLS-1$
					} catch (Exception e) {
						return null;
					}
					final var classpathOutputFolder = readDotClasspathAndReferencestoClasspath(null, this.bundle, bundleURL);
					if (classpathOutputFolder != null) {
						outputFolder = classpathOutputFolder;
					}
					this.binaryBundlePath = outputFolder;
				} else {
					this.binaryBundlePath = bundlePath;
				}

				final var cpEntry = Utilities.newLibraryEntry(this.bundle, this.binaryBundlePath, null);

				final var cpEntries = createBundleDependencySet();
	
				updateBundleClassPath(this.bundle, cpEntry, cpEntries);
				
				if (this.extraDependencies != null) {
					for (final var extraBundleName : this.extraDependencies) {
						final var extraBundle = Platform.getBundle(extraBundleName);
						if (extraBundle != null) {
							updateBundleClassPath(extraBundle, cpEntry, cpEntries);
						}
					}
				}
				
				setBundleDependencies(this.bundle, cpEntries, true);
			
				extractAllBundleDependencies(this.bundle, true);
				dependencies = getBundleDependencies(this.bundle);
			}
			return dependencies;
		}

		@Override
		public Version getBundleVersion() {
			final var dependencies = getDependencyDefinition();
			if (dependencies == null) {
				return null;
			}
			return dependencies.getVersion();
		}

		@Override
		public String getBundleSymbolicName() {
			return this.bundle.getSymbolicName();
		}

		@Override
		public Set<BundleDependency> getDirectDependencies() {
			final var dependencies = getDependencyDefinition();
			if (dependencies == null) {
				return null;
			}
			return Collections.unmodifiableSet(dependencies.getDependencies());
		}

		@Override
		public Iterable<BundleDependency> getTransitiveDependencies(boolean includeFragments) {
			final var dependencies = getDependencyDefinition();
			if (dependencies == null) {
				return Collections.emptyList();
			}
			return () -> new TransitiveDependencyIterator(dependencies.getDependencies(), includeFragments);
		}

		/** Add the given bundle to the entries.
		 *
		 * <p>This function add the classpath entry for the bundle, and the related fragments.
		 *
		 * @param bundle the bundle to point to. Never {@code null}.
		 * @param entry the classpath entry to add to. Never {@code null}.
		 * @param entries the list of entries to add to.
		 * @return the main added dependency.
		 */
		private static BundleDependency updateBundleClassPath(Bundle bundle, IClasspathEntry entry, Set<BundleDependency> entries) {
			assert bundle != null;
			assert entry != null;
			final var rootDep = new BundleDependency(bundle, entry, false);
			entries.add(rootDep);
			final var fragments = Platform.getFragments(bundle);
			if (fragments != null && fragments.length > 0) {
				for (final var fragment : fragments) {
					final var fragmentEntry = Utilities.newLibraryEntry(fragment, null, null);
					entries.add(new BundleDependency(fragment, fragmentEntry, true));
				}
			}
			return rootDep;
		}

		/**
		 * Recursive function to get all the required dependencies of the given bundle and adding the corresponding elements to the
		 * dependency collection.
		 *
		 * @param bundle
		 *      the bundle used as root to start the dynamic search of dependencies.
		 * @param firstCall
		 * 		boolean specifying if we are at the first recursive call, in this case we use the {@link #directDependencies}
		 *      collections to filter the dependencies that are really useful.
		 */
		private void extractAllBundleDependencies(Bundle bundle, boolean firstCall) {
			final var bundleWiring = bundle.adapt(BundleWiring.class);
			final var bundleWires = bundleWiring.getRequiredWires(null);

			if (bundleWires != null) {
				for (final var wire : bundleWires) {

					final var dependency = wire.getProviderWiring().getBundle();
					assert dependency != null;
					final var dependencyInstallationPath = dependency.getLocation();

					final var existingDependencyCPE = getBundleDependencies(dependency);

					final var validDependency = (existingDependencyCPE == null
							|| dependency.getVersion().compareTo(existingDependencyCPE.getVersion()) > 0)
							&& (!firstCall || this.directDependencies == null || this.directDependencies.contains(dependency.getSymbolicName()));

					if (validDependency) {
						URL u = null;
						try {
							u = FileLocator.resolve(dependency.getEntry(ROOT_NAME));
						} catch (IOException e) {
							SARLEclipsePlugin.getDefault().log(e);
							return;
						}

						if (dependencyInstallationPath.contains(JAR_EXTENSION) || u.getProtocol().equals(JAR_EXTENSION)) {
							final var cpEntry = Utilities.newLibraryEntry(dependency, null, this.javadocURLs);
							final var cpEntries = createBundleDependencySet();
							final var dep = updateBundleClassPath(dependency, cpEntry, cpEntries);
							setBundleDependencies(dependency, cpEntries, false);
							addToBundleDependencies(bundle, dep);
						} else {
							// Management of a project having a .classpath to get the classapth
							readDotClasspathAndReferencestoClasspath(bundle, dependency, u);
						}
						extractAllBundleDependencies(dependency, false);
					}
				}
			}
		}

		/**
		 * Explore the various entries of a bundle to find its .classpath file, parse it and update accordingly the {@code classpathEntries}
		 * collection of this bundle.
		 *
		 * @param parent if not {@code null} it is the bundle that depends on the current bundle.
		 * @param bundle the bundle to explore
		 * @param bundleInstallURL the URL where the specified bundle is stored
		 * @return the Path to the output folder used to store .class file if any (if we are in an eclipse project (debug mode))
		 */
		private IPath readDotClasspathAndReferencestoClasspath(Bundle parent, Bundle bundle, URL bundleInstallURL) {
			IPath outputLocation = null;
			BundleDependency mainDependency = null;
			final var cpEntries = createBundleDependencySet();
			final var entries = bundle.getEntryPaths(ROOT_NAME);
			String entry = null;
			while (entries.hasMoreElements()) {
				entry = entries.nextElement();
				if (entry.contains(JavaProject.CLASSPATH_FILENAME)) {
					try {
						// copied from See {@link JavaProject#decodeClasspath}
						final var classpath = JavaClasspathParser.readFileEntriesWithException(bundle.getSymbolicName(),
								bundleInstallURL);

						if (classpath[0].length > 0) {
							// extract the output location
							var outputLocationEntryIndex = -1;
							for (var i = 0; outputLocationEntryIndex < 0 && i < classpath[0].length; ++i) {
								var outputLocationEntry = classpath[0][i];
								if (outputLocationEntry.getContentKind() == ClasspathEntry.K_OUTPUT) {
									outputLocation = outputLocationEntry.getPath();
									// Ensure that the classpath entry has a source attachment path
									final var sourcePath = outputLocationEntry.getSourceAttachmentPath();
									if (sourcePath == null) {
										final var entryPath = outputLocationEntry.getPath();
										outputLocationEntry = Utilities.newOutputClasspathEntry(bundle, entryPath, null);
									}
									mainDependency = new BundleDependency(bundle, outputLocationEntry, false);
									cpEntries.add(mainDependency);
									outputLocationEntryIndex = i;
								}
							}

							// discard the output location and add others real entries
							// to the classpath
							final IClasspathEntry[] copy;
							if (outputLocationEntryIndex >= 0) {
								copy = new IClasspathEntry[classpath[0].length - 1];
								if (outputLocationEntryIndex > 0) {
									System.arraycopy(classpath[0], 0, copy, 0, outputLocationEntryIndex);
								}
								if (outputLocationEntryIndex < (classpath[0].length - 1)) {
									System.arraycopy(classpath[0], outputLocationEntryIndex + 1, copy,
											outputLocationEntryIndex, classpath[0].length - outputLocationEntryIndex - 1);
								}
							} else {
								copy = classpath[0];
							}

							if (copy != null && copy.length > 0) {
								for (final var cpentry : copy) {
									if (cpentry.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
										// Do something if we have a
										// container, usually this is the JRE
										// container
										// already managed by the launch
										// configuration, that's why we do nothing
									} else if (cpentry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
										// Do something with package fragments
									} else {
										// Common entry type:
										// CPE_PROJECT|CPE_LIBRARY|CPE_VARIABLE,
										// directly managed by RuntimeClasspathEntry
										cpEntries.add(new BundleDependency(bundle, cpentry, false));
									}
								}
							}

						}


					} catch (IOException | CoreException | URISyntaxException e) {
						SARLEclipsePlugin.getDefault().log(e);
						return null;
					}

				} else if (entry.contains(DOT_JAR_EXTENSION)) {
					// A jar inside the bundle.
					// We have an error at runtime to these referenced jars.
					try {
						final var bundleJARfileFullURL = new URI(bundleInstallURL.toExternalForm().concat(
								"/").concat(entry)).toURL(); //$NON-NLS-1$
						final var jarFile = Util.toLocalFile(bundleJARfileFullURL.toURI(), null);
						final var jarFilePath = new Path(jarFile.getAbsolutePath());
						final var cpEntry = Utilities.newLibraryEntry(bundle, jarFilePath, this.javadocURLs);
						updateBundleClassPath(bundle, cpEntry, cpEntries);
					} catch (Exception e) {
						SARLEclipsePlugin.getDefault().log(e);
						return null;
					}
				}
			}


			if (cpEntries.size() > 0) {
				setBundleDependencies(bundle, cpEntries, true);
				if (parent != null && mainDependency != null) {
					addToBundleDependencies(parent, mainDependency);
				}
			} else if (parent != null) {
				mainDependency = new BundleDependency(bundle, Utilities.newOutputClasspathEntry(bundle, null, null), false);
				addToBundleDependencies(parent, mainDependency);
			}

			return outputLocation;
		}

		/** Iterator on symbolic names.
		 *
		 * @author $Author: sgalland$
		 * @version $FullVersion$
		 * @mavengroupid $GroupId$
		 * @mavenartifactid $ArtifactId$
		 */
		private class SymbolicNameIterator implements Iterator<String> {

			private final Iterator<BundleDependency> iterator;

			/** Constructor.
			 * @param dependencies the dependencies or {@code null}
			 */
			SymbolicNameIterator(Iterable<BundleDependency> dependencies) {
				if (dependencies == null) {
					this.iterator = Collections.<BundleDependency>emptyList().iterator();
				} else {
					this.iterator = dependencies.iterator();
				}
			}

			@Override
			public boolean hasNext() {
				return this.iterator.hasNext();
			}

			@Override
			public String next() {
				final var dependency = this.iterator.next();
				return dependency.getBundle().getSymbolicName();
			}

		}

		/** Iterator on classpath entries.
		 *
		 * @author $Author: sgalland$
		 * @version $FullVersion$
		 * @mavengroupid $GroupId$
		 * @mavenartifactid $ArtifactId$
		 */
		private class ClasspathEntryIterator implements Iterator<IClasspathEntry> {

			private final Iterator<BundleDependency> iterator;

			/** Constructor.
			 * @param dependencies the dependencies or {@code null}
			 */
			ClasspathEntryIterator(Iterable<BundleDependency> dependencies) {
				if (dependencies == null) {
					this.iterator = Collections.<BundleDependency>emptyList().iterator();
				} else {
					this.iterator = dependencies.iterator();
				}
			}

			@Override
			public boolean hasNext() {
				return this.iterator.hasNext();
			}

			@Override
			public IClasspathEntry next() {
				final var dependency = this.iterator.next();
				return dependency.getClassPathEntry();
			}

		}

		/** Iterator on runtime classpath entries.
		 *
		 * @author $Author: sgalland$
		 * @version $FullVersion$
		 * @mavengroupid $GroupId$
		 * @mavenartifactid $ArtifactId$
		 */
		private class RuntimeClasspathEntryIterator implements Iterator<IRuntimeClasspathEntry> {

			private final Iterator<BundleDependency> iterator;

			/** Constructor.
			 * @param dependencies the dependencies or {@code null}
			 */
			RuntimeClasspathEntryIterator(Iterable<BundleDependency> dependencies) {
				if (dependencies == null) {
					this.iterator = Collections.<BundleDependency>emptyList().iterator();
				} else {
					this.iterator = dependencies.iterator();
				}
			}

			@Override
			public boolean hasNext() {
				return this.iterator.hasNext();
			}

			@Override
			public IRuntimeClasspathEntry next() {
				final var dependency = this.iterator.next();
				return dependency.getRuntimeClassPathEntry();
			}

		}

		/** Iterator on transitive dependencies.
		 *
		 * @author $Author: sgalland$
		 * @version $FullVersion$
		 * @mavengroupid $GroupId$
		 * @mavenartifactid $ArtifactId$
		 */
		private class TransitiveDependencyIterator implements Iterator<BundleDependency> {

			private final boolean includeFragments;

			private final LinkedList<Iterator<BundleDependency>> iterators = new LinkedList<>();

			private final Set<String> repliedBundles = new TreeSet<>();

			private Iterator<BundleDependency> currentIterator;

			private BundleDependency current;

			/** Constructor.
			 * @param dependencies the dependencies or {@code null}
			 * @param includeFragments indicates if bundle fragments must be included.
			 */
			TransitiveDependencyIterator(Iterable<BundleDependency> dependencies, boolean includeFragments) {
				this.includeFragments = includeFragments;
				if (dependencies != null) {
					this.iterators.add(dependencies.iterator());
				}
				searchForNextElement();
			}

			private void searchForNextElement() {
				this.current = null;
				while (this.current == null && !this.iterators.isEmpty()) {
					if (this.currentIterator == null || !this.currentIterator.hasNext()) {
						this.currentIterator = null;
						while (this.currentIterator == null & !this.iterators.isEmpty()) {
							final var iterator = this.iterators.removeFirst();
							if (iterator.hasNext()) {
								this.currentIterator = iterator;
							}
						}
					}
					while (this.current == null && this.currentIterator != null && this.currentIterator.hasNext()) {
						final var dep = this.currentIterator.next();
						if (!this.repliedBundles.contains(dep.getBundle().getSymbolicName())
								&& (this.includeFragments || !dep.isFragment())) {
							this.current = dep;
						}
					}
				}
			}

			@Override
			public boolean hasNext() {
				return this.current != null;
			}

			@Override
			public BundleDependency next() {
				if (this.current == null) {
					throw new NoSuchElementException();
				}
				final var cur = this.current;
				final var deps = getBundleDependencies(cur.getBundle());
				if (deps != null) {
					final var depsdeps = deps.getDependencies();
					if (depsdeps != null) {
						this.iterators.add(depsdeps.iterator());
					}
				}
				this.repliedBundles.add(cur.getBundle().getSymbolicName());
				searchForNextElement();
				return cur;
			}

		}

	}

}
