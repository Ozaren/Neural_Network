package neuron;

import java.util.ArrayList;

import net.connection.AbstractConnection;
import net.node.AbstractNode;
import net.node.Nodable;

public abstract class Neuron implements Nodable<Neuron.NeuronNode> {
    private static int nextid = 0;
    
    public class NeuronNode extends AbstractNode<Synapse , NeuronNode> {
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
        
        public Neuron getNeuron() {
            return THIS;
        }
        
        @Override
        public String getName() {
            return THIS.getType() + " Neuron Node " + THIS.id;
        }
    }
    
    public class Synapse extends AbstractConnection<NeuronNode> {
        double weight;
        
        public Synapse(NeuronNode nodeLeft , NeuronNode nodeRight) {
            super(nodeLeft , nodeRight);
        }
        
        public double getRawWeight() {
            if(nodeLeft.getNeuron() instanceof NeuronInput)
                return ((NeuronInput) nodeLeft.getNeuron()).getWeight();
            else
                return weight;
        }
        
        public double getWeight() {
            
            if(nodeLeft.getNeuron().isFired())
                return getRawWeight();
            else
                return 0;
        }
        
        @Override
        public String toString() {
            return String.format("%d <-> %d" , nodeLeft.getNeuron().id , nodeRight.getNeuron().id);
        }
    }
    
    private final Neuron      THIS = this;
    
    public final int          id;
    
    private NeuronNode        node;
    private ArrayList<Neuron> inputs;
    private ArrayList<Neuron> outputs;
    private double            totalWeight , threshold;
    protected boolean         fired;
    public boolean            invisible;
    
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
    
    protected abstract String getType();
    
    public double getThreshold() {
        return threshold;
    }
    
    public boolean isFired() {
        return fired;
    }
    
    public ArrayList<Neuron> getInputs() {
        return inputs;
    }
    
    public ArrayList<Neuron> getOutputs() {
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
    
    public boolean hasInput() {
        if(this instanceof NeuronInput)
            return true;
        for(Neuron neuron: inputs) {
            if(neuron.hasInput())
                return true;
        }
        return false;
    }
    
    public boolean hasInput(Neuron n) {
        return inputs.contains(n);
    }
    
    public boolean hasOutput(Neuron n) {
        return outputs.contains(n);
    }
    
    public boolean connect(Neuron n , double weight) {
        if(n.inputs.contains(this) || inputs.contains(n))
            return false;
        node.addConnection(n).weight = weight;
        n.inputs.add(this);
        outputs.add(n);
        return true;
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
