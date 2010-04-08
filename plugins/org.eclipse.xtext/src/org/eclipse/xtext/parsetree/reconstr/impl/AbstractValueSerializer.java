/*******************************************************************************
 * Copyright (c) 2010 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.parsetree.reconstr.impl;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.parsetree.AbstractNode;
import org.eclipse.xtext.parsetree.reconstr.ITokenSerializer;

/**
 * @author meysholdt - Initial contribution and API
 */
public class AbstractValueSerializer implements ITokenSerializer.IValueSerializer {

	public boolean equalsOrReplacesNode(EObject context, RuleCall ruleCall, AbstractNode node) {
		return false;
	}

	public boolean equalsOrReplacesNode(EObject context, RuleCall ruleCall, Object value, AbstractNode node) {
		return false;
	}

	public String serializeAssignedValue(EObject context, RuleCall ruleCall, Object value, AbstractNode node) {
		return null;
	}

	public String serializeUnassignedValue(EObject context, RuleCall ruleCall, AbstractNode node) {
		return null;
	}

}