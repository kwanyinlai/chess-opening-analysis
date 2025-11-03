
import java.util.LinkedList;

public class Bishop extends ChessPiece{ // Specific implementation of Bishop generate moves

	public Bishop(byte startX, byte startY, boolean isWhite)  {
		super(startX, startY, isWhite);
	}


	@Override
	public LinkedList<Coordinate> generateMoves(ChessPiece[][] chessBoard) {
		legalMoves.clear();
		generateBounds();

		int i = 1;
		while(i<=Math.min(up,right) && chessBoard[x+i][y+i]==null) { // Iterate the chess board diagonally until a piece is encountered. If
			legalMoves.add(new Coordinate((byte)(x+i),(byte)(y+i))); // the piece can be captured (of the opposite color), add that to legal moves
			i++;
		}
		if(i<=Math.min(up, right) && chessBoard[x+i][y+i]!=null && chessBoard[x+i][y+i].isWhite()!=isWhite) {
			legalMoves.add(new Coordinate((byte)(x+i),(byte)(y+i)));
		}

		i=1;
		while(i<=Math.min(down,left) && chessBoard[x-i][y-i]==null) {
			legalMoves.add(new Coordinate((byte)(x-i),(byte)(y-i)));

			i++;
		}
		if(i<=Math.min(down, left) && chessBoard[x-i][y-i]!=null && chessBoard[x-i][y-i].isWhite()!=isWhite) {
			legalMoves.add(new Coordinate((byte)(x-i),(byte)(y-i)));
		}

		i=1;
		while(i<=Math.min(down,right) && chessBoard[x+i][y-i]==null) {
			legalMoves.add(new Coordinate((byte)(x+i),(byte)(y-i)));
			i++;
		}
		if(i<=Math.min(down, right) && chessBoard[x+i][y-i]!=null && chessBoard[x+i][y-i].isWhite()!=isWhite) {
			legalMoves.add(new Coordinate((byte)(x+i),(byte)(y-i)));
		}
		i=1;
		while(i<=Math.min(up,left) && chessBoard[x-i][y+i]==null) {
			legalMoves.add(new Coordinate((byte)(x-i),(byte)(y+i)));
			i++;
		}
		if(i<=Math.min(up, left) && chessBoard[x-i][y+i]!=null && chessBoard[x-i][y+i].isWhite()!=isWhite) {
			legalMoves.add(new Coordinate((byte)(x-i),(byte)(y+i)));
		}

		return legalMoves;
	}


	@Override
	public String toString() {
		if(isWhite) {

			return "B";
		}
		else {

			return "b";
		}
	}

}
