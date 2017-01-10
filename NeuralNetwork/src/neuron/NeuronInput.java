package neuron;

import java.util.Collection;

public class NeuronInput extends Neuron {
    private double weight;
    
    public NeuronInput(double weight) {
        super();
        this.weight = weight;
    }
    
    @Override
    public double getThreshold() {
        return 0;
    }
    
    @Override
    public boolean connect(Neuron n , double weight) {
        throw new UnsupportedOperationException();
    }
    
    public boolean connect(Neuron n) {
        return super.connect(n , weight);
    }
    
    @Override
    public double getWeight(Neuron n) {
        return weight;
    }
    
    public double getWeight() {
        return weight;
    }
    
    public void fire(boolean on) {
        this.weight = on ? 1 : 0;
        fire();
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
    protected String getType() {
        return "INPUT";
    }
    
    @Override
    public String toString() {
        Collection<Neuron> out = getOutputs();
        
        StringBuffer conn = new StringBuffer();
        
        for(Neuron n: out)
            conn.append(",[").append(n.id).append(", ").append(getNode().getConnection(n).weight).append("]");
        
        if(conn.length() == 0)
            return String.format("INPUT  %d" , id);
        else
            return String.format("INPUT  %d -> {%s}" , id , conn.substring(1));
    }
}
