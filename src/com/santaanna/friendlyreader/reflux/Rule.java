package reflux;

import java.util.ArrayList;

/**
 *
 * @author Robin Keskisärkkä
 */
public class Rule {
    public ArrayList<Node> fromPattern;
    public ArrayList<Node> toPattern;
    
    public Rule(ArrayList<Node> fromPattern, ArrayList<Node> toPattern){
        this.fromPattern = fromPattern;
        this.toPattern = toPattern;
    }
}
