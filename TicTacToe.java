import java.util.Scanner;

public class TicTacToe {
    static final int GRID_SIZE = 3;
    static char[][] gameBoard = new char[GRID_SIZE][GRID_SIZE];
    static final char USER_SYMBOL = 'X';
    static final char AI_SYMBOL = 'O';

    public static void main(String[] args) {
        setupBoard();
        Scanner inputScanner = new Scanner(System.in);
        int gameMode;

        System.out.println("Welcome to the Ultimate Tic-Tac-Toe!");
        System.out.print("Select mode: 1 for Player vs Player, 2 for Player vs AI: ");
        gameMode = inputScanner.nextInt();

        if (gameMode == 1) {
            
            twoPlayerMode(inputScanner);
        } else {
            playerVsAIMode(inputScanner);
        }
        inputScanner.close();
    }

    public static void setupBoard() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                gameBoard[i][j] = '-';
            }
        }
    }

    public static void displayBoard() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                System.out.print(" " + gameBoard[i][j] + " ");
                if (j < GRID_SIZE - 1) System.out.print("|");
            }
            System.out.println();
            if (i < GRID_SIZE - 1) System.out.println("---|---|---");
        }
        System.out.println();
    }

    public static int determineWinner() {
        // Check rows and columns for a win
        for (int i = 0; i < GRID_SIZE; i++) {
            if (gameBoard[i][0] == gameBoard[i][1] && gameBoard[i][1] == gameBoard[i][2] && gameBoard[i][0] != '-') return gameBoard[i][0];
            if (gameBoard[0][i] == gameBoard[1][i] && gameBoard[1][i] == gameBoard[2][i] && gameBoard[0][i] != '-') return gameBoard[0][i];
        }
        // Check diagonals for a win
        if (gameBoard[0][0] == gameBoard[1][1] && gameBoard[1][1] == gameBoard[2][2] && gameBoard[0][0] != '-') return gameBoard[0][0];
        if (gameBoard[0][2] == gameBoard[1][1] && gameBoard[1][1] == gameBoard[2][0] && gameBoard[0][2] != '-') return gameBoard[0][2];

        // Check if game is still ongoing or it's a draw
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (gameBoard[i][j] == '-') return '-'; // Game still ongoing
            }
        }
        return 'T'; // Game ends in a tie
    }

    public static void twoPlayerMode(Scanner inputScanner) {
        int activePlayer = 1;
        char symbol;
        while (true) {
            displayBoard();
            symbol = (activePlayer == 1) ? USER_SYMBOL : AI_SYMBOL;
            playerTurn(inputScanner, activePlayer, symbol);
            int gameOutcome = determineWinner();
            if (gameOutcome != '-') {
                displayBoard();
                if (gameOutcome == USER_SYMBOL) {
                    System.out.println("Player 1 triumphs!");
                } else if (gameOutcome == AI_SYMBOL) {
                    System.out.println("Player 2 takes the victory!");
                } else {
                    System.out.println("It's a stalemate!");
                }
                break;
            }
            activePlayer = (activePlayer % 2) + 1; // Switch player
        }
    }

    public static void playerVsAIMode(Scanner inputScanner) {
        while (true) {
            displayBoard();
            playerTurn(inputScanner, 1, USER_SYMBOL);
            int gameOutcome = determineWinner();
            if (gameOutcome != '-') {
                displayBoard();
                if (gameOutcome == USER_SYMBOL) {
                    System.out.println("Congratulations! You win!");
                } else if (gameOutcome == AI_SYMBOL) {
                    System.out.println("The AI outsmarted you!");
                } else {
                    System.out.println("It’s a draw!");
                }
                break;
            }
            System.out.println("AI's move:");
            aiMove();
            gameOutcome = determineWinner();
            if (gameOutcome != '-') {
                displayBoard();
                if (gameOutcome == USER_SYMBOL) {
                    System.out.println("You won!");
                } else if (gameOutcome == AI_SYMBOL) {
                    System.out.println("The AI wins!");
                } else {
                    System.out.println("It’s a tie!");
                }
                break;
            }
        }
    }

    public static void playerTurn(Scanner inputScanner, int player, char symbol) {
        int row, col;
        while (true) {
            System.out.print("Input your move (row [0-2] and column [0-2]): ");
            row = inputScanner.nextInt();
            col = inputScanner.nextInt();
            if (row >= 0 && row < GRID_SIZE && col >= 0 && col < GRID_SIZE && gameBoard[row][col] == '-') {
                gameBoard[row][col] = symbol;
                break;
            } else {
                System.out.println("Invalid move, try again.");
            }
        }
    }

    public static void aiMove() {
        int optimalScore = Integer.MIN_VALUE;
        int[] optimalMove = new int[2];

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (gameBoard[i][j] == '-') {
                    gameBoard[i][j] = AI_SYMBOL;
                    int moveScore = minimax(gameBoard, 0, false);
                    gameBoard[i][j] = '-';
                    if (moveScore > optimalScore) {
                        optimalScore = moveScore;
                        optimalMove[0] = i;
                        optimalMove[1] = j;
                    }
                }
            }
        }
        gameBoard[optimalMove[0]][optimalMove[1]] = AI_SYMBOL;
        System.out.println("AI chose: " + optimalMove[0] + " " + optimalMove[1]);
    }

    public static int minimax(char[][] boardState, int depth, boolean maximizingPlayer) {
        int result = determineWinner();
        if (result != '-') {
            if (result == USER_SYMBOL) {
                return -10 + depth;
            } else if (result == AI_SYMBOL) {
                return 10 - depth;
            } else {
                return 0;
            }
        }

        if (maximizingPlayer) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    if (boardState[i][j] == '-') {
                        boardState[i][j] = AI_SYMBOL;
                        int score = minimax(boardState, depth + 1, false);
                        boardState[i][j] = '-';
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    if (boardState[i][j] == '-') {
                        boardState[i][j] = USER_SYMBOL;
                        int score = minimax(boardState, depth + 1, true);
                        boardState[i][j] = '-';
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }
}
