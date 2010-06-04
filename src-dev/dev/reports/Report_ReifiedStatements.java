/*
 * (c) Copyright 2010 Talis Systems Ltd.
 * All rights reserved.
 * [See end of file]
 */

package dev.reports;

import com.hp.hpl.jena.rdf.model.Model ;
import com.hp.hpl.jena.rdf.model.ModelFactory ;
import com.hp.hpl.jena.rdf.model.ReifiedStatement ;
import com.hp.hpl.jena.rdf.model.ResourceFactory ;
import com.hp.hpl.jena.rdf.model.Statement ;
import com.hp.hpl.jena.sdb.SDBFactory ;
import com.hp.hpl.jena.sdb.Store ;
import com.hp.hpl.jena.vocabulary.RDFS ;

public class Report_ReifiedStatements
{
    public static void main(String...argv)
    {
        // Add stmt to in memory model and reify it:
        Model model = ModelFactory.createDefaultModel();
        
        Store store = SDBFactory.connectStore("sdb.ttl") ;
        store.getTableFormatter().create() ;
        
        Model sdbModel = SDBFactory.connectDefaultModel(store) ;
        
        Statement stmt = model.createStatement(ResourceFactory
                                               .createResource("urn:test:t1"), RDFS.label, "foo1");
        
        model.add(stmt);
        ReifiedStatement rs = stmt.createReifiedStatement();
        rs.addProperty(ResourceFactory.createProperty("urn:property:foo"),"Foo");

        System.out.println("Model:");
        model.write(System.out, "N-TRIPLES");
        System.out.println();

        // now add in memory model to SDB model
        sdbModel.add(model); // <-------  CannotReifyException THROWN HERE!!!!!!!!!!!!!!!

        System.out.println("SDB Model:");
        sdbModel.write(System.out, "N-TRIPLES");
        System.out.println();

        
        System.out.println("DONE");
    }
}

/*
 * (c) Copyright 2010 Talis Systems Ltd.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */