/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiator;

import java.io.IOException;

/**
 *
 * @author Christian White
 */
public interface Fighter {
    
//    @param: args f: the fighter taking damage
//                dmg: the amount of damage taken
//    called to cause damage to enemy fighter.
    public void attack(Gladiator g, double dmg);
   
//    @param: args f: the fighter to target
//    called to target an enemy fighter, causing all movement to circumscribe fighter
    public void target(Enemy f);
    
//    @param args direction: the direction the fighter is facing
//                drawn: is the fighter's weapon drawn?
//    sets new picture for fighter with weapon drawn
    public void drawWeapon(String direction, boolean drawn) throws IOException;
    
//    @param args dir: the direction the fighter is facing
//                drawn: is the fighter's weapon drawn?
//      sets new picture for fighter after changing directions            
    public void turn(String dir, String drawn) throws IOException;

}
