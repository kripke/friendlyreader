package merge.lib.com.santaanna.friendlyreader.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Pair;

/**
 * Facade class for the part-of-speech tagger in the Stanford CoreNLP
 * natural language processing library. The tagger is a maximal entropy
 * tagger.
 * 
 * @author Johan Falkenjack
 * @version 1.0
 * @since 2012-09-22
 * @see Word, Sentence
 */
public class POSTaggerHandler {

	StanfordCoreNLP pospipeline;
	String newline = System.getProperty("line.separator");
	
	/**
	 * Standard constructor. Creates a StanfordCoreNLP pipeline for POS-tagging.
	 * Tokenization and sentence splitting is performed by the pipeline as
	 * preprocessing for the part-of-speech tagging.
	 * 
	 * @param contextPath The path to a directory where a part-of-speech tagger model is placed.
	 */
	public POSTaggerHandler(String contextPath) {
		Properties posprops = new Properties();
	    posprops.put("annotators", "tokenize, ssplit, pos");
	    
	    posprops.put("pos.model", getTaggerPathString(contextPath));
		this.pospipeline = new StanfordCoreNLP(posprops);
		//System.out.println("pipeline skapad");
	}
	
	/**
	 * Badly written ad-hoc method which hopefully finds a model. Should be re-written.
	 * 
	 * @param contextPath
	 * @return
	 */
	private String getTaggerPathString(String contextPath) {
		File file = new File("resource/swedish-bidirectional-distsim2.tagger");
		
		if (!file.exists()) {
			file = new File(contextPath + "resource/swedish-bidirectional-distsim2.tagger");
		}
		//Hard coded for deployment on tomcat7 server
		if (!file.exists()) {
			file = new File("webapps/FriendlyReaderWeb/resource/swedish-bidirectional-distsim2.tagger");
		}
		
		return file.getAbsolutePath();
		
	}
	
	/**
	 * Takes a string, performs part-of-speech tagging on it and formats the result.
	 * 
	 * @param text A text which is to be tagged
	 * @return Returns an ArrayList with Pair of String representing a word and it's part-of-speech
	 */
	public ArrayList<ArrayList<Pair<String, String>>> posTagString(String text) {
		//System.out.println(StanfordCoreNLP.STANFORD_POS);
		List<CoreMap> sentences = runAnnotation(text, this.pospipeline);
		ArrayList<ArrayList<Pair<String, String>>> result = new ArrayList<ArrayList<Pair<String, String>>>();
		ArrayList<Pair<String, String>> tmpSentence;
		// TODO: Convert format to something equivalent to granska.
		for(CoreMap sentence: sentences) {
			tmpSentence = new ArrayList<Pair<String, String>>();
			for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
		        tmpSentence.add(new Pair<String, String>(token.get(TextAnnotation.class),
		        		token.get(PartOfSpeechAnnotation.class)));
			}
			result.add(tmpSentence);
		}
		System.out.println(result);
		return result;
	}
	
	/**
	 * Runs the actual annotation.
	 * 
	 * @param text The text which is to be tagged.
	 * @param pipeline The pipeline used to perform the annotation.
	 * @return Returns a List of CoreMap representing tagged sentences.
	 */
	private List<CoreMap> runAnnotation(String text, StanfordCoreNLP pipeline) {
		Annotation document = new Annotation(text);
		pipeline.annotate(document);
		return document.get(SentencesAnnotation.class);
	}

	public ArrayList<Pair<String, String>> posTagSentence(String text) {

		List<CoreMap> sentences = runAnnotation(text, this.pospipeline);
		ArrayList<Pair<String, String>> result = new ArrayList<Pair<String, String>>();
		ArrayList<Pair<String, String>> tmpSentence;
		for(CoreMap sentence: sentences) {
			tmpSentence = new ArrayList<Pair<String, String>>();
			for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
		        tmpSentence.add(new Pair<String, String>(token.get(TextAnnotation.class),
		        		token.get(PartOfSpeechAnnotation.class)));
			}
			result.addAll(tmpSentence);
		}
		System.out.println(result);
		return result;
	}
	
}
