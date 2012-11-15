

package com.santaanna.friendlyreader.summarization.summarizer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.santaanna.friendlyreader.summarization.reader.Document;
import com.santaanna.friendlyreader.summarization.reader.Summary;
import com.santaanna.friendlyreader.summarization.reader.SummaryReader;
import com.santaanna.friendlyreader.summarization.Settings;

/**
 *
 * @author christian
 */
public class CSBasic {
static void usage(){
        System.out.println("Usage:");
        System.out.println(
                "\n"
                + "EasyReaderU <in_files> <out_folder> <percentage> <append values=true/false>\n\n "

                + "in_files = folder containing txt files or a single txt file\n "
                + "out_folder = output summaries here\n "
                + "percentage = the amount of text to keep in the summary 0-100\n "
                + "append values, true/false = each sentence's rank are appended after each sentence with a delimiter\n");
    }
    public static void main(String[] args){

        try {
            if (args.length != 4) {
                usage();
                return;
            }
            
            // infile/folder
            File inFolder = new File(args[0]);
            
            //outfile/folder
            String outFolder = args[1];
            
            //percentage
            int percent = Integer.parseInt(args[2]);
            
            //whole text tagged?
            boolean tag = Boolean.parseBoolean(args[3]);
            
            
            StringBuilder builder = new StringBuilder();
            
            //settings
            System.out.println("Reading settings...");
            Settings s = new Settings();
            s.setLeftWindowSize(2);
            s.setRightWindowSize(2);
            s.setRandomDegree(4);
            s.setRandomSeed(22);
            s.setRemoveStopWords(false);
            s.setRemoveDocumentVector(true);
            s.setUsePageRank(true);
            s.setPageRankIterations(50);
            s.setPageRankFactorD(.85f);
            s.setDimensionality(100);
            System.out.println(s);
            
            
            //summarizer
            CogSum cs = new CogSum(s);
            OutputStreamWriter ow;

             //out directory
            new File(outFolder).mkdir();

            File[] inFiles;
            String fileName = ""; //output filename
            String outPath = "";

            if(inFolder.isDirectory()){
                inFiles = inFolder.listFiles();
                fileName = inFolder.getAbsolutePath() + "/";
                outPath = inFolder.getName();
            }
             else{
                inFiles = new File[1];
                inFiles[0] = inFolder;

             }
                System.out.println("Fetching files...");

            for (File in : inFiles) {

                //create output streams
                String fileOut = outFolder + "/"+outPath + in.getName();
                ow = new OutputStreamWriter(new FileOutputStream(fileOut), "UTF-8");
                BufferedWriter buffer = new BufferedWriter(ow);
                PrintWriter pw = new PrintWriter(buffer);


                System.out.println("saving " + fileOut + "...");
                if(inFolder.isDirectory()){
                    fileName = inFolder.getAbsolutePath() + "/";
                }
                //read text from infile
                String fn = in.getName();
                fileName = fileName + fn;
                Document d = new Document();
                d.readFile(fileName);
                System.out.println("Summarizing " + fileName);
                cs.setText(d);
                
                Summary summ = cs.buildSummary();
                SummaryReader reader = new SummaryReader(summ);

                if (!cs.getError()) {
                    String sum = "";

                    sum = reader.retrieveSummary(percent, tag);
                    pw.print(sum);
                    pw.flush();

                }

                else { //error in cogsum
                    System.err.println("Error in cogsum");
                }

                pw.close();
            }
           
        }

        catch (FileNotFoundException ex) {
            Logger.getLogger(CSBasic.class.getName()).log(Level.SEVERE, null, ex);
        }        catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CSBasic.class.getName()).log(Level.SEVERE, null, ex);
        }    }
}
