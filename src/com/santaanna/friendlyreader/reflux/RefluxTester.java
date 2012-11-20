/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package reflux;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * RefluxTester is the new version of CogFLUX. It uses the Stanford parser and
 * enables texts to be modified using rewrite rules that can be formulated in
 * pure text.
 *
 * The rule definition language can remove or move phrase structures within a
 * that fits a certain pattern.
 * 
 * Syntax:
 * RuleString = FROM_PATTERN ' -> ' TO_PATTERN
 * FROM_PATTERN = PATTERN
 * TO_PATTERN = PATTERN | '#'
 * PATTERN = '(' TAG [LABEL] ' ' place_holder | PATTERN ')' [' ' PATTERN]
 * TAG = tag | '*'
 * LABEL = '_' NUMBER
 * 
 * In the  syntax 'tag' is a Stanford Parser phrase structure label,
 * 'NUMBER' is a positive integer, and 'place_holder' can be any type of 
 * non-empty string. The wildcard character '*' can be used to replace any possible
 * tag and may be labeled just as regular tags. Setting the TO_PATTERN to '#'
 * deletes the FROM_PATTERN.
 * While place_holder has no formal function in the rule pattern it can be very
 * useful to demonstrate the typical effects of a rule, allowing the implemented
 * rule to be read and understood easily.
 * 
 * For safety reasons rules can not be applied if a node in a pattern is a parent
 * of a node outside the pattern, that is, the structure must be described in full.
 * Labeling of tags is therefore highly recommended in most rule implementations,
 * since ambiguousness may otherwise be introduced in the surronuding structures.
 * 
 * Below follow a few examples of rules defined using the above syntax:
 * 
 *
 * Common errors and mistakes:
 * 1. The rule pattern doesn't match.
 *    Double-check your syntax. Also, make sure that you if you're parsing a 
 *    text file it is written in UTF-8 format.
 * 2. The pattern matches but the text doesn't change.
 *    Make sure that your TO_PATTERN is not refering to nodes that are not in the
 *    FROM_PATTERN
 * 
 * @author Robin Keskisärkkä
 */
public class RefluxTester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RefluxTester reflux = new RefluxTester();
        reflux.test2_multi_rules();
        
    }

    public void test1_single_rules() {
        ArrayList<Node> nodes = null;
        nodes = Node.parseTreeFromString("(ROOT (NP (N Robin)) (VP (VB jagar) (N kanin)))");
        System.out.println(Node.getStanfordTree(nodes));
        
        //Test 1: Move nodes using full matching of nodes in a text.
        //From:	Robin jagar kanin
        //To:	jagar kanin Robin
        System.out.println("\nTest 1:");
        String rule = "(ROOT (NP (N_1 *)) (VP (VB *) (N_2 *))) -> (ROOT (VP (VB *) (N_2 *)) (NP (N_1 *)))";
        Rule rule1 = RuleParser.parseRuleFromString(rule);
        Rewriter.applyRule(nodes, rule1);
        
        //Test 2: Deletion of nodes using full matching of nodes.
        //From:	Robin jagar kanin
        //To:	Robin jagar
        System.out.println("\nTest 2:");
        rule = "(ROOT (NP (N_1 *)) (VP (VB *) (N_2 *))) -> (ROOT (N_1 *) (VB *))";
        Rule rule2 = RuleParser.parseRuleFromString(rule);
        Rewriter.applyRule(nodes, rule2);
        
        //Test 3: Wild card. Wild cards identify 1 node and can be tagged with a
        //number to avoid ambiguousness
        //From:	Robin jagar kanin
        //To:	jagar kanin Robin
        System.out.println("\nTest 3:");
        rule = "(ROOT (*_1 (*_2 *)) (VP (VB *) (N_2 *))) -> (ROOT (VP (VB *) (N_2 *)) (*_1 (*_2 *))))";
        Rule rule3 = RuleParser.parseRuleFromString(rule);
        Rewriter.applyRule(nodes, rule3);
        
        //Test 4: Partial pattern hit.
        //# denotes any number of wild card nodes
        //From:	Robin jagar kanin
        //To:	Robin kanin jagar
        System.out.println("\nTest 4:");
        rule = "(VB *) (N_2 *) -> (N_2 *) (VB *)";
        Rule rule4 = RuleParser.parseRuleFromString(rule);
        Rewriter.applyRule(nodes, rule4);
    }
    
    public void test2_multi_rules(){
        ArrayList<Node> nodes = null;
        try {
            nodes = Node.parseTreeFromFile("data/testfuse.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RefluxTester.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(RefluxTester.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RefluxTester.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Remove all nouns
        Rule rule4 = RuleParser.parseRuleFromString("(ROOT_1 (NP_1 (DET_1 En) (N_1 bil) (VP_1 (VB_1 åkte) (AD_1 fort))) (PUNKT_1 .)) "
                                                  + "(ROOT_2 (NP_2 (DET_2 En) (ADJ_1 gul) (N_2 bil) (VP_2 (VB_2 åkte) (PP_1 på) (N_3 gatan))) (PUNKT_2 .)) -> "
                                                  + "(ROOT_1 (NP_1 (DET_1 En) (ADJ_1 gul) (N_1 bil) (VB_1 åkte) (AD_1 fort) (PP_1 på) (N_3 gatan) (PUNKT_1 .)))");
        System.out.println(Node.getStringFromNodes(nodes));
        nodes = Rewriter.applyRule(nodes, rule4, false);
        System.out.println(Node.getStringFromNodes(nodes));
        
//        String[] s1 = Node.getStringFromNodes(nodes).replaceAll("([!\\.\\?]+)","$1#").split("#");
//        nodes = Rewriter.applyRule(nodes, rule4, false);
//        String[] s2 = Node.getStringFromNodes(nodes).replaceAll("([!\\.\\?]+)","$1#").split("#");
//        for(int i = 0; i < s1.length && i < s2.length; i++){
//            System.out.println("Före:\t" + s1[i].trim());
//            System.out.println("Efter:\t" + s2[i].trim());
//        }
        
        
    }
}
