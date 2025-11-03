
import java.util.LinkedList;

public class Rook extends ChessPiece { // Specific implementation of rook's movement
	private boolean moved;


	public Rook(byte startX, byte startY, boolean isWhite, boolean moved)  {
		super(startX, startY, isWhite);
		this.moved=moved;
	}

	public void setMoved(boolean moved) {
		this.moved=moved; // Additional variable of keeping track of the rook has moved, which is used to check for castling criteria.
	}


	public void isMoved(boolean moved) {
		this.moved=moved;
	}
	public boolean getMoved() {
		return moved;
	}

	@Override
	public void move(Coordinate destination) { // Override the move function to include castling functionality.
		moved=true;
		ChessPiece[][] chessBoard = DisplayBoard.getDisplayBoard();
		setX(destination.getX());
		setY(destination.getY());
		//DisplayBoard.setIsWhiteMove(!isWhite);

		try {
			if((x+3)<8 && chessBoard[x+3][y]!=null) {
				((Rook) chessBoard[x+3][y]).isMoved(true);
			}
		} catch(Exception e) {

		}


		if(checkLegal(destination)) {
			chessBoard[x][y]=null;
			chessBoard[destination.getX()][destination.getY()]=this;


			if(isWhite) {
				DisplayBoard.setWhiteCastleK(true);
				DisplayBoard.setWhiteCastleQ(true);

			}
			else {
				DisplayBoard.setBlackCastleK(true);
				DisplayBoard.setBlackCastleQ(true);
			}
		}
		else {
			chessBoard=DisplayBoard.getDisplayBoard();
			return;

		}
		DisplayBoard.setDisplayBoard(chessBoard);
		DisplayBoard.printBoard(chessBoard);

		if(!isWhite) {
			DisplayBoard.incrementMoves();
		}



	}

	@Override
	public LinkedList<Coordinate> generateMoves(ChessPiece[][] chessBoard) {
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

		// Iterate through the chess board cardinlly until a piece/edge of board is reached. If a piece,
		// check if it can be captured (of the opposite color)
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

		if((isWhite && DisplayBoard.isWhiteCastleQ())||(!isWhite && DisplayBoard.isBlackCastleQ())) {
			if(x-2>=0) {
				legalMoves.add(new Coordinate((byte)(x-2),y));
			}
		}
		if((isWhite && DisplayBoard.isWhiteCastleK())||(!isWhite && DisplayBoard.isBlackCastleK())) {
			if((x+3)<=7) {
				legalMoves.add(new Coordinate((byte)(x+3),y));
			}
		}

		return legalMoves;
	}

	@Override
	public String toString() {
		if(isWhite) {

			return "R";
		}
		else {

			return "r";
		}
	}
}
