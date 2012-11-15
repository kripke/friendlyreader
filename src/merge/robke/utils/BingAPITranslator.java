/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package merge.robke.utils;

import com.memetix.mst.language.Language;
import com.memetix.mst.language.SpokenDialect;
import com.memetix.mst.translate.Translate;
import com.memetix.mst.speak.Speak;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.DataLine;

/**
 *
 * @author Rybing
 */
public class BingAPITranslator {
//implements ITranslator{
     
    public static final String MYID = 
            "417BC5DCED1017058BB12B3434B5B42D90D25BE6";
    
    public BingAPITranslator(){
        Translate.setKey(MYID);
    }

    public static void setKey(){
        Translate.setKey(MYID);
    }
    
    public String translateToSwedish(String text) {
        Language from = Language.AUTO_DETECT;
        Language to = Language.SWEDISH;;
        
        try {
            return Translate.execute(text, from, to);
        } catch (Exception ex) {
            Logger.getLogger(BingAPITranslator.class.getName()).log(Level.SEVERE, null, ex);
            System.out.print("Error: Translation failed");
            return null;
        }
    }
    public static void Speak(String text, String lang) throws Exception{
        SpokenDialect dialect = null;
        SpokenDialect[] dialects = SpokenDialect.values();
        
        for(int i = 0; i < dialects.length; i++){
            if(dialects[i].getName(Language.ENGLISH).equals(lang)){
                dialect = dialects[i];
            }
        }
        System.out.println("SPEAK DIA " + dialect +  " lang: " + lang);
        if(dialect==null) {
            return;
        }
        
        String sWavUrl = Speak.execute(text, dialect);
        
        //Print the URL returned from the Speak service
        System.out.println(sWavUrl);

        try{
            final URL waveUrl = new URL(sWavUrl);
            final HttpURLConnection uc = (HttpURLConnection) 
                    waveUrl.openConnection();
            playClip(new BufferedInputStream(uc.getInputStream()));
        } catch(Exception e) {
            System.out.println(e);
        }
    }
    
    private static void playClip(InputStream is) throws Exception {
        System.out.println("### PLAY ###");
        
        class AudioListener implements LineListener {
            private boolean done = false;
            
            @Override
            public synchronized void update(LineEvent event) {
                Type eventType = event.getType();
                if (eventType == Type.STOP || eventType == Type.CLOSE) {
                    done = true;
                    notifyAll();
                }
            }
            public synchronized void waitUntilDone() throws InterruptedException {
                while (!done) wait();
            }
        }
        
        AudioListener listener = new AudioListener();
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(is);

        try {
            Clip clip = null;
            try {
                DataLine.Info info = new DataLine.Info(Clip.class, 
                    audioInputStream.getFormat());  
                clip = (Clip)AudioSystem.getLine(info);
            } catch(Exception e){
                System.out.println("Mixer not supported, can't getClip " + 
                    e.getLocalizedMessage());
            }
              
            clip.addLineListener(listener);
            clip.open(audioInputStream);
            
            try {
                clip.start();
                System.out.println("clip.start()");
                listener.waitUntilDone();
            } catch(Exception e){
                System.out.println("clip failed to start with message; " + 
                        e.getMessage());
            } finally {
                clip.close();
            }
          } 
        
        finally {
            audioInputStream.close();
        }
        System.out.println("### END PLAY ###");
    }
}
