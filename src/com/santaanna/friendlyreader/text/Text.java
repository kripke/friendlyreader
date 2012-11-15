package com.santaanna.friendlyreader.text;

import java.util.ArrayList;
import com.santaanna.friendlyreader.utils.POSTaggerHandler;


/**
 * Class for storing internal representations of text data. Keeps a Sentences object for each level of simplification.
 *
 * @author Johan Falkenjack, Christoffer Ohlsson
 * @version 1.2
 * @since 2012-09-28
 * @see Sentence, Sentences, FriendlyReaderAPI
 */
public class Text implements PrintableTextObject {
    
	/**
     * Contains the full text as a String object. Not used at this time but might be 
     * useful for interaction with text which is already loaded.
     */
    @SuppressWarnings("unused")
	private String rawString;

    /**
     * Contains the full text as a Sentences object.
     */
    private Sentences sentences;
    private int originalLength;
    
    /**
     * Standard constructor initializing with data, creates Sentences object for original text.
     * This version is called when part-of-speech tagging is used.
     *
     * @param plain A string representing the whole sentence before processing.
     * @param taggerHandler A part-of-speech tagger used to tag the sentence.
     */
    public Text(String plain, POSTaggerHandler taggerHandler) {
        this.rawString = plain;
        this.sentences = new Sentences(plain, taggerHandler);
        this.originalLength = this.sentences.getLength();
    }
    
    /**
     * Standard constructor initializing with data, creates Sentences object for original text.
     * This version is called when part-of-speech tagging is not used.
     * 
     * @param plain A string representing the whole sentence before processing.
     */
    public Text(String plain) {
    	this.rawString = plain;
    	this.sentences = new Sentences(plain);
	}

//	/**
//     * Generates a path to a unique directory.
//     * You have to create the directory yourself.
//     * @return A unique path as a String, terminated by a separator character (e.g. "data/f61a9988-763e-4e7a-9ea0-7822fac99ef1/")
//     */
//    public static String getUniqueDirPath() {
//        return (getBaseTempDir() + 
//                UUID.randomUUID().toString() + 
//                java.io.File.separator);
//    }
    
//    /**
//     * (possibly temporary) utility function to get the system temporary dir + the name "FriendlyReader"
//     * @return {system temporary directory} + "FriendlyReader" + separator character (/ or \)
//     */
//    public static String getBaseTempDir() {
//        return (System.getProperty("java.io.tmpdir") + "FriendlyReader" + java.io.File.separator);
//    }
    
//    private void deleteDir(String dirPath) {
//        //Why can't Java just have default arguments? "Overloading" like this feels clumsy but is supposedly the desired way of doing it...
//        deleteDir(dirPath, false);
//    }
    
//    private void deleteDir(String dirPath, Boolean isRecursiveCall) {
//        File dir = new File(dirPath);
//        if (dir.exists()) {
//            for (File child : dir.listFiles()) {
//                if (child.isFile()) {
//                    if (child.delete()) {
//                        //Do something/print something?
//                        //System.out.println("Deleted file '" + child.getAbsolutePath() + "'");
//                    } else {
//                        System.err.println("Failed to delete '" + child.getAbsolutePath() + "'");
//                    }
//                } else if (child.isDirectory()) {
//                    deleteDir(child.getAbsolutePath(), true);  //Recursively delete subdir contents (e.g. .../suc/)
//                    if (child.delete()) {
//                    } else {
//                        System.err.println("Failed to delete subdir '" + child.getAbsolutePath() + "'");
//                    }
//                }
//            }
//            
//            if (!isRecursiveCall && !dir.delete()) {
//                //Without the isRecursiveCall check this always fails; I guess File $dir in the parent call blocks the delete...
//                System.err.println("deleteData() failed; the initial target still exists after doing all the work.");
//            } else if (!isRecursiveCall) {
//                //Everything went fine; add prints here if you want
//            }
//        } else {
//            System.err.println("deleteData() quit because '" + dirPath + "' did not exist. Check your calls.");
//        }
//    }
    
//    private String getFileContent(String path) {
//        String line;
//        String content = "";
//         try {
//            //the input stream reader
//            BufferedReader instrom = new BufferedReader(new InputStreamReader(
//                    new FileInputStream(path), "ISO-8859-1"));
//            line = instrom.readLine();
//            while(line != null) {
//                try {
//                    content += line + "\n";
//                    line = instrom.readLine();
//                } catch(Exception ex) {
//                    System.out.println(ex.getMessage());
//                }
//            }
//            instrom.close();
//         } catch(Exception ex) {
//                 System.out.println(ex.getMessage());
//         }
//         return content;
//    }
    
    /**
     * Console printing method. Prints sentences in original order.
     *
     * @param level The simplification level to print.
     */
    public void print() { 
    	sentences.print();
    }
    
    /**
     * Console printing method. Prints sentences in priority order.
     *
     * @param level The simplification level to print.
     */
    public void printPriority(int level) {
    	sentences.printPriority();
    }
    
    /**
     * Get a sentence with a certain index at a certain level of simplification.
     *
     * @param index The index of the sentence to get.
     * @param level The simplification level to get the sentence from.
     * @return The requested Sentence object.
     */
    public Sentence getSentence(int index){
    	return sentences.get(index);
    }
    
    /**
     * Get a Sentences object representing the full text at a certain level of simplification.
     *
     * @param level The simplification level to get the Sentences from.
     * @return A Sentences object representing the full text at a certain level of simplification.
     */
    public Sentences getSentences(){
    	return sentences;
    }
    
    /**
     * @return the originalLength
     */
    public int getOriginalLength() {
        return originalLength;
    }
    
    public ArrayList<Integer> getSumIndexes(double percentageToGet) {
    	return sentences.getNPriorityIndexes(percentageToGet);
    }
    
//    public ArrayList<String> getArrayListOfStrings(){
//    	return sentences.getArrayListOfStrings();
//    }

}

