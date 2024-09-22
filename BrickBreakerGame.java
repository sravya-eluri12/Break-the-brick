import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Graphics2D;
import javax.swing.Timer;

// import javax.swing.*;  used for creating the GUI(Graphical User Interface)
// import java.awt.*;  awt-abstract windowing toolkit-used for drawing shapes and colors
// import java.awt.event.*; used to handle events like key presses, mouse button
// Java AWT is an API used to create GUI or window-based Java programs.
// Event is a platform-independent class that encapsulates events from the platform's GUI - signal received by a program from the OS as a result of some actino taken by the user
// import java.util.Random; used for generating random numbers (ball movement)

// Main class
public class BrickBreakerGame {
    public static void main(String[] args) {
        JFrame obj = new JFrame(); //JFrame: The main window for the game.
        GPanel game = new GPanel();
        obj.setBounds(10, 10, 700, 600);
        obj.setTitle("Break the Brick");
        obj.setResizable(false);
        obj.setVisible(true);        // Displays the window.
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //setDef.. Ensures the program exits when the window is closed
        obj.add(game);
    }  
}


// The type GPanel must implement the inherited abstract method
// ActionListener.actionPerformed(ActionEvent)
// You need to add the actionPerformed method in your GPanel class. This method
// is where you will define what happens when an action event occurs, such as
// when the timer ticks
// Panel where game components are rendered
class GPanel extends JPanel implements ActionListener {
    // class uses JPanel, which is a container that can hold graphical components
    // implements ActionListener - class will handle action events (like timer updates)
    private boolean play = false;
    private int score = 0;
    private int totalBricks = 21;
    private Timer timer;  // Timer is used to control the game's update rate
    private int delay = 8;
    private int paddleX = 310;
    private int ballPositionX = 120;  //current position of the ball at X axis
    private int ballPositionY = 350;
    private int ballDirectionX = -1;  //Direction of the ball's movement
    private int ballDirectionY = -2;
    private BrickMap brickMap;

    public GPanel() {
        brickMap = new BrickMap(3, 7);
        //addKeyListener: Listens for key presses to move the paddle.
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    if (paddleX >= 600) {
                        paddleX = 600;
                    } else {
                        moveRight();
                    }
                }

                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    if (paddleX <= 10) {
                        paddleX = 10;
                    } else {
                        moveLeft();
                    }
                }

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (!play) {
                        play = true;
                        ballPositionX = 120;
                        ballPositionY = 350;
                        ballDirectionX = -1;
                        ballDirectionY = -2;
                        paddleX = 310;
                        score = 0;
                        totalBricks = 21;
                        brickMap = new BrickMap(3, 7);
                        repaint();  // Calls the paint method to update the display.
                    }
                }
            }
        });
        setFocusable(true);      //Allows the panel to receive key events.
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        // Background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        // Drawing the bricks
        brickMap.draw((Graphics2D) g);

        // Borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);       //g.fillRect: Draws the paddle.
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // Paddle
        g.setColor(Color.green);
        g.fillRect(paddleX, 550, 100, 8);

        // Ball
        g.setColor(Color.yellow);
        g.fillOval(ballPositionX, ballPositionY, 20, 20);  //g.fillOval: Draws the ball.

        // Score
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("" + score, 590, 30);       //g.drawString: Displays the current score.

        // When you win the game
        if (totalBricks <= 0) {
            play = false;
            ballDirectionX = 0;
            ballDirectionY = 0;
            g.setColor(Color.green);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You Won, Score: " + score, 260, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }
        // When you lose the game
        if (ballPositionY > 570) {
            play = false;
            ballDirectionX = 0;
            ballDirectionY = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over, Score: " + score, 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        // When you lose the game
        if (ballPositionY > 570) {
            play = false;
            ballDirectionX = 0;
            ballDirectionY = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over, Score: " + score, 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        if (play) {
            if (new java.awt.Rectangle(ballPositionX, ballPositionY, 20, 20)
                    .intersects(new java.awt.Rectangle(paddleX, 550, 100, 8))) {
                ballDirectionY = -ballDirectionY;
            }

            A: for (int i = 0; i < brickMap.bricks.length; i++) {
                for (int j = 0; j < brickMap.bricks[0].length; j++) {
                    if (brickMap.bricks[i][j] > 0) {
                        int brickX = j * brickMap.brickWidth + 80;
                        int brickY = i * brickMap.brickHeight + 50;
                        int brickWidth = brickMap.brickWidth;
                        int brickHeight = brickMap.brickHeight;

                        java.awt.Rectangle rect = new java.awt.Rectangle(brickX, brickY, brickWidth, brickHeight);
                        java.awt.Rectangle ballRect = new java.awt.Rectangle(ballPositionX, ballPositionY, 20, 20);
                        java.awt.Rectangle brickRect = rect;

                        if (ballRect.intersects(brickRect)) {
                            brickMap.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if (ballPositionX + 19 <= brickRect.x || ballPositionX + 1 >= brickRect.x + brickRect.width) {
                                ballDirectionX = -ballDirectionX;
                            } else {
                                ballDirectionY = -ballDirectionY;
                            }

                            break A;
                        }
                    }
                }
            }

            ballPositionX += ballDirectionX;
            ballPositionY += ballDirectionY;

            if (ballPositionX < 0) {
                ballDirectionX = -ballDirectionX;
            }
            if (ballPositionY < 0) {
                ballDirectionY = -ballDirectionY;
            }
            if (ballPositionX > 670) {
                ballDirectionX = -ballDirectionX;
            }

            repaint();
        }
    }

    public void moveRight() {
        play = true;
        paddleX += 20;
    }

    public void moveLeft() {
        play = true;
        paddleX -= 20;
    }
}
// Brick Map class to manage the bricks
class BrickMap {
    public int bricks[][];
    public int brickWidth;
    public int brickHeight;

    public BrickMap(int row, int col) {
        bricks = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                bricks[i][j] = 1;
            }
        }
        brickWidth = 540 / col;
        brickHeight = 150 / row;
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[0].length; j++) {
                if (bricks[i][j] > 0) {
                    g.setColor(Color.white);
                    g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);

                    g.setStroke(new java.awt.BasicStroke(3));
                    g.setColor(Color.black);
                    g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                }
            }
        }
    }

    public void setBrickValue(int value, int row, int col) {
        bricks[row][col] = value;
    }
}
