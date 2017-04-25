/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiator;

import basicgraphics.Sprite;
import basicgraphics.SpriteComponent;
import basicgraphics.images.Picture;
import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Christian White
 */
public class Gladiator extends Sprite implements Fighter{

    public int health;                                                          //variable for playable character health
    SpriteComponent sc;
    Dimension d;
    int range;
    
    public Gladiator() throws IOException{
        Picture standing = new Picture("gladiator_standing.png");
        setPicture(standing);
        this.health = 500;
        this.range = 75;
    }
    
        public void init(SpriteComponent sc, Dimension d) {                     //adds to SpriteComponent in middle of window
        this.sc = sc;
        this.d = d;
        setX(d.getWidth() / 2);
        setY(d.getHeight() / 2 + 300);
        sc.addSprite(this);
    }
    
    public void attack(Gladiator g, double dmg) {                               //used by both Enemy and Gladiator to decrement the other's health
        g.health -= dmg;
    }

    @Override
    public void drawWeapon(String dir, boolean drawn) throws IOException {      //changes picture based on direction and sword's drawn status
        Picture draw;
        if (drawn == true) {
            draw = new Picture("gladiator_facing" + dir + "_drawn" + ".png");
            setPicture(draw);
        } else {
            draw = new Picture("gladiator_facing" + dir + "_sheathed" + ".png");
            setPicture(draw);
        }
    }

    public void turn(String dir, String drawn) throws IOException {             //change picture upon change in direction
        Picture turned = new Picture("gladiator_facing" + dir + "_" + drawn + ".png");
        setPicture(turned);
    }
    
    public void checkBounds(){                                                  //custom function to keep player inbounds. 
                                                                                //the "SpriteCollisionEvent" function was not seamless due to sprite width.
        if(getX() < -getWidth()){
            setX(d.width);
        }
        if(getX() > d.width){
            setX(-getWidth());
        }
        if(getY() > d.height - getHeight()){
            setY(d.height - getHeight());
        }
    }
    
    public void postMove() {
   
        setVelX(getVelX() * 0.9);                                               //buffering of movement in order to make walking gradually stop
        setVelY(getVelY() * 0.9);
        checkBounds();
        System.out.println(health);

        if (getY() < 400) {                                                     //keeps player below "rock" line
            setY(400);
            setVelY(-getVelY());
        }

        if (health < 0) {                                                       //endgame
            setActive(false);
            JOptionPane.showMessageDialog(sc, "You have died.");
            System.exit(0);
        }
    }
    
    public double findDist(Enemy e){                                            //calculates distance from a given enemy
        double diffX = Math.abs(getX() - e.getX());
        double diffY = Math.abs(getY() - e.getY());
        double dist = Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));
        return dist;
    }
    
    public Enemy findClosest(ArrayList<Enemy> enemies){                         //uses findDist to decide closest enemy
        Enemy target = enemies.get(0);
        for(Enemy en: enemies){
            if(findDist(en) < findDist(target))
                target = en;
        }
        return target;
    }
    
    public void attackClose(ArrayList<Enemy> enemies, int dmg){                 //attacks returned enemy from findClosest if it is within range
        
        if(findDist(findClosest(enemies)) < range){
            attack(findClosest(enemies), dmg);
        }
    }    
}