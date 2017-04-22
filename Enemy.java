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
import java.util.Random;

/**
 *
 * @author CWhite
 */
class HealthBar extends Sprite{
    
    int length;                                                                 //length of enemy health bar
    final double fullHealth = 100;                                              //final length for full health. used for ratios to determine health bar color.
    Color c = Color.GREEN;                                                      //current color of health bar
    
    public void setLen(Enemy en){                                               //used to refresh length as enemy health decrements
        length = en.health;
        if(length/fullHealth < 0.4){
            c = Color.RED;
            return;
        }
        if(length/fullHealth < 0.8)
            c = Color.ORANGE;
    }
 
    public void init(SpriteComponent sc, double x, double y, int len){          //adds to SpriteComponent 
        setX(x);
        setY(y);
        setPicture(drawHealth(len));
        sc.addSprite(this);
    }
    
    public Picture drawHealth(int len){                                         //renders new picture with graphics of health bar.
        Image health = BasicFrame.createImage(100, 5);
        Graphics gHealth = health.getGraphics();
        gHealth.setColor(c);
        if(len > 0)
            gHealth.fillRect(0, 0, len, 5);
        Picture bar = new Picture(health);
        return bar;
    }
    
    public void postMove(){                                                     //updates a new picture with a re-drawing of health bar so that it can be updated after taking damage.
        Picture health = drawHealth(length);
        setPicture(health);
        
    }
}

class Enemy extends Gladiator {

    Gladiator target;                                                           //reference to playable character used for tracking
    double speed = 0.2;                                                         //variable movement speed
    double frameCount;                                                          //used to normalize velocity settings
    HealthBar hb = null;
    Score s;                                                                    //reference to score used to increment it upon death
    WaveController wc;
    int value;
    
    public Enemy(Score s, int health, WaveController wc) throws IOException {  
        this.health = health;
        this.s = s;
        this.wc = wc;
        value = 50;
        Picture enemy = new Picture("enemy_facingdown_drawn.png");
        setPicture(enemy);
    }

    public void init(SpriteComponent sc, Dimension d) {                         //adds to SpriteComponent, sets random position within window
        Random r = new Random();
        setX(r.nextInt(d.width - (int)getWidth()));
        setY(r.nextInt(d.height - (int)getHeight()));
        hb = new HealthBar();                                                   //initializes healthbar over each enemy's head
        hb.init(sc, getX(), getY(), health);
        sc.addSprite(this);
    }
    
    public void postMove() {
        hb.setLen(this);                                                        //updates healthbar length
        if (this.health <= 0) {                                                 //actions upon death: deactivate sprite and increment score by enemy's "value"
            setActive(false);
            s.score += value;
        }
         if (getY() < 500) {                                                    //restriction of movement about "rock" border
            setY(500);
            setVelY(-getVelY());
        }
         if(frameCount%60 == 0 && wc.isActive() == false){                                                //refresh rate of velocity only done once every 60 frames
        if (this.getX() > target.getX()) {
            setVelX(-speed);
        }
        if (this.getX() < target.getX() && wc.isActive() == false) {
            setVelX(speed);
        }
        if (this.getY() > target.getY() && wc.isActive() == false) {
            setVelY(-speed);
        }
        if (this.getY() < target.getY() && wc.isActive() == false) {
            setVelY(speed);
        }
        if(Math.abs(getX() - target.getX()) < 20 && Math.abs(getY() - target.getY()) < 20)
            attack(target, 1);
        }
         frameCount++;
         
         hb.setX(getX());                                                       //sets new positions for healthbar as enemy moves
         hb.setY(getY() - 10);
    }
}