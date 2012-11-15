package merge.robke.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christian
 */
public class etStreamWriter extends Thread {
    private PrintWriter os;
    private InputStream is;

    public etStreamWriter(InputStream is, PrintWriter os){
        this.is = is;
        this.os = os;
    }
    @Override
    public void run(){
        try{
            PrintWriter pw = null;
            if (os != null)
                pw = new PrintWriter(os);
            InputStreamReader isr = new InputStreamReader(is, "iso-8859-1");
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null)
            {
                if (pw != null)
                    pw.println(line);
            }
            if (pw != null)
                pw.flush();
        } catch (IOException ex) {
            Logger.getLogger(etStreamWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
/*Ersatt 2012-02-27 med dammig cogfux del rad 85-115
    private String outputFileName;
    private InputStream is;

    public etStreamWriter(InputStream is, String outputFileName) {
        this.is = is;
        this.outputFileName = outputFileName;
    }

    @Override
    public void run() {
        
        
        try {
            PrintWriter pw = new PrintWriter(outputFileName, "ISO-8859-1");
            
            InputStreamReader isr = new InputStreamReader(is, "ISO-8859-1");
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                System.err.println("line = " + line);
                pw.println(line);
            }
            pw.flush();
            br.close();
            pw.close();
        } catch (IOException ex) {
            Logger.getLogger(etStreamWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
}
