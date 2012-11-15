/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package merge.pdfstod.CogsumApplet;

import java.util.Vector;

/**
 *
 * @author Allan
 * Ett textblock är antingen en COSArray eller en COSString.
 *
 */

public class SETextBlock {

    // Sidan där textblocket finns lagras numera!?!?
    public int pagenr; // Nyinförd variabel
    public int index; // Index för textblocket. Den position i Token som den har.
    // På den aktuella sidan. Index i vector = implicit! Effektivare?
    public boolean IsArray; // True om TextBlocket är en array.
    public Vector<SEArrayIndKlass> Carray; // Vektor med SECosstr och objekt.
    public SECosstr cstr; // Cstr om det inte är en Carray. En Cstr är en Cosstring.
    
    public SETextBlock( ) {

    }
}
