
package com.santaanna.friendlyreader.summarization.multi;

import java.util.ArrayList;
import com.santaanna.friendlyreader.summarization.reader.Document;
import com.santaanna.friendlyreader.summarization.reader.Summary;
import com.santaanna.friendlyreader.summarization.reader.SummaryReader;
import com.santaanna.friendlyreader.summarization.summarizer.CogSum;
import com.santaanna.friendlyreader.summarization.Settings;


/**
 * Baby steps for multi document summarization
 * @author christian
 */
public class MultiTest {
    
    public static void main(String[] args){
        
        Settings s = new Settings();
        s.setUseOutsideSpace(false);
        s.setTrainingFile("brownut");
        s.setRemoveStopWords(false);
        CogSum cs = new CogSum(s);
        
        Document doc = new Document();
        doc.readFile("335_Paper.pdf");
        
        cs.setText(doc);
        
        Summary sum =  cs.buildSummary();
        
        SummaryReader reader = new SummaryReader(sum);
        
        ArrayList<Integer> indices = reader.buildIndexListNumSentences(15);
        for(Integer i : indices){
            System.out.println(doc.getSentences().get(i));
        }
//        System.out.println("");
//        System.out.println(reader.retrieveSummaryNumSent(5));
        
    }
}
