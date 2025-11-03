import java.util.LinkedList;

public abstract class ChessPiece{
	protected boolean isWhite; //whether the piece belongs to white or black
	protected byte right;
	protected byte left;
	protected byte down;
	protected byte up;
	protected LinkedList<Coordinate> legalMoves;
	protected byte x;
	protected byte y;
	protected int displayX;
	protected int displayY;
	private static boolean flipBoard=false;

	public static ChessPiece getChessPiece(int mouseX, int mouseY) { // Return the chess piece at the given mouse position
		ChessPiece[][] chessBoard = DisplayBoard.getDisplayBoard();
		mouseY-=48; // Correcting for piece positioning so the piece drag is at the tip of the mouse
		for(int i = 0 ; i<8 ; i++) {
			for(int j = 0 ; j<8 ; j++) {
				if(chessBoard[i][j]!=null) {
					if((chessBoard[i][j].getDisplayX())<mouseX && mouseX<(chessBoard[i][j].getDisplayX()+64)
							&& ((chessBoard[i][j].getDisplayY()-32<mouseY && mouseY<(chessBoard[i][j].getDisplayY()+32)))){
						return chessBoard[i][j]; // Iterate through the entire 2D array.
					}
				}
			}
		}
		return null;
	}



	public byte getX() {
		return x;
	}

	public int getDisplayX() {

		return displayX;
	}

	public int getDisplayY() {

		return displayY;
	}

	public byte getY() {
		return y;
	}

	// Getters and setters

	public void setX(byte x)  {
		if(x<0 || x>7) {
			System.out.println("x must be between 0 and 7");

			System.out.println("x is" +x);
		}
		else{

			this.x=x; // Updating the x position (in the array) updates where it is displayed on the board.
			if(flipBoard) {
				displayX=(7-x)*64;
			}
			else {
				displayX=(x)*64;
			}

		}
	}

	public void setDisplayX(int x) {
		if(0<x && x<480) {
			this.displayX=x;
		}
	}
	public void setDisplayY(int y) {
		if(0<y && y<480) {
			this.displayY=y;

		}
	}

	public void setY(byte y)  {
		if(y<0 || y>7) {
			System.out.println("y must be between 0 and 7");
		}
		else{
			this.y=y;
			if(flipBoard) {
				displayY=y*64;
			}
			else {
				displayY=(7-y)*64;
			}
		}
	}




	public void generateBounds() {
		right = (byte)(7-x);
		left = (byte)(7-right);
		up = (byte)(7-y);
		down = (byte)(7-up);
	}  // Generate the bounds (how far the piece is from the outer bounds of the chess board.


	public ChessPiece(byte x, byte y, boolean isWhite) {
		this.x=x;
		this.y=y;
		if(flipBoard) {
			displayX=(7-x)*64;
			displayY=y*64;
		}
		else {
			displayY=(7-y)*64;
			displayX=x*64;

		}


		legalMoves = new LinkedList<>();
		this.isWhite=isWhite;
		generateBounds();
	}

	public boolean isWhite() {
		return isWhite;
	}

	public void setWhite(boolean isWhite) {
		this.isWhite = isWhite;
	}



	public void move(Coordinate destination) { // Move function which updates the 2D array chess board


		ChessPiece[][] chessBoard = DisplayBoard.getDisplayBoard();
		chessBoard[x][y]=null;

		// Copy the static DiplsyBoard.chessBoard into chessBoard

		if(this instanceof Pawn) { // Implement the specific mechanics of pawn

			Pawn pawn = (Pawn) this; // As ChessPiece is the parent class, must be cast to a pawn class. Checks if instanceof Pawn before casting.
			if(pawn.isEnPassant()) {
				if(isWhite) {
					chessBoard[destination.getX()][4]=null;
				}
				else {
					chessBoard[destination.getX()][3]=null;
				}
				pawn.setEnPassant(false);
			}
			else if((destination.getY()-y)==2) { // If the pawn moves two squares up the board, set a square to be enPassanted on
				// regardless of whether it can be 'en-passant'-ed
				DisplayBoard.setEnPassant(new Coordinate(x,(byte)(y+1)));
			}
			else if((y-destination.getY())==2) {
				DisplayBoard.setEnPassant(new Coordinate(x,(byte)(y-1)));
			}
		}
		else {
			DisplayBoard.setEnPassant(null);
		}



		setX(destination.getX());
		setY(destination.getY());


		chessBoard[destination.getX()][destination.getY()]=this;


		DisplayBoard.setDisplayBoard(chessBoard);
		DisplayBoard.printBoard(chessBoard);
		if(!isWhite) {
			DisplayBoard.incrementMoves(); // When moving, increment number of moves if the move is by black (effectively, once every two moves)
		}
		// Need FIXING 要改正
		//System.out.println("Mate="+checkMate(chessBoard));


	}

	public boolean checkMate(ChessPiece[][] chessBoard) {
		boolean flag=false;


		for(int x = 0 ; x<8 ; x++) {
			for(int y = 0 ; y<8 ; y++) {
				ChessPiece piece=chessBoard[x][y];

				if(piece!=null){
					LinkedList<Coordinate> moves = new LinkedList<>();
					moves=piece.generateMoves(chessBoard);



					if(moves.size()!=0) {


						for (Coordinate element : moves) {
							System.out.println(element);
							if(piece.violateCheck(element)){

								flag=true;
							}
						}
					}
				}
			}
		}

		if(!flag){
			if(King.inCheck(chessBoard, !isWhite)){
				return true;
			}
		}

		return false;

	}

	public boolean violateCheck(Coordinate destination) {
		ChessPiece[][] tempBoard=DisplayBoard.getDisplayBoard().clone();



		if(King.inCheck(tempBoard, isWhite)) {

			return true;
		}
		return false;
	}


	public boolean checkLegal(Coordinate destination) { // Checking for legal move.
		ChessPiece[][] chessBoard=DisplayBoard.getDisplayBoard();

		Pawn enPassantPawn=null;
		generateBounds(); // Updating bounds everytime



		if(generateMoves(chessBoard).contains(destination)) {
			// Check if the destination trying to be moved to is

			byte tempX=x;
			byte tempY=y;
			ChessPiece tempPiece = null;
			byte tempX2=destination.getX();
			byte tempY2=destination.getY();
			if(chessBoard[destination.getX()][destination.getY()]!=null) {
				tempPiece=chessBoard[destination.getX()][destination.getY()];
			}
			else if(this instanceof Pawn) {
				Pawn pawn = (Pawn) this;
				if(pawn.isEnPassant()) {
					if(isWhite) {
						enPassantPawn=pawn;
						chessBoard[destination.getX()][4]=null;
					}
					else {
						enPassantPawn=pawn;
						chessBoard[destination.getX()][3]=null;
					}
				}

			}

			chessBoard[x][y]=null;
			chessBoard[destination.getX()][destination.getY()]=this;
			setX(destination.getX());
			setY(destination.getY());





			if(King.inCheck(chessBoard, isWhite)) {
				setX(tempX);
				setY(tempY);
				if(tempPiece!=null) {
					tempPiece.setX(tempX2);
					tempPiece.setY(tempY2);
					chessBoard[tempX2][tempY2]=tempPiece;
					chessBoard[tempX][tempY]=this;
				}
				return false;
			}

			if(this instanceof King && Math.abs(destination.getX()-x)==2) {
				if(isWhite) {
					if(chessBoard[6][0]!=null && chessBoard[6][0] instanceof King && !(King.inCheck(chessBoard, true))) {
						x=tempX;
						y=tempY;
						if(tempPiece!=null) {
							tempPiece.setX(tempX2);
							tempPiece.setY(tempY2);
						}
						return true;
					}
				}
				else {
					if(chessBoard[6][7]!=null && chessBoard[6][7] instanceof King && !(King.inCheck(chessBoard, false))) {
						x=tempX;
						y=tempY;
						if(tempPiece!=null) {
							tempPiece.setX(tempX2);
							tempPiece.setY(tempY2);
						}
						return true;
					}
				}

				if(tempPiece!=null) {

					tempPiece.setX(tempX2);
					tempPiece.setY(tempY2);
				}
				else if (enPassantPawn!=null) {
					chessBoard[enPassantPawn.getX()][enPassantPawn.getY()]=enPassantPawn;
				}
				setX(tempX);
				setY(tempY);
				chessBoard[tempX][tempY]=this;
				chessBoard[tempX2][tempY2]=tempPiece;
			}
			else {
				setX(tempX);
				setY(tempY);
				chessBoard[tempX][tempY]=this;
				return true;
			}
		}

		return false;
	}

	public abstract LinkedList<Coordinate> generateMoves(ChessPiece[][] chess_board);
	// Abstract method of 'generateMoves' to be implemented with the specifc movement of the pieces


	public static boolean isFlipBoard() {
		return flipBoard;
	}



	public static void setFlipBoard(boolean flipBoard) {
		ChessPiece.flipBoard = flipBoard;
	}


	@Override
	public String toString() {
		if(this instanceof King) {
			return "King";
		}
		else if (this instanceof Rook) {
			return "Rook";
		}
		else if (this instanceof Pawn) {
			return "Pawn";
		}
		else if (this instanceof Knight) {
			return "Knight";
		}
		else if (this instanceof Queen) {
			return "Queen";
		}
		else if (this instanceof Bishop) {
			return "Bishop";
		}
		else {
			return "nope";
		}
	}

	//move the piece depending on piece type

	@Override
	public boolean equals(Object obj) {
		ChessPiece piece = (ChessPiece)obj;
		if(piece.getX()==x && piece.getY()==y && piece.isWhite()==isWhite &&
				piece.getClass()==getClass()) {
			return true;
		}
		return false;
	}

}

