package neuralNet;

import neuron.NeuronHidden;
import neuron.NeuronInput;
import neuron.NeuronOutput;

public class NeuralNet {
    public static NeuralNet createNet(int numOfInput , int numOfOutput , int... numOfHidden) {
        LayerInput in = new LayerInput();
        LayerOutput out = new LayerOutput();
        LayerHidden[] hiddens = new LayerHidden[numOfHidden.length];
        
        for(int i = numOfInput; i > 0; i--)
            in.addNeuron(new NeuronInput(0));
        for(int i = numOfOutput; i > 0; i--)
            out.addNeuron(new NeuronOutput(0));
        for(int i = 0; i < numOfHidden.length; i++) {
            hiddens[i] = new LayerHidden();
            for(int j = numOfHidden[i]; j > 0; j--)
                hiddens[i].addNeuron(new NeuronHidden());
        }
        
        return new NeuralNet(in , out , hiddens);
    }
    
    private LayerInput    lIn;
    private LayerHidden[] lHide;
    private LayerOutput   lOut;
    
    @SuppressWarnings("unchecked")
    public NeuralNet(LayerInput input , LayerOutput output , LayerHidden... hidden) {
        lIn = input;
        lHide = hidden;
        lOut = output;
    }
    
    public void fire() {
        NeuronInput[] inputs = lIn.getNeurons();
        for(NeuronInput input: inputs)
            input.reset();
        for(NeuronInput input: inputs)
            input.fire();
    }
    
    public LayerInput getlIn() {
        return lIn;
    }
    
    public LayerHidden[] getlHide() {
        return lHide;
    }
    
    public LayerOutput getlOut() {
        return lOut;
    }
    
    public void addRandomConnection() {
        int layer = (int) (Math.random() * (lHide.length + 1));
        if(layer == 0) {
            NeuronInput[] inputs = lIn.getNeurons();
            NeuronInput input = inputs[(int) (Math.random() * inputs.length)];
            NeuronHidden[] hiddens = lHide[0].getNeurons();
            NeuronHidden hidden = hiddens[(int) (Math.random() * hiddens.length)];
            
            input.connect(hidden , 0);
        }
        else if(layer == lHide.length) {
            NeuronHidden[] hiddens = lHide[lHide.length - 1].getNeurons();
            NeuronHidden hidden = hiddens[(int) (Math.random() * hiddens.length)];
            NeuronOutput[] outputs = lOut.getNeurons();
            NeuronOutput output = outputs[(int) (Math.random() * outputs.length)];
            
            hidden.connect(output , 0);
        }
        else {
            NeuronHidden[] hiddens1 = lHide[layer - 1].getNeurons();
            NeuronHidden input = hiddens1[(int) (Math.random() * hiddens1.length)];
            NeuronHidden[] hiddens2 = lHide[layer].getNeurons();
            NeuronHidden output = hiddens2[(int) (Math.random() * hiddens2.length)];
            
            input.connect(output , 0);
        }
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(lIn).append("\n\n");
        for(LayerHidden h: lHide) {
            buffer.append(h).append('\n');
        }
        buffer.append('\n').append(lOut);
        return buffer.toString();
    }
}