import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.LinkedList;

/*  Code adapted form @author Rahul A R
 *  Stockfish Port for Java | rahular.github.io
 *
 *  https://github.com/rahular/chess-misc/blob/master/JavaStockfish/src/com/rahul/stockfish/Stockfish.java
 *
 */

public class Stockfish {
	private static Stockfish engine = new Stockfish();
	private static Process engineProcess;
	private static BufferedReader processReader;
	private static BufferedWriter processWriter;
	private static final String PATH = "StockfishEngine/stockfish";

	// Changing the engine to static so that the engine can be started and statically stopped.
	// Removes the need to constantly create new and close Stockfish processes.


	public static boolean startEngine() {
		try {
			engineProcess = Runtime.getRuntime().exec(PATH);
			processReader = new BufferedReader(new InputStreamReader(
					engineProcess.getInputStream()));
			processWriter = new BufferedWriter(new
					OutputStreamWriter(engineProcess.getOutputStream()));

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static void sendCommand(String command) throws IOException {
		try {
			if(processWriter==null) {
				stopEngine();
				startEngine();
			}
			processWriter.write(command + "\n");
			processWriter.flush();


		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getOutput(int waitTime) {
		StringBuffer buffer = new StringBuffer();

		try {

			Thread.sleep(waitTime);
			sendCommand("isready");
			while (true) {
				String text = processReader.readLine();
				if (text==null || text.equals("readyok"))
					break;
				else
					buffer.append(text + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return buffer.toString();
	}

	public static String getBestMove(String fen, int waitTime) throws IOException {

		Stockfish.startEngine();
		sendCommand("position fen " + fen);
		sendCommand("go movetime " + waitTime);
		String[] dump = getOutput(waitTime + 20).split("\n");

		String[] dump2=dump[dump.length-1].split(" ");

		Stockfish.stopEngine();

		return dump2[Arrays.asList(dump2).indexOf("pv")+1]; // Converting to list to access the .indexOf function, then uing that to return a value.

	}

	public static void stopEngine() throws IOException {
		try {
			sendCommand("quit");
			processReader.close();
			processWriter.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public float getEvalScore(String fen, int waitTime) throws IOException {

		sendCommand("position fen " + fen);
		sendCommand("go movetime " + waitTime);
		float evalScore = 0.0f;
		String[] dump = getOutput(waitTime + 20).split("\n");
		for (int i = dump.length - 1; i >= 0; i--) {
			if (dump[i].startsWith("info depth ")) {
				String[] tokens = dump[i].split(" ");
				for (int j = 0; j < tokens.length ; j++) {
					if (tokens[j].equals("score")) {
						evalScore = Float.parseFloat(tokens[j + 2].replaceAll("[^0-9.-]", "")); // Remove unnecessary characters.
						break;
					}
				}
				break;



			}
		}
		return evalScore / 100;
	}


	public static String coloredEval(String fen, int waitTime) throws IOException {
		startEngine();
		sendCommand("position fen " + fen);
		sendCommand("go movetime " + waitTime);
		float evalScore = 0.0f;
		String[] dump = getOutput(waitTime + 20).split("\n");
		for (int i = dump.length - 1; i >= 0; i--) {
			if (dump[i].startsWith("info depth ")) {
				String[] tokens = dump[i].split(" ");
				for (int j = 0; j < tokens.length ; j++) {
					if (tokens[j].equals("score")) {
						evalScore = Float.parseFloat(tokens[j + 2].replaceAll("[^0-9.-]", "")); // Remove unnecessary characters.
						break;
					}
				}
				break;



			}
		}

		System.out.println(evalScore);
		// FIX NEEDED
		stopEngine();
		evalScore=evalScore/100;
		if(!Position.FENisWhite(fen)) {

			evalScore=evalScore*-1;
		}

		String output="(";
		if(evalScore<0) {
			output+="-";
		}
		else {
			output+="+";
		}
		output+=evalScore;
		output+=")";

		return output;
	}


	public static float stockfishMove(String FEN) throws IOException { // Used for analyzing individual moves at a higher depth

		float eval;
		sendCommand("uci");
		getOutput(0);
		eval=engine.getEvalScore(FEN, 100);
		return eval;

	}

	public static LinkedList<Float> stockfishGame(ChessGame game) throws IOException { // Used for analyzing an entire game but at a lower depth.

		String[] FENs = game.getFEN();
		LinkedList<Float> evals = new LinkedList<>();
		evals.add((float) 0.0);


		sendCommand("uci");
		getOutput(0);

		for(String FEN: FENs) {

			evals.add(engine.getEvalScore(FEN, 10));
		}
		return evals;
	}

	public static Stockfish getStockfish() {
		return engine;
	}


}






