package com.santaanna.friendlyreader.summarization.summarizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;
import com.santaanna.friendlyreader.summarization.reader.Document;
import com.santaanna.friendlyreader.summarization.reader.Summary;
import com.santaanna.friendlyreader.summarization.Settings;
import com.santaanna.friendlyreader.summarization.util.IndexHelper;

/**
 * Base class for summarizers. Contains methods for retrieving parts of the text.
 * {@link rank()} should be implemented for deriving classes,
 * with routines for ranking the sentences contain within the {@link Document}
 * object.

 * @see rank, Document
 *
 *
 * @author Christian Smith
 */
public abstract class Summarizer {

    protected Settings settings;
    protected Document document;
    private ArrayList<IndexHelper> orderedValueList = new ArrayList();
    private ArrayList<Float> valueList = new ArrayList();
    private ArrayList<String> sentenceList = new ArrayList();

//    public Summarizer(){
//    }
    public Summarizer(Settings settings) {
        this.settings = settings;
    }

    public void setText(Document d) {
        document = d;
    }

//    public void setTokens(ArrayList<String> list){
//        tokens = list;
//    }
    protected abstract Summary rank();

    public Summary buildSummary() {

        int i = 0;
        orderedValueList.clear();
        sentenceList.clear();
        valueList.clear();

        Summary summary = rank();

        for (String s : summary.getSentences()){
            Float f = new Float(summary.getSentenceRanks()[i]);
            
            orderedValueList.add(new IndexHelper(f, i));
            valueList.add(f);
            sentenceList.add(s);
            i++;
        }
        if (orderedValueList.isEmpty()) {
            return null;
        }

        Collections.sort(orderedValueList);

        summary.setOrderedValues(orderedValueList);
        //summary.save();

        return summary;


        //return retrieveSummary(30, false);

    }



    /**
     * Filter the ranked values according to the percentage while keeping
     * the original order they appeared in.
     *
     * @param percent
     * @return A fraction (specified by percentage) of the list containing the
     * indexes pointing to the ranked values. The list is sorted so that the
     * orignial order is maintained.
     *
     */
//    public ArrayList<Integer> buildIndexList(double percent) {
//        if (orderedValueList.isEmpty()) {
//            buildSummary();
//        }
//
//        double numSentences = (((double) (percent / 100))
//                * orderedValueList.size());
//
//        ArrayList<Integer> indexList = new ArrayList();
//
//        for (int i = 0; i < numSentences; i++) {
//            indexList.add(orderedValueList.get(i).getIndex());
//        }
//
//        Collections.sort(indexList);
//
//        return indexList;
//    }

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
//    public String retrieveSummary(double percent, boolean appendValues) {
//
//        StringBuilder summaryBuilder = new StringBuilder();
//
//        ArrayList<Integer> al = buildIndexList(percent);
//        for (Integer i : al) {
//
//            summaryBuilder.append(sentenceList.get(i).toString().trim()).append("\n");
//            if (appendValues) {
//                summaryBuilder.append("@value:").append(valueList.get(i)).append("\n\n");
//            }
////            summaryBuilder.append("\n");
//
//        }
//
//        return summaryBuilder.toString();
//    }

//    public String retrieveTaggedSummary(double percent) {
//
//        StringBuilder summaryBuilder = new StringBuilder();
//
//        ArrayList<Integer> al = buildIndexList(percent);
//        summaryBuilder.append("<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></head>\n");
//        summaryBuilder.append("<body>\n");
//
//        int i = 0;
//        for (String sentence : sentenceList) {
//            if (!al.contains(i)) {
//
//                /**<p>Normal font color <span style="color:orange">different font color</span>
//                 *
//                 *normal font color <span style="background-color:yellow">different background color</span></ */
//                summaryBuilder.append("<n><span style=\"color:red\">").append(sentence.trim()).append("</span></n><br>").append("\n");
//            } else {
//                summaryBuilder.append("<n><b>" + sentence.trim() + "</n></b><br>").append("\n");
//            }
//            i++;
//        }
//        summaryBuilder.append("</body>\n");
//        return summaryBuilder.toString();
//    }
}
