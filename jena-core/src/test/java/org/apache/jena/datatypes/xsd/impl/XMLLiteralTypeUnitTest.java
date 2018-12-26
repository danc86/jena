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

import static org.junit.Assert.*;

import org.apache.jena.datatypes.RDFDatatype;
import org.junit.Test;

public class XMLLiteralTypeUnitTest {

    private final RDFDatatype type = XMLLiteralType.theXMLLiteralType;

    @Test
    public void isValidShouldReturnFalseForBrokenXml() {
        assertFalse(type.isValid("<div><asdf></div>"));
        assertFalse(type.isValid("asdf"));
    }

    @Test
    public void isValidShouldReturnTrueForValidXml() {
        assertTrue(type.isValid("<div xmlns=\"http://www.w3.org/1999/xhtml\">" +
            "<p>Details of the <a href=\"/rdfschema/1.0/\">RDF schema used" + 
            "throughout the site</a> are now published in human-readable HTML" + 
            "format, and as RDF. Suggestions for improvement are welcome.</p></div>"));
    }

    @Test
    public void shouldNotConsiderNumericEntitiesInvalid() {
        assertTrue(type.isValid("<div xmlns=\"http://www.w3.org/1999/xhtml\">" +
            "Tom&#225;&#353; Masaryk</div>"));
    }

    @Test
    public void shouldNotConsiderNewlinesInsideTagsInvalid() {
        assertTrue(type.isValid("<div xmlns=\"http://www.w3.org/1999/xhtml\">" +
            "<a \n href=\"http://example.com\">a link</a></div>"));
    }

}
