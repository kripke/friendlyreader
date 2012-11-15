/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package merge.summarization.easyreader.util;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author christian
 */
public class FileNameChange {
    
    public static void main(String[] args){
        // AP880217-0100.B17.system.html
        
        String pattern = "(.+)\\.[A-Z]([0-9]{2})\\.(.+)";
        String regex = "$1.$2.$3";
        
        ArrayList<File> files = DocumentUtils.traverse("systems");
        String out = "changed/";    
        
        for(File f : files){
            DocumentUtils.copyFile(f, out + 
                    f.getName().replaceAll(pattern, regex));
        }
        
        
    }
}
