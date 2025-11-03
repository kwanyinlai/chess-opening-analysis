import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

public class ChessBoardGUI extends JPanel{

	private static final long serialVersionUID = 1L;
	private final JFrame frame;
	private final JPanel main;
	private final Background background;
	private final ChessPieces chessPieces;
	private final BufferedImage whiteKingIcon;
	private final BufferedImage blackKingIcon;
	private final BufferedImage whiteBishopIcon;
	private final BufferedImage blackBishopIcon;
	private final BufferedImage whiteKnightIcon;
	private final BufferedImage blackKnightIcon;
	private final BufferedImage whitePawnIcon;
	private final BufferedImage blackPawnIcon;
	private final BufferedImage whiteQueenIcon;
	private final BufferedImage blackQueenIcon;
	private final BufferedImage whiteRookIcon;
	private final BufferedImage blackRookIcon;
	private ChessPiece selected;



	public ChessBoardGUI() throws IOException{

		whiteKingIcon=ImageIO.read(getClass().getClassLoader().getResource("w_king.png"));
		blackKingIcon=ImageIO.read(getClass().getClassLoader().getResource("b_king.png"));
		whiteBishopIcon=ImageIO.read(getClass().getClassLoader().getResource("w_bishop.png"));
		blackBishopIcon=ImageIO.read(getClass().getClassLoader().getResource("b_bishop.png"));
		whiteKnightIcon=ImageIO.read(getClass().getClassLoader().getResource("w_knight.png"));
		blackKnightIcon=ImageIO.read(getClass().getClassLoader().getResource("b_knight.png"));		
		whiteRookIcon=ImageIO.read(getClass().getClassLoader().getResource("w_rook.png"));
		blackRookIcon=ImageIO.read(getClass().getClassLoader().getResource("b_rook.png"));
		whitePawnIcon=ImageIO.read(getClass().getClassLoader().getResource("w_pawn.png"));
		blackPawnIcon=ImageIO.read(getClass().getClassLoader().getResource("b_pawn.png"));
		whiteQueenIcon=ImageIO.read(getClass().getClassLoader().getResource("w_queen.png"));
		blackQueenIcon=ImageIO.read(getClass().getClassLoader().getResource("b_queen.png"));

		// Importing images for icons

		frame=new JFrame();
		frame.setBounds(250,250,512,537);

		JFrame.setDefaultLookAndFeelDecorated(false);
		main = new JPanel();
		main.setLayout(new OverlayLayout(main));
		background = new Background();
		chessPieces = new ChessPieces();
		frame.setTitle("Analysis Board");
		main.add(chessPieces);
		main.add(background);
		frame.setResizable(false);
		frame.setLayout(new BorderLayout());
		frame.add(main);

		frame.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				selected=ChessPiece.getChessPiece(e.getX(),e.getY());

			}

			@Override
			public void mousePressed(MouseEvent e) {
				selected=ChessPiece.getChessPiece(e.getX(),e.getY());

			}




			@Override
			public void mouseReleased(MouseEvent e) {
				if(selected!=null) { // Make sure that a piece has been selected before moving.
					Coordinate move;
					if(!ChessPiece.isFlipBoard()) {
						move = new Coordinate((byte)(e.getX()/64),(byte)((7-(e.getY()-32)/64))); // Generate a new coordinate to be passed into the ChessPiece.move() function
					}
					else {
						move = new Coordinate((byte)(7-(e.getX()/64)),(byte)((e.getY()-32)/64));
					}


					if(DisplayBoard.isWhiteMove()==selected.isWhite && selected.checkLegal(move)) {
						DisplayBoard.setIsWhiteMove(!(DisplayBoard.isWhiteMove()));

						selected.move(move);

					}
					else {


						if(!ChessPiece.isFlipBoard()) { // Check if the board is flipped or not. Could be used to provide extensibility for flipping the board according to color.
							selected.setDisplayX((selected.getX())*64+1);
							selected.setDisplayY((7-selected.getY())*64+1);
						}
						else {
							selected.setDisplayX((7-selected.getX())*64+1);
							selected.setDisplayY((selected.getY())*64+1);
						}
					}
					selected=null;
				}








				frame.repaint();


			}

			@Override
			public void mouseEntered(MouseEvent e) {
				return;
			}

			@Override
			public void mouseExited(MouseEvent e) {
				return;
			}

		});
		frame.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if(selected!=null) {
					selected.setDisplayX(e.getX()-30);
					selected.setDisplayY(e.getY()-64);
				}
				main.repaint(); // Repaint main frame to update as piece moves.
			}


			@Override
			public void mouseMoved(MouseEvent e) {
			}
		});
		frame.setDefaultCloseOperation(2); // Allows the analysis board to be opened and close while keeping the main software open.
		frame.setVisible(true);

		//issue with illegal moves being played after a legal move is chosen afterwards
	}



	public class Background extends JComponent { // Separating Background and ChessPieces as JComponents
												 // to avoid having the board overlapping pieces.

		private static final long serialVersionUID = 1L;

		@Override
		public void paintComponent(Graphics g) {
			boolean paint=false;

			for(int i = 0 ; i<8 ; i++) {
				for(int j=7; j>=0 ; j--) {
					if(paint) {
						g.setColor(Color.decode("#f5e6bf"));

						g.fillRect(i*64, j*64, 64,64);
					}
					else {
						g.setColor(Color.decode("#664439"));
						g.fillRect(i*64, j*64, 64,64); // Squares are 64 in width and height, move along 64 each time and paint a new square.
					}
					paint=!paint;
				}
				paint=!paint; // paint the squares in alteranting fashion
			}


		}


	}

	public class ChessPieces extends JComponent{
		private static final long serialVersionUID = 1L;

		@Override
		protected void paintComponent(Graphics g) {
			ChessPiece[][] chessBoard = DisplayBoard.getDisplayBoard();

			for(int i = 0 ; i<8 ; i++) {
				for(int j=7; j>=0 ; j--) {
					ChessPiece piece = chessBoard[i][j];

					if(piece!=null) {

						if(piece instanceof Pawn) {

							if(piece.isWhite()) {
								g.drawImage(whitePawnIcon, piece.getDisplayX(),piece.getDisplayY(),this);
							}
							else {
								g.drawImage(blackPawnIcon, piece.getDisplayX(),piece.getDisplayY(),this);
							}
						}
						else if(chessBoard[i][j] instanceof Knight) {
							if(piece.isWhite()) {
								g.drawImage(whiteKnightIcon,piece.getDisplayX(),piece.getDisplayY(),this);
							}
							else {
								g.drawImage(blackKnightIcon, piece.getDisplayX(),piece.getDisplayY(),this);
							}
						}
						else if(chessBoard[i][j] instanceof Bishop) {
							if(piece.isWhite()) {
								g.drawImage(whiteBishopIcon, piece.getDisplayX(),piece.getDisplayY(),this);
							}
							else {
								g.drawImage(blackBishopIcon, piece.getDisplayX(),piece.getDisplayY(),this);
							}
						}
						else if(chessBoard[i][j] instanceof Queen) {
							if(piece.isWhite()) {
								g.drawImage(whiteQueenIcon, piece.getDisplayX(),piece.getDisplayY(),this);
							}
							else {
								g.drawImage(blackQueenIcon, piece.getDisplayX(),piece.getDisplayY(),this);
							}
						}
						else if(chessBoard[i][j] instanceof King) {
							if(piece.isWhite()) {
								g.drawImage(whiteKingIcon, piece.getDisplayX(),piece.getDisplayY(),this);
							}
							else {
								g.drawImage(blackKingIcon,piece.getDisplayX(),piece.getDisplayY(),this);
							}
						}
						else if(chessBoard[i][j] instanceof Rook) {
							if(piece.isWhite()) {
								g.drawImage(whiteRookIcon, piece.getDisplayX(),piece.getDisplayY(),this);
							}
							else {
								g.drawImage(blackRookIcon, piece.getDisplayX(),piece.getDisplayY(),this);
							}
						}
					}
				}
			}

		}
	} // Draw piece as necessary


}
