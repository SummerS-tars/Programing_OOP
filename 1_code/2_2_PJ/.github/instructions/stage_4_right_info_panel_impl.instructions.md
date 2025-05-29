# Stage 4: Right Information Panel Implementation (Player Info, Game List, Game Status)

## Objective

Populate the three-column information area on the right, displaying all necessary game information and action buttons, and implement button click events.

## Prompt

Please implement the various components in the right three-column information area and bind their events.

1.  FXML Binding: In `GUIController`, bind all `Label`, `Button`, and `ListView` components within the right `GridPane` of `MainGameView.fxml` using the `@FXML` annotation.
    * Components to bind include: `currentGameNumberLabel`, `blackPlayerLabel`, `blackPlayerScoreLabel`, `whitePlayerLabel`, `whitePlayerScoreLabel`, `currentRoundLabel`, `blackBombsLabel`, `whiteBombsLabel`, `passButton`, `useBombButton`, `gameListView`, `addPeaceButton`, `addReversiButton`, `addGomokuButton`, `playbackButton`, `quitButton`, `feedbackMessageLabel`.
2.  First Column (Game Info & Player Controls):
    * Initialize the text for `currentGameNumberLabel`, `blackPlayerLabel`, `whitePlayerLabel`.
    * Reversi Mode Score: 'Only reversi needs to display scores for both players'. Control the visibility of `blackPlayerScoreLabel` and `whitePlayerScoreLabel` (default to `false`), and dynamically update their text when needed.
    * Current Player's Turn: Dynamically update the prompt next to the player label (e.g., via color or a piece icon).
    * Button Layout: 'pass' (`passButton`) and 'use bomb' (`useBombButton`) buttons should be placed in the correct position in this column in FXML. They should always exist in FXML, but their visibility (default to `false`) and availability (`disable` property) are dynamically controlled by the controller.
    * Gomoku Mode Bomb Count: 'The current remaining number of bombs needs to be displayed in the player status list'. Control the visibility of `blackBombsLabel` and `whiteBombsLabel` (default to `false`), and dynamically update the remaining bomb count in Gomoku mode.
    * Gomoku Mode Round Number: 'Based on the existing print content, this Lab requires additionally printing the game status (current round of the game) in Gomoku mode'. Control the visibility of `currentRoundLabel` (default to `false`), and dynamically update the current round number in Gomoku mode.
3.  Second Column (Game List):
    * The `gameListView` list should be populated with the initial game list (if it exists).
    * Switch Game Event: Add a `SelectionModel` listener to `gameListView`. When the user clicks a list item to select a game, get the selected game number, convert it to a command string (e.g., '2'), and pass it to `GUICommandProvider`, implementing 'Switch game: Keyboard input 2 --> Mouse click on the button corresponding to game 2.'
4.  Third Column (New Game & System Controls):
    * Add click events to buttons related to "Add New Game" (`addPeaceButton`, `addReversiButton`, `addGomokuButton`). When clicked, get the corresponding game type string ('peace', 'reversi', 'gomoku') and pass it to `GUICommandProvider`, implementing 'New game: Keyboard input `gomoku`--> Mouse click on the button corresponding to new gomoku.'
    * Add click events for the "Playback Mode" button (`playbackButton`) and "Quit Game" button (`quitButton`). When `quitButton` is clicked, pass the 'quit' command to `GUICommandProvider`, implementing 'Quit game: Keyboard input quit --> Mouse click on the quit button.'

Functional Requirements:
* 'The interface should include content covered in previous labs: a numbered chessboard, current game number, black and white player names and scores, whose turn it is, game list, remaining bomb count, game end result display, etc.'
* 'Different game types will have different GUI aspects, with specific requirements психолог previous labs. For example, only reversi needs to display scores for both players, only gomoku needs to display remaining bomb count, etc.'
* 'The game list in the 2nd column requires using a ListView format.'

## Testing and Acceptance Criteria
* After the application starts, all static text and buttons on the right panel are displayed correctly.
* `passButton` and `useBombButton` are correctly located in the first column.
* Clicking any game type button under "Add New Game" can trigger the corresponding 'newGame' command via `GUICommandProvider`.
* Clicking a game item in `gameListView` can trigger the correct 'switchBoard' command via `GUICommandProvider`.
* Clicking the `passButton` can trigger the 'pass' command via `GUICommandProvider`.
* Clicking the `quitButton` can trigger the 'quit' command via `GUICommandProvider`.
* On initial load, irrelevant labels for scores, bomb counts, rounds, etc., are hidden by default.

After this stage is completed, please clearly report its completion status and await my further instructions.