package com.mycompany.seniorproject;

import java.time.Duration;
import java.time.Instant;

/**
 * A timer to track game playtime.
 * @author ndars
 */
public class Timer {
    
    private Instant start;
    private Instant stop;
    private Game game;
    
    /**
     * Instantiate a timer that tracks a particular game.
     * @param game the game to track
     */
    public Timer(Game game) {
        this.game = game;
        this.start = null;
        this.stop = null;
    }
    
    /**
     * Starts the timer. If the timer is already running or stopped, then it resets the timer before restarting it.
     * @return the instant the timer was started.
     */
    public Instant startTimer() {
        if(null != start) {
            reset();
        }
        return start = Instant.now();
    }
    
    
    /**
     * Stops the timer.
     * @return the instant the timer was stopped at. Can be null if the timer was never started.
     */
    public Instant stopTimer() {
        if(!this.isRunning()) {
            return null;
        }
        return stop = Instant.now();
    }
    
    /**
     * Resets the timer.
     */
    public void reset() {
        start = null;
        stop = null;
    }
    
    /**
     * Gets the instant the timer was started.
     * @return the instant the timer was started. Can be null if the timer was never started.
     */
    public Instant getStartTime() {
        return start;
    }
    
    /**
     * Gets the instant the timer was stopped.
     * @return the instant the timer was stopped. Can be null if the timer was never stopped.
     */
    public Instant getStopTime() {
        return stop;
    }
    
    /**
     * Gets the game that this timer is tracking.
     * @return the game that this timer is tracking.
     */
    public Game getTrackedGame() {
        return game;
    }
    
    /**
     * Gets the run-state of the timer.
     * @return true if the timer was started but hasn't stopped yet
     */
    public boolean isRunning() {
        return (null != start) && (null == stop);
    }
    
    /**
     * Calculates the duration between the start and stop time.
     * @return the duration between the start and stop time. Can be null if the timer hasn't finished running.
     */
    public Duration getDuration() {
        if(null != start && null != stop) {
            return Duration.between(start, stop);
        } else {
            return null;
        }
    }
}
