package Pong.cena;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.gl2.GLUT;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import textura.Textura;

public class Jogo implements GLEventListener {

    //variaveis para controle do SRU
    private float xMin, xMax, yMin, yMax, zMin, zMax; 
    
    //para renderização da tela (utilização de bibliot. para captar resolução da tela)
    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public double width = screenSize.getWidth();
    public double height = screenSize.getHeight();
    public float aspect = (float)(this.width/this.height);
    
    //para movimento do nave e cenário
    public float movNaveX, movCenario;
    
    //para movimento da asteroide
    public float movAsteroideX = 0, movAsteroideY = 0;
    //para colisão da asteroide com as paredes e com o objeto (lua)
    public float colisaoAsteroidY = 0, colisaoAsteroidX = 0;
    //para direção da asteroide (b: baixo, c: cima, e: esquerda, d: direita)
    private char xDirecao, yDirecao = 'b';
    
    //velocidade do jogo (velocidade da translação da asteroide)
    private float velocidadeJogo = 3.5f;
    //para determinar a fase do jogo (0: menu inicial)
    public int fase = 0;
    //para armazenar pontuação
    public int pontuacao = 0;
    //para vida do jogador (inicia com 5)
    public int vidas = 5;
    //variavel para pausar o jogo
    public boolean pausar = true;

    public float limite;
    private Textura textura;
    private int totalTextura = 6;

    private float angulo = 0;

    public float incAngulo = 0;


    //Constantes para identificar as imagens
    public static final String FACE1 = "C:\\Users\\Nicolas Ferreira\\Documents\\antlr\\PongGame\\PongGame\\Game-SkatePong-JOGL-main\\Game-SkatePong-JOGL-main\\src\\imagens\\terra.png";
    public int filtro = GL2.GL_LINEAR; ////GL_NEAREST ou GL_LINEAR
    public int wrap = GL2.GL_REPEAT;  //GL.GL_REPEAT ou GL.GL_CLAMP
    public int modo = GL2.GL_DECAL; ////GL.GL_MODULATE ou GL.GL_DECAL ou GL.GL_BLEND

    //iniciar textos
    private TextRenderer textRenderer;
    private TextRenderer textRenderer1;
    private TextRenderer textRenderer2;
    
    //glu
    GLU glu;

    //Chamadas dos objetos de outros classes
    Asteroide asteroide = new Asteroide();
    Nave nave = new Nave();
    Cenarios cenarios = new Cenarios();

    @Override
    public void init(GLAutoDrawable drawable) {
        //dados iniciais da cena
        glu = new GLU();
        GL2 gl = drawable.getGL().getGL2();

        angulo = 0;
        incAngulo = 0;
        limite = 1;

        textura = new Textura(totalTextura);

        //Estabelece as coordenadas do SRU (Sistema de Referencia do Universo)
        xMin = yMin = zMin = -100*this.aspect;
        xMax = yMax = zMax = 100*this.aspect;
        
        //Caracteristicas dos textos
        textRenderer = new TextRenderer(new Font("Bookman Old Style", Font.CENTER_BASELINE, 50*(int)(this.aspect)));
        textRenderer1 = new TextRenderer(new Font("Bookman Old Style", Font.CENTER_BASELINE, 15*(int)(this.aspect)));
        textRenderer2 = new TextRenderer(new Font("Bookman Old Style", Font.PLAIN, 15*(int)(this.aspect)));

        //Habilita o buffer de profundidade
        gl.glEnable(GL2.GL_DEPTH_TEST);
        geradorEsfera();
    }
    
    @Override
    public void display(GLAutoDrawable drawable) {
        //obtem o contexto Opengl
        GL2 gl = drawable.getGL().getGL2();
        //objeto para desenho 3D
        GLUT glut = new GLUT();
        //define drawablea cor da janela (R, G, G, alpha)
        gl.glClearColor(0, 0, 0, 1);
        //limpa a janela com a cor especificada e o buffer de profundidade
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        //lê a matriz identidade
        gl.glLoadIdentity();

        /*
        DESENHO DA CENA
         */
        
        switch(fase)
        {
            case 0: 
                //caso fase 0, inicia o menu               
                menu(gl, glut);
            break;
            
            case 1:
                //caso fase 1, inicia a primeira fase do jogo
                primeiraFase(gl, glut);
            break;
            
            case 2:
                //caso fase 2, inicia a segunda fase do jogo                
                segundaFase(gl, glut);
            break;
            
            case 3:
                //caso a fase 3, fim do jogo (se as vidas acabarem)
                fimDoJogo(gl, glut);
            break;
        }
        
            
    }
    
    //método para mostrar o menu inicial
    public void menu(GL2 gl, GLUT glut)
    {
        //desliga luz difusa
        desligaLuz1(gl);
        //liga luz ambiente
        iluminacaoAmbiente(gl);

        //não é geração de textura automática
        textura.setAutomatica(false);
        
        //configura os filtros
        textura.setFiltro(filtro);
        textura.setModo(modo);
        textura.setWrap(wrap);  

        textura.gerarTextura(gl, FACE1, 0);

        //Desenho do cenário junto ao menu
        gl.glPushMatrix();
        cenarios.drawCenario1(gl, glut);
        gl.glPopMatrix();

        //desenho da base atrás do menu

        // gl.glPushMatrix();

        // Asteroide com cor de piscina
        // gl.glColor3f(0.5f, 0.7f, 1.0f); // Azul claro como cor de piscina
        // gl.glTranslatef(50 * this.aspect, 2 * this.aspect, 120 * this.aspect);
        // glut.glutSolidSphere(40 * this.aspect, 50, 50);
        // gl.glPopMatrix();
        gl.glPushMatrix();
        //quadrado com textura
            gl.glBegin (GL2.GL_QUADS );
            //coordenadas da Textura            //coordenadas do quads
                gl.glTexCoord2f(0.0f, limite);     gl.glVertex3f(-30.0f, -30.0f,  30.0f);
                gl.glTexCoord2f(limite, limite);     gl.glVertex3f( 30.0f, -30.0f,  30.0f);
                gl.glTexCoord2f(limite, 0.0f);     gl.glVertex3f( 30.0f,  30.0f,  30.0f);
                gl.glTexCoord2f(0.0f, 0.0f);     gl.glVertex3f(-30.0f,  30.0f,  30.0f);
            gl.glEnd();
        gl.glPopMatrix();
        //desabilita a textura indicando o índice
        textura.desabilitarTextura(gl, 0);



        //escrita do menu
        desenhaTexto(gl, (int)(width/5.0), (int)(height/1.5), Color.MAGENTA, "Protect The Earth");
        desenhaTexto1(gl, (int)(width/5.5), (int)(height/1.55), Color.WHITE, "Regras do jogo:\n\n\n\n ");
        desenhaTexto2(gl, (int)(width/5.2), (int)(height/1.61), Color.WHITE, "- Você deve evitar que o asteroide chegue a Terra.");
        desenhaTexto2(gl, (int)(width/5.2), (int)(height/1.67), Color.WHITE, "- Você possui 5 tentativas para impedir que o asteroide colida com os humanos.");
        desenhaTexto2(gl, (int)(width/5.2), (int)(height/1.73), Color.WHITE, "- Caso o asteroide consiga passar pela sua nave, uma vida se perde e a Terra perde população.");
        desenhaTexto2(gl, (int)(width/5.2), (int)(height/1.80), Color.WHITE, "- Você ganha 20 pontos a cada colisão de asteroide evitada.");
        desenhaTexto2(gl, (int)(width/5.2), (int)(height/1.87), Color.WHITE, "- Some 200 pontos e passe de fase onde a gravidade do Sol influência no movimento do asteroide.");
        desenhaTexto2(gl, (int)(width/5.2), (int)(height/1.94), Color.WHITE, "- Na 2°, dificuldade maior e pontuação infinita.");
        desenhaTexto1(gl, (int)(width/5.5), (int)(height/2.05), Color.WHITE, "Botões: ");
        desenhaTexto2(gl, (int)(width/5.2), (int)(height/2.15), Color.WHITE, "- \" <- \"/\" -> \"/\" A \"/\" D \": Movem a nave.");
        desenhaTexto2(gl, (int)(width/5.2), (int)(height/2.25), Color.WHITE, "- \" P \": Pausa o jogo.");
        desenhaTexto2(gl, (int)(width/5.2), (int)(height/2.35), Color.WHITE, "- \" M \": Volta para o menu.");
        desenhaTexto2(gl, (int)(width/5.2), (int)(height/2.45), Color.WHITE, "- \" ENTER \": Start do jogo.");
        desenhaTexto2(gl, (int)(width/5.2), (int)(height/2.58), Color.WHITE, "- \" S \": Fecha o jogo.");
        desenhaTexto1(gl, (int)(width/5.5), (int)(height/2.95), Color.GREEN, "- Aperte \" ENTER \" para começar.");
        desenhaTexto1(gl, (int)(width/2.8), (int)(height/2.95), Color.RED, " - \" S \" para sair.");

        //se o usuário está no menu, ele tem 5 vidas e sua pontuação zerada
        vidas = 5;
        pontuacao = 0;
    }
    
    //inicia a primeira fase do jogo
    public void primeiraFase(GL2 gl, GLUT glut)
    {
        desligaLuz1(gl);
        //liga luz ambiente
        ligaLuz0(gl);
        iluminacaoAmbiente(gl);
        
        //desenha cenário com movimentação adaptada
        gl.glPushMatrix();
            gl.glTranslatef(movCenario, 0, 0);
            cenarios.drawCenario2(gl, glut);
        gl.glPopMatrix(); 
        
        //desenha barra com a movimentação adaptada
        gl.glPushMatrix();
            gl.glTranslatef(movNaveX, 0, 0);
            nave.desenhaNave(gl, glut);
        gl.glPopMatrix();
        
        //desenha a asteroide com a translação adaptada
        gl.glPushMatrix();
            gl.glTranslatef(movAsteroideX, movAsteroideY, 0);
            asteroide.draw(gl, glut);
        gl.glPopMatrix();

        velocidadeJogo = 3.5f;
        
        //mini menu na tela
        miniMenu(gl, glut);
        
        //contagem da pontuacao na tela
        desenhaTexto1(gl, (int)(width/2.0), (int)(height/1.07), Color.YELLOW, "Pontos = ");
        desenhaTexto1(gl, (int)(width/1.82), (int)(height/1.07), Color.YELLOW, Integer.toString(pontuacao));
        
        
        //desenho da vida (coração) + contagem
        desenhaTexto1(gl, (int)(width/14.0), (int)(height/1.07), Color.MAGENTA, "  =  ");
        desenhaTexto1(gl, (int)(width/11.3), (int)(height/1.07), Color.MAGENTA, Integer.toString(vidas));
        gl.glPushMatrix();
            gl.glTranslatef(-90*this.aspect, 90*this.aspect, 30*this.aspect);
            desenhaVidas(gl, glut);
        gl.glPopMatrix();
        
        //acionamento do botão pausar
        if(pausar == false) 
        {
            movimentoAsteroide();
        }
        else 
        {
            desenhaTexto(gl, (int)(width/2.30), (int)(height/1.67), Color.MAGENTA, "PAUSADO");
            desenhaTexto1(gl, (int)(width/2.2), (int)(height/1.74), Color.GREEN, "Aperte M para voltar.");
        }
        
        //se atingir 200 de pontuação, passa de fase
        if(pontuacao == 40)
        {
            fase = 2;
        }
        
        //se as vidas acabarem, mostra tela de fim de jogo
        if(vidas == 0)
        {
            fase = 3;
        } 
        
    }
    
    //inicia a segunda fase
    public void segundaFase(GL2 gl, GLUT glut)
    {
        desligaLuz0(gl);
        //liga a luz difusa
        ligaLuz1(gl);
        iluminacaoDifusa(gl);
        
        //desenha a lua
        gl.glPushMatrix();
            cenarios.drawSol(gl, glut);
        gl.glPopMatrix();
               
        //desenha cenário com movimentação adaptada
        gl.glPushMatrix();
            gl.glTranslatef(movCenario, 0, 0);
            cenarios.drawCenario3(gl, glut);
        gl.glPopMatrix();
        
        //desenha barra com a movimentação adaptada
        gl.glPushMatrix();
            gl.glTranslatef(movNaveX, 0, 0);
            nave.desenhaNave(gl, glut);
        gl.glPopMatrix();
        
        //desenha a asteroide com a translação adaptada
        gl.glPushMatrix();
            gl.glTranslatef(movAsteroideX, movAsteroideY, 0);
            asteroide.draw(gl, glut);
        gl.glPopMatrix();
        
        //velocidade do jogo aumentada 
        velocidadeJogo = 6;
        
        //mini menu na tela
        miniMenu(gl, glut);
        
        //contagem da pontuacao na tela
        desenhaTexto1(gl, (int)(width/2.0), (int)(height/1.07), Color.YELLOW, "Pontos = ");
        desenhaTexto1(gl, (int)(width/1.82), (int)(height/1.07), Color.YELLOW, Integer.toString(pontuacao));
        
        //desenho da vida (coração) + contagem
        desenhaTexto1(gl, (int)(width/14.0), (int)(height/1.07), Color.MAGENTA, "  =  ");
        desenhaTexto1(gl, (int)(width/11.3), (int)(height/1.07), Color.MAGENTA, Integer.toString(vidas));
        gl.glPushMatrix();
            gl.glTranslatef(-90*this.aspect, 90*this.aspect, 30*this.aspect);
            desenhaVidas(gl, glut);
        gl.glPopMatrix();
        
        //acionamento do botão pausar
        if(pausar == false) 
        {
            movimentoAsteroide();
        }
        else 
        {
            desenhaTexto(gl, (int)(width/2.30), (int)(height/1.67), Color.MAGENTA, "Pausado");
            desenhaTexto1(gl, (int)(width/2.2), (int)(height/1.74), Color.GREEN, "Aperte M para voltar.");
        }
        
        //se as vidas acabarem, mostra tela de fim de jogo
        if(vidas == 0)
        {
            fase = 3;
        }
    }
       
    //mostra tela de fim de jogo, caso as vidas acabarem
    public void fimDoJogo(GL2 gl, GLUT glut)
    {

        //desenha o cenário 4
        gl.glPushMatrix();
            cenarios.drawCenario4(gl, glut);
        gl.glPopMatrix();

        //informações sobre o fim do jogo
        desenhaTexto(gl, (int)(width/2.4), (int)(height/1.5), Color.MAGENTA, "A Terra foi destruída!");
        desenhaTexto1(gl, (int)(width/2.3), (int)(height/1.8), Color.WHITE, "Sua pontuação: ");
        desenhaTexto(gl, (int)(width/2.3), (int)(height/2.2), Color.WHITE, Integer.toString(pontuacao));
        desenhaTexto1(gl, (int)(width/2.3), (int)(height/2.6), Color.GREEN, "Aperte M para voltar ao menu.");
        desenhaTexto1(gl, (int)(width/2.3), (int)(height/2.8), Color.RED, "Aperte S para fechar o jogo.");

        pausar = true;

    }
    
    //desenho da vida (coração)
    public void desenhaVidas(GL2 gl, GLUT glut)
    {
        // Desenha a cabeça
        gl.glPushMatrix();
        gl.glColor3f(1.0f, 0.84f, 0.66f); // Cor amarela para a cabeça
        glut.glutSolidSphere(4.0f * this.aspect, 50, 50); // Aumentei o raio para 4.0f
        gl.glPopMatrix();

        // Desenha os olhos (dois círculos pequenos)
        gl.glPushMatrix();
        gl.glColor3f(0.0f, 0.0f, 0.0f); // Cor preta para os olhos
        gl.glTranslatef(-1.5f * this.aspect, 1.0f * this.aspect, 3.2f * this.aspect); // Ajustei as coordenadas
        glut.glutSolidSphere(0.8f * this.aspect, 50, 50); // Aumentei o raio para 0.8f
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glColor3f(0.0f, 0.0f, 0.0f); // Cor preta para os olhos
        gl.glTranslatef(1.5f * this.aspect, 1.0f * this.aspect, 3.2f * this.aspect); // Ajustei as coordenadas
        glut.glutSolidSphere(0.8f * this.aspect, 50, 50); // Aumentei o raio para 0.8f
        gl.glPopMatrix();

        // Desenha a boca (um arco)
        gl.glPushMatrix();
        gl.glColor3f(0.0f, 0.0f, 0.0f); // Cor preta para a boca
        gl.glTranslatef(0.0f, -1.4f * this.aspect, 3.0f * this.aspect); // Ajustei as coordenadas
        gl.glRotatef(180, 1, 0, 0); // Gira a boca para baixo
        glut.glutSolidCone(2.0f * this.aspect, 2.5f * this.aspect, 50, 50); // Reduzi o tamanho da boca
        gl.glPopMatrix();
    }
    
    //mostra o mini menu durante o jogo
    public void miniMenu(GL2 gl, GLUT glut)
    {
        desenhaTexto1(gl, (int)(width/8.0), (int)(height/1.07), Color.GREEN, "\"P\": Pausar | \"M\": Menu");
    }
    
    //para desenhar textos do tipo 0
    public void desenhaTexto(GL2 gl, int xPosicao, int yPosicao, Color cor, String frase)
    {         
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        //Retorna a largura e altura da janela
        textRenderer.beginRendering(Renderizar.screenWidth, Renderizar.screenHeight);
        textRenderer.setColor(cor);
        textRenderer.draw(frase, xPosicao, yPosicao);
        textRenderer.endRendering();
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    }
    
    //textos 1
    public void desenhaTexto1(GL2 gl, int xPosicao, int yPosicao, Color cor, String frase)
    {         
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        //Retorna a largura e altura da janela
        textRenderer1.beginRendering(Renderizar.screenWidth, Renderizar.screenHeight);
        textRenderer1.setColor(cor);
        textRenderer1.draw(frase, xPosicao, yPosicao);
        textRenderer1.endRendering();
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    }
    
    //textos　2
    public void desenhaTexto2(GL2 gl, int xPosicao, int yPosicao, Color cor, String frase)
    {         
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        //Retorna a largura e altura da janela
        textRenderer2.beginRendering(Renderizar.screenWidth, Renderizar.screenHeight);
        textRenderer2.setColor(cor);
        textRenderer2.draw(frase, xPosicao, yPosicao);
        textRenderer2.endRendering();
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    }
    
    //luz ambiente
    public void iluminacaoAmbiente(GL2 gl) 
    {
        float luzAmbiente[] = {0.5f, 0.5f, 0.4f, 1}; //cor
        float posicaoLuz[] = {90, 90, 0, 0}; //1.0: pontual, 0: distante
        // define parametros de luz de numero 0 (zero)
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, luzAmbiente, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, posicaoLuz, 0);
    }
    
    //luz difusa
    public void iluminacaoDifusa(GL2 gl) 
    {
        float luzDifusa[] = {0.8f, 0.8f, 0.8f, 1}; //cor
        float posicaoLuz[] = {10, 10, 30, 0}; //1.0: pontual, 0: distante
        //define os parametros de luz de numero 1
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, luzDifusa, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, posicaoLuz, 0);
    }
    
    //luz ambiente on
    public void ligaLuz0(GL2 gl) 
    {
        // habilita a definicao da cor do material a partir da cor corrente
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        // ilumina cena
        gl.glEnable(GL2.GL_LIGHTING);
        // luz on
        gl.glEnable(GL2.GL_LIGHT0);
        //Especifica o Modelo de tonalizacao a ser utilizado 
        //GL_FLAT -> modelo de tonalizacao flat 
        //GL_SMOOTH -> modelo de tonalizacao GOURAUD (default)        
        gl.glShadeModel(GL2.GL_SMOOTH);
    }
    
    //luz difusa on
    public void ligaLuz1(GL2 gl) 
    {
        // habilita a definicao da cor do material a partir da cor corrente
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        // habilita o uso da ilumina��o na cena
        gl.glEnable(GL2.GL_LIGHTING);
        // habilita a luz
        gl.glEnable(GL2.GL_LIGHT1);
        //Especifica o Modelo de tonalizacao a ser utilizado 
        //GL_FLAT -> modelo de tonalizacao flat 
        //GL_SMOOTH -> modelo de tonalizacao GOURAUD (default)        
        gl.glShadeModel(GL2.GL_SMOOTH);
    }

    //luz ambiente
    public void desligaLuz0(GL2 gl) 
    {
        //desabilita o ponto de luz
        gl.glDisable(GL2.GL_LIGHT0);
    }
    
    //luz difusa
    public void desligaLuz1(GL2 gl) 
    {
        //desabilita o ponto de luz
        gl.glDisable(GL2.GL_LIGHT1);
    }
    
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) 
    {
        //obtem o contexto grafico Opengl
        GL2 gl = drawable.getGL().getGL2();

        //seta o viewport para abranger a janela inteira
        gl.glViewport(0, 0, width, height);

        //ativa a matriz de projeção
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity(); //lê a matriz identidade

        //Projeção ortogonal sem a correcao do aspecto
        gl.glOrtho(xMin, xMax, yMin, yMax, zMin, zMax);

        //ativa a matriz de modelagem
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity(); //lê a matriz identidade
        System.out.println("Reshape: " + width + ", " + height);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {}

    //método que altera a posição da asteroide no eixo x aleatoriamente na parte superior da tela (quando ela não colide com a barra)
    public void geradorEsfera() 
    {
        if ((-50*this.aspect) + (Math.random() * this.xMax) > 0) {
            this.xDirecao = 'd';
        } else {
            this.xDirecao = 'e';
        }
    }

    //altera a direção da asteroide no eixo X (d: direita e e: esquerda)
    public void mudaDirecaoX() 
    {
        if (this.xDirecao == 'd') {
            this.xDirecao = 'e';
        } else {
            this.xDirecao = 'd';
        }
    }

    //altera a direção da asteroide no eixo Y (b: baixo e c: cima)
    public void mudaDirecaoY() 
    {
        if (this.yDirecao == 'b') 
        {
            this.yDirecao = 'c';
        } 
        else 
        {
            this.yDirecao = 'b';
        }
    }

    //o nave foi "dividido" em três para alterar a direção da asteroide caso colidir em diferentes extremidades
   
    //método para colisão da asteroide com o nave na extremidade da esquerda
    public boolean colisaoEsquerdaNave()
    {
        float limiteEsqSkate = movNaveX - 19.0f;//limite do tamanho do nave na esquerda
        float esquerdaSkate = movNaveX - 17.0f;//um pouco abaixo do limite
        //se o movimento da asteroide no eixo x for maior ou igual ao limite do nave
        //e menor ou igual um pouco abaixo do limite, ela colide na ponta esquerda
        if(movAsteroideX >= limiteEsqSkate && movAsteroideX <= esquerdaSkate)
        {
            return true;        
        }
        else
            return false;            
    }
    
    //método para colisão da asteroide com o meio do nave
    public boolean colisaoMeioNave()
    {
        float limiteEsqSkate = movNaveX - 17.0f;//limite do tamanho do nave na esquerda
        float limiteDirSkate = movNaveX + 17.0f;//limite do tamanho do nave na direita
        //se o movimento da asteroide no eixo x for maior ou igual ao limite do nave na esq
        //e maior igual ao da direita, ela colide no meio
        if(movAsteroideX >= limiteEsqSkate && movAsteroideX <= limiteDirSkate)
        {
            return true;        
        }
        else
            return false;            
    }
 
    //método para colisão da asteroide com o nave na extremidade da direita
    public boolean colisaoDireitaNave()
    {
        float limiteDirSkate = movNaveX + 19; //limite do tamanho do nave na direita
        float direitaSkate = movNaveX + 17; //um pouco abaixo do limite
        //se o movimento da asteroide no eixo x for menor ou igual ao limite do nave
        //e maior igual um pouco abaixo do limite, ela colide na ponta direita
        if(movAsteroideX <= limiteDirSkate && movAsteroideX >= direitaSkate)
        {
            return true;
        }
        else
            return false;
    }
    
    //método para colisão da asteroide com a parte inferior da lua
    public boolean colisaoSolEmbaixo()
    {
        
        float alturaY = 65.000000f; //altura da lua no eixo Y
        float limiteDireita = 175.000000f; //limite da direita da lua
        float limiteEsquerda = 80.000000f; //limite da esquerda da lua
        
        //se o movimento da asteroide no eixo X for menor que o limite da direira, maior que o da esquerda
        //e o movimento da asteroide no eixo Y for maior ou igual a altura, retorna verdadeiro
        if(movAsteroideX < limiteDireita && movAsteroideX > limiteEsquerda && movAsteroideY >= alturaY)
        {
            return true;     
        }
        
        else
            return false;
        
    }
    
    //método para colisão da asteroide com a esquerda da lua
    public boolean colisaoSolLado()
    {
        float alturaLateralX = 70.000000f;//localizacao da altura da lateral da lua no eixo x
        float limiteSuperior = 175.000000f;//limite superior da lateral da lua
        float limiteInferior = 65.000000f;//limite inferior da lateral da lua
        
        //se o movimento da asteroide no eixo Y for menor que o limite da superior, maior que o inferior
        //e o movimento da asteroide no eixo X for maior ou igual a altura da lateral da lua, retorna verdadeiro
        if(movAsteroideY < limiteSuperior && movAsteroideY > limiteInferior && movAsteroideX >= alturaLateralX)
        {
            return true;
        }
        
        //se não retorna falso
        else
            return false;       
    }
    
    //quando a asteroide passa da barra, outra é gerada
    public void reset() 
    {
        this.movAsteroideY = this.yMax;
        this.movAsteroideX = (float) Math.random();
        geradorEsfera();
    }

    //método para movimentar a asteroide
    public void movimentoAsteroide()
    {
        //movimento da asteroide no eixo x
        switch (this.xDirecao) 
        {
            //caso a asteroide estiver se movimentando para esquerda
            case 'e' -> 
            {   
                //se estiver na fase 2, indo para esquerda, pra cima e colidir embaixo da lua, 
                if(this.fase == 2 && yDirecao == 'c' && colisaoSolEmbaixo())
                {
                    //muda a direcao da asteroide no eixo y
                    mudaDirecaoY();
                }
                
                //enquanto o movimento da asteroide for maior que o limite da tela na esquerda
                if (this.movAsteroideX > -92*this.aspect)
                {
                    //decrementa o valor da variavel de mov no eixo x com a velocidade, que sera usada no translate
                    this.movAsteroideX -= velocidadeJogo;
                }
                
                //se o movimento da asteroide atingir a parede da esquerda,
                if (this.movAsteroideX == -92*this.aspect && this.xDirecao == 'e' || this.movAsteroideX <= -92*this.aspect && this.xDirecao == 'e')
                {
                    //muda a direção da asteroide no eixo x
                    mudaDirecaoX();
                }
            }//fim do case e
            
            //caso a asteroide estiver se movimentando para direita
            case 'd' -> 
            {
                //se estiver na fase 2, indo para direita, para baixo e colidir com a lateral da lua,
                if(this.fase == 2 && yDirecao == 'b' && colisaoSolLado()) 
                {
                    //muda a direção de x
                    mudaDirecaoX();
                }

                    //se estiver na fase 2, indo para direita, pra cima e colidir embaixo da lua,
                    else if(this.fase == 2 && yDirecao == 'c' && colisaoSolEmbaixo())
                    {
                        //muda a direção de y
                        mudaDirecaoY();
                    }
                
                    //se estiver na fase 2, indo para direita, pra cima e colidir com a lateral da lua,
                    else if(this.fase == 2 && yDirecao == 'c' && colisaoSolLado()) 
                    {
                        //muda a direção de x
                        mudaDirecaoX();
                    }
                
                //enquanto o movimento for menor que a extremidade da parede da direira e tiver indo pra direita,
                if (this.movAsteroideX < 92*this.aspect && this.xDirecao == 'd')
                {
                    //incrementa o valor da variavel de mov no eixo x com a velocidade, que sera usada no translate
                    this.movAsteroideX += velocidadeJogo;
                }
                
                //se o movimento da asteroide atingir a extremidade da parede da direita,
                if (this.movAsteroideX == 92*this.aspect && this.xDirecao == 'd' || this.movAsteroideX >= 92*this.aspect && this.xDirecao == 'd')
                {
                    //muda de posição
                    this.mudaDirecaoX();
                }
            } //fim do case d
            
        }//fim do switch x
        
        //movimento da asteroide no eixo y
        switch (this.yDirecao) 
        {
            //caso a asteroide estiver indo para baixo
            case 'b' -> 
            {
                
                //se a asteroide estiver na mesma altura que o nave e colidir com a ponta esquerda do nave
                if (this.movAsteroideY <= -76*this.aspect && colisaoEsquerdaNave())
                {
                    //muda a direcao de y, de x e soma 20 na pontuacao
                    mudaDirecaoY();
                    this.xDirecao = 'e';
                    pontuacao += 20;
                }
                
                    //se a asteroide estiver na mesma altura que o nave e colidir com a ponta direita
                    else if (this.movAsteroideY <= -76*this.aspect && colisaoDireitaNave())
                    {
                        //muda a direcao de y, de x e soma 20 na pontuacao
                        mudaDirecaoY();
                        this.xDirecao = 'd';
                        pontuacao += 20;
                    }
                
                    //se a asteroide estiver na mesma altura que o nave e colidir com o meio
                    else if (this.movAsteroideY <= -76*this.aspect && colisaoMeioNave())
                    {
                        //muda a direcao de y e soma 20 na pontuacao
                        mudaDirecaoY();
                        pontuacao += 20;
                    }
                
                //se a asteroide passar a altura do nave, logicamente ela nao colide
                else if (this.movAsteroideY <= -90*this.aspect)
                {
                    //o movimento da asteroide reinicia e uma vida eh descontada
                    reset();
                    vidas--;
                }
                
                //enquanto nenhuma colisão ocorre ou a asteroide seja reiniciada,
                else 
                {
                    //decrementa o valor da variavel de mov no eixo y com a velocidade, que sera usada no translate
                    movAsteroideY -= velocidadeJogo;
                }
            }//fim do case b
            
            //caso indo para cima
            case 'c' -> 
            {                             
                //enquanto nao atingir o teto, 
                if (this.movAsteroideY < (92*this.aspect))
                { 
                    //incrementa o valor da variavel de mov no eixo y com a velocidade, que sera usada no translate
                    this.movAsteroideY += velocidadeJogo;
                }
                //se atingir o teto, muda a direcao
                if (this.movAsteroideY >= (92*this.aspect))
                { 
                    mudaDirecaoY();
                }
            }//fim do case c 
            
        }//fim do switch y

    }//fim do movimentoEsfera()
    
    //getters e setters
    public float getMovNaveX()
    {
        return movNaveX;
    }

    public void setMovNaveX(float movNaveX)
    {
        this.movNaveX = movNaveX;
    }

    public float getMovCenario() 
    {
        return movCenario;
    }

    public void setMovCenario(float movCenario) 
    {
        this.movCenario = movCenario;
    }
    
    public int getFase() {
        return fase;
    }

    public void setFase(int fase) {
        this.fase = fase;
    }

    public boolean isPausar() {
        return pausar;
    }

    public void setPausar(boolean pausar) {
        this.pausar = pausar;
    }
    
    public float getAspect() {
        return aspect;
    }

    public void setAspect(float aspect) {
        this.aspect = aspect;
    }

    public float getxMin() {
        return xMin;
    }

    public void setxMin(float xMin) {
        this.xMin = xMin;
    }

    public float getxMax() {
        return xMax;
    }

    public void setxMax(float xMax) {
        this.xMax = xMax;
    }
       
}//fim da classe cena
