
package merge.summarization.easyreader.reader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import merge.summarization.easyreader.util.IndexHelper;

/**
 *
 * @author christian
 */
public class Summary{
    public static final int METHOD_MILLER = 0;
    public static final int METHOD_GLOBAL = 1;
    public static final int METHOD_PREVIOUS_SENTENCE = 2;
    
    private ArrayList<String> sentences;
    private ArrayList<String> words; //list of unique words

    private float[] wordRanks;
    private float[] sentenceRanks;

    private float[][] sentenceComparisons;

    private ArrayList<IndexHelper> orderedValues;
    

    public Summary(){
    }

    public ArrayList<String> getSentences() {
        return sentences;
    }

    public void setSentences(ArrayList<String> sentences) {
        this.sentences = sentences;
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public float[][] getSentenceComparisons() {
        return sentenceComparisons;
    }

    public void setSentenceComparisons(float[][] sentenceComparisons) {
        this.sentenceComparisons = sentenceComparisons;
    }

    public float[] getSentenceRanks() {
        return sentenceRanks;
    }

    public void setSentenceRanks(float[] sentenceRanks) {
        this.sentenceRanks = sentenceRanks;
    }

    public float[] getWordRanks() {
        return wordRanks;
    }

    public void setWordRanks(float[] wordRanks) {
        this.wordRanks = wordRanks;
    }

    public void setOrderedValues(ArrayList<IndexHelper> orderedValueList) {
        this.orderedValues = orderedValueList;
    }

    public ArrayList<IndexHelper> getOrderedValues() {
        return this.orderedValues;
    }

    public void save(String fileOut){
        FileWriter fw = null;
        try {
            fw = new FileWriter(fileOut);
            BufferedWriter buffer = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(buffer);
            pw.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            pw.println("<doc>");

            pw.println("\t<words>");
            for(int i = 0; i < this.words.size(); i++){
                pw.println("\t\t<w word=\""+this.words.get(i)+"\" value=\""+this.wordRanks[i]+"\"/>");
            }

            pw.println("\t</words>");

            pw.println("\t<sentences>");
            for(int i = 0; i < this.sentences.size(); i++){
                String sentence = this.sentences.get(i);
                sentence = sentence.replaceAll("\"","&quot;");
                sentence = sentence.replaceAll("&","&amp;");
                sentence = sentence.replaceAll("<","&lt;");
                sentence = sentence.replaceAll(">","&gt;");
                sentence = sentence.replaceAll("'","&apos;");
                pw.println("\t\t<s content=\""+sentence+"\" rank=\""+this.sentenceRanks[i]+"\"/>");
            }
            pw.println("\t</sentences>");


            pw.println("\t<comparisons>");

            for(int i = 0; i < this.sentenceComparisons.length; i++){
                pw.println("\t\t<si index=\""+i+"\">");
                for(int j = 0; j < this.sentenceComparisons.length; j++){
                    pw.println("\t\t\t<sj index=\""+j+"\" sim=\""+this.sentenceComparisons[i][j]+"\"/>");
                }
                pw.println("\t\t</si>");
            }

            pw.println("\t</comparisons>");

            pw.println("\t<order>");
            for(int i = 0; i < this.orderedValues.size(); i++){
                pw.println("\t\t<ih index=\""+orderedValues.get(i).getIndex()+"\" value=\""+orderedValues.get(i).getValue()+"\"/>");
            }
            pw.println("\t</order>");
            
            pw.println("</doc>");
            pw.flush();
            pw.close();
        } catch (IOException ex) {
            Logger.getLogger(Summary.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(Summary.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


}
