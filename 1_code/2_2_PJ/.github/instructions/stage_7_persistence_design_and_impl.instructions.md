# Stage 7: Game State Saving and Restoration (Design and Implementation)

## Objective

Implement saving of game state to a file upon exit and restoring it from the file upon startup.

## Prompt

Please do not start implementing the code for this stage yet; please provide a design first.

Please first propose a specific design plan for 'Game State Saving and Restoration', including but not limited to:

1.  Serialization Implementation:
    * 'When the game exits, the current state of the game needs to be saved to a file (e.g., pj.game). When starting the game, if a pj.game file exists in the current directory, it should be able to restore the current state of the game. The restored content includes: a. The game list b. The state of each game when it was exited c. The currently running game (which should be the game that was running at exit).'
    * Please list all core classes that need to implement the `java.io.Serializable` interface (e.g., `GameList`, `GameContainer`, `ChessBoard`, `Player`, `Step`, `Event`, etc.), and all related objects they reference. Ensure all necessary fields are considered.
2.  Save Method Design:
    * Design a method for saving the game state (e.g., `public static void saveGameState(GameList gameList, String filename)`).
    * Specify its containing class (e.g., a new utility class `GamePersistenceTools` could be created under the `top.thesumst.tools` package) and its responsibilities.
    * Describe the basic flow using `FileOutputStream` and `ObjectOutputStream`.
3.  Load Method Design:
    * Design a method for loading the game state (e.g., `public static GameList loadGameState(String filename)`).
    * Specify its containing class and responsibilities.
    * Describe the basic flow using `FileInputStream` and `ObjectInputStream`.
4.  Startup Interaction Logic Design:
    * 'The player can choose whether to load it (if they choose not to load, delete the file).'
    * Please describe in detail the application startup flow (e.g., at the beginning of the `start()` method in `top.thesumst.Main`):
        * How to check if the `pj.game` file exists.
        * If it exists, how to use a `javafx.scene.control.Alert` dialog (type `Confirmation`) to show the player a confirmation prompt with options like "Load Saved Game" and "Start New Game (Delete Save)".
        * How to perform the corresponding action based on the player's choice (call the load method or delete the file and initialize a new game).
5.  Exception Handling Design:
    * How to handle potential `IOException` and `ClassNotFoundException` during serialization and deserialization.
    * Please consider in your design how to provide user-friendly error messages.

Please return your design plan to me in Markdown format. After I evaluate and agree, you can then proceed with code implementation.

After this stage is completed (design plan only), please clearly report its completion status and await my further instructions.