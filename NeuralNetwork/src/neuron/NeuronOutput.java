package neuron;

public class NeuronOutput extends Neuron {
    public NeuronOutput(double threshold) {
        super(threshold);
    }
    
    @Override
    public void fire() {
        fired = canFire();
    }
    
    @Override
    public String toString() {
        return String.format("OUTPUT %d = %.3f" , id , getThreshold());
    }
}
