package com.santaanna.friendlyreader.text;

import java.util.Comparator;


/**
 * A Comparator class used for sorting sentences based on their importance.
 *
 * @author Johan Falkenjack
 * @version 1.0
 * @since 2012-09-19
 * @see Sentence
 */
public class SentenceRankComparator implements Comparator {
    
    @Override
    public int compare(Object s1, Object s2){
        if (((Sentence)s1).getRank() > ((Sentence)s2).getRank()) {
            return -1;
        } else if (((Sentence)s1).getRank() < ((Sentence)s2).getRank()) {
            return 1;
        }
        return 0;
    }
    
}