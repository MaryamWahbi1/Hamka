/**
 * 
 */
package Model;

import Utils.PrimaryColor;
import Utils.SeconderyTileColor;

/**
 * @author aseel
 *
 */
public class Tile implements Comparable<Tile>{
	//requiredParameters
	private Location location;
	private PrimaryColor color1;

	//optional parameters
	private SeconderyTileColor color2=null;
	private Piece piece = null;


	protected static abstract class Init<T extends Init<T>>{
		
		protected abstract T self();

		//requiredParameters
		protected Location location;
		protected PrimaryColor color1;

		//optional parameters
		private SeconderyTileColor color2=null;
		private Piece piece=null ;

//		/**
//		 * Constructor
//		 * @param location
//		 * @param color1
//		 */
//		public TileBuilder(Location location,PrimaryColor color1) {
//			this.location = location;
//			this.color1 = color1;	
//		}

		/**
		 * sets secondary color to tile
		 * @param color2 secondary color to set to tile
		 * @return TileBuilder
		 */
		public T setColor2(SeconderyTileColor color2) {
			this.color2 = color2;
			return self();
		}

		/**
		 * sets piece to tile
		 * @param piece to set to tile
		 * @return TileBuilder
		 */
		public T setPiece(Piece piece) {
			this.piece = piece;
			return self();
		}

		/**
		 * Build the the Tile instance
		 * @return Tile instance
		 */
		public Tile build() {
			return new Tile(this);
		}
	}

	//Builder class
	public static class Builder extends Init<Builder>{
		public Builder(Location location,PrimaryColor color1) {
			this.location = location;
			this.color1 = color1;	
		}
		@Override
		protected Builder self() {
			return this;
		}
	}
	
	/**
	 *  Constructor
	 * @param builder 
	 */
	protected Tile(Init<?> init) {
		this.location=init.location;
		this.color1 = init.color1;
		this.color2 = init.color2;
		this.piece = init.piece;
	}

	//	/**
	//	 * Constructor for regular tile without piece
	//	 * 
	//	 * @param location - Tile's location
	//	 * @param color1 - Tile's primary color - white or black
	//	 */
	//	public Tile(Location location, PrimaryColor color1) {
	//		super();
	//		this.location = location;
	//		this.color1 = color1;		
	//	}
	//
	//	/**
	//	 * Constructor for Special tile without piece
	//	 * 
	//	 * @param location - Tile's location
	//	 * @param color1 - Tile's primary color - white or black
	//	 * @param color2 - Tile's secondary color
	//	 */
	//	public Tile(Location location, PrimaryColor color1, SeconderyTileColor color2) {
	//		super();
	//		this.location = location;
	//		this.color1 = color1;
	//		this.color2 = color2;
	//
	//	}
	//
	//	/**
	//	 * Constructor for regular tile with piece
	//	 * 
	//	 * @param location
	//	 * @param color1
	//	 * @param piece
	//	 */
	//
	//	public Tile(Location location, PrimaryColor color1, Piece piece) {
	//		super();
	//		this.location = location;
	//		this.color1 = color1;		
	//		this.piece = piece;
	//	}
	//
	//	/**
	//	 * Constructor for Special tile with piece
	//	 * 
	//	 * @param location
	//	 * @param color1
	//	 * @param color2
	//	 * @param piece
	//	 */
	//	public Tile(Location location, PrimaryColor color1, SeconderyTileColor color2, Piece piece) {
	//		super();
	//		this.location = location;
	//		this.color1 = color1;
	//		this.color2 = color2;
	//		this.piece = piece;
	//	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tile other = (Tile) obj;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		return true;
	}

	@Override
	public int compareTo(Tile t) {
		// TODO Auto-generated method stub
		int result;
		if (this.location.getRow()==t.location.getRow()) {
			result = this.location.getColumn()-t.location.getColumn();
		}else {
			result = this.location.getRow()-t.location.getRow();
		}

		return result;
	}

	//setters & getters

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}



	/**
	 * @return the color1
	 */
	public PrimaryColor getColor1() {
		return color1;
	}



	/**
	 * @return the color2
	 */
	public SeconderyTileColor getColor2() {
		return color2;
	}



	/**
	 * @return the piece
	 */
	public Piece getPiece() {
		return piece;
	}

	/**
	 * 
	 * @param color2 secondary color
	 */
	public void setColor2(SeconderyTileColor color2) {
		this.color2 = color2;

	}

	/**
	 * 
	 * @param piece to set
	 */
	public void setPiece(Piece piece) {
		this.piece = piece;

	}

	//methods


	/**
	 * checks if this tile has piece on it or not
	 * @return true if tile has no piece , false otherwise
	 */
	public boolean  isEmpty() {

		if(this.piece==null) {
			return true;
		}
		return false;
	}



	@Override
	public String toString() {
		return "Tile [location=" + location + ", color1=" + color1 + ", color2=" + color2 + ", piece=" + piece + "]";
	}



}
