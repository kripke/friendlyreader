/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package reflux;

import java.io.*;
import java.util.ArrayList;

/**
 * This is the fundamental component for representing a phrase structure
 * parsed text. Node contains a number of useful static functions that enable users to
 * transform strings into node trees, and vice versa.
 * 
 * To do:
 * Node should contain a static reference to the Standord Parser to increase
 * efficiency and allow parsing of pure text.
 * @author Robin Keskisärkkä
 */
public class Node {

    public static double counter = 0;
    double id;
    String word = null;
    String tag;
    Node parent;

    /**
     * Create a node. Each node is automatically assigned an order number.
     *
     * @param word (null if not a leaf node)
     * @param tag (the phrase tag of the node)
     * @param parent (the parent of this node)
     */
    public Node(String tag, Node parent) {
        id = counter;
        counter++;
        this.tag = tag;
        this.parent = parent;
    }

    /**
     * Returns true if this node has no parent.
     *
     * @return
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * Set parent node.
     *
     * @param parent
     */
    public void setParentNode(Node parent) {
        this.parent = parent;
    }

    /**
     * Return nodes parent.
     *
     * @return
     */
    public Node getParentNode() {
        return parent;
    }

    /**
     * Returns true if node has a parent.
     *
     * @return
     */
    public boolean hasParentNode() {
        return parent != null;
    }

    /**
     * Returns true if node has at least one child.
     *
     * @return
     */
    public boolean hasChildNode() {
        return parent != null;
    }

    /**
     * Set a word to a node. All nodes with a words are leaves in the graph
     * tree.
     *
     * @param word
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * Returns the word of a node.
     *
     * @return
     */
    public String getWord() {
        return word;
    }

    /**
     * Returns the tag of a node.
     *
     * @return
     */
    public String getTag() {
        return tag;
    }

    /**
     * Returns true if the node is leaf node i.e. has a word.
     *
     * @return
     */
    public boolean isLeaf() {
        return word != null;
    }

    /**
     * Returns an array list of nodes as a string in stanford format.
     *
     * @param nodes
     * @return stanfordTreeString
     */
    public static String getStanfordTree(ArrayList<Node> nodes) {
        String s = "";
        int depth = 0;
        int prevDepth = -1;
        for (Node n : nodes) {
            if (n == null) {
                continue;
            }
            depth = countDepth(n);

            //Add end parentheses depending on change in depth
            int j = 0;
            while (j <= prevDepth - depth) {
                s += ")";
                j++;
            }
            if (prevDepth > -1) {
                s += "\n";
            }

            int i = 0;
            while (i < depth) {
                i++;
                s += "  ";
            }
            s += "(";
            s += n.getTag();
            if (n.isLeaf()) {
                s += " " + n.getWord();
            }
            prevDepth = depth;
        }

        //Add the last parentheses
        int j = 0;
        while (j <= depth) {
            s += ")";
            j++;
        }
        return s;
    }

    /**
     * Returns the depth of a node. The root of a sentence is at depth 0.
     *
     * @param node
     * @return depth
     */
    public static int countDepth(Node node) {
        int depth = 0;
        while (node.hasParentNode()) {
            node = node.getParentNode();
            depth += 1;
        }
        return depth;
    }
    
    /**
     * Returns a human readable text from a list of nodes.
     * @param nodes
     * @return text
     */
    public static String getStringFromNodes(ArrayList<Node> nodes) {
        String text = "";
        for (Node n : nodes) {
            if(n == null){
                continue;
            }
            if (n.isLeaf()) {
                text += n.getWord() + " ";
            }
        }
        //Remove spaces before .,;:!?)]}
        text = text.replaceAll(" ([\\.\\]\\)\\},:;?!])", "$1");
        //Remove spaces after [)}
        text = text.replaceAll("([\\{\\[]) ", "$1");
        //What about quotes?
        return text.trim();
    }
    
    /**
     * Takes a file path pointing to a text file in UTF-8 format, parsed in the
     * Stanford Parser format, as input and returns the parsed node structure.
     * @param path
     * @return nodes
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws IOException 
     */
    public static ArrayList<Node> parseTreeFromFile(String path) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(new File(path)), "UTF-8"));

        ArrayList<Node> nodes = new ArrayList<Node>();
        ArrayList<Node> parentTracker = new ArrayList<Node>();
        parentTracker.add(null);

        String tag = "";
        boolean tagIsWord = false;
        int c;
        while ((c = br.read()) != -1) {
            //A type of dot character often appears in the beginning
            //of text files. Also, ignore all '\r'.
            if (c == 65279 || Character.toString((char) c).equals("\r")) {
                continue;
            }
            String s = Character.toString((char) c);
            if (s.equals(" ") || s.equals("\n") || s.equals("\t")) {
                if (tag.equals("")) {
                    continue;
                }
                Node parent = parentTracker.get(parentTracker.size() - 1);
                if (!tagIsWord) {
                    Node n = new Node(tag, parent);
                    nodes.add(n);
                    parentTracker.add(n);
                    tag = "";
                }
                tagIsWord = true;
            } else if (s.equals(")")) {
                if(tagIsWord){
                    parentTracker.get(parentTracker.size()-1).setWord(tag);
                }
                tagIsWord = false;
                tag = "";
                parentTracker.remove(parentTracker.size() - 1);
            } else if (s.equals("(")) {
                tagIsWord = false;
            } else {
                tag += s;
            }
        }
        br.close();
        return nodes;
    }
    
     /**
     * Takes a string as input and returns the node structure text based on the 
     * Stanford Parser format.
     * @param text
     * @return nodes
     */
    public static ArrayList<Node> parseTreeFromString(String text)  {
        if(text.equals("#")){
            return null;
        }
        
        ArrayList<Node> nodes = new ArrayList<Node>();
        ArrayList<Node> parentTracker = new ArrayList<Node>();
        parentTracker.add(null);

        String tag = "";
        boolean tagIsWord = false;
        int c = 0;
        while (c < text.length()) {
            String s = Character.toString(text.charAt(c));
            c++;
            if (s.equals(" ") || s.equals("\n") || s.equals("\t")) {
                if (tag.equals("")) {
                    continue;
                }
                Node parent = parentTracker.get(parentTracker.size() - 1);
                if (!tagIsWord) {
                    Node n = new Node(tag, parent);
                    nodes.add(n);
                    parentTracker.add(n);
                    tag = "";
                }
                tagIsWord = true;
            } else if (s.equals(")")) {
                if(tagIsWord){
                    parentTracker.get(parentTracker.size()-1).setWord(tag);
                }
                tagIsWord = false;
                tag = "";
                parentTracker.remove(parentTracker.size() - 1);
            } else if (s.equals("(")) {
                tagIsWord = false;
            } else {
                tag += s;
            }
        }

        return nodes;
    }
    
   /**
     * ! -- To be implemented -- !
     * Takes a file path pointing to a text file in UTF-8 format as input and
     * returns a Stanford Parser tagged string.
     * @param path
     * @return taggedText
     */
    public static String parseFile(String path) {
        return null;
    }
    
     /**
     * ! -- To be implemented -- !
     * Takes a string as input and returns returns a Stanford Parser tagged
     * string.
     * @param text
     * @return taggedText
     */
    public static String parseString(String text){
        return null;
    }
}
