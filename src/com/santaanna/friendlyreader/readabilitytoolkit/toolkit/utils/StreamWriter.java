

package toolkit.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christian
 */
public class StreamWriter extends Thread{
    private PrintWriter os;
    private InputStream is;

    public StreamWriter(InputStream is, PrintWriter os){
        this.is = is;
        this.os = os;
    }
    @Override
    public void run(){
        try{
            PrintWriter pw = null;
            if (os != null)
                pw = new PrintWriter(os);
            
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is,"ISO-8859-1"));
            
            String line=null;
            while ( (line = br.readLine()) != null)
            {
                pw.println(line);
            }
            pw.flush();
            pw.close();
            
        } catch (IOException ex) {
            Logger.getLogger(StreamWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
