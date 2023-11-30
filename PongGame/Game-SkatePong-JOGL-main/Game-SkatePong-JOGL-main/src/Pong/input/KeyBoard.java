package Pong.input;

import Pong.cena.Cena;
import static Pong.cena.Cena.screenSize;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

public class KeyBoard implements KeyListener{
    private Cena cena;
    
    private final int setaEsquerda = 149;
    private final int setaDireita = 151;
    private final int teclaJ = 75;
    private final int teclaj = 74;
    private final int teclaP = 80;
    private final int teclaS = 83;
    private final int teclaF = 70;
    private final int teclaA = 65;
    private final int teclaD = 68;
    
    
    //dados para renderização dos objetos
    public double width = screenSize.getWidth();
    public double height = screenSize.getHeight();
    public float aspect = (float)(this.width/this.height);
    
    public KeyBoard(Cena cena){
        this.cena = cena;
    }
    
    @Override
    public void keyPressed(KeyEvent e)
    {        
//        System.out.println("Key pressed: " + e.getKeyCode());
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            System.exit(0);
        
        if(screenSize.getWidth() == 1920 && screenSize.getHeight() == 1080)
        {
            switch (e.getKeyCode()) 
            {     
                
            case setaEsquerda, teclaA:
                if(cena.getMovSkateX() > (float)(this.width/14)*-1)
                {                
                    cena.setMovSkateX(cena.getMovSkateX() - 15.0f);                    
                    cena.setMovCenario(cena.getMovCenario() + 1); 
                }            
            break;
                
            case setaDireita, teclaD:
                if(cena.getMovSkateX() < (float)(this.width/14))
                {
                    cena.setMovSkateX(cena.getMovSkateX() + 15.0f);
                    cena.setMovCenario(cena.getMovCenario() - 1);
                }
            break;
            }
        }
        
        else
        {
            switch (e.getKeyCode()) 
            {     
                
            case setaEsquerda, teclaA:
                if(cena.getMovSkateX() > (float)(this.width/10)*-1)
                {                
                    cena.setMovSkateX(cena.getMovSkateX() - 15.0f);                    
                    cena.setMovCenario(cena.getMovCenario() + 1); 
                }            
            break;
                
            case setaDireita, teclaD:
                if(cena.getMovSkateX() < (float)(this.width/10))
                {
                    cena.setMovSkateX(cena.getMovSkateX() + 15.0f);
                    cena.setMovCenario(cena.getMovCenario() - 1);
                }
            break;
            }
        }
            switch (e.getKeyCode()) 
            {
                
            
            case teclaj, teclaJ:
                if(cena.getFase() == 0)
                    cena.setFase(1);
            
            case teclaP:
                if(cena.isPausar() == false)
                    cena.setPausar(true);
                else
                    cena.setPausar(false);
            break;
            
            case teclaS:
                cena.setFase(0);
            break; 
            
            case teclaF:
                if(cena.getFase() == 3 || cena.getFase() == 0)
                    System.exit(0);           
            break;
            }    
    }   
    

    @Override
    public void keyReleased(KeyEvent e) { }

}
