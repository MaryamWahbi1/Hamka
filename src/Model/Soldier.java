package Model;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import Controller.BoardController;
import Exceptions.IllegalMoveException;
import Exceptions.LocationException;
import Utils.Directions;
import Utils.PrimaryColor;

public class Soldier extends Piece{

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param color
	 * @param location
	 */
	public Soldier(int id,PrimaryColor color, Location location) {
		super(id, color, location);
	}



	@Override
	
	public boolean move(Tile targetTile,Directions direction) throws LocationException, IllegalMoveException {
		Board board = Board.getInstance();
		Location targetLocation = targetTile.getLocation();
		Directions legalDirection =getLocation().getRelativeDirection(targetLocation);
		if(legalDirection!=direction) {
			throw new IllegalMoveException("The move direction doesn't match the relative direction of target tile location which is "+ legalDirection);
		}
		
		if(this.isMoveLegal(targetLocation)) {
			if(Board.getInstance().canPieceMove(this, targetLocation, direction)) {
				Piece toEat = getEdiblePieceByDirection(targetTile.getLocation(), direction);
				if(toEat != null)
				{
					board.eat(this, toEat);
				}
				board.removePiece(this);
				if(targetLocation.isEndOfBoard()) {
					Queen newQueen= new Queen(getId(), getColor(), targetLocation);
					newQueen.setEatingCntr(this.getEatingCntr());
					board.addPiece(newQueen);
					Game.getInstance().getTurn().setLastPieceMoved(newQueen);
					System.out.println("Soldier upgraded to Queen !!");
					return true;
				}else {
					this.setLocation(targetLocation);
					board.addPiece(this);
					
					Game.getInstance().getTurn().setLastPieceMoved(this);
						
					return true;
				}
				
			}
		}else {
			throw new IllegalMoveException("Illegal Move!");
		}
		return false;
	}


	@Override
	public boolean isMoveLegal(Location targetLocation) {
		Board board = Board.getInstance();
		int targetRow = targetLocation.getRow();
		char targetCol= targetLocation.getColumn();

		Location currentPos = this.getLocation();
		int curRow = currentPos.getRow();
		char curCol= currentPos.getColumn();

		//rows info
		int rowLowerBound= 1;
		int rowUpperBound=board.getBoardSize();

		int row1Up= curRow+1;
		int row2Up=curRow+2;
		int row1Down=curRow-1;
		int row2Down=curRow-2;

		//column info
		int colLowerBound= board.getColumnLowerBound();
		int colUpperBound=board.getColumnUpperBound();

		int col1Right= (char)curCol+1;
		int col2Right=(char)curCol+2;
		int col1Left=(char)curCol-1;
		int col2Left=(char)curCol-2;



		if((targetRow==row1Up && row1Up<= rowUpperBound) || (targetRow==row1Down && row1Down>= rowLowerBound) ) {
			if((targetCol == col1Right && col1Right<=colUpperBound)|| (targetCol == col1Left && col1Left>=colLowerBound)) {
				return true;
			}
		}
		else if( (targetRow==row2Up &&  row2Up<= rowUpperBound) || (targetRow==row2Down && row2Down>= rowLowerBound) ) {
			if((targetCol == col2Right && col2Right<=colUpperBound) || (targetCol == col2Left && col2Left>=colLowerBound)) {
				return true;
			}
		}


		return false;
	}


	/**
	 * gets all edible pieces around this piece
	 * @param piece that we want to check
	 * @return list of pieces that are edible for specific piece, null if piece can't eat
	 */
	@Override
	public ArrayList<Piece> getMustEdiblePieces() {
		ArrayList<Piece> ediblePieces = new ArrayList<Piece>();
		Board board = Board.getInstance();
		Player currPlayer = Game.getInstance().getTurn().getCurrentPlayer();
		Location pieceLoc = this.getLocation();
		Location UL = null;
		Location UR = null;
		Location DL = null;
		Location DR = null;
		try {
			UL = pieceLoc.addToLocationDiagonally(Directions.UP_LEFT, 1);
			UR =pieceLoc.addToLocationDiagonally(Directions.UP_RIGHT, 1);
			DL =pieceLoc.addToLocationDiagonally(Directions.DOWN_LEFT, 1);
			DR =pieceLoc.addToLocationDiagonally(Directions.DOWN_RIGHT, 1);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			if((getEatingCntr()>=1 && currPlayer.getColor().equals(PrimaryColor.BLACK))
					|| currPlayer.getColor().equals(PrimaryColor.WHITE)) {
				if(UL!=null) {
					Piece p =board.getTileInLocation(UL).getPiece();
					if(p!=null) {
						if(!p.getColor().equals(currPlayer.getColor())) {
							Location jmpLoc = UL.addToLocationDiagonally(Directions.UP_LEFT, 1);
							if(jmpLoc!=null && board.getTileInLocation(jmpLoc).isEmpty()) {
								ediblePieces.add(p);
							}
						}
					}
				}
				if(UR!=null) {
					Piece p =board.getTileInLocation(UR).getPiece();
					if(p!=null) {
						if(!p.getColor().equals(currPlayer.getColor())) {
							Location jmpLoc = UR.addToLocationDiagonally(Directions.UP_RIGHT, 1);
							if(jmpLoc!=null && board.getTileInLocation(jmpLoc).isEmpty()) {
								ediblePieces.add(p);
							}
						}
					}
				}
			}
			if((getEatingCntr()>=1 && currPlayer.getColor().equals(PrimaryColor.WHITE))
					|| currPlayer.getColor().equals(PrimaryColor.BLACK)) {
				if(DL!=null) {
					Piece p =board.getTileInLocation(DL).getPiece();
					if(p!=null) {
						if(!p.getColor().equals(currPlayer.getColor())) {
							Location jmpLoc = DL.addToLocationDiagonally(Directions.DOWN_LEFT, 1);
							if(jmpLoc!=null && board.getTileInLocation(jmpLoc).isEmpty()) {
								ediblePieces.add(p);
							}
						}
					}
				}
				if(DR!=null) {
					Piece p =board.getTileInLocation(DR).getPiece();
					if(p!=null) {
						if(!p.getColor().equals(currPlayer.getColor())) {
							Location jmpLoc = DR.addToLocationDiagonally(Directions.DOWN_RIGHT, 1);
							if(jmpLoc!=null && board.getTileInLocation(jmpLoc).isEmpty()) {
								ediblePieces.add(p);
							}
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return ediblePieces;

	}

	
	@Override
	/**
	 * gets the piece that is going be eaten by soldier if it moves to direction
	 * 
	 * @param direction
	 * @return Piece that is going to be eaten by moving in direction, null if no piece was found
	 */
	public Piece getEdiblePieceByDirection( Directions direction) {
		// TODO Auto-generated method stub
		ArrayList<Piece> ediblePieces= getEdiblePieces();
		Location newLocal = null;
		try {
			newLocal = this.getLocation().addToLocationDiagonally(direction, 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//if location not out of boundaries
		if(newLocal!= null) {
			for (Piece p : ediblePieces) {
				if(p.getLocation().equals(newLocal)) {
					return p;
				}
			}
		}

		return null;
	}

	@Override
	/**
	 * 
	 * @param targetLocation
	 * @param direction
	 * @return Piece edible piece
	 */
	public Piece getEdiblePieceByDirection(Location targetLocation, Directions direction) {
		return getEdiblePieceByDirection(direction);
	}

	@Override
	public boolean canEatPiece(Piece targetPiece) {
		// TODO Auto-generated method stub
		Location pieceLoc = this.getLocation();
		Location targetPieceLoc = targetPiece.getLocation();
		Directions targetDir =pieceLoc.getRelativeDirection(targetPieceLoc);
		Piece legalTargetPiece = getEdiblePieceByDirection(targetDir);
		if(targetPiece.equals(legalTargetPiece)) {
			return true;
		}
		
		return false;
	}



	@Override
	public String toString() {
		return "Soldier-"+this.getColor()+" in "+getLocation();
	}



	@Override
	public ArrayList<Piece> getEdiblePieces() {
		return getMustEdiblePieces();
	}



	@Override
	public ArrayList<Tile> getPossibleMoves(PrimaryColor playerColor) {
		Board board= Board.getInstance();
		final Directions[] upDirections = {Directions.UP_LEFT,Directions.UP_RIGHT};
		final Directions[] downDirections = {Directions.DOWN_LEFT,Directions.DOWN_RIGHT};
		LinkedHashSet<Tile> possibleTileSet = new LinkedHashSet<Tile>();
		try {
			//adding tiles without eating
			Location pieceLocal = this.getLocation();
			Directions[] direc = null;
			if(playerColor == PrimaryColor.WHITE)
				direc = upDirections;
			else 
				direc = downDirections;

			for (Directions dir : direc) {
				Location tempLoc = pieceLocal.addToLocationDiagonally(dir, 1);

				if(tempLoc != null) {
					Tile locTile = board.getTileInLocation(tempLoc);
					if( locTile.isEmpty()) {
						possibleTileSet.add(locTile);
					} 
				}
			}
			Directions[] eat = null;
			if(playerColor == PrimaryColor.WHITE)
				eat = downDirections;
			else 
				eat = upDirections;

			//adding tiles with eating
			for (Directions dir :direc) {
				Piece ediblePiece=this.getEdiblePieceByDirection(dir);
				if(ediblePiece != null) {
					Location afterEatLoc=ediblePiece.getLocation().addToLocationDiagonally(dir, 1);
					if(afterEatLoc != null && board.getTileInLocation(afterEatLoc).isEmpty()) {
						possibleTileSet.add(board.getTileInLocation(afterEatLoc));
					}
				}
			}
			if(this.getEatingCntr()>=1) {
				for (Directions dir :eat) {
					Piece ediblePiece=this.getEdiblePieceByDirection(dir);
					if(ediblePiece != null) {
						Location afterEatLoc=ediblePiece.getLocation().addToLocationDiagonally(dir, 1);
						if(afterEatLoc != null && board.getTileInLocation(afterEatLoc).isEmpty()) {
							possibleTileSet.add(board.getTileInLocation(afterEatLoc));
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Tile>(possibleTileSet);
	}



	






}
