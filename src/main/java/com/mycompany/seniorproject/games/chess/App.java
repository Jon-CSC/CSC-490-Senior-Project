package edu.farmingdale.chessgame;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * JavaFX App
 */
public class App extends Application {


   // using 3 groups, one for the pieces, one for the grid,
    // and one for the highlighted square 
    // makes it easier to update the board with getChildren.clear();
    private static Scene scene;
    private static Group gridGroup;
    private static Group pieceGroup;
    private static Group highlightGroup;
    private static Pane pane;
    
    // for adding the pieces to the screen
    private static InputStream stream;
    private static Image image;
    private static ImageView imageView;

    // variable for finding position when mouse clicked
    private static int[] coordinates;

    // list of pieces to easily access, along with king declarations for setMoves()
    private static ArrayList<Piece> pieces = new ArrayList<Piece>();
    private static Piece whiteKing;
    private static Piece blackKing;
    private static Piece king;

    // stores the current turn as "white" or "black"
    private static String turn;

    // figures out if the curPiece is ready to be moved
    private static boolean toMove;
    private static Piece curPiece;

    // placeholder Piece p to call isOccupied for this class
    private static Piece p = new Piece(-1, -1, "n/a", "placement", "filename");
    
    // variable to store the corresponding column of a possible en passant, as the previous move needs to be tracked
    private static int enPassant = -1;
    
    // relative file paths for all the pieces, edited in gimp software
    private static String whiteRookFileStr = "src/main/resources/PiecePics/whiteRook.png";
    private static String blackRookFileStr = "src/main/resources/PiecePics/blackRook.png";
    private static String whiteKnightFileStr = "src/main/resources/PiecePics/whiteKnight.png";
    private static String blackKnightFileStr = "src/main/resources/PiecePics/blackKnight.png";
    private static String whiteBishopFileStr = "src/main/resources/PiecePics/whiteBishop.png";
    private static String blackBishopFileStr = "src/main/resources/PiecePics/blackBishop.png";
    private static String whiteQueenFileStr = "src/main/resources/PiecePics/whiteQueen.png";
    private static String blackQueenFileStr = "src/main/resources/PiecePics/blackQueen.png";
    private static String whiteKingFileStr = "src/main/resources/PiecePics/whiteKing.png";
    private static String blackKingFileStr = "src/main/resources/PiecePics/blackKing.png";
    private static String whitePawnFileStr = "src/main/resources/PiecePics/whitePawn.png";
    private static String blackPawnFileStr = "src/main/resources/PiecePics/blackPawn.png";

    @Override
    public void start(Stage stage) throws IOException {
        // assign the groups and pane
        gridGroup = new Group();
        pieceGroup = new Group();
        highlightGroup = new Group();
        pane = new Pane(gridGroup, pieceGroup, highlightGroup);

        // constructing the board
        int count = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // make the grid with alternating colors white and grey
                Rectangle r = new Rectangle();
                
                coordinates = coordinateFormula(i, j); // to find the top left corner of the rectangle
                
                r.setX(coordinates[0]);
                r.setY(coordinates[1]);
                r.setWidth(100);
                r.setHeight(100);
                
                if (count % 2 == 0) r.setFill(Color.WHITE);
                else r.setFill(Color.rgb(51, 153, 175)); // 8 bit numbers for a light blue
                
                gridGroup.getChildren().add(r);

                count++; // to alternate colors
            }
            count++; // to alternate colors
        }
        
        setUpPieces();
        drawBoard();

        // declare the scene with after the pieces are added to the screen
        scene = new Scene(pane, 850, 850);
        // a scene of 850x850 fit the pieces onto the screen perfectly, not sure how to resize the images
        stage.setScene(scene);
        stage.setTitle("Chess");
        stage.setResizable(true);
        stage.show();
        
        startGame();
    }

    private void setUpPieces() {
        /* purpose
         * set up all the pieces with coordinates, 
         * color, and set the available moves
         * also set the whiteKing and blackKing accordingly
         */
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = null;
                if (j == 0) { // 8th rank
                    if (i == 0 || i == 7) {piece = new Piece(i, j, "black", "rook", blackRookFileStr);} // add black rook
                    if (i == 1 || i == 6) {piece = new Piece(i, j, "black", "knight", blackKnightFileStr);} // add black knight
                    if (i == 2 || i == 5) {piece = new Piece(i, j, "black", "bishop", blackBishopFileStr);} // add black bishop
                    if (i == 3) {piece = new Piece(i, j, "black", "queen", blackQueenFileStr);} // add black queen
                    if (i == 4) { // add black king
                        piece = new Piece(i, j, "black", "king", blackKingFileStr); 
                        blackKing = piece;
                    } 
                }
                if (j == 1) {piece = new Piece(i, j, "black", "pawn", blackPawnFileStr);} // add black pawns
                if (j == 6) {piece = new Piece(i, j, "white", "pawn", whitePawnFileStr);} // add white pawns
                if (j == 7) { // 1st rank
                    if (i == 0 || i == 7) {piece = new Piece(i, j, "white", "rook", whiteRookFileStr);} // add white rook
                    if (i == 1 || i == 6) {piece = new Piece(i, j, "white", "knight", whiteKnightFileStr);} // add white knight
                    if (i == 2 || i == 5) {piece = new Piece(i, j, "white", "bishop", whiteBishopFileStr);} // add white bishop
                    if (i == 3) {piece = new Piece(i, j, "white", "queen", whiteQueenFileStr);} // add white queen
                    if (i == 4) { // add white king
                        piece = new Piece(i, j, "white", "king", whiteKingFileStr);
                        whiteKing = piece;
                    } 
                }
                if (piece != null) {pieces.add(piece);}
            }   
        }        
    }

    private void highlightSquare(int x, int y) {
        /* purpose
         * this function highlights a chosen square
         * so long as the click is inside the grid, helps the user
         * to determine where they have clicked and if they have
         * selected a piece or not
         */
        highlightGroup.getChildren().clear();
        Rectangle r = new Rectangle();
        coordinates = coordinateFormula(x, y);
        r.setX(coordinates[0]);
        r.setY(coordinates[1]);
        r.setWidth(100);
        r.setHeight(100);
        r.setFill(Color.rgb(0, 255, 0));
        r.setOpacity(0.15);

        highlightGroup.getChildren().add(r);
    }

    private void startGame() {
        // assign the first turn to be white
        turn = "white";

        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // getting x and y coordinates
                int x = (int)event.getX();
                int y = (int)event.getY();
                int[] square = findSquare(x, y); // finds the 0-indexed row and column of where the user clicked
                x = square[0];
                y = square[1];
                
                highlightSquare(x, y); // highlight the chosen square
                
                // set the king for the setMoves methods
                king = turn.equals("white") ? whiteKing: blackKing;
                
                if (!toMove) {
                    toMove = setPiece(x, y, toMove); // satisfying one liner
                }
                else {
                    for (Location location: curPiece.getMoves()) {
                        if (location.getX() == x && location.getY() == y) {

                            // if a piece is took, remove that piece
                            Piece occupiedPiece = p.isOccupied(pieces, new Location(x, y));
                            if (occupiedPiece != null) {pieces.remove(occupiedPiece);}

                            String type = curPiece.type; // this helps to set for castling evaluation
                            if (type.equals("rook") || type.equals("king")) {curPiece.hasMoved = true;}
                            
                            // call castle and en passant funcs() to make this shorter
                            checkCastled(x);
                            checkEnPassant(x, y);

                            // move the piece to its new location
                            curPiece.setX(x);
                            curPiece.setY(y);

                            checkPromotion(y);

                            // clear the groups, prepare for redraw
                            pieceGroup.getChildren().clear();
                            highlightGroup.getChildren().clear();
                                
                            // if turn is white, then turn is black and vice versa
                            turn = turn.equals("white") ? "black": "white";
                            king = turn.equals("white") ? whiteKing: blackKing;

                            king.check = king.inCheck(pieces, king); // determine if the king is in check

                            checkConditions(pieces, turn); // this looks for stalemate or checkmate
                            
                            flipBoard(); // makes the game much cooler when flipping the board

                            try {
                                drawBoard();
                            } catch (FileNotFoundException e) {e.printStackTrace(); System.out.println("paths are likely wrong");}
                        }
                        toMove = setPiece(x, y, toMove); // see if user selected another piece of same color to move
                    }
                }
            }
        });
    }

    private void checkCastled(int x) {
        /* purpose
         * this function checks to see if the king has castled,
         * and if so, where the rook should move because of the 
         * distinction between king and queen side
         * also, this is made a lot easier when the board is flipped
         * so the side playing is always on the bottom
         */
        // if the king castled, move the rook accordingly
        if (curPiece.getType().equals("king")) {
            Piece rookToTransfer;
            if (curPiece.x - x == -2) { // right side castle
                rookToTransfer = curPiece.isOccupied(pieces, new Location(7, 7));
                rookToTransfer.x = x - 1;
            }
            if (curPiece.x - x == 2) { // left side castle
                rookToTransfer = curPiece.isOccupied(pieces, new Location(0, 7));
                rookToTransfer.x = x + 1;
            }
        }
    }

    private void checkEnPassant(int x, int y) {
        /* purpose
         * find if a pawn can En Passant another pawn,
         * a special move in chess to not allow for easier pawn promotion
         * article about En Passant: https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=&cad=rja&uact=8&ved=2ahUKEwjqqtnPycT4AhUGEEQIHf1VAhUQFnoECAcQAQ&url=https%3A%2F%2Fen.wikipedia.org%2Fwiki%2FEn_passant&usg=AOvVaw031ojfxMC8PKhVpAE20T7l
         */
        // en passant check
        if (enPassant == x) {
            Location location1 = new Location(x, y);
            Location location2 = new Location(curPiece.x, curPiece.y);
            double distance = location1.distance(location1, location2);
            if (distance > 1.0 && distance < 2.0) { // checks for a diagonal move, and a move up 2 isn't possible when trying to en passant
                Piece enPassantPiece = p.isOccupied(pieces, new Location(enPassant, 3)); // en passant piece is always on the 5th rank
                if (enPassantPiece != null && enPassantPiece.type.equals("pawn")) {
                    pieces.remove(enPassantPiece);
                }
            }
            enPassant = -1;
        }
        // if a pawn moves 2 squares, it has the opportunity to be "en passanted"
        if (curPiece.type.equals("pawn")) {
            if (curPiece.y - y == 2) {
                enPassant = curPiece.x;
                // must flip the en passant column value for the next turn only
                int xDifference = Math.abs(3 - enPassant);
                if (enPassant <= 3) {enPassant = 4 + xDifference;}
                else {enPassant = 4 - xDifference;}
            }
        } else {enPassant = -1;}
    }

    private void checkPromotion(int y) {
        /* purpose
         * check if the pawn has reached the promotion square
         * although either a knight or queen should be minimally allowed,
         * I decided to decrease that work and just make it a queen, so 
         * hopefully the user likes queens
         * 
         * an easy way to decide would be to show a prompt label asking for
         * a queen or knight, simply by saying
         * Press "1" for a queen, and "2" for a knight
         * 
         * note: this is a lot easier when flipping the board
         */
        if (curPiece.type.equals("pawn") && curPiece.y == 0) {
            curPiece.type = "queen"; 
            if (curPiece.color.equals("white")) {curPiece.setFileString(whiteQueenFileStr);}
            else {curPiece.setFileString(blackQueenFileStr);}
        }
    }

    private void checkConditions(ArrayList<Piece> pieces, String turn) {
        /* purpose
         * after every move, need to evaulate whether or not the 
         * enemy's king is checkmated or it is a stalemate
         */
        if (king.checkMate(pieces, turn)) {
            String otherColor = turn.equals("white") ? "black": "white";
            System.out.println("checkmate for " + otherColor);
        }
        if (king.staleMate(pieces, turn)) {
            System.out.println("stalemate");
        }
    }

    private boolean setPiece(int x, int y, boolean toMove) {
        /* purpose
         * using the placeholder Piece p, find whether or not
         * the user has selected a piece that matches their color, 
         * so that on the next clicked it can be moved 
         */
        try {
            Piece piece = p.isOccupied(pieces, new Location(x, y));
            if (piece.color.equals(turn)) {
                curPiece = piece;
                curPiece.setMoves(pieces, curPiece, king, true);
                if (curPiece.type.equals("pawn") && enPassant != -1 && curPiece.y == 3) {
                    curPiece.addMove(pieces, king, new Location(enPassant, 2));
                }
                return true;
            }
        } catch (NullPointerException e) {}
        return false;
    }

    private void flipBoard() {
        /* purpose
         * flips the board, so that the current turn
         * of the game is at the bottom of the screen,
         * makes for easier playing
         * 
         * for flipping the pieces, the pieces need
         * to be flipped on a x-axis and a y-axis, which 
         * I centered at coulmn and row 4
         */
        for (Piece piece: pieces) {
            int xDifference = Math.abs(3 - piece.x);
            if (piece.x <= 3) {piece.x = 4 + xDifference;}
            else {piece.x = 4 - xDifference;}
            int yDifference = Math.abs(3 - piece.y);
            if (piece.y <= 3) {piece.y = 4 + yDifference;}
            else {piece.y = 4 - yDifference;}
        }
    }

    private int[] findSquare(int x, int y) {
        /* purpose
         * returns the square coordinates
         * given the clicked mouse coords
         * 
         * each sqaure is 100 units wide and 
         * 100 unis long
         * 
         * we want to use floor division here
         */
        int[] res = new int[2];
        res[0] = (x-25)/100;
        res[1] = (y-25)/100;
        return res;
    }

    private void drawBoard() throws FileNotFoundException {
        /* purpose
         * using all the Piece objects active in pieces, 
         * draw them to the screen given their corresponding 
         * coordinates
         */
        for (Piece piece: pieces) {
            setImage(piece.getFileString(), piece.getX(), piece.getY());
        }
    }

    private void setImage(String pathname, int i, int j) throws FileNotFoundException {
        /* purpose
         * delivers the pictures of the pieces 
         * with a String pathname to the board,
         * by calling coordinateFormula() and using
         * pieceGroup
         */
        try {
            stream = new FileInputStream(pathname);
            image = new Image(stream);
            imageView = new ImageView();
            imageView.setImage(image);
            coordinates = coordinateFormula(i, j);
            imageView.setX(coordinates[0]);
            imageView.setY(coordinates[1]); 
            pieceGroup.getChildren().add(imageView);
        } catch (FileNotFoundException e) {System.out.println("cannot find file"); e.printStackTrace();}
    }

    private int[] coordinateFormula(int x, int y) {
        /* purpose
         * given an x and y coordinate,
         * find the corresponding location
         * for the top left part of an image
         * and rectangle
         */
        int[] res = new int[2];
        res[0] = 25+100*x;
        res[1] = 25+100*y;
        return res;
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}


/* additional notes
 *
 * https://www.geeksforgeeks.org/class-type-casting-in-java/#:~:text=Typecasting%20is%20the%20assessment%20of,direction%20to%20the%20inheritance%20tree.
 * shows how to Override functions in parent classes to properly call functions of subclasses
 * just use the same name and arguments with @Override
 * 
 * java platform se binary was telling me that my program was in an infinite loop
 * 
 * extra code to identify checks
 * ArrayList<Location> toRemove = new ArrayList<Location>();
            for (Location loc: res) {
                this.x = loc.x;
                this.y = loc.y;
                if (king.inCheck(pieces)) {
                    toRemove.add(loc);
                }
            }
            for (Location loc: toRemove) {res.remove(loc);}
            this.x = x;
            this.y = y;
 * 
 * extra code for pawn.setMoves()
 * case "black":
                // up 1 and not at end of the board
                Location downOne = new Location(x, y+1);
                if (y < 7 && isOccupied(pieces, downOne) == null) {
                    res.add(downOne);
                    // up 2 and at starting position
                    Location downTwo = new Location(x, y+2); // can only move down two if can move down one
                    if (y == 1 && isOccupied(pieces, downTwo) == null) {res.add(downTwo);}
                }
                //check diagonals
                Location downRight = new Location(x+1, y+1);
                if (isOccupied(pieces, downRight) != null) {helper(pieces, downRight, res, otherColor);}
                Location downLeft = new Location(x-1, y+1);
                if (isOccupied(pieces, downLeft) != null) {helper(pieces, downLeft, res, otherColor);}
                break;
 * 
 * extra code for castling without flipBoard()
 * 
 * for castling with white always at the bottom of the screen
 * try {
                if (isOccupied(pieces, new Location(7, 7)).hasMoved) {kingSideCastle = false;}
            } catch (NullPointerException e) {}
            try {
                if (isOccupied(pieces, new Location(0, 7)).hasMoved && isOccupied(pieces, new Location(1, 7)) == null) {
                    queenSideCastle = false;
                }
            } catch (NullPointerException e) {}
 * 
 * 
 * extra code for moving a rook when castling
 * // move the rook if a castle move is played
                            if (curPiece.type.equals("king") && curPiece.x - x == -2) { // king side castle
                                if (curPiece.color.equals("white")) {
                                    Piece rook = curPiece.isOccupied(pieces, new Location(7, 7));
                                    rook.setX(rook.getX()-2);
                                }
                                else { // color = "black"
                                    Piece rook = curPiece.isOccupied(pieces, new Location(7, 0));
                                    rook.setX(rook.getX()-2);
                                }
                            }
                            if (curPiece.type.equals("king") && curPiece.x - x == 2) { // queen side castle
                                if (curPiece.color.equals("white")) {
                                    Piece rook = curPiece.isOccupied(pieces, new Location(0, 7));
                                    rook.setX(rook.getX()+3);
                                }
                                else { // color = "black"
                                    Piece rook = curPiece.isOccupied(pieces, new Location(0, 0));
                                    rook.setX(rook.getX()+3);
                                }
                            }
 */
