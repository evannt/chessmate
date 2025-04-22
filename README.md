# :chess_pawn: Chessmate

### A functional chess engine written completely in Java! You can play against a friend or against a worthy AI opponent.

This project is a chess engine that can be used to play, practice, and improve your chess skills against your friends or a chess bot. The project inlcudes the following features:

* Two-player mode
* AI opponent mode
* Interactive UI
* Move generation and validation
* Move searching
* FEN Notation support
* Algebraic notation for moves

## :tv: Demo

![ChessmateMenuGif](https://github.com/user-attachments/assets/31320ecf-8d1c-4ef4-8b2e-415a54dae679)

![FoolsMateGif](https://github.com/user-attachments/assets/3bf2adcb-4f69-416c-819d-12cec825f67c)

![BBotGif](https://github.com/user-attachments/assets/9b7a81ab-8a9a-4f10-94ca-f1c6d1666369)

![WBotGif](https://github.com/user-attachments/assets/81813c05-9be2-4665-9e5c-a35eae7c1b9f)

## :inbox_tray: Installation and Instructions

### Clone the repository
``` bash
git clone https://github.com/evannt/Chessmate.git
```

### Compile and Run

``` bash
# Bash
javac -d ./bin src/**/*.java
java -cp ./bin main.Main
```



## :clipboard: Additional Details


The [Chess Programming Wiki](https://www.chessprogramming.org/Main_Page) was the primary resource when developing this project. It provided insights into board representations, move generation, search algorithms, and testing methodologies. The underlying chessboard is stored in the **bitboard** structure, offering a compact and efficient way to handle positions and operations through bitwise logic.

### ♟️ Move Generation & Validation

Moves generation utilizes bitboards, enabling fast enumeration of legal positions. Each piece type has its own movement and special rules like castling, en passant, and pawn promotion. The initial move generation yields pseudo-legal moves that may lead to illegal positions. These moves get filtered after generation to ensure legality (e.g., not moving into check).

### 🔍 Move Searching

The engine implements a **minimax** algorithm enhanced with **alpha-beta pruning** to explore the game tree efficiently. The algorithm evaluates potential moves several plies deep while minimizing unnecessary evaluations. The evaluation function considers material balance, mobility, and basic positional heuristics. This modular structure enables more advanced evaluation strategies (like piece-square tables or endgame tablebases) to be included later.

### 🧮 FEN Notation Support

The engine supports **Forsyth–Edwards Notation (FEN)**, allowing it to load and save board states at any point in the game. This notation is critical for debugging, testing, and implementing new features like "load from position" or puzzle modes.

Example FEN:

Start Position: `rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1`

Tricky Position: `r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1`
