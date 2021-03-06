/**
 * Copyright (c) 2016 TypeFox GmbH (http://www.typefox.io) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.ide.server;

import com.google.common.base.Objects;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import io.typefox.lsapi.services.json.LanguageServerProtocol;
import io.typefox.lsapi.services.json.LanguageServerToJsonAdapter;
import io.typefox.lsapi.services.json.LoggingJsonAdapter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import org.eclipse.xtext.ide.server.LanguageServerImpl;
import org.eclipse.xtext.ide.server.ServerModule;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

/**
 * @author Sven Efftinge - Initial contribution and API
 * @since 2.11
 */
@SuppressWarnings("all")
public class ServerLauncher {
  private static boolean IS_DEBUG = false;
  
  public static void main(final String[] args) {
    final Function1<String, Boolean> _function = (String it) -> {
      return Boolean.valueOf(Objects.equal(it, "debug"));
    };
    boolean _exists = IterableExtensions.<String>exists(((Iterable<String>)Conversions.doWrapArray(args)), _function);
    ServerLauncher.IS_DEBUG = _exists;
    final InputStream stdin = System.in;
    final PrintStream stdout = System.out;
    ServerLauncher.redirectStandardStreams();
    ServerModule _serverModule = new ServerModule();
    Injector _createInjector = Guice.createInjector(_serverModule);
    final ServerLauncher launcher = _createInjector.<ServerLauncher>getInstance(ServerLauncher.class);
    launcher.start(stdin, stdout);
  }
  
  @Inject
  private LanguageServerImpl languageServer;
  
  public void start(final InputStream in, final OutputStream out) {
    try {
      System.err.println("Starting Xtext Language Server.");
      LanguageServerToJsonAdapter _xifexpression = null;
      if (ServerLauncher.IS_DEBUG) {
        LoggingJsonAdapter _loggingJsonAdapter = new LoggingJsonAdapter(this.languageServer);
        final Procedure1<LoggingJsonAdapter> _function = (LoggingJsonAdapter it) -> {
          PrintWriter _printWriter = new PrintWriter(System.err);
          it.setErrorLog(_printWriter);
          PrintWriter _printWriter_1 = new PrintWriter(System.out);
          it.setMessageLog(_printWriter_1);
        };
        _xifexpression = ObjectExtensions.<LoggingJsonAdapter>operator_doubleArrow(_loggingJsonAdapter, _function);
      } else {
        LanguageServerToJsonAdapter _languageServerToJsonAdapter = new LanguageServerToJsonAdapter(this.languageServer);
        final Procedure1<LanguageServerToJsonAdapter> _function_1 = (LanguageServerToJsonAdapter it) -> {
          LanguageServerProtocol _protocol = it.getProtocol();
          final Procedure2<String, Throwable> _function_2 = (String p1, Throwable p2) -> {
            p2.printStackTrace(System.err);
          };
          _protocol.addErrorListener(_function_2);
        };
        _xifexpression = ObjectExtensions.<LanguageServerToJsonAdapter>operator_doubleArrow(_languageServerToJsonAdapter, _function_1);
      }
      final LanguageServerToJsonAdapter messageAcceptor = _xifexpression;
      messageAcceptor.connect(in, out);
      System.err.println("started.");
      messageAcceptor.join();
      while (true) {
        Thread.sleep(10_000l);
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public static void redirectStandardStreams() {
    try {
      byte[] _newByteArrayOfSize = new byte[0];
      ByteArrayInputStream _byteArrayInputStream = new ByteArrayInputStream(_newByteArrayOfSize);
      System.setIn(_byteArrayInputStream);
      String _name = ServerLauncher.class.getName();
      String _plus = (_name + "-");
      long _currentTimeMillis = System.currentTimeMillis();
      Timestamp _timestamp = new Timestamp(_currentTimeMillis);
      final String id = (_plus + _timestamp);
      if (ServerLauncher.IS_DEBUG) {
        final FileOutputStream stdFileOut = new FileOutputStream((("out-" + id) + ".log"));
        PrintStream _printStream = new PrintStream(stdFileOut);
        System.setOut(_printStream);
        final FileOutputStream stdFileErr = new FileOutputStream((("error-" + id) + ".log"));
        PrintStream _printStream_1 = new PrintStream(stdFileErr);
        System.setErr(_printStream_1);
      } else {
        ByteArrayOutputStream _byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream _printStream_2 = new PrintStream(_byteArrayOutputStream);
        System.setOut(_printStream_2);
        ByteArrayOutputStream _byteArrayOutputStream_1 = new ByteArrayOutputStream();
        PrintStream _printStream_3 = new PrintStream(_byteArrayOutputStream_1);
        System.setErr(_printStream_3);
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
