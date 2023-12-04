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

public class Cena implements GLEventListener {

    //variaveis para controle do SRU
    private float xMin, xMax, yMin, yMax, zMin, zMax; 
    
    //para renderização da tela (utilização de bibliot. para captar resolução da tela)
    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public double width = screenSize.getWidth();
    public double height = screenSize.getHeight();
    public float aspect = (float)(this.width/this.height);
    
    //para movimento do skate e cenário
    public float movSkateX, movCenario;
    
    //para movimento da esfera
    public float movEsferaX = 0, movEsferaY = 0;
    //para colisão da esfera com as paredes e com o objeto (lua)
    public float colisaoEsferaY = 0, colisaoEsferaX = 0;
    //para direção da esfera (b: baixo, c: cima, e: esquerda, d: direita)
    private char xDirecao, yDirecao = 'b';
    
    //velocidade do jogo (velocidade da translação da esfera)
    private float velocidadeJogo = 3.5f;
    //para determinar a fase do jogo (0: menu inicial)
    public int fase = 0;
    //para armazenar pontuação
    public int pontuacao = 0;
    //para vida do jogador (inicia com 5)
    public int vidas = 5;
    //variavel para pausar o jogo
    public boolean pausar = true;

    //iniciar textos
    private TextRenderer textRenderer;
    private TextRenderer textRenderer1;
    private TextRenderer textRenderer2;
    
    //glu
    GLU glu;

    //Chamadas dos objetos de outros classes
    Esfera esfera = new Esfera();
    Skate skate = new Skate();
    Cenarios cenarios = new Cenarios();

    @Override
    public void init(GLAutoDrawable drawable) {
        //dados iniciais da cena
        glu = new GLU();
        GL2 gl = drawable.getGL().getGL2();

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
    //método para mostrar o menu inicial
    public void menu(GL2 gl, GLUT glut)
    {
        //desliga luz difusa
        desligaLuz1(gl);
        //liga luz ambiente
        iluminacaoAmbiente(gl);

        //Desenho do cenário junto ao menu
        gl.glPushMatrix();
        cenarios.drawCenario1(gl, glut);
        gl.glPopMatrix();

        //desenho da base atrás do menu

        gl.glPushMatrix();

        // Esfera com cor de piscina
        gl.glColor3f(0.5f, 0.7f, 1.0f); // Azul claro como cor de piscina
        gl.glTranslatef(50 * this.aspect, 2 * this.aspect, 120 * this.aspect);
        glut.glutSolidSphere(40 * this.aspect, 50, 50);

        gl.glPopMatrix();

        //escrita do menu
        desenhaTexto(gl, (int)(width/5.0), (int)(height/1.5), Color.MAGENTA, "Protect The Earth");
        desenhaTexto1(gl, (int)(width/5.5), (int)(height/1.55), Color.WHITE, "Regras do jogo:\n\n\n\n ");
        desenhaTexto2(gl, (int)(width/5.2), (int)(height/1.61), Color.WHITE, "- Você deve evitar que o asteroide chegue a Terra.");
        desenhaTexto2(gl, (int)(width/5.2), (int)(height/1.67), Color.WHITE, "- Você possui 5 tentativas para impedir que o asteroide colida com os humanos.");
        desenhaTexto2(gl, (int)(width/5.2), (int)(height/1.73), Color.WHITE, "- Caso o asteroide consiga passar pela sua nave, uma vida se perde e a Terra perde população.");
        desenhaTexto2(gl, (int)(width/5.2), (int)(height/1.80), Color.WHITE, "- Você ganha 20 pontos a cada colisão de asteroide evitada.");
        desenhaTexto2(gl, (int)(width/5.2), (int)(height/1.87), Color.WHITE, "- Some 200 pontos e passe de fase onde agora o Sol influência no movimento do asteroide.");
        desenhaTexto2(gl, (int)(width/5.2), (int)(height/1.94), Color.WHITE, "- Na 2°, dificuldade maior e pontuação infinita.");
        desenhaTexto1(gl, (int)(width/5.5), (int)(height/2.05), Color.WHITE, "Botões: ");
        desenhaTexto2(gl, (int)(width/5.2), (int)(height/2.15), Color.WHITE, "- \" <- \"/\" -> \"/\" A \"/\" D \": Movem a nave.");
        desenhaTexto2(gl, (int)(width/5.2), (int)(height/2.25), Color.WHITE, "- \" P \": Pausa o jogo.");
        desenhaTexto2(gl, (int)(width/5.2), (int)(height/2.35), Color.WHITE, "- \" S \": Volta para o menu.");
        desenhaTexto2(gl, (int)(width/5.2), (int)(height/2.45), Color.WHITE, "- \" J \": Start do jogo.");
        desenhaTexto2(gl, (int)(width/5.2), (int)(height/2.58), Color.WHITE, "- \" F \": Fecha o jogo.");
        desenhaTexto1(gl, (int)(width/5.5), (int)(height/2.95), Color.GREEN, "- Aperte \" J \" para começar.");
        desenhaTexto1(gl, (int)(width/2.8), (int)(height/2.95), Color.RED, " | \" F \" para sair.");

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
            gl.glTranslatef(movSkateX, 0, 0);
            skate.desenhaSkate(gl, glut);
        gl.glPopMatrix();
        
        //desenha a esfera com a translação adaptada
        gl.glPushMatrix();
            gl.glTranslatef(movEsferaX, movEsferaY, 0);
            esfera.draw(gl, glut);
        gl.glPopMatrix();

        velocidadeJogo = 3.5f;
        
        //mini menu na tela
        miniMenu(gl, glut);
        
        //contagem da pontuacao na tela
        desenhaTexto1(gl, (int)(width/2.0), (int)(height/1.07), Color.YELLOW, "Score = ");
        desenhaTexto1(gl, (int)(width/1.82), (int)(height/1.07), Color.YELLOW, Integer.toString(pontuacao));
        
        
        //desenho da vida (coração) + contagem
        desenhaTexto1(gl, (int)(width/14.0), (int)(height/1.07), Color.MAGENTA, "  =  ");
        desenhaTexto1(gl, (int)(width/11.3), (int)(height/1.07), Color.MAGENTA, Integer.toString(vidas));
        gl.glPushMatrix();
            gl.glTranslatef(-90*this.aspect, 90*this.aspect, 30*this.aspect);
            desenhaCoracao(gl, glut);
        gl.glPopMatrix();
        
        //acionamento do botão pausar
        if(pausar == false) 
        {
            movimentoEsfera();
        }
        else 
        {
            desenhaTexto(gl, (int)(width/2.30), (int)(height/1.67), Color.MAGENTA, "PAUSADO");
            desenhaTexto1(gl, (int)(width/2.2), (int)(height/1.74), Color.GREEN, "Aperte P para voltar.");
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
            cenarios.drawLua(gl, glut);
        gl.glPopMatrix();
               
        //desenha cenário com movimentação adaptada
        gl.glPushMatrix();
            gl.glTranslatef(movCenario, 0, 0);
            cenarios.drawCenario3(gl, glut);
        gl.glPopMatrix();
        
        //desenha barra com a movimentação adaptada
        gl.glPushMatrix();
            gl.glTranslatef(movSkateX, 0, 0);
            skate.desenhaSkate(gl, glut);
        gl.glPopMatrix();
        
        //desenha a esfera com a translação adaptada
        gl.glPushMatrix();
            gl.glTranslatef(movEsferaX, movEsferaY, 0);
            esfera.draw(gl, glut);
        gl.glPopMatrix();
        
        //velocidade do jogo aumentada 
        velocidadeJogo = 6;
        
        //mini menu na tela
        miniMenu(gl, glut);
        
        //contagem da pontuacao na tela
        desenhaTexto1(gl, (int)(width/2.0), (int)(height/1.07), Color.YELLOW, "Score = ");
        desenhaTexto1(gl, (int)(width/1.82), (int)(height/1.07), Color.YELLOW, Integer.toString(pontuacao));
        
        //desenho da vida (coração) + contagem
        desenhaTexto1(gl, (int)(width/14.0), (int)(height/1.07), Color.MAGENTA, "  =  ");
        desenhaTexto1(gl, (int)(width/11.3), (int)(height/1.07), Color.MAGENTA, Integer.toString(vidas));
        gl.glPushMatrix();
            gl.glTranslatef(-90*this.aspect, 90*this.aspect, 30*this.aspect);
            desenhaCoracao(gl, glut);
        gl.glPopMatrix();
        
        //acionamento do botão pausar
        if(pausar == false) 
        {
            movimentoEsfera();
        }
        else 
        {
            desenhaTexto(gl, (int)(width/2.30), (int)(height/1.67), Color.MAGENTA, "PAUSADO");
            desenhaTexto1(gl, (int)(width/2.2), (int)(height/1.74), Color.GREEN, "Aperte P para voltar.");
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
        desenhaTexto(gl, (int)(width/2.4), (int)(height/1.5), Color.MAGENTA, "Fim de jogo!");
        desenhaTexto1(gl, (int)(width/2.3), (int)(height/1.8), Color.WHITE, "Sua pontuação: ");
        desenhaTexto(gl, (int)(width/2.3), (int)(height/2.2), Color.WHITE, Integer.toString(pontuacao));
        desenhaTexto1(gl, (int)(width/2.3), (int)(height/2.6), Color.GREEN, "Aperte S para voltar ao menu.");
        desenhaTexto1(gl, (int)(width/2.3), (int)(height/2.8), Color.RED, "Aperte F para fechar o jogo.");

        pausar = true;

    }
    
    //desenho da vida (coração)
    public void desenhaCoracao(GL2 gl, GLUT glut)
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
        desenhaTexto1(gl, (int)(width/8.0), (int)(height/1.07), Color.GREEN, "\"P\": pausar | \"S\": stop (menu)");
    }
    
    //para desenhar textos do tipo 0
    public void desenhaTexto(GL2 gl, int xPosicao, int yPosicao, Color cor, String frase)
    {         
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        //Retorna a largura e altura da janela
        textRenderer.beginRendering(Renderer.screenWidth, Renderer.screenHeight);       
        textRenderer.setColor(cor);
        textRenderer.draw(frase, xPosicao, yPosicao);
        textRenderer.endRendering();
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    }
    
    //para desenhar textos do tipo 1
    public void desenhaTexto1(GL2 gl, int xPosicao, int yPosicao, Color cor, String frase)
    {         
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        //Retorna a largura e altura da janela
        textRenderer1.beginRendering(Renderer.screenWidth, Renderer.screenHeight);       
        textRenderer1.setColor(cor);
        textRenderer1.draw(frase, xPosicao, yPosicao);
        textRenderer1.endRendering();
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    }
    
    //para desenhar textos do tipo 2
    public void desenhaTexto2(GL2 gl, int xPosicao, int yPosicao, Color cor, String frase)
    {         
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        //Retorna a largura e altura da janela
        textRenderer2.beginRendering(Renderer.screenWidth, Renderer.screenHeight);       
        textRenderer2.setColor(cor);
        textRenderer2.draw(frase, xPosicao, yPosicao);
        textRenderer2.endRendering();
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    }
    
    //desenvolve a luz ambiente
    public void iluminacaoAmbiente(GL2 gl) 
    {
        float luzAmbiente[] = {0.5f, 0.5f, 0.4f, 1}; //cor
        float posicaoLuz[] = {90, 90, 0, 0}; //1.0: pontual, 0: distante
        // define parametros de luz de numero 0 (zero)
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, luzAmbiente, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, posicaoLuz, 0);
    }
    
    //desenvolve a luz difusa
    public void iluminacaoDifusa(GL2 gl) 
    {
        float luzDifusa[] = {0.8f, 0.8f, 0.8f, 1}; //cor
        float posicaoLuz[] = {10, 10, 30, 0}; //1.0: pontual, 0: distante
        //define os parametros de luz de numero 1
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, luzDifusa, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, posicaoLuz, 0);
    }
    
    //liga a luz ambiente (0)
    public void ligaLuz0(GL2 gl) 
    {
        // habilita a definicao da cor do material a partir da cor corrente
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        // habilita o uso da ilumina��o na cena
        gl.glEnable(GL2.GL_LIGHTING);
        // habilita a luz 
        gl.glEnable(GL2.GL_LIGHT0);
        //Especifica o Modelo de tonalizacao a ser utilizado 
        //GL_FLAT -> modelo de tonalizacao flat 
        //GL_SMOOTH -> modelo de tonalizacao GOURAUD (default)        
        gl.glShadeModel(GL2.GL_SMOOTH);
    }
    
    //liga a luz difusa (1)
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

    //desliga a luz ambiente (0)
    public void desligaLuz0(GL2 gl) 
    {
        //desabilita o ponto de luz
        gl.glDisable(GL2.GL_LIGHT0);
    }
    
    //desliga a luz difusa (1)
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

    //método que altera a posição da esfera no eixo x aleatoriamente na parte superior da tela (quando ela não colide com a barra)
    public void geradorEsfera() 
    {
        if ((-50*this.aspect) + (Math.random() * this.xMax) > 0) {
            this.xDirecao = 'd';
        } else {
            this.xDirecao = 'e';
        }
    }

    //altera a direção da esfera no eixo X (d: direita e e: esquerda)
    public void mudaDirecaoX() 
    {
        if (this.xDirecao == 'd') {
            this.xDirecao = 'e';
        } else {
            this.xDirecao = 'd';
        }
    }

    //altera a direção da esfera no eixo Y (b: baixo e c: cima)
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

    //o skate foi "dividido" em três para alterar a direção da esfera caso colidir em diferentes extremidades
   
    //método para colisão da esfera com o skate na extremidade da esquerda
    public boolean colisaoEsquerdaSkate() 
    {
        float limiteEsqSkate = movSkateX - 19.0f;//limite do tamanho do skate na esquerda
        float esquerdaSkate = movSkateX - 17.0f;//um pouco abaixo do limite
        //se o movimento da esfera no eixo x for maior ou igual ao limite do skate 
        //e menor ou igual um pouco abaixo do limite, ela colide na ponta esquerda
        if(movEsferaX >= limiteEsqSkate && movEsferaX <= esquerdaSkate)
        {
            return true;        
        }
        else
            return false;            
    }
    
    //método para colisão da esfera com o meio do skate
    public boolean colisaoMeioSkate() 
    {
        float limiteEsqSkate = movSkateX - 17.0f;//limite do tamanho do skate na esquerda
        float limiteDirSkate = movSkateX + 17.0f;//limite do tamanho do skate na direita
        //se o movimento da esfera no eixo x for maior ou igual ao limite do skate na esq 
        //e maior igual ao da direita, ela colide no meio
        if(movEsferaX >= limiteEsqSkate && movEsferaX <= limiteDirSkate)
        {
            return true;        
        }
        else
            return false;            
    }
 
    //método para colisão da esfera com o skate na extremidade da direita
    public boolean colisaoDireitaSkate() 
    {
        float limiteDirSkate = movSkateX + 19; //limite do tamanho do skate na direita
        float direitaSkate = movSkateX + 17; //um pouco abaixo do limite
        //se o movimento da esfera no eixo x for menor ou igual ao limite do skate 
        //e maior igual um pouco abaixo do limite, ela colide na ponta direita
        if(movEsferaX <= limiteDirSkate && movEsferaX >= direitaSkate) 
        {
            return true;
        }
        else
            return false;
    }
    
    //método para colisão da esfera com a parte inferior da lua
    public boolean colisaoLuaEmbaixo()
    {
        
        float alturaY = 65.000000f; //altura da lua no eixo Y
        float limiteDireita = 175.000000f; //limite da direita da lua
        float limiteEsquerda = 80.000000f; //limite da esquerda da lua
        
        //se o movimento da esfera no eixo X for menor que o limite da direira, maior que o da esquerda
        //e o movimento da esfera no eixo Y for maior ou igual a altura, retorna verdadeiro
        if(movEsferaX < limiteDireita && movEsferaX > limiteEsquerda && movEsferaY >= alturaY)
        {
            return true;     
        }
        
        else
            return false;
        
    }
    
    //método para colisão da esfera com a esquerda da lua 
    public boolean colisaoLuaLado() 
    {
        float alturaLateralX = 70.000000f;//localizacao da altura da lateral da lua no eixo x
        float limiteSuperior = 175.000000f;//limite superior da lateral da lua
        float limiteInferior = 65.000000f;//limite inferior da lateral da lua
        
        //se o movimento da esfera no eixo Y for menor que o limite da superior, maior que o inferior
        //e o movimento da esfera no eixo X for maior ou igual a altura da lateral da lua, retorna verdadeiro
        if(movEsferaY < limiteSuperior && movEsferaY > limiteInferior && movEsferaX >= alturaLateralX)
        {
            return true;
        }
        
        //se não retorna falso
        else
            return false;       
    }
    
    //quando a esfera passa da barra, outra é gerada
    public void reset() 
    {
        this.movEsferaY = this.yMax;
        this.movEsferaX = (float) Math.random();
        geradorEsfera();
    }

    //método para movimentar a esfera
    public void movimentoEsfera() 
    {
        //movimento da esfera no eixo x
        switch (this.xDirecao) 
        {
            //caso a esfera estiver se movimentando para esquerda
            case 'e' -> 
            {   
                //se estiver na fase 2, indo para esquerda, pra cima e colidir embaixo da lua, 
                if(this.fase == 2 && yDirecao == 'c' && colisaoLuaEmbaixo()) 
                {
                    //muda a direcao da esfera no eixo y
                    mudaDirecaoY();
                }
                
                //enquanto o movimento da esfera for maior que o limite da tela na esquerda
                if (this.movEsferaX > -92*this.aspect) 
                {
                    //decrementa o valor da variavel de mov no eixo x com a velocidade, que sera usada no translate
                    this.movEsferaX -= velocidadeJogo;
                }
                
                //se o movimento da esfera atingir a parede da esquerda, 
                if (this.movEsferaX == -92*this.aspect && this.xDirecao == 'e' || this.movEsferaX <= -92*this.aspect && this.xDirecao == 'e') 
                {
                    //muda a direção da esfera no eixo x
                    mudaDirecaoX();
                }
            }//fim do case e
            
            //caso a esfera estiver se movimentando para direita
            case 'd' -> 
            {
                //se estiver na fase 2, indo para direita, para baixo e colidir com a lateral da lua,
                if(this.fase == 2 && yDirecao == 'b' && colisaoLuaLado()) 
                {
                    //muda a direção de x
                    mudaDirecaoX();
                }

                    //se estiver na fase 2, indo para direita, pra cima e colidir embaixo da lua,
                    else if(this.fase == 2 && yDirecao == 'c' && colisaoLuaEmbaixo()) 
                    {
                        //muda a direção de y
                        mudaDirecaoY();
                    }
                
                    //se estiver na fase 2, indo para direita, pra cima e colidir com a lateral da lua,
                    else if(this.fase == 2 && yDirecao == 'c' && colisaoLuaLado()) 
                    {
                        //muda a direção de x
                        mudaDirecaoX();
                    }
                
                //enquanto o movimento for menor que a extremidade da parede da direira e tiver indo pra direita,
                if (this.movEsferaX < 92*this.aspect && this.xDirecao == 'd') 
                {
                    //incrementa o valor da variavel de mov no eixo x com a velocidade, que sera usada no translate
                    this.movEsferaX += velocidadeJogo;
                }
                
                //se o movimento da esfera atingir a extremidade da parede da direita,
                if (this.movEsferaX == 92*this.aspect && this.xDirecao == 'd' || this.movEsferaX >= 92*this.aspect && this.xDirecao == 'd') 
                {
                    //muda de posição
                    this.mudaDirecaoX();
                }
            } //fim do case d
            
        }//fim do switch x
        
        //movimento da esfera no eixo y
        switch (this.yDirecao) 
        {
            //caso a esfera estiver indo para baixo
            case 'b' -> 
            {
                
                //se a esfera estiver na mesma altura que o skate e colidir com a ponta esquerda do skate
                if (this.movEsferaY <= -76*this.aspect && colisaoEsquerdaSkate()) 
                {
                    //muda a direcao de y, de x e soma 20 na pontuacao
                    mudaDirecaoY();
                    this.xDirecao = 'e';
                    pontuacao += 20;
                }
                
                    //se a esfera estiver na mesma altura que o skate e colidir com a ponta direita
                    else if (this.movEsferaY <= -76*this.aspect && colisaoDireitaSkate()) 
                    {
                        //muda a direcao de y, de x e soma 20 na pontuacao
                        mudaDirecaoY();
                        this.xDirecao = 'd';
                        pontuacao += 20;
                    }
                
                    //se a esfera estiver na mesma altura que o skate e colidir com o meio
                    else if (this.movEsferaY <= -76*this.aspect && colisaoMeioSkate()) 
                    {
                        //muda a direcao de y e soma 20 na pontuacao
                        mudaDirecaoY();
                        pontuacao += 20;
                    }
                
                //se a esfera passar a altura do skate, logicamente ela nao colide
                else if (this.movEsferaY <= -90*this.aspect) 
                {
                    //o movimento da esfera reinicia e uma vida eh descontada
                    reset();
                    vidas--;
                }
                
                //enquanto nenhuma colisão ocorre ou a esfera seja reiniciada,
                else 
                {
                    //decrementa o valor da variavel de mov no eixo y com a velocidade, que sera usada no translate
                    movEsferaY -= velocidadeJogo;
                }
            }//fim do case b
            
            //caso indo para cima
            case 'c' -> 
            {                             
                //enquanto nao atingir o teto, 
                if (this.movEsferaY < (92*this.aspect)) 
                { 
                    //incrementa o valor da variavel de mov no eixo y com a velocidade, que sera usada no translate
                    this.movEsferaY += velocidadeJogo;
                }
                //se atingir o teto, muda a direcao
                if (this.movEsferaY >= (92*this.aspect)) 
                { 
                    mudaDirecaoY();
                }
            }//fim do case c 
            
        }//fim do switch y

    }//fim do movimentoEsfera()
    
    //getters e setters
    public float getMovSkateX() 
    {
        return movSkateX;
    }

    public void setMovSkateX(float movSkateX) 
    {
        this.movSkateX = movSkateX;
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
