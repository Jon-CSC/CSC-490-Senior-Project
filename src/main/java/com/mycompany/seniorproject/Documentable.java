package com.mycompany.seniorproject;

import java.util.Map;

/**
 * An interface for objects that can be rendered into a Firestore-friendly form.
 * @author ndars
 */
public interface Documentable {
    
    /**
     * Transform this object into Firestore document format.
     * 
     * A quick primer on creating Firestore documents in Java:
     * Firestore, being a NoSQL database, maintains data in documents. 
     * Documents have some number of fields, represented here as Strings.
     * Each field has a corresponding value, represented here as Objects.
     * For instance, a document maintaining building data might look something like this:
     * 
     * <pre>
     * {@code
     * {
     *     "number": 123,
     *     "street": "Example Street",
     *     "city": "Anytown",
     *     "occupants": {
     *             "John Doe",
     *             "Jane Doe"
     *     }
     * }
     * }
     * </pre>
     * 
     * In Java, this document would be a key-value map constructed like so:
     * 
     * <pre>
     * {@code 
     * Map<String, Object> doc = new HashMap<>();
     * doc.put("number", 123);
     * doc.put("street", "Example Street");
     * doc.put("city", "Anytown");
     * doc.put("occupants", Arrays.asList("John Doe", "Jane Doe"));
     * }
     * </pre>
     *      
     * @return a Firestore document representation of the object
     */
    public Map<String, Object> toDocument();
}
