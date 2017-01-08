package neuralNet;

import neuron.NeuronOutput;

public class LayerOutput extends Layer<NeuronOutput> {
    public LayerOutput() {
    }
    
    public LayerOutput(double... thresholds) {
        for(double threshold: thresholds)
            addNeuron(new NeuronOutput(threshold));
    }
    
    public LayerOutput(NeuronOutput... neuronOutputs) {
        for(NeuronOutput neuronOutput: neuronOutputs)
            addNeuron(neuronOutput);
    }
    
    @Override
    public NeuronOutput[] getNeurons() {
        return getNeurons(new NeuronOutput[size()]);
    }
    
    @Override
    public String toString() {
        return "Layer Output:\n" + super.toString();
    }
}
