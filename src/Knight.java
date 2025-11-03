
import java.util.LinkedList;

public class Knight extends ChessPiece {


	public Knight(byte startX, byte startY, boolean isWhite)  {
		super(startX, startY, isWhite);
	}


	@Override
	public LinkedList<Coordinate> generateMoves(ChessPiece[][] chessBoard){ // Generating movements for a knight
		generateBounds();
		legalMoves.clear();
		if(right>=2 && up>=1 && (chessBoard[x+2][y+1]==null || (chessBoard[x+2][y+1]!=null && chessBoard[x+2][y+1].isWhite()!=isWhite))) {
			legalMoves.add(new Coordinate((byte)(x+2),(byte)(y+1)));
		}
		if(right>=2 && down>=1 && (chessBoard[x+2][y-1]==null || (chessBoard[x+2][y-1]!=null && chessBoard[x+2][y-1].isWhite()!=isWhite))) {
			legalMoves.add(new Coordinate((byte)(x+2),(byte)(y-1)));
		}
		if(left>=2 && up>=1 && (chessBoard[x-2][y+1]==null || (chessBoard[x-2][y+1]!=null && chessBoard[x-2][y+1].isWhite()!=isWhite))) {
			legalMoves.add(new Coordinate((byte)(x-2),(byte)(y+1)));
		}
		if(left>=2 && down>=1 && (chessBoard[x-2][y-1]==null || (chessBoard[x-2][y-1]!=null && chessBoard[x-2][y-1].isWhite()!=isWhite))) {
			legalMoves.add(new Coordinate((byte)(x-2),(byte)(y-1)));
		}
		if(up>=2 && right>=1 && (chessBoard[x+1][y+2]==null || (chessBoard[x+1][y+2]!=null && chessBoard[x+1][y+2].isWhite()!=isWhite))) {
			legalMoves.add(new Coordinate((byte)(x+1),(byte)(y+2)));
		}
		if(up>=2 && left>=1 && (chessBoard[x-1][y+2]==null || (chessBoard[x-1][y+2]!=null && chessBoard[x-1][y+2].isWhite()!=isWhite))) {
			legalMoves.add(new Coordinate((byte)(x-1),(byte)(y+2)));
		}
		if(down>=2 && right>=1 && (chessBoard[x+1][y-2]==null || (chessBoard[x+1][y-2]!=null && chessBoard[x+1][y-2].isWhite()!=isWhite))) {
			legalMoves.add(new Coordinate((byte)(x+1),(byte)(y-2)));
		}
		if(down>=2 && left>=1 && (chessBoard[x-1][y-2]==null || (chessBoard[x-1][y-2]!=null && chessBoard[x-1][y-2].isWhite()!=isWhite))) {
			legalMoves.add(new Coordinate((byte)(x-1),(byte)(y-2)));
		}
		return legalMoves;
	}


	@Override
	public String toString() {
		if(isWhite) {

			return "N";
		}
		else {

			return "n";
		}
	}


}
