package neuralNet;

import neuron.NeuronHidden;

public class LayerHidden extends Layer<NeuronHidden> {
    public LayerHidden(NeuronHidden... neuronHiddens) {
        for(NeuronHidden neuronHidden: neuronHiddens)
            addNeuron(neuronHidden);
    }
    
    @Override
    public NeuronHidden[] getNeurons() {
        return getNeurons(new NeuronHidden[size()]);
    }
    
    @Override
    public String toString() {
        return "Layer Hidden:\n" + super.toString();
    }
}
