
package com.santaanna.friendlyreader.readabilitytoolkit.toolkit.measures.swedish;

import com.santaanna.friendlyreader.readabilitytoolkit.toolkit.utils.GranskaReader;
import com.santaanna.friendlyreader.summarization.util.DocumentUtils;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import toolkit.utils.GranskaWriter;

/**
 * Calculates nominal ratio given a GranskaReader-object.
 * 
 * @author christian
 */
public class NR extends ReadabilityMeasure{
    private GranskaReader reader;

    public NR(GranskaReader granska){
        reader = granska;
    }
    
    @Override
    public double evaluateText(String text) {
        
        double nn=0;
        double pp=0;
        double pc=0;
        double pn=0;
        double ab=0;
        double vb=0;

        for(String p : reader.getPos()){
            if (p.equals("nn")) {
                nn++;
            }
            if (p.equals("pp")) {
                pp++;
            }
            if (p.equals("ab")) {
                ab++;
            }
            if (p.equals("vb")) {
                vb++;
            }
            if (p.equals("pn")) {
                pn++;
            }
            if (p.contains("pc")) {
                pc++;
            }
        }

        double nr =  (
            ((nn + pp + pc)) /
        ((pn + ab + vb)));

        return nr;
        
        
        
    }
    
    public static void main(String[] args){
        
        String text = DocumentUtils.readFile("temp.txt");
        
        // tag
        GranskaWriter.write(text,"tagged");
        
        GranskaReader gr = null;
        
        try {
            
            //read a previously tagged file
            gr = new GranskaReader("tagged");
            
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(NR.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // calculate nr. The GranskaReader-object contains POS-information
        // among other things.
        NR nr = new NR(gr);
        
        // print stuff
        System.out.println(nr.evaluateText(text));
            
        
    }
}
