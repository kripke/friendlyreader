
package merge.summarization.easyreader.reader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import merge.summarization.easyreader.util.IndexHelper;
import merge.summarization.easyreader.util.IndexHelperHelper;


/**
 *
 * @author christian
 */
public class Coherence extends SummaryRevision{
    public Coherence(SummaryReader reader){
        super(reader);
    }
    

    
    public ArrayList<Integer> buildIndexList(float sumPerc, float pastePerc){

        // determine the new summary percentage (amount of most important
        // sentences for the document), based on the original percentage and
        // how much of the original percentage that should be paste sentences
        float percent = 100 - pastePerc;
        ArrayList<Integer> origIndexList = this.reader.buildIndexListPercent((int)sumPerc);
        float newSumPercent = (int) ((percent / 100) * sumPerc);

        // contains a new list of indices with the most important sentences
        ArrayList<Integer> newIndexList = this.reader.buildIndexListPercent(newSumPercent);

        // calculate how many paste sentences based on pastePerc
        float pastePercent = 100 - percent;
        float numPaste = (pastePercent / 100) * (float)origIndexList.size();

        // contains a list of indices of paste sentences
//        ArrayList<Integer> pasteList = this.buildPasteList(newIndexList, (int) numPaste);
        ArrayList<Integer> pasteList = this.localTops(newIndexList, (int) numPaste);
        
        // merge the paste list and the list of important sentences and
        // return them as one!
        ArrayList<Integer> all = new ArrayList();
        all.addAll(pasteList);
        all.addAll(newIndexList);
        Collections.sort(all);
        return all;
    }
    
    /**
     * Increments the specified list with one index.
     * @param list
     * @return 
     */
    public void prevSentences(ArrayList<Integer> list){
   
//        ArrayList<Integer> indexList = list;
//        ArrayList<Integer> returnList = new ArrayList(indexList.size()+1);
        
        for(int i = 0; i < list.size(); i++){
            Integer index = list.get(i);
            if(index > 0){
                if(!list.contains(index-1)){
                    System.out.println("added");
//                    returnList.add(index-1);
                    list.add(index-1);
                    return;
                }
            }
        }
        
//        returnList.addAll(indexList);
//  
//
//       return returnList;

    }
    /**
     *
     * Builds list with indices indicating what sentences should act as
     * glue sentences.
     *
     * @param list A list containing indices that not should be included as
     * glue
     * @param num The number of glue sentences
     * @return A list with glue sentence indices
     */
    public ArrayList<Integer> localTops(List<Integer> list, int num){

        ArrayList<IndexHelperHelper> helperHelperList = new ArrayList();

        float[][] comps = this.reader.getSummary().getSentenceComparisons();
        List<IndexHelper> helperList = new ArrayList();
        for(Integer i : list){
            for(int j = 0; j < comps.length; j++){
                Float d = comps[i][j];
                if(i!=j && !list.contains(j) && d < .95)
                    helperList.add(new IndexHelper(d,j));

            }
            helperHelperList.add(new IndexHelperHelper((ArrayList<IndexHelper>) helperList));

        }
        Collections.sort(helperHelperList);

        ArrayList<Integer> returnList = new ArrayList();
        
        try{
            while(returnList.size()<num){

                IndexHelperHelper h = (IndexHelperHelper) helperHelperList.get(0);

                Integer inte = h.getHelpers().get(0).getIndex();
                if(!returnList.contains(inte))
                    returnList.add(inte);


                boolean b = h.remove(0); //remove first element in helper
                helperHelperList.remove(0);
                if(!b)
                    helperHelperList.add(helperHelperList.size()-1, h);//replace first element with the new helper
                else
                    helperHelperList.add(0,h);

                Collections.sort(helperHelperList);
            }
        }
        
        catch(IndexOutOfBoundsException e){
            System.err.println("IndexError, summary length: " + list.size());
        }

        return returnList;

    }
    
    public ArrayList<Integer> millerTops(ArrayList<Integer> sumIndices){
        ArrayList al = this.miller(sumIndices);
        System.out.println("AL: " + al);
        System.out.println("SI: " +sumIndices);
        return al;
    }
    
    private float f(float c1, float c2){
        return (c1*c2)*(1-Math.abs(c1-c2));
        
    }
    
    private float sim(int i, int j){
        return this.reader.getSummary().getSentenceComparisons()[i][j];
    }
    /**
     * 
     * @param sumIndices
     * @return A list of indices that should be added as glue.
     */
    private ArrayList<Integer> miller(ArrayList<Integer> sumIndices){

        float beta = 1;
        float gamma = .1f;
        
        ArrayList<Integer> returnList = new ArrayList(sumIndices);
//        if(num<1){
//            return sumIndices;
//        }
        
//        List<IndexHelper> helperList = new ArrayList();
        
        //one pass, for each sentence, look between this and the next
        for(int i = 0; i < sumIndices.size();i++){
            if(i < sumIndices.size()-1){
                int index = sumIndices.get(i);
                int index2 = sumIndices.get(i+1);

                float s = sim(index, index2);
                float g = 0;
                int tempJ = 0; // g
                float tempS = 0;

                if(s < beta && !(index == index2-1)){
                    // argmax
                    for(int j = index; j < index2; j++){
                        float c1 = sim(index, j);
                        float c2 = sim(index2,j);

                        float f = f(c1,c2);
                        if(f > g){
                            tempJ = j;
                            g = f;
                            tempS = f;
                        }
                    }
                    tempS = f(sim(tempJ,index), sim(tempJ,index2));
                     if( tempS > gamma && !returnList.contains(tempJ)){
                         returnList.add(tempJ);
//                        System.out.println(tempJ);
//                            IndexHelper ih = new IndexHelper(tempS,tempJ);
//                            helperList.add(ih);
                         System.out.println("Found!");
                            break;
                    }
                }
            }
        }
        return returnList;
//        
//        System.out.println(helperList);
//        Collections.sort(helperList);
//
//
//        if(helperList.isEmpty()){
//            System.out.println("EMPTY");
//            return sumIndices;
//        }
//        
//        else if(helperList.size() + sumIndices.size() > length){
//            
//            ArrayList al = new ArrayList();
//            for(IndexHelper h : helperList){
////                if(!al.contains(h.getIndex()))
//                    al.add(h.getIndex());
//            }
//            
//            while(al.size() + sumIndices.size() > length){
//                al.remove(al.size()-1);
//            }
//            
//            ArrayList _sumIndices = sumIndices;
//            _sumIndices.addAll(al);
//            Set<Integer> s = new LinkedHashSet<Integer>(_sumIndices);
//
//            System.out.println("NUM");
//            System.out.println(length);
//            System.out.println(_sumIndices.size());
//            return _sumIndices;
//        }
//        else{
//            ArrayList al = sumIndices;
//            for(IndexHelper h : helperList){
//                if(!al.contains(h.getIndex()))
//                    al.add(h.getIndex());
//            }
//            System.out.println("REC");
//            ArrayList temp = miller(al,length);
//            if(temp.size() != length){
//                System.out.println("ERROR");
//            }
//            
//            return temp;
//        }

    }
    public String retrieveCoherentSummary(float summaryPercent, float coherence, int method) {

//        StringBuilder summaryBuilder = new StringBuilder();
        
        int numSent = (int) (this.reader.summary.getSentences().size() * (summaryPercent/100));
        int numPaste = (int) (numSent * (coherence/100));
        
//        float percent = 100 - coherence;
//        ArrayList<Integer> origIndexList = this.reader.buildIndexListPercent((int)summaryPercent);
//        float newSumPercent = (int) ((percent / 100) * summaryPercent);
//
//        // contains a new list of indices with the most important sentences
//        ArrayList<Integer> newIndexList = this.reader.buildIndexListPercent(newSumPercent);

        // calculate how many paste sentences based on pastePerc
//        float pastePercent = 100 - percent;
//        float numPaste = (pastePercent / 100) * (float)origIndexList.size();
        
        return retrieveCoherentSummaryNum(numSent, (int)numPaste, method);
//        
//        ArrayList<Integer> al = null;
//        
//        switch(method){
//            case(Summary.METHOD_GLOBAL):
//                al = localTops(newIndexList, (int) numPaste);
//                
//                ArrayList<Integer> all = new ArrayList();
//                all.addAll(al);
//                all.addAll(newIndexList);
//                Collections.sort(all);
//                
//                for (Integer i : all) {
//                    summaryBuilder.append(reader.getSummary().getSentences().get(i).toString().trim()).append("\n");
//                }
//                
//                break;
//                
//            case(Summary.METHOD_MILLER):
//                al = new ArrayList(newIndexList);
//                ArrayList old = new ArrayList();
//                while(al.size() < al.size() + numPaste){
//                    
//                    al = millerTops(al);
//                    if(al.size() == old.size()){
//                        System.out.println("!");
//                        break;
//                    }
//                    old=al;
//                }
//                
//                for (Integer i : al) {
//                    summaryBuilder.append(reader.getSummary().getSentences().get(i).toString().trim()).append("\n");
//                }
//                
//                break;
//                
//            case(Summary.METHOD_PREVIOUS_SENTENCE):
//                
//                ArrayList<Integer> indexList = this.reader.buildIndexListPercent(summaryPercent/2);
//                
//                ArrayList<Integer> returnList = prevSentences(indexList);
//                
//                while(returnList.size() < indexList.size()*2){
//                    returnList=prevSentences(returnList);
//                }
//
//
//                Collections.sort(returnList);
//
//
//                
//                
//                
////                al = prevSentences(summaryPercent);
////                Collections.sort(al);
//                
//                for (Integer i : returnList) {
//                    summaryBuilder.append(reader.getSummary().getSentences().get(i).toString().trim()).append("\n");
//                }
//                
//                break;
//        }
        
        //al = buildIndexList(summaryPercent,coherence);


//        return summaryBuilder.toString();
    }

    public String retrieveCoherentSummaryNum(int sumSentenceNum, int cohSentenceNum, int method) {

        StringBuilder summaryBuilder = new StringBuilder();
        
//        float percent = 100 - coherence;
//        ArrayList<Integer> origIndexList = this.reader.buildIndexListPercent((int)summaryPercent);
//        float newSumPercent = (int) ((percent / 100) * summaryPercent);

        // contains a new list of indices with the most important sentences
        ArrayList<Integer> newIndexList = this.reader.buildIndexListNumSentences(sumSentenceNum-cohSentenceNum);

        // calculate how many paste sentences based on pastePerc
//        float pastePercent = 100 - percent;
//        float numPaste = (pastePercent / 100) * (float)origIndexList.size();
        
        
        ArrayList<Integer> al = null;
        
        switch(method){
            case(Summary.METHOD_GLOBAL):
                al = localTops(newIndexList, cohSentenceNum);
                
                ArrayList<Integer> all = new ArrayList();
                all.addAll(al);
                all.addAll(newIndexList);
                Collections.sort(all);
                
                for (Integer i : all) {
                    summaryBuilder.append(reader.getSummary().getSentences().get(i).toString().trim()).append("\n");
                }
                
                break;
                
            case(Summary.METHOD_MILLER):
                al = new ArrayList(newIndexList);
                ArrayList old = new ArrayList();
                while(al.size() < al.size() + cohSentenceNum){
                    
                    al = millerTops(al);
                    if(al.size() == old.size()){
                        System.out.println("!");
                        break;
                    }
                    old=al;
                }
                
                for (Integer i : al) {
                    summaryBuilder.append(reader.getSummary().
                            getSentences().get(i).toString().trim()).
                            append("\n");
                }
                
                break;
                
            case(Summary.METHOD_PREVIOUS_SENTENCE):
                
                ArrayList<Integer> indexList = this.reader.buildIndexListNumSentences(sumSentenceNum/2);
                
                
                prevSentences(indexList);
                System.out.println("forever... ");
                while(indexList.size() < sumSentenceNum){
                    
                    ArrayList<Integer> fullIndexList = this.reader.buildIndexListNumSentences(sumSentenceNum);
                    
                    int size = indexList.size();
                    
                    prevSentences(indexList);
                    
                    if (size == indexList.size()){
                        System.out.println(indexList);
                        System.out.println(fullIndexList);
                        
                        fullIndexList.removeAll(indexList);
                        indexList.add(fullIndexList.get(0));
                    }
//                    System.out.println("and ever... ");
                }


                Collections.sort(indexList);

                for (Integer i : indexList) {
                    summaryBuilder.append(reader.getSummary().
                            getSentences().get(i).toString().trim()).
                            append("\n");
                }
                
                break;
        }
        

        return summaryBuilder.toString();
    }
}
