
package com.santaanna.friendlyreader.summarization.evaluation.coherence;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import com.santaanna.friendlyreader.summarization.util.DocumentUtils;
import com.santaanna.friendlyreader.summarization.vectors.WordVectors;
import moj.util.FileTools;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Evaluator based on StanfordCoreNLP coreference package.
 * 
 * Performs the full chain of necessary operations, including POS-tagging, 
 * parsing and co-reference solution. The results from parsing will be saved 
 * in xml files (as defined by the Stanford package) and then read immediately, 
 * parsed and peformed upon various calculations depicting co-reference based
 * coherence.
 * 
 * These include COH-metrix local/global noun overlap measures and a variant of 
 * Nenkova et al.'s co-reference coherence measure. 
 * 
 * @author Christian
 */
public class StanfordCoreEvaluator {

    private static Integer findPart(int size, int PARTS, int head) {
        float f = (float)head/((size-(size%PARTS))/PARTS);
        int i = (int) Math.ceil(f);
        return i;
    }
    
    Coref currentCoref;
    
    int[][] corefMatr;
    
    //evaluation results
    private double corefGlobalUnweighted = 0;
    private double corefLocal = 0;
//    private double nounCorefLocal = 0;
//    private double nounCorefGlobalUnweighted = 0;
    private int globalNounOverlap = 0;
    private int localNounOverlap = 0;
    
    // all sentences belonging to a co-reference
    ArrayList<Integer> sentenceList = new ArrayList(); 
    
    ArrayList<String> posList = new ArrayList(); 
    ArrayList<String> tokenList = new ArrayList(); 
    ArrayList<Integer> headList = new ArrayList(); 
    
    ArrayList<Integer> reprMentionSentences = new ArrayList();
    ArrayList<ArrayList<String>>  sentencePosList = new ArrayList();
    ArrayList<ArrayList<String>>  sentenceWordList = new ArrayList();
    ArrayList<ArrayList<Integer>> sentenceCorefList = new ArrayList();
    ArrayList<Double> corefsGlobal = new ArrayList();
    ArrayList<Double> corefsLocal = new ArrayList();
    
    //counters
    private int numSentencesGlobal = 0; // number of sentences in the text
    private double corefSum = 0; // number of mentions of the entire text
    private double adjCorefSum = 0; //number of adjacent corefs
    
    private static WordVectors sdm;
    SAXParserFactory factory = SAXParserFactory.newInstance();
            
    SAXParser parser;
    
    StringBuilder outputCSV = new StringBuilder();
    String header = "text\t"
                + "folder\t"
                + "coref_glob\t"
                + "coref_loc\t"
                + "adjCorefs\t"
                + "noun_ovlp_loc\t"
                + "noun_ovlp_glob\t"
                + "corefs\n";
                ArrayList<Coref> corefObjectList = new ArrayList();

    public ArrayList<Coref> getCorefObjectList(){
                    return corefObjectList;
    }
    public StanfordCoreEvaluator(){
        try {
            outputCSV.append(header);
            parser = factory.newSAXParser();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(StanfordCoreEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(StanfordCoreEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void reset(){
        
        //evaluation results
        corefGlobalUnweighted = 0;
        corefLocal = 0;
        globalNounOverlap = 0;
        localNounOverlap = 0;

        //lists
        sentenceList = new ArrayList(); 
        posList = new ArrayList(); 
        tokenList = new ArrayList(); 
        headList = new ArrayList(); 
        reprMentionSentences = new ArrayList();
        sentencePosList = new ArrayList();
        sentenceWordList = new ArrayList();
        sentenceCorefList = new ArrayList();
        corefsGlobal = new ArrayList();
        corefsLocal = new ArrayList();
        corefObjectList = new ArrayList();
        
        

        //counters
        numSentencesGlobal = 0; // number of sentences in the text
        corefSum = 0; // number of mentions of the entire text
        adjCorefSum = 0; //number of adjacent corefs
    }
                
    public double getCorefGlobal(){
        return this.corefGlobalUnweighted;
    }
    
    public double getCorefLocal(){
        return this.corefLocal;
    }
    
    public ArrayList<Integer> getReprMentionSentences(){
        return this.reprMentionSentences;
    }
    
    public double getAdjCorefSum() {
        return adjCorefSum;
    }

    public double getCorefSum() {
        return corefSum;
    }

    public int getNumSentencesGlobal() {
        return numSentencesGlobal;
    }

    public int getGlobalNounOverlap() {
        return globalNounOverlap;
    }

    public int getLocalNounOverlap() {
        return localNounOverlap;
    }
    
    /**
     * Upon creation, StanfordCoreEvaluatortakes care of parsing the xml files
     * containing the co-reference information.
     * 
     * @param input 
     */
    public void parse(String input){
        
        reset();
        
        try {
            

            
            /**
             * Parser handler.
             */
            DefaultHandler handler = new DefaultHandler(){
                
                //booleans
                private boolean coref = false;
                private boolean sent = false;
                private boolean sentences = false;
                private boolean pos = false;
                private boolean word = false;
                private boolean head = false;
                private boolean token = false;
                
                private boolean reprMention = false;
                
                Integer sentenceIndex = 0;
                
                private int[] corefCounts;


                @Override
                public void startDocument(){
                }
                

                @Override
                public void startElement(String uri, String localName,
                String qName, Attributes attributes) throws SAXException {
                    
                    if(qName.equals("mention")){
                        if(attributes.getIndex("representative") == 0){
                        reprMention = true;
                        }
                    }
                    
                    if(qName.equals("sentences")){
                        sentences = true;
                    }
                    
                    if(qName.equalsIgnoreCase("coreference")){
                        coref = true;
                    }
                    
                    if(qName.equals("sentence")){
                        sent = true;
                        if(sentences){
                            numSentencesGlobal++;
                        }
                    }
                    
                    if(qName.equals("POS")){
                        pos = true;
                    }
                    
                    if(qName.equals("word")){
                        word = true;
                    }
                    
                    if(qName.equals("head")){
                        head = true;
                    }
                    
                    if(qName.equals("tokens")){
                        tokenList = new ArrayList();
                        posList = new ArrayList();
                        
                    }
                    
                    if(qName.equals("token")){
                        token = true;
                    }
                }
                
                
                @Override
                public void characters(char[] ch, int start, int length) 
                        throws SAXException {
                    
                    
                    
                    if(coref && sent){  
                        String str = new String(ch, start, length);
                        sentenceIndex = Integer.parseInt(str);
                        
//                        System.out.println(sentenceIndex);
//                        System.out.println(Arrays.toString(corefCounts));
                        corefCounts[sentenceIndex]++;
                        if(!sentenceList.contains(sentenceIndex)){
                            sentenceList.add(sentenceIndex);
                        }
                        
                        
                        if(reprMention){
                            currentCoref = new Coref(sentenceIndex);
                            if(!reprMentionSentences.contains(sentenceIndex))
                                reprMentionSentences.add(sentenceIndex);
                        }
                        
                        currentCoref.addSentenceIndex(sentenceIndex);
//                        else
//                            headList.remove(headList.size()-1);
                    }
                    
                    if(word){
                        String w = new String(ch,start,length);
                        tokenList.add(w);
                       
                    }
//                    
                    if(pos){
                        String str = new String(ch, start, length);
                        posList.add(str);   
                    }
//                    
//                    if(word){
//                        String str = new String(ch, start, length);
//                        tokenList.add(str);
//                    }
//                    
//                    if(coref && head){
//                        Integer headI = Integer.parseInt(new String(ch, start, length));
//                        headList.add(headI);
//
//                    }

                }
               

                @Override
                public void endElement(String uri, String localName,
                        String qName) throws SAXException {
                    
                    if(qName.equals("head")){
                        head = false;
                    }
                    
                    if(qName.equals("token")){
                        token = false;
                    }
                    
                    if(qName.equals("tokens")){
                        sentenceWordList.add(tokenList);
                        sentencePosList.add(posList);
                    }
                    
                    if(qName.equals("POS")){
                        pos = false;
                    }
                    if(qName.equals("word")){
                        word = false;
                    }
                    if(qName.equals("sentences")){
                        sentences = false;
                        corefMatr = new int[sentenceWordList.size()][];
                        this.corefCounts = new int[sentenceWordList.size()+1];
                    }
                    if(qName.equals("sentence")){
                        sent = false;
                    }
                    if(qName.equals("mention")){
                        reprMention = false;
                        
                    }
                    if(qName.equals("coreference")){
                        coref = false;
                        sentenceCorefList.add(sentenceList);
                        corefObjectList.add(currentCoref);
                        
                        //number of co-referring mentions of each coreference category
                        // 5 + 4 + 3 + 2 + 1, or  n (n-1) / 2
                        // calculates the number of pairs from the number
                        // of corefs.
                        double d = ((double)sentenceList.size()-1) * 
                                (double) sentenceList.size() / 2;
                        // whatif the same pair appears more than once?
                        corefSum += sentenceList.size();
//                        corefSum += d;
                        
                        
                        //count adjacent corefs
                        Integer prev = -1;
                        for(Integer i : sentenceList){
                            
                            //the first sentence has nothing before
                            if(prev>-1){
                                // if difference in index between the sentences
                                // is one, they are adjacent
                                if(i-prev == 1){ 
                                    adjCorefSum++;
                                }
                            }
                            
                            prev = i;
                        }
                        sentenceList = new ArrayList();
                        
                    }
                }
                
                @Override
                public void endDocument(){
                    
                    corefGlobalUnweighted = corefSum / (numSentencesGlobal * 
                            (numSentencesGlobal-1d) / 2);
                    
                    corefLocal = (double)adjCorefSum / (numSentencesGlobal-1);
                    
                    localNounOverlap = 0;
                    globalNounOverlap = 0 ;
                    
                     //evaluate coh-metrix "coreference"
                    for(int i = 0; i < sentencePosList.size(); i++){
                        
                        ArrayList<String> 
                                currentPosList = sentencePosList.get(i);
                        
                        ArrayList<String> 
                                currentWordList = sentenceWordList.get(i);
                        
                        
                        
                        boolean br = false;
                        
                        ArrayList<Integer> localTried = new ArrayList();
                        
                        for(int j = 0; j < currentPosList.size(); j++){
                            
                            String currentPos = currentPosList.get(j);
                            
                            if(currentPos.contains("NN")){
                                String currentWord = currentWordList.get(j);
                                
                                //compare to previous sentence
                                ArrayList<String> prevWordList;
                                if(i > 0 != br){
                                    prevWordList = sentenceWordList.get(i-1);
                                    if(prevWordList.contains(currentWord)){
                                        localNounOverlap++;
                                        br = true;
                                    }
                                }
                                
                                ArrayList<String> wordList;
                                
                                for(int k = i; k < sentencePosList.size(); k++){
                                    if(k != i && !localTried.contains(k)){
                                        wordList = sentenceWordList.get(k);
                                        if(wordList.contains(currentWord)){
                                            localTried.add(k);
                                            globalNounOverlap++;
                                        }
                                        
                                    }
                                }
                            }
                        }
                    }
//                    System.out.println(Arrays.toString(corefCounts));
                }
                
                
            };
            
            File f = new File(input);
            if(!f.isDirectory() && !f.getName().startsWith(".")){
                parser.parse(input, handler);
                parser.reset();
            }
            
                
            
        } catch (Exception ex) {
            System.err.println("file...");
            ex.printStackTrace();
        }
    }
    
    public void annotate(String input){
            
            Properties corefProps = new Properties();
            
            corefProps.put("annotators", 
                    "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
            
            StanfordCoreNLP corefPipeLine = new StanfordCoreNLP(corefProps);

            String summaryDocPath = input;
            String corefEvalPath = "corefs2012/"+summaryDocPath+"/";

            DocumentUtils.mkdirs(corefEvalPath);


//            File[] summaryDocs = new File(summaryDocPath).listFiles();
            ArrayList<File> summaryDocs = DocumentUtils.traverse(summaryDocPath);
            for(File f : summaryDocs){
                
            try {
                
                System.out.println("Beginning on " + f.getName());

                // read some text in the text variable
//                String text = FileTools.readFile2String(summaryDocPath + "/" + 
//                        f.getName());
                String text = FileTools.readFile2String(f.getPath());

                text = text.replaceAll("[\n´`_;:/\\(\\\")]+", " ").
                    replaceAll("\\.+", ".").
                    replaceAll(" +", " ").
                    replaceAll("'' ", " ").
                        replaceAll(" ''+", " ").
                        replaceAll("(\\. )+", ". ").
                        replaceAll("' ", " ");

                Annotation document; 

                document = new Annotation(text);


                // run all Annotators on this text
                corefPipeLine.annotate(document);
                PrintWriter pw = new PrintWriter(
                        corefEvalPath + f.getName());
                corefPipeLine.xmlPrint(document,  pw);
                pw.close();
                
            } catch (IOException ex) {
                Logger.getLogger(StanfordCoreEvaluator.class.getName()).log(Level.SEVERE, null, ex);
            }
                
            }
    }
    
    /**
     * Reads all files in the specified input folder, parses them and saves the
     * result in "corefs2012/" + input. The result of the evaluation will
     * be saved in input + ".csv". 
     * 
     * @param input The folder containing text files that should be evaluated.
     */
    public String evaluate(String input){
        

        String summaryDocPath = input;
        String corefEvalPath = "corefs2012/"+summaryDocPath+"/";

        DocumentUtils.mkdirs(corefEvalPath);

        File[] summaryDocs = new File(summaryDocPath).listFiles();
        
        for(File f : summaryDocs){
            System.out.println("Beginning on " + f.getName());

            try{

                parse(corefEvalPath + f.getName());

                outputCSV.append(f.getName()).append("\t")
                        .append(f.getParent()).append("\t")
                        .append(getCorefGlobal()).append("\t")
                        .append(getCorefLocal()).append("\t")
                        .append(getAdjCorefSum()).append("\t")
                        .append(getLocalNounOverlap()).append("\t")
                        .append(getGlobalNounOverlap()).append("\t")
                        .append(getCorefSum()).append("\n");
                
                
            }
            catch(Exception e){
                System.err.println("missing file...");
            }
        }

        String retString = outputCSV.toString();
        
        DocumentUtils.save(outputCSV.insert(0, header).toString(), 
                summaryDocPath+".csv");
        outputCSV = new StringBuilder();
        
        
        return retString;
            
    }
    
    /**
     * annotate + evaluate on input directory
     * @param in Input directory
     */
    public void process(String in){
        StanfordCoreEvaluator sce = new StanfordCoreEvaluator();
        String csv = "";
        sce.annotate(in);
        csv += sce.evaluate(in);
        DocumentUtils.save(csv, in+".csv");
    }
    
    public static int countPos(ArrayList<String> list){
        
        String[] common = DocumentUtils.readFile("common.txt").split("\n");
        ArrayList commonList = new ArrayList( Arrays.asList(common));
        int count = 0;
        for(String s : list){
//            if( s.equals("DT")  || s.equals("IN") )
            if(commonList.contains(s.toLowerCase()))
                count ++;
        }
        return count;
    }
    
    public static void countCorefs(){
        StanfordCoreEvaluator sce = new StanfordCoreEvaluator();
        
        String csv = ""; 
        ArrayList<int[][]> matrices = new ArrayList();
        ArrayList<int[][]> largeMatrices = new ArrayList();
        int largestMatrix = 0;
            
        for(File f : DocumentUtils.traverse("corefs2012/brown_stripped/misc")){
            sce.parse(f.getAbsolutePath());
            int[] corefSumOut = new int[sce.sentencePosList.size()];
            int[] corefSumIn = new int[sce.sentencePosList.size()];
            int[] parts = new int[sce.sentencePosList.size()];
//            float[] lengths = new float[sce.sentencePosList.size()];
            
//            float[] corefAvgIndex = new float[sce.sentencePosList.size()];
            float[] dtCount = new float[sce.sentencePosList.size()];
//            ArrayList heads = new ArrayList();
//            System.out.println(sce.sentencePosList);
            
            // count various pos-features///////////////////////////
            
            int partCounter = 1;
            int PARTS = 10;
            int splitIndex = (sce.sentencePosList.size() - (sce.sentencePosList.size() % PARTS))/PARTS;
            

            
            int c = 0;
//            for(ArrayList<String> posList:sce.sentenceWordList){
//                float[] result = new float[1800];
////                sdm.addText(posList);
//                for(String w : posList){
//                    
//                    float[] vec = sdm.getWordContextVector(((String)w).toLowerCase());
//
//                    if(vec != null && vec.length > 0)
//                        result = CSArrayTools.addVector(result, vec);
//                }
////                System.out.println(posList);
//                float[] d = sdm.getMeanDocumentVector(posList.toArray(new String[posList.size()]));
////                System.out.println(posList.size()+","+CSArrayTools.norm(result));
////                lengths[c] = CSArrayTools.compare(result,d);
//                dtCount[c] = posList.size();
//                
//                parts[c] = partCounter;
//                
//                if(c!= 0 && c%splitIndex == 0){
//                    partCounter++;
//                }
//                
//                c++;
//
//                
//            }
//            System.out.println(Arrays.toString(lengths));
            /////////////////////////////////////////////////////////
            
            
            ArrayList<Integer> distances = new ArrayList();
            ArrayList<Integer> distanceParts = new ArrayList();
            
            int[][] coma = new int
                    [sce.sentencePosList.size()]
                    [sce.sentencePosList.size()];
            


            for(int i = 0; i < coma.length; i++){
                for(int j = 0; j < coma.length; j++){
                    coma[i][j] = 0;
                }
            }
            
//            int c =0;
            for(Coref cor: sce.corefObjectList){
                
                corefSumOut[cor.head-1] += cor.notLocalUniqueSentenceIndices.size();
                
//                float avg = 0;
                for(Integer i : cor.sentenceIndices){
                    corefSumIn[i-1] ++;
                }
                
//                corefAvgIndex[cor.head-1] = avg;
                
                for(Integer i : cor.notLocalUniqueSentenceIndices){
//                    heads.add(i);
                    coma[cor.head-1][i-1]++;
                    coma[i-1][cor.head-1]++;
                    distances.add(Math.abs(cor.head - i));
                    distanceParts.add(findPart(sce.sentenceWordList.size(),PARTS,cor.head));
                }
//                System.out.println(cor.head);
//                System.out.println(cor.notLocalUniqueSentenceIndices);
                
//                c++;
            }
            if(coma.length > largestMatrix)
                largestMatrix = 50;//coma.length;
            matrices.add(coma);
            
//            for(int i = 0; i < coma.length; i++){
//                System.out.println(Arrays.toString(coma[i]));
//            }
            
//            System.out.println("--");
//            System.out.println(Arrays.toString(corefSumOut));
//            System.out.println(Arrays.toString(corefSumIn));
//            System.out.println(Arrays.toString(lengths));
//            System.out.println(heads);
//            System.out.println(distances);
            
//            System.out.println(f.getName());
    //        sce.sentenceCorefList.remove(0);
            //sentence X sentence matrix where each cell denotes number of corefs
            // between sentence i and j.
//            int[][] coMa = new int
//                    [sce.sentencePosList.size()]
//                    [sce.sentencePosList.size()];
//
//            for(int i = 0; i < coMa.length; i++){
//                for(int j = 0; j < coMa.length; j++){
//                    coMa[i][j] = 0;
//                }
//            }
//
//            for(ArrayList<Integer> l : sce.sentenceCorefList){
//                for(Integer i : l){
//                    for(Integer j: l){
//                        // sentence at index 0 is sentence 1 in stanford
//                        coMa[i-1][j-1]++;
//                        coMa[j-1][i-1]++;
//                    }
//                    
//                }
//            }
//
//
//            int[] rowSums = new int[coMa.length];
//            int[] rowNums = new int[coMa.length];
//            distances = new ArrayList();
////            System.out.println(coMa.length);
//            for(int i = 0; i < coMa.length; i++){
//
//                int rowSum = 0;
//                int rowNum = 0;
//                int distanceCounter = 0;
//                boolean shouldCount = false;
//                for(int j = 0; j < coMa.length; j++){
//    //                System.out.print(coMa[i][j]+",");
//
//                    if(coMa[i][j] == 0){
//                        if(shouldCount)
//                            distanceCounter++;
//                    }
//                    else{
//                        rowNum++;
//                        distances.add(distanceCounter);
//                        distanceCounter = 0;
//                        shouldCount = true;
//                    }
//
//                    rowSum += coMa[i][j];
//                }
////                System.out.println(Arrays.toString(coMa[i]));
//                rowSums[i] = rowSum;
//                rowNums[i] = rowNum;
//
//    //            System.out.println("");
//            }

    //        System.out.println("Row sums:");
//            System.out.println(Arrays.toString(rowSums));
        
//        System.out.println("Distances:");
//        System.out.println(distances);
        
//        System.out.println("Row nums:");
//        System.out.println(Arrays.toString(rowNums));
        }
        
//        System.out.println(largestMatrix);
        for(int[][] matr : matrices){
            
            int[][] tempMat;
            
            if(matr.length < largestMatrix){
                tempMat = new int[largestMatrix][largestMatrix];
                for(int i = 0; i < tempMat.length; i++){
                    Arrays.fill(tempMat[i], 0);
                }
                
                for(int i = 0; i < matr.length; i++){
                    for(int j = 0; j < matr.length; j++){
                        tempMat[i][j] = matr[i][j];
                    }
                }
                
            }
            else
                tempMat = matr;
            
            largeMatrices.add(tempMat);
        }
        
        float[][] resultMatrix = new float[largestMatrix][largestMatrix];
        // print
        for(int i = 0; i < largeMatrices.size(); i++){
            
            int[][] tempMat = largeMatrices.get(i);
            
            for(int j = 0; j < tempMat.length; j++){
                
                for(int k = 0; k < tempMat[j].length; k++){
                    if(j<largestMatrix && k < largestMatrix)
                    resultMatrix[j][k] += tempMat[j][k];
                    
                }
            }
            

        }
//        for(int j = 0; j < resultMatrix.length; j++){
//
//            for(int k = 0; k < resultMatrix[j].length; k++){
//                resultMatrix[j][k] = k - j <= 0  ? resultMatrix[j][k+resultMatrix.length-1-j] : 0;
//
//            }
//        } 
        for(int j = 0; j < resultMatrix.length; j++){

            for(int k = 0; k < resultMatrix[j].length; k++){

                resultMatrix[j][k] /= (float)largeMatrices.size();

            }
            
            System.out.println(Arrays.toString(resultMatrix[j]));
        }
    }
    
    public static void main(String[] args){
        
        /**
         * Will read the files in the folder First12, parse them and save the
         * parsing results in corefs2012/First12. The resultfile will be named
         * First12.csv and saved in the working directory.
         */
        
        
//        sdm.load("brownut");
//        System.out.println("space loaded...");
//        sdm = new WordVectors("brownut");
        countCorefs();
        

        
//        StanfordCoreEvaluator sce = new StanfordCoreEvaluator();
//        
//
//            
//        for(File f : DocumentUtils.traverse("corefs2012/clean_big")){
//            sce.parse(f.getAbsolutePath());
//            
//            CorefGraph g = new CorefGraph(sce.sentencePosList.size(),sce.corefObjectList);
//            
//            break;
                    
            
            
//        }
////        StanfordCoreEvaluator sce = new StanfordCoreEvaluator();
//        
        String csv = ""; 
//        
////        sce.annotate("clean_big");
//        
//        sce.annotate("/media/axe/Corpora/brown/temp");
//////        sce.annotate("PREV/P1202");
//////        sce.annotate("PREV/P1204");
//////        sce.annotate("PREV/P1206");
//////        sce.annotate("PREV/P1208");
//////        sce.annotate("PREV/P1210");
////////        sce.annotate("First12");
////////        csv += sce.evaluate("First12");
//////        
//        csv += sce.evaluate("/media/axe/Corpora/brown/temp");
//////        csv += sce.evaluate("PREV/P1202");
//////        csv += sce.evaluate("PREV/P1204");
//////        csv += sce.evaluate("PREV/P1206");
//////        csv += sce.evaluate("PREV/P1208");
//////        csv += sce.evaluate("PREV/P1210");
////        
////        
////        
//        DocumentUtils.save(csv, "browns2temp.csv");
        
        
    }
    
//    class Coref{
//        private ArrayList<Integer> sentenceIndices = new ArrayList();
//        private ArrayList<Integer> uniqueSentenceIndices = new ArrayList();
//        private ArrayList<Integer> notLocalUniqueSentenceIndices = new ArrayList();
//        
//        private final int head;
//        
//        Coref(int head){
//            this.head = head;
//            
//        }
//        
//        int getHead(){
//            return head;
//        }
//        
//        
//        void addSentenceIndex(int i){
//            this.sentenceIndices.add(i);
//            
//            if(!  this.uniqueSentenceIndices.contains(i))
//                this.uniqueSentenceIndices.add(i);
//            
//            if(i != head && !this.notLocalUniqueSentenceIndices.contains(i)){
//                this.notLocalUniqueSentenceIndices.add(i);
//            }
//        }
//        
//    }

    public ArrayList<ArrayList<String>> getSentencePosList() {
        return this.sentencePosList;
    }

    public ArrayList<ArrayList<String>> getSentenceWordList() {
        return this.sentenceWordList;
    }
}
