

package merge.summarization.easyreader.summarization;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kricke
 */
public class Settings {

    private boolean removeStopWords = true;
    private boolean removeDocumentVector = true;
    private boolean saveToDisk = false;
    private boolean usePR = true;

    //PageRank
    private int pageRankIterations = 50;
    private float pageRankFactorD = .85f;

    //RI
    private int dimensionality = 100;
    private int leftWindowSize = 2;
    private int rightWindowSize = 2;
    private int randomDegree = 4; //number of NON-zeroes, i. e 4 = 2 +1 and 2 -1
    private int randomSeed = 777;
    private String weightingScheme = "moj.ri.weighting.MangesWS";
    private String trainingOutput = "output";
    private String stopWordFile = "stoppord.txt";
    private String trainingFile = "large.xml";

    private String path = "";

    private boolean split; // split document?
    private boolean eval; // save evaluttion?

    private String stringCounter = "00000";
    private int stringIndex = 0;

    private boolean outsideSpace = false;
    private boolean shouldUpdate = false;

    public Settings(){
        System.out.println("Creating default settings...");
    }

    public void load(String path){
        System.out.println("Loading settings: " + path + "props.txt");

        this.path = path;

        Properties props = new Properties();

        try {
            props.load(new InputStreamReader(new FileInputStream(path + "props.txt")));
            removeStopWords = Boolean.valueOf(props.getProperty("removeStopWords", Boolean.toString(removeStopWords)));
            saveToDisk = Boolean.valueOf(props.getProperty("saveToDisk", Boolean.toString(saveToDisk)));
            usePR = Boolean.valueOf(props.getProperty("usePageRank", Boolean.toString(usePR)));
            pageRankIterations = Integer.valueOf(props.getProperty("pageRankIterations", Integer.toString(pageRankIterations)));
            pageRankFactorD = Float.valueOf(props.getProperty("pageRankFactorD", Float.toString(pageRankFactorD)));
            dimensionality = Integer.valueOf(props.getProperty("dimensionality", Integer.toString(dimensionality)));
            leftWindowSize = Integer.valueOf(props.getProperty("leftWindowSize", Integer.toString(leftWindowSize)));
            rightWindowSize = Integer.valueOf(props.getProperty("rightWindowSize", Integer.toString(rightWindowSize)));
            randomDegree = Integer.valueOf(props.getProperty("randomDegree", Integer.toString(randomDegree)));
            randomSeed = Integer.valueOf(props.getProperty("randomSeed", Integer.toString(randomSeed)));
            weightingScheme = props.getProperty("weightingScheme", weightingScheme);
            trainingOutput = props.getProperty("trainingOutput", trainingOutput);
            stopWordFile = props.getProperty("stopWordFile", stopWordFile);


        } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setUsePageRank(boolean usePR){
        this.usePR = usePR;
    }

    public void setUseOutsideSpace(boolean b){
        this.outsideSpace = b;
    }


    public void setDimensionality(int dimensionality) {
        this.dimensionality = dimensionality;
    }

    public void setLeftWindowSize(int leftWindowSize) {
        this.leftWindowSize = leftWindowSize;
    }

    public void setPageRankFactorD(float pageRankFactorD) {
        this.pageRankFactorD = pageRankFactorD;
    }

    public void setPageRankIterations(int pageRankIterations) {
        this.pageRankIterations = pageRankIterations;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setRandomDegree(int randomDegree) {
        this.randomDegree = randomDegree;
    }

    public void setRandomSeed(int randomSeed) {
        this.randomSeed = randomSeed;
    }

    public void setRemoveStopWords(boolean removeStopWords) {
        this.removeStopWords = removeStopWords;
    }
    public void setRemoveDocumentVector(boolean removeVec) {
        this.removeDocumentVector = removeVec;
    }
    public void setRightWindowSize(int rightWindowSize) {
        this.rightWindowSize = rightWindowSize;
    }

    public void setSaveToDisk(boolean saveToDisk) {
        this.saveToDisk = saveToDisk;
    }

    public void setTrainingFile(String file){
        this.trainingFile = file;
    }

    public void setTrainingOutput(String trainingOutput) {
        this.trainingOutput = trainingOutput;
    }

    public void setWeightingScheme(String weightingScheme) {
        this.weightingScheme = weightingScheme;
    }
    public void setSplit(boolean split) {
        this.split = split;
    }

    public void setEval(boolean eval) {
        this.eval = eval;
    }
    public String getPath() {
        return path;
    }

    public boolean getUsePageRank(){
        return this.usePR;
    }
    public String getTrainingOutput() {
        return trainingOutput;
    }

    public String getWeightingScheme() {
        return weightingScheme;
    }

    public int getDimensionality() {
        return dimensionality;
    }

    public int getLeftWindowSize() {
        return leftWindowSize;
    }

    public float getPageRankFactorD() {
        return pageRankFactorD;
    }

    public int getPageRankIterations() {
        return pageRankIterations;
    }

    public int getRandomDegree() {
        return randomDegree;
    }

    public int getRandomSeed() {
        return randomSeed;
    }

    public boolean getRemoveStopWords() {
        return removeStopWords;
    }

    public int getRightWindowSize() {
        return rightWindowSize;
    }

    public boolean getSaveToDisk() {
        return saveToDisk;
    }

    public String getStopWordFile() {
        return stopWordFile;
    }

    public String getTrainingFile(){
        return this.trainingFile;
    }

    public boolean getEval() {
        return eval;
    }

    public boolean getSplit() {
        return split;
    }

    public String getInfo(){

        String dimstring = Integer.toString(this.dimensionality);
        
        switch(dimstring.length()){
            case 0:
                dimstring = "00000";
                break;
            case 1:
                dimstring = "0000"+dimstring;
                break;
            case 2:
                dimstring = "000"+dimstring;
                break;
                
            case 3:
                dimstring = "00"+dimstring;
                break;
            case 4:
                dimstring = 0+dimstring;
        }

        StringBuilder builder = new StringBuilder();
        builder.
                append("d").append(dimstring)
                .append("_wl").append(this.leftWindowSize)
                .append("_wr").append(this.rightWindowSize)
                .append("_e").append(this.randomDegree)
                .append("_rsw").append(this.removeStopWords).
                append("_rdv").append(this.removeDocumentVector).append(this.randomSeed);
        return builder.toString();
    }
    @Override
    public String toString(){

        StringBuilder str = new StringBuilder();
        str.append("Settings are as follows:\n");

        str.append("dimensionality: ").append(Integer.toString(this.dimensionality)).append("\n");
        str.append("leftWindowSize: ").append(this.leftWindowSize).append("\n");
        str.append("rightWindowSize: ").append(this.rightWindowSize).append("\n");
        str.append("randomDegree: ").append(this.randomDegree).append("\n");
        str.append("removeStopWords: ").append(this.removeStopWords).append("\n");
        str.append("weighting: ").append(this.weightingScheme).append("\n");
        return str.toString();
    }

    public boolean getRemoveDocumentVector() {
        return removeDocumentVector;
    }

    public boolean getUseOutsideSpace() {
        return outsideSpace;
    }

    public void setStopWordFile(String file) {
        this.stopWordFile = file;
    }

    /**
     * Get whether the space should be updated with the text to be summarized,
     * as set by setShouldUpdate(boolean b).
     * 
     * Default: false
     * @return true if the space should be updated, false otherwise
     */
    public boolean getShouldUpdate() {
        return this.shouldUpdate;
    }
   
    /**
     * Set whether the space should be updated with the text to be summarized 
     * or not.
     * Default - false
     * @param b whether the space should be updated with the text to be summarized 
     * or not.
     */
    public void setShouldUpdate(boolean b){
        this.shouldUpdate = b;
    }

    public void randomizeSeed() {
        this.setRandomSeed(new Random().nextInt());
    }


}
