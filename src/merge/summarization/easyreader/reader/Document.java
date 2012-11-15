

package merge.summarization.easyreader.reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import moj.lang.GenericTokenizer;
import moj.util.FileTools;


/**
 * Reads a source text.
 *
 * @author Christian
 */
public class Document {

    protected String text;

    protected ArrayList<String> sentences;
    private ArrayList<String> words;

    /**
     * Creates a document.
     */
    public Document(){
        
    }

    public String getText(){
        return text.toString();
    }
    public ArrayList<String> getSentences(){
        if(sentences == null){
//            sentences = new ArrayList();
            
            String[] sents = text.split("\n");
            sentences = new ArrayList(Arrays.asList(sents));
//            System.out.println("List size: " + sentences.size());

//
//            Pattern sentencePattern = Pattern.compile("([.!?:][\"']*[\n]*)(\\s+)([`]*[A-ZÅÄÖ0-9\\-\\–\\(”\"])");
//            Matcher sm = sentencePattern.matcher(text);
//
//            if(sm.find()){
//                String repl = sm.replaceAll("$1<<<s>>>$3");
//                sentences.addAll(Arrays.asList(repl.split("<<<s>>>")));
//            }

        }

        return sentences;

    }

    /**
     * Get the words in the text.
     *
     * @return
     */
    public ArrayList<String> getWords(){
        GenericTokenizer tokenizer = new GenericTokenizer();
        if(words == null){
            words = new ArrayList(Arrays.asList
                    (tokenizer.getWords(text.toString().toLowerCase(), true)));
        }


//        HashSet h = new HashSet(words);
//        words.clear();
//        words.addAll(h);

        
        //System.out.println("\nRetrieving " + words.size() + " words\n");
        return words;
    }

    /**
     * Reads a text from a source filename and stores it as an array of 
     * sentences.
     * 
     * @param inputFileName
     */
    public void readFile(String inputFileName){
//        if(inputFileName.endsWith(".pdf"))
//            text = PDFTools.PDF2Text(inputFileName);
        
//        else
            text = FileTools.readFile2String(inputFileName);
        
        System.out.println(text);
    }

    public void read(String in) {
        text = in;
    }

    public void read(ArrayList<String> sentences) {
        StringBuilder builder = new StringBuilder();
        
        for(String s : sentences){
            builder.append(s).append("\n");
        }

        text = builder.toString();
    }
}
