

package merge.summarization.easyreader.vectors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import merge.summarization.easyreader.util.CSArrayTools;
import moj.lang.GenericTokenizer;
import moj.ri.RandomLabel;
import moj.ri.SparseDistributedMemory;

/**
 * Object for holding wordVector representaitons of words and sentences.
 * 
 * @author Christian
 */
public class WordVectors {

    private SparseDistributedMemory sdm = new SparseDistributedMemory();
    
    private int dimensionality;

    /* holds the word vectors*/
//    private HashMap<String, RandomLabel> wordMap;

    /* the avarage term vector, the central theme of the document*/
    private float[] meanDocumentVector;
    private  float[] totalVector;
    private int randomSeed;

    /**
     * Creates a mapping of words to sentenceVectors on top of a random index (xml)
     *
     * @param fileName
     */
    public WordVectors(String fileName){

//        wordMap = new HashMap();
        long t = System.currentTimeMillis();
        
//        try {

            // Load random index from xml and store it in the word map
//            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
//            IndexHandler handler = new IndexHandler();
            
            
            sdm.load(fileName);
            
//                BufferedReader reader = new BufferedReader(
//                        new FileReader(fileName + ".xml"));
//            InputSource source = new InputSource(reader);
//            
//            parser.parse(source, handler);
//            
//            for(RandomLabel rl : handler.randomIndex.values()){
//                wordMap.put(rl.getWord().toLowerCase(), rl);
////                System.out.println(rl.getWord());
//            }
            

            randomSeed = sdm.getSeed();
            
            // set dimensionality
            dimensionality = sdm.getDimensionality();

            // the total vector is the sum of all the words vectors
            totalVector = new float[dimensionality];

//            Set<Entry<String, RandomLabel>> s = wordMap.entrySet();
            Set<Entry<String, RandomLabel>> s = sdm.entrySet();
            Iterator it = s.iterator();
            int sum = 0;
            while(it.hasNext()){
                Map.Entry<String, RandomLabel> e = (Entry) it.next();
                int tf = (int) e.getValue().getTermFrequency();
                float[] cv = CSArrayTools.unit(e.getValue().getContext());
                //cv = CSArrayTools.scalMult(cv, tf);
                sum += tf;
//                System.out.println(cv);
                this.totalVector = CSArrayTools.addVector(totalVector, cv);
            }

            this.meanDocumentVector = CSArrayTools.avarage(this.totalVector, sdm.size());
            System.err.println("Time reading space: " + (System.currentTimeMillis()-t));
            
            //this.meanDocumentVector = CSArrayTools.unit(this.meanDocumentVector);

//        } catch (ParserConfigurationException ex) {
//            Logger.getLogger(WordVectors.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SAXException ex) {
//            Logger.getLogger(WordVectors.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public WordVectors(SparseDistributedMemory ri) {
        sdm = ri;
//        wordMap = new HashMap();
        dimensionality = ri.getDimensionality();
        long t = System.currentTimeMillis();
        totalVector = new float[dimensionality];
//        Set<Entry<String, RandomLabel>> s = ri.entrySet();
        Set<Entry<String, RandomLabel>> s = ri.entrySet();
        Iterator it = s.iterator();
        int sum = 0;
        while(it.hasNext()){
            Map.Entry<String, RandomLabel> e = (Entry) it.next();
            float[] cv = e.getValue().getContext();
//            float[] cv = CSArrayTools.unit(e.getValue().getContext());
            int tf = (int) e.getValue().getTermFrequency();
//            cv = CSArrayTools.scalMult(cv, 1 / tf);
            sum += tf;
            this.totalVector = CSArrayTools.addVector(this.totalVector, cv); 
//            wordMap.put(e.getValue().getWord().toLowerCase(), e.getValue());
        }
        //System.out.println(CSArrayTools.norm(totalVector));
        this.meanDocumentVector = CSArrayTools.unit(totalVector);
        //System.out.println(CSArrayTools.norm(totalVector));
//        this.meanDocumentVector = CSArrayTools.avarage(totalVector, sdm.size());
        //System.out.println(CSArrayTools.norm(meanDocumentVector));
        System.err.println("Time creating space: " + (System.currentTimeMillis()-t));
        

    }
    
    public void addText(ArrayList<String> words){
        sdm.addText(words.toArray(new String[words.size()]));
    }

    public int getRandomSeed() {
        return randomSeed;
    }

//    public float[] getTotalVector(){
//        return totalVector;
//    }

    public ArrayList<float[]> getSentenceVectors(ArrayList<String> sentenceList){
        return getSentenceVectors(sentenceList, true);
    }

    /**
     * Get the semantic sentence sentenceVectors of a document.
     *
     * @param sentenceList The document in the form of a list of sentences
     * @return A list with all sentence sentenceVectors
     */
    public ArrayList<float[]> getSentenceVectors(ArrayList<String> sentenceList, boolean removeDocumentVector){

        // tokenizer
        GenericTokenizer token = new GenericTokenizer();

        //build document from sentences
        StringBuilder document = new StringBuilder();
        for(String sentence: sentenceList){
            document.append(sentence).append("\n");
        }
        
        //calculate mean document vector
//        float[] documentVector = this.getUnbiasedDocumentVector
//                (token.getWords(document.toString().toLowerCase(), true));

            //a list holding a vector for each sentence
            ArrayList<float[]> sentenceVectors = new ArrayList();

            for(String sentence : sentenceList){

                // the total sentence vector
                float[] sentenceVectorTotal = new float[dimensionality];

                // an array of all the words in the sentence
                String[] sentenceTokens;
                sentenceTokens = token.getWords(sentence.toLowerCase(),true);


                Set<String> set = new HashSet<String>(Arrays.asList(sentenceTokens));

                int swc=0;
                for(String sentenceWord : sentenceTokens){


                    // stem the words to get them in the same form
                    // they were in training
//                    swedishStemmer stemmer = new swedishStemmer();
//                    stemmer.setCurrent(sentenceWord);
//                    stemmer.stem();

//                    RandomLabel rl =  this.wordMap.get(sentenceWord);
                    RandomLabel rl =  this.sdm.getRandomLabel(sentenceWord);

                    if(rl != null){
                        swc++;
                        
                        float[] wordVector = CSArrayTools.unit(rl.getContext());
                        
                        if(rl.getContext().length!=0){
                        
                            if(removeDocumentVector){

                                // subtract the avarage document vector from the
                                // sentence vector
    //                            float[] sentenceWordVector =  wordVector;
                                    float[] sentenceWordVector =  CSArrayTools.subtract
                                    (wordVector, this.meanDocumentVector);

    //                                float v =  CSArrayTools.compare(documentVector, sentenceWordVector);
    //
    //
    //                                float[] docV;
    //                                    docV = CSArrayTools.scalMult(sentenceWordVector,v);

                                    //add the word vector to the total sentence vector
                                sentenceVectorTotal = CSArrayTools.addVector
                                        (sentenceWordVector, sentenceVectorTotal);
    //                            sentenceVectorTotal = CSArrayTools.addVector
    //                                    (docV, sentenceVectorTotal);
                            }

                            else{
                                //add the word vector to the total sentence vector
                                sentenceVectorTotal = CSArrayTools.addVector
                                        (wordVector, sentenceVectorTotal);
                            }
                        }
                    }
                    
//                    else{
//                        
//                        System.out.println("contains not: " + sentenceWord);
//                    }
                }


                // the avarage sentence vector

//                if(swc != 0){
                    float[] avarageSentenceVector = CSArrayTools.avarage
                            (sentenceVectorTotal, swc);
    //                avarageSentenceVector = CSArrayTools.unit(sentenceVectorTotal);
    //                System.out.println(Arrays.toString(docV));
                    sentenceVectors.add(avarageSentenceVector);
//                }
//                 else
//                     sentenceVectors.add(null);
            }

            return sentenceVectors;
        }

    public float[] getWordContextVector(String w){
//        RandomLabel rl = this.wordMap.get(w);
        RandomLabel rl = this.sdm.getRandomLabel(w);
        return (rl!=null) ? rl.getContext() : null;
    }

    /**
     * Get the global mean document vector, that is, the avarage
     * of the entire space.
     * @return An avarage vector of all the terms in the space
     */
    public float[] getMeanDocumentVector(){
        return this.meanDocumentVector;
    }

    /**
     * Build an avarage document vector of the specified words.
     *
     * @param words The words to build a document vector from
     * @return An avarage document vector
     */
    public float[] getMeanDocumentVector(String[] words){
        float[] mean = new float[sdm.getDimensionality()];
        int wc = 0; // word count
        for(String w : words){
//            RandomLabel rl = this.wordMap.get(w.toLowerCase());
            RandomLabel rl = this.sdm.getRandomLabel(w.toLowerCase());
            if(rl != null){
                float[] normalized = CSArrayTools.unit(rl.getContext());
                if(normalized.length != 0){
                    mean = CSArrayTools.addVector(normalized, mean);
                    wc++;
                }
//                else
//                    System.out.println(w);
            }
        }
        mean = CSArrayTools.avarage(mean, sdm.size());

        return mean;
    }


    /**
     * Get a document vector with the avarage subtracted from each word.
     *
     * @param words
     * @return
     */
    public float[] getUnbiasedDocumentVector(String[] words){

        float[] mean = getMeanDocumentVector(words);

        float[] docVec = new float[dimensionality];
        int wc = 0;
        for(String w : words){
//            RandomLabel rl = this.wordMap.get(w.toLowerCase());
            RandomLabel rl = this.sdm.getRandomLabel(w.toLowerCase());
            if(rl != null){
                docVec = CSArrayTools.addVector(
                    CSArrayTools.subtract(CSArrayTools.unit(rl.getContext()),this.meanDocumentVector),
                        docVec);
                wc++;
            }
        }
//        System.out.println("!!");
//        System.out.println(Arrays.toString(docVec));
        //docVec = CSArrayTools.avarage(docVec, wc);
//        System.out.println(Arrays.toString(docVec));
//        System.out.println("??");

        return docVec;

    }

    public static String vectorMappingToString(HashMap map){
            StringBuilder sb = new StringBuilder();
            Set<Entry<String, float[]>> s = map.entrySet();
            Iterator it = s.iterator();
            while(it.hasNext()){
                Map.Entry<String, float[]> e = (Entry) it.next();
                sb.append(e.getKey());
                sb.append("=[");
                for(float f : e.getValue()){
                    sb.append(f).append(",");
                }
                sb.append("]");
            }
            return sb.toString();
    }
//    public HashMap<String, float> compareToDocument(HashMap map){
//        HashMap retMap = new HashMap();
//
//        Set<Entry<String, float[]>> s = map.entrySet();
//        Iterator it = s.iterator();
//        while(it.hasNext()){
//            Map.Entry<String, float[]> e = (Entry) it.next();
//            retMap.put(e.getKey(), CSArrayTools.compare(e.getValue(), meanDocumentVector));
//        }
//        return retMap;
//    }
//
//    public float getSumCorr(HashMap map){
//        HashMap m = compareToDocument(map);
//        float f = 0;
//        Set<Entry<String, float>> s = m.entrySet();
//        Iterator it = s.iterator();
//        while(it.hasNext()){
//            Map.Entry<String, float> e = (Entry) it.next();
//            f += e.getValue();
//        }
//
//        return f;
//    }

//    public float testPrediction(String w1, String w2){
//        float[] f = new float[dimensionality];
//        Arrays.fill(f, 0);
//
//        RandomLabel templ1 = wordMap.get(w1);
//        RandomLabel templ2 = wordMap.get(w2);
//        float temp = 0;
//
//        if(templ1 != null && templ2 != null){
//            templ1 = OrderPermutation.permutate(templ2, -1);
//
//            for(int i = 0; i < templ1.getPositivePositions().length; i++){
//                f[templ1.getPositivePositions()[i]] = -1;
//            }
//            for(int i = 0; i < templ1.getNegativePositions().length; i++){
//                f[templ1.getNegativePositions()[i]] = 1;
//            }
//
//            temp = CSArrayTools.compare(templ2.getContext(), f);
//
//        }
//        return temp;
//    }

//    public String predict(String string) {
//        float[] f = new float[dimensionality];
//        Arrays.fill(f, 0);
//
//        RandomLabel templ = wordMap.get(string);
//        
//        if(templ != null){
//            templ = OrderPermutation.permutate(templ, -1);
////            System.out.println("Predicting on permutation!");
////            System.out.println(templ.getWord());
//
//            for(int i = 0; i < templ.getPositivePositions().length; i++){
////                System.out.println("pos");
////                System.out.println(templ.getPositivePositions()[i]-1);
//
//                f[templ.getPositivePositions()[i]] = -1;
//            }
//            for(int i = 0; i < templ.getNegativePositions().length; i++){
//                f[templ.getNegativePositions()[i]] = 1;
//            }
//
//
//
//            float h = 0;
//            int hi = 0;
//            int i = 0;
//
//            for(RandomLabel fi : this.wordMap.values()){
//                float temp = CSArrayTools.compare(fi.getContext(), f);
////                System.out.println(wordMap.keySet().toArray()[i]);
////                System.out.println(temp);
//                if (temp > h){
//                    h = temp;
//                    hi = i;
//                }
//                i++;
//            }
//            return(String) (wordMap.keySet().toArray()[hi]);
//        }
// else
//     return null;
//
//    }

//        private RandomLabel permutate(RandomLabel label, int n){
//
//        int dim = label.getDimensionality();
//
//        RandomLabel rl = label;
//        int[] ones = label.getNegativePositions();
//        for(int i = 0; i < ones.length; i++){
//             ones[i] += n;
//            if(ones[i] > dim - 1){
//                ones[i] %= dim;
//            }
//            else if(ones[i] < 0){
//                ones[i] = dim + n;
//            }
//        }
//
//        ones = label.getPositivePositions();
//
//        for(int i = 0; i < ones.length; i++){
//            ones[i] += n;
//            if(ones[i] > dim - 1){
//                ones[i] %= dim;
//            }
//            else if(ones[i] < 0){
//                ones[i] = dim + n;
//            }
//
//        }
//        return label;
//    }

    public String[] getMap(){
        return this.sdm.keySet().toArray(new String[this.sdm.size()]);
    }
}
