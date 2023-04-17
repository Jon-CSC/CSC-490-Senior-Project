package com.mycompany.seniorproject.games.javastroids;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Glow;
import javafx.scene.effect.MotionBlur;
import javafx.scene.paint.Color;

/**
 * Contains code for the LaserBolt class, which is a subclass of the Sprite class.
 * Controls various components of the LaserBolts fired by the player during gameplay.
 *
 * @author Justin Karp
 * @version 1.0
 * @since 12/8/2021
 */
class LaserBolt extends Sprite {

    private double distanceTraveled = 0;
    private double maxDistance;

    /**
     * Instantiates a new LaserBolt object with a size of (5, 5) and max
     * travel distance of 500.
     */
    public LaserBolt() {
        super(5, 5);
        maxDistance = 500;
    }

    /**
     * Method for modifying the max distance the laser bolt can travel.
     * 
     * @param maxDistance Distance as a double value.
     */
    public void maxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    /**
     * Method for return the currently set max distance of the laser bolt.
     * 
     * @return Max distance as a double value.
     */
    public double maxDistance() {
        return maxDistance;
    }

    /**
     * Method to check whether a laser bolt has traveled a total distance exceeding
     * its max distance.
     * 
     * @return Boolean value where true means distance traveled is greater than 
     * max distance.
     */
    public boolean traveledTotalMaxDistance() {
        return (distanceTraveled > maxDistance);
    }

    /**
     * Method to update the current positional and rotational values of the given sprite.
     */
    @Override
    public void update() {
        super.update();
        distanceTraveled += Math.sqrt((this.getVelX()* this.getVelX()) + (this.getVelY() * this.getVelY()));
    }

    /**
     * Method to render this laser bolt sprite to the specified GraphicsContext element.
     * 
     * @param gc GraphicsContext object to be rendered to.
     */
    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.save();
        super.rotateAboutPivot(gc, super.getAngle(), super.getPosX() + super.getWidth() / 2, super.getPosY() + super.getHeight() / 2);
        renderGivenShape(gc); // sprite comes up as rectangle shape size of bounding box
        gc.restore();
    }

    private void renderGivenShape(GraphicsContext gc) {
        MotionBlur mb = new MotionBlur();
        mb.setRadius(25);
        mb.setAngle(45);
        gc.setEffect(mb);
        gc.strokeRect(super.getPosX(), super.getPosY(), super.getWidth(), super.getHeight());

        mb.setAngle(135);
        gc.setEffect(mb);
        gc.strokeRect(super.getPosX(), super.getPosY(), super.getWidth(), super.getHeight());

        Bloom bloom = new Bloom();
        bloom.setThreshold(100);
        gc.setEffect(bloom);
        gc.fillOval(super.getPosX(), super.getPosY(), super.getWidth(), super.getHeight());

        Glow glow = new Glow();
        glow.setLevel(100);
        gc.setEffect(glow);
        gc.fillOval(super.getPosX(), super.getPosY(), super.getWidth(), super.getHeight());
    }
}
