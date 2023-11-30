package Pong.cena;

import static Pong.cena.Cena.screenSize;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

// Classe para produção dos cenários
public class Cenarios {
    // Dados para animação do cenário no menu
    public float movNuvens = 0;

    // Dados para renderização dos objetos
    public double width = screenSize.getWidth();
    public double height = screenSize.getHeight();
    public float aspect = (float) (this.width / this.height);

    // Construtor
    public Cenarios() {
    }

    // Método para desenhar cenário do menu
    public void drawCenario1(GL2 gl, GLUT glut) {
        // Implementação específica do cenário do menu
    }

    // Método para desenhar cenário da primeira fase
    public void drawCenario2(GL2 gl, GLUT glut) {
        desenhaCampo1Fase(gl);
        desenhaCeu1Fase(gl, glut);
    }

    // Método para desenhar cenário da segunda fase
    public void drawCenario3(GL2 gl, GLUT glut) {
        desenhaCampo2Fase(gl);
        desenhaCeu2Fase(gl, glut);
    }

    // Método para desenhar cenário do fim do jogo
    public void drawCenario4(GL2 gl, GLUT glut) {
        desenhaCampoFim(gl);
        desenhaCeuFim(gl, glut);
    }

    // Método privado para desenhar o céu do fim do jogo
    private void desenhaCeuFim(GL2 gl, GLUT glut) {
        // Implementação específica do céu do fim do jogo
    }

    // Desenha o campo da primeira fase
    public void desenhaCampo1Fase(GL2 gl) {
        // Desenha um campo verde
        gl.glPushMatrix();
        gl.glColor3f(0.0f, 0.5f, 0.0f); // Cor verde
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(-200 * this.aspect, -81.6f * this.aspect);
        gl.glVertex2f(-200 * this.aspect, -20 * this.aspect);
        gl.glVertex2f(200 * this.aspect, -20 * this.aspect);
        gl.glVertex2f(200 * this.aspect, -81.6f * this.aspect);
        gl.glEnd();
        gl.glFlush();
        gl.glPopMatrix();
    }

    // Desenha o campo da segunda fase
    public void desenhaCampo2Fase(GL2 gl) {
        // Desenha um campo mais escuro
        gl.glPushMatrix();
        gl.glColor3f(0, 0.1f, 0);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(-200 * this.aspect, -81.6f * this.aspect);
        gl.glVertex2f(-200 * this.aspect, -20 * this.aspect);
        gl.glVertex2f(200 * this.aspect, -20 * this.aspect);
        gl.glVertex2f(200 * this.aspect, -81.6f * this.aspect);
        gl.glEnd();
        gl.glFlush();
        gl.glPopMatrix();
    }

    // Desenha o campo do fim de jogo
    public void desenhaCampoFim(GL2 gl) {
        // Desenha um campo mais escuro para o fim do jogo
        gl.glColor3f(0, 0.39f, 0);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(-200 * this.aspect, -81.6f * this.aspect);
        gl.glVertex2f(-200 * this.aspect, -20 * this.aspect);
        gl.glVertex2f(200 * this.aspect, -20 * this.aspect);
        gl.glVertex2f(200 * this.aspect, -81.6f * this.aspect);
        gl.glEnd();
        gl.glFlush();
    }

    // Desenha o céu da primeira fase
    public void desenhaCeu1Fase(GL2 gl, GLUT glut) {
        // Limpa o buffer de cor e profundidade, configurando o fundo para preto
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        // Desenha estrelas brancas no céu
        gl.glColor3f(1.0f, 1.0f, 1.0f); // Cor branca para estrelas

        for (int i = 0; i < 100; i++) {
            float x = (float) (Math.random() * 400 * this.aspect) - 200 * this.aspect;
            float y = (float) (Math.random() * 200 * this.aspect) - 100 * this.aspect;

            gl.glPushMatrix();
            gl.glTranslatef(x, y, 0);
            glut.glutSolidSphere(0.1f * this.aspect, 5, 5); // Desenha uma pequena esfera como estrela
            gl.glPopMatrix();
        }

        // Configuração da iluminação para os planetas
        configurarIluminacao(gl);

        // Desenha planetas estáticos
        drawPlanet(gl, glut, -150 * this.aspect, 20, 5 * this.aspect, 0.7f, 0.7f, 0.7f); // Mercúrio
        drawPlanet(gl, glut, -100 * this.aspect, 40, 8 * this.aspect, 1.0f, 0.7f, 0.2f); // Vênus
        drawPlanet(gl, glut, -50 * this.aspect, -70, 8 * this.aspect, 0.2f, 0.2f, 1.0f); // Terra
        drawPlanet(gl, glut, 0, -10, 7 * this.aspect, 1.0f, 0.4f, 0.4f); // Marte
        drawPlanet(gl, glut, 50 * this.aspect, 30, 20 * this.aspect, 0.8f, 0.5f, 0.2f); // Júpiter

        // Desenha anéis para a Terra (semelhantes aos de Saturno)
        drawRings(gl, glut, -50 * this.aspect, -70, 8 * this.aspect);

        // Desativa a iluminação ao finalizar o desenho
        desativarIluminacao(gl);

        // Desenha outros elementos, como a lua
        drawLua(gl, glut);

        gl.glFlush();
    }

    // Método auxiliar para desenhar um planeta estático
    private void drawPlanet(GL2 gl, GLUT glut, float x, float y, float radius, float r, float g, float b) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, 0);
        gl.glColor3f(r, g, b);
        glut.glutSolidSphere(radius, 20, 20); // Desenha um planeta
        gl.glPopMatrix();
    }

    // Método auxiliar para configurar a iluminação
    private void configurarIluminacao(GL2 gl) {
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
    }

    // Método auxiliar para desenhar anéis (rings)
    private void drawRings(GL2 gl, GLUT glut, float x, float y, float radius) {
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl.glColor4f(0.6f, 0.6f, 0.6f, 0.5f); // Cor cinza semi-transparente para os anéis
        gl.glPushMatrix();
        gl.glTranslatef(x, y, 0);
        gl.glRotatef(90, 1.0f, 0.0f, 0.0f); // Rotaciona os anéis para serem paralelos ao plano xy
        glut.glutSolidTorus(1.5 * this.aspect, 12 * this.aspect, 50, 50); // Desenha os anéis
        gl.glPopMatrix();
        gl.glDisable(GL2.GL_BLEND);
    }

    // Método auxiliar para desativar a iluminação
    private void desativarIluminacao(GL2 gl) {
        gl.glDisable(GL2.GL_LIGHTING);
    }

    // Método para desenhar o céu da segunda fase
    public void desenhaCeu2Fase(GL2 gl, GLUT glut) {
        // Implementação específica do céu da segunda fase
        configurarIluminacao(gl);

        // Desenha planetas e anéis
        drawPlanet(gl, glut, -150 * this.aspect, 20, 5 * this.aspect, 0.7f, 0.7f, 0.7f); // Mercúrio
        drawPlanet(gl, glut, -100 * this.aspect, 40, 8 * this.aspect, 1.0f, 0.7f, 0.2f); // Vênus
        drawPlanet(gl, glut, -50 * this.aspect, -70, 8 * this.aspect, 0.2f, 0.2f, 1.0f); // Terra
        drawPlanet(gl, glut, 0, -10, 7 * this.aspect, 1.0f, 0.4f, 0.4f); // Marte
        drawPlanet(gl, glut, 50 * this.aspect, 30, 20 * this.aspect, 0.8f, 0.5f, 0.2f); // Júpiter

        // Desenha anéis para a Terra (semelhantes aos de Saturno)
        drawRings(gl, glut, -50 * this.aspect, -70, 8 * this.aspect);

        // Desativa a iluminação ao finalizar o desenho
        desativarIluminacao(gl);
        gl.glFlush();
    }

    // Método para desenhar nuvens
    public void desenhaNuvens(GL2 gl, GLUT glut) {
        drawAlienShips(gl, glut);
    }

    // Método para desenhar naves alienígenas
    public void drawAlienShips(GL2 gl, GLUT glut) {
        // Desenha a nave alienígena 1
        drawAlienShip(gl, glut, -50 * this.aspect, 30 * this.aspect, 10 * this.aspect, 1.0f, 0.0f, 0.0f);

        // Desenha a nave alienígena 2
        drawAlienShip(gl, glut, 100 * this.aspect, -40 * this.aspect, 12 * this.aspect, 0.0f, 1.0f, 0.0f);
    }

    // Método para desenhar uma nave alienígena
    public void drawAlienShip(GL2 gl, GLUT glut, float x, float y, float size, float r, float g, float b) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, 0);
        gl.glColor3f(r, g, b);

        // Desenha o corpo da nave alienígena (pode ser personalizado)
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(-size, -size / 2);
        gl.glVertex2f(-size, size / 2);
        gl.glVertex2f(size, size / 2);
        gl.glVertex2f(size, -size / 2);
        gl.glEnd();

        // Desenha outras partes da nave alienígena conforme necessário

        gl.glPopMatrix();
    }

    // Método para desenhar nuvem pequena
    // Desenha uma nuvem pequena composta por três esferas
    public void desenhaNuvemP(GL2 gl, GLUT glut) {
        // Primeira esfera da nuvem
        gl.glPushMatrix();
        gl.glTranslatef(0, 0, 0); // Translação para a posição da primeira esfera
        gl.glColor3f(1, 1, 1);    // Cor da esfera (branco)
        glut.glutSolidSphere(8 * this.aspect, 50, 50); // Desenha a esfera sólida
        gl.glPopMatrix();

        // Segunda esfera da nuvem
        gl.glPushMatrix();
        gl.glTranslatef(8 * this.aspect, 2 * this.aspect, 0); // Translação para a posição da segunda esfera
        gl.glColor3f(1, 1, 1);    // Cor da esfera (branco)
        glut.glutSolidSphere(8 * this.aspect, 50, 50); // Desenha a esfera sólida
        gl.glPopMatrix();

        // Terceira esfera da nuvem
        gl.glPushMatrix();
        gl.glTranslatef(16 * this.aspect, 0, 0); // Translação para a posição da terceira esfera
        gl.glColor3f(1, 1, 1);    // Cor da esfera (branco)
        glut.glutSolidSphere(8 * this.aspect, 50, 50); // Desenha a esfera sólida
        gl.glPopMatrix();
    }

    // Desenha a lua como uma esfera
    public void drawLua(GL2 gl, GLUT glut) {
        gl.glPushMatrix();
        gl.glColor3f(1, 0.99f, 0.82f); // Cor da lua (tom amarelado)
        gl.glTranslatef(65 * this.aspect, 65 * this.aspect, 15 * this.aspect); // Translação para a posição da lua
        glut.glutSolidSphere(20 * this.aspect, 50, 50); // Desenha a esfera sólida representando a lua
        gl.glPopMatrix();
    }
}