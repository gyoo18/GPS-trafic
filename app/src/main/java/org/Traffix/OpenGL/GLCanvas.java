package org.Traffix.OpenGL;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.Traffix.maths.Mat4;
import org.Traffix.maths.Vec4;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;
import org.lwjgl.opengl.awt.AWTGLCanvas;
import org.lwjgl.opengl.awt.GLData;

public class GLCanvas extends JPanel {

    public GLSubCanvas canvas;

    public Scène scène = new Scène();

    public class GLSubCanvas extends AWTGLCanvas{
        private boolean aFenêtreÉtéModifié = false;
        private static final long serialVersionUID = 1L;

        public GLSubCanvas(GLData data){
            super(data);
        }

        @Override
        public void initGL() {
            System.out.println("OpenGL version: " + effective.majorVersion + "." + effective.minorVersion + " (Profile: " + effective.profile + ")");
            GL.createCapabilities();
            GL46.glClearColor(0.3f, 0.4f, 0.5f, 1);

            scène.construireScène();

            glErreur(true);
        }
        @Override
        public void paintGL() {
            GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);

            if(aFenêtreÉtéModifié){
                ajusterFenêtre();
                aFenêtreÉtéModifié = false;
            }

            if (scène.estConstruite){
                ArrayList<Objet> objets = scène.objets;
                for (Objet o : objets){
                    if(!o.estConstruit){
                        o.construire();
                    }
                    
                    if (o.dessiner && o.aMaillage() && o.aNuanceur()){
                        o.avoirMaillage().préparerAuDessin();
                        GL46.glUseProgram(o.avoirNuanceur().ID);

                        if (o.aTexture()){
                            GL46.glActiveTexture(GL46.GL_TEXTURE0);
                            GL46.glBindTexture(GL46.GL_TEXTURE_2D, o.avoirTexture().ID);
                            GL46.glUniform1i(GL46.glGetUniformLocation(o.avoirNuanceur().ID,"Tex"),0);
                        }

                        if (o.aCouleur()){
                            Vec4 coul = o.avoirCouleur();
                            GL46.glUniform4f(GL46.glGetUniformLocation(o.avoirNuanceur().ID,"Coul"),coul.x,coul.y,coul.z,coul.w);
                        }

                        if (o.aTransformée()){
                            GL46.glUniformMatrix4fv(GL46.glGetUniformLocation(o.avoirNuanceur().ID,"transforme"),false,o.avoirTransformée().avoirMat().mat);
                            GL46.glUniformMatrix4fv(GL46.glGetUniformLocation(o.avoirNuanceur().ID,"rotation"),false,o.avoirTransformée().avoirRotMat().mat);
                        }else{
                            GL46.glUniformMatrix4fv(GL46.glGetUniformLocation(o.avoirNuanceur().ID,"transforme"),false, new Mat4().mat);
                            GL46.glUniformMatrix4fv(GL46.glGetUniformLocation(o.avoirNuanceur().ID,"rotation"),false, new Mat4().mat);
                        }

                        GL46.glUniformMatrix4fv(GL46.glGetUniformLocation(o.avoirNuanceur().ID,"vue"),false,scène.caméra.avoirVue().avoirMat().mat);
                        GL46.glUniformMatrix4fv(GL46.glGetUniformLocation(o.avoirNuanceur().ID,"projection"),false,scène.caméra.projection.mat);

                        if (o.avoirMaillage().estIndexé){
                            GL46.glDrawElements(GL46.GL_TRIANGLES, o.avoirMaillage().NSommets, GL46.GL_UNSIGNED_INT,0);
                        }else {
                            GL46.glDrawArrays(GL46.GL_TRIANGLES, 0, o.avoirMaillage().NSommets);
                        }
                    }
                }
            }

            glErreur(false);
            swapBuffers();
            repaint();
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

        public void surFenêtreModifiée(){
            aFenêtreÉtéModifié = true;
        }

        private void ajusterFenêtre(){
            GL46.glViewport(0, 0, getFramebufferWidth(), getFramebufferHeight());
            scène.surModificationFenêtre((float)getFramebufferWidth()/(float)getFramebufferHeight());
        }
    }

    public GLCanvas(){

        setLayout(new BorderLayout());
        GLData data = new GLData();
        canvas = new GLSubCanvas(data);
        add(canvas, BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter(){
            @Override
            public void componentResized(ComponentEvent event){
                canvas.surFenêtreModifiée();
            }
        });
    }
}
