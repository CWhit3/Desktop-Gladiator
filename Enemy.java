/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiator;

import basicgraphics.BasicFrame;
import basicgraphics.CollisionEventType;
import basicgraphics.Sprite;
import basicgraphics.SpriteCollisionEvent;
import basicgraphics.SpriteComponent;
import basicgraphics.images.Picture;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CWhite
 */
class HealthBar extends Sprite{
    int length = 100;
    
    public void setLen(int len){
        length = len;
    }
    public HealthBar(int len) throws IOException{
        setPicture(drawHealth(len));
    }
    public void init(SpriteComponent sc, double x, double y, int len){
        setX(x);
        setY(y);
        sc.addSprite(this);
    }
    
    public Picture drawHealth(int len){
        Image health = BasicFrame.createImage(len, 5);
        Graphics gHealth = health.getGraphics();
        gHealth.setColor(Color.RED);
        gHealth.fillRect(0, 0, len, 5);
        Picture bar = new Picture(health);
        return bar;
    }
    
    public void postMove(){
        Picture health = drawHealth(length);
        setPicture(health);
    }
}

class Enemy extends Gladiator {

    Gladiator target;
    double speed = 0.2;
    double frameCount;
    HealthBar hb = null;

    public Enemy() throws IOException {
        
        this.health = 100;
        Picture enemy = new Picture("enemy_facingdown_drawn.png");
        setPicture(enemy);
    }

    public void init(SpriteComponent sc, Dimension d) {
        Random r = new Random();
        setX(d.width/2);
        setY(200);
        try {
            hb = new HealthBar(health);
        } catch (IOException ex) {
            Logger.getLogger(Enemy.class.getName()).log(Level.SEVERE, null, ex);
        }
        hb.init(sc, getX(), getY() , health);
        sc.addSprite(this);
    }
    
    

    public void processEvent(SpriteCollisionEvent sce){
        if(sce.eventType == CollisionEventType.SPRITE){
            if(sce.sprite2 instanceof Gladiator){
                
            }
        }
    }
    public void postMove() {
        if (this.health < 0) {
            setActive(false);
        }
        hb.setLen(health);
         if (getY() < 400) {
            setY(400);
            setVelY(-getVelY());
        }
         if(frameCount%60 == 0){
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
        if(Math.abs(getX() - target.getX()) < 20 && Math.abs(getY() - target.getY()) < 20)
            attack(target, 1);
        }
         frameCount++;
         hb.setX(getX());
         hb.setY(getY() - 10);
    }
}