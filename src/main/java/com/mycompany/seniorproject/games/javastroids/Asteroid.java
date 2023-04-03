package com.mycompany.seniorproject.games.javastroids;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Contains code and method for the Asteroid class, which is a subclass of the
 * Sprite class.
 *
 * @author Justin Karp
 * @version 1.0
 * @since 12/8/2021
 */
public class Asteroid extends Sprite {

    private int pointValue;
    private double[] x;
    private double[] y;
    private int n;
    private int scaleFactor = 5;
    private int asteroidSize;

    /**
     * Instantiation of an Asteroid object of a random size.
     */
    public Asteroid() {
        // Range = 1->8, therefore 3/4 chance of large asteroid, 1/4 for others
        this((int) (Math.round(Math.random() * 7) + 1)); 
    }

    /**
     * Instantiation of an Asteroid object of a specified size of 1, 2, or 3.
     * 
     * @param size Integer of range from 1-3, other values use default size of 3.
     */
    public Asteroid(int size) { // 1 = small, 2 = medium, 3 = large
        super();
        switch (size) {
            case 1: // small
                this.setWidth(7 * scaleFactor);
                this.setHeight(6 * scaleFactor);
                x = new double[]{0, 1, 3, 6, 6, 7, 6.5, 6, 5, 3, 1};
                y = new double[]{2, 1, 0, 0.5, 2, 3, 5.2, 5, 6, 5, 5};
                n = 11;
                pointValue = 100;
                asteroidSize = 1;
                break;

            case 2: // medium
                this.setWidth(12 * scaleFactor);
                this.setHeight(11 * scaleFactor);
                x = new double[]{0, 1, 5, 7, 8, 10, 12, 8, 4, 1};
                y = new double[]{9, 3, 0, 3, 1, 2, 8, 11, 9, 10};
                n = 10;
                pointValue = 50;
                asteroidSize = 2;
                break;

            case 3: // large
                this.setWidth(16 * scaleFactor);
                this.setHeight(16 * scaleFactor);
                x = new double[]{0, 4, 8, 11, 13, 10, 16, 13, 7, 6, 1, 2};
                y = new double[]{3, 0, 2, 1, 5, 6, 9, 16, 15, 16, 13, 9};
                n = 12;
                pointValue = 20;
                asteroidSize = 3;
                break;

            default: // Defaults to 'case 3' if invalid input is detected
                this.setWidth(16 * scaleFactor);
                this.setHeight(16 * scaleFactor);
                x = new double[]{0, 4, 8, 11, 13, 10, 16, 13, 7, 6, 1, 2};
                y = new double[]{3, 0, 2, 1, 5, 6, 9, 16, 15, 16, 13, 9};
                n = 12;
                pointValue = 20;
                asteroidSize = 3;
                break;
        }
        addShapeRandomnessAndScaling();
    }

    /**
     * Method to render this Sprite object to the specified GraphicsContext.
     * 
     * @param gc GraphicsContext object to be drawn to.
     */
    @Override
    public void render(GraphicsContext gc) {
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.save();
        super.rotateAboutPivot(gc, super.getAngle(), super.getPosX() + super.getWidth() / 2, super.getPosY() + super.getHeight() / 2);
        renderGivenShape(gc); // sprite comes up as rectangle shape size of bounding box
        gc.restore();
    }
    
    /**
     * Method for retrieving the point value of a given Asteroid.
     * 
     * @return Point value of this object as integer.
     */
    public int getPointValue() {
        return pointValue;
    }
    
    /**
     * Method for retrieving Asteroid size of this Asteroid object.
     * 
     * @return Asteroid size as integer, will return value between 1 and 3.
     */
    public int getAsteroidSize() {
        return asteroidSize;
    }

    private void addShapeRandomnessAndScaling() {
        for (int i = 0; i < n; i++) {
            x[i] = (x[i] + ((Math.random() * 1.5) - 0.75)) * scaleFactor;
            y[i] = (y[i] + ((Math.random() * 1.5) - 0.75)) * scaleFactor;
        }
    }

    private void renderGivenShape(GraphicsContext gc) {
        // Calculating position of polygon nodes
        double[] xRelative = new double[n];
        double[] yRelative = new double[n];
        for (int i = 0; i < n; i++) {
            xRelative[i] = x[i] + super.getPosX();
            yRelative[i] = y[i] + super.getPosY();
        }

        // GraphicsContext rendering
        gc.strokePolygon(xRelative, yRelative, n);
    }
    
    /**
     * Method for cloning an Asteroid object, of which the attributes that are 
     * copied are the size and position.
     * 
     * @return New instance of an Asteroid object.
     * @throws CloneNotSupportedException 
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        Asteroid newAsteroid = new Asteroid(this.getAsteroidSize());
        newAsteroid.setPos(this.getPosX(), this.getPosY());
        return newAsteroid;
    }
}
