
package merge.summarization.easyreader.summarization;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import merge.summarization.easyreader.reader.Document;

/**
 *
 * @author christian
 */
public class TagSum {
    static void usage(){
        System.out.println("Usage:");
        System.out.println("EasyReaderU <in_folder> <out_folder> <percentage>");
    }
    public static void main(String[] args){

        try {
            if (args.length != 4) {
                usage();
                return;
            }
                    //17,33,50,67,83
            int[] sizes = {17, 33, 50, 67, 83};
            File inFolder = new File(args[0]);
            String outFolder = args[1];
            int percent = Integer.parseInt(args[2]);
            boolean tag = Boolean.parseBoolean(args[3]);
            StringBuilder builder = new StringBuilder();
            System.out.println("Reading settings...");
            Settings s = new Settings();
            s.setLeftWindowSize(2);
            s.setRightWindowSize(2);
            s.setRandomDegree(4);
            s.setRandomSeed(22);
            s.setRemoveStopWords(false);
            s.setRemoveDocumentVector(true);
            s.setUsePageRank(false);
            s.setPageRankIterations(50);
            s.setPageRankFactorD(.85f);
            s.setDimensionality(100);
            System.out.println(s);
            CogSum cs = new CogSum(s);
            File[] inFiles = inFolder.listFiles();
            System.out.println("Fetching files...");
            OutputStreamWriter ow;
            //                    FileWriter fw = null;
            new File(outFolder).mkdir(); //create directory
            
            //                    fw = new FileWriter(fileOut);

            for (File in : inFiles) {
                String fileOut = outFolder + "/"+inFolder.getName() + in.getName()+ ".html";
                ow = new OutputStreamWriter(new FileOutputStream(fileOut), "UTF-8");
                System.out.println("saving " + fileOut + "...");
                BufferedWriter buffer = new BufferedWriter(ow);
                PrintWriter pw = new PrintWriter(buffer);
                String fn = in.getName();
                Document d = new Document();
                String fileName = inFolder.getAbsolutePath() + "/" + fn;
                d.readFile(fileName);
                System.out.println("Summarizing " + fileName);
                cs.setText(d);
                cs.buildSummary();
                if (!cs.getError()) {
                    String sum = "";

                    for(int i = 0; i < sizes.length; i++){
                        pw.print(sizes[i] + "% ---------------------------------------------------------------------------------\n<br>");

                        if (tag) {
//                            sum = cs.retrieveTaggedSummary(sizes[i]);
                        }

                        else {
//                            sum = cs.retrieveSummary(sizes[i], false);
                        }
                        pw.print(sum);
                        pw.print("\n--------------------------------------------------------------------------------------------\n");
                        pw.print("<br><br><br>");
                        pw.flush();
                        
                    }
                    pw.close();

                } else {
                    System.err.println("Error in cogsum");
                }
            }
           
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TagSum.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TagSum.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
