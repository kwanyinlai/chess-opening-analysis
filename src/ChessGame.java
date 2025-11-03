import java.util.LinkedList;

public class ChessGame { // ChessGame objecet to hold the name of the players and the moves played. Extensibility is provided
						 // to include metrics like player's ratings, date, etc.
	private Player whitePlayer;
	private Player blackPlayer;
	private LinkedList<Move> moves = new LinkedList<>();
	private String[] FENs;
	private String PGN;
	private ChessPiece[] pieces;


	public ChessGame(Player whitePlayer, Player blackPlayer, LinkedList<Move> moves, String pgn) {
		this.whitePlayer=whitePlayer;
		this.blackPlayer=blackPlayer;
		this.moves = moves;
		this.PGN=pgn;

	}

	public ChessPiece[] getPieces() {
		return pieces;
	}

	public LinkedList<Move> getMoves() {
		return moves;

	}

	public void setFEN(String[] FENs) {
		this.FENs=FENs;
	}

	public String[] getFEN() {
		return FENs;
	}

	public String getWhite() {
		return whitePlayer.getName();
	}

	public String getBlack() {
		return blackPlayer.getName();
	}

	public void setPieces(ChessPiece[] pieces) {
		this.pieces=pieces;

	}

	public String getPGN() {
		return PGN;
	}

}

