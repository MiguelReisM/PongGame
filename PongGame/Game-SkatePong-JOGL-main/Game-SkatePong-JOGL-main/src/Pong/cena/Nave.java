package Pong.cena;

import static Pong.cena.Jogo.screenSize;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;


public class Nave
{

    //construtor
    public Nave() {}
     
    //dados para renderização dos objetos
    public double width = screenSize.getWidth();
    public double height = screenSize.getHeight();
    public float aspect = (float)(this.width/this.height);
    
    //metodo para desenhar o nave completo
    public void desenhaNave(GL2 gl, GLUT glut)
    {
        gl.glPushMatrix();
            nave(gl, glut);
        gl.glPopMatrix();
    }
    
    //desenho do nave
    public void nave(GL2 gl, GLUT glut)
    {
        // Configuração da iluminação
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0); // Habilita a luz 0

// Configuração da luz ambiente
        float[] ambientLight = {0.2f, 0.2f, 0.2f, 1.0f}; // Cor ambiente (RGB)
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0);

// Configuração da luz direcional
        float[] lightPosition = {0.0f, 1.0f, 0.0f, 0.0f}; // Direção da luz (na direção y)
        float[] diffuseLight = {1.0f, 1.0f, 1.0f, 1.0f}; // Cor difusa (RGB)
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseLight, 0);

// Corpo principal da nave
        gl.glColor3f(0.8f, 0.2f, 0.8f); // Cor roxa
        gl.glPushMatrix();
        gl.glTranslatef(0, -90 * this.aspect, 60 * this.aspect);
        gl.glScalef(1, 0.5f, 0.5f);
        glut.glutSolidSphere(15 * this.aspect, 16, 8);
        gl.glPopMatrix();

// Parte frontal da nave
        gl.glColor3f(0.2f, 0.8f, 0.2f); // Cor verde
        gl.glPushMatrix();
        gl.glTranslatef(0, -90 * this.aspect, 40 * this.aspect);
        glut.glutSolidCone(10 * this.aspect, 40 * this.aspect, 16, 8);
        gl.glPopMatrix();

// Desativa a iluminação ao finalizar o desenho da nave
        gl.glDisable(GL2.GL_LIGHTING);

    }
}   