

package merge.summarization.easyreader.util;
import java.lang.Math.*;
import java.util.Arrays;



/**
 *
 * @author Christian
 */
public class CSArrayTools {

    private static float[] array_result;

    public static float[] scalMult(float[] v1, float k){
        float[] result = new float[v1.length];

        for(int i = 0; i< v1.length; i++){
            result[i] = v1[i] * k;
        }
        return result;
    }
    
    public static float [] addVector(float[] array_one, float[] array_two){
        array_result = new float[array_one.length];
        if(array_one.length != array_two.length){
            System.err.println("Vector length differs!");
            return null;
        }
        for(int i = 0; i<array_result.length; i++)
            array_result[i] = (float)array_one[i] + (float) array_two[i];
        return array_result;
    }
    public static float[] avarage(float[] vector, int number){
        array_result = new float[vector.length];
        for(int i = 0; i<vector.length; i++)
            array_result[i] = (float) vector[i] / (float) number;
        return array_result;
    }
    public static float dot(float[] vector1, float[] vector2){
        float result = (float) 0;
        for(int i = 0; i< vector1.length; i++){
            result += (vector1[i] * vector2[i]);
        }
        return (float) result;
    }
    
    public static float taxicab(float[] v1, float[] v2){
        float result = 0;
        for(int i = 0; i < v1.length; i++){
            result += Math.abs(v1[i] - v2[i]);
        }
        return result;
    }
    public static float norm(float[] v){
        return (float) Math.sqrt(dot(v,v));
    }
    
    
    public static float[] unit(float[] v){
        float norm = norm(v);
        float[] result = new float[v.length];
        for(int i = 0; i < result.length; i++)
            result[i] = v[i] / norm;
        return result;
    }

    /**
     * Cosine between two vectors
     * @param v1 Vector 1
     * @param v2 Vector 2
     * @return The angle between the two vectors
     */
    public static float compare(float[] v1, float[] v2){
         return dot(v1,v2) /( norm(v1)*norm(v2));
    }

    public static float[] subtract(float[] vector1, float[] vector2){
        float[] result = new float[vector1.length];
        for(int i=0; i< vector1.length; i++){
            result[i] = vector1[i] - vector2[i];
        }
        return result;
    }

}
