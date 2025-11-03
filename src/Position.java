public class Position { // Position object represents all the paramters of the position and its respect chess board

	private String currFEN;
	private ChessPiece[][] currBoard = new ChessPiece[8][8];
	private Coordinate enPassant;

	private boolean whiteCastleK;
	private boolean whiteCastleQ;
	private boolean blackCastleK;
	private boolean blackCastleQ;
	private boolean whiteToMove;
	private int numMoves;
	private int fiftyMoveRule;


	public Position(String FEN) {
		currFEN=FEN;

	}

	public String getCurrFEN() {
		return currFEN;
	}

	public ChessPiece[][] getCurrBoard() {
		return currBoard;
	}

	public Coordinate getEnPassant() {
		return enPassant;
	}

	public boolean isWhiteCastleK() {
		return whiteCastleK;
	}

	public boolean isWhiteCastleQ() {
		return whiteCastleQ;
	}

	public boolean isBlackCastleK() {
		return blackCastleK;
	}

	public boolean isBlackCastleQ() {
		return blackCastleQ;
	}

	public boolean isWhiteToMove() {
		return whiteToMove;
	}

	public int getNumMoves() {
		return numMoves;
	}

	public int getFiftyMoveRule() {
		return fiftyMoveRule;
	}

	public ChessPiece[][] getBoard(){
		return currBoard;
	}


	public Position(boolean whiteToMove, ChessPiece[][] chessBoard, Coordinate enPassant, boolean whiteCastleK,
			boolean whiteCastleQ, boolean blackCastleK, boolean blackCastleQ,
			int numMoves, int fiftyMoveRule) {

		currBoard=chessBoard;
		this.whiteToMove=whiteToMove;
		this.enPassant=enPassant;
		this.whiteCastleK=whiteCastleK;
		this.numMoves=numMoves;
		this.fiftyMoveRule=fiftyMoveRule;
		this.whiteCastleQ=whiteCastleQ;
		this.blackCastleK=blackCastleK;
		this.blackCastleQ=blackCastleQ;


	}

	public static Position FENtoPos(String FEN, ChessPiece[][] chessBoard) { // Enabling conversion of FEN strings to a Position object to be analysed
		int end=0;
		ChessPiece[][] tempBoard= chessBoard;
		boolean whiteToMove=true;
		boolean whiteCastleK=false;
		boolean blackCastleK=false;
		boolean blackCastleQ=false;
		boolean whiteCastleQ=false;
		int fiftyMove=0;
		int numMoves=0;
		Coordinate enPassant=null;
		Position pos;
		byte x = 0;
		byte y = 7;
		char temp;
		for (int i = 0; i<FEN.length();i++) {
			temp = FEN.charAt(i);
			char tempLower=Character.toLowerCase(temp);

			if(y>=0) {
				switch (tempLower){
				case 'p':
					if (temp=='P') {
						tempBoard[x][y]=new Pawn(x,y,true);
						x++;
						break;

					}
					else {
						tempBoard[x][y]=new Pawn(x,y,false);
						x++;
						break;
					}
				case 'n':
					if (temp=='N') {
						tempBoard[x][y]=new Knight(x,y,true);
						x++;
						break;
					}
					else {
						tempBoard[x][y]=new Knight(x,y,false);
						x++;
						break;
					}
				case 'b':
					if (temp=='B') {
						tempBoard[x][y]=new Bishop(x,y,true);
						x++;
						break;
					}
					else {
						tempBoard[x][y]=new Bishop(x,y,false);
						x++;
						break;
					}
				case 'q':
					if (temp=='Q') {
						tempBoard[x][y]=new Queen(x,y,true);
						x++;
						break;
					}
					else {
						tempBoard[x][y]=new Queen(x,y,false);
						x++;
						break;
					}
				case 'r':
					if (temp=='R') {
						tempBoard[x][y]=new Rook(x,y,true, true);
						x++;
						break;
					}
					else {
						tempBoard[x][y]=new Rook(x,y,false, true);
						x++;
						break;
					}

				case 'k':
					if (temp=='K') {
						tempBoard[x][y]=new King(x,y,true);

						x++;
						break;
					}
					else {
						tempBoard[x][y]=new King(x,y,false);
						x++;
						break;

					}
				case '/':
					y--;
					x=0;
					break;


				default:

					x+=Character.getNumericValue(tempLower);


				}
				if(x==8 && y==0) {
					y--;
				}

			}

			else {

				end=i;
				break;
			}
		}
		String[] remaining = FEN.substring(end).split(" ");

		for(int i = 0 ; i<remaining.length; i++) {
			int count=0;
			remaining[i]=remaining[i].trim();

			if(remaining[i].length()==0) {
				continue;
			}
			if(remaining[i].equals("w")) {
				whiteToMove=true;

			}
			else if(remaining[i].equals("b")) {

				whiteToMove=false;

			}
			else if(count==0) {

				if(remaining[i].length()!=0) {
					String[] castleString=remaining[i].split("");

					for (String element : castleString) {
						if(element.equals("K") && chessBoard[7][0]!=null) {
							whiteCastleK=true;
							if(chessBoard[7][0] instanceof Rook) {
								((Rook) chessBoard[7][0]).setMoved(false);
							}

						}
						else if(element.equals("Q")&& chessBoard[0][0]!=null) {
							whiteCastleQ=true;
							if(chessBoard[0][0] instanceof Rook) {
								((Rook) chessBoard[0][0]).setMoved(false);
							}
						}
						else if(element.equals("k")&& chessBoard[7][7]!=null) {
							blackCastleK=true;
							if(chessBoard[7][7] instanceof Rook) {
								((Rook) chessBoard[7][7]).setMoved(false);
							}
						}
						else if(element.equals("q")&& chessBoard[0][7]!=null) {
							blackCastleQ=true;
							if(chessBoard[0][7] instanceof Rook) {
								((Rook) chessBoard[0][7]).setMoved(false);
							}
						}
					}
				}

				else {
					count=1;
				}

			}
			else if(count==1) {
				if(!remaining[i].equals("-")) {
					enPassant=Coordinate.stringToCoord(remaining[i]);
				}
				count++;
			}
			else if(count==2) {
				fiftyMove=Integer.parseInt(remaining[i]);
				count++;
			}
			else if(count==3) {
				numMoves=Integer.parseInt(remaining[i]);
			}
		}


		pos = new Position(whiteToMove, tempBoard, enPassant, whiteCastleK, whiteCastleQ, blackCastleK, blackCastleQ,
				numMoves, fiftyMove);

		return pos;

	}

	public static String posToFEN(Position pos) { // Conversion of Position objects to a FEN string to allow for
		ChessPiece[][] chessBoard = pos.getBoard(); // analysis using the Stockfish engine.
		String FEN="";
		for(int j = 7; j>=0 ; j--) {
			int k=0; // k arbitrarily selected as local variable to travel along the FEN string.
			for(int i = 0 ; i<8 ; i++) {
				ChessPiece piece = chessBoard[i][j];
				if(piece!=null && k!=0) {
					FEN+=k;
					k=0;
				}
				if(piece instanceof Pawn) { // Add a character to the FEN string based on the chess board
					if(piece.isWhite()) {
						FEN+="P";
					}
					else {
						FEN+="p";
					}
				}
				else if(piece instanceof Knight) {
					if(piece.isWhite()) {
						FEN+="N";
					}
					else {
						FEN+="n";
					}
				}
				else if(piece instanceof Bishop) {
					if(piece.isWhite()) {
						FEN+="B";
					}
					else {
						FEN+="b";
					}
				}
				else if(piece instanceof Rook) {
					if(piece.isWhite()) {
						FEN+="R";
					}
					else {
						FEN+="r";
					}
				}
				else if(piece instanceof Queen) {
					if(piece.isWhite()) {
						FEN+="Q";
					}
					else {
						FEN+="q";
					}
				}
				else if(piece instanceof King) {
					if(piece.isWhite()) {
						FEN+="K";
					}
					else {
						FEN+="k";
					}
				}
				else if(piece == null) {
					k++;
				}
			}
			if(k!=0) {
				FEN+=k;
			}
			if(j!=0) {
				FEN+="/";
			}

		}
		FEN+=" ";
		if(pos.isWhiteToMove()) {
			FEN+="w";
		}
		else {
			FEN+="b";
		}

		FEN+=" ";

		boolean flag=false; // Adding additional String to the end of the FEN string based on castling availability, etc.
		if(pos.isWhiteCastleK()) {
			FEN+="K";
			flag=true;
		}
		if(pos.isWhiteCastleQ()) {
			FEN+="Q";
			flag=true;
		}
		if(pos.isBlackCastleK()) {
			FEN+="k";
			flag=true;
		}
		if(pos.isBlackCastleQ()) {
			FEN+="q";
			flag=true;
		}
		if(!flag) {
			FEN+="-";
		}
		FEN+=" ";

		if(pos.enPassant==null) {
			FEN+="-";
		}
		else {
			FEN+=Coordinate.coordToString(pos.getEnPassant());
		}

		FEN+=" ";

		FEN+=pos.getFiftyMoveRule();

		FEN+=" ";

		FEN+=pos.getNumMoves();


		return FEN;
	}


	public static Move deltaFEN(FlaggedMove move) { // Takes two FENs and sees the difference between the two FENs to see what move was made.
		ChessPiece[][] currBoard = new ChessPiece[8][8];
		ChessPiece[][] prevBoard = new ChessPiece[8][8];

		Position currPos=Position.FENtoPos(move.getCurrFEN(), currBoard); // Convert the FENs to Position objects
		currBoard = currPos.getBoard();
		Position prevPos=Position.FENtoPos(move.getPrevFEN(), prevBoard);
		prevBoard = prevPos.getBoard();

		Move returnValue=null;

		for(int i = 0 ; i<8 ; i++) {
			for(int j = 0 ; j<8 ; j++) {
				if(currBoard[i][j]==null && prevBoard[i][j]==null){
					continue;
				}
				else if((currBoard[i][j]==null && prevBoard!=null)) {
					continue;
				}
				else if	(prevBoard[i][j]==null && currBoard!=null){
					returnValue =  new Move(currBoard[i][j], (byte)0,(byte)0, (currBoard[i][j].getX()),(currBoard[i][j].getY()));
				}
				else if(!(currBoard[i][j].equals(prevBoard[i][j]))){
					return new Move(currBoard[i][j], (byte)0,(byte)0, (currBoard[i][j].getX()),(currBoard[i][j].getY()));
				}
			}
		} // Checks to see which piece moved off which square

		if(returnValue!=null) {
			return returnValue;
		}

		return null;

	}

	public static String stockfishToString(String stockfishRaw, String FEN) {
		// Convert a raw Stockfish output to a String for GUI presentation
		String output="";
		ChessPiece[][] board = new ChessPiece[8][8];
		Position position = Position.FENtoPos(FEN, board);
		board=position.getBoard();
		int x=0;
		switch(stockfishRaw.charAt(0)) {
		case 'a':
			x=0;
			break;
		case 'b':
			x=1;
			break;
		case 'c':
			x=2;
			break;
		case 'd':
			x=3;
			break;
		case 'e':
			x=4;
			break;
		case 'f':
			x=5;
			break;
		case 'g':
			x=6;
			break;
		case 'h':
			x=7;
			break;
		}


		int y = Character.getNumericValue(stockfishRaw.charAt(1))-1;




		if(board[x][y] instanceof Pawn) {
			output= stockfishRaw.substring(2);
		}
		else if(board[x][y] instanceof Knight) {
			output= "N"+stockfishRaw.substring(2);
		}
		else if(board[x][y] instanceof Bishop) {
			output= "B"+stockfishRaw.substring(2);
		}
		else if(board[x][y] instanceof Rook) {
			output= "R"+stockfishRaw.substring(2);
		}
		else if(board[x][y] instanceof Queen) {
			output= "Q"+stockfishRaw.substring(2);
		}

		else if(board[x][y] instanceof King) {
			output= "K"+stockfishRaw.substring(2);
		}
		return output;
		// POSSIBLE TO DO, ADD DISAMBIGUATION, CAPTURES FOR PAWNS, AND CASTLING
	}


	public static boolean FENisWhite(String FEN) {
		if(FEN.split(" ")[1]=="b") {
			return false;
		}

		return true;


	}

}













