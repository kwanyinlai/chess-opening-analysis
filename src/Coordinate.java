
public class Coordinate { // Coordinate class to store an x, y coordinate in one object
	private byte x;
	private byte y;


	public Coordinate(byte x, byte y) {
		this.x=x;
		this.y=y;
	}

	public byte getX() {
		return x;
	}

	public byte getY() {
		return y;
	}



	public static Coordinate stringToCoord(String coordinate) {
		switch(coordinate.charAt(0)) {
		case 'a':
			return new Coordinate((byte)0, (byte)(coordinate.charAt(1)));
		case 'b':
			return new Coordinate((byte)1, (byte)(coordinate.charAt(1)));
		case 'c':
			return new Coordinate((byte)2, (byte)(coordinate.charAt(1)));
		case 'd':
			return new Coordinate((byte)3, (byte)(coordinate.charAt(1)));

		case 'e':
			return new Coordinate((byte)4, (byte)(coordinate.charAt(1)));

		case 'f':
			return new Coordinate((byte)5, (byte)(coordinate.charAt(1)));
		case 'g':
			return new Coordinate((byte)6, (byte)(coordinate.charAt(1)));

		case 'h':
			return new Coordinate((byte)7, (byte)(coordinate.charAt(1)));
		default:
			return null;
		}


	}

	@Override
	public boolean equals(Object obj) {
		if(obj!=null){
			if(((Coordinate) obj).getX()==x && ((Coordinate) obj).getY()==y) {
				return true;
			}
		}
		return false;
	}


	public static String coordToString(Coordinate coord) {
		switch(coord.getX()) {
		case 0:
			return new String("a"+coord.getY());
		case 1:
			return new String("b"+coord.getY());
		case 2:
			return new String("c"+coord.getY());
		case 3:
			return new String("d"+coord.getY());
		case 4:
			return new String("e"+coord.getY());
		case 5:
			return new String("f"+coord.getY());
		case 6:
			return new String("g"+coord.getY());
		case 7:
			return new String("h"+coord.getY());

		default:
			return null;
		}

	}

	@Override
	public String toString() {
		String temp = "(x="+(x)+", y="+(y)+")";
		return temp;
	}


}
