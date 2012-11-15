/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package merge.robke.utils;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.ByteOrderMarkDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 *
 * @author Keski
 */
public class CharsetDetector {
    
    /**
     * Pass a text file and get back a suggested charset. 
     * @param document
     * @return 
     */
    static public Charset getCharset(File document) {
        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
        // Add the implementations of info.monitorenter.cpdetector.io.ICodepageDetector: 
        // This one is quick if we deal with unicode codepages: 
        detector.add(new ByteOrderMarkDetector());
        // The first instance delegated to tries to detect the meta charset attribut in html pages.
        detector.add(new ParsingDetector(true)); // be verbose about parsing.
        // This one does the tricks of exclusion and frequency detection, if first implementation is 
        // unsuccessful:
        detector.add(JChardetFacade.getInstance()); // Another singleton.
        detector.add(ASCIIDetector.getInstance()); // Fallback, see javadoc.
        
        try {
            Charset charset = null;
            charset = detector.detectCodepage(document.toURL());
            return charset;
        } catch (IOException ex) {
            return null;
        }
    }
}
