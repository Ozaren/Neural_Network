package neuralNet;

import java.util.ArrayList;

import neuron.Neuron;

public abstract class Layer<N extends Neuron> {
    private ArrayList<N> neurons;
    
    @SafeVarargs
    public Layer(N... neurons) {
        this.neurons = new ArrayList<>();
        for(N neuron: neurons) {
            addNeuron(neuron);
        }
    }
    
    public void addNeuron(N neuron) {
        neurons.add(neuron);
    }
    
    public int size() {
        return neurons.size();
    }
    
    public abstract N[] getNeurons();
    
    protected N[] getNeurons(N[] array) {
        return neurons.toArray(array);
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        
        for(N n: neurons)
            buffer.append('\n').append(n);
        
        return buffer.substring(1);
    }
}
