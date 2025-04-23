package org.Traffix.OpenGL;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class GLCanvas extends JPanel {

    private BufferedImage bi;

    public GLCanvas(){
        
    }

    public void dessiner(){
        try{
            // BufferStrategy bs = getBufferStrategy();
            
            bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_3BYTE_BGR);

            // Graphics2D g2d = bi.createGraphics();
            // g2d.setColor(Color.RED);
            // g2d.fillRect(0, 0, getWidth(), getHeight());

            // super.printAll(g2d);
            // g2d.dispose();

            // Graphics2D g2d = bi.createGraphics();
            // printAll(g2d);
            // g.dispose();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        byte[] données = new byte[getWidth()*getHeight()*3];
        for (int i = 0; i < données.length; i++){
            données[i] = (byte)150;
        }
        ByteBuffer buffer = ByteBuffer.allocateDirect(getWidth()*getHeight()*3*Byte.BYTES).order(ByteOrder.nativeOrder()).put(données).position(0);
            
        byte[] pixels = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        buffer.rewind();
        buffer.get(pixels);

        g.drawImage(bi, 0, 0, null);
    }
}
