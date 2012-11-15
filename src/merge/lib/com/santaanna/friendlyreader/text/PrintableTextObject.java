package merge.lib.com.santaanna.friendlyreader.text;



/**
 * Interface for text representation objects.
 *
 * @author Johan Falkenjack
 * @version 1.0
 * @since 2012-09-28
 * @see Sentence, Sentences, Text
 */
public interface PrintableTextObject {

	/**
	 * All classes implementing this interface must be printable and
	 * therefore implement a print method.
	 */
	public void print();
	
}
