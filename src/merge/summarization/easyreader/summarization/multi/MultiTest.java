
package merge.summarization.easyreader.summarization.multi;

import java.util.ArrayList;
import merge.summarization.easyreader.reader.Document;
import merge.summarization.easyreader.reader.Summary;
import merge.summarization.easyreader.reader.SummaryReader;
import merge.summarization.easyreader.summarization.CogSum;
import merge.summarization.easyreader.summarization.Settings;


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
