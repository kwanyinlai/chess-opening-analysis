import FlaggedMove.moveClass;

public class FlaggedMove {
	// FlaggedMove object to hold a reference to any moves of notes to be flagged in a game report object.
	private String prevFEN;
	private float deltaEval;
	private String currFEN;
	private int frequency;
	private ChessPiece piece;
	private moveClass classification;
	private float prevEval;
	private float currEval;
	private String pgn;

	public enum moveClass{
		INACCURACY,
		MISTAKE,
		BLUNDER
	}


	public void setPrevEval(float prevEval) {
		this.prevEval=prevEval;
	}

	public void setCurrEval(float currEval) {
		this.currEval=currEval;
	}

	public String getCurrEval() {
		if(Position.FENisWhite(prevFEN)){
			return "-"+currEval;
		}
		return "+"+currEval;
	}

	public String getPrevEval() {
		if(Position.FENisWhite(prevFEN)){
			return "+"+prevEval;
		}
		return "-"+prevEval;
	}


	public FlaggedMove(String prevFEN, String currFEN, float deltaEval, ChessPiece piece) {
		this.prevFEN=prevFEN;
		this.currFEN=currFEN;
		this.deltaEval=deltaEval;
		this.piece = piece;
		frequency=1;
		if(Math.abs(deltaEval)<1.0) {
			classification=moveClass.INACCURACY;

		}
		else if(Math.abs(this.deltaEval)<3.0) {
			classification=moveClass.MISTAKE;
		}
		else {
			System.out.println("EVAL IS "+Math.abs(deltaEval));
			classification=moveClass.BLUNDER;
		}

	}

	public void setPGN(String pgn) {
		this.pgn=pgn;
	}

	public String getPGN() {
		return pgn;
	}

	public String getClassification() {

		if(classification==moveClass.INACCURACY) {
			return "an inaccuracy!";
		}
		else if (classification==moveClass.MISTAKE) {
			return "a mistake!";
		}
		else {
			return "a blunder!";
		}
	}

	public ChessPiece getPiece() {
		return piece;
	}
	public String getPrevFEN() {
		return prevFEN;
	}
	public float getKey() {
		return (float) (deltaEval+Math.pow(frequency, 20));
	}
	public String getCurrFEN() {
		return currFEN;
	}

	public void incrementFreq() {
		frequency++;
	}

	@Override
	public String toString() {
		return prevFEN + " " + currFEN + " " + deltaEval + " " +frequency + " " + getKey();
	}

	public int getFrequency() {
		return frequency;
	}



	public void setDeltaEval(float deltaEval) {
		this.deltaEval=deltaEval;
		if(Math.abs(deltaEval)<1.0) {
			classification=moveClass.INACCURACY;

		}
		else if(Math.abs(this.deltaEval)<3.0) {
			classification=moveClass.MISTAKE;
		}
		else {
			classification=moveClass.BLUNDER;
		}
	}

	@Override
	public int hashCode() {
		if(prevFEN!=null && currFEN!=null) {
			return prevFEN.substring(0, prevFEN.length()-4).hashCode()*167 +
					currFEN.substring(0, currFEN.length()-4).hashCode()*167;
		}
		else {
			return -1;
		}

	}
}
