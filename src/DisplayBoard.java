public class DisplayBoard {
	private static Coordinate enPassant;
	private static boolean whiteCastleK;
	private static boolean whiteCastleQ;
	private static boolean blackCastleK;
	private static boolean blackCastleQ;
	private static int numMoves;
	private static ChessPiece[][] chessBoard  = new ChessPiece[8][8];
	private static King whiteKing;
	private static King blackKing;
	private static boolean isWhiteMove;

	// DisplayBoard class which simply holds all the necessary attributes for moves to be played across a virtual board.


	public static boolean isWhiteCastleK() {
		return whiteCastleK;
	}

	public static void setWhiteCastleK(boolean whiteCastleK) {
		DisplayBoard.whiteCastleK = whiteCastleK;
	}

	public static boolean isWhiteCastleQ() {
		return whiteCastleQ;
	}

	public static void setWhiteCastleQ(boolean whiteCastleQ) {
		DisplayBoard.whiteCastleQ = whiteCastleQ;
	}

	public static boolean isBlackCastleK() {
		return blackCastleK;
	}

	public static void setBlackCastleK(boolean blackCastleK) {
		DisplayBoard.blackCastleK = blackCastleK;
	}

	public static boolean isBlackCastleQ() {
		return blackCastleQ;
	}

	public static void setBlackCastleQ(boolean blackCastleQ) {
		DisplayBoard.blackCastleQ = blackCastleQ;
	}



	public static void incrementMoves() {
		numMoves++;
	}

	public static void setIsWhiteMove(boolean isWhiteMove) {
		DisplayBoard.isWhiteMove=isWhiteMove;
	}

	public static boolean isWhiteMove() {
		return isWhiteMove;
	}

	public static void setEnPassant(Coordinate enPassant) {
		DisplayBoard.enPassant=enPassant;
	}

	public static Coordinate getEnPassant() {
		return enPassant;
	}




	public static ChessPiece[][] getDisplayBoard() {
		return DisplayBoard.chessBoard;
	}

	public static void setDisplayBoard(ChessPiece[][] chessBoard) {
		DisplayBoard.chessBoard = chessBoard;
	}

	public static void setPosition(Position pos) {
		chessBoard=pos.getBoard();

		DisplayBoard.printBoard(chessBoard);
		DisplayBoard.setIsWhiteMove(pos.isWhiteToMove());

		if(isWhiteMove) {
			ChessPiece.setFlipBoard(false);
		}
		else {
			ChessPiece.setFlipBoard(true);
		}
		whiteCastleK=pos.isWhiteCastleK();
		blackCastleK=pos.isBlackCastleK();
		whiteCastleQ=pos.isWhiteCastleQ();
		blackCastleQ=pos.isBlackCastleQ();
	}



	public static void printBoard(ChessPiece[][] chessBoard) { // Used for debugging. Statically call to print out the entire board.
		String output = "";
		for(int i = 7 ; i>=0 ; i--) {
			String temp = "";
			for(int j = 0 ; j<8 ; j++) {
				if(chessBoard[j][i]!=null) {
					temp+=chessBoard[j][i];
					temp+=" ";
				}
				else {
					temp+="# ";
				}

			}
			output+=temp;
			output+="\n";
		}

		System.out.println(output);
	}






	public static ChessPiece[][] startPos(ChessPiece[][] chessBoard)  { // Taking a chessBoard as a parameter, populate it with pieces
																		// and other basic starting position parameters
		chessBoard[0][1]=new Pawn((byte)0,(byte)1,true);
		chessBoard[1][1]=new Pawn((byte)1,(byte)1,true);
		chessBoard[2][1]=new Pawn((byte)2,(byte)1,true);
		chessBoard[3][1]=new Pawn((byte)3,(byte)1,true);
		chessBoard[4][1]=new Pawn((byte)4,(byte)1,true);
		chessBoard[5][1]=new Pawn((byte)5,(byte)1,true);
		chessBoard[6][1]=new Pawn((byte)6,(byte)1,true);
		chessBoard[7][1]=new Pawn((byte)7,(byte)1,true);
		chessBoard[0][6]=new Pawn((byte)0,(byte)6,false);
		chessBoard[1][6]=new Pawn((byte)1,(byte)6,false);
		chessBoard[2][6]=new Pawn((byte)2,(byte)6,false);
		chessBoard[3][6]=new Pawn((byte)3,(byte)6,false);
		chessBoard[4][6]=new Pawn((byte)4,(byte)6,false);
		chessBoard[5][6]=new Pawn((byte)5,(byte)6,false);
		chessBoard[6][6]=new Pawn((byte)6,(byte)6,false);
		chessBoard[7][6]=new Pawn((byte)7,(byte)6,false);
		chessBoard[0][0]=new Rook((byte)0,(byte)0,true, false);
		chessBoard[7][0]=new Rook((byte)7,(byte)0,true, false);
		chessBoard[0][7]=new Rook((byte)0,(byte)7,false, false);
		chessBoard[7][7]=new Rook((byte)7,(byte)7,false, false);
		chessBoard[1][0]=new Knight((byte)1,(byte)0,true);
		chessBoard[6][0]=new Knight((byte)6,(byte)0,true);
		chessBoard[1][7]=new Knight((byte)1,(byte)7,false);
		chessBoard[6][7]=new Knight((byte)6,(byte)7,false);
		chessBoard[2][0]=new Bishop((byte)2,(byte)0,true);
		chessBoard[5][0]=new Bishop((byte)5,(byte)0,true);
		chessBoard[2][7]=new Bishop((byte)2,(byte)7,false);
		chessBoard[5][7]=new Bishop((byte)5,(byte)7,false);
		chessBoard[4][0]=new King((byte)4, (byte)0,true);
		chessBoard[4][7]=new King((byte)4, (byte)7,false);
		chessBoard[3][0]=new Queen((byte)3, (byte)0,true);
		chessBoard[3][7]=new Queen((byte)3, (byte)7,false);
		isWhiteMove=true;
		enPassant=null;
		whiteCastleK=true;
		whiteCastleQ=true;
		blackCastleK=true;
		blackCastleQ=true;
		numMoves=0;


		return chessBoard;
	}

	public static Coordinate getWhiteKing() { // The coordinate of the kings available for ease of access
		return new Coordinate(whiteKing.getX(),whiteKing.getY());
	}
	public static Coordinate getBlackKing() {
		return new Coordinate(blackKing.getX(),blackKing.getY());
	}

	public static void setWhiteKing(King king) {
		whiteKing=king;
	}
	public static void setBlackKing(King king) {
		blackKing=king;
	}



}
