
package com.santaanna.friendlyreader.summarization.vectors;

import java.util.Arrays;
//import org.jblas.FloatMatrix;

/**
 * Takes a fully connected undirected graph of vectors and performs
 * Weighted PageRank, where the weights equals the cosine measures of the
 * vectors.
 *
 * @author Christian Smith
 */
public class VectorRank {
    private float factorD = .85f;
    private int iterations = 50;
    private SimpleVectorGraph graph;
    public static StringBuilder builder = new StringBuilder();
    private boolean verbose0 = false;
    private boolean fail = false;
//        FloatMatrix mEdges;
//        FloatMatrix mNodes;
//        FloatMatrix mEdgeSum;
    public VectorRank(SimpleVectorGraph graph){
        this.graph = graph;

    }

    public boolean getSuccess(){
        return !fail;
    }

   
    public void setFactorD(float f){
        this.factorD = f;
    }
    
    public void setIterations(int i){
        this.iterations = i;
    }

    public void execute(){
        
        float sumWji = 0;

        long t = System.currentTimeMillis();

        for(int iteration = 0; iteration < this.iterations; iteration++){
            float newWeight = 0;

            if(verbose0){
                System.out.println(Arrays.toString(this.graph.getNodeWeights()));
            }

            for(int vin = 0; vin< this.graph.getSize(); vin++){
                sumWji = 0; //reset sumWji

                sumWji = this.fastSumInt(vin,
                        graph.getNodeWeights(),
                        graph.getEdgeSums());

                newWeight = (float) ((1 - this.factorD) +
                            (this.factorD * sumWji));

                this.graph.setWeight(vin, newWeight);
//                graph.printEdgeWeights();

            }
        }

        long newT =System.currentTimeMillis()-t;
//        System.out.println(Arrays.toString(this.graph.getNodeWeights()));
//        System.out.println(this.graph.getNodeWeights().length);
//        System.err.println("Time: " + newT);

    }
   public SimpleVectorGraph getGraph(){
       return graph;
   }
   public float fastSumInt(int edgeI, float[] nodes, float[] edgeSum){

        float[] res = new float[nodes.length];
        for(int i = 0; i < nodes.length; i++){
            res[i] = nodes[i] / edgeSum[i];
        }
        for(int i = 0; i < nodes.length; i++){
            res[i] = graph.getEdges()[edgeI][i] * res[i];
        }
        float sum =0 ;
        for(int i = 0; i < nodes.length; i++){
           sum += res[i];
        }
        return sum;
    }
   
//   public float fastSum(int edgeI, float[] nodes, float[] edgeSum){
//
//       if(mEdges==null)
//            mEdges = new FloatMatrix(graph.getEdges());
//
//        mNodes = new FloatMatrix(nodes);
//
//        if(mEdgeSum==null)
//            mEdgeSum = new FloatMatrix(edgeSum);
//
//        FloatMatrix res = mNodes.div(mEdgeSum);
//        res = mEdges.getColumn(edgeI).mul(res);
//
//        return res.sum();
//    }

   public float oldSumOpt(int vin){
        float wji, sumWkj, sumWji = 0;
        for(int vjn = 0; vjn < this.graph.getSize(); vjn++){
            if(!(vjn == vin)){
                wji = this.graph.getEdgeWeight(vin, vjn);
                sumWkj = 0f;
                sumWkj = this.graph.getEdgeSums()[vjn];

                if(!Float.isNaN(wji)){
                    sumWji += (float)(wji * (graph.getNodeWeights()[vjn]
                            / sumWkj));
                }
            }
        }

        return sumWji;
    }

   public float oldSum(int vin){

        float wji, sumWkj, wkj, sumWji = 0;

        for(int vjn = 0; vjn < this.graph.getSize(); vjn++){

            if(!(vjn == vin)){
                wji = this.graph.getEdgeWeight(vin, vjn);
                sumWkj = 0f;
                for(int vkn = 0; vkn < this.graph.getSize(); vkn++){
                    if(!(vkn==vjn)){
                        wkj = this.graph.getEdgeWeight(vkn, vjn);
                        sumWkj += wkj;
                    }
                }
                if(!Float.isNaN(wji)){
                    sumWji += (float)(wji * (graph.getNodeWeights()[vjn]
                            / sumWkj));
                }

            }
        }

        return sumWji;
    }
}
