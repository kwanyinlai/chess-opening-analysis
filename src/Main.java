import java.io.IOException;
import java.util.LinkedList;

public class Main {
	private static String[] chessGames;
	private static int gameLimit = 1000;

	public static void main(String[] args) throws IOException{
		UserGUI.displayGUI();
	} // Main driver class to start program


	public static int getTotalGames() {
		return chessGames.length;
	}

	public static void analyzePGN() throws Exception {

		chessGames = PositionParser.separatePGNFile(gameLimit); //gameLimit indicates the maximum number of games that can be extracted


		for(int i = 0 ; i<chessGames.length ; i++) {

			String chessGame = chessGames[i];
			Stockfish.startEngine();
			ChessGame game;
			try {
				game = PositionParser.pgnToMoves(10, chessGame); // Analyze the first 10 moves associated with the chessGame
			} catch (UsernameException e) {
				// TODO Auto-generated catch block
				throw new UsernameException(); // If UsernameException is caught, pass it along up the call hierarchy
			}
			if(game==null) {
				throw new FileException();
			}
			PositionParser.movesToFEN(game);

			LinkedList<Float> evals= Stockfish.stockfishGame(game);
			ChessPiece[] pieces = game.getPieces();
			LinkedList<FlaggedMove> flag = MoveFlagger.flagMoves(evals, game, pieces);
			MoveFlagger.maintainPriorityQueue(flag);
			Stockfish.stopEngine();
			UserGUI.setProgress((double)(i)/(double)(chessGames.length-1));

		}





	}
}

