import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class PositionParser {

	private LinkedList<Coordinate> moves;
	private static File pgnFile;


	public static File getPGN() {
		return pgnFile;
	}

	public static void setPGNFile(File file) {
		pgnFile=file;
	}

	public void addMove(Coordinate move) {
		moves.add(move);
	}

	// Constructors, getters and setters.

	public static Move chessNotationConverter(String move, ChessPiece[][] chessBoard, boolean isWhite) {

		Class<?> pieceType = null;  // Declare a local variable of type wildcard, which will be used the store the class of the piece.
		Coordinate coordinate = null;
		int p = -1; // Declaring a local temporary variable. The naming 'p' and also below 'k' was selected arbitrarily to avoid duplicate
		// local variable of i and j. Using 'x' and 'y' would be confusing as they would incorectly suggest that they represent
		// x and y coordinates. Initializing with the value -1 ensures that errors are caught.

		if(move.length()!=0) { // Ensuring that the String which is passed into the function is valid.

			if(Character.isLowerCase(move.charAt(0))) {
				pieceType=Pawn.class;
				move="P"+move;
			} // If the first character doesn't include an explicit reference to the class, then it is by default a pawn.

			else {

				switch(move.charAt(0)){
				case 'N':
					pieceType=Knight.class;
					break;
				case 'K':
					pieceType=King.class;
					break;
				case 'Q':
					pieceType=Queen.class;
					break;
				case 'B':
					pieceType=Bishop.class;
					break;
				case 'R':
					pieceType=Rook.class;
					break;

				case 'O': // Piece type null indicates the move is castling. The move involves both Rook and King so it is given its own unqiue identifier.
					pieceType=null;
					break;
				}

				// Switch statement to determine the piece's class.
			}

			int q = 1; // Pointer q is used to move along the String.

			if(move.length()>=3) {
				if(Character.isLetter(move.charAt(1)) && Character.isLetter(move.charAt(2))) {
					q++;
					switch (move.charAt(1)){
					case 'a':
						p=8;
						break;
					case 'b':
						p=9;
						break;
					case 'c':
						p=10;
						break;
					case 'd':
						p=11;
						break;
					case 'e':
						p=12;
						break;
					case 'f':
						p=13;
						break;
					case 'g':
						p=14;
						break;
					case 'h':
						p=15;
						break;

					} // Converting from alphabetical to numerical notation to be compatible with 2D array. This is set to start from 8 onwards to indicate
					// that the k value has been converted from an alphabetical notation; this is to differentiate moves like Nfe4 (where f acts as an indicator
					// to the source x square) and R1e4 (where 1 indicates the source square). In both scenarios, k would be set to 'f' and '1' and serve similar
					// but slightly different purposes.

				}


			}
			switch (move.charAt(q)){
			case 'a':
				coordinate=new Coordinate((byte) 0, (byte)((Character.getNumericValue(move.charAt(q+1)))-1));
				break;
			case 'b':
				coordinate=new Coordinate((byte) 1, (byte)((Character.getNumericValue(move.charAt(q+1)))-1));
				break;
			case 'c':
				coordinate=new Coordinate((byte) 2, (byte)((Character.getNumericValue(move.charAt(q+1)))-1));
				break;
			case 'd':
				coordinate=new Coordinate((byte) 3, (byte)((Character.getNumericValue(move.charAt(q+1)))-1));
				break;
			case 'e':
				coordinate=new Coordinate((byte) 4, (byte)((Character.getNumericValue(move.charAt(q+1)))-1));
				break;
			case 'f':
				coordinate=new Coordinate((byte) 5, (byte)((Character.getNumericValue(move.charAt(q+1)))-1));
				break;
			case 'g':
				coordinate=new Coordinate((byte) 6, (byte)((Character.getNumericValue(move.charAt(q+1)))-1));
				break;
			case 'h':
				coordinate=new Coordinate((byte) 7, (byte)((Character.getNumericValue(move.charAt(q+1)))-1));
				break;

			}
			// Using the x value, initialize coordinate variable

			if(pieceType!=null) { // If the type of move isn't castling...
				if(p!=-1) { // The x value is only set if the move length is 3, so anything larger than it will be handled separately (as they are usually special moves
					if(p>=8) { // p being greater than 8 indicates that we have been given additional information about the x cooridinate. p being less than 8 indicates information
						// about the y coordinate has instead been given.
						p-=8;
						for(int i = 0 ; i<8 ; i++) {
							if(pieceType.isInstance(chessBoard[p][i]) && chessBoard[p][i].isWhite()==isWhite &&
									chessBoard[p][i].generateMoves(chessBoard).contains(coordinate)) {
								return new Move(chessBoard[p][i], (coordinate.getX()), (coordinate.getY()), (byte)p, (byte)i);
							} // return moves using this additioanl information
						}
					}

					else {
						for(int i = 0 ; i<8 ; i++) {
							if(pieceType.isInstance(chessBoard[i][p]) && chessBoard[i][p].isWhite()==isWhite &&
									chessBoard[i][p].generateMoves(chessBoard).contains(coordinate)) {
								return new Move(chessBoard[i][p], (coordinate.getX()), (coordinate.getY()), (byte)i, (byte)p);
							}
						}
					}
				}
				else { // If we are not given additional information, brute force iterating across the whole array.
					for(int i = 0 ; i<8 ; i++) {
						for(int j = 0 ; j<8 ; j++) {
							if(pieceType.isInstance(chessBoard[i][j]) && chessBoard[i][j].isWhite()==isWhite &&
									chessBoard[i][j].generateMoves(chessBoard).contains(coordinate)) {
								Move piece = new Move(chessBoard[i][j],(coordinate.getX()), (coordinate.getY()), (byte) i, (byte) j);
								if(chessBoard[i][j] instanceof Pawn && i!=coordinate.getX() &&
										chessBoard[coordinate.getX()][coordinate.getY()]==null) { // Check if is a pawn that is playing enPassant. Notate this in the Move object
									// with the function .enPassant() which turns enPassant to true
									piece.enPassant();
								}
								return piece;
							}
						}
					}
				}

			}
			else { // If it is castling...
				if(move.length()==5) { // Move length of 5 indicates "O-O-O" which is queen side castling. Return code for queen side castling.
					if(isWhite) { // Changing depending on white or black.
						Move piece= new Move(chessBoard[4][0],(byte)(2), (byte)(0), (byte) 4, (byte) 0);
						piece.castle(false,true);
						return piece;
					}
					else {

						Move piece= new Move(chessBoard[4][7], (byte)(2), (byte)(7), (byte) 4, (byte) 7);
						piece.castle(false,true);
						return piece;
					}
				}
				else { // Move length !=5 indicates "O-O". Same applies.
					if(isWhite) {

						Move piece= new Move(chessBoard[4][0], (byte) 6,(byte) 0, (byte) 4, (byte) 0);
						piece.castle(true,false);
						return piece;
					}
					else {

						Move piece= new Move(chessBoard[4][7], (byte) 6,(byte) 7, (byte) 4, (byte) 7);
						piece.castle(true,false);
						return piece;
					}
				}

			}


		}

		return null; // Return null in case of errors and no move could be identified.

	}

	public static String[] separatePGNFile(int numGames) throws IOException{
		BufferedReader pgnFile = null;
		try {
			pgnFile = new BufferedReader(new FileReader(PositionParser.pgnFile)); // Read the file at the file location specified.
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String PGN=""; // Initialize an empty PGN string.
		String pgnLine;
		while((pgnLine=pgnFile.readLine())!=null) {
			PGN+=pgnLine+"\n"; // Read the PGN into separate lines

		}
		pgnFile.close(); // Close the file once it has been read.
		String[] PGNs = PGN.split("\n\n\n"); // Three line breaks indicate a separate PGN String/Chess Game so split these.
		return Arrays.copyOfRange(PGNs, 0, numGames); // Once all the PGN files have been read, return a number of PGN files correspodning with parameter numGames.

	}

	public static ChessGame pgnToMoves(int numMoves, String pgn) throws Exception {
		if(numMoves==0) { // numMoves = 0 means no limit to number of moves
			numMoves=999999999; // Arbitrarily large integer
		}
		else {
			numMoves=numMoves*2; // As moves comes in pairs, multiply the number by two.
		}

		String white=null;
		String black=null;
		// Initialize Strings to hold the name of the black and white player.
		boolean whiteMove=true;
		ChessPiece[][] chessBoard = new ChessPiece[8][8];
		chessBoard=DisplayBoard.startPos(chessBoard);
		// Initialize a chess board with the starting position.
		LinkedList<Move> moves = new LinkedList<>();
		if(pgn==null) {
			return null;
		}
		String[] lines = pgn.split("\n"); // Separate the PGN by lines.

		int j = 0;
		int k=0;
		while(j<lines.length && k<numMoves) {
			String line = lines[j];
			j++;
			if(line.contains("[White ")) { // if contains the following String, then indicates that the name / username of the white player is given.

				white = line.replaceAll("\\[White ", ""); // Replace the indicator of "[White" and only copy the username afterwards.
				white=white.replace("\"", "");
				white=white.replace("]", "");
				white=white.trim();
				//Remove unnecessary characters.
			}
			if(line.contains("[Black ")) { // Same as above.
				black = line.replaceAll("\\[Black ", "");
				black=black.replace("\"", "");
				black=black.replace("]", "");
				black=black.trim();
			}



			if((white!=null && !white.equals(Player.getUser())) && (black!=null && !black.equals(Player.getUser()))) { // Check if the name matches the user input, if not

				throw new UsernameException("Username doesn't match PGN"); // Creating my own exception to differentiate UsernameException from generic exceptions
				// so they can be handled differntly

			}

			line = line.replaceAll("\\[.*?\\]", ""); // Replace all unnecessary characters to isolate move string
			line = line.replaceAll("x", ""); // Ignore captures symbol
			line = line.replaceAll("\\+", ""); // Ignore check symbol
			line = line.replaceAll("\\#", ""); // Ignore checkmate symbol
			String[] moveLine=line.split(" "); // Split the lines into individual moves
			int i = 0;
			while(i<moveLine.length && k<numMoves) { // while loop to include the condition of how many moves can be accepted. k is an arbitrary variable name to avoid
				// duplicate local variable j
				String move=moveLine[i];
				i++;
				move=move.trim();
				if(move.equals("0-1") || move.equals("1-0") || move.equals("0.5-0.5")) {
					break;
				} // This string indicates the end of the game, so terminate loop here.

				if(move.matches("\\d+\\.") || move.length()==0 || move==""){
					continue; // Ignore move numbers
				}

				k++;

				if(whiteMove) {

					Move halfMove=PositionParser.chessNotationConverter(move, chessBoard, true);
					moves.add(halfMove); // Add move to list of moves.

					if(halfMove!=null) {
						ChessPiece piece = halfMove.getPiece();

						if(halfMove.getCastleKing()) { // Check for castling, and update Rook position if it is a castle.
							ChessPiece rook = chessBoard[7][halfMove.getSourceY()];
							if(rook!=null) {
								chessBoard[7][halfMove.getSourceY()]=null;
								chessBoard[5][halfMove.getSourceY()]=rook;

								if(rook!=null){
									rook.setX((byte)5);
								}
							}

						}
						else if(halfMove.getCastleQueen()) { // Same as above.
							ChessPiece rook = chessBoard[7][halfMove.getSourceY()];

							if(rook!=null) {
								chessBoard[0][halfMove.getSourceY()]=null;
								chessBoard[3][halfMove.getSourceY()]=rook;
								if(rook!=null){
									rook.setX((byte)4);
								}
							}

						}
						chessBoard[halfMove.getSourceX()][halfMove.getSourceY()]=null;
						chessBoard[halfMove.getX()][halfMove.getY()]=piece;
						if(piece!=null) {

							piece.setX(halfMove.getX());
							piece.setY(halfMove.getY());
						}
					}


				}
				else { // Same as above but for black pieces.
					Move halfMove=PositionParser.chessNotationConverter(move, chessBoard, false);
					moves.add(halfMove);
					if(halfMove!=null) {
						ChessPiece piece = halfMove.getPiece();

						if(halfMove.getCastleKing()) {
							ChessPiece rook = chessBoard[7][halfMove.getSourceY()];
							chessBoard[7][halfMove.getSourceY()]=null;
							chessBoard[5][halfMove.getSourceY()]=rook;
							if(rook!=null){
								rook.setX((byte)5);
							}

						}
						else if(halfMove.getCastleQueen()) {
							ChessPiece rook = chessBoard[7][halfMove.getSourceY()];
							chessBoard[0][halfMove.getSourceY()]=null;
							chessBoard[3][halfMove.getSourceY()]=rook;

							if(rook!=null){
								rook.setX((byte)4);
							}
						}
						else if (halfMove.canEnPassant()) {
							if(halfMove.getPiece().isWhite()) {
								chessBoard[halfMove.getX()][halfMove.getY()+1]=null;
							}
							else {
								chessBoard[halfMove.getX()][halfMove.getY()-1]=null;
							}
						}

						chessBoard[halfMove.getSourceX()][halfMove.getSourceY()]=null;
						chessBoard[halfMove.getX()][halfMove.getY()]=piece;
						piece.setX(halfMove.getX());
						piece.setY(halfMove.getY());
					}


				}

				whiteMove=!whiteMove; // Set white move to not white move (pass the move to the other color)

			}
		}
		String[] pgnOutput=pgn.split("\n");
		return new ChessGame(new Player(white), new Player(black), moves, pgnOutput[pgnOutput.length-1]);

	}

	public static void movesToFEN(ChessGame game) {

		LinkedList<Move> moves = game.getMoves();
		String[] FEN = new String[moves.size()];
		ChessPiece[] pieces = new ChessPiece[moves.size()];
		ChessPiece[][] chessBoard = new ChessPiece[8][8];
		chessBoard=DisplayBoard.startPos(chessBoard);
		Coordinate enPassant=null;
		boolean whiteCastleK=true;
		boolean whiteCastleQ=true;
		boolean blackCastleK=true;
		boolean blackCastleQ=true;
		boolean whiteToMove=false;
		int numMoves=0;
		int fiftyMoveRule=1;

		for(int i = 0 ; i<moves.size() ; i++) { // Iterate through list of moves, and using attributes of the move to convert it into a Position object incoporated with FEN.
			Move move = moves.get(i);

			if(move==null) {
				continue;
			}

			if(move.getCastleKing()) {
				ChessPiece rook =chessBoard[7][move.getSourceY()];
				chessBoard[5][move.getSourceY()]=rook;
				chessBoard[7][move.getSourceY()]=null;
			}
			else if(move.getCastleQueen()) {
				ChessPiece rook =chessBoard[0][move.getSourceY()];
				chessBoard[3][move.getSourceY()]=rook;
				chessBoard[0][move.getSourceY()]=null;

			}
			else if(move.getPiece() instanceof Pawn) { // Checking for en passant, and updating the fifty move rule because a pawn move has been made.
				fiftyMoveRule=0;
				if(move.canEnPassant()) {
					if(move.getPiece().isWhite()) {
						chessBoard[move.getX()][move.getY()+1]=null;
					}
					else {
						chessBoard[move.getX()][move.getY()-1]=null;
					}
				}
				else {
					int deltaY=move.getY()-move.getSourceY();
					if(deltaY==2) {
						enPassant = new Coordinate(move.getSourceX(),(byte) 3);
					}
					else if(deltaY==-2) {
						enPassant = new Coordinate(move.getSourceX(),(byte) 6);
					}
					else {
						enPassant=null;
					}
				}



			}
			else {
				enPassant=null;
			}
			chessBoard[move.getX()][move.getY()]=move.getPiece();
			if(chessBoard[move.getX()][move.getY()]!=null) {
				fiftyMoveRule=0; // Piece being captured leads to reset of fifty move rule
			}

			chessBoard[move.getSourceX()][move.getSourceY()]=null;

			if(chessBoard[4][0]!=null && chessBoard[4][0] instanceof King) { // Check if can castle (to add to the FEN)
				if(((King) chessBoard[4][0]).canCastleKingside(chessBoard)) {
					whiteCastleK=true;
				}
				else {
					whiteCastleK=false;
				}


				if(((King) chessBoard[4][0]).canCastleQueenside(chessBoard)) {
					whiteCastleQ=true;
				}
				else {
					whiteCastleQ=false;
				}
			}
			else if(chessBoard[4][7]!=null && chessBoard[4][7] instanceof King) {
				if(((King) chessBoard[4][7]).canCastleKingside(chessBoard)) {
					blackCastleK=true;
				}
				else {
					blackCastleK=false;
				}
				if(((King) chessBoard[4][7]).canCastleQueenside(chessBoard)) {
					blackCastleQ=true;
				}
				else {
					blackCastleQ=false;
				}
			}
			else { // If none of the above, set all castling to false.
				whiteCastleK=false;
				whiteCastleQ=false;
				blackCastleK=false;
				blackCastleQ=false;
			}



			Position pos = new Position(whiteToMove, chessBoard, enPassant, whiteCastleK, whiteCastleQ, blackCastleK, blackCastleQ,
					numMoves, fiftyMoveRule); // Using position object to generate a FEN (by using the function posToFEN)
			pieces[i] = move.getPiece(); // Used for higher-end GUI interaction and converting user moves into a String.





			FEN[i]=Position.posToFEN(pos);
			if(whiteToMove) {
				numMoves++;
			}
			fiftyMoveRule++;

			whiteToMove=!whiteToMove;



		}
		game.setPieces(pieces);
		game.setFEN(FEN);


	}



}








