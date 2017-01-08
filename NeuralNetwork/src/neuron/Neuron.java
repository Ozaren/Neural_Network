package neuron;

import java.util.ArrayList;
import java.util.Collection;

import net.connection.AbstractConnection;
import net.node.AbstractNode;
import net.node.Nodable;

public abstract class Neuron implements Nodable<Neuron.NeuronNode> {
    private static int nextid = 0;
    
    class NeuronNode extends AbstractNode<Synapse , NeuronNode> {
        public NeuronNode(NeuronNode... connections) {
            super(connections);
        }
        
        @Override
        public Synapse[] getConnections() {
            return connections.toArray(new Synapse[connections.size()]);
        }
        
        @Override
        public Synapse createConnection(NeuronNode node) {
            return new Synapse(this , node);
        }
    }
    
    class Synapse extends AbstractConnection<NeuronNode> {
        double weight;
        
        public Synapse(NeuronNode nodeLeft , NeuronNode nodeRight) {
            super(nodeLeft , nodeRight);
        }
    }
    
    public final int          id;
    
    private NeuronNode        node;
    private ArrayList<Neuron> inputs;
    private ArrayList<Neuron> outputs;
    private double            totalWeight , threshold;
    protected boolean         fired;
    
    public Neuron() {
        this(0);
    }
    
    public Neuron(double threshold) {
        inputs = new ArrayList<>();
        outputs = new ArrayList<>();
        this.threshold = threshold;
        
        fired = false;
        node = new NeuronNode();
        id = nextid++;
    }
    
    public abstract void fire();
    
    public final void reset() {
        totalWeight = 0;
        fired = false;
        for(Neuron neuron: outputs)
            neuron.reset();
    }
    
    public double getThreshold() {
        return threshold;
    }
    
    public boolean isFired() {
        return fired;
    }
    
    protected ArrayList<Neuron> getInputs() {
        return inputs;
    }
    
    protected ArrayList<Neuron> getOutputs() {
        return outputs;
    }
    
    public double getTotalWeight() {
        return totalWeight;
    }
    
    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }
    
    public void addWeightToTotal(double weight) {
        this.totalWeight += weight;
    }
    
    @Override
    public NeuronNode getNode() {
        return node;
    }
    
    @Override
    public void setNode(NeuronNode node) {
        throw new UnsupportedOperationException();
    }
    
    protected double getWeight(Neuron n) {
        return node.getConnection(n).weight;
    }
    
    public void connect(Neuron n , double weight) {
        node.addConnection(n).weight = weight;
        n.inputs.add(this);
        outputs.add(n);
    }
    
    public boolean canFire() {
        for(Neuron neuron: inputs)
            if(!neuron.fired)
                return false;
        return threshold <= totalWeight;
    }
    
    public final void setThreshold(double threshold) {
        this.threshold = threshold;
    }
    
    public int getNumOfOutputs() {
        return outputs.size();
    }
    
    @Override
    public abstract String toString();
}
