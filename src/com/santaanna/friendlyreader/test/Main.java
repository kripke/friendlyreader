package com.santaanna.friendlyreader.test;

import java.util.Properties;

import com.santaanna.friendlyreader.engine.FriendlyReaderAPI;
import com.santaanna.friendlyreader.text.Sentences;

public class Main {

	public static void main(String args[]) {
		System.out.println("Starting main!");
		String example = 
				"Naturvetenskap och humaniora!\n" + 
				"Något som alltid lyfts fram är neandertalmänniskans " +
				"eventuella förmåga till kultur, kulturell ackumulation " +
				"och kulturell transmission. Det intressanta här är om det" +
				" fanns en skillnad i förmåga till kulturell transmission " +
				"som medförde att det är den anatomiskt moderna människan " +
				"och inte neandertalmänniskan som idag befolkar hela " +
				"jordklotet. Utifrån vad vi vet i dag verkar det som att det" +
				" är de stora kulturella skillnaderna mellan de två som " +
				"resulterat i att det bara är den anatomiskt moderna människan" +
				" som överlevt. En tidig indikation på en sådan skillnad är" +
				" huruvida neandertalmänniskan hade samma förmåga till " +
				"abstraktion som den anatomiskt moderna människan och om de" +
				" kunde producera avbildningar likt dem vi ser i grottkonsten. " +
				"Kanske kommer de genetiska skillnader som nu är på väg att " +
				"uppdagas av bland annat den svenske forskaren Svante Pääbo att" +
				" ge oss ledtrådar om skillnader i förmågan att uttrycka kultur," +
				" kulturell ackumulation och kulturell transmission. Den ena av " +
				"de två forskargrupper som nyligen i tidskrifterna Science och" +
				" Nature publicerat de första DNA-sekvenserna av kärn-DNA från" +
				" neandertalmänniska har också deklarerat att man tänker satsa" +
				" på att sekvensera den så kallade FOXP2-genen, som hos den " +
				"anatomiskt moderna människan är kopplad till tal och språk och " +
				"som skiljer sig från motsvarande gen hos schimpans. Frågan som" +
				" kommer att besvaras med den typen av analys är alltså om " +
				"neandertalmänniskan hade förmåga till kulturell transmission " +
				"via talet.\n";
		
		
//		String semikolon = "Amanda var mycket trött; en kvart senare gick hon och lade sig.";
//		
//		String semikolonOchBindestreck = "Semikolon används mellan satser - normalt huvudsatser, " +
//				"dvs. kompletta meningar - som har ett nära innehållsligt samband till varandra; " +
//				"det har då liknande funktion som tankstreck (som snarare markerar paus).";
		
		System.out.println("Creating API.");
		Properties props = new Properties();
		props.put("usePOStags", "false");
		props.put("contextPath", "");
		FriendlyReaderAPI api = new FriendlyReaderAPI(example, props);
		System.out.println("Getting sentences.");
		Sentences sentences = api.getSentences();
//		System.out.println("Meningar i ordning: ");
//		sentences.print();
//		System.out.println("\n\nMeningar i prioritet: ");
//		sentences.printPriority();
		
//		String newline = System.getProperty("line.separator");
//		System.out.println("Newline: " + newline);
		
		while (true) {
			//do nothing
		}
		
//		ResourceHandler rh = new ResourceHandler();
//		System.out.println(rh.getTaggerPath());
//		System.out.println(rh.getSynonymsPath());
		
//		String sweText = "Det h�r �r en exempelmening som anv�nds f�r att testa Stanford CoreNLP";
//		//testAndPrintAll(text);
//		
//		POSTaggerHandler facade = new POSTaggerHandler();
//		System.out.println("Testing tagger:");
//		System.out.println(facade.posTagSentence(sweText));
		//System.out.println("Testing parser:");
		//System.out.println(facade.parseString(engText));
	}

	
	
}