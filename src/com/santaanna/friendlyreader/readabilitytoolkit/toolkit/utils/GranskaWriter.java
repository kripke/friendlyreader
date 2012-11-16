

package toolkit.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Requires wine (not expecting Windows..)
 * 
 * @author Christian Smith
 */
public class GranskaWriter {

    public static void write(String text){
        write(text, "tagged.txt");
    }
    
    public static void write(String text, String tagfile){
        write(text, tagfile, false);
    }
    
    public static void write(String text, String tagfile, boolean verbose){

            String temp = ".temp";

            try {
                PrintWriter pw = null;
                pw = new PrintWriter(".iso"+temp, "iso-8859-1");
                pw.append(text);
                pw.close();
                //save iso-file

                System.out.println("Tagging...");
                String cmd = "wine granska/tagg.exe " +
                        "-B " + "-l " + "granska/lex " +
                        ".iso"+temp;
                
                Runtime r = Runtime.getRuntime();

                Process p = r.exec(cmd);

                StreamWriter errstream = new StreamWriter(p.getErrorStream(), 
                        new PrintWriter(System.out));

                PrintWriter fos = new PrintWriter(tagfile,"iso-8859-1");
                StreamWriter ostream = new StreamWriter(p.getInputStream(), fos);
                
                if(verbose)
                    errstream.start();
                ostream.start();

                
                while (errstream.isAlive() || ostream.isAlive()) {
                    /*wait*/
                }
                
                int eVal = -100000000;

                try {
                    eVal = p.waitFor();
                } catch (InterruptedException ex) {
                    Logger.getLogger(GranskaWriter.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                if(eVal != 0)
                    System.err.println("Error while granska..\n Check paths.");
                fos.flush();
                fos.close();
                p.destroy();
                System.out.println("Done!");
 
            } catch (IOException ex) {
                Logger.getLogger(GranskaWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
