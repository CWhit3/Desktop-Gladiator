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

/**
 *
 * @author CWhite
 */
public class WaveController extends Sprite{
    Integer round = new Integer(1);
    String roundNum = round.toString();
    Dimension d;
    int duration = 0;
    
    public WaveController(Dimension d){
        this.d = d;
    }
    
    public String calcRound(int round){
        StringBuilder sb = new StringBuilder();
        sb.append("Round ");
        sb.append(roundNum);
        return sb.toString();
    }
    
    public Picture drawRound(){
        Image roundIm = BasicFrame.createImage(400, 80);
        Graphics roundGraphics = roundIm.getGraphics();
        roundGraphics.setFont(new Font("SansSerif", 2, 100));
        roundGraphics.setColor(Color.darkGray);
        roundGraphics.drawString(calcRound(round), 0, 75);
        return new Picture(roundIm);
    }
    
    public void init(SpriteComponent sc){
        setPicture(drawRound());
        setX(d.width/2 - getWidth()/2);
        setY(d.height/2 - getHeight()/2);
        sc.addSprite(this);
    }
    
    public void postMove(){
        if(duration > 100){
            setActive(false);
        }
        duration++;
    }
    
}
