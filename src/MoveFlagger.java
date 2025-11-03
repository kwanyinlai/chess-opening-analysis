import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;

public class MoveFlagger {
	private static PriorityQueue<FlaggedMove> maxHeap = new PriorityQueue
			<>(Comparator.comparing(FlaggedMove::getKey, Comparator.reverseOrder()));
			// Priority queue in a heap-based structure for a more efficient maintaining of a data structure
			// to store FlaggedMoves to be popped when needed to generate a report. They will be ordered based
			// on their key.
	private static Map<Integer, FlaggedMove> flaggedMovePointers = new HashMap<>();
			// Additional flaggedMove Dictionary as a duplicate of the PriorityQueue as PriorityQueue
			// doesn't allow accessing specific flaggedMoves, meaning that incrementing frequency cannot be implemented
			//  unless a data structure holding pointers of the flaggedMoves is maintained.



	public MoveFlagger() {
		return;
	}

	public static LinkedList<FlaggedMove> flagMoves(LinkedList<Float> evals,
			ChessGame game, ChessPiece[] pieces) throws IOException{

		float prevEval=(float) 0.0;
		float eval;
		int start;
		if(game.getWhite().equals(Player.getUser())) {
			start=2; // Changing the start point of iterating depending on colour
		}
		else if(game.getBlack().equals(Player.getUser())) {
			start=3;
		}
		else {
			return null;
		} // Check whether white or black is the name given by the user. If neither, return null (which is converted to
		  // a UsernameException

		String[] FENs = game.getFEN();
		LinkedList<FlaggedMove> flaggedMoves = new LinkedList<>();
		for(int i = start ; i<evals.size()-1 ; i+=2) { // Iterate through the list of evaluations, looking for a change
													   // a change in eval of 0.25 or more.
			eval = (evals.get(i));
			prevEval=(evals.get(i-1));
			if(Math.abs(eval+prevEval)>=0.25) {
				FlaggedMove flagged = new FlaggedMove(FENs[i-1], FENs[i], (float)(0.0), pieces[i-1]);
				int hash = flagged.hashCode();
				if(flaggedMovePointers!=null && flaggedMovePointers.containsKey(hash)) {
					flaggedMovePointers.get(hash).incrementFreq();
					continue;
				} // Check if this FlaggedMove already exists, and if so, increment the frequency and break out of the current iteration



				float deeperCurr=Stockfish.stockfishMove(FENs[i]);
				float deeperPrev=Stockfish.stockfishMove(FENs[i-1]);

				// Perform a more in-depth analysis of the move once it has been preliminarily flagged, at a higher depth.

				if((Math.abs(deeperCurr+deeperPrev)>=0.5)) {
					flagged.setCurrEval(deeperCurr);
					flagged.setPrevEval(deeperPrev);
					flagged.setDeltaEval(Math.abs(deeperCurr+deeperPrev));
					flaggedMoves.add(flagged);
					// iterate through pgn and add sequence of moves
					String[] fullPGN=game.getPGN().split(" ");
					String pgnSnippet="";
					int count=0;
					int lineLength=0;
					for (String element : fullPGN) {
						if(element=="") {
							continue;
						}
						pgnSnippet+=element +" ";
						lineLength+=element.length()+1;

						count++;

						if(Character.isDigit(element.charAt(0))) {
							count--;

						}
						if(lineLength>=30) { //ensuring there is no repeat of line breaks for the smae count number
							pgnSnippet+="\n";
							lineLength=0;
						}

						if(count==i+1) { // fix this to not show the number in the file
							// SHOW CHECKMATE MESSAGE
							pgnSnippet+=element +" ";
							flagged.setPGN(pgnSnippet);

							break;
						}

					}

					flaggedMovePointers.put(hash, flagged); // If passes second round of screening, add to the list.



					continue;
				}
			}
		}
		return flaggedMoves;
	}

	public static void maintainPriorityQueue(LinkedList<FlaggedMove> flaggedMoves) {

		if(flaggedMoves==null) {
			return;
		} // Ignore if flaggedMove has not been initialized
		for(FlaggedMove move : flaggedMoves) {
			maxHeap.offer(move); // Add new elements
		}
	}

	public static LinkedList<FlaggedMove> getTopMoves(int totalGames){

		int i=0;
		if(totalGames!=0) {
			totalGames=2+(int) Math.round(Math.log(totalGames));
			// Calculating the number of games to be extracted from the queue based on a metric. +2 ensures that users can get value
			// out of it even with a small number of games played.
		}
		else {
			totalGames=99999999; // 0 indicates export all games. Set total games to an arbitrarily large integer.
		}
		LinkedList<FlaggedMove> flagMoves = new LinkedList<>();
		while (!maxHeap.isEmpty() && i<totalGames) {
			flagMoves.add(maxHeap.poll());
			i++;

		} // While their are still games in the priority queue and the move limit isn't reached, return the moves.

		return flagMoves;

	}

}
