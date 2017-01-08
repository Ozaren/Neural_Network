package neuron;

import java.util.Collection;

public class NeuronInput extends Neuron {
    private double weight;
    
    public NeuronInput(double weight) {
        super();
        this.weight = weight;
    }
    
    @Override
    public void connect(Neuron n , double weight) {
        throw new UnsupportedOperationException();
    }
    
    public void connect(Neuron n) {
        super.connect(n , weight);
    }
    
    @Override
    public double getWeight(Neuron n) {
        return weight;
    }
    
    public double getWeight() {
        return weight;
    }
    
    public void setWeight(double weight) {
        this.weight = weight;
    }
    
    @Override
    public void fire() {
        fired = true;
        
        Collection<Neuron> output = getOutputs();
        
        for(Neuron n: output) {
            n.addWeightToTotal(getWeight());
            n.fire();
        }
    }
    
    @Override
    public String toString() {
        Collection<Neuron> out = getOutputs();
        
        StringBuffer conn = new StringBuffer();
        
        for(Neuron n: out)
            conn.append(",[").append(n.id).append(", ").append(getNode().getConnection(n).weight).append("]");
        
        return String.format("INPUT  %d -> {%s}" , id , conn.substring(1));
    }
}
