/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import edu.stanford.nlp.io.NumberRangeFileFilter;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.parser.lexparser.GrammarCompactor;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.lexparser.Options;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.Treebank;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.util.Timing;
import java.io.FileFilter;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author christian
 */
public class Parser {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Options op = new Options();
        
        LexicalizedParser lp = new LexicalizedParser("swePCFG");
        
        TreebankLanguagePack tlp = new PennTreebankLanguagePack();
        
        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
        
        for (List<HasWord> sentence : new DocumentPreprocessor("allDN.txt")) {
          Tree parse = lp.apply(sentence);
          parse.pennPrint();
          System.out.println();

//          GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
//          Collection tdl = gs.typedDependenciesCCprocessed(true);
//          System.out.println(tdl);
//          System.out.println();
        }
        
    }
}
