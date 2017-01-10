package neuralNetObject;

import neuron.Neuron;

public class NeuronNotFoundException extends RuntimeException {
    public NeuronNotFoundException(Neuron n) {
        super(String.format("Neuron &s not found" , n));
    }
}
