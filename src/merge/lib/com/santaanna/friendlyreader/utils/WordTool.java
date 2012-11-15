package merge.lib.com.santaanna.friendlyreader.utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import merge.lib.com.santaanna.friendlyreader.resource.ResourceHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 *
 * @author Robin Keskisärkkä
 */
public class WordTool {

    private HashMap map = new HashMap();
    //private URL path;
    private String path = "resource/synonyms.xml";
    
    public WordTool() {
        //Load map
        loadSynonyms();
    }

    /**
     * Return all synonyms in tuple form of a specific word, ordered by synonym
     * strength.
     * @param word
     * @return 
     */
    public ArrayList<Tuple> getSynonyms(String word) {
        return getSynonyms(word, -1);
    }
    
    /**
     * Return synonyms for a specific word. Limit sets the maximum amount
     * of synonym tuples returned. The synonyms are ordered by synonym strength.
     * Returns null if the no synonyms are found.
     * @param word
     * @param limit
     * @return 
     */
    public ArrayList<Tuple> getSynonyms(String word, int limit) {
        word = word.trim().toLowerCase();
//        System.out.println("Get synonyms of: " + word);
        if(!map.containsKey(word)) {
            return null;
        }
        
        ArrayList<Tuple> synonyms = (ArrayList<Tuple>)map.get(word);
        if(synonyms != null) {
            TupleComparator comparator = new TupleComparator();
            Collections.sort(synonyms,comparator);
        }
//        for(Tuple t : synonyms) {
//            System.out.println(t.T1 + "," + t.T2);
//        }
        
        
        ArrayList<Tuple> list = new ArrayList<Tuple>();
        int i = 0;
        while(i < synonyms.size() && i < limit) {
            list.add(synonyms.get(i));
            i++;
        }
        return list;
    }

    /**
     * Loads the file containing synonyms into a hash map where each word has an
     * array containing its synonyms as value.
     */
    private void loadSynonyms() {
        try {
            map = new HashMap();
            //Local
            File file = new File("./" + path);
            //Deployed in tomcat 7
            if (!file.exists()) {
            	file = new File("webapps/FriendlyReaderWeb/"+ path);
            }
//            File file = new File( URLDecoder.decode( this.path.getFile(), "UTF-8" ) );
//            try {
//            	file = new File(this.url.toURI());
//            } catch(URISyntaxException e) {
//            	file = new File(this.url.getPath());
//            } catch(IllegalArgumentException e) {
//            	file = new File(this.url.getPath());
//            }
//            
//            System.out.println("URL: " + url);
//            System.out.println("File: " + file.getAbsolutePath());
            
            file = new File(Thread.currentThread().getContextClassLoader().getResource("com/santaanna/friendlyreader/resource/synonyms.xml").getFile());
			
            
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(ResourceHandler.getSynonymsPath());
            doc.getDocumentElement().normalize();
            NodeList listOfWordPairs = doc.getElementsByTagName("syn");

            for (int s = 0; s < listOfWordPairs.getLength(); s++) {
                Node entryNode = listOfWordPairs.item(s);
                Element entryElement = (Element) entryNode;

                //Get word 1
                NodeList wordList = entryElement.getElementsByTagName("w1");
                Element wordElement = (Element) wordList.item(0);
                NodeList wordElementList = wordElement.getChildNodes();
                String word1 = ((Node) wordElementList.item(0)).getNodeValue();

                //Get word 2
                wordList = entryElement.getElementsByTagName("w2");
                wordElement = (Element) wordList.item(0);
                wordElementList = wordElement.getChildNodes();
                String word2 = ((Node) wordElementList.item(0)).getNodeValue();

                String level = entryElement.getAttribute("level");
                Tuple t = new Tuple(level,word2);
                
                if (map.containsKey(word1)) {
                    ArrayList<Tuple> set  = (ArrayList<Tuple>) map.get(word1);
                    set.add(t);
                } else {
                    ArrayList<Tuple> set = new ArrayList<Tuple>();
                    map.put(word1, set);
                }
            }
            System.out.println("This is the end of loading fused xml. Has size " + map.size());
        } catch (SAXException ex) {
            Logger.getLogger(WordTool.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
        	Logger.getLogger(WordTool.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(WordTool.class.getName()).log(Level.SEVERE, null, ex);
		}
    }
}
