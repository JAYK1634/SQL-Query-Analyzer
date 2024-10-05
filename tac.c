#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#define SIZE 3

// Function declarations
void printBoard(char board[SIZE][SIZE]);
int checkWin(char board[SIZE][SIZE]);
void playerMove(char board[SIZE][SIZE], int player);
void computerMove(char board[SIZE][SIZE]);

int main() {
    char board[SIZE][SIZE] = { {' ', ' ', ' '},
                               {' ', ' ', ' '},
                               {' ', ' ', ' '} };
    int player = 1;
    int result = 0;
    int mode;

    printf("Welcome to Tic-Tac-Toe!\n");
    printf("Choose mode: 1 for Player vs Player, 2 for Player vs Computer: ");
    while (scanf("%d", &mode) != 1 || (mode != 1 && mode != 2)) {
        printf("Invalid input. Please enter 1 or 2: ");
        while (getchar() != '\n'); // Clear the input buffer
    }

    // Seed random number generator
    srand(time(0));

    while (result == 0) {
        printBoard(board);
        if (mode == 1) {
            playerMove(board, player);
        } else {
            if (player == 1) {
                playerMove(board, player);
            } else {
                computerMove(board);
            }
        }
        result = checkWin(board);
        player = (player % 2) + 1; // Switch player/computer
    }

    printBoard(board);

    if (result == 1) {
        printf("Player %d wins!\n", (player % 2) + 1);
    } else {
        printf("It's a draw!\n");
    }

    return 0;
}

void printBoard(char board[SIZE][SIZE]) {
    printf("Current board:\n");
    for (int i = 0; i < SIZE; i++) {
        for (int j = 0; j < SIZE; j++) {
            printf(" %c ", board[i][j]);
            if (j < SIZE - 1) printf("|");
        }
        printf("\n");
        if (i < SIZE - 1) printf("---|---|---\n");
    }
}

int checkWin(char board[SIZE][SIZE]) {
    // Check rows and columns
    for (int i = 0; i < SIZE; i++) {
        if ((board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != ' ') ||
            (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != ' ')) {
            return 1;
        }
    }

    // Check diagonals
    if ((board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != ' ') ||
        (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != ' ')) {
        return 1;
    }

    // Check for draw
    for (int i = 0; i < SIZE; i++) {
        for (int j = 0; j < SIZE; j++) {
            if (board[i][j] == ' ') {
                return 0;
            }
        }
    }

    return -1;
}

void playerMove(char board[SIZE][SIZE], int player) {
    int row, col;
    char mark = (player == 1) ? 'X' : 'O';

    while (1) {
        printf("Player %d, enter your move (row and column): ", player);
        if (scanf("%d %d", &row, &col) != 2 || row < 0 || row >= SIZE || col < 0 || col >= SIZE || board[row][col] != ' ') {
            printf("Invalid input. Please enter valid row and column numbers (0-2) for an empty cell.\n");
            while (getchar() != '\n'); // Clear the input buffer
        } else {
            board[row][col] = mark;
            break;
        }
    }
}

void computerMove(char board[SIZE][SIZE]) {
    int row, col;

    // Function to check if a player can win with one move
    int canWin(char b[SIZE][SIZE], char mark) {
        for (int i = 0; i < SIZE; i++) {
            if (b[i][0] == mark && b[i][1] == mark && b[i][2] == ' ') return i * SIZE + 2;
            if (b[i][0] == mark && b[i][2] == mark && b[i][1] == ' ') return i * SIZE + 1;
            if (b[i][1] == mark && b[i][2] == mark && b[i][0] == ' ') return i * SIZE;
            if (b[0][i] == mark && b[1][i] == mark && b[2][i] == ' ') return 2 * SIZE + i;
            if (b[0][i] == mark && b[2][i] == mark && b[1][i] == ' ') return SIZE + i;
            if (b[1][i] == mark && b[2][i] == mark && b[0][i] == ' ') return i;
        }
        if (b[0][0] == mark && b[1][1] == mark && b[2][2] == ' ') return 2 * SIZE + 2;
        if (b[0][0] == mark && b[2][2] == mark && b[1][1] == ' ') return SIZE + SIZE + 1;
        if (b[1][1] == mark && b[2][2] == mark && b[0][0] == ' ') return 0;
        if (b[0][2] == mark && b[1][1] == mark && b[2][0] == ' ') return 2 * SIZE;
        if (b[0][2] == mark && b[2][0] == mark && b[1][1] == ' ') return SIZE + SIZE + 1;
        if (b[1][1] == mark && b[2][0] == mark && b[0][2] == ' ') return 2;
        return -1;
    }

    // Check if computer can win
    int move = canWin(board, 'O');
    if (move == -1) {
        // Block player win
        move = canWin(board, 'X');
    }
    if (move == -1) {
        // Otherwise make a random move
        do {
            row = rand() % SIZE;
            col = rand() % SIZE;
        } while (board[row][col] != ' ');
    } else {
        row = move / SIZE;
        col = move % SIZE;
    }

    board[row][col] = 'O';
    printf("Computer chose: %d %d\n", row, col);
}
