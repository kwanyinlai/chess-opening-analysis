public class Move extends Coordinate{
	// Extension of the coordinate object to include the piece type, source square, and special moves like castling
	private ChessPiece piece;
	private boolean castleKing;
	private boolean castleQueen;
	private byte sourceX;
	private byte sourceY;
	private boolean enPassant;

	public void enPassant() {
		enPassant=true;
	}

	public boolean canEnPassant() {
		return enPassant;
	}


	public ChessPiece getPiece() {
		return piece;
	}

	public void setCastleKing(boolean castleKing) {
		this.castleKing=castleKing;
	}

	public void setCastleQueen(boolean castleQueen) {
		this.castleQueen=castleQueen;
	}
	public boolean getCastleKing() {
		return castleKing;
	}

	public boolean getCastleQueen() {
		return castleQueen;
	}

	public byte getSourceX() {
		return sourceX;
	}



	public byte getSourceY() {
		return sourceY;
	}


	public Move(ChessPiece piece, byte x, byte y, byte sourceX, byte sourceY) {
		super(x, y);
		this.piece=piece;
		this.sourceX=sourceX;
		this.sourceY=sourceY;

	}

	public void castle(boolean castleKing, boolean castleQueen) {
		this.castleKing=castleKing;
		this.castleQueen=castleQueen;
	}

	@Override
	public String toString() {
		String output="";
		if(piece instanceof Pawn) {
			output="";
		}
		else if(piece instanceof Knight) {
			output+="N";
		}
		else if(piece instanceof Bishop) {
			output+="B";
		}
		else if(piece instanceof Rook) {
			output+="R";
		}
		else if(piece instanceof Queen) {
			output+="Q";
		}
		else if(piece instanceof King) {
			output+="K";
		}
		if(sourceX==0) {
			output+="a";
		}
		else if(sourceX==1) {
			output+="b";
		}
		else if(sourceX==2) {
			output+="c";
		}
		else if(sourceX==3) {
			output+="d";
		}
		else if(sourceX==4) {
			output+="e";
		}
		else if(sourceX==5) {
			output+="f";
		}
		else if(sourceX==6) {
			output+="g";
		}
		else if(sourceX==7) {
			output+="h";
		}
		return output+(sourceY+1);

	}


}