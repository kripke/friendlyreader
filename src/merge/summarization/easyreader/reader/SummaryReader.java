
package merge.summarization.easyreader.reader;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author christian
 */
public class SummaryReader {
    Summary summary;
//    private ArrayList<Integer> indexList;

    public SummaryReader(Summary sum){
        this.summary = sum;
    }

    public Summary getSummary(){
        return summary;
    }

//    public ArrayList<Integer> getIndexList() {
//        return indexList;
//    }



    /**
     * Filter the ranked sentences according to the percentage while keeping
     * the original order they appeared in.
     *
     * @param percent
     * @return A fraction (specified by percentage 0- 100) of the list containing the
     * indices pointing to the ranked values. The list is sorted so that the
     * original order is maintained.
     *
     */
    public ArrayList<Integer> buildIndexListPercent(double percent) {
//        if (orderedValueList.isEmpty()) {
//            buildSummary();
//        }
        
        double numSentences = (((double) (percent / 100))
                * this.summary.getOrderedValues().size());

        ArrayList<Integer> indexList = new ArrayList();
        indexList = buildIndexListNumSentences(numSentences);

//        for (int i = 0; i < numSentences; i++) {
//            indexList.add(this.summary.getOrderedValues().get(i).getIndex());
//        }
//
//        Collections.sort(indexList);
//        this.indexList = indexList;

        return indexList;
    }
    
    public ArrayList<Integer> buildIndexListNumWords(int numWords) {

        int wordCount = 0;
        
        // to hold a list of sentence indices
        ArrayList<Integer> indexList = new ArrayList();
        
        //keep track of sentence index
        int i = 0;
        
        while(wordCount < numWords) {
            
            //iterate through all sentences, high through low rank
            if(i < summary.getOrderedValues().size()){
                Integer index = this.summary.getOrderedValues().get(i).getIndex();

                // wordWount < numWords, add to resulting summary indexlist
                indexList.add(index);

                // count the words in the current sentence
                String sentence = this.summary.getSentences().get(index);
                Document d = new Document(); 
                d.read(sentence); 
                wordCount += d.getWords().size();
            }
            else{
                System.err.println("Too many words!");
                System.err.println("wc: "+wordCount);
                System.err.println("i: " + i);
                System.err.println("size: " + this.summary.getOrderedValues().size());
                break;
            }
                
            
            i++; //number of sentences extracted - 1
        } //while, stops if wordCount > numWords

        Collections.sort(indexList);

        return indexList;
    }
    
    public ArrayList<Integer> buildIndexListNumSentences(double sumSentenceNum) {

        int wordCount = 0;
        
        // to hold a list of sentence indices
        ArrayList<Integer> indexList = new ArrayList();
        
        //keep track of sentence index
        int i = 0;
        
        while(i < sumSentenceNum) {
            
            //iterate through all sentences, high through low rank
            Integer index = this.summary.getOrderedValues().get(i).getIndex();
            
            // wordWount < numWords, add to resulting summary indexlist
            indexList.add(index);
            
            i++; //number of sentences extracted - 1
        } 

        Collections.sort(indexList);

        return indexList;
    }
    
    /**
     * Builds a string containing the most important sentences in the document
     * set by the setDocument() method. The sentences should have been ranked
     * first with the buildSummary() method.
     *
     * @param percent the amount of text to retrieve with regards to original
     * length.
     * @param appendValues should the ranked value of each value be appended
     * to the return string?
     * @return a portion of the text
     */
    public String retrieveSummary(double percent, boolean appendValues) {

//        StringBuilder summaryBuilder = new StringBuilder();

        ArrayList<Integer> al = buildIndexListPercent(percent);
        return retrieveSummary(al, appendValues);
        
//        for (Integer i : al) {
//            summaryBuilder.append(this.summary.getSentences().get(i).
//                    toString().trim()).append("\n");
//            if (appendValues) {
//                summaryBuilder.append("@value:").append(this.summary.
//                        getSentenceRanks()[i]).append("\n\n");
//            }
//        }
//        return summaryBuilder.toString();
    }
    
    /**
     * Builds a string containing the most important sentences in the document
     * set by the setDocument() method. The sentences should have been ranked
     * first with the buildSummary() method.
     * 
     * @param indices The indices (as provided by buildIndexList()-methods)
     * @param appendValues Whether to append the ranks of sentences in the summary
     * @return A String containing the summary
     */
    public String retrieveSummary(ArrayList<Integer> indices, boolean appendValues) {

        StringBuilder summaryBuilder = new StringBuilder();
        
        for (Integer i : indices) {
            summaryBuilder.append(this.summary.getSentences().get(i).
                    toString().trim()).append("\n");
            if (appendValues) {
                summaryBuilder.append("@value:").
                        append(this.summary.getSentenceRanks()[i]).append("\n\n");
            }
        }

        return summaryBuilder.toString();
    }

    public String retrieveSummaryNumWords(int numWords, boolean appendValues) {

//        StringBuilder summaryBuilder = new StringBuilder();

        ArrayList<Integer> al = buildIndexListNumWords(numWords);
        return retrieveSummary(al, appendValues);
//        
//        for (Integer i : al) {
//            summaryBuilder.append(this.summary.getSentences().get(i).
//                    toString().trim()).append("\n");
//            if (appendValues) {
//                summaryBuilder.append("@value:").append(this.summary.
//                        getSentenceRanks()[i]).append("\n\n");
//            }
////            summaryBuilder.append("\n");
//
//        }
//
//        return summaryBuilder.toString();
    }

    /**
     * Retrieve the specified number of sentences as a String.
     * What sentences will be returned is determined by the ranks in the <i>Summary</i>.
     * @param numSent The number of sentences to be retrieved. <p>numSent = 0 will
     * retrieve all sentences. </p>
     * @return The summary as a String
     */
    public String retrieveSummaryNumSent(int numSent) {

//        StringBuilder summaryBuilder = new StringBuilder();;

        ArrayList<Integer> al;
        if(numSent == 0)
            al = this.buildIndexListNumSentences(this.summary.getSentences().size());
        else
            al = this.buildIndexListNumSentences(numSent);
        
        return retrieveSummary(al, false);
//
//        for (Integer i : al) {
//            summaryBuilder
//                    .append(this.summary.getSentences().get(i).toString().trim())
//                    .append("\n");
//        }
//        
//        return summaryBuilder.toString();
    }
    
    


}
