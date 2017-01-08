package neuralNet;

import neuron.NeuronInput;

public class LayerInput extends Layer<NeuronInput> {
    public LayerInput() {
    }
    
    public LayerInput(double... weights) {
        for(double threshold: weights)
            addNeuron(new NeuronInput(threshold));
    }
    
    public LayerInput(NeuronInput... neuronInputs) {
        for(NeuronInput neuronInput: neuronInputs)
            addNeuron(neuronInput);
    }
    
    @Override
    public NeuronInput[] getNeurons() {
        return getNeurons(new NeuronInput[size()]);
    }
    
    @Override
    public String toString() {
        return "Layer Input:\n" + super.toString();
    }
}
