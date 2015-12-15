package com.tuwien.semanticweb;


import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.VCARD;

import java.io.InputStream;

public class App {
    public static void main( String[] args ) {
        // some definitions
        String personURI    = "http://somewhere/JohnSmith";
        String familyName = "Smith";
        String givenName = "John";
        String fullName = givenName + " " + familyName;

        // create an empty Model
        Model model = ModelFactory.createDefaultModel();

        // create resource and add properties cascading style
        Resource johnSmith = model.createResource(personURI)
                .addProperty(VCARD.FN, fullName)
                .addProperty(VCARD.N, model.createResource()
                    .addProperty(VCARD.Given, givenName)
                    .addProperty(VCARD.Family, familyName));

        // list the statements in the model
        StmtIterator iter = model.listStatements();

        // print out the predicate, subject and object of each statement
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();
            Resource subject = stmt.getSubject();
            Resource predicate = stmt.getPredicate();
            RDFNode object = stmt.getObject();

            System.out.print(subject.toString());
            System.out.print(" " + predicate.toString() + " ");
            if(object instanceof Resource) {
                System.out.print(object.toString());
            } else {
                // object is a literal
                System.out.print(" \"" + object.toString() + "\"");
            }

            System.out.println(" .");
        }
        // Jena's "dumb" writer
        model.write(System.out);

        // now write the model in xml form to a file
        model.write(System.out, "RDF/XML-ABBREV");

        // now write the model in N-Triples form to a file
        model.write(System.out, "N-TRIPLES");

        System.out.println("Reading from RDF File...");

        readingRDF();

    }

    private static void readingRDF() {
        String inputFileName = "vc-db-1.rdf";

        Model model = ModelFactory.createDefaultModel();

        InputStream in = FileManager.get().open(inputFileName);

        if (in == null) {
            throw new IllegalArgumentException("File: " + inputFileName + " not found");
        }

        model.read(in, null);

        model.write(System.out);
    }

}
