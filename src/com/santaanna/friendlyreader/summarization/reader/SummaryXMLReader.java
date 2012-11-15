
package com.santaanna.friendlyreader.summarization.reader;

import java.io.File;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 *
 * @author christian
 */
public class SummaryXMLReader {
    public static void main(String[] args){
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {

            SAXParser saxParser = factory.newSAXParser();
            SummaryHandler handler = new SummaryHandler();
            saxParser.parse( new File("out.xml"), handler);
            System.out.println(handler.getResult().getSentences());
        } catch (Throwable err) {
            err.printStackTrace ();
        }
    }

    public static Summary read(String fileName){
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {

            SAXParser saxParser = factory.newSAXParser();
            SummaryHandler handler = new SummaryHandler();
            saxParser.parse( new File(fileName), handler);
            return handler.getResult();

        } catch (Throwable err) {
            err.printStackTrace ();
        }
        return null;

    }



}
