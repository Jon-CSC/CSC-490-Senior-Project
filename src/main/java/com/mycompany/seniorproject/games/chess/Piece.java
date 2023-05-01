/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.farmingdale.chessgame;

import java.util.ArrayList;

/**
 *
 * @author nicolechan
 */
public class Piece extends Location{
    
    
    /*
     * some of these variables are not needed
     * by certain pieces, so have to be careful 
     * in calling, don't want to call .check
     * for a pawn piece, and so on
     */
    protected boolean alive;
    protected String color;
    protected ArrayList<Location> moves;
    protected String filename;
    protected String type;
    protected boolean check;
    protected boolean hasMoved;

    public Piece(int x, int y, String color, String type, String filename) {
        super(x, y); // to set the location attributes
        this.color = color;
        this.alive = true;
        this.type = type;

        // the hasMoved variable is for castling
        if (type.equals("king")) { // if king, set the check and hasMoved variable
            this.check = false;
            this.hasMoved = false;
        }
        if (type.equals("rook")) {this.hasMoved = false;} // if rook, set the hasMoved var

        this.filename = filename;
        this.moves = new ArrayList<Location>();
    }

    /* getter and setter methods
     * some setter methods are left out for 
     * the reason that an object's color should
     * never change
     */
    public String getColor() {return color;}

    public String getFileString() {return this.filename;}
    public void setFileString(String filename) {this.filename = filename;}

    public boolean isAlive() {return alive;}
    public void setAlive(boolean alive) {this.alive = alive;}

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public ArrayList<Location> getMoves() {return moves;}

    public void addMove(ArrayList<Piece> pieces, Piece king, Location location) { 
        // only for special en passant move
        int x = this.x;
        int y = this.y;
        this.x = location.x;
        this.y = location.y;
        if (!inCheck(pieces, king)) {
            this.moves.add(location);
        }
        this.x = x;
        this.y = y;

    }

    private ArrayList<Location> setMoves(ArrayList<Piece> pieces, Piece piece, Piece king, boolean lookForCheck, boolean flipped) {
        /* purpose
         * this special function with a difference
         * is required for when checking if a move
         * is possible, all moves are set, but if the 
         * type is pawn, when the board is flipped the piece is 
         * attacking downward, so just need to check its 
         * diagonols to see if it is attacking the king
         */
        ArrayList<Location> res = new ArrayList<Location>();
        String otherColor = piece.color.equals("white") ? "black": "white";
        Location downRight = new Location(x+1, y+1);
        if (isOccupied(pieces, downRight) != null) {helper(pieces, downRight, res, otherColor);}
        Location downLeft = new Location(x-1, y+1);
        if (isOccupied(pieces, downLeft) != null) {helper(pieces, downLeft, res, otherColor);}
        this.moves = res;
        return res;
    }

    public void setMoves(ArrayList<Piece> pieces, Piece piece, Piece king, boolean lookForCheck) {
        /* purpose
         * through a switch statement
         * find the correspoding moves 
         * for a selected piece
         * the findChecks() method in most
         * of these functions below look for pins
         * for a selected piece
         */
        ArrayList<Location> res = new ArrayList<Location>();
        String otherColor = piece.color.equals("white") ? "black": "white";
        switch (piece.type) {
            case "pawn": // set pawn moves
                pawnMoves(pieces, piece, king, lookForCheck, res, otherColor);
                break;
            case "knight": // set knight moves
                knightMoves(pieces, piece, king, lookForCheck, res, otherColor);
                break;
            case "bishop": // set bishop moves
                bishopMoves(pieces, piece, king, lookForCheck, res, otherColor);
                break;
            case "rook": // set rook moves
                rookMoves(pieces, piece, king, lookForCheck, res, otherColor);
                break;
            case "queen": // set queen moves
                queenMoves(pieces, piece, king, lookForCheck, res, otherColor);
                break;
            case "king": // set king moves
                kingMoves(pieces, piece, king, lookForCheck, res, otherColor);
                break;
            default:
                System.out.println("this shouldn't happen ever");
                break;
        }
    }

    private ArrayList<Location> pawnMoves(ArrayList<Piece> pieces, Piece piece, Piece king, boolean lookForCheck,  
        ArrayList<Location> res, String otherColor) {
        /* purpose
         * find all available pawn moves
         * this is a lot easier when the board flips
         * because the pawn only goes up
         * regardless of color
         */
        //up 1
        Location upOne = new Location(x, y-1);
        if (y > 0 && isOccupied(pieces, upOne) == null) {
            res.add(upOne);
            //up 2 and at starting position
            Location upTwo = new Location(x, y-2); // can only move 2 up if can move one up
            if (y == 6 && isOccupied(pieces, upTwo) == null) {res.add(upTwo);}
        }
        //check diagonals
        Location upRight = new Location(x+1, y-1);
        if (isOccupied(pieces, upRight) != null) {helper(pieces, upRight, res, otherColor);}
        Location upLeft = new Location(x-1, y-1);
        if (isOccupied(pieces, upLeft) != null) {helper(pieces, upLeft, res, otherColor);}
        
        if (lookForCheck) {
            res = findChecks(res, piece, king, pieces, x, y);
        }
        this.moves = res;
        return res;  
        
    }

    private ArrayList<Location> knightMoves(ArrayList<Piece> pieces, Piece piece, Piece king,
     boolean lookForCheck, ArrayList<Location> res, String otherColor) {
        /* purpose
         * find all the knight moves
         * for any position, so long as the
         * knight is in bounds after the move, there
         * are 8 possible moves
         */
        Location location;

        // up 1, left 2
        location = new Location(x-2, y-1);
        helper(pieces, location, res, otherColor);

        // up 2, left 1
        location = new Location(x-1, y-2);
        helper(pieces, location, res, otherColor);

        // up 1, right 2
        location = new Location(x+2, y-1);
        helper(pieces, location, res, otherColor);

        // up 2, right 1
        location = new Location(x+1, y-2);
        helper(pieces, location, res, otherColor);

        // down 1, left 2
        location = new Location(x-2, y+1);
        helper(pieces, location, res, otherColor);

        // down 2, left 1
        location = new Location(x-1, y+2);
        helper(pieces, location, res, otherColor);

        // down 1, right 2
        location = new Location(x+2, y+1);
        helper(pieces, location, res, otherColor);

        // down 2, right 1
        location = new Location(x+1, y+2);
        helper(pieces, location, res, otherColor);

        if (lookForCheck) {
            res = findChecks(res, piece, king, pieces, x, y);
        }
        this.moves = res;
        return res;
    }

    private ArrayList<Location> bishopMoves(ArrayList<Piece> pieces, Piece piece, Piece king, 
        boolean lookForCheck, ArrayList<Location> res, String otherColor) {
        /* purpose
         * the bishop moves on the diagonol, so move
         * the bishop on the four diagonols until it goes
         * out of bounds, reaches a piece of its own color,
         * or of another color
         */
        boolean upRight, upLeft, downRight, downLeft;
        upRight = upLeft = downRight = downLeft = true;
        Location location;
        
        for (int i = 1; i <= 7; i++) {
            // up and right
            if (upRight) {
                location = new Location(x+i, y-i);
                if (!helper(pieces, location, res, otherColor)) {upRight = false;}
            }

            // up and left
            if (upLeft) {
                location = new Location(x-i, y-i);
                if (!helper(pieces, location, res, otherColor)) {upLeft = false;}
            }

            // down and right
            if (downRight) {
                location = new Location(x+i, y+i);
                if (!helper(pieces, location, res, otherColor)) {downRight = false;}
            }

            // down and left
            if (downLeft) {
                location = new Location(x-i, y+i);
                if (!helper(pieces, location, res, otherColor)) {downLeft = false;}
            }
        }

        if (lookForCheck) {
            res = findChecks(res, piece, king, pieces, x, y);
        }
        this.moves = res;
        return res;
        
    }   

    private ArrayList<Location> rookMoves(ArrayList<Piece> pieces, Piece piece, Piece king, 
        boolean lookForCheck, ArrayList<Location> res, String otherColor) {
            /* purpose
             * find the moves of the rook, which travels
             * on the non-diagonols, until it goes out of bounds,
             * or interacts with another piece
             */
            Location location;
            boolean up, down, left, right;
            up = down = left = right = true;
            for (int i = 1; i <= 7; i++) {
                // up
                if (up) {
                    location = new Location(x, y-i);
                    if (!helper(pieces, location, res, otherColor)) {up = false;}
                }
                // down
                if (down) {
                    location = new Location(x, y+i);
                    if (!helper(pieces, location, res, otherColor)) {down = false;}
                }
                // right
                if (right) {
                    location = new Location(x+i, y);
                    if (!helper(pieces, location, res, otherColor)) {right = false;}
                }
                // left
                if (left) {
                    location = new Location(x-i, y);
                    if (!helper(pieces, location, res, otherColor)) {left = false;}
                }
            }
            if (lookForCheck) {
                res = findChecks(res, piece, king, pieces, x, y);
            }
            this.moves = res;
            return res;
        }

    private ArrayList<Location> queenMoves(ArrayList<Piece> pieces, Piece piece, Piece king, 
        boolean lookForCheck, ArrayList<Location> res, String otherColor) {
        // queen's moves are a combination of rook and bishop moves, diagonols and non-diagonols
        bishopMoves(pieces, piece, king, lookForCheck, res, otherColor);
        rookMoves(pieces, piece, king, lookForCheck, res, otherColor);
        this.moves = res;
        return res;
    }

    private ArrayList<Location> kingMoves(ArrayList<Piece> pieces, Piece piece, Piece king, 
        boolean lookForCheck, ArrayList<Location> res, String otherColor) {
        // king's moves are all the queen's moves, but only for one square
        // also want to find if the king can castle or not, then add the moves accordingly
        queenMoves(pieces, piece, king, lookForCheck, res, otherColor);
        ArrayList<Location> toRemove = new ArrayList<Location>();
        for (Location location: res) {
            if (distance(location, new Location(this.x, this.y)) >= 1.5) {
                toRemove.add(location);
            }
        }
        for (Location loc: toRemove) {res.remove(loc);}
        
        if (lookForCheck) {
            res = findChecks(res, piece, king, pieces, x, y);
            res = findCastleMoves(pieces, king, res);
        }
        
        this.moves = res;
        return res;
    }

    private ArrayList<Location> findCastleMoves (ArrayList<Piece> pieces, Piece king, ArrayList<Location> res) {
        /* purpose
         * figure out if the king can
         * castle, either queen or king side
         * 
         * flipping the board makes this so much easier
         */
        int x = this.x;
        int y = this.y;
        if (!king.hasMoved) {
            // because of App.flipBoard(), this changes to right and left instead of king and queen side
            boolean rightSideCastle = true;
            boolean leftSideCastle = true;
            for (int i = 1; i <= 2; i++) {
                if (isOccupied(pieces, new Location(x+i, this.y)) == null) {
                    this.x = x + i; // king side castle
                    if (inCheck(pieces, king)) {rightSideCastle = false;}
                } else {rightSideCastle = false;}
                    
                if (isOccupied(pieces, new Location(x-i, this.y)) == null) {
                    this.x = x - i; // queen side castle
                    if (inCheck(pieces, king)) {leftSideCastle = false;} 
                } else {leftSideCastle = false;}
                    
            }
            /*
             * for queen side castling, need to make sure 
             * there is another empty space that is vacant, 
             * so check both sides and see if there is a piece
             * that is not a rook and also hasn't moved
             */
            
            // checking right
            try {
                Piece rightPiece = isOccupied(pieces, new Location(this.x + 1, y));
                // if the piece is not a rook and has not moved
                if (!(rightPiece.type.equals("rook") && !rightPiece.hasMoved)) {rightSideCastle = false;}
            } catch (NullPointerException e) {}

            // checking left
            try {
                Piece leftPiece = isOccupied(pieces, new Location(this.x - 1, y));
                // if the left piece is not a rook and has not moved
                if (!(leftPiece.type.equals("rook") && !leftPiece.hasMoved)) {leftSideCastle = false;}
            } catch (NullPointerException e) {}
            
            this.x = x;
            this.y = y;

            // set the new moves
            if (rightSideCastle) {res.add(new Location(this.x + 2, y));}
            if (leftSideCastle) {res.add(new Location(this.x - 2, y));}  
        }
        return res;
    }

    private boolean helper(ArrayList<Piece> pieces, Location location, ArrayList<Location> res, String otherColor) {
        /* purpose
         * a simple helper function to see
         * if a corresponding move of a piece
         * given a new location is valid or not
         */
        Piece occupiedPiece;
        if (inBounds(location)) {
            occupiedPiece = isOccupied(pieces, location);
            if (occupiedPiece == null) {
                res.add(location);
                return true;
            }
            else if (occupiedPiece.color.equals(otherColor)) {
                res.add(location);
                return false;
            }
        }
        return false;
    }

    public Piece isOccupied(ArrayList<Piece> pieces, Location location) {
        /* purpose
         * checks to see if a piece occupies a square,
         * if so return that piece
         */
        for (Piece piece: pieces) {
            if (piece.x == location.x && piece.y == location.y) {
                return piece;
            }
        }
        return null;
    }

    public ArrayList<Location> findChecks(ArrayList<Location> res, Piece piece, Piece king, ArrayList<Piece> pieces, int x, int y) {
        /* purpose
         * for all the possible moves of the king,
         * look if there is a check, and if so, remove
         * that location from the list of possible moves
         */
        ArrayList<Location> toRemove = new ArrayList<Location>();
        
        for (Location location: res) {
            Piece occupiedPiece = isOccupied(pieces, location);
            
            if (occupiedPiece != null) {
                occupiedPiece.alive = false;
            }

            piece.x = location.x;
            piece.y = location.y;
            
            if (inCheck(pieces, king)) {
                toRemove.add(location);
            }
            try {
                occupiedPiece.alive = true; // occupied piece may be null
            } catch (NullPointerException e) {}
        }
        for (Location location: toRemove) {res.remove(location);}
        piece.x = x;
        piece.y = y;
        return res;
    }
    
    public boolean inCheck(ArrayList<Piece> pieces, Piece king) {
        /* purpose
         * see if a move causes check, 
         * do this by seeing all the opponents move
         * for all pieces
         */
        String otherColor = king.color.equals("white") ? "black": "white";
        for (Piece piece: pieces) {
            if (piece.color.equals(otherColor) && piece.alive) {
                if (piece.type.equals("pawn")) {piece.setMoves(pieces, piece, null, false, true);}
                else {piece.setMoves(pieces, piece, null, false);}
                for (Location location: piece.getMoves()) {
                    if (king.x == location.x && king.y == location.y) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean checkMate(ArrayList<Piece> pieces, String turn) {
        /* purpose
         * for any given turn, if there are no more turns for all
         * of the pieces, and the king is in check, then it is
         * checkmate, and the opponent wins
         */
        int counter = 0;
        for (Piece piece: pieces) {
            if (piece.color.equals(turn)) {
                piece.setMoves(pieces, piece, this, true);
                counter = counter + piece.getMoves().size();
            }
            
        }
        if (counter == 0 && this.check) return true;
        else return false;

    }

    public boolean staleMate(ArrayList<Piece> pieces, String turn) {
        /* purpose
         * for any given turn, check for a stalemate, if the 
         * now person's turn has any moves for any of its pieces, 
         * and the king is not in check, game over, it is a stalemate
         */
        int counter = 0;
        for (Piece piece: pieces) {
            if (piece.color.equals(turn)) {
                piece.setMoves(pieces, piece, this, true);
                counter = counter + piece.getMoves().size();
            }
        }
        if (counter == 0 && !this.check) return true;
        return false;
    }
}