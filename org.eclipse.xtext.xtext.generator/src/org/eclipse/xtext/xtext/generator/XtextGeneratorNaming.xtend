/*******************************************************************************
 * Copyright (c) 2015 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.xtext.generator

import com.google.inject.Inject
import org.eclipse.xtext.Grammar
import org.eclipse.xtext.xtext.generator.model.TypeReference
import org.eclipse.xtext.xtext.generator.model.project.IXtextProjectConfig

import static org.eclipse.xtext.GrammarUtil.*

/**
 * @noreference
 */
class XtextGeneratorNaming {
	
	@Inject
	IXtextProjectConfig projectConfig
	
	def getRuntimeBasePackage(Grammar grammar) {
		return getNamespace(grammar)
	}
	
	def getRuntimeTestBasePackage(Grammar grammar) {
		grammar.runtimeBasePackage + ".tests"
	}
	
	def getRuntimeModule(Grammar grammar) {
		new TypeReference(grammar.runtimeBasePackage, getSimpleName(grammar) + 'RuntimeModule')
	}
	
	def getRuntimeGenModule(Grammar grammar) {
		new TypeReference(grammar.runtimeBasePackage, 'Abstract' + getSimpleName(grammar) + 'RuntimeModule')
	}
	
	def getRuntimeDefaultModule(Grammar grammar) {
		new TypeReference('org.eclipse.xtext.service.DefaultRuntimeModule')
	}
	
	def getRuntimeSetup(Grammar grammar) {
		new TypeReference(grammar.runtimeBasePackage, getSimpleName(grammar) + 'StandaloneSetup')
	}
	
	def getRuntimeGenSetup(Grammar grammar) {
		new TypeReference(grammar.runtimeBasePackage, getSimpleName(grammar) + 'StandaloneSetupGenerated')
	}
	
	def getIdeBasePackage(Grammar grammar) {
		return grammar.runtimeBasePackage+".ide"
	}
	
	def getIdeTestBasePackage(Grammar grammar) {
		grammar.ideBasePackage + ".tests"
	}
	
	def getIdeModule(Grammar grammar) {
		new TypeReference(grammar.ideBasePackage, getSimpleName(grammar) + 'IdeModule')
	}
	
	def getIdeGenModule(Grammar grammar) {
		new TypeReference(grammar.ideBasePackage, 'Abstract' + getSimpleName(grammar) + 'IdeModule')
	}
	
	def getIdeDefaultModule(Grammar grammar) {
		new TypeReference('org.eclipse.xtext.ide.DefaultIdeModule')
	}
	
	def getIdeSetup(Grammar grammar) {
		new TypeReference(grammar.ideBasePackage, getSimpleName(grammar) + 'IdeSetup')
	}
	
	def getEclipsePluginBasePackage(Grammar grammar) {
		return getNamespace(grammar) + '.ui'
	}
	
	def getEclipsePluginTestBasePackage(Grammar grammar) {
		grammar.eclipsePluginBasePackage + ".tests"
	}
	
	def getEclipsePluginModule(Grammar grammar) {
		new TypeReference(grammar.eclipsePluginBasePackage, getSimpleName(grammar) + 'UiModule')
	}
	
	def getEclipsePluginGenModule(Grammar grammar) {
		new TypeReference(grammar.eclipsePluginBasePackage, 'Abstract' + getSimpleName(grammar) + 'UiModule')
	}
	
	def getEclipsePluginDefaultModule(Grammar grammar) {
		new TypeReference('org.eclipse.xtext.ui.DefaultUiModule')
	}
	
	def getEclipsePluginExecutableExtensionFactory(Grammar grammar) {
		new TypeReference(grammar.eclipsePluginBasePackage, getSimpleName(grammar) + 'ExecutableExtensionFactory')
	}
	
	/**
	 * @return name for the eclipsePlugin Activator or <code>null</code> if eclipsePlugin has no name
	 */
	def getEclipsePluginActivator() {
		val pluginName = projectConfig.eclipsePlugin.name
		
		if(pluginName === null) {
			return null
		}
		
		// start determining the activator's name by stripping the common ".ui" suffix
		var activatorName = pluginName.replaceAll('\\.ui$', '')
		
		// build the activator's name based on the last segment of the stripped FQN
		//  the call of 'new TypeReference(...)' below checks for invalid characters
		activatorName = activatorName.substring(activatorName.lastIndexOf('.') + 1).toFirstUpper + 'Activator'
		
		new TypeReference(pluginName + '.internal', activatorName)
	}
	
	def getGenericIdeBasePackage(Grammar grammar) {
		return getNamespace(grammar) + '.ide'
	}
	
	def getGenericIdeModule(Grammar grammar) {
		new TypeReference(grammar.genericIdeBasePackage, getSimpleName(grammar) + 'IdeModule')
	}
	
	def getGenericIdeGenModule(Grammar grammar) {
		new TypeReference(grammar.genericIdeBasePackage, 'Abstract' + getSimpleName(grammar) + 'IdeModule')
	}
	
	def getIdeaBasePackage(Grammar grammar) {
		return getNamespace(grammar) + ".idea"
	}
	
	def getIdeaModule(Grammar grammar) {
		new TypeReference(grammar.ideaBasePackage, getSimpleName(grammar) + 'IdeaModule')
	}
	
	def getIdeaDefaultModule(Grammar grammar) {
		new TypeReference('org.eclipse.xtext.idea.DefaultIdeaModule')
	}
	
	def getIdeaGenModule(Grammar grammar) {
		new TypeReference(grammar.ideaBasePackage, 'Abstract' + getSimpleName(grammar) + 'IdeaModule')
	}
	
	def getIdeaStandaloneSetup(Grammar grammar) {
		new TypeReference(grammar.ideaBasePackage, getSimpleName(grammar) + "StandaloneSetupIdea")
	}
	
	def getWebBasePackage(Grammar grammar) {
		return getNamespace(grammar) + ".web"
	}
	
	def getWebModule(Grammar grammar) {
		new TypeReference(grammar.webBasePackage, getSimpleName(grammar) + 'WebModule')
	}
	
	def getWebDefaultModule(Grammar grammar) {
		new TypeReference('org.eclipse.xtext.web.server.DefaultWebModule')
	}
	
	def getWebGenModule(Grammar grammar) {
		new TypeReference(grammar.webBasePackage, 'Abstract' + getSimpleName(grammar) + 'WebModule')
	}
	
	def getWebSetup(Grammar grammar) {
		new TypeReference(grammar.webBasePackage, getSimpleName(grammar) + 'WebSetup')
	}
	
}