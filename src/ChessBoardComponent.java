import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ChessBoardComponent extends JPanel {

    private static final long serialVersionUID = 1L;
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
	private static String FEN="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

	public static void setFEN(String FEN) {
		ChessBoardComponent.FEN=FEN;
	}

    public ChessBoardComponent() throws IOException {
    	setBackground(Color.white);
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
        setPreferredSize(new Dimension(8*64, 8*64));

        // Importing chessIcon pngs.
    }

    @Override
    protected void paintComponent(Graphics g) { // Overriding paintComponent objects

        super.paintComponent(g);

        boolean paint=false;


		for(int i = 0 ; i<8 ; i++) {
			for(int j=7; j>=0 ; j--) {
				if(paint) {
					g.setColor(Color.decode("#f5e6bf"));

					g.fillRect(i*64, j*64, 64,64);
				}
				else {
					g.setColor(Color.decode("#664439"));
					g.fillRect(i*64, j*64, 64,64);
				}
				paint=!paint;
			}
			paint=!paint;
		}
		// Draw chess board
		int x;
		int y;
		int increment;
		if(FEN.split(" ")[1].equals(("w"))) {
			 increment=1;
			 x=0;
			 y=0;
		}
		else {
			 increment= -1;
			 y=7;
			 x=7;
		}

		for (int i = 0; i<FEN.length();i++) {
			char temp = FEN.charAt(i);
			char tempLower=Character.toLowerCase(temp);

			if((increment==1 && y<8) || (increment==-1 && y>=0)) {
				switch (tempLower){
				case 'p':
					if (temp=='P') {
						g.drawImage(whitePawnIcon, x*64,y*64,this);
						x+=increment;
						break;

					}
					else {
						g.drawImage(blackPawnIcon, x*64,y*64,this);
						x+=increment;
						break;
					}
				case 'n':
					if (temp=='N') {
						g.drawImage(whiteKnightIcon, x*64,y*64,this);
						x+=increment;
						break;
					}
					else {
						g.drawImage(blackKnightIcon, x*64,y*64,this);
						x+=increment;
						break;
					}
				case 'b':
					if (temp=='B') {
						g.drawImage(whiteBishopIcon, x*64,y*64,this);
						x+=increment;
						break;
					}
					else {
						g.drawImage(blackBishopIcon, x*64,y*64,this);
						x+=increment;
						break;
					}
				case 'q':
					if (temp=='Q') {
						g.drawImage(whiteQueenIcon, x*64,y*64,this);
						x+=increment;
						break;
					}
					else {
						g.drawImage(blackQueenIcon, x*64,y*64,this);
						x+=increment;
						break;
					}
				case 'r':
					if (temp=='R') {
						g.drawImage(whiteRookIcon, x*64,y*64,this);
						x+=increment;
						break;
					}
					else {
						g.drawImage(blackRookIcon, x*64,y*64,this);
						x+=increment;
						break;
					}

				case 'k':
					if (temp=='K') {
						g.drawImage(whiteKingIcon, x*64,y*64,this);

						x+=increment;
						break;
					}
					else {
						g.drawImage(blackKingIcon, x*64,y*64,this);
						x+=increment;
						break;

					}
				case '/':
					y+=increment;
					if(increment==1) {
						x=0;
					}
					else {
						x=7;
					}


					break;


				default:
					if(increment==1) {
						x+=Character.getNumericValue(tempLower);
					}
					else {
						x-=Character.getNumericValue(tempLower);
					}



				}
				if((x==8 && y==7)) {

					y++;
				}
				else if (x==-1 && y==0) {
					y--;
				}

			}

			else {
				break;
			}
		}
    } // Draw in chess pieces
}