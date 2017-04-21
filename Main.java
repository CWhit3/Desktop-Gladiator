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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

class Arena extends Sprite {

    public Arena() throws IOException {
        Picture arena = new Picture("arena.png");
        setPicture(arena);
    }

    public void init(SpriteComponent sc) {
        sc.addSprite(this);
    }
}

class HealthMeter extends Sprite {

    Gladiator g;

    public HealthMeter(Gladiator g) throws IOException {
        Picture meter = new Picture("health_meter.png");
        setPicture(meter);
        this.g = g;
    }

    public void init(SpriteComponent sc, Dimension d) {
        setX(0);
        setY(d.height - getHeight());
        sc.addSprite(this);
        setDrawingPriority(1);
    }

    public void updateHealth(Gladiator g) throws IOException {
        if(g.health < 50){
            Picture hit10 = new Picture("health_meter_10hit.png");
            setPicture(hit10);
            return;
        }
        if(g.health < 100){
            Picture hit9 = new Picture("health_meter_9hit.png");
            setPicture(hit9);
            return;
                    
        }
        if(g.health < 150){
            Picture hit8 = new Picture("health_meter_8hit.png");
            setPicture(hit8);
            return;
        }
        if(g.health < 200){
            Picture hit7 = new Picture("health_meter_7hit.png");
            setPicture(hit7);
            return;
        }
        if(g.health < 250){
            Picture hit6 = new Picture("health_meter_6hit.png");
            setPicture(hit6);
            return;
        }
        if(g.health < 300){
            Picture hit5 = new Picture("health_meter_5hit.png");
            setPicture(hit5);
            return;
        }
        
        if(g.health < 350){
            Picture hit4 = new Picture("health_meter_4hit.png");
            setPicture(hit4);
            return;
        }
        
        if(g.health < 400){
            Picture hit3 = new Picture("health_meter_3hit.png");
            setPicture(hit3);
            return;
        }
        
        if (g.health < 450) {
            Picture hitTwice = new Picture("health_meter_2hit.png");
            setPicture(hitTwice);
            return;
        }
                
        if (g.health < 500) {
            Picture hitOnce = new Picture("health_meter_1hit.png");
            setPicture(hitOnce);
            return;
        }   
    }

    public void preMove() {
        try {
            updateHealth(g);
        } catch (IOException ex) {
            Logger.getLogger(HealthMeter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class ScoreBanner extends Sprite {

    public ScoreBanner() throws IOException {
        Picture score = new Picture("score_banner.png");
        setPicture(score);
    }

    public void init(SpriteComponent sc, Dimension d) {
        setX(d.width - getWidth());
        setY(0);
        sc.addSprite(this);
        setDrawingPriority(1);
    }
}

public class Main {

    Dimension d = new Dimension(1000, 1000);
    SpriteComponent sc = new SpriteComponent();
    String swordStatus = "sheathed";
    String direction = "down";
    boolean drawn = false;
     final ArrayList<Enemy> enemies = new ArrayList<>(); 
    
     
     public void initializeEnemies(Gladiator champion, int round) throws IOException{        
        for(int i = 0; i < round; i++){
        enemies.add(new Enemy());
        enemies.get(i).init(sc, d);
        enemies.get(i).target = champion;
        }
    }

    public void run() throws IOException {
        final BasicFrame bf = new BasicFrame("Gladiators: Champion of the Coliseum");

        Arena arena = new Arena();
        arena.init(sc);

        final Gladiator champion = new Gladiator();
        champion.init(sc, d);

        final HealthMeter meter = new HealthMeter(champion);
        meter.init(sc, d);
        final ScoreBanner score = new ScoreBanner();
        score.init(sc, d);

        initializeEnemies(champion, 3);
 
        meter.updateHealth(champion);
        bf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                switch (ke.getKeyCode()) {
                    case KeyEvent.VK_Q:
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
                    case KeyEvent.VK_W:
                        try {
                            direction = "up";
                            champion.turn(direction, swordStatus);
                            champion.setVelY(-4);
                        } catch (IOException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case KeyEvent.VK_S:
                         {
                            try {
                                direction = "down";
                                champion.turn(direction, swordStatus);
                            } catch (IOException ex) {
                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            champion.setVelY(4);
                        }
                        break;
                    case KeyEvent.VK_A:
                        direction = "left";
                        try {
                            champion.turn(direction, swordStatus);
                            champion.setVelX(-4);
                        } catch (IOException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            break;
                        }
                        break;
                    case KeyEvent.VK_D:
                        direction = "right";
                        try {
                            champion.turn(direction, swordStatus);
                            champion.setVelX(4);
                        } catch (IOException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case KeyEvent.VK_SPACE:
                        if(drawn == true){
                            for(Enemy e: enemies){
                                champion.attackClose(enemies, 100);
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
