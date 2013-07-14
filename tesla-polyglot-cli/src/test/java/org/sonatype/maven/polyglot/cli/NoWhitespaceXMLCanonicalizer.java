/*
 * Copyright (C) 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sonatype.maven.polyglot.cli;

import java.io.StringReader;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;

import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;

public class NoWhitespaceXMLCanonicalizer {
  private static boolean securityInitialized = false;

  private static Transformer noWhitespaceTransformer = null;
  private static String noWhitespaceXSL = "<xsl:stylesheet version=\"1.0\" "
      + "xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"> \n"
      + "<xsl:output method=\"xml\" omit-xml-declaration=\"yes\"/> \n"
      + "<xsl:strip-space elements=\"*\"/> \n" +

      "<xsl:template match=\"@*|node()\"> \n" + "<xsl:copy> \n"
      + "<xsl:apply-templates select=\"@*|node()\"/> \n"
      + "</xsl:copy> \n" + "</xsl:template>" + "</xsl:stylesheet>";

  private static Canonicalizer c11r = null;

  /**
   * Idempotent. Must be called at before the first instance's transform()
   * method gets called.
   **/
  public synchronized static void initCanonicalizationEngine() {
    if (!securityInitialized) {
      try {
        initApacheC14n();
        initNoWhitespaceTransformer();
      } catch (TransformerConfigurationException e) {
        throw new RuntimeException(e);
      } catch (TransformerFactoryConfigurationError e) {
        throw new RuntimeException(e);
      } catch (InvalidCanonicalizerException e) {
        throw new RuntimeException(e);
      }
    }
    securityInitialized = true;
  }

  private static void initNoWhitespaceTransformer()
      throws TransformerFactoryConfigurationError,
      TransformerConfigurationException {
    TransformerFactory factory = TransformerFactory.newInstance();
    noWhitespaceTransformer = factory.newTransformer(new StreamSource(
        new StringReader(noWhitespaceXSL)));
  }

  private static void initApacheC14n() throws InvalidCanonicalizerException {
    org.apache.xml.security.Init.init();
    c11r = Canonicalizer
        .getInstance(Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
  }

  public String transform(String input) {
    initCanonicalizationEngine();

    System.out.println("INPUT: " + input);

    DOMResult domResult = new DOMResult();
    try {
      noWhitespaceTransformer.transform(new StreamSource(
          new StringReader(input)), domResult);
      String output = new String(c11r.canonicalizeSubtree(domResult
          .getNode()));
      
      System.out.println("OUTPUT: " + output);
      
      return output;
    } catch (TransformerException e) {
      throw new RuntimeException(e);
    } catch (CanonicalizationException e) {
      throw new RuntimeException(e);
    }

  }
}
