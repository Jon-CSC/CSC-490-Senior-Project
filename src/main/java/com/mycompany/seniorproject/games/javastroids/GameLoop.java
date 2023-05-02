package com.mycompany.seniorproject.games.javastroids;

import com.mycompany.seniorproject.App;
import com.mycompany.seniorproject.Game;
import com.mycompany.seniorproject.LocalUserAccount;
import com.mycompany.seniorproject.UserAccount;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * The heart of the video game application. This contains the code to run the 
 * game engine, manage multi-threading, control physics timestep/refresh rate, 
 * and graphics rendering. Also handles all player keyboard input.
 *
 * @author Justin Karp
 * @version 1.0
 * @since 12/8/2021
 */
public class GameLoop extends AnimationTimer {

//    private long startNanoTime = System.nanoTime();
    private GraphicsContext gc;
    private Scene currScene;
    private Font hyperspaceFont;

    private final ArrayList<String> inputBuffer;
    private volatile ArrayList<Asteroid> asteroids; // Thread-safe using volatile
    private volatile ArrayList<LaserBolt> laserBolts; // Thread-safe using volatile

    private final Ship playerShip;
    private int playerLifeCount;
    private volatile int totalScore;
    private long highScore = 0L;
    private double asteroidVelocityModifier;
    private int asteroidCount;
    private boolean readyToFire;
    private boolean enableKeyboardInput;
    private boolean displayScore;
    private boolean asteroidSpawnInProgress;
//    private String nameInput;

    private final long physicsTimeStep;
    private ScheduledFuture<?> physicsHandle;
    private final Runnable physicsEngine;
    private static ScheduledExecutorService gameEngineScheduler;
    
//    private final Runnable soundThread;

    /**
     * Instantiates the game engine, and clears all previous variables and game 
     * state for a new gameplay instance.
     */
    public GameLoop() {
        playerShip = new Ship();
        inputBuffer = new ArrayList<>();
        asteroids = new ArrayList<>();
        laserBolts = new ArrayList<>();
        gameEngineScheduler = Executors.newScheduledThreadPool(5);

        // 7,812,500 ns = 7.8125 ms = 128 hz (cycles/second)
        // 16,666,667 ns = 16.67 ms = 60 hz (cycles/second)
        physicsTimeStep = 16_666_667;
        this.physicsEngine = new Runnable() {
            /*  The keyboard input processing and physics/positional calculations
            *   run in their own thread. This is to keep rendering smooth and
            *   calculations consistent across displays with different refresh rates.
             */
            @Override
            public void run() {

                // Game logic for player
                if (enableKeyboardInput) {
                    processPlayerInput();
                }

                // Detecting asteroid-to-player collisions
                runCollisionAndBoundsDetection();

                // Updating sprite attributes (e.g. position)
                updateSpriteStates();
            }
        };
    }

    /**
     * Method inherited from the AnimationTimer abstract class. This method handles
     * the rendering of game objects and is called on every frame update of the 
     * JavaFX GUI.
     * 
     * @param currentNanoTime Not used.
     */
    @Override
    public void handle(long currentNanoTime) {
        // This method is called on every frame update of the JavaFX scene
        renderSprites();
    }

    /** 
     * Stops all threads related to and run by the game engine thread scheduler.
     */
    public static void stopAllThreads() {
        if (gameEngineScheduler != null) {
            gameEngineScheduler.shutdown();
        }
    }

    private void setGameConditions() {
        // Initialize game variables
        totalScore = 0;
        asteroidVelocityModifier = 0.8;
        asteroidCount = 4;
        readyToFire = true;
        playerLifeCount = 4;
        enableKeyboardInput = true;
        displayScore = false;
        asteroidSpawnInProgress = false;

        // Starts ship sprite in the center of the JavaFX scene
        playerShip.setPos(currScene.getWidth() / 2.0, currScene.getHeight() / 2.0);
        playerShip.setMaxVel(900);

        // Spawns initial set of asteroids
        spawnAsteroids(asteroidCount);

        // Spawns ship with delay, as to not destroy ship on startup by accident
        shipRespawnEvent();
    }

    /**
     * Method for setting the JavaFX Canvas element for the game engine to render
     * game objects on.
     * 
     * @param c Canvas object element to be used.
     */
    public void setGraphicsContext(Canvas c) {
        gc = c.getGraphicsContext2D();
    }

    /**
     * Initializes the entire game in the desired Scene object, including keyboard 
     * input, thread scheduling, initial game conditions, and window-resize conditions.
     * 
     * @param s Scene object to be used.
     */
    public void initializeGameScene(Scene s) {
        // Setting lots of listeners and instantiating most of the game engine
        currScene = s;
        setGameConditions();
        setListenerForGameWindowResize();
        initializePhysics();
        initializeInputDetection();

        // Pull in font file for score number rendering
        try {
            String fontFile = "HyperspaceBold.otf";
            InputStream fontStream = GameLoop.class.getResourceAsStream(fontFile);
            hyperspaceFont = Font.loadFont(fontStream, 36);
            fontStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void initializeInputDetection() {
        /*  Listens for keystrokes coming from the JavaFX scene, and adds their
        *   input codes to an buffer using ArrayList. This allows for multiple 
        *   concurrent inputs to be processed simaltaneously via iterating 
        *   through the ArrayList and processing all current active keystrokes.
         */
        currScene.setOnKeyPressed(e -> {
            String inputCode = e.getCode().toString();
            if (!inputBuffer.contains(inputCode)) {
                inputBuffer.add(inputCode);
            }
        });
        currScene.setOnKeyReleased(e -> {
            String inputCode = e.getCode().toString();
            inputBuffer.remove(inputCode);
        });
    }

    private void setListenerForGameWindowResize() {
        // Listener for 
        currScene.widthProperty().addListener(wl -> {
            gc.getCanvas().setWidth(currScene.getWidth());
        });
        currScene.heightProperty().addListener(hl -> {
            gc.getCanvas().setHeight(currScene.getHeight());
        });
    }

    private void initializePhysics() {
        physicsHandle = gameEngineScheduler.scheduleAtFixedRate(physicsEngine, 0, physicsTimeStep, TimeUnit.NANOSECONDS);
    }

    private void spawnAsteroids(int amount) {
        // Generate asteroids with random positions and velocities
        for (int i = 0; i < amount; i++) {
            Asteroid newRock = new Asteroid();
            newRock.setPos(Math.random() * gc.getCanvas().getWidth(),
                    Math.random() * gc.getCanvas().getHeight());
            newRock.setVel((Math.random() - 0.5) * 5 * asteroidVelocityModifier,
                    (Math.random() - 0.5) * 5 * asteroidVelocityModifier);
            newRock.setRotate((Math.random() * 2) - 1);
            asteroids.add(newRock);
        }
    }

    private void spawnSmallerAsteroidsFromCollision(Queue<Asteroid> newAsteroidQueue) {
        for (int i = 0; i < newAsteroidQueue.size(); i++) {
            Asteroid originalRock = newAsteroidQueue.poll();
            for (int j = 0; j < 2; j++) {
                Asteroid newRock = new Asteroid(originalRock.getAsteroidSize() - 1);
                newRock.setPos(originalRock.getPosX(), originalRock.getPosY());
                newRock.setVel((Math.random() - 0.5) * 5 * asteroidVelocityModifier,
                        (Math.random() - 0.5) * 5 * asteroidVelocityModifier);
                newRock.setRotate((Math.random() * 2) - 1);
                asteroids.add(newRock);
            }
        }
    }

    private void shipRespawnEvent() {
        playerShip.setNoCollideOn(true);
        playerShip.setInvisible(true);
        enableKeyboardInput = false;

        /*  Creates two delay sequences using existing threads in the physics 
        *   scheduler executor service.
         */
        Runnable delayCollisions = new Runnable() {
            @Override
            public void run() {
                playerShip.setNoCollideOn(false);
            }
        };
        Runnable delayShipSpawn = new Runnable() {
            @Override
            public void run() {
                playerShip.setPos(currScene.getWidth() / 2.0, currScene.getHeight() / 2.0);
                playerShip.setVel(0, 0);
                playerShip.setAngle(0);
                playerShip.setInvisible(false);
                enableKeyboardInput = true;
                gameEngineScheduler.schedule(delayCollisions, 2, TimeUnit.SECONDS);
            }
        };
        gameEngineScheduler.schedule(delayShipSpawn, 1, TimeUnit.SECONDS);
    }

    private void checkAndCreateNewAsteroidWaveEvent() {
        asteroidSpawnInProgress = true;
        asteroidVelocityModifier += 0.2;
        asteroidCount += 3;
        Runnable delayNewAsteroidWave = new Runnable() {
            @Override
            public void run() {
                spawnAsteroids(asteroidCount);
                asteroidSpawnInProgress = false;
            }
        };
        gameEngineScheduler.schedule(delayNewAsteroidWave, 2, TimeUnit.SECONDS);
    }

    private void runCollisionAndBoundsDetection() {
        Queue<Asteroid> newAsteroidQueue = new LinkedList<>();
        Iterator<Asteroid> asteroidsIter = asteroids.iterator();

        while (asteroidsIter.hasNext()) {
            boolean checkRock = true;
            Asteroid nextRock = asteroidsIter.next();
            if (playerShip.intersects(nextRock)) {
                if (playerLifeCount > 1) {
                    shipRespawnEvent();
                } else {
                    gameOverEvent();
                }
                playerLifeCount--;
            }
            Iterator<LaserBolt> laserBoltIter = laserBolts.iterator();
            while (laserBoltIter.hasNext()) {
                LaserBolt nextBolt = laserBoltIter.next();
                if (nextBolt.intersects(nextRock)) {
                    totalScore += nextRock.getPointValue();
                    if (nextRock.getAsteroidSize() != 1) {
                        // If it's not the smallest rock, break into smaller ones
                        try {
                            newAsteroidQueue.add((Asteroid) nextRock.clone());
                        } catch (CloneNotSupportedException ex) {
                            ex.printStackTrace();
                        }
                    }
                    asteroidsIter.remove();
                    laserBoltIter.remove();
                    checkRock = false;

                    break;
                }
            }
            if (checkRock) {
                nextRock.checkAndCorrectOutOfBoundsPosition(gc);
            }
        }

        Iterator<LaserBolt> laserBoltIter = laserBolts.iterator();
        while (laserBoltIter.hasNext()) {
            LaserBolt nextBolt = laserBoltIter.next();
            if (nextBolt.traveledTotalMaxDistance()) {
                laserBoltIter.remove();
            }
            nextBolt.checkAndCorrectOutOfBoundsPosition(gc);
        }

        spawnSmallerAsteroidsFromCollision(newAsteroidQueue);
        playerShip.checkAndCorrectOutOfBoundsPosition(gc);
    }

    private void processPlayerInput() {
        /*  Branchless method of processing inputs, gives no input priority 
        *   over another (e.g. pressing right and left simaltaneously will make
        *   the ship not turn in either direction, as they cancel out).
         */
        int left = (inputBuffer.contains("LEFT") || inputBuffer.contains("A")) ? 1 : 0;
        int right = (inputBuffer.contains("RIGHT") || inputBuffer.contains("D")) ? 1 : 0;
        int up = (inputBuffer.contains("UP") || inputBuffer.contains("W")) ? 1 : 0;
        int down = (inputBuffer.contains("DOWN") || inputBuffer.contains("S")) ? 1 : 0;

        playerShip.setRotate((-6 * left) + (6 * right));
        playerShip.addVelRelativeToAngle((-0.17 * up));// + (0.15 * down));
        playerShip.applyDrag(0.01);
        playerShip.setBoostersOn(up);

        if (inputBuffer.contains("SPACE")) {
            createAndFireLaserBolt();
            readyToFire = false;
        } else {
            readyToFire = true;
        }
    }

    private void createAndFireLaserBolt() {
        if (readyToFire) {
            LaserBolt newBolt = new LaserBolt();
            //double[] pos = playerShip.getCenterPosition();
            //newBolt.setPos(pos[0], pos[1]);
            newBolt.setPos(playerShip.getPosX(), playerShip.getPosY());
            newBolt.setAngle(playerShip.getAngle());
            newBolt.setVelRelativeToAngle(-10);//playerShip.getVelRelativeToAngle() - 450);
            laserBolts.add(newBolt);
        }
    }

    private void updateSpriteStates() {
        playerShip.update();

        asteroids.forEach(asteroid -> {
            asteroid.update();
        });

        laserBolts.forEach(laserBolt -> {
            laserBolt.update();
        });

        if (asteroids.isEmpty() && !asteroidSpawnInProgress) {
            checkAndCreateNewAsteroidWaveEvent();
        }
    }

    private void renderSprites() {
        gc.clearRect(0, 0, currScene.getWidth(), currScene.getHeight());

        // Sprite rendering
        playerShip.render(gc);
        asteroids.forEach(asteroid -> {
            if (asteroid != null) {
                asteroid.render(gc);
            }
        });
        laserBolts.forEach(laserBolt -> {
            if (laserBolt != null) {
                laserBolt.render(gc);
            }
        });

        // Score rendering
        if (playerLifeCount == 0) {
            renderGameOver();
        } else {
            renderScoreAndLives();
        }
        if (displayScore) {
            renderNewScore();
            renderHighestScore();
            renderReturnToMenu();
        }
    }

    private void renderScoreAndLives() {
        gc.setFill(Color.WHITE);
        gc.setFont(hyperspaceFont);
        gc.fillText("" + totalScore, 40, 50);

        double offsetX = 45;
        double offsetY = 70;
        double[] x = new double[]{0 + offsetX, 8 + offsetX, 16 + offsetX};
        double[] y = new double[]{22 + offsetY, 0 + offsetY, 22 + offsetY};

        for (int i = 0; i < playerLifeCount; i++) {
            double[] xRelative = new double[x.length];
            for (int j = 0; j < 3; j++) {
                xRelative[j] = x[j] + (i * 24);
            }
            gc.strokePolygon(xRelative, y, 3);
        }
    }

    private void renderGameOver() {
        gc.setFill(Color.WHITE);
        gc.setFont(hyperspaceFont);
        gc.fillText("GAME OVER", (gc.getCanvas().getWidth() / 2) - 86, (gc.getCanvas().getHeight() / 2) - 210);
    }

//    private void renderNameInput() {
//        gc.setFill(Color.WHITE);
//        gc.setFont(hyperspaceFont);
//        gc.fillText("> Enter name: " + nameInput + "_", (gc.getCanvas().getWidth() / 2) - 220, gc.getCanvas().getHeight() / 2);
//    }
    
    private void renderNewScore() {
        gc.setFill(Color.WHITE);
        gc.setFont(hyperspaceFont);
        gc.fillText("Your score: " + totalScore, (gc.getCanvas().getWidth() / 2) - 160, (gc.getCanvas().getHeight() / 2) - 100);
    }
    
    private void renderHighestScore() {
        gc.setFill(Color.WHITE);
        gc.setFont(hyperspaceFont);
        gc.fillText("High score: " + highScore, (gc.getCanvas().getWidth() / 2) - 160, (gc.getCanvas().getHeight() / 2));
    }
    
    private void renderReturnToMenu() {
        gc.setFill(Color.WHITE);
        gc.setFont(hyperspaceFont);
        gc.fillText("Press enter to return to menu", (gc.getCanvas().getWidth() / 2) - 310, (gc.getCanvas().getHeight() / 2) + 100);
    }

    private void gameOverEvent() {
        playerShip.setNoCollideOn(true);
        playerShip.setInvisible(true);
        enableKeyboardInput = false;
        
        Runnable delayNameInputOnGameOver = new Runnable() {
            @Override
            public void run() {
                LocalUserAccount.getInstance().recordHiscore(Game.JAVASTROIDS, totalScore);
                LocalUserAccount.getInstance().recordLastScore(Game.JAVASTROIDS, totalScore);
                
                String userID = LocalUserAccount.getInstance().getUser().getUserID();
                UserAccount profileUser = UserAccount.downloadUser(userID, App.fstore);
                HashMap<String, Object> gameData = profileUser.getGameData();
                highScore = (long)gameData.getOrDefault(Game.JAVASTROIDS.getScoreField(), (long)0);
                
                displayScore = true;
                
                currScene.setOnKeyPressed(e -> {
                    KeyCode currentCode = e.getCode();
                    if (currentCode.equals(KeyCode.ENTER)) {
                        try {
                            App.setRoot("games/javastroids/mainMenu");
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        stopAllThreads();
                    }
                });
            }
        };
        gameEngineScheduler.schedule(delayNameInputOnGameOver, 2, TimeUnit.SECONDS);
    }

}
