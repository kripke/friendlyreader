

package com.santaanna.friendlyreader.summarization.summarizer;

import java.util.Random;
import com.santaanna.friendlyreader.summarization.reader.Summary;
import com.santaanna.friendlyreader.summarization.Settings;

/**
 *
 * @author Kricke
 */
public class RandSum extends Summarizer{

    public RandSum(Settings settings){
        super(settings);
    }

    @Override
    protected Summary rank() {
        Summary sum = new Summary();
        Random r = new Random();
        int s = this.document.getSentences().size();
        float[] f = new float[s];

        for(int i = 0; i < f.length; i++){
            f[i] = r.nextFloat();
        }

        sum.setSentences(document.getSentences());
        sum.setSentenceRanks(f);

        return sum;
    }

}
