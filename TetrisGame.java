import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class TetrisGame extends JPanel {
    private static final int ROWS = 20, COLS = 10, BLOCK_SIZE = 30;
    private int[][] gameBoard = new int[ROWS][COLS];
    private Block currentBlock;
    private Timer timer;
    private boolean gameOver = false;
    private int score = 0;
    private Random random = new Random();

    private static final int[][][] SHAPES = {
            {{1, 1, 1, 1}},  // I shape
            {{1, 1}, {1, 1}},  // O shape
            {{0, 1, 0}, {1, 1, 1}},  // T shape
            {{1, 1, 0}, {0, 1, 1}},  // S shape
            {{0, 1, 1}, {1, 1, 0}},  // Z shape
            {{1, 0, 0}, {1, 1, 1}},  // J shape
            {{0, 0, 1}, {1, 1, 1}}   // L shape
    };

    public TetrisGame() {
        setPreferredSize(new Dimension(COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE));
        setBackground(new Color(30, 30, 30));
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (currentBlock == null || gameOver) return;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        moveLeft();
                        break;
                    case KeyEvent.VK_RIGHT:
                        moveRight();
                        break;
                    case KeyEvent.VK_DOWN:
                        moveDown();
                        break;
                    case KeyEvent.VK_UP:
                        rotateBlock();
                        break;
                }
                repaint();
            }
        });
        startGame();
    }

    private void startGame() {
        gameOver = false;
        gameBoard = new int[ROWS][COLS]; // Reset the game board
        score = 0;
        generateNewBlock();
        timer = new Timer(600, e -> gameLoop());
        timer.start();
    }

    private void gameLoop() {
        if (!moveDown()) {
            lockBlock();
            if (checkGameOver()) {
                gameOver = true;
                timer.stop();
                int response = JOptionPane.showConfirmDialog(this, "Game Over! Score: " + score + "\nTry Again?", "Game Over", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    startGame();
                }
            } else {
                generateNewBlock();
            }
        }
        repaint();
    }

    private void generateNewBlock() {
        int[][] shape = SHAPES[random.nextInt(SHAPES.length)];
        currentBlock = new Block(shape, COLS / 2 - shape[0].length / 2, 0);
        if (!canMove(currentBlock.x, currentBlock.y, currentBlock.shape)) {
            gameOver = true; // Game over if the new block cannot spawn
        }
    }

    private boolean moveDown() {
        if (canMove(currentBlock.x, currentBlock.y + 1, currentBlock.shape)) {
            currentBlock.y++;
            return true;
        }
        return false;
    }

    private void moveLeft() {
        if (canMove(currentBlock.x - 1, currentBlock.y, currentBlock.shape)) {
            currentBlock.x--;
        }
    }

    private void moveRight() {
        if (canMove(currentBlock.x + 1, currentBlock.y, currentBlock.shape)) {
            currentBlock.x++;
        }
    }

    private void rotateBlock() {
        int[][] rotated = rotate(currentBlock.shape);
        if (canMove(currentBlock.x, currentBlock.y, rotated)) {
            currentBlock.shape = rotated;
        }
    }

    private boolean canMove(int newX, int newY, int[][] shape) {
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] == 1) {
                    int boardX = newX + j;
                    int boardY = newY + i;
                    if (boardX < 0 || boardX >= COLS || boardY >= ROWS || (boardY >= 0 && gameBoard[boardY][boardX] == 1)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void lockBlock() {
        for (int i = 0; i < currentBlock.shape.length; i++) {
            for (int j = 0; j < currentBlock.shape[i].length; j++) {
                if (currentBlock.shape[i][j] == 1) {
                    gameBoard[currentBlock.y + i][currentBlock.x + j] = 1;
                }
            }
        }
        clearRows();
    }

    private void clearRows() {
        for (int row = ROWS - 1; row >= 0; row--) {
            boolean fullRow = true;
            for (int col = 0; col < COLS; col++) {
                if (gameBoard[row][col] == 0) {
                    fullRow = false;
                    break;
                }
            }
            if (fullRow) {
                for (int r = row; r > 0; r--) {
                    System.arraycopy(gameBoard[r - 1], 0, gameBoard[r], 0, COLS);
                }
                gameBoard[0] = new int[COLS];
                score += 100; // Increase score for each cleared row
                row++; // Recheck the current row after shifting
            }
        }
    }

    private boolean checkGameOver() {
        for (int col = 0; col < COLS; col++) {
            if (gameBoard[0][col] == 1) return true; // Game over if the top row is occupied
        }
        return false;
    }

    private int[][] rotate(int[][] shape) {
        int rows = shape.length, cols = shape[0].length;
        int[][] rotated = new int[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotated[j][rows - 1 - i] = shape[i][j];
            }
        }
        return rotated;
    }

    private static class Block {
        int[][] shape;
        int x, y;

        Block(int[][] shape, int x, int y) {
            this.shape = shape;
            this.x = x;
            this.y = y;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);

        // Draw the game board
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (gameBoard[row][col] == 1) {
                    g.setColor(new Color(0, 128, 255)); // Light blue for locked blocks
                    g.fillRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }

        // Draw the current block
        if (currentBlock != null) {
            g.setColor(new Color(255, 165, 0)); // Orange for the current block
            for (int i = 0; i < currentBlock.shape.length; i++) {
                for (int j = 0; j < currentBlock.shape[i].length; j++) {
                    if (currentBlock.shape[i][j] == 1) {
                        g.fillRect((currentBlock.x + j) * BLOCK_SIZE, (currentBlock.y + i) * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                        g.setColor(Color.BLACK);
                        g.drawRect((currentBlock.x + j) * BLOCK_SIZE, (currentBlock.y + i) * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris Game");
        TetrisGame game = new TetrisGame();
        
        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        JButton leftButton = new JButton("← Left");
        JButton rightButton = new JButton("Right →");
        JButton restartButton = new JButton("Restart");

        // Add action listeners to buttons
        leftButton.addActionListener(e -> {
            if (!game.gameOver) {
                game.moveLeft();
                game.repaint();
            }
        });

        rightButton.addActionListener(e -> {
            if (!game.gameOver) {
                game.moveRight();
                game.repaint();
            }
        });

        restartButton.addActionListener(e -> game.startGame());

        // Add buttons to the button panel
        buttonPanel.add(leftButton);
        buttonPanel.add(rightButton);
        buttonPanel.add(restartButton);

        // Add game panel and button panel to the frame
        frame.setLayout(new BorderLayout());
        frame.add(game, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
