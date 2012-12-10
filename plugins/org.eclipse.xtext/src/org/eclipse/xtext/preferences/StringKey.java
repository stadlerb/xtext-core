/*******************************************************************************
 * Copyright (c) 2012 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.preferences;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 * @author Sven Efftinge
 */
public class StringKey extends IPreferenceKey.AbstractKey<String> {

	private String defaultValue;

	public StringKey(String name, String defaultValue) {
		super(name);
		this.defaultValue = defaultValue;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public String valueToString(String value) {
		return value;
	}

	public String stringToValue(String value) {
		return value;
	}

}
