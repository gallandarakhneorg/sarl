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

package io.sarl.lang.maven.compiler.abstractmojos;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.jar.JarFile;

import com.google.common.base.Strings;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.arakhne.afc.vmutil.FileSystem;

import io.sarl.lang.compiler.batch.OptimizationLevel;
import io.sarl.lang.core.SARLVersion;
import io.sarl.lang.core.SREBootstrap;
import io.sarl.lang.maven.compiler.compiler.JavaCompiler;
import io.sarl.lang.maven.compiler.utils.Utils;

/** Abstract Mojo for compiling SARL (standard en test).
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 0.8
 */
public abstract class AbstractCompileMojo extends AbstractSarlBatchCompilerMojo {

	private static final String STUB_FOLDER = "sarl-temp"; //$NON-NLS-1$

	private static final String METAINF_FOLDER_NAME = "META-INF"; //$NON-NLS-1$
	
	private static final String METAINF_SERVICE_FOLDER_NAME = "services"; //$NON-NLS-1$

	private static final String JAR_SEPARATOR = "/"; //$NON-NLS-1$

	/** Version of the Java specification used for the source files.
	 */
	@Parameter(defaultValue = SARLVersion.MINIMAL_JDK_VERSION_IN_SARL_PROJECT_CLASSPATH, required = false)
	private String source;

	/** Encoding.
	 */
	@Parameter(required = false)
	private String encoding;

	/**
	 * Location of the temporary compiler directory.
	 */
	@Parameter
	private File tempDirectory;

	/** Indicates the Java compiler to be invoked by the SARL maven plugin.
	 */
	@Parameter(required = false)
	private String javaCompiler;

	private JavaCompiler javaCompilerInstance;

	/** Indicates if the inline annotations must be generated by the SARL maven plugin.
	 */
	@Parameter(defaultValue = "false", required = false)
	private boolean generateInlines;

	/** Indicates if the pure annotations must be generated by the SARL maven plugin.
	 */
	@Parameter(defaultValue = "true", required = false)
	private boolean generatePures;

	/** Indicates if the trace files should be generated.
	 */
	@Parameter(defaultValue = "true", required = false)
	private boolean generateTraceFiles;

	/** Indicates if the storage files should be generated.
	 */
	@Parameter(defaultValue = "true", required = false)
	private boolean generateStorageFiles;

	/** Indicates if the equality test functions must be generated by the SARL maven plugin.
	 * @since 0.8
	 */
	@Parameter(defaultValue = "true", required = false)
	private boolean generateEqualityTestFunctions;

	/** Indicates if the toString functions must be generated by the SARL maven plugin.
	 * @since 0.8
	 */
	@Parameter(defaultValue = "true", required = false)
	private boolean generateToStringFunctions;

	/** Indicates if the clone functions must be generated by the SARL maven plugin.
	 * @since 0.8
	 */
	@Parameter(defaultValue = "true", required = false)
	private boolean generateCloneFunctions;

	/** Indicates if the serial number fields must be generated by the SARL maven plugin.
	 * @since 0.8
	 */
	@Parameter(defaultValue = "true", required = false)
	private boolean generateSerialNumberFields;

	/** Indicates if the classpath is provided by Tycho.
	 */
	@Parameter(defaultValue = "false", required = false)
	private boolean tycho;

	/** Indicates if the extra-language generators to be enabled.
	 *
	 * @since 0.8
	 */
	@Parameter(required = false)
	private String[] extraGenerators;

	/** Indicates the level of optimization.
	 *
	 * @since 0.8
	 */
	@Parameter(required = false)
	private String optimization;

	private OptimizationLevel optimizationInstance;

	/** Indicates if the JRE must be first on the classpath.
	 * @since 0.12
	 */
	@Parameter(defaultValue = "false")
	private boolean fixClasspathJdtJse;

	/** Indicates if the the plugin verify if SRE is in the Maven dependencies.
	 * @since 0.13
	 */
	@Parameter(defaultValue = "true")
	private boolean verifySREInDependencies;

	/** If the SRE on the classpath is verified, this parameter indicates if SRE missing generates an error or not.
	 * @since 0.13
	 */
	@Parameter(defaultValue = "false")
	private boolean failWhenMissedSRE;

	@Override
	protected boolean isTychoEnvironment() {
		return this.tycho;
	}
	
	@Override
	protected boolean isFixingJavaClasspathForJre() {
		return this.fixClasspathJdtJse;
	}

	@Override
	protected List<File> buildClassPath() throws MojoExecutionException {
		if (isFixingJavaClasspathForJre()) {
			return fixJreClassPathFiles(super.buildClassPath(), Messages.AbstractCompileMojo_3);
		}
		return super.buildClassPath();
	}

	@Override
	protected List<File> buildTestClassPath() throws MojoExecutionException {
		if (isFixingJavaClasspathForJre()) {
			return fixJreClassPathFiles(super.buildTestClassPath(), Messages.AbstractCompileMojo_4);
		}
		return super.buildTestClassPath();
	}

	@Override
	protected String[] getExtraGenerators() {
		if (this.extraGenerators == null) {
			this.extraGenerators = new String[0];
		}
		return this.extraGenerators;
	}

	@Override
	protected String getSourceVersion() {
		return this.source;
	}

	@Override
	protected String getEncoding() {
		return this.encoding == null || this.encoding.isEmpty() ? Charset.defaultCharset().displayName() : this.encoding;
	}

	@Override
	protected File getTempDirectory() {
		if (this.tempDirectory == null) {
			final var targetDir = new File(getProject().getBuild().getDirectory());
			return makeAbsolute(new File(targetDir, STUB_FOLDER));
		}
		return makeAbsolute(this.tempDirectory);
	}

	@Override
	protected JavaCompiler getJavaCompiler() {
		if (this.javaCompilerInstance == null) {
			if (!Strings.isNullOrEmpty(this.javaCompiler)) {
				this.javaCompilerInstance = JavaCompiler.valueOfCaseInsensitive(this.javaCompiler);
				if (this.javaCompilerInstance == null) {
					String values = null;
					for (final var comp : JavaCompiler.values()) {
						if (values == null) {
							values = comp.getNameInMavenConfiguration();
						} else {
							values = MessageFormat.format(Messages.AbstractCompileMojo_1, values,
									comp.getNameInMavenConfiguration());
						}
					}
					getLogger().warn(MessageFormat.format(Messages.AbstractCompileMojo_0, this.javaCompiler,
							values, JavaCompiler.getDefault().getNameInMavenConfiguration()));
				}
			}
			if (this.javaCompilerInstance == null) {
				this.javaCompilerInstance = JavaCompiler.getDefault();
			}
		}
		return this.javaCompilerInstance;
	}

	@Override
	protected OptimizationLevel getOptimization() {
		if (this.optimizationInstance == null) {
			if (!Strings.isNullOrEmpty(this.optimization)) {
				this.optimizationInstance = OptimizationLevel.valueOfCaseInsensitive(this.optimization);
				if (this.optimizationInstance == null) {
					String values = null;
					for (final var lvl : OptimizationLevel.values()) {
						if (values == null) {
							values = lvl.getCaseInsensitiveName();
						} else {
							values = MessageFormat.format(Messages.AbstractCompileMojo_1, values,
									lvl.getCaseInsensitiveName());
						}
					}
					getLogger().warn(MessageFormat.format(Messages.AbstractCompileMojo_2, this.optimization,
							values, OptimizationLevel.getDefault().getCaseInsensitiveName()));
				}
			}
			if (this.optimizationInstance == null) {
				this.optimizationInstance = OptimizationLevel.getDefault();
			}
		}
		return this.optimizationInstance;
	}

	@Override
	protected boolean getGenerateInlines() {
		return this.generateInlines;
	}

	@Override
	protected boolean getGenerateEqualityTestFunctions() {
		return this.generateEqualityTestFunctions;
	}

	@Override
	protected boolean getGenerateToStringFunctions() {
		return this.generateToStringFunctions;
	}

	@Override
	protected boolean getGenerateCloneFunctions() {
		return this.generateCloneFunctions;
	}

	@Override
	protected boolean getGenerateSerialNumberFields() {
		return this.generateSerialNumberFields;
	}

	@Override
	protected boolean getGeneratePures() {
		return this.generatePures;
	}

	@Override
	protected boolean getGenerateTraceFiles() {
		return this.generateTraceFiles;
	}

	@Override
	protected boolean getGenerateStorageFiles() {
		return this.generateStorageFiles;
	}

	@Override
	protected void buildPropertyString(StringBuilder buffer) {
		super.buildPropertyString(buffer);
		buffer.append("source = ").append(this.source).append("\n"); //$NON-NLS-1$//$NON-NLS-2$
		buffer.append("encoding = ").append(this.encoding).append("\n"); //$NON-NLS-1$//$NON-NLS-2$
		buffer.append("tempDirectory = ").append(this.tempDirectory).append("\n"); //$NON-NLS-1$//$NON-NLS-2$
		buffer.append("javaCompiler = ").append(this.javaCompiler).append("\n"); //$NON-NLS-1$//$NON-NLS-2$
		buffer.append("generateInlines = ").append(this.generateInlines).append("\n"); //$NON-NLS-1$//$NON-NLS-2$
		buffer.append("generateTraceFiles = ").append(this.generateTraceFiles).append("\n"); //$NON-NLS-1$//$NON-NLS-2$
		buffer.append("generateStorageFiles = ").append(this.generateStorageFiles).append("\n"); //$NON-NLS-1$//$NON-NLS-2$
		buffer.append("generateEqualityTestFunctions = ").append(this.generateEqualityTestFunctions).append("\n"); //$NON-NLS-1$//$NON-NLS-2$
		buffer.append("generateToStringFunctions = ").append(this.generateToStringFunctions).append("\n"); //$NON-NLS-1$//$NON-NLS-2$
		buffer.append("generateCloneFunctions = ").append(this.generateCloneFunctions).append("\n"); //$NON-NLS-1$//$NON-NLS-2$
		buffer.append("generateSerialNumberFields = ").append(this.generateSerialNumberFields).append("\n"); //$NON-NLS-1$//$NON-NLS-2$
	}

	@Override
	protected void ensureDefaultParameterValues() {
		super.ensureDefaultParameterValues();
		if (Strings.isNullOrEmpty(this.encoding)) {
			final var properties = this.mavenHelper.getSession().getCurrentProject().getProperties();
			this.encoding = properties.getProperty("project.build.sourceEncoding", null); //$NON-NLS-1$
			if (Strings.isNullOrEmpty(this.encoding)) {
				this.encoding = Charset.defaultCharset().name();
			}
		}
	}

	@Override
	protected void executeMojo() throws MojoExecutionException, MojoFailureException {
		ensureSARLVersions();
		validateDependencyVersions();
		compileSARL();
	}

	private static boolean containsVersion(ArtifactVersion version, ArtifactVersion rangeMin, ArtifactVersion rangeMax) {
		return (version.compareTo(rangeMin) >= 0) && (version.compareTo(rangeMax) < 0);
	}

	private void ensureSARLVersions() throws MojoExecutionException, MojoFailureException {
		final var compilerVersionString = this.mavenHelper.getConfig("plugin.version"); //$NON-NLS-1$
		final var compilerVersion = new DefaultArtifactVersion(compilerVersionString);
		final var maxCompilerVersion = new DefaultArtifactVersion(
				compilerVersion.getMajorVersion() + "." //$NON-NLS-1$
				+ (compilerVersion.getMinorVersion() + 1)
				+ ".0"); //$NON-NLS-1$
		getLogger().info(MessageFormat.format(Messages.AbstractCompileMojo_5, compilerVersionString, maxCompilerVersion));
		final var classpath = new StringBuilder();
		final var foundVersions = findSARLLibrary(compilerVersion, maxCompilerVersion, classpath,
				isTychoEnvironment());
		if (foundVersions.isEmpty()) {
			throw new MojoFailureException(MessageFormat.format(Messages.AbstractCompileMojo_6, classpath.toString()));
		}
		final var versions = new StringBuilder();
		for (final var version : foundVersions) {
			if (versions.length() > 0) {
				versions.append(", "); //$NON-NLS-1$
			}
			versions.append(version);
		}
		if (foundVersions.size() > 1) {
			getLogger().info(MessageFormat.format(Messages.AbstractCompileMojo_7, versions));
		} else {
			getLogger().info(MessageFormat.format(Messages.AbstractCompileMojo_8, versions));
		}
	}

	private Set<String> findSARLLibrary(ArtifactVersion compilerVersion, ArtifactVersion maxCompilerVersion,
			StringBuilder classpath, boolean enableTycho) throws MojoExecutionException, MojoFailureException {
		final var sarlLibGroupId = this.mavenHelper.getConfig("sarl-lib.groupId"); //$NON-NLS-1$
		final var sarlLibArtifactId = this.mavenHelper.getConfig("sarl-lib.artifactId"); //$NON-NLS-1$
		final var sarlLibGroupIdTycho = "p2.eclipse-plugin"; //$NON-NLS-1$
		final var sarlLibArtifactIdTycho = this.mavenHelper.getConfig("sarl-lib.osgiBundleId"); //$NON-NLS-1$
		final var foundVersions = new TreeSet<String>();
		for (final var dep : this.mavenHelper.getSession().getCurrentProject().getArtifacts()) {
			getLogger().debug(MessageFormat.format(Messages.AbstractCompileMojo_9, dep.getGroupId(), dep.getArtifactId(), dep.getVersion()));
			if (classpath.length() > 0) {
				classpath.append(":"); //$NON-NLS-1$
			}
			classpath.append("{"); //$NON-NLS-1$
			classpath.append(ArtifactUtils.versionlessKey(dep));
			classpath.append("}"); //$NON-NLS-1$
			String gid = null;
			String aid = null;
			if (sarlLibGroupId.equals(dep.getGroupId())
					&& sarlLibArtifactId.equals(dep.getArtifactId())) {
				gid = sarlLibGroupId;
				aid = sarlLibArtifactId;
			} else if (enableTycho
					&& sarlLibGroupIdTycho.equals(dep.getGroupId())
					&& sarlLibArtifactIdTycho.equals(dep.getArtifactId())) {
				gid = sarlLibGroupIdTycho;
				aid = sarlLibArtifactIdTycho;
			}
			if (gid != null && aid != null) {
				final var dependencyVersion = new DefaultArtifactVersion(dep.getVersion());
				if (!containsVersion(dependencyVersion, compilerVersion, maxCompilerVersion)) {
					final String shortMessage = MessageFormat.format(Messages.AbstractCompileMojo_10,
							gid, aid, dependencyVersion.toString(),
							compilerVersion.toString(), maxCompilerVersion.toString());
					final String longMessage = MessageFormat.format(Messages.AbstractCompileMojo_11,
							sarlLibGroupId, sarlLibArtifactId, dependencyVersion.toString(),
							compilerVersion.toString(), maxCompilerVersion.toString());
					throw new MojoFailureException(this, shortMessage, longMessage);
				}
				foundVersions.add(dep.getVersion());
			}
		}
		return foundVersions;
	}

	private void validateDependencyVersions() throws MojoExecutionException, MojoFailureException {
		final var projectDependencyTree = this.mavenHelper.getSession().getCurrentProject().getArtifactMap();
		validateSarlSdk(projectDependencyTree);
		validateSreOnClasspath(projectDependencyTree);
	}

	private void validateSarlSdk(Map<String, Artifact> projectDependencyTree) throws MojoExecutionException, MojoFailureException {
		getLogger().info(Messages.AbstractCompileMojo_12);
		final var sarlSdkGroupId = this.mavenHelper.getConfig("sarl-sdk.groupId"); //$NON-NLS-1$
		final var sarlSdkArtifactId = this.mavenHelper.getConfig("sarl-sdk.artifactId"); //$NON-NLS-1$

		var hasError = false;
		final var sdkArtifactKey = ArtifactUtils.versionlessKey(sarlSdkGroupId, sarlSdkArtifactId);
		final var sdkArtifact = projectDependencyTree.get(sdkArtifactKey);
		if (sdkArtifact != null) {
			final var pluginDependencyTree = new TreeMap<String, ArtifactVersion>();
			final var pluginScopeDependencies = this.mavenHelper.resolveDependencies(sdkArtifactKey, false);
			for (final var pluginScopeDependency : pluginScopeDependencies) {
				final var pluginScopeDependencyVersion = new DefaultArtifactVersion(pluginScopeDependency.getVersion());
				final var pluginScopeDependencyKey = ArtifactUtils.versionlessKey(pluginScopeDependency);
				final var currentVersion = pluginDependencyTree.get(pluginScopeDependencyKey);
				if (currentVersion == null || pluginScopeDependencyVersion.compareTo(currentVersion) > 0) {
					pluginDependencyTree.put(pluginScopeDependencyKey, pluginScopeDependencyVersion);
				}
			}

			for (final var projectDependency : projectDependencyTree.entrySet()) {
				final var pluginDependencyArtifactVersion = pluginDependencyTree.get(projectDependency.getKey());
				if (pluginDependencyArtifactVersion != null) {
					final var projectArtifact = projectDependency.getValue();
					final var projectDependencyVersion = new DefaultArtifactVersion(projectArtifact.getVersion());
					if (Utils.compareMajorMinorVersions(pluginDependencyArtifactVersion, projectDependencyVersion) != 0) {
						final var message = MessageFormat.format(Messages.AbstractCompileMojo_13,
								projectArtifact.getGroupId(), projectArtifact.getArtifactId(),
								pluginDependencyArtifactVersion.toString(), projectDependencyVersion.toString());
						getLogger().error(message);
						hasError = true;
					}
				}
			}
		}

		if (hasError) {
			throw new MojoFailureException(Messages.AbstractCompileMojo_14);
		}
	}

	private void validateSreOnClasspath(Map<String, Artifact> projectDependencyTree) {
		if (this.verifySREInDependencies) {
			getLogger().info(Messages.AbstractCompileMojo_15);
			for (final var artifact : projectDependencyTree.values()) {
				final var file = artifact.getFile();
				var found = false;
				if (file != null) {
					if (file.isDirectory()) {
						final var sreServiceFile = FileSystem.join(file, METAINF_FOLDER_NAME, METAINF_SERVICE_FOLDER_NAME, SREBootstrap.class.getName());
						found = sreServiceFile.exists();
					} else {
						try (final var jarFile = new JarFile(file)) {
							found = jarFile.getEntry(METAINF_FOLDER_NAME + JAR_SEPARATOR + METAINF_SERVICE_FOLDER_NAME + JAR_SEPARATOR + SREBootstrap.class.getName()) != null;
						} catch (IOException ex) {
							// Ignore this error. Assume the given file is not a valid SRE implementation
						}
					}
				}
				if (found) {
					// The SRE service file was found in the project
					getLogger().info(MessageFormat.format(Messages.AbstractCompileMojo_17, artifact.getId()));
					return;
				}
			}
			// No SRE implementation was found
			getLogger().warn(Messages.AbstractCompileMojo_18);
		} else if (getLogger().isDebugEnabled()) {
			getLogger().debug(Messages.AbstractCompileMojo_16);
		}
	}

	/** Run the SARL compiler.
	 *
	 * @throws MojoExecutionException if the mojo cannot be run.
	 * @throws MojoFailureException if an error occurs during the compilation.
	 */
	protected abstract void compileSARL() throws MojoExecutionException, MojoFailureException;

}
