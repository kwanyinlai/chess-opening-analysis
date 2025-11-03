import java.util.LinkedList;

public class Pawn extends ChessPiece { // Specific implementation of pawn movement.

	private boolean enPassant=false;

	public boolean isEnPassant() {
		return enPassant;
	}

	public void setEnPassant(boolean isEnPassant) {
		enPassant=isEnPassant;
	}

	public Pawn(byte startX, byte startY, boolean isWhite)  {
		super(startX, startY, isWhite);
	}



	@Override
	public LinkedList<Coordinate> generateMoves(ChessPiece[][] chessBoard) {
		legalMoves.clear();
		enPassant=false;
		generateBounds();
		if(isWhite) {
			if(y==1 && chessBoard[x][2]==null && chessBoard[x][3]==null) {
				legalMoves.add(new Coordinate(x,(byte)3));
			}
			if (up!=0 && chessBoard[x][y+1]==null) {
				if(y==7) {

				}
				else {
					legalMoves.add(new Coordinate(x,(byte)(y+1)));
				}
			}
			if (up!=0 && right!=0 && chessBoard[x+1][y+1]!=null && !chessBoard[x+1][y+1].isWhite()){
				legalMoves.add(new Coordinate((byte)(x+1),(byte)(y+1)));
			}
			if (DisplayBoard.getEnPassant()!=null && DisplayBoard.getEnPassant().equals(new Coordinate((byte)(x+1),(byte)(y+1)))) {
				legalMoves.add(DisplayBoard.getEnPassant());
				enPassant=true;
				System.out.println("test");

				// If 'en-passant'-ing, mark it down.
			}
			if (up!=0 && left!=0 && chessBoard[x-1][y+1]!=null && !chessBoard[x-1][y+1].isWhite()) {
				legalMoves.add(new Coordinate((byte)(x-1),(byte)(y+1)));
			}
			if (DisplayBoard.getEnPassant()!=null && DisplayBoard.getEnPassant().equals(new Coordinate((byte)(x-1),(byte)(y+1)))) {
				legalMoves.add(DisplayBoard.getEnPassant());
				enPassant=true;
			}
		}
		else {
			if (y==6 && chessBoard[x][5]==null && chessBoard[x][4]==null) {
				legalMoves.add(new Coordinate(x,(byte)4));
			}
			if (down!=0 && chessBoard[x][y-1]==null) {
				legalMoves.add(new Coordinate(x,(byte)(y-1)));
			}
			if (down!=0 && right!=0 && chessBoard[x+1][y-1]!=null && chessBoard[x+1][y-1].isWhite()) {
				legalMoves.add(new Coordinate((byte)(x+1),(byte)(y-1)));
			}
			if (down!=0 && left!=0 && chessBoard[x-1][y-1]!=null && chessBoard[x-1][y-1].isWhite()) {

				legalMoves.add(new Coordinate((byte)(x-1),(byte)(y-1)));
			}

			if (DisplayBoard.getEnPassant()!=null && DisplayBoard.getEnPassant().equals(new Coordinate((byte)(x-1),(byte)(y-1)))) {
				legalMoves.add(DisplayBoard.getEnPassant());
				enPassant=true; // If moving two squares, set en-passant to the square behind.
			}
			if (DisplayBoard.getEnPassant()!=null && DisplayBoard.getEnPassant().equals(new Coordinate((byte)(x+1),(byte)(y-1)))) {
				legalMoves.add(DisplayBoard.getEnPassant());

				enPassant=true;
			}

		}

		return legalMoves;
	}

	@Override
	public String toString() {
		if(isWhite) {

			return "P";
		}
		else {

			return "p";
		}
	}

}
