package Pong.cena;

import static Pong.cena.Cena.screenSize;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Esfera {

    // Construtor
    public Esfera() {}

    // Dados para renderização dos objetos
    public double width = screenSize.getWidth();
    public double height = screenSize.getHeight();
    public float aspect = (float)(this.width/this.height);

    // Método para desenhar a esfera
    public void draw(GL2 gl, GLUT glut) {
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

        // Define a cor principal como cinza
        gl.glColor3f(0.5f, 0.5f, 0.5f); // Define a cor como cinza
        gl.glTranslatef(0, 0, 15 * this.aspect); // Translação para posicionar a esfera

        // Desenha a esfera principal
        glut.glutSolidSphere(8 * this.aspect, 50, 50);

        // Define a cor como preto para os círculos
        gl.glColor3f(0.0f, 0.0f, 0.0f);

        // Adiciona detalhes com círculos pretos usando ruído
        float noiseScale = 0.2f;
        float noiseStrength = 2.0f;

        for (float u = 0; u <= 1.0; u += 0.1) {
            for (float v = 0; v <= 1.0; v += 0.1) {
                float x = 8 * this.aspect * (float) Math.sin(Math.PI * u) * (float) Math.cos(2 * Math.PI * v);
                float y = 8 * this.aspect * (float) Math.sin(Math.PI * u) * (float) Math.sin(2 * Math.PI * v);
                float z = 8 * this.aspect * (float) Math.cos(Math.PI * u);

                x += noiseStrength * (float) Math.random() * noiseScale;
                y += noiseStrength * (float) Math.random() * noiseScale;
                z += noiseStrength * (float) Math.random() * noiseScale;

                gl.glPushMatrix();
                gl.glTranslatef(x, y, z);

                // Desenha pequenos círculos pretos
                glut.glutSolidSphere(0.5f * this.aspect, 10, 10);

                gl.glPopMatrix();
            }
        }

        // Desativa a iluminação ao finalizar o desenho
        gl.glDisable(GL2.GL_LIGHTING);
    }
}
