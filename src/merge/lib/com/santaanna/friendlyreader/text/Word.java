package merge.lib.com.santaanna.friendlyreader.text;

import java.util.ArrayList;
import merge.lib.com.santaanna.friendlyreader.utils.Tuple;
import merge.lib.com.santaanna.friendlyreader.utils.WordTool;


/**
 * Class keeping Word objects representing a single word in a text. Also keeps
 * information about part-of-speech and synonyms.
 * 
 * @author Johan Falkenjack
 * @version 1.2
 * @since 2012-09-28
 * @see Sentence, Text, FriendlyReaderAPI
 */
public class Word implements PrintableTextObject {

	private String token;
	private String pos;
	private ArrayList<String> synonyms;
	private Boolean delimiter = false;
	private final int MAX_NO_SYNONYMS = 5;


	/**
	 * Standard constructor.
	 * 
	 * @param wordString A String representing the word.
	 * @param posString A String representing it's part-of-speech.
	 * @param delimited A boolean representing whether
	 */
	public Word(String wordString, String posString, WordTool wordTool, boolean delimited) {
		this.token = wordString;
		this.pos = posString;
		if (isDelimiter(this.pos)) {
			this.delimiter = true;
		}
		if (!delimited) {
			while (hasSuffix(wordString)) {
				wordString = wordString.substring(0, wordString.length()-1);
				//System.out.println("Deleted suffix of " + wordString);
			}
			while (hasPrefix(wordString)) {
				wordString = wordString.substring(1, wordString.length());
				//System.out.println("Deleted prefix of " + wordString);
			}
		}
		synonyms = new ArrayList<String>();
		ArrayList<Tuple> tuples = wordTool.getSynonyms(wordString);
		if (tuples != null) {
			for (Tuple t : wordTool.getSynonyms(wordString, MAX_NO_SYNONYMS)) {
				synonyms.add((String)t.T2);
			}
		}
		//synonyms.add("Synonym 1");
		//synonyms.add("Synonym 2");
	}

	private boolean hasSuffix(String word) {
		String[] suffixes = {".", ",", "!", "?", "\"", ";", ":", "'", ")", "}", "]"};
		for (String suffix : suffixes) {
			if (word.endsWith(suffix)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean hasPrefix(String word) {
		String[] prefixes = {"\"", "'", "(", "{", "["};
		for (String prefix : prefixes) {
			if (word.startsWith(prefix)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Method checking whether a part-of-speech is punctuation.
	 * 
	 * @param pos A string representing the part-of-speech.
	 * @return A boolean
	 */
	private Boolean isDelimiter(String pos) {
		if (pos.equals("MAD") || pos.equals("MID")) {
			return true;
		}
		return false;
	}

	/**
	 * Printing function for testing.
	 */
	public void print() {
		System.out.print(this.token + " " + this.pos);
		for (String synonym : this.synonyms) {
			System.out.print(" " + synonym);
		}
		System.out.println();
	}

	/**
	 * Getter for the original word form or token.
	 * 
	 * @return A String representing the word form or token.
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Getter for the part-of-speech.
	 * 
	 * @return A String representing the part-of-speech.
	 */
	public String getPos() {
		return pos;
	}

	/**
	 * Getter for the synonyms. Synonyms are ordered based on
	 * their similarity to the original word.
	 * 
	 * @return An ArrayList of String containing synonyms.
	 */
	public ArrayList<String> getSynonyms() {
		return synonyms;
	}

	/**
	 * Getter for the delimiter status.
	 * 
	 * @return A boolean.
	 */
	public Boolean isDelimiter() {
		return delimiter;
	}
}
