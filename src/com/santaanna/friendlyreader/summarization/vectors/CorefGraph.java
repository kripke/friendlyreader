
package com.santaanna.friendlyreader.summarization.vectors;

import java.util.ArrayList;
import java.util.Arrays;
import com.santaanna.friendlyreader.summarization.evaluation.coherence.Coref;

/**
 *
 * @author christian
 */
public class CorefGraph {


/**
 *  
 *
 * @author Christian Smith
 */


    private ArrayList<Coref> nodes;
    protected int[][] edges;
    protected int[][] outEdges;
    
    
    float[] nodeWeights;

    private void execute(){

        for(int iteration = 0; iteration < 100; iteration++){
            
            for(int vin = 0; vin< this.getSize(); vin++){
                
                float sumVin = 0;
                for(int vjn = 0; vjn < this.getSize(); vjn++){

                    int edge = edges[vin][vjn];

                    if(edge != 0){ // this is a node with at least one in/out-link
                        
                        
                        int sumVj = 0;
                        for(int j = 0; j < edges[vin].length; j++){
                            sumVj += edges[vjn][j];
                        }
                        sumVin += nodeWeights[vjn] / sumVj;
                        
                    }
                }
                
               nodeWeights[vin] = (1 - .85f) + .85f * sumVin;
                
            }
        }
    }
    
    
    public CorefGraph(int size, ArrayList corefs){
        this.nodes = corefs;
        edges = new int[size][size];
        outEdges = new int[size][size];
        
        nodeWeights = new float[size];
        Arrays.fill(nodeWeights, 0);
        for(Object cor : corefs){
            int node = ((Coref)cor).getHead();
            ArrayList<Integer> out = ((Coref)cor).getNotLocalUniqueSentenceIndices();
            for(Integer i : out){
                edges[node-1][i-1]++;
//                edges[i-1][node-1]++;
                
                outEdges[node-1][i-1]++;
            }
        }
        
        execute();
        

    }



    public int getSize(){
        return nodeWeights.length;
    }

    public int[] getNode(int index){
        return edges[index];
    }

    public float[] getNodeWeights() {
        return this.nodeWeights;
    }

    public int[][] getEdges() {
        return this.edges;
    }
    
    


}
