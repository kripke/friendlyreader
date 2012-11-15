/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package merge.summarization.easyreader.evaluation.coherence;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import merge.summarization.easyreader.reader.Summary;
import merge.summarization.easyreader.reader.SummaryReader;
import merge.summarization.easyreader.summarization.PortableCorefSum;
import merge.summarization.easyreader.summarization.Settings;
import merge.summarization.easyreader.util.DocumentUtils;

/**
 *
 * @author christian
 */
public class PortableStanfordCoref {
    
    private static StanfordCoreNLP corefPipeLine;
    
    public PortableStanfordCoref(){
        System.out.println("Loading...");

        Properties corefProps = new Properties();

        corefProps.put("annotators", 
                "tokenize, ssplit, pos, lemma, ner, parse, dcoref");

        corefPipeLine = new StanfordCoreNLP(corefProps);
        
        System.out.println("Done loading.");

    }
    
    /**
     * Get the parsed text
     * @param text
     * @return 
     */
    public String process(String text){
        try {
            Annotation document = new Annotation(text);
            
            // run all Annotators on this text
            corefPipeLine.annotate(document);
            
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            
            try {
                corefPipeLine.xmlPrint(document, os);
                os.flush();
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(PortableStanfordCoref.
                        class.getName()).log(Level.SEVERE, null, ex);
            }
            
            BufferedReader reader = new BufferedReader(new 
                    InputStreamReader(new ByteArrayInputStream(os.toByteArray())));
            
            String t = "";
            String line = "";
            while((line = reader.readLine()) != null){
                t+=line+"\n";
            }
            reader.close();
            
            
            return t;
        } catch (IOException ex) {
            Logger.getLogger(PortableStanfordCoref.class.getName())
                    .log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static void main(String[] args){
        PortableStanfordCoref psf = new PortableStanfordCoref();
        String text = DocumentUtils.readFile("test");
        
        
        String out = psf.process(text);
        DocumentUtils.save(out, ".parse");
        
        PortableCorefSum sum = new PortableCorefSum(new Settings());
        sum.setCorefFile("parse");
        
        Summary s = sum.buildSummary();
        SummaryReader reader = new SummaryReader(s);
        
        ArrayList<Integer> list = reader.buildIndexListNumSentences(4);
        System.out.println(list);
        
    }
    
        
}