package merge.lib.com.santaanna.friendlyreader.utils;

import java.util.ArrayList;

/**
 * Class for identifying sentences in a text.
 *
 * @author Main code by Allan Holmström edited by Robin Keskisärkkä
 * @version 1.1
 * @since 2012-09-19
 */
public class SentenceIdentifier {


    
    /**
     * isBlank checks if 'c' is tab, space, newline etc.
     * @param c (char)
     * @return 
     */
    public static Boolean isBlank(char c) {
        if ((c == ' ') || (c == '\t') || (c == '\n') || (c == '\r') || (c == '\f')) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * isEndChar checks if 'c' is and end char.
     * @param c (char)
     * @return 
     */
    public static Boolean isEndChar(char c) {
        if ((c == '.') || (c == '!') || (c == '?') || (c == '…')) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * isUppercase checks if 'c' is an uppercase letter
     * @param c (char)
     * @return 
     */
    public static Boolean isUppercase(char c) {
        if ((c == 'Å') || (c == 'Ä') || (c == 'Ö')
                || ((c >= '0') && (c <= '9'))
                || ((c >= 'A') && (c <= 'Z'))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * findSentences identifies the sentences in 'text'
     * @param text (String)
     * @return 
     */
    public static ArrayList<String> findSentences(String text) {
        int counter = 0;
        final int regularText = 0;
        final int endChars = 1;
        final int blanks = 2;
        final int uppercase = 3;
        int state = regularText;
        String tempSentence = "";
        String tempstr = "";
        ArrayList<String> sencenceVector = new ArrayList();
        int sentenceNumber = 0;
        int k;
        char tc;
        for (k = 0; k < text.length(); k++) {
            tc = text.charAt(k);
            switch (state) {
                case regularText: // Alla tecken utom sluttecken.
                    // Vid sluttecken byt state.
                    if (isEndChar(tc)) {
                        state = endChars;
                        tempSentence += tc;
                    } else // Samma för alla efterkommande tecken.
                    {
                        tempSentence += tc;
                    }
                    break;
                case endChars: // .,! och ?
                    if (isEndChar(tc)) {
                        tempSentence += tc;
                    } else if (isBlank(tc)) {
                        state = blanks;
                        tempstr += tc;
                    } else if (isUppercase(tc)) { // Ny mening. >> Kräver INTE blanka innan!
                        // Spara tidigare mening.
                        sencenceVector.add(tempSentence);
                        sentenceNumber++;
                        tempSentence = "" + tc;
                        state = regularText;
                    } else // inte sluttecken, blank eller versal.
                    { // => fortsatt mening!
                        tempSentence += tc;
                        state = regularText;
                    }
                    break;
                case blanks: // ' ', '\t','\n'
                    // Bara blanka efter sluttecken!
                    if (isBlank(tc)) {
                        tempstr += tc;
                    } else if (isUppercase(tc)) { // Ny mening.
                        // Spara tidigare mening.
                        sencenceVector.add(tempSentence);
                        sentenceNumber++;
                        tempSentence = tempstr + tc;
                        tempstr = "";
                        state = regularText;
                    } else // Ingen ny mening!
                    {
                        tempSentence += tempstr + tc;
                        tempstr = "";
                        state = regularText;
                    }
                    break;

                case uppercase: // A-Z, Å,Ä,Ö, 0-9.
                    // Start på ny mening hittad. INGET SKALL GÖRAS!
                    break;
                default:
                    System.out.println("Inget alt vid meningsläsning.");
            }

        }
        // Efterbearbetning av resultatet!
        switch (state) {
            case regularText: // Alla tecken utom sluttecken.
                // Vid sluttecken byt state.
                sencenceVector.add(tempSentence);
                sentenceNumber++;
                break;
            case endChars: // .,! och ?
                sencenceVector.add(tempSentence);
                sentenceNumber++;
                break;
            case blanks: // ' ', '\t','\n'
                sencenceVector.add(tempSentence);
                sencenceVector.add(tempstr);
                sentenceNumber += 2;
                break;
            case uppercase: // A-Z, Å,Ä,Ö, 0-9.
                // Start på ny mening hittad. INGET SKALL GÖRAS!
                break;
            default:
                System.out.println("Inget alt vid meningsläsning.");
        }
        // Testutskrift av alla meningarna.
        // for (k=0; k< meningsnummer; k++ )
        // {
        //  System.out.println("Mening: " + meningsvektor.get(k));
        // }
        return sencenceVector;
    }

    /**
     * This method returns a string were each sentence is separated by a
     * newline character ('\n') and all superfluous  line space is removed.
     * @return 
     */
    public static String getLineSeparatedSentences(String text) {
        //Clean the text
        text = text.replaceAll("[\r\n\t ]+"," ");
        String editedText = "";
        ArrayList<String> sen = findSentences(text);
        for(String s : sen) {
            editedText += s.trim() + "\n";
        }
        return editedText;
    }
    
    public static void printSentence(String text) {
        ArrayList<String> sen= findSentences(text);
        System.out.println("Number of sentences: " + sen.size());
        for (String n : sen) {
            System.out.println(n);
        }
    }
}
