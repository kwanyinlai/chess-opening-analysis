
import java.util.LinkedList;

public class King extends ChessPiece {
	private boolean moved=false;
	public boolean canCastleKingside(ChessPiece[][] chessBoard) { // Checking if castling criteria are met

		if((isWhite && !moved && chessBoard[7][0]!=null && chessBoard[7][0] instanceof Rook && !((Rook) chessBoard[7][0]).getMoved())
				|| (!isWhite && !moved && chessBoard[7][7]!=null && chessBoard[7][7] instanceof Rook
				&& !((Rook) chessBoard[7][7]).getMoved())) {
			// See if rooks are still in the position of start position and if they have maade.

			for(int i = 0 ; i<8 ; i++) {
				for(int j = 0 ; j<8 ; j++) {
					if(chessBoard[i][j]!=null && chessBoard[i][j].isWhite()!=isWhite
							&& !(chessBoard[i][j] instanceof King)) {

						LinkedList<Coordinate> moves = chessBoard[i][j].generateMoves(chessBoard);
						if(moves.contains(new Coordinate((byte)(x+1),y))
								|| moves.contains(new Coordinate((byte)(x+2),y))
								|| moves.contains(new Coordinate(x,y))) {
							// Check whether the squares being castled through are being attacked by a piece

							return false;
						}
					}
				}
			}
			// If no pieces attack the square being castled through, return true
			return true;

		}

		return false;
	}


	public boolean canCastleQueenside(ChessPiece[][] chessBoard) { // Same as above but for castlingQueenside
		if((isWhite && !moved && chessBoard[0][0]!=null && chessBoard[0][0] instanceof Rook
				&& !((Rook) chessBoard[0][0]).getMoved())
				|| (!isWhite && !moved && chessBoard[0][7]!=null && chessBoard[0][7] instanceof Rook
				&& !((Rook) chessBoard[0][7]).getMoved())) {
			for(int i = 0 ; i<8 ; i++) {
				for(int j = 0 ; j<8 ; j++) {
					if(chessBoard[i][j]!=null && chessBoard[i][j].isWhite()!=isWhite) {
						if(!(chessBoard[i][j] instanceof King)) {
							LinkedList<Coordinate> moves = chessBoard[i][j].generateMoves(chessBoard);
							if(moves.contains(new Coordinate((byte)(x-1),y))
									|| moves.contains(new Coordinate((byte)(x-2),y))
									|| moves.contains(new Coordinate(x,y))) {
								return false;
							}
						}

					}
				}

			}

			return true;

		}
		return false;
	}


	@Override
	public void move(Coordinate destination) { // Overriding the ChessPiece.move() to ensure castling can occur; when castling, the King object
											   // moves the Rook as well.
		ChessPiece[][] chessBoard = DisplayBoard.getDisplayBoard();
		int deltaX=(destination.getX()-x);
		chessBoard[x][y]=null;
		setX(destination.getX());
		setY(destination.getY());
		if(!isWhite) {
			DisplayBoard.incrementMoves();
		}

		chessBoard[destination.getX()][destination.getY()]=this;
		if(deltaX==-2) { // King can only move one square at a time, so if they move horizontally two square, should check for castling.
			chessBoard[x-2][y].setX((byte)(x+1));
			chessBoard[x+1][y]=chessBoard[x-2][y];
			chessBoard[x-2][y]=null;
		}
		else if(deltaX==2) {
			chessBoard[x+1][y].setX((byte)(x-1));
			chessBoard[x-1][y]=chessBoard[x+1][y];
			chessBoard[x+1][y]=null;
		}
		if(this instanceof King) {
			moved=true;
		}



		if(isWhite) { // If the king is moved, regardless of whether castling or moved, then check that they can no longer castle.
			if(DisplayBoard.isWhiteCastleK() || DisplayBoard.isWhiteCastleQ()) {
				DisplayBoard.setWhiteCastleK(false);
				DisplayBoard.setWhiteCastleQ(false);
			}
		}
		else {
			if(DisplayBoard.isBlackCastleK() || DisplayBoard.isBlackCastleQ()) {
				DisplayBoard.setBlackCastleK(false);
				DisplayBoard.setBlackCastleQ(false);
			}
		}

		//DisplayBoard.setIsWhiteMove(!isWhite);
		DisplayBoard.setDisplayBoard(chessBoard);
		DisplayBoard.printBoard(chessBoard);

		if(!isWhite) {
			DisplayBoard.incrementMoves();
		}

	}



	public King(byte startX, byte startY, boolean isWhite) {
		super(startX,startY,isWhite);
		if(isWhite) {
			DisplayBoard.setWhiteKing(this);
		}
		else {
			DisplayBoard.setBlackKing(this);
		}
	}




	public static boolean inCheck(ChessPiece[][] chessBoard, boolean checkWhiteKing) {

		if(checkWhiteKing) { // Iterate through the chess board and find position of king
			Coordinate whiteKing=null;
			for(int i = 0 ; i<8 ; i++) {
				for(int j = 0 ; j<8 ; j++) {
					if(chessBoard[i][j] instanceof King && chessBoard[i][j].isWhite()) {
						whiteKing=new Coordinate(chessBoard[i][j].getX(), chessBoard[i][j].getY());
						break;
					}
				}
			} // Check if any pieces have a move which contains the coordinate the King (are attacking the King)
			for(int i = 0 ; i<8 ; i++) {
				for(int j = 0 ; j<8 ; j++) {
					if(chessBoard[i][j]!=null && !chessBoard[i][j].isWhite() && chessBoard[i][j].generateMoves(chessBoard).contains(whiteKing)) {

						return true;
					}
				}
			}
		}
		else {
			Coordinate blackKing=null;
			for(int i = 0 ; i<8 ; i++) {
				for(int j = 0 ; j<8 ; j++) {
					if(chessBoard[i][j] instanceof King && !chessBoard[i][j].isWhite()) {
						blackKing=new Coordinate(chessBoard[i][j].getX(),chessBoard[i][j].getY());
						break;
					}
				}
			}

			for(int i = 0 ; i<8 ; i++) {
				for(int j = 0 ; j<8 ; j++) {
					if(chessBoard[i][j]!=null && chessBoard[i][j].isWhite() && chessBoard[i][j].generateMoves(chessBoard).contains(blackKing)) {


						return true;

					}
				}
			}

		}

		return false;
	}


	@Override
	public LinkedList<Coordinate> generateMoves(ChessPiece[][] chessBoard) { // Generating moves for king
		legalMoves.clear();
		generateBounds();

		if(left!=0 && up!=0 && ((chessBoard[x-1][y+1]!=null && chessBoard[x-1][y+1].isWhite()!=isWhite) || chessBoard[x-1][y+1]==null)) {
			legalMoves.add(new Coordinate((byte)(x-1),(byte)(y+1)));
		}
		if(left!=0 && down!=0 && ((chessBoard[x-1][y-1]!=null && chessBoard[x-1][y-1].isWhite()!=isWhite)|| chessBoard[x-1][y-1]==null)) {
			legalMoves.add(new Coordinate((byte)(x-1),(byte)(y-1)));
		}
		if(right!=0 && up!=0 && ((chessBoard[x+1][y+1]!=null && chessBoard[x+1][y+1].isWhite()!=isWhite)|| chessBoard[x+1][y+1]==null))  {
			legalMoves.add(new Coordinate((byte)(x+1),(byte)(y+1)));
		}
		if(right!=0 && down!=0 && ((chessBoard[x+1][y-1]!=null && chessBoard[x+1][y-1].isWhite()!=isWhite)|| chessBoard[x+1][y-1]==null))  {
			legalMoves.add(new Coordinate((byte)(x+1),(byte)(y-1)));
		}
		if(left!=0 && ((chessBoard[x-1][y]!=null && chessBoard[x-1][y].isWhite()!=isWhite)|| chessBoard[x-1][y]==null))  {
			legalMoves.add(new Coordinate((byte)(x-1),y));
		}
		if(right!=0 && ((chessBoard[x+1][y]!=null && chessBoard[x+1][y].isWhite()!=isWhite)|| chessBoard[x+1][y]==null))  {
			legalMoves.add(new Coordinate((byte)(x+1),y));
		}
		if(down!=0 && ((chessBoard[x][y-1]!=null && chessBoard[x][y-1].isWhite()!=isWhite)|| chessBoard[x][y-1]==null))  {
			legalMoves.add(new Coordinate(x,(byte)(y-1)));
		}
		if(up!=0 && ((chessBoard[x][y+1]!=null && chessBoard[x][y+1].isWhite()!=isWhite) || chessBoard[x][y+1]==null)) {
			legalMoves.add(new Coordinate(x,(byte)(y+1)));
		}

		if(canCastleKingside(chessBoard) && chessBoard[x+1][y]==null && chessBoard[x+2][y]==null) {
			legalMoves.add(new Coordinate((byte)(x+2),y));
		}

		if(canCastleQueenside(chessBoard) && chessBoard[x-1][y]==null && chessBoard[x-2][y]==null && chessBoard[x-3][y]==null) {
			legalMoves.add(new Coordinate((byte)(x-2),y));


		}
		return legalMoves;
	}

	@Override
	public String toString() {
		if (isWhite) {return "K";}
		else { return "k";}
	}





}
