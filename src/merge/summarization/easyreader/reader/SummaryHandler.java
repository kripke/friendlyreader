/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package merge.summarization.easyreader.reader;

import java.util.ArrayList;
import merge.summarization.easyreader.util.IndexHelper;
import moj.ri.IndexHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author christian
 */
public class SummaryHandler extends DefaultHandler{
    Summary sum = new Summary();
    ArrayList<String> words = new ArrayList();
    ArrayList<Float> wordRanks = new ArrayList();
    ArrayList<String> sentences = new ArrayList();
    ArrayList<Float> sentenceRanks = new ArrayList();
    float[][] comparisons;
    ArrayList<IndexHelper> orderedValues = new ArrayList();

    int iMemory = 0;

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {

        if(qName.equals("w")){
            words.add(attributes.getValue("word"));
            wordRanks.add(Float.parseFloat(attributes.getValue("value")));
        }

        if(qName.equals("s")){
            sentences.add(attributes.getValue("content"));
            sentenceRanks.add(Float.parseFloat(attributes.getValue("rank")));
        }

        if(qName.equals("si")){
            iMemory = Integer.parseInt(attributes.getValue("index"));

        }
        if(qName.equals("sj")){
            int j = Integer.parseInt(attributes.getValue("index"));

            String val = attributes.getValue("sim");
            if(!val.equals("null"))
                comparisons[iMemory][j] = Float.parseFloat(val);
            else
                comparisons[iMemory][j] = 0f;

        }

        if(qName.equals("ih")){
            IndexHelper ih = new IndexHelper(
                    Float.parseFloat(attributes.getValue("value")),
                    Integer.parseInt(attributes.getValue("index")));
            orderedValues.add(ih);
        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equals("sentences"))
            comparisons = new float[sentences.size()][sentences.size()];

    }
    
    public Summary getResult(){
        float[] ranks = new float[wordRanks.size()];
        for(int i = 0; i < ranks.length; i++){
            ranks[i] = wordRanks.get(i);
        }
        
        sum.setWordRanks(ranks);
        sum.setSentences(sentences);
        sum.setWords(words);
        sum.setSentenceComparisons(comparisons);

        ranks = new float[sentenceRanks.size()];
        for(int i = 0; i < ranks.length; i++){
            ranks[i] = sentenceRanks.get(i);
        }
        sum.setSentenceRanks(ranks);
        sum.setOrderedValues(orderedValues);
        
        return sum;
    }





}

