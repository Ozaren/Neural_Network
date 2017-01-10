package neuralNetGrid;

public class NeuralNet {
    public static final double normalize(double val) {
        return 1 / (1 + Math.exp(val));
    }
    
    private final double[][] connections;
    private final int[]      numOfNodes;
    
    public NeuralNet(int numOfInputNode , int numOfOutputNode , int... numOfHiddenNode) {
        numOfNodes = new int[2 + numOfHiddenNode.length];
        int[] partitions = new int[2 + numOfHiddenNode.length];
        
        numOfNodes[0] = partitions[0] = numOfInputNode;
        for(int i = 0; i < numOfHiddenNode.length; i++)
            partitions[i + 1] = partitions[i] + (numOfNodes[i + 1] = numOfHiddenNode[i]);
        
        partitions[partitions.length - 1] = partitions[partitions.length - 2] + (numOfNodes[numOfNodes.length - 1] = numOfOutputNode);
        
        int numOfNeurons = partitions[partitions.length - 1];
        connections = new double[numOfNeurons - numOfOutputNode][];
        
        int j = 0;
        
        for(int i = 0; i < connections.length; i++) {
            if(i >= partitions[j])
                j++;
            connections[i] = new double[numOfNodes[j + 1]];
        }
    }
    
    public void randomizeConnections() {
        for(int i = 0; i < connections.length; i++) {
            for(int j = 0; j < connections[i].length; j++) {
                connections[i][j] = Math.random() * 2 - 1;
            }
        }
    }
    
    public double[] fire(boolean... inputs) {
        if(inputs.length != numOfNodes[0])
            throw new IllegalArgumentException();
        
        double[] tempIn = new double[inputs.length];
        double[] tempOut = null;
        
        for(int i = 0; i < inputs.length; i++) {
            tempIn[i] = inputs[i] ? 1 : 0;
        }
        
        int set = 0;
        
        for(int i = 0; i < connections.length; i += numOfNodes[set++]) {
            tempOut = new double[connections[i].length];
            for(int j = 0; j < connections[i].length; j++) {
                for(int k = 0; k < numOfNodes[set]; k++) {
                    tempOut[j] += normalize(tempIn[k]) * connections[i + k][j];
                }
            }
            tempIn = tempOut;
        }
        
        for(int i = 0; i < tempOut.length; i++)
            tempOut[i] = normalize(tempOut[i]);
        
        return tempOut;
    }
    
    public static void main(String[] args) {
        NeuralNet net = new NeuralNet(8 , 2 , 16 , 12);
        net.randomizeConnections();
        for(double[] ds: net.connections) {
            for(double d: ds) {
                System.out.print(d + "\t");
            }
            System.out.println();
        }
        
        System.out.println();
        
        for(double d: net.fire(true , false))
            System.out.println(d);
    }
}
