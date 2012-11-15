
package merge.summarization.easyreader.summarization;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import merge.summarization.easyreader.evaluation.coherence.StanfordCoreEvaluator;
import merge.summarization.easyreader.reader.Document;
import merge.summarization.easyreader.reader.Summary;
import merge.summarization.easyreader.vectors.CorefGraph;

/**
 *
 * @author christian
 */
public class PortableCorefSum extends Summarizer{
    private String corefFile;
    private ArrayList sents;
    
    public PortableCorefSum(Settings s){
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
//        for(File f : DocumentUtils.traverse("corefs2012/clean_all")){
//            
//            PortableCorefSum sum = new PortableCorefSum(new Settings());
//            sum.setCorefFile("corefs2012/clean_all/"+f.getName());
//            Summary s = sum.buildSummary();
//            SummaryReader reader = new SummaryReader(s);
////            ArrayList<Integer> list = reader.buildIndexListNumSentences(12);
////            
////            System.out.println(list);
//            DocumentUtils.save(reader.retrieveSummaryNumWords(100, false), "summaries/cleanAll_und/"+f.getName());
//        }
    }

    public void setCorefFile(String string) {
        this.corefFile = string;
    }
    
}
