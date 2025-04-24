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
import org.lwjgl.opengl.GL46;
import org.lwjgl.opengl.awt.AWTGLCanvas;
import org.lwjgl.opengl.awt.GLData;

public class GLCanvas extends JPanel {

    public AWTGLCanvas canvas;

    public GLCanvas(){

        setLayout(new BorderLayout());
        GLData data = new GLData();
        add(canvas = new AWTGLCanvas(data) {
            private static final long serialVersionUID = 1L;
            @Override
            public void initGL() {
                System.out.println("OpenGL version: " + effective.majorVersion + "." + effective.minorVersion + " (Profile: " + effective.profile + ")");
                GL.createCapabilities();
                GL46.glClearColor(0.3f, 0.4f, 0.5f, 1);
            }
            @Override
            public void paintGL() {
                int w = getFramebufferWidth();
                int h = getFramebufferHeight();
                float aspect = (float) w / h;
                double now = System.currentTimeMillis() * 0.001;
                float width = (float) Math.abs(Math.sin(now * 0.3));
                GL46.glClear(GL46.GL_COLOR_BUFFER_BIT);
                GL46.glViewport(0, 0, w, h);
                GL46.glBegin(GL46.GL_QUADS);
                GL46.glColor3f(0.4f, 0.6f, 0.8f);
                GL46.glVertex2f(-0.75f * width / aspect, 0.0f);
                GL46.glVertex2f(0, -0.75f);
                GL46.glVertex2f(+0.75f * width/ aspect, 0);
                GL46.glVertex2f(0, +0.75f);
                GL46.glEnd();
                swapBuffers();
                repaint();
            }
        }, BorderLayout.CENTER);
    }
}
