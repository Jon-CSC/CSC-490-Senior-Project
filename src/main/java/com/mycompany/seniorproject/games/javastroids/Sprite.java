/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.seniorproject.games.javastroids;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

/**
 * Contains code for the main Sprite class from which all other sprites inherit from.
 * Designates sprite attributes such as position, rotation, and velocity.
 * 
 * @author Justin Karp
 * @version 1.0
 * @since 12/8/2021
 */
public class Sprite {

    private double posX;
    private double posY;
    private double velX;
    private double velY;
    private double velMax;
    private double angle;
    private double angleRate;
    private double width;
    private double height;
    protected boolean noCollideOn;

    /**
     * Instantiates a generic sprite (renders as rectangle) with height and width of 50.
     */
    public Sprite() {
        this(50, 50);
    }
    
    /**
     * Instantiates a generic sprite (renders as rectangle) with specified height
     * and width.
     * 
     * @param w Width as a double value.
     * @param h Height as a double value.
     */
    public Sprite(double w, double h) {
        posX = 0;
        posY = 0;
        velX = 0;
        velY = 0;
        velMax = 1000;
        noCollideOn = false;
        
        // Size of effective collision box/boundary
        width = w;
        height = h;
    }

    /**
     * Method returns the x-position of the sprite.
     * 
     * @return X-pos as double value.
     */
    public double getPosX() {
        return posX;
    }

    /**
     * Method returns the y-position of the sprite.
     * 
     * @return Y-pos as double value.
     */
    public double getPosY() {
        return posY;
    }
    
    /**
     * Method to return the position relative to the center of a rectangle, since
     * position with this shape is measured from the top-right corner normally.
     * 
     * @return Double array containing x-pos at [0] and y-pos at [1];
     */
    public double[] getCenterPosition() {
        double[] centerPos = {posX + (width / 2), posY + (height / 2)};
        return centerPos;
    }

    /**
     * Method to return x-velocity.
     * 
     * @return X-velocity as double value.
     */
    public double getVelX() {
        return velX;
    }

    /**
     * Method to return y-velocity.
     * 
     * @return Y-velocity as double value.
     */
    public double getVelY() {
        return velY;
    }

    /**
     * Method to return current angle in degrees.
     * 
     * @return Angle (in degrees) as double value.
     */
    public double getAngle() {
        return angle;
    }

    /**
     * Method to return width of sprite.
     * 
     * @return Width as double value.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Method to return height of sprite.
     * 
     * @return Height as double value.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Method to disable all collision detection for this sprite object.
     * 
     * @param nc Boolean value where TRUE means collisions are off, and FALSE means
     * collisions are on.
     */
    public void setNoCollideOn(boolean nc) {
        noCollideOn = nc;
    }
    
    /**
     * Method to set the width of this sprite.
     * 
     * @param w Width as a double value.
     */
    public void setWidth(double w) {
        width = w;
    }

    /**
     * Method to set the height of this sprite.
     * 
     * @param h Height as a double value.
     */
    public void setHeight(double h) {
        height = h;
    }
    
    /**
     * Method to directly set the current angle in degrees of this sprite.
     * 
     * @param a Angle (in degrees) as a double value.
     */
    public void setAngle(double a) {
        angle = a;
    }
    
    /**
     * Method to set the absolute position of this sprite.
     * 
     * @param x X-pos as a double value.
     * @param y Y-pos as a double value.
     */
    public void setPos(double x, double y) {
        posX = x;
        posY = y;
    }

    /**
     * Method to set a static velocity for this sprite.
     * 
     * @param x X-velocity as a double value.
     * @param y Y-velocity as a double value.
     */
    public void setVel(double x, double y) {
        velX = x;
        velY = y;
    }

    /**
     * Method to set the rate of velocity increase/decrease (effectively, setting 
     * the acceleration) of this sprite.
     * 
     * @param x Rate of change of x-velocity as a double value.
     * @param y Rate of change of y-velocity as a double value.
     */
    public void addVel(double x, double y) {
        velX += x;
        velY += y;
        if (Math.abs(velX) > velMax) {
            velX -= x;
        }
        if (Math.abs(velY) > velMax) {
            velY -= y;
        }
    }

    /**
     * Method to set an upper-bound on the total velocity of this sprite.
     * 
     * @param max Max velocity as a double value.
     */
    public void setMaxVel(double max) {
        velMax = max;
    }

    /**
     * Method to set the static/constant rate of rotation of this sprite.
     * 
     * @param r Rate of rotation (in degrees) as a double value
     */
    public void setRotate(double r) { // rotate by this many degrees/sec
        angleRate = r;
    }

    /**
     * Method to set the angular acceleration of this sprite.
     * 
     * @param r Angular acceleration (in degrees) as a double value.
     */
    public void addRotate(double r) { // accelerate rotation by this many degrees/sec*sec
        angleRate += r;
    }

    /**
     * Method to input and translate a given total constant velocity into X and Y components 
     * based on the current angle for this sprite.
     * 
     * @param v Velocity as a double value.
     */
    public void setVelRelativeToAngle(double v) {
        double radianAngle = Math.toRadians(angle);
        velX = -v * Math.sin(radianAngle);
        velY = v * Math.cos(radianAngle);
    }
    
    /**
     * Method to input and translate a given rate of acceleration into X and Y components 
     * based on the current angle for this sprite.
     * 
     * @param v Acceleration rate as a double value.
     */
    public void addVelRelativeToAngle(double v) {
        double radianAngle = Math.toRadians(angle);
        velX -= v * Math.sin(radianAngle);
        velY += v * Math.cos(radianAngle);
    }
    
    /**
     * Returns the total velocity based on the X and Y velocity components of this sprite.
     * 
     * @return Total velocity as a double value.
     */
    public double getVelRelativeToAngle() {
        //double radianAngle = Math.toRadians(angle);
        double result = (velX*velX)*Math.signum(velX) + (velY*velY)*Math.signum(velY);
        return -result;//(velX / Math.cos(radianAngle)); //Math.min((velX / Math.cos(radianAngle)), (velY / Math.sin(radianAngle)));
    }

    /**
     * Applies an amount of drag that slowly decreases the velocity of this sprite
     * on every sprite update.
     * 
     * @param d Drag coefficient as a double value.
     */
    public void applyDrag(double d) {
        if (d > 1) {
            d = 1;
        } else if (d < 0) {
            d = 0;
        }
        velX -= velX * d;
        velY -= velY * d;
    }

    /**
     * Checks for and corrects sprite objects that reach beyond the bounds of
     * the current game window. The sprite's position will loop directly back to the other
     * side of the game window while maintaining its previous angle and velocity.
     * 
     * @param gc GraphicsContext object to check boundaries of.
     */
    public void checkAndCorrectOutOfBoundsPosition(GraphicsContext gc) {
        double centerX = posX + (width / 2);
        double centerY = posY + (height / 2);
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();

        if (centerX > canvasWidth) {
            posX -= canvasWidth;
        } else if (centerX < 0) {
            posX += canvasWidth;
        }
        if (centerY > canvasHeight) {
            posY -= canvasHeight;
        } else if (centerY < 0) {
            posY += canvasHeight;
        }
    }

    /**
     * Provides a collision box based on this sprite's width, height, and position.
     * 
     * @return Rectangle2D with position, width and height corresponding to this sprite.
     */
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(posX, posY, width, height); // originally width, height
    }

    /**
     * Updates this sprite's position and angle based on sprite's velocity and 
     * angular rates of change.
     */
    public void update() {
        posX += velX;
        posY += velY;
        angle += angleRate;
    }

    /**
     * Renders this sprite to the specified GraphicsContext element.
     * 
     * @param gc GraphicsContext object to render on.
     */
    public void render(GraphicsContext gc) {
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.save();
        rotateAboutPivot(gc, angle, posX + width / 2, posY + height / 2);
        renderGivenShape(gc); // sprite comes up as rectangle shape size of bounding box
        gc.restore();
    }

    /**
     * Checks whether the collision boxes of two given sprites intersects, effectively
     * checking if two objects have collided.
     * 
     * @param otherSprite Sprite object to check intersection with.
     * @return Boolean value of TRUE or FALSE if two sprites have collided.
     */
    public boolean intersects(Sprite otherSprite) {
        if (noCollideOn) {
            return false;
        } else {
            return otherSprite.getBoundingBox().intersects(this.getBoundingBox());
        }
    }

    /**
     * Produces a String detailing the current position and velocity components
     * of this sprite.
     * 
     * @return String in single line.
     */
    @Override
    public String toString() {
        return "Sprite{" + "posX=" + posX + ", posY=" + posY + ", velX=" + velX + ", velY=" + velY + '}';
    }

    void rotateAboutPivot(GraphicsContext gc, double angle, double pivotPosX, double pivotPosY) {
        Rotate r = new Rotate(angle, pivotPosX, pivotPosY);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }

    private void renderGivenShape(GraphicsContext gc) {
        // Draw whatever shape or image you desire, young padawan
        gc.strokeRect(posX, posY, width, height);
    }
}
