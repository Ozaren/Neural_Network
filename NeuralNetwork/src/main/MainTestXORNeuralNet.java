package main;

import neuralNet.LayerHidden;
import neuralNet.LayerInput;
import neuralNet.LayerOutput;
import neuralNet.NeuralNet;
import neuron.NeuronHidden;
import neuron.NeuronInput;
import neuron.NeuronOutput;

public class MainTestXORNeuralNet {
    public static void main(String[] args) {
        boolean[] vals = {true , true};
        
        LayerInput input = new LayerInput();
        LayerHidden hidden = new LayerHidden();
        LayerOutput output = new LayerOutput();
        NeuralNet net = new NeuralNet(input , output , hidden);
        
        NeuronOutput xor = new NeuronOutput(1);
        NeuronHidden left = new NeuronHidden(2);
        NeuronHidden right = new NeuronHidden(1);
        
        hidden.addNeuron(left);
        
        hidden.addNeuron(right);
        
        left.connect(xor , -1);
        right.connect(xor , 1);
        output.addNeuron(xor);
        
        for(boolean val: vals) {
            NeuronInput op = new NeuronInput(val ? 1 : 0);
            op.connect(left);
            op.connect(right);
            input.addNeuron(op);
        }
        
        net.fire();
        
        System.out.println("Result: " + xor.isFired());
        System.out.println(net);
    }
}
