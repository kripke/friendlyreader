package com.santaanna.friendlyreader.text;

import java.util.ArrayList;


import edu.stanford.nlp.util.Pair;
import com.santaanna.friendlyreader.utils.WordTool;

/**
 * Class for storing internal representation of a sentence. Words are stored as Word objects.
 * 
 * @author Johan Falkenjack, Christoffer Ohlsson
 * @version 1.2
 * @since 2012-09-28
 * @see Word, Sentence, Sentences, FriendlyReaderAPI
 */
public class Sentence implements PrintableTextObject {
	private Double rank;
	private int originalIndex;
	private ArrayList<Word> content;
	private boolean newline = false;

	/**
	 * Constructor initializing and assigns content for tagged words, priority rank for summarization and original index.
	 * 
	 * @param content An ArrayList of Pair of Strings representing words and their part-of-speech tags.
	 * @param rank The priority rank used in summarization.
	 * @param index The index of the sentence in the original text.
	 * @param wordTool A WordTool object used to handle synonyms.
	 */
	public Sentence(ArrayList<Pair<String, String>> content, int index, WordTool wordTool) {
		this.content = new ArrayList<Word>();
		for (Pair<String, String> wordPair : content) {
			this.content.add(new Word(wordPair.first, wordPair.second, wordTool, true));
		}
		this.originalIndex = index;
	}

	/**
	 * Constructor initializing and assigns content for untagged words, priority rank for summarization and original index.
	 * 
	 * @param content A String representing unprocessed strings.
	 * @param rank The priority rank used in summarization.
	 * @param index The index of the sentence in the original text.
	 * @param wordTool A WordTool object used to handle synonyms.
	 */
	public Sentence(String content, int index, WordTool wordTool) {
		this.content = new ArrayList<Word>();
		for (String word : content.split(" ")) {
			this.content.add(new Word(word, "NOTAG", wordTool, false));
		}
		this.originalIndex = index;
	}

	public Sentence(String string, int index) {
		if (string.equals("#NEWLINE#")) {
			this.newline = true;
			this.originalIndex = index;
			this.rank = new Double(200);
		} else {
			System.out.println("ERROR: Sentence newline constructor called for non-newline object.");
		}
	}

	public boolean isNewline() {
		return newline;
	}

	public void setRank(double rank) {
		this.rank = rank;
	}

	/**
	 * Getter for priority rank.
	 * 
	 * @return The priority rank represented as a double.
	 */
	public Double getRank() {
		return this.rank;
	}

	/**
	 * Getter for original index.
	 * 
	 * @return The original index represented as an integer.
	 */
	public int getOriginalIndex() {
		return this.originalIndex;
	}

	/**
	 * Console printing method.
	 * 
	 */
	public void print(){
		if (this.isNewline()) {
			System.out.println("Sentence index: " + this.originalIndex);
			System.out.println("Sentence rank: " + this.rank);
			System.out.println("#NEWLINE#");
		} else {
			System.out.println("Sentence index: " + this.originalIndex);
			System.out.println("Sentence rank: " + this.rank);
			for (Word word : this.content) {
				word.print();
			}
		}
	}

	public void println() {
		if (this.isNewline()) {
			System.out.println("I: " + this.originalIndex + " R: " + this.rank + " C: " + "NEWLINE");
		} else {
			String tmpSentence = "";
			for (Word word : content) {
				tmpSentence += word.getToken() + " ";
			}
			System.out.println("I: " + this.originalIndex + " R: " + this.rank + " C: " + tmpSentence);
		}
	}

	/**
	 * Getter for contents.
	 * 
	 * @return The contents represented as a String.
	 */
	public ArrayList<Word> getContent() {
		return content;
	}

}
