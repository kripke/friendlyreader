

package merge.summarization.easyreader.vectors;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *  Simple fully connected undirected graph where each node is a vector of
 * floats
 *
 * @author Christian Smith
 */
public class SimpleVectorGraphDirected extends SimpleVectorGraph{

    private float[] documentVector;

    private ArrayList<float[]> nodes;
//    private float[] nodeWeights;
    private float[][] edgeWeights;

    private float[][] inWeights;
    private float[][] outWeights;

    public SimpleVectorGraphDirected(ArrayList<float[]> nodes){
        super(nodes);

//        this.nodes = nodes;
//        nodeWeights = new float[nodes.size()];
//
//        // random starting values
//        for(int i = 0; i < nodeWeights.length; i++)
//            nodeWeights[i] = 0;

        
        //edgeWeights = new float[nodeWeights.length][nodeWeights.length];
        inWeights = new float[nodeWeights.length][nodeWeights.length];
        outWeights = new float[nodeWeights.length][nodeWeights.length];

    }

    public float[] getDocumentVector(){
        return this.documentVector;
    }
    public void setDocumentVector(float[] v){
        this.documentVector = v;
    }

    public void setInEdge(int i, int j, float v){
        inWeights[i][j] = v;
    }
    public float getInEdge(int i, int j){
        return inWeights[i][j];
    }

    public void setOutEdge(int i, int j, float v){
        outWeights[i][j] = v;
    }
    public float getOutEdge(int i, int j){
        return outWeights[i][j];
    }

}