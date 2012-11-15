
package merge.summarization.easyreader.util;

/**
 * Experimental
 * @author Christian
 */
public class Sphere {
    public boolean test(float R, float[] v){
        if(CSArrayTools.norm(v) < R)
            return true;
        else
            return false;

//        float C = Math.pow(Math.PI, n/2) / factorial(n/2);
//        float V = C * Math.pow(R, n);


    }

    public static long factorial( int n )
    {
        if( n <= 1 )     // base case
            return 1;
        else
            return n * factorial( n - 1 );
    }
}
