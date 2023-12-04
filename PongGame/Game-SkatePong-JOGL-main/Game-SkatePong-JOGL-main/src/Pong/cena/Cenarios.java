package Pong.cena;

import static Pong.cena.Jogo.screenSize;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

// Classe para produção dos cenários
public class Cenarios {
    // Dados para animação do cenário no menu
    public float movNave = 0;

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
        desenhaCeuMenu(gl, glut);
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
        drawSol(gl, glut);
    }

    // Método para desenhar cenário do fim do jogo
    public void drawCenario4(GL2 gl, GLUT glut) {
        desenhaCampoFim(gl);
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

    // Desenha o campo do fim de jogo
    public void desenhaCampoFim(GL2 gl) {
        gl.glPushMatrix();
        gl.glColor3f(0, 0, 0); // Cor preta
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(-200 * this.aspect, -20 * this.aspect);
        gl.glVertex2f(-200 * this.aspect, 100 * this.aspect);
        gl.glVertex2f(200 * this.aspect, 100 * this.aspect);
        gl.glVertex2f(200 * this.aspect, -20 * this.aspect);
        gl.glEnd();
        gl.glFlush();
        gl.glPopMatrix();
    }

    //desenha o ceu com as nuvens do menu

    public void desenhaCeuMenu(GL2 gl, GLUT glut) {
        // Define a cor de fundo para preto
        gl.glClearColor(0, 0, 0, 1);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

        gl.glPushMatrix();
        gl.glTranslatef((movNave += 0.04f) * this.aspect, 0, 0);
        desenhaNuvens(gl, glut);
        gl.glPopMatrix();

        // Desenha o céu preto
        gl.glPushMatrix();
        gl.glColor3f(0, 0, 0); // Cor preta
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(-200 * this.aspect, -20 * this.aspect);
        gl.glVertex2f(-200 * this.aspect, 100 * this.aspect);
        gl.glVertex2f(200 * this.aspect, 100 * this.aspect);
        gl.glVertex2f(200 * this.aspect, -20 * this.aspect);
        gl.glEnd();
        gl.glFlush();
        gl.glPopMatrix();
    }

    // Desenha o céu da primeira fase
    public void desenhaCeu1Fase(GL2 gl, GLUT glut) {
        // Limpa o buffer de cor e profundidade, configurando o fundo para preto
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        // Desenha estrelas brancas no céu
        gl.glColor3f(1.0f, 1.0f, 1.0f); // Cor branca para estrelas

        for (int i = 0; i < 25; i++) {
            float x = (float) (Math.random() * 400 * this.aspect) - 200 * this.aspect;
            float y = (float) (Math.random() * 200 * this.aspect) - 100 * this.aspect;

            gl.glPushMatrix();
            gl.glTranslatef(x, y, 0);
            glut.glutSolidSphere(0.1f * this.aspect, 5, 5); // Desenha uma pequena asteroide como estrela
            gl.glPopMatrix();
        }

        // Configuração da iluminação para os planetas
        configurarIluminacao(gl);

        // Fatores de escala para representar tamanhos reais dos planetas (em relação à Terra)
        float escalaPlaneta1 = 0.383f;
        float escalaPlaneta2 = 0.949f;
        float escalaPlaneta3 = 1.0f; // Tamanho da Terra mantido como referência
        float escalaPlaneta4 = 0.532f;
        float escalaPlaneta5 = 5.0f;
        float escalaPlaneta6 = 4.0f;
        float escalaPlaneta7 = 2.0f;
        float escalaPlaneta8 = 1.8f;

        // Desenha planetas estáticos do sistema solar (excluindo o Sol)
        desenharPlaneta(gl, glut, -100 * this.aspect, 20, escalaPlaneta1 * 5 * this.aspect, 0.7f, 0.7f, 0.7f); // Mercúrio
        desenharPlaneta(gl, glut, -80 * this.aspect, 90, escalaPlaneta2 * 5 * this.aspect, 0.9f, 0.5f, 0.2f);    // Vênus
        desenharPlaneta(gl, glut, -70 * this.aspect, -70, escalaPlaneta3 * 7 * this.aspect, 0.2f, 0.2f, 1.0f);   // Terra
        desenharPlaneta(gl, glut, -50, 90, escalaPlaneta4 * 6 * this.aspect, 1.0f, 0.4f, 0.4f);                   // Marte
        desenharPlaneta(gl, glut, 20 * this.aspect, 20, escalaPlaneta6 * 5 * this.aspect, 0.8f, 0.8f, 0.7f); // Saturno
        desenharPlaneta(gl, glut, 60 * this.aspect, -80, escalaPlaneta7 * 5 * this.aspect, 0.5f, 0.8f, 0.8f);     // Urano
        desenharPlaneta(gl, glut, 100 * this.aspect, 50, escalaPlaneta8 * 5 * this.aspect, 0.2f, 0.4f, 0.8f);   // Netuno

        // Desenha anéis para Terra
        DesenheAneis(gl, glut, -70 * this.aspect, -70, escalaPlaneta6 * 7 * this.aspect);


        // Desativa a iluminação ao finalizar o desenho
        desativarIluminacao(gl);

        gl.glFlush();
    }

    // Método auxiliar para desenhar um planeta estático
    private void desenharPlaneta(GL2 gl, GLUT glut, float x, float y, float radius, float r, float g, float b) {
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
    private void DesenheAneis(GL2 gl, GLUT glut, float x, float y, float radius) {
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

    public void desenhaCeu2Fase(GL2 gl, GLUT glut) {
        // Limpa o buffer de cor e profundidade, configurando o fundo para preto
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        // Desenha estrelas brancas no céu
        gl.glColor3f(1.0f, 1.0f, 1.0f); // Cor branca para estrelas

        for (int i = 0; i < 25; i++) {
            float x = (float) (Math.random() * 400 * this.aspect) - 200 * this.aspect;
            float y = (float) (Math.random() * 200 * this.aspect) - 100 * this.aspect;

            gl.glPushMatrix();
            gl.glTranslatef(x, y, 0);
            glut.glutSolidSphere(0.1f * this.aspect, 5, 5); // Desenha uma pequena asteroide como estrela
            gl.glPopMatrix();
        }

        // Configuração da iluminação para os planetas
        configurarIluminacao(gl);

        // Fatores de escala para representar tamanhos reais dos planetas (em relação à Terra)
        float escalaPlaneta1 = 0.383f;
        float escalaPlaneta2 = 0.949f;
        float escalaPlaneta3 = 1.0f; // Tamanho da Terra mantido como referência
        float escalaPlaneta4 = 0.532f;
        float escalaPlaneta5 = 5.0f;
        float escalaPlaneta6 = 4.0f;
        float escalaPlaneta7 = 2.0f;
        float escalaPlaneta8 = 1.8f;

        // Desenha planetas estáticos do sistema solar (excluindo o Sol)
        desenharPlaneta(gl, glut, -100 * this.aspect, 20, escalaPlaneta1 * 5 * this.aspect, 0.7f, 0.7f, 0.7f); // Mercúrio
        desenharPlaneta(gl, glut, -80 * this.aspect, 90, escalaPlaneta2 * 5 * this.aspect, 0.9f, 0.5f, 0.2f);    // Vênus
        desenharPlaneta(gl, glut, -70 * this.aspect, -70, escalaPlaneta3 * 7 * this.aspect, 0.2f, 0.2f, 1.0f);   // Terra
        desenharPlaneta(gl, glut, -50, 90, escalaPlaneta4 * 6 * this.aspect, 1.0f, 0.4f, 0.4f);                   // Marte
        desenharPlaneta(gl, glut, 20 * this.aspect, 20, escalaPlaneta6 * 5 * this.aspect, 0.8f, 0.8f, 0.7f); // Saturno
        desenharPlaneta(gl, glut, 60 * this.aspect, -80, escalaPlaneta7 * 5 * this.aspect, 0.5f, 0.8f, 0.8f);     // Urano
        desenharPlaneta(gl, glut, 100 * this.aspect, 50, escalaPlaneta8 * 5 * this.aspect, 0.2f, 0.4f, 0.8f);   // Netuno

        // Desenha anéis para Terra
        DesenheAneis(gl, glut, -70 * this.aspect, -70, escalaPlaneta6 * 7 * this.aspect);

        // Desenha a lua
        drawSol(gl, glut);

        // Desativa a iluminação ao finalizar o desenho
        desativarIluminacao(gl);

        gl.glFlush();
    }

    public void desenhaNuvens(GL2 gl, GLUT glut)
    {
        // Draw alien ships
        desenheNaveAlien(gl, glut);
    }

    public void desenheNaveAlien(GL2 gl, GLUT glut)
    {
        // Draw alien ship 1
        desenheNaveAlien(gl, glut, -50 * this.aspect, 30 * this.aspect, 10 * this.aspect, 1.0f, 0.0f, 0.0f);

        // Draw alien ship 2
        desenheNaveAlien(gl, glut, 100 * this.aspect, -40 * this.aspect, 12 * this.aspect, 0.0f, 1.0f, 0.0f);

        // Draw more alien ships as needed
    }

    public void desenheNaveAlien(GL2 gl, GLUT glut, float x, float y, float size, float r, float g, float b)
    {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, 0);
        gl.glColor3f(r, g, b);

        // Corpo principal da nave
        gl.glColor3f(0.8f, 0.2f, 0.8f); // Cor roxa
        gl.glPushMatrix();
        gl.glTranslatef(0, -90 * this.aspect, 60 * this.aspect);
        gl.glScalef(1, 0.5f, 0.5f); // Achatamento para uma forma mais interessante
        glut.glutSolidSphere(15 * this.aspect, 16, 8);
        gl.glPopMatrix();

        // Parte frontal da nave
        gl.glColor3f(0.2f, 0.8f, 0.2f); // Cor verde
        gl.glPushMatrix();
        gl.glTranslatef(0, -90 * this.aspect, 40 * this.aspect);
        glut.glutSolidCone(10 * this.aspect, 40 * this.aspect, 16, 8);
        gl.glPopMatrix();

        // Draw other parts of the alien ship as needed

        gl.glPopMatrix();
    }

    // Método para desenhar a lua como uma asteroide
    void drawSol(GL2 gl, GLUT glut) {
        gl.glPushMatrix();
        gl.glTranslatef(65 * this.aspect, 65 * this.aspect, 15 * this.aspect); // Posição da lua
        gl.glColor3f(1.0f, 1.0f, 0.0f); // Cor da lua (amarela)
        glut.glutSolidSphere(25 * this.aspect, 50, 50); // Ajuste o raio para 30 (por exemplo)
        gl.glPopMatrix();
    }
}