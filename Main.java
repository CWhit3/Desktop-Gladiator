/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiator;

/**
 *
 * @author CWhite
 */
import basicgraphics.*;
import gladiator.Enemy;
import gladiator.Gladiator;
import basicgraphics.images.Picture;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Main {

    Dimension d = new Dimension(1000, 1000);                                    //Dimensions of window
    SpriteComponent sc = new SpriteComponent();                                 //SpriteComponent
    String swordStatus = "sheathed";                                            //String used to find frames regarding if sword is drawn
    String direction = "down";                                                  //String used to find frames regarding direction gladiator is facing
    boolean drawn = false;                                                      //Boolean for whether sword is drawn
    final ArrayList<Enemy> enemies = new ArrayList<>();                         //Enemy ArrayList

    public void initializeEnemies(Gladiator champion, int round, Score s, WaveController wc) throws IOException {
        for (int i = 0; i < round; i++) {
            enemies.add(new Enemy(s, 100, wc));
            enemies.get(i).init(sc, d);
            enemies.get(i).target = champion;
        }
    }

    public void run() throws IOException {
        final BasicFrame bf = new BasicFrame("Gladiators: Champion of the Coliseum");

        Arena arena = new Arena();                                              //Sprite that displays arena drawing
        arena.init(sc);
        WaveController wc = new WaveController(d);
        wc.init(sc);
        final Gladiator champion = new Gladiator(wc);                             //Playable character
        champion.init(sc, d);
        UI ui = new UI();                                                       //Class containing the UI components, which include: HealthMeter, ScoreBanner, and Score sprites
        ui.init(sc, d, champion);
        initializeEnemies(champion, 1, ui.returnScore(), wc);                       //call to initialize enemies. This will increment number of enemies after successive waves
        
        bf.addKeyListener(new KeyAdapter() {                                    //KeyListener to handle user input
            @Override
            public void keyPressed(KeyEvent ke) {
                switch (ke.getKeyCode()) {
                    case KeyEvent.VK_Q:                                         //draws sword and changes sprite picture
                        drawn = !drawn;
                        try {
                            champion.drawWeapon(direction, drawn);
                        } catch (IOException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (swordStatus.equals("drawn") == true) {
                            swordStatus = "sheathed";
                        } else {
                            swordStatus = "drawn";
                        }
                        break;
                    case KeyEvent.VK_W:                                         //moves upwards and sets sprite picture to gladiator facing up
                        try {
                            direction = "up";
                            champion.turn(direction, swordStatus);
                            champion.setVelY(-4);
                        } catch (IOException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case KeyEvent.VK_S: {                                       //moves downwards and sets sprite picture to gladiator facing down
                        try {
                            direction = "down";
                            champion.turn(direction, swordStatus);
                        } catch (IOException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        champion.setVelY(4);
                    }
                    break;
                    case KeyEvent.VK_A:                                         //moves left and sets sprite picture to gladiator facing left
                        direction = "left";
                        try {
                            champion.turn(direction, swordStatus);
                            champion.setVelX(-4);
                        } catch (IOException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            break;
                        }
                        break;
                    case KeyEvent.VK_D:                                         //moves right and sets sprite picture to gladiator facing right
                        direction = "right";
                        try {
                            champion.turn(direction, swordStatus);
                            champion.setVelX(4);
                        } catch (IOException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case KeyEvent.VK_SPACE:                                     //if sword is drawn, attack enemies within range
                        if (drawn == true) {
                            for (Enemy e : enemies) {
                                champion.attackClose(enemies, 25);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        sc.setPreferredSize(d);
        bf.add("center", sc, 0, 0, 1, 1);
        sc.start(0, 10);
        bf.show();
    }

    public static void main(String[] args) {
        Main main = new Main();
        try {
            main.run();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
