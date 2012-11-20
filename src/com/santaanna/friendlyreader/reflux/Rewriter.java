package reflux;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Rewriter applies a rule to a given array list of nodes and returns a
 * modified list of nodes.
 *
 * @author Robin Keskisärkkä
 */
public class Rewriter {
    
    public static ArrayList<Node> applyRule(ArrayList<Node> nodes, Rule rule, boolean iterate) {
        ArrayList<Node> temp = nodes;
        nodes = applyRule(nodes, rule);
        if(iterate){
            //If rules had no effect 
            if(!nodes.equals(temp)){
                nodes = applyRule(nodes, rule, iterate);
            }
        }
        return nodes;
    }

    public static ArrayList<Node> applyRule(ArrayList<Node> nodes, Rule rule) {
        //Start looking for the pattern, first try from first node, then second etc.
        //Ignore the first node as it is a null node
        HashMap<String, Node> map = new HashMap<String, Node>();
        ArrayList<Node> before = new ArrayList<Node>();
        ArrayList<Node> after = new ArrayList<Node>();
        boolean hit = false;
        for (int n = 0; n < nodes.size(); n++) {
            hit = true;
            for (int p = 0; p < rule.fromPattern.size(); p++) {
                if (nodes.size() <= p + n) {
                    break;
                }

                String ruleTag = rule.fromPattern.get(p).getTag();
                String nodeTag = nodes.get(n + p).getTag();
//                System.out.println(ruleTag + " vs " + nodeTag);
                if (!ruleTag.matches(nodeTag + "_{0,1}[0-9]*")) {
                    hit = false;
                    map.clear();
                    break;
                } else {
                    //Map the node to the tagged name
                    map.put(ruleTag, nodes.get(n + p));
                }
            }
            //If a node following the hit pattern is a child
            //of a node the pattern then hit was false.
            if(hit) {
                for(int i = rule.fromPattern.size() + n; i < nodes.size(); i++){
                    Node parent = nodes.get(i).getParentNode();
                    if(parent != null && map.containsKey(parent.getTag())){
                        hit = false;
                        map.clear();
                        break;
                    }
                }
            }
            
            //If a hit has been confirmed collect beginning and end nodes.
            if (hit) {
                for(int i=0; i < nodes.size(); i++){
                    if (i < n) {
                        before.add(nodes.get(i));
                    } else if(i > n + rule.fromPattern.size() - 1) {
                        after.add(nodes.get(i));
                    }
                }
                break;
            }
        }

        //A pattern was found, remap nodes
        if (hit) {
            ArrayList<Node> remapped = new ArrayList<Node>();
            //Add beginning nodes
            remapped.addAll(before);
            if (rule.toPattern != null) {
                for (Node resultNode : rule.toPattern) {
                    Node node = map.get(resultNode.getTag()); //Take first real node in pattern
                    if (resultNode.hasParentNode()) {
                        Node parent = map.get(resultNode.getParentNode().getTag());
                        node.setParentNode(parent);
                    }
                    remapped.add(node);
                }
            }
            //Add end nodes
            remapped.addAll(after);

//            System.out.println("Remapped:");
//            System.out.println("  From:\t" + Node.getStringFromNodes(nodes));
//            System.out.println("  To:\t" + Node.getStringFromNodes(remapped));
//            System.out.println(Node.getStanfordTree(remapped));
            return remapped;
        }

        return nodes;
    }
}
