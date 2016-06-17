/**
 * Copyright (c) 2016 TypeFox GmbH (http://www.typefox.io) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.ide.server;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.build.BuildRequest;
import org.eclipse.xtext.build.IncrementalBuilder;
import org.eclipse.xtext.build.IndexState;
import org.eclipse.xtext.build.Source2GeneratedMapping;
import org.eclipse.xtext.resource.IExternalContentSupport;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.resource.impl.ChunkedResourceDescriptions;
import org.eclipse.xtext.resource.impl.ProjectDescription;
import org.eclipse.xtext.resource.impl.ResourceDescriptionsData;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.util.IAcceptor;
import org.eclipse.xtext.util.IFileSystemScanner;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * @author Sven Efftinge - Initial contribution and API
 * @since 2.11
 */
@SuppressWarnings("all")
public class ProjectManager {
  @Inject
  protected IncrementalBuilder incrementalBuilder;
  
  @Inject
  protected Provider<XtextResourceSet> resourceSetProvider;
  
  @Inject
  protected IResourceServiceProvider.Registry languagesRegistry;
  
  @Inject
  protected IFileSystemScanner fileSystemScanner;
  
  @Inject
  protected IExternalContentSupport externalContentSupport;
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private IndexState indexState = new IndexState();
  
  private URI baseDir;
  
  private Procedure2<? super URI, ? super Iterable<Issue>> issueAcceptor;
  
  private Provider<Map<String, ResourceDescriptionsData>> indexProvider;
  
  private IExternalContentSupport.IExternalContentProvider openedDocumentsContentProvider;
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private XtextResourceSet resourceSet;
  
  public IncrementalBuilder.Result initialize(final URI baseDir, final Procedure2<? super URI, ? super Iterable<Issue>> acceptor, final IExternalContentSupport.IExternalContentProvider openedDocumentsContentProvider, final Provider<Map<String, ResourceDescriptionsData>> indexProvider, final CancelIndicator cancelIndicator) {
    final ArrayList<URI> uris = CollectionLiterals.<URI>newArrayList();
    this.baseDir = baseDir;
    this.issueAcceptor = acceptor;
    this.openedDocumentsContentProvider = openedDocumentsContentProvider;
    this.indexProvider = indexProvider;
    final IAcceptor<URI> _function = (URI it) -> {
      uris.add(it);
    };
    this.fileSystemScanner.scan(baseDir, _function);
    List<URI> _emptyList = CollectionLiterals.<URI>emptyList();
    return this.doBuild(uris, _emptyList, cancelIndicator);
  }
  
  public IncrementalBuilder.Result doBuild(final List<URI> dirtyFiles, final List<URI> deletedFiles, final CancelIndicator cancelIndicator) {
    final BuildRequest request = this.newBuildRequest(dirtyFiles, deletedFiles, cancelIndicator);
    final Function1<URI, IResourceServiceProvider> _function = (URI it) -> {
      return this.languagesRegistry.getResourceServiceProvider(it);
    };
    final IncrementalBuilder.Result result = this.incrementalBuilder.build(request, _function);
    IndexState _indexState = result.getIndexState();
    this.indexState = _indexState;
    XtextResourceSet _resourceSet = request.getResourceSet();
    this.resourceSet = _resourceSet;
    return result;
  }
  
  protected BuildRequest newBuildRequest(final List<URI> changedFiles, final List<URI> deletedFiles, final CancelIndicator cancelIndicator) {
    BuildRequest _buildRequest = new BuildRequest();
    final Procedure1<BuildRequest> _function = (BuildRequest it) -> {
      it.setBaseDir(this.baseDir);
      List<IResourceDescription> _emptyList = CollectionLiterals.<IResourceDescription>emptyList();
      ResourceDescriptionsData _resourceDescriptionsData = new ResourceDescriptionsData(_emptyList);
      XtextResourceSet _createFreshResourceSet = this.createFreshResourceSet(_resourceDescriptionsData);
      it.setResourceSet(_createFreshResourceSet);
      ResourceDescriptionsData _resourceDescriptions = this.indexState.getResourceDescriptions();
      ResourceDescriptionsData _copy = _resourceDescriptions.copy();
      Source2GeneratedMapping _fileMappings = this.indexState.getFileMappings();
      Source2GeneratedMapping _copy_1 = _fileMappings.copy();
      IndexState _indexState = new IndexState(_copy, _copy_1);
      it.setState(_indexState);
      IndexState _state = it.getState();
      ResourceDescriptionsData _resourceDescriptions_1 = _state.getResourceDescriptions();
      XtextResourceSet _createFreshResourceSet_1 = this.createFreshResourceSet(_resourceDescriptions_1);
      it.setResourceSet(_createFreshResourceSet_1);
      it.setDirtyFiles(changedFiles);
      it.setDeletedFiles(deletedFiles);
      final BuildRequest.IPostValidationCallback _function_1 = (URI uri, Iterable<Issue> issues) -> {
        this.issueAcceptor.apply(uri, issues);
        return true;
      };
      it.setAfterValidate(_function_1);
      it.setCancelIndicator(cancelIndicator);
    };
    return ObjectExtensions.<BuildRequest>operator_doubleArrow(_buildRequest, _function);
  }
  
  protected XtextResourceSet createFreshResourceSet(final ResourceDescriptionsData newIndex) {
    XtextResourceSet _get = this.resourceSetProvider.get();
    final Procedure1<XtextResourceSet> _function = (XtextResourceSet it) -> {
      ProjectDescription _projectDescription = new ProjectDescription();
      final Procedure1<ProjectDescription> _function_1 = (ProjectDescription it_1) -> {
        it_1.setName("test-project");
      };
      final ProjectDescription projectDescription = ObjectExtensions.<ProjectDescription>operator_doubleArrow(_projectDescription, _function_1);
      projectDescription.attachToEmfObject(it);
      Map<String, ResourceDescriptionsData> _get_1 = this.indexProvider.get();
      final ChunkedResourceDescriptions index = new ChunkedResourceDescriptions(_get_1, it);
      String _name = projectDescription.getName();
      index.setContainer(_name, newIndex);
      this.externalContentSupport.configureResourceSet(it, this.openedDocumentsContentProvider);
    };
    return ObjectExtensions.<XtextResourceSet>operator_doubleArrow(_get, _function);
  }
  
  public Resource getResource(final URI uri) {
    return this.resourceSet.getResource(uri, true);
  }
  
  @Pure
  public IndexState getIndexState() {
    return this.indexState;
  }
  
  @Pure
  public XtextResourceSet getResourceSet() {
    return this.resourceSet;
  }
}