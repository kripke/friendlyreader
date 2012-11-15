

package merge.summarization.easyreader.summarization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;
import merge.summarization.easyreader.reader.Summary;
import merge.summarization.easyreader.util.CSArrayTools;
import merge.summarization.easyreader.util.IndexHelper;
import merge.summarization.easyreader.vectors.SimpleVectorGraph;
import merge.summarization.easyreader.vectors.Trainer;
import merge.summarization.easyreader.vectors.VectorRank;
import merge.summarization.easyreader.vectors.WordVectors;

/**
 *
 * @author Kricke
 */
public class CogSum extends Summarizer{

    private ArrayList<String> stemmedList = new ArrayList();
    private WordVectors space;
    private float[] keywordWeights;
    private boolean error = false; // an error has occurred

    private Summary summary = new Summary();

    public CogSum(Settings settings){
        super(settings);
    }
    
    public CogSum(WordVectors wv, Settings s){
        super(s);
        space = wv;
    }

    public boolean getError(){
        return error;
    }

    @Override
    protected Summary rank(){

        if(space == null){
            if(!settings.getUseOutsideSpace()){
                ArrayList<String> wordList = document.getWords();
                Trainer trainer = new Trainer();
                trainer.train(wordList, settings);

                space = new WordVectors(trainer.getRI());
            }

            else{
                System.out.println("Using outside space...");
                space = new WordVectors(settings.getTrainingFile());
            }
        }

        if(settings.getShouldUpdate())
            space.addText(document.getWords());


        ArrayList<float[]> sentenceVectors =
            space.getSentenceVectors(document.getSentences(), settings.getRemoveDocumentVector());

        
        if(sentenceVectors.size() > 100){
            System.err.println("Warning: number of sentences = " +
                    sentenceVectors.size());
        }

        float[] ranks = null;

        if(this.settings.getUsePageRank()){
            SimpleVectorGraph g = new SimpleVectorGraph(sentenceVectors);
            
    //        System.out.println("Ranking " + g.getSize() + " sentences.\n");
    //        System.out.println("----------------------------------------");
            VectorRank pr = new VectorRank(g);
            pr.setIterations(settings.getPageRankIterations());
            pr.setFactorD(settings.getPageRankFactorD());
            pr.execute();

            ranks = pr.getGraph().getNodeWeights();

            summary.setSentenceComparisons(pr.getGraph().getEdges());
            summary.setSentenceRanks(ranks);
            summary.setSentences(document.getSentences());

            
            
            if(!pr.getSuccess())
                error = true;

        }
        else{
            ranks = new float[sentenceVectors.size()];

            for(int i = 0; i  < ranks.length; i++){
                ranks[i] = CSArrayTools.compare(sentenceVectors.get(i), 
                        space.getMeanDocumentVector());
            }


            //System.out.println(Arrays.toString(ranks));
        }
        /*
         * Lastly, sneak in keyword ranking before the ranking of sentences.
         * HAck!
         */
        keywordWeights = new float[document.getWords().size()];

        int i = 0;
        for(String w : this.document.getWords()){

            float d;
            if(!settings.getRemoveStopWords()){
//                d = CSArrayTools.compare(
//                        CSArrayTools.subtract(space.getWordContextVector(w),
//                        space.getMeanDocumentVector()), space.getMeanDocumentVector());
//                keywordWeights[i] = d;
            }
            else{
                float[] vec = space.getWordContextVector(w);
                if(vec != null){
                    d = CSArrayTools.compare(vec, space.getMeanDocumentVector());
                    keywordWeights[i] = d;
                }
            }
            
            i++;
        }

        summary.setWords(document.getWords());
        summary.setWordRanks(keywordWeights);
        
        return summary;
    }

    public Vector<String> retrieveKeywords(int percent, boolean appendValues) {
        Vector<String> retVec = new Vector<String>();

        ArrayList<IndexHelper> orderedValueList = new ArrayList<IndexHelper>();
        ArrayList valueList = new ArrayList();
        ArrayList wordList = new ArrayList();

        int i = 0;
        //sort values
        for(String s : this.document.getWords()){
            Float f = new Float(keywordWeights[i]);
            orderedValueList.add(new IndexHelper(f, i));
            valueList.add(f);
            wordList.add(s);
            i++;
        }

        if(orderedValueList.isEmpty()){
            //
        }

        Collections.sort(orderedValueList);


        float numWords =  (((float) (percent / 100d)) * orderedValueList.size());

        ArrayList<Integer> indexList = new ArrayList();

        for(int j = 0; j < numWords; j++){
            indexList.add(orderedValueList.get(j).getIndex());
        }
//        Collections.sort(indexList);
        
        for(Integer index : indexList){
            String s = wordList.get(index).toString().trim();// + ":" +
                        //valueList.get(index);

            if(!retVec.contains(s)){
                retVec.add(s);
            }
        }
        

        return retVec;
    }

}
