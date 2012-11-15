
package com.santaanna.friendlyreader.summarization.summarizer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import com.santaanna.friendlyreader.summarization.evaluation.coherence.StanfordCoreEvaluator;
import com.santaanna.friendlyreader.summarization.reader.Document;
import com.santaanna.friendlyreader.summarization.reader.Summary;
import com.santaanna.friendlyreader.summarization.reader.SummaryReader;
import com.santaanna.friendlyreader.summarization.Settings;
import com.santaanna.friendlyreader.summarization.util.DocumentUtils;
import com.santaanna.friendlyreader.summarization.vectors.CorefGraph;

/**
 *
 * @author christian
 */
public class CorefSum extends Summarizer{
    private String corefFile;
    private ArrayList sents;
    
    public CorefSum(Settings s){
        super(s);
    }

    @Override
    protected Summary rank() {
        StanfordCoreEvaluator sce = new StanfordCoreEvaluator();
        sce.parse(corefFile);
        String str = "";
        sents = new ArrayList();
        for(ArrayList<String> sent : sce.getSentenceWordList()){
            
            for(String w : sent){
                str += w+" ";
            }
            str += ".";
            sents.add(str);
            str = "";
        }
        
//        Document d = new Document();
//        d.read(sents);
        
        this.document = new Document();
        this.document.read(sents);
        
        CorefGraph g = new CorefGraph(
                sce.getSentenceWordList().size(),
                sce.getCorefObjectList());
        
        float[] ranks = g.getNodeWeights();
//        System.out.println(Arrays.toString(ranks));
        
        Summary summary = new Summary();
//        summary.setSentenceComparisons(new float[100][100]);
        summary.setSentenceRanks(ranks);
        summary.setSentences(sents);

        
        return summary;
    }
    
    public static void main(String[] args){
        for(File f : DocumentUtils.traverse("corefs2012/clean_all")){
            
            CorefSum sum = new CorefSum(new Settings());
            sum.setCorefFile("corefs2012/clean_all/"+f.getName());
            Summary s = sum.buildSummary();
            SummaryReader reader = new SummaryReader(s);
//            ArrayList<Integer> list = reader.buildIndexListNumSentences(12);
//            
//            System.out.println(list);
            DocumentUtils.save(reader.retrieveSummaryNumWords(100, false), "summaries/cleanAll_und/"+f.getName());
        }
    }

    private void setCorefFile(String string) {
        this.corefFile = string;
    }
    
}
