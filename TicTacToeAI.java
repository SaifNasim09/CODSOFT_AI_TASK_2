import java.util.Scanner;

public class TicTacToeAI {
    private static final char HUMAN = 'X';
    private static final char AI = 'O';
    private static final char EMPTY = ' ';
    private static final Scanner SCANNER = new Scanner(System.in);

    private static final int[][] WINNING_LINES = {
        {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
        {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
        {0, 4, 8}, {2, 4, 6}
    };

    public static void main(String[] args) {
        printWelcome();

        boolean playAgain = true;
        while (playAgain) {
            char[] board = new char[9];
            for (int i = 0; i < board.length; i++) {
                board[i] = EMPTY;
            }

            System.out.println("\nChoose who plays first:");
            System.out.println("1. You (X)");
            System.out.println("2. AI (O)");
            int first = readIntInRange("Enter 1 or 2: ", 1, 2);
            boolean humanTurn = first == 1;

            while (true) {
                printBoard(board);

                if (humanTurn) {
                    int move = readHumanMove(board);
                    board[move] = HUMAN;
                } else {
                    System.out.println("\nAI is thinking...");
                    int move = findBestMove(board);
                    board[move] = AI;
                    System.out.println("AI chose position " + (move + 1) + ".");
                }

                if (hasWon(board, humanTurn ? HUMAN : AI)) {
                    printBoard(board);
                    if (humanTurn) {
                        System.out.println("\nYou won! Great job.");
                    } else {
                        System.out.println("\nAI wins! Better luck next time.");
                    }
                    break;
                }

                if (isBoardFull(board)) {
                    printBoard(board);
                    System.out.println("\nIt's a draw!");
                    break;
                }

                humanTurn = !humanTurn;
            }

            System.out.print("\nWould you like to play again? (y/n): ");
            String answer = SCANNER.nextLine().trim().toLowerCase();
            playAgain = answer.equals("y") || answer.equals("yes");
        }

        System.out.println("\nThanks for playing Tic-Tac-Toe AI!");
        SCANNER.close();
    }

    private static void printWelcome() {
        System.out.println("======================================");
        System.out.println("          TIC-TAC-TOE AI");
        System.out.println("======================================");
        System.out.println("You are X and the AI is O.");
        System.out.println("Choose a position from 1 to 9.");
        System.out.println("The AI uses the Minimax algorithm.");
    }

    private static void printBoard(char[] board) {
        System.out.println();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int index = row * 3 + col;
                char display = board[index] == EMPTY
                        ? (char) ('1' + index)
                        : board[index];
                System.out.print(" " + display + " ");
                if (col < 2) {
                    System.out.print("|");
                }
            }
            System.out.println();
            if (row < 2) {
                System.out.println("---+---+---");
            }
        }
    }

    private static int readHumanMove(char[] board) {
        while (true) {
            int position = readIntInRange("\nYour move (1-9): ", 1, 9);
            int index = position - 1;

            if (board[index] == EMPTY) {
                return index;
            }

            System.out.println("That position is already occupied. Choose an empty position.");
        }
    }

    private static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine().trim();

            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // Ask again for invalid non-numeric input.
            }

            System.out.println("Invalid input. Please enter a number from " + min + " to " + max + ".");
        }
    }

    private static int findBestMove(char[] board) {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;

        // Try each available move and choose the one with the best Minimax score.
        for (int i = 0; i < board.length; i++) {
            if (board[i] == EMPTY) {
                board[i] = AI;
                int score = minimax(board, 0, false);
                board[i] = EMPTY;

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }

        return bestMove;
    }

    private static int minimax(char[] board, int depth, boolean maximizing) {
        if (hasWon(board, AI)) {
            return 10 - depth;
        }
        if (hasWon(board, HUMAN)) {
            return depth - 10;
        }
        if (isBoardFull(board)) {
            return 0;
        }

        if (maximizing) {
            int bestScore = Integer.MIN_VALUE;

            for (int i = 0; i < board.length; i++) {
                if (board[i] == EMPTY) {
                    board[i] = AI;
                    int score = minimax(board, depth + 1, false);
                    board[i] = EMPTY;
                    bestScore = Math.max(bestScore, score);
                }
            }

            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;

            for (int i = 0; i < board.length; i++) {
                if (board[i] == EMPTY) {
                    board[i] = HUMAN;
                    int score = minimax(board, depth + 1, true);
                    board[i] = EMPTY;
                    bestScore = Math.min(bestScore, score);
                }
            }

            return bestScore;
        }
    }

    private static boolean hasWon(char[] board, char player) {
        for (int[] line : WINNING_LINES) {
            if (board[line[0]] == player
                    && board[line[1]] == player
                    && board[line[2]] == player) {
                return true;
            }
        }
        return false;
    }

    private static boolean isBoardFull(char[] board) {
        for (char cell : board) {
            if (cell == EMPTY) {
                return false;
            }
        }
        return true;
    }
}
