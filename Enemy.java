/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiator;

import basicgraphics.BasicFrame;
import basicgraphics.Sprite;
import basicgraphics.SpriteComponent;
import basicgraphics.images.Picture;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CWhite
 */
class HealthBar extends Sprite {
    
    Enemy en;
    double length;                                                              //length of enemy health bar
    double fullHealth = Main.enHealth;                                          //final length for full health. used for ratios to determine health bar color.
    Color c = Color.GREEN;                                                      //current color of health bar

    public void setLen(Enemy en) {                                               //used to refresh length as enemy health decrements
        length = en.health;
        if(length > 0)
        System.out.println(length/fullHealth);
        if (length / fullHealth < 0.4) {
            c = Color.RED;
            return;
        }
        if (length / fullHealth < 0.75) {
            c = Color.ORANGE;
        }
    }
    public HealthBar(Enemy enemy){
        en = enemy;
    }
    public void init(SpriteComponent sc, double x, double y, int len) {         //adds to SpriteComponent 
        setX(x);
        setY(y);
        setPicture(drawHealth(len));
        sc.addSprite(this);
    }

    public Picture drawHealth(double len) {                                     //renders new picture with graphics of health bar.
        Image health = BasicFrame.createImage(100, 5);
        Graphics gHealth = health.getGraphics();
        gHealth.setColor(c);
        if (len > 0) {
            gHealth.fillRect(0, 0, (int) len, 5);
        }
        Picture bar = new Picture(health);
        return bar;
    }

    public void postMove() {                                                     //updates a new picture with a re-drawing of health bar so that it can be updated after taking damage.
        setLen(en);
        Picture health = drawHealth(length);
        setPicture(health);

    }
}

class Enemy extends Gladiator {

    Gladiator target;                                                           //reference to playable character used for tracking
    private double speed = 0.2;                                                  //variable movement speed
    private int value;
    private int dmg = 2;
    private double frameCount;                                                          //used to normalize velocity settings
    private HealthBar hb = null;
    private Score s;                                                                    //reference to score used to increment it upon death
    private static boolean enemyPresent = true;

    public Enemy() throws IOException {

    }

    public Enemy(Score s, int health, int val) throws IOException {
        this.health = health;
        this.s = s;
        value = val;
        Picture enemy = new Picture("enemy_facingdown_drawn.png");
        setPicture(enemy);
    }

    public void init(SpriteComponent sc, Dimension d) {                         //adds to SpriteComponent, sets random position within window
        Random r = new Random();
        setX(r.nextInt(d.width - (int) getWidth()));
        setY(r.nextInt(d.height - (int) getHeight()));
        hb = new HealthBar(this);                                                   //initializes healthbar over each enemy's head
        hb.init(sc, getX(), getY(), health);
        sc.addSprite(this);
    }

    public static boolean returnEnPresent() {
        return enemyPresent;
    }

    public static void setEnPresent(boolean b) {
        enemyPresent = b;
    }

    public void postMove() {                    
        if (this.health <= 0) {                                                 //actions upon death: deactivate sprite and increment score by enemy's "value"
            setActive(false);
            s.score += value;
            enemyPresent = false;
            Main.enValue += 25;
            if (dmg < 40) {
                dmg *= 2;
            }
            if(speed < 5){
                speed *= 1.5;
            }
            Main.enHealth *= 1.5;
        }

        if (getY() < 500) {                                                    //restriction of movement about "rock" border
            setY(500);
            setVelY(-getVelY());
        }
        if (getVelY() < 0) {
            try {
                setPicture(new Picture("enemy_facingup_drawn.png"));
            } catch (IOException ex) {
                Logger.getLogger(Enemy.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                setPicture(new Picture("enemy_facingdown_drawn.png"));
            } catch (IOException ex) {
                Logger.getLogger(Enemy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (frameCount % 350 == 0) {                      //refresh rate of velocity only done once every 350 frames
            if (this.getX() > target.getX()) {
                setVelX(-speed);
            }
            if (this.getX() < target.getX()) {
                setVelX(speed);
            }
            if (this.getY() > target.getY()) {
                setVelY(-speed);
            }
            if (this.getY() < target.getY()) {
                setVelY(speed);
            }
        }

        boolean closeX = Math.abs(getX() - target.getX()) < 50;
        boolean closeY = Math.abs(getY() - target.getY()) < 50;
        boolean facingDown = getVelY() > 0;
        boolean targetBelow = getY() < target.getY();

        if (closeX && closeY) {
            if (facingDown == targetBelow) {
                attack(target, dmg);
            }
        }

        frameCount++;
        hb.setX(getX());                                                       //sets new positions for healthbar as enemy moves
        hb.setY(getY() - 10);
    }
}
