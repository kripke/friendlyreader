

package merge.summarization.easyreader.reader;

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
public class StreamWriter extends Thread{
    private OutputStream os;
    private InputStream is;

    public StreamWriter(InputStream is, OutputStream os){
        this.is = is;
        this.os = os;
    }
    @Override
    public void run(){
        try{
            PrintWriter pw = null;
            if (os != null)
                pw = new PrintWriter(os);
            InputStreamReader isr = new InputStreamReader(is);
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
            Logger.getLogger(StreamWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
