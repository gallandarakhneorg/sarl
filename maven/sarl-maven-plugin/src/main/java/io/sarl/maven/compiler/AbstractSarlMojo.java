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

package io.sarl.maven.compiler;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.artifact.resolver.ResolutionErrorHandler;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.MojoDescriptor;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.repository.RepositorySystem;
import org.arakhne.afc.vmutil.locale.Locale;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.configuration.PlexusConfigurationException;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.Xpp3DomUtils;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import io.sarl.lang.SARLConfig;

/** Abstract mojo for compiling SARL.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractSarlMojo extends AbstractMojo {

	/** The tool that permits to access to Maven features.
	 */
	protected MavenHelper mavenHelper;

	/**
	 * The current Maven session.
	 */
	@Parameter(defaultValue = "${session}", required = true, readonly = true)
	private MavenSession session;

	/**
	 * The Build PluginManager component.
	 */
	@Component
	private BuildPluginManager buildPluginManager;

	@Component
	private RepositorySystem repositorySystem;

	@Component
	private ResolutionErrorHandler resolutionErrorHandler;

	/** Output directory.
	 */
	@Parameter
	private File output;

	/**
	 * Input directory.
	 */
	@Parameter
	private File input;

	/**
	 * Output directory for tests.
	 */
	@Parameter
	private File testOutput;

	/**
	 * Input directory for tests.
	 */
	@Parameter
	private File testInput;

	@Override
	public final void execute() throws MojoExecutionException, MojoFailureException {
		try {
			this.mavenHelper = new MavenHelper(this.session, this.buildPluginManager, this.repositorySystem,
					this.resolutionErrorHandler, getLog());
			ensureDefaultParameterValues();
			executeMojo();
		} catch (Exception e) {
			getLog().error(e.getLocalizedMessage());
			throw e;
		}
	}

	/** Ensure the mojo parameters have at least their default values.
	 */
	protected void ensureDefaultParameterValues() {
		//
	}

	/** Make absolute the given filename, relatively to the project's folder.
	 *
	 * @param file - the file to convert.
	 * @return the absolute filename.
	 */
	protected File makeAbsolute(File file) {
		if (!file.isAbsolute()) {
			final File basedir = this.mavenHelper.getSession().getCurrentProject().getBasedir();
			return new File(basedir, file.getPath()).getAbsoluteFile();
		}
		return file;
	}

	/** Replies the input folder.
	 *
	 * @return the input folder.
	 */
	protected File getInput() {
		return makeAbsolute((this.input == null) ? new File(SARLConfig.FOLDER_SOURCE_SARL) : this.input);
	}

	/** Replies the output folder.
	 *
	 * @return the output folder.
	 */
	protected File getOutput() {
		return makeAbsolute((this.output == null) ? new File(SARLConfig.FOLDER_SOURCE_GENERATED) : this.output);
	}

	/** Replies the test input folder.
	 *
	 * @return the test input folder.
	 */
	protected File getTestInput() {
		return makeAbsolute((this.testInput == null) ? new File(SARLConfig.FOLDER_TEST_SOURCE_SARL) : this.testInput);
	}

	/** Replies the test output folder.
	 *
	 * @return the test output folder.
	 */
	protected File getTestOutput() {
		return makeAbsolute((this.testOutput == null) ? new File(SARLConfig.FOLDER_TEST_SOURCE_GENERATED) : this.testOutput);
	}

	/** Execute the mojo.
	 *
	 * @throws MojoExecutionException if an unexpected problem occurs. Throwing this
	 *     exception causes a "BUILD ERROR" message to be displayed.
	 * @throws MojoFailureException if an expected problem (such as a compilation failure)
	 *     occurs. Throwing this exception causes a "BUILD FAILURE" message to be displayed.
	 */
	protected abstract void executeMojo() throws MojoExecutionException, MojoFailureException;

	/** Execute another MOJO.
	 *
	 * @param groupId - identifier of the MOJO plugin group.
	 * @param artifactId - identifier of the MOJO plugin artifact.
	 * @param version - version of the MOJO plugin version.
	 * @param goal - the goal to run.
	 * @param configuration - the XML code for the configuration.
	 * @param dependencies - the dependencies of the plugin.
	 * @throws MojoExecutionException when cannot run the MOJO.
	 * @throws MojoFailureException when the build failed.
	 */
	protected void executeMojo(
			String groupId, String artifactId,
			String version, String goal,
			String configuration,
			Dependency... dependencies) throws MojoExecutionException, MojoFailureException {
		final Plugin plugin = new Plugin();
		plugin.setArtifactId(artifactId);
		plugin.setGroupId(groupId);
		plugin.setVersion(version);
		plugin.setDependencies(Arrays.asList(dependencies));

		getLog().debug(Locale.getString(AbstractSarlMojo.class, "LAUNCHING", plugin.getId())); //$NON-NLS-1$

		final PluginDescriptor pluginDescriptor = this.mavenHelper.loadPlugin(plugin);
		if (pluginDescriptor == null) {
			throw new MojoExecutionException(Locale.getString(AbstractSarlMojo.class,
					"PLUGIN_NOT_FOUND", plugin.getId())); //$NON-NLS-1$
		}
		final MojoDescriptor mojoDescriptor = pluginDescriptor.getMojo(goal);
		if (mojoDescriptor == null) {
			throw new MojoExecutionException(Locale.getString(AbstractSarlMojo.class,
					"GOAL_NOT_FOUND", goal)); //$NON-NLS-1$
		}

		final Xpp3Dom mojoXml;
		try {
			mojoXml = toXpp3Dom(mojoDescriptor.getMojoConfiguration());
		} catch (PlexusConfigurationException e1) {
			throw new MojoExecutionException(e1.getLocalizedMessage(), e1);
		}
		Xpp3Dom configurationXml = null;
		if (configuration != null && !configuration.isEmpty()) {
			try (StringReader sr = new StringReader(configuration)) {
				try {
					configurationXml = Xpp3DomBuilder.build(sr);
				} catch (XmlPullParserException | IOException e) {
					getLog().debug(e);
				}
			}
		}
		if (configurationXml != null) {
			configurationXml = Xpp3DomUtils.mergeXpp3Dom(
					configurationXml,
					mojoXml);
		} else {
			configurationXml = mojoXml;
		}

		getLog().debug(Locale.getString(AbstractSarlMojo.class, "CONFIGURATION_FOR", //$NON-NLS-1$
				plugin.getId(), configurationXml.toString()));

		final MojoExecution execution = new MojoExecution(mojoDescriptor, configurationXml);

		this.mavenHelper.executeMojo(execution);
	}

	private Xpp3Dom toXpp3Dom(PlexusConfiguration config) throws PlexusConfigurationException {
		final Xpp3Dom result = new Xpp3Dom(config.getName());
		result.setValue(config.getValue(null));
		for (final String name : config.getAttributeNames()) {
			result.setAttribute(name, config.getAttribute(name));
		}
		for (final PlexusConfiguration child : config.getChildren()) {
			result.addChild(toXpp3Dom(child));
		}
		return result;
	}

	/** Extract the dependencies that are declared for a Maven plugin.
	 * This function reads the list of the dependencies in the configuration
	 * resource file with {@link MavenHelper#getConfig(String)}.
	 * The key given to {@link MavenHelper#getConfig(String)} is
	 * <code>&lt;configurationKeyPrefix&gt;.dependencies</code>.
	 *
	 * @param configurationKeyPrefix - the string that is the prefix in the configuration file.
	 * @return the list of the dependencies.
	 * @throws MojoExecutionException if something cannot be done when extracting the dependencies.
	 */
	protected Dependency[] getDependenciesFor(String configurationKeyPrefix) throws MojoExecutionException {
		final List<Dependency> dependencies = new ArrayList<>();
		final Pattern pattern = Pattern.compile(
				"^[ \t\n\r]*([^: \t\n\t]+)[ \t\n\r]*:[ \t\n\r]*([^: \t\n\t]+)[ \t\n\r]*$"); //$NON-NLS-1$
		final String rawDependencies = this.mavenHelper.getConfig(configurationKeyPrefix + ".dependencies"); //$NON-NLS-1$

		final Map<String, Dependency> pomDependencies = this.mavenHelper.getPluginDependencies();

		for (final String dependencyId : rawDependencies.split("\\s*[;|,]+\\s*")) { //$NON-NLS-1$
			final Matcher matcher = pattern.matcher(dependencyId);
			if (matcher != null && matcher.matches()) {
				final String dependencyGroupId = matcher.group(1);
				final String dependencyArtifactId = matcher.group(2);
				final String dependencyKey = ArtifactUtils.versionlessKey(dependencyGroupId, dependencyArtifactId);
				final Dependency dependencyObject = pomDependencies.get(dependencyKey);
				if (dependencyObject == null) {
					throw new MojoExecutionException(Locale.getString(AbstractSarlMojo.class,
							"ARTIFACT_NOT_FOUND", dependencyKey)); //$NON-NLS-1$
				}
				dependencies.add(dependencyObject);
			}
		}

		final Dependency[] dependencyArray = new Dependency[dependencies.size()];
		dependencies.toArray(dependencyArray);
		return dependencyArray;
	}

	/** Put the string representation of the properties of this object into the given buffer.
	 *
	 * @param buffer the buffer.
	 */
	protected void buildPropertyString(StringBuilder buffer) {
		buffer.append("input = ").append(this.input).append("\n"); //$NON-NLS-1$//$NON-NLS-2$
		buffer.append("output = ").append(this.output).append("\n"); //$NON-NLS-1$//$NON-NLS-2$
		buffer.append("testInput = ").append(this.testInput).append("\n"); //$NON-NLS-1$//$NON-NLS-2$
		buffer.append("testOutput = ").append(this.testOutput).append("\n"); //$NON-NLS-1$//$NON-NLS-2$
	}

}
