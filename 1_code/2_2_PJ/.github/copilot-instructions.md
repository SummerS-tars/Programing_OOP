# Board Game GUI Development Overview and Stage Guide (Copilot Guidance Manual)

## Introduction

This document aims to provide the IDE Copilot with a clear, phased development path for implementing a JavaFX-based board game graphical user interface. Each stage defines clear objectives, detailed implementation steps, necessary reuse points, and strict acceptance criteria.

Copilot Code of Conduct:
* Step-by-step Execution: Please strictly follow the stage order and the steps within each stage's prompt.
* Prioritize Code Reuse: Before writing new code, please deeply understand and maximize the reuse of existing classes and methods in the `src/main/java` directory (such as `GameList`, `InputParser`, `Observer`, `GUICommandProvider`, `GUIGameLoop`, etc.).
* Structural Optimization Suggestions: If, during the analysis of the existing code structure, you identify any areas for significant improvement or optimization, please pause before implementation and explain your proposed improvements to me in detail.
* Clear Progress Reporting: Upon completion of each stage, please clearly state that the stage is complete, list the test results for that stage, and then await my further instructions.
* Self-check and Debugging: Before submitting the stage completion report, please perform basic code compilation and self-testing to ensure there are no syntax errors or obvious runtime exceptions.
* FXML Usage: We assume that the main interface layout FXML file (`MainGameView.fxml`) has been pre-generated and provided by an external tool (like Scene Builder). Copilot's task is to load this FXML and be responsible for writing its controller (`top.thesumst.view.gui.GUIController`) code, including binding UI elements and implementing dynamic update logic.
* `Platform.runLater`: Please always remember that all modifications and updates to JavaFX UI elements must be performed on the JavaFX Application Thread. If game logic runs on a background thread, be sure to use `Platform.runLater()` to submit UI update tasks.
* `.github/reference/MainGameView.fxml`: the reference FXML file for the main game interface is provided in the `.github/reference` directory. Please refer to it when implementing the GUI.

## Project Overview

The goal of this project is to convert an existing command-line (CLI) board game into a fully functional JavaFX Graphical User Interface (GUI) application, while preserving and reusing the core game logic.

## Stage Guide and File List

The following are the development stages for this project and their corresponding prompt files. Please execute them strictly in order.

* Stage 1: JavaFX Project Initialization, Basic Window Construction, and MOTD Display
    * File: `stage_1_init_and_motd.md`
    * Objective: Launch a basic JavaFX window and display the MOTD in the GUI.
* Stage 2: Core GUI Layout Design and Responsive Adjustment
    * File: `stage_2_layout_and_resizing.md`
    * Objective: Implement the main interface layout, with the chessboard fixed on the left and the right information area responsively adjusting.
* Stage 3: Visual Implementation and Click Event Handling for Chessboard Area
    * File: `stage_3_chessboard_rendering_and_click.md`
    * Objective: Draw the chessboard and handle click events on the chessboard cells.
* Stage 4: Right Information Panel Implementation (Player Info, Game List, Game Status)
    * File: `stage_4_right_info_panel_impl.md`
    * Objective: Populate the right information panel, displaying all game-related data and action buttons.
* Stage 5: Integration of GUI Operations and Command Provider
    * File: `stage_5_gui_command_integration.md`
    * Objective: Implement `GUICommandProvider` to connect GUI actions to core game command processing.
* Stage 6: Game Loop and Dynamic UI Update
    * File: `stage_6_game_loop_and_ui_update.md`
    * Objective: Integrate `GUIGameLoop` to drive the game process and update the GUI in real-time and responsively.
* Stage 7: Game State Saving and Restoration (Design and Implementation)
    * File: `stage_7_persistence_design_and_impl.md`
    * Objective: Implement functionality for persisting game state and providing a restoration option at startup.
    * (This stage will first undergo a design review, then implementation.)
* Stage 8: Error Handling and Game Over Display
    * File: `stage_8_error_handling_and_game_over.md`
    * Objective: Optimize error messages and clearly display results when the game ends.