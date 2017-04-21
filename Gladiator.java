/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiator;

import basicgraphics.CollisionEventType;
import basicgraphics.Sprite;
import basicgraphics.SpriteCollisionEvent;
import basicgraphics.SpriteComponent;
import basicgraphics.images.Picture;
import gladiator.Main;
import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Christian White
 */
public class Gladiator extends Sprite implements Fighter{

    public int health;
    Enemy target;
    SpriteComponent sc;
    
    public Gladiator() throws IOException {
        Picture standing = new Picture("gladiator_standing.png");
        setPicture(standing);
        this.health = 500;
    }
    
        public void init(SpriteComponent sc, Dimension d) {
        this.sc = sc;
        setX(d.getWidth() / 2);
        setY(d.getHeight() / 2 + 300);
        sc.addSprite(this);
        setDrawingPriority(1);
    }
    
    public void attack(Gladiator g, double dmg) {
        g.health -= dmg;
    }

    public void target(Enemy f) {
        target = f;
    }

    @Override
    public void drawWeapon(String dir, boolean drawn) throws IOException {
        Picture draw;
        if (drawn == true) {
            draw = new Picture("gladiator_facing" + dir + "_drawn" + ".png");
            setPicture(draw);
        } else {
            draw = new Picture("gladiator_facing" + dir + "_sheathed" + ".png");
            setPicture(draw);
        }
    }

    public void turn(String dir, String drawn) throws IOException {
        Picture turned = new Picture("gladiator_facing" + dir + "_" + drawn + ".png");
        setPicture(turned);
    }
    
    public void processEvent(SpriteCollisionEvent sce){
        if(sce.eventType == CollisionEventType.SPRITE){
            if (sce.sprite2 instanceof Enemy){
                health -= 1;
            }
        }
    }
    
    public void postMove() {
        setVelX(getVelX() * 0.9);
        setVelY(getVelY() * 0.9);

        if (getY() < 400) {
            setY(400);
            setVelY(-getVelY());
        }

        if (health < 0) {
            setActive(false);
            JOptionPane.showMessageDialog(sc, "You have died.");
            System.exit(0);
        }
    }
    
    public double findDist(Enemy e){
        double diffX = Math.abs(getX() - e.getX());
        double diffY = Math.abs(getY() - e.getY());
        double dist = Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));
        return dist;
    }
    
    public Enemy findClosest(ArrayList<Enemy> enemies){
        Enemy target = enemies.get(0);
        for(Enemy en: enemies){
            if(findDist(en) < findDist(target))
                target = en;
        }
        return target;
    }
    
    public void attackClose(ArrayList<Enemy> enemies, int dmg){
        
        if(findDist(findClosest(enemies)) < 75){
            attack(findClosest(enemies), dmg);
        }
    }    
}