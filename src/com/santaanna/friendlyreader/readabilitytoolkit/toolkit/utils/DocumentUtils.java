/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package toolkit.utils;

import toolkit.measures.swedish.LIX;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author christian
 */
public class DocumentUtils {
/**
     * Reads a text file from disk.
     * @param fileName The file name to be read.
     * @return A string containing the text in the file.
     */
    public static String readFile(String fileName){
        
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(fileName)));
            
            String line;
            StringBuilder text = new StringBuilder();
            
            while((line = reader.readLine()) != null){
                text.append(line).append("\n");
            }
            
            return text.toString();
            
        } catch (IOException ex) {
            Logger.getLogger(LIX.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
}
