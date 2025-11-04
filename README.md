This program makes use of Stockfish engine provided under the GNU General Public 
License Version 3 (GPLv3) to achieve it purpose and functionality. Stockfish is a
free and strong UCI chess engine. 

The official website for Stockfish is [here](https://stockfishchess.org/about/)

Source code for the Stockfish engine can be found inthe GitHub repository, 
found [here](https://github.com/official-stockfish/Stockfish)

A full copy of the GLPv3 Licence can be found in the text file "/stockfish_binary/LICENSE.txt

# Chess Opening Analysis Tool

This is a tool built using Java + Swing which allows users to analyse their own chess openings quickly, 
efficiently and in-mass.

## Installation

The executable can be downloaded [here](https://github.com/kwanyinlai/chess-opening-analysis/releases/tag/v1.0.0) or from the releases page.

## Features

This program can be used to play out full chess games, with complete chess logic implemented 
(including checks, captures, castling, en-passant, pawn promotion, etc.) using the analysis
board feature. 

This program can take in PGN files from an online chess website as input (alongside the username 
associated  with the PGN in the games). The program will parse the PGN file and convert it into
internal chess data structures as well as FEN files. It will then analyse all games (up to a limit 
of 1000 games). 

The program will find all inaccuracies, mistakes and blunders the player made in their opening games, 
and weight frequency with the degree of the mistake and create a succinct report for the player.
The program will classify the severity of the mistake alongside showing how frequently the mistake
is made, and use that to rank moves and display the few 'worst' moves (and the number of moves shown
to the user scales based off the number of games given as input), as well as giving a suggested better 
move. 

The player can navigate through all the moves using the 'Next' and 'Prev' button, and at any point press
the analysis board button which will pop up a chess board with the loaded in chess position.