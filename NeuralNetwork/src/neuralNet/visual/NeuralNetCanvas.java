package neuralNet.visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import io.AbstractCanvas;
import neuralNetObject.NeuralNet;

public class NeuralNetCanvas extends AbstractCanvas {
    private BufferedImage image;
    private NeuralNet     net;
    
    public NeuralNetCanvas(NeuralNet net) {
        super(getDim(net));
        this.net = net;
        image = new BufferedImage(getWidth() , getHeight() , BufferedImage.TYPE_4BYTE_ABGR);
    }
    
    private static Dimension getDim(NeuralNet net) {
        return new Dimension(NeuralNet.DIST_X * (net.getlHide().length + 2) , NeuralNet.DIST_Y * (net.getHeight()));
    }
    
    @Override
    public void tick() {
    }
    
    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0 , 0 , getWidth() , getHeight());
        net.draw(image);
        g.drawImage(image , 0 , 0 , null);
    }
    
}
