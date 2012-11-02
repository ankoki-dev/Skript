/*
 *   This file is part of Skript.
 *
 *  Skript is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Skript is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 * Copyright 2011, 2012 Peter Güttinger
 * 
 */

package ch.njol.skript.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.njol.skript.Skript;

/**
 * @author Peter Güttinger
 */
public class Version implements Serializable, Comparable<Version> {
	
	private static final long serialVersionUID = -8351795409976068916L;
	
	private final int[] version = new int[3];
	/**
	 * Everything after the version, e.g. "alpha", "b", "rc 1", "build 2314" etc. or null if nothing.
	 */
	private final String postfix;
	
	public Version(final int... version) {
		if (version.length < 1 || version.length > 3)
			throw new IllegalArgumentException("Versions must have a minimum of 2 and a maximum of 3 numbers (" + version.length + " numbers given)");
		for (int i = 0; i < version.length; i++)
			this.version[i] = version[i];
		postfix = null;
	}
	
	public final static Pattern versionPattern = Pattern.compile("(\\d+)\\.(\\d+)(?:\\.(\\d+))?\\s*(.*)");
	
	public Version(final String version) {
		final Matcher m = versionPattern.matcher(version.trim());
		if (!m.matches())
			throw new IllegalArgumentException("'" + version + "' is not a valid version string");
		for (int i = 0; i < 3; i++) {
			if (m.group(i + 1) != null)
				this.version[i] = Skript.parseInt(m.group(i + 1));
		}
		postfix = m.group(m.groupCount()).isEmpty() ? null : m.group(m.groupCount());
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Version))
			return false;
		return compareTo((Version) obj) == 0;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(version) * 31 + (postfix == null ? 0 : postfix.hashCode());
	}
	
	@Override
	public int compareTo(final Version other) {
		for (int i = 0; i < version.length; i++) {
			if (version[i] > other.version[i])
				return 1;
			if (version[i] < other.version[i])
				return -1;
		}
		if (postfix == null)
			return other.postfix == null ? 0 : 1;
		else
			return other.postfix == null ? -1 : postfix.compareTo(other.postfix);
	}
	
	public boolean isSmallerThan(final Version other) {
		return compareTo(other) < 0;
	}
	
	public boolean isLargerThan(final Version other) {
		return compareTo(other) > 0;
	}
	
	@Override
	public String toString() {
		return version[0] + "." + version[1] + (version[2] == 0 ? "" : "." + version[2]) + (postfix == null ? "" : " " + postfix);
	}
	
	public final static int compare(final String v1, final String v2) {
		return new Version(v1).compareTo(new Version(v2));
	}
}