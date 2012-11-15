
package merge.summarization.easyreader.summarization.multi;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import merge.summarization.easyreader.reader.Document;
import merge.summarization.easyreader.util.DocumentUtils;

/**
 *
 * @author christian
 */
public class MultiDocument extends Document{
    
    // Maps sentence indices to documents
    private HashMap<Integer, String> sentenceIndexMapping = new HashMap();
    
    public void readMultiFile(String path){
        ArrayList<File> files = DocumentUtils.traverse(path);
        int sentenceCounter = 0;
        Document doc = new Document();
        sentences = new ArrayList();
        for(File f : files){
            doc.readFile(f.getPath());
            
            for(String sent : doc.getSentences()){
                sentences.add(sent);
                text += sent + "\n";
                sentenceIndexMapping.put(new Integer(sentenceCounter), f.getName());
                sentenceCounter++;
            }
            
        }
        
    }
    public String getDocId(Integer sentenceIndex){
        return sentenceIndexMapping.get(sentenceIndex);
    }
}
