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

package io.sarl.eclipse.runtime;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.launching.LibraryLocation;
import org.eclipse.jdt.launching.PropertyChangeEvent;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

import io.sarl.eclipse.SARLEclipsePlugin;
import io.sarl.eclipse.util.Utilities;

/**
 * Abstract implementation of a SRE install.
 *
 * <p>Clients implementing SRE installs must subclass this class.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractSREInstall implements ISREInstall {

	private String id;

	private String name;

	private String minimalSarlVersion;

	private String maximalSarlVersion;

	private String mainClass;

	private boolean isStandalone;

	private LibraryLocation[] libraryLocations;

	private Map<String, String> attributeMap;

	private boolean dirty = true;

	/** Whether change events should be fired.
	 */
	private boolean notify = true;

	/** Construct a SRE installation.
	 *
	 * @param id - the identifier of this SRE installation.
	 */
	public AbstractSREInstall(String id) {
		this.id = id;
	}

	@Override
	public AbstractSREInstall clone() {
		try {
			final AbstractSREInstall clone = (AbstractSREInstall) super.clone();
			clone.attributeMap = this.attributeMap == null ? null
					: new HashMap<>(this.attributeMap);
			if (this.libraryLocations != null) {
				clone.libraryLocations = this.libraryLocations.clone();
			}
			if (this.libraryLocations != null) {
				clone.libraryLocations = this.libraryLocations.clone();
			}
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	@Override
	public ISREInstall copy(String id) {
		final AbstractSREInstall copy = clone();
		copy.id = id;
		return copy;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (this.getClass() == obj.getClass()) {
			return getId().equals(((ISREInstall) obj).getId());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public void setNotify(boolean notify) {
		this.notify = notify;
	}

	@Override
	public boolean getNotify() {
		return this.notify;
	}

	/** Replies if this installation has one of its field unresolved.
	 *
	 * @return <code>true</code> if one field has a too old value.
	 */
	protected boolean isDirty() {
		return this.dirty;
	}

	/** Set if this installation has one of its field unresolved.
	 *
	 * @param dirty - <code>true</code> if one field has a too old value.
	 */
	protected void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	/** Force the computation of the installation validity.
	 *
	 * @param ignoreCauses - a set of bits that indicates the invalidity causes to ignore.
	 * @return the validity status.
	 */
	protected IStatus revalidate(int ignoreCauses) {
		try {
			setDirty(false);
			resolveDirtyFields(false);
			return getValidity(ignoreCauses);
		} catch (Throwable e) {
			if ((ignoreCauses & CODE_GENERAL) == 0) {
				return SARLEclipsePlugin.getDefault().createStatus(IStatus.ERROR, CODE_GENERAL, e);
			}
			return SARLEclipsePlugin.getDefault().createOkStatus();
		}
	}

	@Override
	public final IStatus revalidate() {
		return revalidate(0);
	}

	@Override
	public final IStatus getValidity() {
		return getValidity(0);
	}

	@Override
	public IStatus getValidity(int ignoreCauses) {
		if (isDirty()) {
			return revalidate(ignoreCauses);
		}
		IStatus status = null;
		try {
			if (!isStandalone() && (ignoreCauses & CODE_STANDALONE_SRE) == 0) {
				return SARLEclipsePlugin.getDefault().createStatus(IStatus.ERROR, CODE_STANDALONE_SRE,
						Messages.AbstractSREInstall_5);
			}
			final String mainClass = getMainClass();
			if (Strings.isNullOrEmpty(mainClass) && (ignoreCauses & CODE_MAIN_CLASS) == 0) {
				return SARLEclipsePlugin.getDefault().createStatus(IStatus.ERROR, CODE_MAIN_CLASS,
						Messages.AbstractSREInstall_2);
			}
			final String name = getName();
			if (Strings.isNullOrEmpty(name) && (ignoreCauses & CODE_NAME) == 0) {
				return SARLEclipsePlugin.getDefault().createStatus(IStatus.ERROR, CODE_NAME,
						Messages.AbstractSREInstall_3);
			}
			status = getLibraryLocationValidity(ignoreCauses);
			if (status == null) {
				status = getSARLVersionValidity(ignoreCauses);
			}
		} catch (Throwable e) {
			//
		}
		if (status == null) {
			status = SARLEclipsePlugin.getDefault().createOkStatus();
		}
		return status;
	}

	private IStatus getLibraryLocationValidity(int ignoreCauses) {
		final LibraryLocation[] locations = getLibraryLocations();
		if ((locations == null || locations.length == 0) &&  (ignoreCauses & CODE_LIBRARY_LOCATION) == 0) {
			return SARLEclipsePlugin.getDefault().createStatus(IStatus.ERROR, CODE_LIBRARY_LOCATION,
					Messages.AbstractSREInstall_4);
		}
		return null;
	}

	private IStatus getSARLVersionValidity(int ignoreCauses) {
		final Bundle bundle = Platform.getBundle("io.sarl.lang"); //$NON-NLS-1$
		if (bundle != null) {
			final Version sarlVersion = bundle.getVersion();
			final Version minVersion = Utilities.parseVersion(getMinimalSARLVersion());
			final Version maxVersion = Utilities.parseVersion(getMaximalSARLVersion());
			final int cmp = Utilities.compareVersionToRange(sarlVersion, minVersion, maxVersion);
			if (cmp < 0 && (ignoreCauses & CODE_SARL_VERSION) == 0) {
				return SARLEclipsePlugin.getDefault().createStatus(IStatus.ERROR,
						CODE_SARL_VERSION,
						MessageFormat.format(
								Messages.AbstractSREInstall_0,
								sarlVersion.toString(),
								minVersion.toString()));
			} else if (cmp > 0 && (ignoreCauses & CODE_SARL_VERSION) == 0) {
				return SARLEclipsePlugin.getDefault().createStatus(IStatus.ERROR,
						CODE_SARL_VERSION,
						MessageFormat.format(
								Messages.AbstractSREInstall_1,
								sarlVersion.toString(),
								maxVersion.toString()));
			}
		}
		return null;
	}

	/** Invoked when the JAR file has changed for updating the other
	 * fields.
	 *
	 * @param forceSettings - indicates if the fields of this SRE must be set.
	 */
	protected abstract void resolveDirtyFields(boolean forceSettings);

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getName() {
		if (isDirty()) {
			setDirty(false);
			resolveDirtyFields(true);
		}
		return this.name;
	}

	@Override
	public String getMinimalSARLVersion() {
		if (isDirty()) {
			setDirty(false);
			resolveDirtyFields(true);
		}
		return this.minimalSarlVersion;
	}

	@Override
	public String getMaximalSARLVersion() {
		if (isDirty()) {
			setDirty(false);
			resolveDirtyFields(true);
		}
		return this.maximalSarlVersion;
	}

	@Override
	public String getMainClass() {
		if (isDirty()) {
			setDirty(false);
			resolveDirtyFields(true);
		}
		return this.mainClass;
	}

	@Override
	public LibraryLocation[] getLibraryLocations() {
		if (isDirty()) {
			setDirty(false);
			resolveDirtyFields(true);
		}
		if (this.libraryLocations == null) {
			return new LibraryLocation[0];
		}
		return this.libraryLocations;
	}

	@Override
	public Map<String, String> getVMSpecificAttributesMap() {
		if (isDirty()) {
			setDirty(false);
			resolveDirtyFields(true);
		}
		if (this.attributeMap == null) {
			return Collections.emptyMap();
		}
		return Collections.unmodifiableMap(this.attributeMap);
	}

	@Override
	public void setName(String name) {
		if (isDirty()) {
			setDirty(false);
			resolveDirtyFields(true);
		}
		final String normalizedName = Strings.nullToEmpty(name);
		if (!name.equals(Strings.nullToEmpty(this.name))) {
			final PropertyChangeEvent event = new PropertyChangeEvent(
					this, ISREInstallChangedListener.PROPERTY_NAME, this.name, normalizedName);
			this.name = normalizedName;
			if (this.notify) {
				SARLRuntime.fireSREChanged(event);
			}
		}
	}

	/**
	 * Change the library locations of this ISREInstall.
	 *
	 * @param libraries - The library locations of this ISREInstall.
	 *     Must not be <code>null</code>.
	 */
	@Override
	public void setLibraryLocations(LibraryLocation... libraries) {
		if (isDirty()) {
			setDirty(false);
			resolveDirtyFields(true);
		}
		if ((libraries == null && this.libraryLocations != null)
				|| (libraries != null
				&& (this.libraryLocations == null
				|| Arrays.equals(libraries, this.libraryLocations)))) {
			final PropertyChangeEvent event = new PropertyChangeEvent(
					this, ISREInstallChangedListener.PROPERTY_LIBRARY_LOCATIONS,
					this.libraryLocations, libraries);
			this.libraryLocations = libraries;
			if (this.notify) {
				SARLRuntime.fireSREChanged(event);
			}
		}
	}

	@Override
	public void setMinimalSARLVersion(String version) {
		if (isDirty()) {
			setDirty(false);
			resolveDirtyFields(true);
		}
		final String newVersion = Strings.nullToEmpty(version);
		if (!newVersion.isEmpty()) {
			// Check version number
			try {
				Version.parseVersion(newVersion);
			} catch (Throwable exception) {
				return;
			}
		}
		if (!newVersion.equals(Strings.nullToEmpty(this.minimalSarlVersion))) {
			final PropertyChangeEvent event = new PropertyChangeEvent(
					this, ISREInstallChangedListener.PROPERTY_MINIMAL_SARL_VERSION,
					this.minimalSarlVersion, newVersion);
			this.minimalSarlVersion = newVersion;
			if (this.notify) {
				SARLRuntime.fireSREChanged(event);
			}
		}
	}

	@Override
	public void setMaximalSARLVersion(String version) {
		if (isDirty()) {
			setDirty(false);
			resolveDirtyFields(true);
		}
		final String newVersion = Strings.nullToEmpty(version);
		if (!newVersion.isEmpty()) {
			// Check version number
			try {
				Version.parseVersion(newVersion);
			} catch (Throwable exception) {
				return;
			}
		}
		if (!newVersion.equals(Strings.nullToEmpty(this.maximalSarlVersion))) {
			final PropertyChangeEvent event = new PropertyChangeEvent(
					this, ISREInstallChangedListener.PROPERTY_MAXIMAL_SARL_VERSION,
					this.maximalSarlVersion, newVersion);
			this.maximalSarlVersion = newVersion;
			if (this.notify) {
				SARLRuntime.fireSREChanged(event);
			}
		}
	}

	@Override
	public void setVMSpecificAttributesMap(Map<String, String> attributes) {
		if (isDirty()) {
			setDirty(false);
			resolveDirtyFields(true);
		}
		if (!Objects.equal(attributes, this.attributeMap)) {
			final Map<String, String> oldValues;
			final Map<String, String> newValues;
			if (attributes == null) {
				newValues = Collections.emptyMap();
			} else {
				newValues = attributes;
			}
			if (this.attributeMap == null) {
				oldValues = Collections.emptyMap();
			} else {
				oldValues = this.attributeMap;
			}
			final PropertyChangeEvent event = new PropertyChangeEvent(
					this, ISREInstallChangedListener.PROPERTY_VM_ATTRIBUTES,
					oldValues, newValues);
			this.attributeMap = attributes;
			if (this.notify) {
				SARLRuntime.fireSREChanged(event);
			}
		}
	}

	@Override
	public void setMainClass(String mainClass) {
		if (isDirty()) {
			setDirty(false);
			resolveDirtyFields(true);
		}
		final String normalizedName = Strings.nullToEmpty(mainClass);
		if (!normalizedName.equals(Strings.nullToEmpty(this.mainClass))) {
			final PropertyChangeEvent event = new PropertyChangeEvent(
					this, ISREInstallChangedListener.PROPERTY_NAME, this.mainClass, normalizedName);
			this.mainClass = normalizedName;
			if (this.notify) {
				SARLRuntime.fireSREChanged(event);
			}
		}
	}

	@Override
	public void setStandalone(boolean isStandalone) {
		if (isDirty()) {
			setDirty(false);
			resolveDirtyFields(true);
		}
		if (isStandalone != this.isStandalone) {
			final PropertyChangeEvent event = new PropertyChangeEvent(
					this, ISREInstallChangedListener.PROPERTY_STANDALONE_SRE, this.isStandalone, isStandalone);
			this.isStandalone = isStandalone;
			if (this.notify) {
				SARLRuntime.fireSREChanged(event);
			}
		}
	}

	@Override
	public boolean isStandalone() {
		if (isDirty()) {
			setDirty(false);
			resolveDirtyFields(true);
		}
		return this.isStandalone;
	}

}
