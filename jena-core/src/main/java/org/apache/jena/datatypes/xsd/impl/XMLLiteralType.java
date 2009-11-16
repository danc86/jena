/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jena.datatypes.xsd.impl;

import java.io.IOException ;
import java.io.StringReader ;

import org.apache.jena.datatypes.BaseDatatype ;
import org.apache.jena.datatypes.DatatypeFormatException ;
import org.apache.jena.datatypes.RDFDatatype ;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException ;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Builtin data type to represent XMLLiteral (i.e. items created
 * by use of <code>rdf:parsetype='literal'</code>.
 */
public class XMLLiteralType extends BaseDatatype implements RDFDatatype {
    /** Singleton instance */
    // Include the string for the RDF namespace, not use RDF.getURI(), to avoid an initializer circularity
    public static final RDFDatatype theXMLLiteralType = new XMLLiteralType("http://www.w3.org/1999/02/22-rdf-syntax-ns#XMLLiteral");
    
    /**
     * Private constructor.
     */
    private XMLLiteralType(String uri) {
        super(uri);
    }
    
    /**
     * Convert a serialize a value of this datatype out
     * to lexical form.
     */
    @Override
    public String unparse(Object value) {
        return value.toString();
    }
    
    /**
     * Parse a lexical form of this datatype to a value
     * @throws DatatypeFormatException if the lexical form is not legal
     */
    @Override
    public Object parse(String lexicalForm) throws DatatypeFormatException {
        try {
            parseXml(lexicalForm);
        } catch (SAXException e) {
            throw new DatatypeFormatException(lexicalForm, this, "SAXException: " + e);
        } catch (IOException e) {
            throw new DatatypeFormatException(lexicalForm, this, "IOException: " + e);
        }
        return lexicalForm; // XXX could we return the parsed form instead here?
    }
    
    /**
     * Test whether the given string is a legal lexical form
     * of this datatype.
     */
    @Override
    public boolean isValid(final String lexicalForm) {
        try {
            parseXml(lexicalForm);
        } catch (SAXException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private void parseXml(String xml) throws SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);
        SAXParser parser;
        try {
            parser = factory.newSAXParser();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        parser.parse(new InputSource(new StringReader(xml)), new DefaultHandler());
    }

}
