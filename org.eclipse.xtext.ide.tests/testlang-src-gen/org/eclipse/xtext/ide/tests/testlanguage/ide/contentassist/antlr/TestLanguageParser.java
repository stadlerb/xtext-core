/*
 * Copyright (c) 2016 TypeFox GmbH (http://www.typefox.io) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.ide.tests.testlanguage.ide.contentassist.antlr;

import com.google.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.xtext.AbstractElement;
import org.eclipse.xtext.ide.editor.contentassist.antlr.AbstractContentAssistParser;
import org.eclipse.xtext.ide.tests.testlanguage.ide.contentassist.antlr.internal.InternalTestLanguageParser;
import org.eclipse.xtext.ide.tests.testlanguage.services.TestLanguageGrammarAccess;

public class TestLanguageParser extends AbstractContentAssistParser {

	@Inject
	private TestLanguageGrammarAccess grammarAccess;

	private Map<AbstractElement, String> nameMappings;

	@Override
	protected InternalTestLanguageParser createParser() {
		InternalTestLanguageParser result = new InternalTestLanguageParser(null);
		result.setGrammarAccess(grammarAccess);
		return result;
	}

	@Override
	protected String getRuleName(AbstractElement element) {
		if (nameMappings == null) {
			nameMappings = new HashMap<AbstractElement, String>() {
				private static final long serialVersionUID = 1L;
				{
					put(grammarAccess.getTypeAccess().getAlternatives_0(), "rule__Type__Alternatives_0");
					put(grammarAccess.getPrimitiveTypeAccess().getNameAlternatives_0(), "rule__PrimitiveType__NameAlternatives_0");
					put(grammarAccess.getTypeDeclarationAccess().getGroup(), "rule__TypeDeclaration__Group__0");
					put(grammarAccess.getTypeDeclarationAccess().getGroup_2(), "rule__TypeDeclaration__Group_2__0");
					put(grammarAccess.getPropertyAccess().getGroup(), "rule__Property__Group__0");
					put(grammarAccess.getTypeAccess().getGroup(), "rule__Type__Group__0");
					put(grammarAccess.getTypeAccess().getGroup_1(), "rule__Type__Group_1__0");
					put(grammarAccess.getModelAccess().getTypesAssignment(), "rule__Model__TypesAssignment");
					put(grammarAccess.getTypeDeclarationAccess().getNameAssignment_1(), "rule__TypeDeclaration__NameAssignment_1");
					put(grammarAccess.getTypeDeclarationAccess().getSuperTypeAssignment_2_1(), "rule__TypeDeclaration__SuperTypeAssignment_2_1");
					put(grammarAccess.getTypeDeclarationAccess().getPropertiesAssignment_4(), "rule__TypeDeclaration__PropertiesAssignment_4");
					put(grammarAccess.getPropertyAccess().getTypeAssignment_0(), "rule__Property__TypeAssignment_0");
					put(grammarAccess.getPropertyAccess().getNameAssignment_1(), "rule__Property__NameAssignment_1");
					put(grammarAccess.getTypeAccess().getArrayDiemensionsAssignment_1_0(), "rule__Type__ArrayDiemensionsAssignment_1_0");
					put(grammarAccess.getTypeReferenceAccess().getTypeRefAssignment(), "rule__TypeReference__TypeRefAssignment");
					put(grammarAccess.getPrimitiveTypeAccess().getNameAssignment(), "rule__PrimitiveType__NameAssignment");
				}
			};
		}
		return nameMappings.get(element);
	}
			
	@Override
	protected String[] getInitialHiddenTokens() {
		return new String[] { "RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT" };
	}

	public TestLanguageGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}

	public void setGrammarAccess(TestLanguageGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
}
