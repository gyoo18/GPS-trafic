package org.Traffix.OpenGL;

import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWErrorCallbackI;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.Callback;
import org.lwjgl.system.MemoryUtil;

public class GLCanvas extends JPanel {

    private static long glfwFenêtre;

    private class Peintre extends Thread{

        public boolean vivant = true;

        public BufferedImage bi;

        private int largeur = 600;
        private int hauteur = 600;
        private int nouvelleLargeur = 600;
        private int nouvelleHauteur = 600;
        private ByteBuffer buffer;
        private boolean doitChangerTaille = true;

        private static boolean initialisé = false;
        private boolean canalOccupé = false;

        private long tempsDernierChangementTaille = 0;

        public Peintre(){
            super();
            initialiserOpenGL();
        }

        @Override
        public void run(){
            GLFW.glfwMakeContextCurrent(glfwFenêtre);
            GL.createCapabilities();

            glErreur(true);

            byte[] pixels = null;
            while(vivant){
                try{
                    if(canalOccupé){
                        continue;
                    }
                    canalOccupé = true;
                    System.out.println("dct:"+doitChangerTaille+"|t:"+(System.currentTimeMillis()-tempsDernierChangementTaille));
                    if(doitChangerTaille && System.currentTimeMillis()-tempsDernierChangementTaille > 1000){
                        largeur = nouvelleLargeur;
                        hauteur = nouvelleHauteur;
                        if(buffer == null || buffer.capacity() < largeur*hauteur*3*Byte.BYTES || buffer.capacity()/2 > largeur*hauteur*3*Byte.BYTES*2){
                            buffer = ByteBuffer.allocateDirect(largeur*hauteur*3*Byte.BYTES*4).position(0);
                            System.out.println("b");
                        }
                        bi = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_3BYTE_BGR);
                        pixels = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
                        GL46.glViewport(0, 0, largeur, hauteur);
                        doitChangerTaille = false;
                        System.out.println("a");
                    }

                    GL46.glClearColor(1, 0, 0, 0);
                    GL46.glClear(GL46.GL_COLOR_BUFFER_BIT);

                    // Les deux prochaines lignes mystérieusement empêchent des fautes de corruption de mémoire...
                    buffer.rewind();
                    GL46.glFlush();
                    GL46.glReadPixels(0,0,largeur,hauteur,GL46.GL_BGR, GL46.GL_BYTE, buffer);

                    buffer.rewind();
                    buffer.get(pixels);

                    glErreur(false);
                    
                    canalOccupé = false;
                    Thread.sleep(100);
                }catch(Exception e){
                    e.printStackTrace();
                    canalOccupé = false;
                }
            }
        }

        private static void initialiserOpenGL(){
            if (initialisé){
                return;
            }
    
            GLFW.glfwInit();
            GLFW.glfwDefaultWindowHints();
            GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
            glfwFenêtre = GLFW.glfwCreateWindow(800, 800, "", 0, 0);
            if(glfwFenêtre <= 0){
                throw new RuntimeException("La fenêtre GLFW n'a pas pue être créé.");
            }

            GLFW.glfwSetErrorCallback(new GLFWErrorCallback() {
                @Override
                public void invoke(int error, long description) {

                    switch (error) {
                        case GLFW.GLFW_NO_ERROR:
                            System.out.println("[ERREUR] GLFW : GLFW_NO_ERROR");
                            break;
                        case GLFW.GLFW_NOT_INITIALIZED:
                            System.out.println("[ERREUR] GLFW : GLFW_NOT_INITIALIZED");
                            break;
                        case GLFW.GLFW_NO_CURRENT_CONTEXT:
                            System.out.println("[ERREUR] GLFW : GLFW_NO_CURRENT_CONTEXT");
                            break;
                        case GLFW.GLFW_INVALID_ENUM:
                            System.out.println("[ERREUR] GLFW : GLFW_INVALID_ENUM");
                            break;
                        case GLFW.GLFW_INVALID_VALUE:
                            System.out.println("[ERREUR] GLFW : GLFW_INVALID_VALUE");
                            break;
                        case GLFW.GLFW_OUT_OF_MEMORY:
                            System.out.println("[ERREUR] GLFW : GLFW_OUT_OF_MEMORY");
                            break;
                        case GLFW.GLFW_API_UNAVAILABLE:
                            System.out.println("[ERREUR] GLFW : GLFW_API_UNAVAILABLE");
                            break;
                        case GLFW.GLFW_VERSION_UNAVAILABLE:
                            System.out.println("[ERREUR] GLFW : GLFW_VERSION_UNAVAILABLE");
                            break;
                        case GLFW.GLFW_PLATFORM_ERROR:
                            System.out.println("[ERREUR] GLFW : GLFW_PLATFORM_ERROR");
                            break;
                        case GLFW.GLFW_FORMAT_UNAVAILABLE:
                            System.out.println("[ERREUR] GLFW : GLFW_FORMAT_UNAVAILABLE");
                            break;
                        case GLFW.GLFW_NO_WINDOW_CONTEXT:
                            System.out.println("[ERREUR] GLFW : GLFW_NO_WINDOW_CONTEXT");
                            break;
                        case GLFW.GLFW_CURSOR_UNAVAILABLE:
                            System.out.println("[ERREUR] GLFW : GLFW_CURSOR_UNAVAILABLE");
                            break;
                        case GLFW.GLFW_FEATURE_UNAVAILABLE:
                            System.out.println("[ERREUR] GLFW : GLFW_FEATURE_UNAVAILABLE");
                            break;
                        case GLFW.GLFW_FEATURE_UNIMPLEMENTED:
                            System.out.println("[ERREUR] GLFW : GLFW_FEATURE_UNIMPLEMENTED");
                            break;
                        case GLFW.GLFW_PLATFORM_UNAVAILABLE:
                            System.out.println("[ERREUR] GLFW : GLFW_PLATFORM_UNAVAILABLE");
                            break;

                    }
                }
                
            });
    
            initialisé = true;
        }

        public boolean glErreur(boolean direNoError) {
            int erreur = GL46.glGetError();
            switch (erreur) {
                case GL46.GL_INVALID_ENUM:
                    System.err.println("GL_INVALID_ENUM");
                    break;
                case GL46.GL_INVALID_VALUE:
                    System.err.println("GL_INVALID_VALUE");
                    break;
                case GL46.GL_INVALID_OPERATION:
                    System.err.println("GL_INVALID_OPERATION");
                    break;
                case GL46.GL_INVALID_FRAMEBUFFER_OPERATION:
                    System.err.println("GL_INVALID_FRAMEBUFFER_OPERATION");
                    break;
                case GL46.GL_OUT_OF_MEMORY:
                    System.err.println("GL_OUT_OF_MEMORY");
                    break;
                case GL46.GL_STACK_UNDERFLOW:
                    System.err.println("GL_STACK_UNDERFLOW");
                    break;
                case GL46.GL_STACK_OVERFLOW:
                    System.err.println("GL_STACK_OVERFLOW");
                    break;
                case GL46.GL_NO_ERROR:
                    if (direNoError) {
                        System.err.println("GL_NO_ERROR");
                    }
                    break;
            }
            
            return erreur != GL46.GL_NO_ERROR;
        }

        public void changerTaille(int largeur, int hauteur){
            if(this.largeur != largeur || this.hauteur != hauteur){
                this.nouvelleLargeur = largeur;
                this.nouvelleHauteur = hauteur;
                this.doitChangerTaille = true;
                tempsDernierChangementTaille = System.currentTimeMillis();
                System.out.println("c");
                System.out.println("dct:"+doitChangerTaille+"|t:"+(System.currentTimeMillis()-tempsDernierChangementTaille));
            }
        }

        public synchronized void dessinerImage(Graphics g){
            if(bi == null || buffer == null){
                return;
            }

            class A extends Thread{
                @Override
                
            }
            try{
                while(canalOccupé){
                    Thread.sleep(10);
                }
                canalOccupé = true;
                
                bi.flush();
                g.drawImage(bi, 0, 0, null);

                canalOccupé = false;
            }catch(Exception e){
                e.printStackTrace();
                canalOccupé = false;
            }
        }
    }

    private Peintre peintre;

    public GLCanvas(){
        //initialiserOpenGL();
        peintre = new Peintre();
        peintre.setDaemon(true);
        peintre.start();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        synchronized(peintre){
            try{
                peintre.changerTaille(getWidth(), getHeight());
                peintre.dessinerImage(g);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
