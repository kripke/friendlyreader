/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package merge.summarization.easyreader.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author christian
 */
public class DocumentUtils {
    
    public static void copyFile(File file, String to){
        
        
        InputStream in = null;

        // skip hidden files
            if(!file.getName().startsWith(".")){
                try {
                    

                    in = new FileInputStream(file);
                    File tof = new File(to);
                    if(!tof.isDirectory())
                        tof = tof.getParentFile();
                    
                    if(!tof.exists()){
                        System.out.println("Creating "+tof);
                        tof.mkdir();
                        
                            
                    }
                    
                    OutputStream out = new FileOutputStream(to);


                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    
                    in.close();
                    out.flush();
                    out.close();
                    System.out.println("File copied.");

                } catch (IOException ex) {
                    Logger.getLogger(DocumentUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }
    
    /**
     * Saves a string to disk at the given path.
     * @param text The text to be saved.
     * @param path Where the text is saved.
     */
    public static void save(String text, String path){
        PrintWriter out = null;
        try {
            out = new PrintWriter(path);
            out.print(text);
            out.flush();
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DocumentUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }
    }
    public static String getSentences(String text){
        
        
            Pattern sentencePattern = Pattern.compile("([.!?:][\"']*[\n]*)(\\s+)([`]*[A-ZÅÄÖ0-9\\-\\–\\(”\"])");
            Matcher sm = sentencePattern.matcher(text);

            String repl = null;
            if(sm.find()){
                repl = sm.replaceAll("$1\n$3");
            }
            
            return repl;
    }
    public static String clean(String text){
        String returnString = text;
        
        returnString = returnString.replaceAll("[^A-ZÅÄÖa-zåäö.!? ',]", " ");
        returnString = returnString.replaceAll(" +", " ");
        
        return returnString;
    }
    
    public static String mkdirs(String path){
        File testIn = new File(path);
        int c = 0;
        String name;
        while(testIn.exists()){
            name = path + "_" + c;
            testIn = new File(name);
            c++;
        }
        
        testIn.mkdirs();
        
        return testIn.getPath();
    }
    
    public static void mergeCSVs(String folder){
        
        String ccheader = "";
        String header = "";
        
        HashMap<String, String[]> ccmap = new HashMap();
        HashMap<String, String[]> map = new HashMap();
        
        ArrayList<File> files = traverse(folder);
        
        for(File f : files){
            
        
            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(f)));

                String line;
                
                int i = 0;
                while((line=reader.readLine())!= null){
                    String[] row = line.split("\t");
//                    System.out.println(Arrays.toString(row));
                    
                    String[] data;
                    
                    // skip header/ first row
                    if(i != 0){
//                       System.out.println(f.getName().split("\\.")[0].replaceAll("cc", "") +"%"+ row[0]);
                        // cc file
                        String temp = (f.getName().split("\\.")[0].replaceAll("cc", "") +"%"+ row[0]).replaceAll("\"", "");
                        if(row[1].contains(".S")){
                            //String temp = (f.getName().split("\\.")[0].replaceAll("cc", "") +"%"+ row[0]).replaceAll("\"", "");
                            data = Arrays.copyOfRange(row, 2, 7);
                            ccmap.put(temp, data);
                        }
                        
                        // no cc file
                        else{
                            data = Arrays.copyOfRange(row, 2, 8);
//                            map.put(f.getName().split("\\.")[0]  +"%"+  row[0], data);
                            map.put(temp, data);
                        }
                    }
                    
                    // save header
                    else{
                        if(row[0].equals("text"))
                            header = line.replaceAll("text\t", "").replaceAll("folder\t", "");
                        else
                            ccheader = line.replaceAll("Text1\t", "").replaceAll("Text2\t", "");
                        
                    }
                    i++;
                }

            } catch (IOException ex) {
                Logger.getLogger(DocumentUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        HashMap<String, String[]> merged = new HashMap();
        
        for(String key : map.keySet()){
            System.out.println(key);
            String[] ccKeyData = ccmap.get(key);
            String[] keyData = map.get(key);
            String[] mergedData = new String[ccKeyData.length+keyData.length];
            
            int i = 0;
            for(String s : ccKeyData){
                mergedData[i] = s;
                i++;
            }
            
            for(String s : keyData){
                mergedData[i] = s;
                i++;
            }
            
            merged.put(key, mergedData);
        }
        StringBuilder csv = new StringBuilder();
        csv.append("method\ttext\t").append(ccheader).append("\t").append(header).append("\n");
        for(String s : merged.keySet()){
           csv.append(s.split("%")[0]).append("\t");
           csv.append(s.split("%")[1]).append("\t");
           
           for(String s2 : merged.get(s)){
               csv.append(s2).append("\t");
           }
           
           csv.append("\n");
        }
        save(csv.toString(), "merged.csv");
        
        
    }
    
    public static ArrayList<File> traverse(String in){
        
        ArrayList<File> returnA = new ArrayList();
        
        File inFolder = new File(in);
        File[] files = inFolder.listFiles();
        
        for(File f : files){
            if(f.isDirectory() && !f.getName().startsWith(".")){
                returnA.addAll(traverse(f.getPath()));
            }
            
            if(f.isFile())
                returnA.add(f);
        }
        
        return returnA;        
    }
    
    /**
     * Reads a text file from disk.
     * @param fileName The file name to be read.
     * @return A string containing the text in the file.
     */
    public static String readFile(String fileName){
        
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(fileName)));
            
            String line;
            StringBuilder text = new StringBuilder();
            
            while((line = reader.readLine()) != null){
                text.append(line).append("\n");
            }
            
            return text.toString();
            
        } catch (IOException ex) {
            Logger.getLogger(DocumentUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public static void main(String[] args){
        mergeCSVs("test");
    }
}
