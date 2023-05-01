/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.farmingdale.chessgame;

/**
 *
 * @author nicolechan
 */
public class Location {
    protected int x;
    protected int y;
    
    public Location(int x, int y){
        this.x = x;
        this.y = y;
        
    }
    
    public int getX(){return x;}
    public void setX(int x) {this.x = x;}
    
    public int getY(){return y;}
    public void setY(int y) {this.y = y;}
    
    public double distance (Location location1, Location location2){
        double toBeSquare = Math.pow(location1.x-location2.x, 2) +
                Math.pow(location1.y-location2.y, 2);
        return Math.sqrt(toBeSquare);
        
    }
    
    public boolean inBounds (Location location){
        if (location.x >= 0 && location.x <= 7 && location.y >= 0 && location.y <=7){
            return true;
            
        }
        return false;
    }
    
}
