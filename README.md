# chess-game-java

A chess application developed in Java as part of the Advanced Programming / Object-Oriented Programming course, featuring a graphical user interface in JavaFX and an architecture based on the MVC pattern.

## Objective

This project was created to develop a functional, modular, and organized chess application, applying fundamental concepts of object-oriented programming, design patterns, and graphical user interface development.

## Key Features

- Chess game with fully implemented core logic
- Game creation with player name input
- Save and load games through serialization in `.dat` format
- Real-time match log recording and visualization
- Support for undo and redo moves
- Pawn promotion
- Special moves implemented, including *en passant*
- Graphical user interface developed with JavaFX
- Unit testing

## Architecture

The project follows the **MVC (Model-View-Controller)** pattern, allowing the separation of business logic from the graphical interface, making the code more modular, organized, and easier to maintain.

### Design Patterns Used

- **MVC** for separation between model, view, and controller
- **Command** to encapsulate player actions, including moves and undo/redo
- **Factory** for dynamic chess piece creation via `PieceType`
- **Singleton** to centralize log recording via `ModelLog`
- **Abstract classes** to define common behavior among pieces

## Main Structure

Some of the most relevant classes in the project:

- `ChessGameManager` — controls the state and logic of the game
- `ChessGame` — represents the internal state of the match
- `Board` — manages the board and interactions between pieces
- `Piece` and subclasses — represent the pieces and their respective moves
- `CommandManager` — manages the history for undo/redo
- `MovePieceCommand` — implements movement commands
- `ChessGameSerialization` — handles game serialization
- `ModelLog` — manages model logs
- `RootPane`, `ChessBoardJFX`, `PlayersInfoPane`, and `LogsJFX` — main UI components

## Implemented Features

- Core chess logic
- Game startup with player names
- Save and load game functionality
- End of game detection (checkmate and draw)
- Code organization
- Unit testing
- Special moves
- Audio feedback on certain game events
- Learning mode with highlighted possible moves, undo, and redo

## Technologies Used

- Java
- JavaFX
- Object-Oriented Programming (OOP)
- Serialization
- MVC
- Design Patterns

## Academic Context

Course: **Advanced Programming (2024/2025)**.
