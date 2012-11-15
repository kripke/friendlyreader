package com.santaanna.friendlyreader.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import edu.stanford.nlp.util.Pair;
import edu.stanford.nlp.util.StringUtils;
import com.santaanna.friendlyreader.utils.POSTaggerHandler;
import com.santaanna.friendlyreader.utils.SentenceIdentifier;
import com.santaanna.friendlyreader.utils.WordTool;
import com.santaanna.friendlyreader.summarization.evaluation.coherence.PortableStanfordCoref;
import com.santaanna.friendlyreader.summarization.reader.Summary;
import com.santaanna.friendlyreader.summarization.summarizer.PortableCorefSum;
import com.santaanna.friendlyreader.summarization.Settings;
import com.santaanna.friendlyreader.summarization.util.DocumentUtils;


/**
 * Class keeping Sentence objects representing a text.
 * 
 * @author Johan Falkenjack, Christoffer Ohlsson
 * @version 1.2
 * @since 2012-09-28
 * @see Sentence, Text, FriendlyReaderAPI
 */
public class Sentences implements PrintableTextObject {

	private Vector<Sentence> sentences;
	private Vector<Integer> prioritySortedIndexes;
	private int length;
	private WordTool wordTool;
	private double[] ranks;
	private ArrayList<ArrayList<Pair<String, String>>> taggedSentences;
	private ArrayList<String> untaggedSentences;
	private POSTaggerHandler taggerHandler;
	private String untaggedSentenceLines;
        private String plainText="";


	/**
	 * Standard constructor initializing collections and invoking splitting and summarization of sentences.
	 * Also invokes sorting and part-of-speech tagging.
	 * 
	 * @param plain Original unprocessed text as a String.
	 * @param taggerHandler A part-of-speech tagger
	 */
	public Sentences(String plain, POSTaggerHandler taggerHandler) {
		this.wordTool = new WordTool();
		this.sentences = new Vector<Sentence>();
		this.prioritySortedIndexes = new Vector<Integer>();
		this.taggerHandler = taggerHandler;
                this.plainText = plain;
//		splitSentences(plain);
//		tagSentences();
		addSentences(true);
		generateSummarization();
		sortSentences();
		this.length = sentences.size();
	}

	/**
	 * Standard constructor initializing collections and invoking splitting and summarization of sentences.
	 * Also invokes sorting. Called when part-of-speech tagging is not used.
	 * 
	 * @param plain Original unprocessed text as a String.
	 */
	public Sentences(String plain) {
            this.plainText = plain;
		//System.out.println("In Sentences constructor, printing plain");
		//System.out.println(plain);
		// TODO: Chomp off all trailing newlines as these fuck up newline handling in the rest of the text
		// The following chomps off the last newline, but not all newlines.
		plain = StringUtils.chomp(plain);
		
		this.wordTool = new WordTool();
		this.sentences = new Vector<Sentence>();
		this.prioritySortedIndexes = new Vector<Integer>();
		splitSentences(plain);
		addSentences(false);
		generateSummarization();
		sortSentences();
		this.length = sentences.size();
	}

	/**
	 * Method which splits sentences when part-of-speech tagging is not used.
	 * 
	 * @param plain Original unprocessed text as a String.
	 */
	private void splitSentences(String plain) {
		//String newline = System.getProperty("line.separator");
		String newline = "\n";
		ArrayList<String> tmpSentences = SentenceIdentifier.findSentences(plain);
		this.untaggedSentences = new ArrayList<String>();
		StringBuilder builder = new StringBuilder();
		for (String sentence : tmpSentences) {
			// This assumes that newlines always occur at the start of sentences
			// as per SentenceIdentifier v1.1
			if (sentence.startsWith(newline)) {
				this.untaggedSentences.add("#NEWLINE#");
			}
			this.untaggedSentences.add(sentence.trim());
			builder.append(sentence.trim() + "\n");
		}
		this.untaggedSentenceLines = builder.toString();
		this.untaggedSentenceLines = untaggedSentenceLines.trim();
		for (String sentence : this.untaggedSentences) {
			//System.out.println(sentence);
		}
		//System.out.println(this.untaggedSentenceLines);
	}

	/**
	 * Method which splits and tags sentences when part-of-speech tagging is used.
	 * 
	 * @param plain Original unprocessed text as a String.
	 * @param taggerHandler A part-of-speech tagger
	 */
	private void tagSentences() {
		//POSTaggerHandler tagger = new POSTaggerHandler(contextPath);
		for (String sentenceString : this.untaggedSentences) {
			if (sentenceString.equals("#NEWLINE#")) {
				this.taggedSentences.add(null);
			} else {
				this.taggedSentences.add(this.taggerHandler.posTagSentence(sentenceString));
			}
		}
	}

	private void generateSummarization() {
		Summary sum = null;

		Settings s = new Settings();
		s.setRemoveStopWords(false);

                PortableCorefSum cs = new PortableCorefSum(s);
                PortableStanfordCoref psf = new PortableStanfordCoref();
                String out = psf.process(this.plainText);
                DocumentUtils.save(out, "parse");
                cs.setCorefFile("parse");
        
		//CogSum cs = new CogSum(s);

//		Document d = new Document();

		//this.untaggedSentenceLines = this.untaggedSentenceLines.trim();
		//this.untaggedSentenceLines.getBytes("ISO-8859-1");
		System.out.println("in generateSummarization, printing untaggedSentenceLines");
		System.out.println(plainText);
		System.out.println("end of untaggedSentenceLines");

//		d.read(this.untaggedSentenceLines);
//		cs.setText(d);
		sum = cs.buildSummary();

		if (sum != null) {
			ArrayList<String> tmpSentences = sum.getSentences();
                        System.out.println(sum.getSentences().size());
                        System.out.println(sum.getSentenceRanks().length);
			for (String sentence : tmpSentences) {
				System.out.println("in generateSummarization, printing sentence by sentence from sum");
				System.out.println(sentence);
				System.out.println("end of sentence");
			}
			System.out.println("Number of sentences: " + this.sentences.size());
			int counter = 0;
			float[] tmpRanks = sum.getSentenceRanks();
			System.out.println("Ranks:");
			for (double dub : tmpRanks) {
				System.out.println(dub);
			}
			for (Sentence sentence : this.sentences) {
//				System.out.println("in generateSummarization, printing sentence by sentence");
//				sentence.print();
//				System.out.println("end of sentence");
				if (!sentence.isNewline()) {
					System.out.println("Counter: " + counter);
					sentence.setRank(new Double(tmpRanks[counter]));
					counter++;
				}
			}
			
		} else {
			System.err.println("WARNING: SplitAndGenerateSummarization failed, CogSum is unhappy.");
			this.ranks = new double[0];  //Magic size; the below FOR loop is going to be skipped so it doesn't matter
		}
	}

	//	/**
	//     * Method splitting text into sentences, calculating summarization ranks and generating Sentence objects.
	//     * 
	//     * @param text The text to process.
	//     */
	//    private void splitAndGenerateSummarization(String text) {
	//    	Summary sum = null;
	//    	
	//    	Settings s = new Settings();
	//    	s.setRemoveStopWords(false);
	//    	
	//    	CogSum cs = new CogSum(s);
	//    	text = SentenceIdentifier.getLineSeparatedSentences(text);
	//    	Document d = new Document();
	//    	
	//    	d.read(text);
	//    	cs.setText(d);
	//    	sum = cs.buildSummary();
	//    	
	//        @SuppressWarnings("unused")
	//		ArrayList<String> tmpSentences;
	//        if (sum != null) {
	//             tmpSentences = sum.getSentences();
	//             this.ranks = sum.getSentenceRanks();
	//        } else {
	//            System.err.println("WARNING: SplitAndGenerateSummarization failed, CogSum is unhappy.");
	//            tmpSentences = new ArrayList<String>();
	//            this.ranks = new double[0];  //Magic size; the below FOR loop is going to be skipped so it doesn't matter
	//        }
	//    }

	/**
	 * Method to add a sentence.
	 * 
	 * @param tagged A boolean representing whether the sentence is part-of-speech tagged or not.
	 */
	private void addSentences(Boolean tagged) {
		//System.out.println("ranks:");
		//System.out.println(this.ranks);
		if (tagged) {
			//System.out.println("taggedSentences:");
			//System.out.println(this.taggedSentences);
			int index = 0;
			for (ArrayList<Pair<String, String>> sentence : this.taggedSentences) {
				if (sentence == null) {
					sentences.add(new Sentence("#NEWLINE#", index));
				} else {
					sentences.add(new Sentence(sentence, index, this.wordTool));
				}
				index++;
			}
		} else {
			//System.out.println("untaggedSentences:");
			//System.out.println(this.untaggedSentences);
			int index = 0;
			for (String sentence : this.untaggedSentences) {
				if (sentence.equals("#NEWLINE#")) {
					sentences.add(new Sentence("#NEWLINE#", index));
				} else {
					sentences.add(new Sentence(sentence, index, this.wordTool));
				}
				index++;
			}
		}
	}

	/**
	 * Calculates an index ordering based on sentence priority rank.
	 */
	@SuppressWarnings("unchecked")
	private void sortSentences() {
		Vector<Sentence> tmp = new Vector<Sentence>(sentences);
		Collections.sort(tmp, new SentenceRankComparator());
		for (Sentence s : tmp) {
			prioritySortedIndexes.add(s.getOriginalIndex());
		}
	}

	/**
	 * Getter for Sentence objects based on index.
	 * 
	 * @param index The index of the Sentence object to get.
	 * @return The Sentence object with the provided index.
	 */
	public Sentence get(int index) {
		return sentences.get(index);
	}

	/**
	 * Console printing method. Prints all sentences in original order.
	 */
	public void print() {
		for (Sentence s : sentences){
			s.print();
		}
	}

	/**
	 * Console printing method. Prints all sentences in priority order.
	 */
	public void printPriority() {
		for (Integer i : prioritySortedIndexes){
			sentences.get(i).print();
		}
	}

	//    /**
	//     * gets an ArrayList of all sentences as Strings. Used by legacy GUI.
	//     * 
	//     * @return ArrayList of all sentences as Strings
	//     */
	//    public ArrayList<String> getArrayListOfStrings(){
	//        ArrayList<String> tmp = new ArrayList<String>();
	//        for (Sentence s : sentences){
	//            tmp.add(s.getContent());
	//        }
	//        return tmp;
	//    }

	//    /**
	//     * Concatenates all Sentence objects, separated with one space (" "), into the full text. Suitable for printing/processing.
	//     * @return A string containing the full text separated by a single space for each Sentence
	//     */
	//    public String toFullString() {
	//        String retval = "";
	//        for (Sentence s : sentences) {
	//            retval += s.getContent() + " ";
	//        }
	//        
	//        return retval;
	//    }

	/**
	 * @return the length
	 */
	public int getLength() {
		return this.length;
	}

	/**
	 * Get indexes of the N% most important sentences
	 * 
	 * @param percentageToGet The percentage of the text which is to be included in the summary.
	 * @return An ArrayList representing the indexes of the included sentences.
	 */
	public ArrayList<Integer> getNPriorityIndexes(double percentageToGet) {
		double percentage = (((double)percentageToGet) / 100);
		int lengthToGet = (int) Math.round(this.length*percentage);
		ArrayList<Integer> res = new ArrayList<Integer>();
		for (int i = 0; i < lengthToGet; i++) {
			res.add(prioritySortedIndexes.get(i));
		}
		return res;
	}

	/**
	 * @return A Vector of Sentence
	 */
	public Vector<Sentence> getSentences() {
		return this.sentences;
	}

	public ArrayList<Integer> getNewlines() {
		ArrayList<Integer> res = new ArrayList<Integer>();
		for (Sentence sentence : this.sentences) {
			if (sentence.isNewline()) {
				res.add(sentence.getOriginalIndex());
			}
		}
		return res;
	}
}
