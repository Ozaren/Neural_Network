package neuralNetObject;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import math.Math2;
import neuron.Neuron;
import neuron.Neuron.NeuronNode;
import neuron.Neuron.Synapse;
import neuron.NeuronHidden;
import neuron.NeuronInput;
import neuron.NeuronOutput;

public class NeuralNet {
    public static Color        color                = Color.WHITE;
    public static final int    RADIUS               = 15 , DIAMETER = RADIUS * 2 , DIST_X = RADIUS * 12 , DIST_Y = RADIUS * 3;
    
    public static final String NEURON_OUT_DELIMITER = "<";
    public static final String NEURON_DELIMITER     = ">";
    
    private static class Data {
        public int                id;
        public int                layer;
        public Neuron             neuron;
        public ArrayList<Integer> out;
        public ArrayList<Double>  weight;
        
        public Data(Neuron n , int id , int layer) {
            neuron = n;
            this.id = id;
            this.layer = layer;
            out = new ArrayList<>();
            weight = new ArrayList<>();
        }
        
        @Override
        public String toString() {
            return String.format("%d#%d" , id , out.size());
        }
    }
    
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
    
    public static final NeuralNet createNet(String saveString) {
        String[] layers = saveString.split("L");
        
        LayerInput in = new LayerInput();
        LayerOutput out = new LayerOutput();
        LayerHidden[] hiddens = new LayerHidden[layers.length - 2];
        
        int index = 0 , neuronCount = -1;
        
        do {
            index = saveString.indexOf(NEURON_DELIMITER , index + 1);
            neuronCount++;
        } while(index > 0);
        
        Data[] data = new Data[neuronCount];
        
        for(int i = 0; i < hiddens.length; i++)
            hiddens[i] = new LayerHidden();
        
        for(String s: layers[0].substring(2).split(NEURON_DELIMITER)) {
            String[] dat = s.split(NEURON_OUT_DELIMITER);
            
            index = Integer.parseInt(dat[0]);
            data[index] = new Data(new NeuronInput(0) , index , 0);
            
            for(int i = 2; i < dat.length; i += 2) {
                data[index].out.add(Integer.parseInt(dat[i]));
                data[index].weight.add(Double.parseDouble(dat[i + 1]));
            }
        }
        
        for(String s: layers[1].substring(2).split(NEURON_DELIMITER)) {
            String[] dat = s.split(NEURON_OUT_DELIMITER);
            
            index = Integer.parseInt(dat[0]);
            data[index] = new Data(new NeuronOutput(0) , index , 1);
            data[index].neuron.setThreshold(Double.parseDouble(dat[1]));
            
            for(int i = 2; i < dat.length; i += 2) {
                data[index].out.add(Integer.parseInt(dat[i]));
                data[index].weight.add(Double.parseDouble(dat[i + 1]));
            }
        }
        
        for(int j = 2; j < layers.length; j++) {
            for(String s: layers[j].substring(2).split(NEURON_DELIMITER)) {
                String[] dat = s.split(NEURON_OUT_DELIMITER);
                
                index = Integer.parseInt(dat[0]);
                data[index] = new Data(new NeuronHidden(0) , index , j);
                data[index].neuron.setThreshold(Double.parseDouble(dat[1]));
                
                for(int i = 2; i < dat.length; i += 2) {
                    data[index].out.add(Integer.parseInt(dat[i]));
                    data[index].weight.add(Double.parseDouble(dat[i + 1]));
                }
            }
        }
        
        for(Data dat: data) {
            switch (dat.layer) {
                case 0: {
                    in.addNeuron((NeuronInput) dat.neuron);
                    break;
                }
                case 1: {
                    out.addNeuron((NeuronOutput) dat.neuron);
                    break;
                }
                default: {
                    hiddens[dat.layer - 2].addNeuron((NeuronHidden) dat.neuron);
                    break;
                }
            }
            if(dat.layer == 0)
                for(int i = 0; i < dat.out.size(); i++)
                    ((NeuronInput) dat.neuron).connect(data[dat.out.get(i)].neuron);
            else
                for(int i = 0; i < dat.out.size(); i++)
                    dat.neuron.connect(data[dat.out.get(i)].neuron , dat.weight.get(i));
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
    
    public void randomizeThresholds(int max) {
        for(NeuronInput n: lIn.getNeurons())
            n.setThreshold(Math.random() * max);
        for(NeuronOutput n: lOut.getNeurons())
            n.setThreshold(Math.random() * max);
        for(LayerHidden ns: lHide)
            for(NeuronHidden n: ns.getNeurons())
                n.setThreshold(Math.random() * max);
    }
    
    public void fire() {
        NeuronInput[] inputs = lIn.getNeurons();
        for(NeuronInput input: inputs)
            input.reset();
        for(NeuronInput input: inputs)
            input.fire();
    }
    
    public void fire(boolean... inputStates) {
        if(inputStates.length < lIn.size())
            throw new IllegalArgumentException("Too little inputs");
        NeuronInput[] inputs = lIn.getNeurons();
        for(NeuronInput input: inputs)
            input.reset();
        for(int i = 0; i < inputs.length; i++)
            inputs[i].fire(inputStates[i]);
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
    
    public void updateVisibility() {
        NeuronInput[] ins = lIn.getNeurons();
        
        for(NeuronInput n: ins)
            n.invisible = false;
        
        for(LayerHidden h: lHide)
            for(NeuronHidden n: h.getNeurons())
                n.invisible = !n.hasInput();
        
        for(NeuronOutput n: lOut.getNeurons())
            n.invisible = !n.hasInput();
    }
    
    public void addRandomConnection() {
        double weight = Math.random() * 2 - 1;
        int layer = (int) (Math.random() * (lHide.length + 1));
        boolean connected;
        if(layer == 0) {
            NeuronInput[] inputs = lIn.getNeurons();
            NeuronInput input = inputs[(int) (Math.random() * inputs.length)];
            NeuronHidden[] hiddens = lHide[0].getNeurons();
            NeuronHidden hidden = hiddens[(int) (Math.random() * hiddens.length)];
            
            connected = input.connect(hidden);
        }
        else if(layer == lHide.length) {
            NeuronHidden[] hiddens = lHide[lHide.length - 1].getNeurons();
            NeuronHidden hidden = hiddens[(int) (Math.random() * hiddens.length)];
            NeuronOutput[] outputs = lOut.getNeurons();
            NeuronOutput output = outputs[(int) (Math.random() * outputs.length)];
            
            connected = hidden.connect(output , weight);
        }
        else {
            NeuronHidden[] hiddens1 = lHide[layer - 1].getNeurons();
            NeuronHidden input = hiddens1[(int) (Math.random() * hiddens1.length)];
            NeuronHidden[] hiddens2 = lHide[layer].getNeurons();
            NeuronHidden output = hiddens2[(int) (Math.random() * hiddens2.length)];
            
            connected = input.connect(output , weight);
        }
        if(!connected)
            addRandomConnection();
    }
    
    private HashSet<NeuronNode> printed = new HashSet<>();
    
    public void draw(BufferedImage image) {
        if(image.getWidth() / DIST_X < lHide.length + 2 || image.getHeight() < getHeight() * DIST_Y)
            throw new UnsupportedOperationException();
        
        Graphics2D g = image.createGraphics();
        g.clearRect(0 , 0 , image.getWidth() , image.getHeight());
        
        NeuronInput[] inputs = lIn.getNeurons();
        NeuronOutput[] outputs = lOut.getNeurons();
        
        printed.clear();
        
        for(int i = 0; i < lIn.size(); i++)
            drawConnections(g , inputs[i].getNode() , 0 , i);
        
        for(int i = 0; i < lHide.length; i++) {
            NeuronHidden[] hiddens = lHide[i].getNeurons();
            for(int j = 0; j < hiddens.length; j++)
                drawConnections(g , hiddens[j].getNode() , i + 1 , j);
        }
        
        for(int i = 0; i < lOut.size(); i++)
            drawConnections(g , outputs[i].getNode() , lHide.length + 1 * DIST_X , i);
        
        Neuron n;
        g.setColor(color);
        
        for(int i = 0; i < lIn.size(); i++) {
            n = lIn.getNeuron(i);
            if(!n.invisible)
                if(n.isFired())
                    g.fillOval(0 , i * DIST_Y , DIAMETER , DIAMETER);
                else
                    g.drawOval(0 , i * DIST_Y , DIAMETER , DIAMETER);
        }
        
        for(int i = 0; i < lHide.length; i++) {
            for(int j = 0; j < lHide[i].size(); j++) {
                n = lHide[i].getNeuron(j);
                if(!n.invisible)
                    if(n.isFired())
                        g.fillOval((i + 1) * DIST_X , j * DIST_Y , DIAMETER , DIAMETER);
                    else
                        g.drawOval((i + 1) * DIST_X , j * DIST_Y , DIAMETER , DIAMETER);
            }
        }
        for(int i = 0; i < lOut.size(); i++) {
            n = lOut.getNeuron(i);
            if(!lOut.getNeuron(i).invisible)
                if(n.isFired())
                    g.fillOval((lHide.length + 1) * DIST_X , i * DIST_Y , DIAMETER , DIAMETER);
                else
                    g.drawOval((lHide.length + 1) * DIST_X , i * DIST_Y , DIAMETER , DIAMETER);
        }
    }
    
    private void drawConnections(Graphics2D g , NeuronNode node , int i , int j) {
        if(printed.contains(node))
            return;
        else
            printed.add(node);
        if(node.getNeuron().invisible)
            return;
        Synapse[] conns = node.getConnections();
        
        for(Synapse conn: conns) {
            if(printed.contains(conn.getOther(node)))
                continue;
            Neuron n = conn.getOther(node).getNeuron();
            Point p = getNodePoint(n , j);
            
            if(n.invisible)
                continue;
            
            double w = conn.getWeight();
            if(w == 0) {
                g.setColor(Color.BLUE);
            }
            else if(w < 0) {
                g.setColor(Color.RED);
                w *= -1;
            }
            else {
                g.setColor(Color.GREEN);
            }
            w *= 2;
            
            Stroke s = g.getStroke();
            g.setStroke(new BasicStroke(w == 0 ? 1 : (float) w));
            
            g.drawLine(i * DIST_X + RADIUS , j * DIST_Y + RADIUS , p.x , p.y);
            g.setStroke(s);
        }
    }
    
    private Point getNodePoint(Neuron n , int i) {
        int x = 0 , y = 0;
        if(lIn.contains(n)) {
            y = lIn.getIndex(n);
        }
        else if(lOut.contains(n)) {
            x = lHide.length + 1;
            y = lOut.getIndex(n);
        }
        else {
            int j = 0;
            boolean done = false;
            for(; j < lHide.length; j++) {
                if(lHide[j].contains(n)) {
                    x = j + 1;
                    y = lHide[j].getIndex(n);
                    done = true;
                }
            }
            if(!done)
                throw new NeuronNotFoundException(n);
        }
        return new Point(x * DIST_X + RADIUS , y * DIST_Y + RADIUS);
    }
    
    public String getSaveString() {
        StringBuffer buffer = new StringBuffer();
        HashMap<Neuron , Integer> map = new HashMap<>();
        
        int index = 0;
        
        for(NeuronInput in: lIn.getNeurons())
            map.put(in , index++);
        for(NeuronOutput out: lOut.getNeurons())
            map.put(out , index++);
        for(LayerHidden hidden: lHide)
            for(NeuronHidden hid: hidden.getNeurons())
                map.put(hid , index++);
        buffer.append("I");
        for(NeuronInput in: lIn.getNeurons()) {
            buffer.append(NEURON_DELIMITER).append(map.get(in)).append(NEURON_OUT_DELIMITER).append(in.getThreshold()).append(NEURON_OUT_DELIMITER);
            
            for(Neuron n: in.getOutputs())
                buffer.append(map.get(n)).append(NEURON_OUT_DELIMITER).append(n.getNode().getConnection(in).getRawWeight()).append(NEURON_OUT_DELIMITER);
            
            buffer.deleteCharAt(buffer.length() - 1);
        }
        buffer.append("LO");
        for(NeuronOutput out: lOut.getNeurons()) {
            buffer.append(NEURON_DELIMITER).append(map.get(out)).append(NEURON_OUT_DELIMITER).append(out.getThreshold()).append(NEURON_OUT_DELIMITER);
            
            for(Neuron n: out.getOutputs())
                buffer.append(map.get(n)).append(NEURON_OUT_DELIMITER).append(n.getNode().getConnection(out).getRawWeight()).append(NEURON_OUT_DELIMITER);
            
            if(out.getOutputs().size() > 0)
                buffer.deleteCharAt(buffer.length() - 1);
        }
        for(LayerHidden hidden: lHide) {
            buffer.append("LH");
            for(NeuronHidden hid: hidden.getNeurons()) {
                buffer.append(NEURON_DELIMITER).append(map.get(hid)).append(NEURON_OUT_DELIMITER).append(hid.getThreshold()).append(NEURON_OUT_DELIMITER);
                
                for(Neuron n: hid.getOutputs())
                    buffer.append(map.get(n)).append(NEURON_OUT_DELIMITER).append(n.getNode().getConnection(hid).getRawWeight()).append(NEURON_OUT_DELIMITER);
                
                buffer.deleteCharAt(buffer.length() - 1);
            }
        }
        
        return buffer.toString();
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(lIn).append("\n\n");
        for(LayerHidden h: lHide)
            buffer.append(h).append('\n');
        
        buffer.append('\n').append(lOut);
        return buffer.toString();
    }
    
    public int getHeight() {
        int[] sizes = new int[lHide.length + 2];
        
        sizes[0] = lIn.size();
        sizes[1] = lOut.size();
        
        for(int i = 0; i < lHide.length; i++)
            sizes[i + 2] = lHide[i].size();
        
        return Math2.max(sizes);
    }
}