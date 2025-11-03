import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
//NEED MULTIPLE INSTANCES OF CHESS BOARDS BY MAKING NEW OBJECTS
public class UserGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private JButton uploadButton;
	private JLabel selectedFileLabel;
	private JTextArea info;
	private JButton next;
	private JButton analysisBoardButton;
	private ChessBoardComponent chessBoard;
	private LinkedList<FlaggedMove> FENs = new LinkedList<>();
	private int i =0;
	private JTextField username;
	private JButton previous;
	private JLabel usernameLabel;
	private JTextArea moveInfo;
	private static JProgressBar progressBar;

	public static void setProgress(double progress) {
		progressBar.setValue((int) (progress*100));
		progressBar.repaint();
	}


	public UserGUI() throws IOException {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		JPanel panel = new JPanel();
		JPanel topPanel = new JPanel(new FlowLayout());
		JPanel bottomPanel = new JPanel(new FlowLayout());
		JPanel rightPanel = new JPanel(new BorderLayout());

		panel.setBackground(Color.white);
		topPanel.setBackground(Color.white);
		bottomPanel.setBackground(Color.white);

		rightPanel.setBackground(Color.white);
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setLayout(new BorderLayout());
		setContentPane(panel);
		setResizable(false);
		setTitle("Opening Analysis Tool");
		setSize(800, 650);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		topPanel.setLayout(new FlowLayout());

		progressBar = new JProgressBar();
		progressBar.setValue(0);
		uploadButton = new JButton("Upload File");
		analysisBoardButton = new JButton("Analysis Board");
		selectedFileLabel = new JLabel("Selected File: ");
		chessBoard = new ChessBoardComponent();
		next = new JButton("Next");
		previous = new JButton("Previous");
		info = new JTextArea();
		username = new JTextField(15);
		usernameLabel = new JLabel("Username:");
		moveInfo=new JTextArea("");
		moveInfo.setEditable(false);

		// Instantiating and declaring all GUI components



		username.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {


				Player.setUser(username.getText());

			}
		}); // Input username associated with the PGN files so that the algorithm can determine which color pieces to look at.

		uploadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					final File[] selectedFile = new File[1];
					selectedFile[0]=fileChooser.getSelectedFile();
					String fileName = fileChooser.getSelectedFile().getName();
					PositionParser.setPGNFile(selectedFile[0]);

					if (fileName.substring(fileName.lastIndexOf(".")).toLowerCase().equals(".pgn")) {
						SwingUtilities.invokeLater(() -> {



							info.setText("\n \n \n \n \n \n \n \n \n \n \n \nGames are being analyzed... Please wait as the games are processed."
									+ "This process can take from a few minutes to a few hours depending on the number of"
									+ " games.");



							selectedFileLabel.setText("Selected File: " + selectedFile[0].getName());

							moveInfo.setText("");
						});

						new Thread(() -> {

							/* Creating a separate thread for the analyzePGN() function to allow the UI to be updated concurrently while the game report
							 * is being generated. The user can thus continue to interact with the application while the games are being analyzed, which is
							 * important because the analyzePGN function can often be very time consuming.
							 */


							try {
								Main.analyzePGN(); //Analyze the games attached to the PGN file
								FENs=MoveFlagger.getTopMoves(Main.getTotalGames());  // Once the games have been analyzed, extract the top few moves from the priority queue and set
																 // it to the FENs LinkedList
								info.setText("\n \n \n \n \n \nAnalysis complete. \n \n \nPosition "
								+ (i+1) + " of "+FENs.size()+" \n"); //Update text box to inform user of the completion of the analysis
								DisplayBoard.setPosition(Position.FENtoPos(FENs.get(0).getPrevFEN(), DisplayBoard.getDisplayBoard()));
								String temp = Position.stockfishToString(Stockfish.getBestMove(FENs.get(0).getPrevFEN(), 500),
										FENs.get(0).getPrevFEN()); // Convert the raw output of Stockfish to something interpretable by the user. Store it in a temporary variable.
								moveInfo.setText(FENs.get(0).getPGN()+")\n \nYou reached this position " + FENs.get(0).getFrequency() +" times. "
										+ "\nThe best move is " + temp +" ("+ FENs.get(0).getPrevEval() +").\n" + Position.deltaFEN(FENs.get(0))
												+ " ("+ FENs.get(0).getCurrEval()+") is "+FENs.get(0).getClassification());
								//Provide information about the flagged move.
								ChessBoardComponent.setFEN(FENs.get(0).getPrevFEN()); // Set the FEN of the chess board to the position currently displayed to visually represent
																					  // the FEN string.


								chessBoard.repaint(); // Repaint the component based on the updated FEN string.

							} catch (IOException ex) {
								ex.printStackTrace(); // Catching errors...
								progressBar.setValue(0);
							}
							catch(UsernameException ey) {
								progressBar.setValue(0);
								JOptionPane.showMessageDialog(null, "The username "+Player.getUser() +" does not match the "
										+ "name provided in the .pgn file.", "error", JOptionPane.ERROR_MESSAGE); // Catch the exception UsernameException to allow
																												  // the user to reinput any necessary files or Strings
																												  // and continue using the code.
								info.setText("\n \n \n \n \n \n \n \n \n \n \n \nNo games uploaded yet. Get started by uploading a PGN file.");
								selectedFile[0]=null;
								selectedFileLabel.setText("Selected File:");
								PositionParser.setPGNFile(null);


								// Unselect the file selected so the user can reselect a new file.
							} catch (FileException ez) {
								JOptionPane.showMessageDialog(null, "The PGN file provided is incorrectly formatted or has been corrupted. Please try again with a different PGN file."
										+ "", "error", JOptionPane.ERROR_MESSAGE); // Catch the exception UsernameException to allow
																												  // the user to reinput any necessary files or Strings
																												  // and continue using the code.
							}
							catch (Exception e1) {
								e1.printStackTrace();
							}
						}).start();

					} else {
						JOptionPane.showMessageDialog(null, "The selected file is not a .pgn file."
								+ " Please reinput a file of valid type.", "error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});



		analysisBoardButton.addActionListener(new ActionListener() { // Adding event handler for analysisBoardButton

			@Override
			public void actionPerformed(ActionEvent e) {

				ChessPiece[][] displayBoard=new ChessPiece[8][8]; // If the button to create an analysis board is pressed, update the FEN of the analysis board
																  // to the one currently being viewed.
				Position currentPos = null;

				try {
					currentPos=Position.FENtoPos(FENs.get(i).getPrevFEN(), displayBoard);
					displayBoard=currentPos.getBoard();
					DisplayBoard.setPosition(currentPos);
				}
				catch(Exception e1) {
					DisplayBoard.startPos(displayBoard); // If no games have been analyzed yet, then set the analysis board to the starting position.
					DisplayBoard.setDisplayBoard(displayBoard);
				}


				try {
					new ChessBoardGUI(); // Open analysis board GUI in a new window.
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});


		next.addActionListener(new ActionListener() { // Adding event handler for analysisBoardButton
			@Override
			public void actionPerformed(ActionEvent e) {
				if(FENs.size()!=0) {
					if(i<FENs.size()-1) {
						i++;
					}
					else {

						i=0;
					}
					// When next is clicked, increment the iterator. If the iterator reaches the end of the FENs LinkedList, loop back to the start.

					ChessPiece[][] displayBoard= new ChessPiece[8][8];

					ChessBoardComponent.setFEN(FENs.get(i).getPrevFEN());
					DisplayBoard.setPosition(Position.FENtoPos(FENs.get(i).getPrevFEN(), displayBoard));

					String temp;
					try {
						String prevFEN = FENs.get(i).getPrevFEN();
						info.setText("\n \n \n \n \n \nAnalysis complete. \n \n \nPosition " + (i+1) + " of "+FENs.size()+" \n");
						temp = Position.stockfishToString(Stockfish.getBestMove(prevFEN, 500),FENs.get(i).getPrevFEN());
						moveInfo.setText(FENs.get(i).getPGN()+"\n \nYou reached this position " + FENs.get(i).getFrequency() +" times. "
								+ "\nThe best move is " + temp +" ("+ FENs.get(i).getPrevEval()+").\n" + Position.deltaFEN(FENs.get(i))
										+ " ("+ FENs.get(i).getCurrEval()+") is "+FENs.get(i).getClassification());
						//Provide information about the flagged move.

					} catch (IOException e1) {
						e1.printStackTrace();
					}

					// When moving between positions, update the FEN and GUI.

					panel.repaint();
				}
				else {
					System.out.println("ERROR");
					return;
				}
			}

		});


		previous.addActionListener(new ActionListener() { // Adding event handler for analysisBoardButton
			@Override
			public void actionPerformed(ActionEvent e) {

				if(FENs.size()!=0) {
					if(i>0) {
						i--;
					}
					else {
						i=FENs.size()-1;
					}
					// When next is clicked, increment the iterator. If the iterator reaches the end of the FENs LinkedList, loop back to the start.

					ChessPiece[][] displayBoard= new ChessPiece[8][8];

					ChessBoardComponent.setFEN(FENs.get(i).getPrevFEN());

					DisplayBoard.setPosition(Position.FENtoPos(FENs.get(i).getPrevFEN(), displayBoard));
					String temp;
					try {
						String prevFEN = FENs.get(i).getPrevFEN();
						info.setText("\n \n \n \n \n \nAnalysis complete. \n \n \nPosition " + (i+1) + " of "+FENs.size()+" \n");
						temp = Position.stockfishToString(Stockfish.getBestMove(prevFEN, 500),FENs.get(i).getPrevFEN());
						moveInfo.setText(FENs.get(i).getPGN()+"\n \nYou reached this position " + FENs.get(i).getFrequency() +" times. "
								+ "\nThe best move is " + temp +" ("+ FENs.get(i).getPrevEval()+").\n" + Position.deltaFEN(FENs.get(i))
										+ " ("+ FENs.get(i).getCurrEval()+") is "+FENs.get(i).getClassification());
						//Provide information about the flagged move.
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					// When moving between positions, update the FEN and GUI.

					panel.repaint();
				}
				else {
					return;
				}
			}

		});

		info.setText("\n \n \n \n \n \n \n \n \n \n \n \nNo games uploaded yet. Get started by uploading a PGN file.");
		info.setLineWrap(true);
		info.setWrapStyleWord(true);
		rightPanel.setPreferredSize(new Dimension(250, 500));
		info.setEditable(false);
		info.setForeground(Color.BLACK);

		bottomPanel.add(previous,BorderLayout.EAST);
		bottomPanel.add(next, BorderLayout.EAST);
		bottomPanel.add(analysisBoardButton);

		rightPanel.add(moveInfo,BorderLayout.CENTER);
		rightPanel.add(info, BorderLayout.NORTH);
		rightPanel.add(progressBar, BorderLayout.SOUTH);

		topPanel.add(usernameLabel);
		topPanel.add(username);
		topPanel.add(uploadButton);
		topPanel.add(selectedFileLabel);

		panel.add(chessBoard, BorderLayout.WEST);
		panel.add(rightPanel, BorderLayout.EAST);
		panel.add(bottomPanel, BorderLayout.SOUTH);
		panel.add(topPanel, BorderLayout.NORTH);
		info.setBackground(Color.white);

		// Adding all component to their respective JPanels, and adding all the JPanels to the JFrame.



	}

	public static void displayGUI() {
		SwingUtilities.invokeLater(() -> {
			UserGUI gui = null;
			try {
				gui = new UserGUI();
			} catch (IOException e) {
				e.printStackTrace();
			}
			gui.setVisible(true);
		});
	}
	// Function to create GUI.

}

