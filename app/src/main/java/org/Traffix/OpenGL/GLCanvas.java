package org.Traffix.OpenGL;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import org.checkerframework.checker.units.qual.t;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.awt.AWTGLCanvas;
import org.lwjgl.opengl.awt.GLData;

public class GLCanvas extends JPanel {

    public AWTGLCanvas canvas;

    public GLCanvas(){

        setLayout(new BorderLayout());
        // GLData data = new GLData();
        // AWTGLCanvas canvas = new AWTGLCanvas(data) {
        //     private static final long serialVersionUID = 1L;
        //     @Override
        //     public void initGL() {
        //         System.out.println("OpenGL version : "+effective.majorVersion+"."+effective.minorVersion+" "+effective.profile);
        //         setSize(new Dimension(100,100));
        //         setBackground(Color.RED);
        //         GL.createCapabilities();
        //         GL46.glClearColor(1,0,0,0);
        //         GL46.glViewport( 0, 0, 100, 100);
        //         GL46.glClear(GL46.GL_COLOR_BUFFER_BIT);
        //         System.out.println("a");
        //         glErreur(false);
        //     }

        //     @Override
        //     public void paintGL() {
        //         GL46.glViewport( 0, 0, 100, 100);
        //         GL46.glClear(GL46.GL_COLOR_BUFFER_BIT);
        //         swapBuffers();
        //         repaint();
        //         glErreur(false);
        //     }

        //     public boolean glErreur(boolean direNoError) {
        //         int erreur = GL46.glGetError();
        //         switch (erreur) {
        //             case GL46.GL_INVALID_ENUM:
        //                 System.err.println("GL_INVALID_ENUM");
        //                 break;
        //             case GL46.GL_INVALID_VALUE:
        //                 System.err.println("GL_INVALID_VALUE");
        //                 break;
        //             case GL46.GL_INVALID_OPERATION:
        //                 System.err.println("GL_INVALID_OPERATION");
        //                 break;
        //             case GL46.GL_INVALID_FRAMEBUFFER_OPERATION:
        //                 System.err.println("GL_INVALID_FRAMEBUFFER_OPERATION");
        //                 break;
        //             case GL46.GL_OUT_OF_MEMORY:
        //                 System.err.println("GL_OUT_OF_MEMORY");
        //                 break;
        //             case GL46.GL_STACK_UNDERFLOW:
        //                 System.err.println("GL_STACK_UNDERFLOW");
        //                 break;
        //             case GL46.GL_STACK_OVERFLOW:
        //                 System.err.println("GL_STACK_OVERFLOW");
        //                 break;
        //             case GL46.GL_NO_ERROR:
        //                 if (direNoError) {
        //                     System.err.println("GL_NO_ERROR");
        //                 }
        //                 break;
        //         }
                
        //         return erreur != GL46.GL_NO_ERROR;
        //     }
        // };
        // add(canvas, BorderLayout.CENTER);

        // Runnable test = new Runnable() {
        //     @Override
        //     public void run(){
        //         if(canvas.isValid()){
        //             canvas.render();
        //         }else{
        //             GL.setCapabilities(null);
        //         }
        //         SwingUtilities.invokeLater(this);
        //     }
        // };
        // SwingUtilities.invokeLater(test);

        GLData data = new GLData();
        add(canvas = new AWTGLCanvas(data) {
            private static final long serialVersionUID = 1L;
            @Override
            public void initGL() {
                System.out.println("OpenGL version: " + effective.majorVersion + "." + effective.minorVersion + " (Profile: " + effective.profile + ")");
                GL.createCapabilities();
                GL11.glClearColor(0.3f, 0.4f, 0.5f, 1);
            }
            @Override
            public void paintGL() {
                int w = getFramebufferWidth();
                int h = getFramebufferHeight();
                float aspect = (float) w / h;
                double now = System.currentTimeMillis() * 0.001;
                float width = (float) Math.abs(Math.sin(now * 0.3));
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
                GL11.glViewport(0, 0, w, h);
                GL11.glBegin(GL11.GL_QUADS);
                GL11.glColor3f(0.4f, 0.6f, 0.8f);
                GL11.glVertex2f(-0.75f * width / aspect, 0.0f);
                GL11.glVertex2f(0, -0.75f);
                GL11.glVertex2f(+0.75f * width/ aspect, 0);
                GL11.glVertex2f(0, +0.75f);
                GL11.glEnd();
                swapBuffers();
                repaint();
            }
        }, BorderLayout.CENTER);
    }
}
