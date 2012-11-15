

package merge.summarization.easyreader.vectors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import merge.summarization.easyreader.summarization.Settings;
import moj.ri.SparseDistributedMemory;
import moj.ri.weighting.WeightingScheme;
import moj.util.FileTools;
//import org.tartarus.snowball.ext.swedishStemmer;

/**
 * Train a random index on a source text.
 *
 * @author Christian
 */
public class Trainer {

    private SparseDistributedMemory sdm;

    public Trainer(){
    }

    public SparseDistributedMemory getRI(){
        return sdm;
    }

    public void train(ArrayList<String> list, Settings settings) {

        ArrayList<String> wordList = list;

        if(settings.getRemoveStopWords()){
            System.out.println("Reading stoplist " + settings.getPath()+
                    settings.getStopWordFile());
            ArrayList<String> stopList = new ArrayList
                        (Arrays.asList(FileTools.readFile2String(settings.getPath()+
                        settings.getStopWordFile()).split(",")));

            wordList.removeAll(stopList);

        }

//        ArrayList<String> stemmedList = new ArrayList();
//
//        swedishStemmer stemmer = new swedishStemmer();
//
//        for(String w : wordList){
//            stemmer.setCurrent(w);
//            stemmer.stem();
//            stemmedList.add(stemmer.getCurrent());
//        }


        try {
            //System.out.println("creating space...");
            sdm = new SparseDistributedMemory(settings.getDimensionality(),
                    settings.getRandomDegree(), settings.getRandomSeed(),
                    settings.getLeftWindowSize(), settings.getRightWindowSize(),
                    (WeightingScheme) Class.forName(
                settings.getWeightingScheme())
                    .newInstance());

        } catch (InstantiationException ex) {
            Logger.getLogger(Trainer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Trainer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Trainer.class.getName()).log(Level.SEVERE, null, ex);
        }

        //System.out.println("adding text...");
//        sdm.addText(stemmedList.toArray(new String[wordList.size()]));

        String[] words = wordList.toArray(new String[wordList.size()]);
        sdm.setMaxNumThreads(1);
        
        int num = sdm.addText(words);

        if(settings.getSaveToDisk()){
            System.out.println("saving to " + settings.getTrainingOutput());
            sdm.save(settings.getTrainingOutput());
        }
    }

    public static void main(String[] args){

    }
}
