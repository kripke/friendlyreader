

package merge.summarization.easyreader.vectors;

import java.util.ArrayList;
import java.util.Arrays;
import merge.summarization.easyreader.util.CSArrayTools;

/**
 *  Simple fully connected undirected graph where each node is a vector of
 * floats
 *
 * @author Christian Smith
 */
public class SimpleVectorGraph {

    private ArrayList<float[]> nodes;
    protected float[] nodeWeights;
    private float[][] edgeWeights;
    private float[] edgeSums;

    public SimpleVectorGraph(){

    }
    private void buildGraph(){

        float sumWji = 0;
        float wkj;
        float sumWkj = 0;
        float wji;

        //nodes
        float[] vi;
        float[] vj;
        float[] vk;

        for(int vin = 0; vin< this.getSize(); vin++){
            vi = this.getNode(vin);
            for(int vjn = 0; vjn < this.getSize(); vjn++){
                
                vj = this.getNode(vjn);
                
                wji = (float)CSArrayTools.compare(vi, vj);
                
                this.setEdgeWeight(vin, vjn, wji);
            }
        }

    }
    public SimpleVectorGraph(ArrayList<float[]> nodes){
        this.nodes = nodes;
        nodeWeights = new float[nodes.size()];

        // random starting values
        for(int i = 0; i < nodeWeights.length; i++)
            nodeWeights[i] = 0;

        
        edgeWeights = new float[nodeWeights.length][nodeWeights.length];
        buildGraph();
        edgeSums = new float[nodeWeights.length];

        for(int i = 0; i < nodeWeights.length; i++){
            float temp = 0;
            for(int j = 0; j < nodeWeights.length; j++){
                if(i == j)
                    edgeWeights[i][j] = 0f;
                temp += edgeWeights[i][j];
//                System.out.println(i + " " + j + " " + edgeWeights[i][j]);
            }
            edgeSums[i] = temp;
        }

    }

    public float[] getEdgeSums(){
        return this.edgeSums;
    }
    public float[][] getEdges(){
        return edgeWeights;
    }

    public int getSize(){
        return nodes.size();
    }

    public float[] getNode(int index){
        return nodes.get(index);
    }

    public void setWeight(int node, float weight){
        this.nodeWeights[node] = weight;

    }

    public Float getEdgeWeight(int i, int j){
//        int x = i > j ? j : i;
//        int y = i > j ? i : j;
        return this.edgeWeights[i][j];
    }

    public float[] getNodeWeights(){
        return nodeWeights;
    }

    @Override
    public String toString(){
        return Arrays.toString(nodeWeights);
    }

    public void setEdgeWeight(int i, int j, float wji) {
//        int x = i > j ? j : i;
//        int y = i > j ? i : j;
        this.edgeWeights[i][j] = wji;
    }

    public float[][] printEdgeWeights(){
        for(int i = 0; i < edgeWeights.length; i++){
            System.out.println(Arrays.toString(edgeWeights[i]));
        }
        return  this.edgeWeights;
    }
}