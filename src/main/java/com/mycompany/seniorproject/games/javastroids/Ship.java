/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.seniorproject.games.javastroids;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;

/**
 * Contains code for the Ship class, which is a subclass of the Sprite class.
 * This contains the coordinates and methods for rendering the ship polygons.
 * 
 * @author Justin Karp
 * @version 1.0
 * @since 12/8/2021
 */
public class Ship extends Sprite {

    private double[] xHull;
    private double[] yHull;
    private int nHull = 3;
    
    private double[] xBack;
    private double[] yBack;
    private int nBack = 2;
    
    private double[] xBoosters;
    private double[] yBoosters;
    private int nBoosters = 3;
    private int setBoostersOn = 0;
    
    private double scaleFactor = 3.4;
    private boolean setInvisible;
    
    /**
     * Instantiates a Ship object with a set height of 30 and width of 20.
     */
    public Ship() {
        this(20.0, 30.0);
    }

    /**
     * Instantiates a Ship object with the desired width and height parameters.
     * 
     * @param w Width as a double value.
     * @param h Height as a double value.
     */
    public Ship(double w, double h) {
        super(w, h);
        xHull = new double[]{0, 3, 6};
        yHull = new double[]{9, 0, 9};
        xBack = new double[]{1, 5};
        yBack = new double[]{7, 7};
        xBoosters = new double[]{2, 3, 4};
        yBoosters = new double[]{7, 7, 7};
        noCollideOn = false;
        setInvisible = false;
        applyScaling();
    }

    /**
     * Method to set ship to render in all black, making it effectively invisible.
     * 
     * @param i Boolean value designating if invisibility is true or false.
     */
    public void setInvisible(boolean i) {
        setInvisible = i;
    }
    
    /**
     * Method for setting the rear booster polygon to render with ship.
     * 
     * @param sbo Degree to which booster render it's length from the back end 
     * of the ship as an integer.
     */
    public void setBoostersOn(int sbo) {
        setBoostersOn = sbo;
    }
    
    /**
     * Method to render this Sprite object to the specified GraphicsContext element.
     * 
     * @param gc GraphicsContext object to be drawn to.
     */
    @Override
    public void render(GraphicsContext gc) {
        if (setInvisible) {
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(0);
        } else if (noCollideOn) {
            gc.setStroke(Color.DARKGREY);
            gc.setLineWidth(1);
        } else {
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(2);
        }
        gc.save();
        super.rotateAboutPivot(gc, super.getAngle(), super.getPosX() + super.getWidth() / 2, super.getPosY() + super.getHeight() / 2);
        renderGivenShape(gc); // sprite comes up as rectangle shape size of bounding box
        gc.restore();
    }

    private void applyScaling() {
        for (int i = 0; i < nHull; i++) {
            xHull[i] = xHull[i] * scaleFactor;
            yHull[i] = yHull[i] * scaleFactor;
            xBoosters[i] = xBoosters[i] * scaleFactor;
            yBoosters[i] = yBoosters[i] * scaleFactor;
        }
        for (int i = 0; i < nBack; i++) {
            xBack[i] = xBack[i] * scaleFactor;
            yBack[i] = yBack[i] * scaleFactor;
        }
    }
    
    private void renderGivenShape(GraphicsContext gc) {
        // Calculating position of polygon nodes
        double[] xHullRelative = new double[nHull];
        double[] yHullRelative = new double[nHull];
        double[] xBackRelative = new double[nBack];
        double[] yBackRelative = new double[nBack];
        double[] xBoostersRelative = new double[nBoosters];
        double[] yBoostersRelative = new double[nBoosters];
        for (int i = 0; i < nHull; i++) {
            xHullRelative[i] = xHull[i] + super.getPosX();
            yHullRelative[i] = yHull[i] + super.getPosY();
            xBoostersRelative[i] = xBoosters[i] + super.getPosX();
            yBoostersRelative[i] = yBoosters[i] + super.getPosY();
        }
        for (int i = 0; i < nBack; i++) {
            xBackRelative[i] = xBack[i] + super.getPosX();
            yBackRelative[i] = yBack[i] + super.getPosY();
        }
        
        // GraphicsContext rendering
        gc.strokePolyline(xHullRelative, yHullRelative, nHull);
        gc.strokePolyline(xBackRelative, yBackRelative, nBack);
        
        // Booster/thruster on back of ship gets glowing effect applied to it
        yBoostersRelative[1] = yBoostersRelative[1] + (14 * setBoostersOn * Math.random());
        Bloom bloom = new Bloom();
        bloom.setThreshold(100);
        gc.setEffect(bloom);
        gc.strokePolyline(xBoostersRelative, yBoostersRelative, nBoosters);

        Glow glow = new Glow();
        glow.setLevel(100);
        gc.setEffect(glow);
        gc.strokePolyline(xBoostersRelative, yBoostersRelative, nBoosters);
    }
}
