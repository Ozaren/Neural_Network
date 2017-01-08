package neuron;

import java.util.Collection;

public class NeuronHidden extends Neuron {
    public NeuronHidden() {
        super();
    }
    
    public NeuronHidden(double threshold) {
        super(threshold);
    }
    
    @Override
    public void fire() {
        fired = true;
        if(canFire()) {
            Collection<Neuron> output = getOutputs();
            
            for(Neuron n: output) {
                n.addWeightToTotal(getWeight(n));
                n.fire();
            }
        }
    }
    
    @Override
    public String toString() {
        Collection<Neuron> out = getOutputs();
        
        StringBuffer conn = new StringBuffer();
        
        for(Neuron n: out)
            conn.append(",[").append(n.id).append(", ").append(getNode().getConnection(n).weight).append("]");
        
        return String.format("HIDDEN %d = %.3f -> {%s}" , id , getThreshold() , conn.substring(1));
    }
}
