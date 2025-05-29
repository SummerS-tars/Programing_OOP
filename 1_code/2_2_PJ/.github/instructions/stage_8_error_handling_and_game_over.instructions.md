# Stage 8: Error Handling and Game Over Display

## Objective

Optimize the user experience by providing feedback for illegal operations and clearly displaying results when the game ends.

## Prompt

We will refine error handling and the interface display for game endings.

1.  Optimization of Illegal Operation Prompts:
    * 'Error reporting for exceptional situations: No specific requirement for the graphical interface. It can have no reaction, pop up a window, or display relevant text information in a specific area of the interface. (This is an effective debugging method)'.
    * Ensure all illegal operation exceptions (e.g., `top.thesumst.type.exception.IllegalMoveException`, `top.thesumst.type.exception.IllegalCommandException`) thrown in the game logic (e.g., in the `execute` method of `GameCommand`) are caught by `GUIGameLoop`.
    * Pass these exception messages via the `message` field of the `Event` object, and in the `update` method of `GUIView`, display `event.message` on the `feedbackMessageLabel` to provide clear user feedback.
2.  Game Over Condition Judgment and Display:
    * Implement judgment for game end conditions in `GUIGameLoop` or the core game logic.
    * Peace Mode Game End: 'Condition for Peace ending: The board is full.' When the condition is met, the GUI should display game over information on `feedbackMessageLabel` or via an `Alert` dialog.
    * Reversi Mode Game End: 'Conditions for Reversi ending: The board is full, or neither player has a legal move. When a Reversi game ends, it needs to display both players' scores and indicate the game result (xx player wins or it's a draw).' When the condition is met, the GUI should display the final scores and the win/loss result on `feedbackMessageLabel` (e.g., "Player1 [Tom] wins!" or "Draw!").
    * Gomoku Mode Game End: 'Implement the logic for five-in-a-row detection. After one side achieves five-in-a-row, print the winner and end that game board.' When the condition is met, the GUI should display the winning player on `feedbackMessageLabel`.
3.  Interaction After Game End: After a game ends, the GUI should continue to display the final result and allow the user to switch to other games or start a new game.

## Testing and Acceptance Criteria
* Attempt various illegal inputs and operations (e.g., placing a piece on an occupied cell, forcing a 'pass' in Reversi mode when legal moves exist). Verify that error messages are correctly displayed on `feedbackMessageLabel` without affecting program operation.
* Play a Peace game until the board is full. Verify that the game over message is displayed correctly.
* Play a Reversi game until the end condition is met (board full or no legal moves for either player). Verify that the final scores and win/loss result are displayed correctly.
* Play a Gomoku game until five-in-a-row is achieved. Verify that the winning player is displayed correctly.
* After any game ends, verify that it is still possible to normally switch to other games or create a new game.

At this point, the main GUI functions of the project are all completed. Please clearly report its completion.