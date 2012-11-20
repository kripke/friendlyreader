/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package reflux;

import java.util.ArrayList;

/**
 * This class i used to parse rules into node format.
 * @author Robin Keskisärkkä
 */

public class RuleParser {
    
    /**
     * Takes a string as argument and parses it into a rule
     * @param s
     * @return rule
     */
    public static Rule parseRuleFromString(String s){
        String[] rule = s.split(" -> ");
        ArrayList<Node> fromPattern = Node.parseTreeFromString(rule[0]);
        ArrayList<Node> toPattern = Node.parseTreeFromString(rule[1]);
        return new Rule(fromPattern,toPattern);
    }
}
