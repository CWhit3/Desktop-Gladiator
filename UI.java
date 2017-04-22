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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CWhite
 */
public class UI {
    
    HealthMeter hm;
    ScoreBanner sb;
    Score s;
    
    public void init(SpriteComponent sc, Dimension d, Gladiator champion) throws IOException{
    final HealthMeter meter = new HealthMeter(champion);
        hm = meter;
        meter.init(sc, d);
        
    final ScoreBanner score = new ScoreBanner();
        sb = score;
        score.init(sc, d);
        
    final Score num = new Score();
        s = num;
        num.init(sc, score);
    }
    
    public HealthMeter returnHM(){
        return hm;
    }
    
    public ScoreBanner returnSB(){
        return sb;
    }
    
    public Score returnScore(){
        return s;
    }
    
}

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
        setDrawingPriority(0);
    }
}

class Score extends Sprite{
    Long score = new Long(0);
    String scoreStr = score.toString();
    Font f = new Font("Serif", 0, 32);
    
    public Picture writeScore(){
        Image score = BasicFrame.createImage(100, 50);
        Graphics scoreG = score.getGraphics();
        scoreG.setColor(Color.BLACK);
        scoreG.setFont(f);
        scoreG.drawString(scoreStr, 0, 30);
        return new Picture(score);
    }
    public void init(SpriteComponent sc, ScoreBanner sb){
        setPicture(writeScore());
        setX(sb.getX() + 100);
        setY(sb.getY() + 5);
        sc.addSprite(this);
    }
    
    public void postMove(){
        scoreStr = score.toString();
        setPicture(writeScore());  
    }
}