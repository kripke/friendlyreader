package merge.robke.utils;

import java.util.Comparator;

/**
 * Enables sorting using Collections of the Tuple class. Sorting is based on the
 * T1 value
 *
 * @author Robin Keskisärkkä
 */
public class TupleComparator implements Comparator<Tuple> {

    @Override
    public int compare(Tuple t1, Tuple t2) {
        if (((String) t1.T1).compareTo((String) t2.T1) > 0){
            return 1;
        } if(((String) t1.T1).compareTo((String) t2.T1) < 0) {
            return -1;
        } if (((String) t1.T2).compareTo((String) t2.T2) > 0){
            return 1;
        } else {
            return -1;
        }
    }
}
    

