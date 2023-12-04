package Pong.cena;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;
import Pong.input.Teclas;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Renderizar {
    
    //utlização de biblioteca para captar o tamanho da tela automaticamente
    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public final static int width = (int)screenSize.getWidth();
    public final static int height = (int)screenSize.getHeight();
    private static GLWindow window = null;
    public static int screenWidth = width; //640;  //1280
    public static int screenHeight = height; //480; //960

    //Cria a janela de rendeziracao do JOGL
    public static void init()
    {
        GLProfile.initSingleton();
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(profile);
        window = GLWindow.create(caps);
        window.setSize(screenWidth, screenHeight);
        window.setResizable(false);

        Jogo jogo = new Jogo();

        window.addGLEventListener(jogo); //adiciona a Jogo a Janela
        //Habilita o teclado : jogo
        window.addKeyListener(new Teclas(jogo));

        //window.requestFocus();
        FPSAnimator animator = new FPSAnimator(window, 60);
        animator.start(); //inicia o loop de animacao

        //encerrar a aplicacao adequadamente
        window.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowDestroyNotify(WindowEvent e)
            {
                animator.stop();
                System.exit(0);
            }
        });

        window.setFullscreen(true);
        window.setVisible(true);
    }
  
    public static void main(String[] args) {
        init();
    }
}
