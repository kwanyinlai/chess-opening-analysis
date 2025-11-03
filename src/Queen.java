import java.util.LinkedList;

public class Queen extends ChessPiece { // Specific implementation of the Queen's movement.

	public Queen(byte startX, byte startY, boolean isWhite) {
		super(startX, startY, isWhite);
		// TODO Auto-generated constructor stub
	}




	@Override
	public LinkedList<Coordinate> generateMoves(ChessPiece[][] chessBoard) { // Simply a combination of Rook and Bishop's movement.
		legalMoves.clear();
		generateBounds();


		int i = 1;
		while(i<=up && chessBoard[x][y+i]==null) {
			legalMoves.add(new Coordinate((x),(byte)(y+i)));
			i++;
		}

		if(i<=up && chessBoard[x][y+i]!=null && chessBoard[x][y+i].isWhite()!=isWhite) {
			legalMoves.add(new Coordinate((x),(byte)(y+i)));
		}
		i=1;
		while(i<=down && chessBoard[x][y-i]==null) {
			legalMoves.add(new Coordinate((x),(byte)(y-i)));
			i++;

		}
		if(i<=down && chessBoard[x][y-i]!=null && chessBoard[x][y-i].isWhite()!=isWhite) {
			legalMoves.add(new Coordinate((x),(byte)(y-i)));
		}
		i=1;
		while(i<=right && chessBoard[x+i][y]==null) {
			legalMoves.add(new Coordinate((byte)(x+i),(y)));
			i++;
		}
		if(i<=right && chessBoard[x+i][y]!=null && chessBoard[x+i][y].isWhite()!=isWhite) {
			legalMoves.add(new Coordinate((byte)(x+i),(y)));
		}
		i=1;
		while(i<=left && chessBoard[x-i][y]==null) {
			legalMoves.add(new Coordinate((byte)(x-i),(y)));
			i++;

		}
		if(i<=left && chessBoard[x-i][y]!=null && chessBoard[x-i][y].isWhite()!=isWhite) {
			legalMoves.add(new Coordinate((byte)(x-i),(y)));
		}
		i = 1;

		while(i<=Math.min(up,right) && chessBoard[x+i][y+i]==null) {
			legalMoves.add(new Coordinate((byte)(x+i),(byte)(y+i)));

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

			return "Q";
		}
		else {

			return "q";
		}
	}
}
