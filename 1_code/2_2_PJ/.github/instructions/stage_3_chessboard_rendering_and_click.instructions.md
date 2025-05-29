# Stage 3: Visual Implementation and Click Event Handling for Chessboard Area

## Objective

Draw the chessboard within the fixed-size chessboard area container and enable it to respond to mouse click events, converting click positions into game commands.

## Prompt

Now, we will implement the specific drawing and interaction logic for the chessboard area.

1.  Chessboard Dynamic Creation and Drawing:
    * In `GUIController`, add a method (e.g., `updateChessboardDisplay()`) to dynamically create and update the chessboard's `GridPane`.
    * Add this dynamically generated `GridPane` chessboard to the `chessboardContainer` (`StackPane`).
    * Chessboard Size Adaptation: I have two chessboard sizes, 8x8 (for Peace/Reversi mode) and 15x15 (for Gomoku mode). Please design a mechanism to correctly draw the corresponding sized chessboard based on the current game type (defaults to 8x8 on initial load).
    * Size Adaptation and Centering: Ensure that both 8x8 and 15x15 chessboards are well centered and fill the fixed-size `chessboardContainer` defined in Stage 2. You will need to calculate the appropriate size for each cell (`StackPane` or `Rectangle`) based on the fixed dimensions of `chessboardContainer` and the current number of cells (8x8 or 15x15), so it adapts proportionally and remains centered.
    * Each chessboard cell can be represented by a `StackPane` (containing a `Circle` or `Text` to represent pieces).
    * Draw chess pieces (e.g., using `Circle` for black '●', white '○').
2.  Row and Column Labels:
    * Outside or inside the chessboard `GridPane`, implement the display of row numbers (1-F) on the left and column letters (A-O) at the top.
3.  Click Event Handling:
    * Add a mouse click event listener to each cell `StackPane` of the chessboard.
    * When a cell is clicked, get its corresponding row and column coordinates (e.g., 0-7 or 0-14).
    * Coordinate Conversion and `InputParser` Reuse:
        * Convert these GUI coordinates into a string format recognizable by `InputParser` (e.g., '1A', '8H', 'FA'). This requires handling the conversion of row numbers from decimal to hexadecimal (if greater than 9) and column numbers to letters (A-O).
        * Reuse `InputParser`: Utilize the `parse()` method of `top.thesumst.io.input.InputParser` to parse these strings and get an `InputResult` object.
    * Bomb Operation State Management:
        * Maintain a boolean flag in `GUIController` (e.g., `isBombModeActive`), set to `true` when the user clicks the "Use Bomb" button (to be implemented in Stage 4).
        * When `isBombModeActive` is `true` and a chessboard cell is clicked, construct the bomb position command string (e.g., '@1A') and pass it to `GUICommandProvider`. Then reset `isBombModeActive` to `false`.
4.  Command Passing: Ensure `GUIController` can pass these converted command strings through an interface provided by `GUICommandProvider` (e.g., `offerCommand` method).

Functional Requirements:
* 'Place piece: Keyboard input 1a -> Mouse click on the board cell corresponding to 1a.'
* Chessboard size can be 8x8 or 15x15.

## Testing and Acceptance Criteria
* After the application starts, a clear chessboard (initially should be 8x8) is displayed within the fixed area on the left, with correct row and column labels.
* The chessboard size (8x8 or 15x15) is correctly centered within the fixed area, and each cell size is reasonable.
* Clicking any cell on the chessboard, the console is able to print the `InputResult` object parsed by `InputParser`, with type `CHESS_MOVE` and correct coordinates.
* After simulating activation of bomb mode, clicking a chessboard cell, the console is able to print the `InputResult` object, with type `USE_BOMB` and correct coordinates.

After this stage is completed, please clearly report its completion status and await my further instructions.