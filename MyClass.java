import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Random;

public class MyClass {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(SnakeGame::new);
    }
}

class SnakeGame extends JFrame {
    public SnakeGame() {
        setTitle("Simple Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        SnakeBoard board = new SnakeBoard();
        add(board);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

class SnakeBoard extends JPanel implements ActionListener, KeyListener {
    public static final int BOARD_WIDTH = 800;
    public static final int BOARD_HEIGHT = 700;
    public static final int TILE_SIZE = 20;
    public static final int DELAY = 90;

    private Timer timer;
    private Snake snake;
    private Food food;
    private int score;

    public SnakeBoard() {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        snake = new Snake();
        food = new Food();
        score = 0;

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        snake.draw(g);
        food.draw(g);

        // Display score on the window
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        snake.move();

        if (snake.collidesWith(food)) {
            snake.eat(food);
            food.generateFood();
            score += 10; // Increase the score when the snake eats food
        }

        if (snake.checkCollision()) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game Over!\nYour Score: " + score, "Game Over", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_LEFT:
                snake.setDirection(Direction.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                snake.setDirection(Direction.RIGHT);
                break;
            case KeyEvent.VK_UP:
                snake.setDirection(Direction.UP);
                break;
            case KeyEvent.VK_DOWN:
                snake.setDirection(Direction.DOWN);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}

enum Direction {
    UP, DOWN, LEFT, RIGHT
}

class Snake {
    private LinkedList<Point> body;
    private Direction direction;

    public Snake() {
        body = new LinkedList<>();
        body.add(new Point(3, 0));
        body.add(new Point(2, 0));
        body.add(new Point(1, 0));
        direction = Direction.RIGHT;
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        for (Point p : body) {
            g.fillRect(p.x * SnakeBoard.TILE_SIZE, p.y * SnakeBoard.TILE_SIZE, SnakeBoard.TILE_SIZE, SnakeBoard.TILE_SIZE);
        }
    }

    public void move() {
        Point head = body.getFirst();
        Point newHead;

        switch (direction) {
            case UP:
                newHead = new Point(head.x, (head.y - 1 + SnakeBoard.BOARD_HEIGHT / SnakeBoard.TILE_SIZE) % (SnakeBoard.BOARD_HEIGHT / SnakeBoard.TILE_SIZE));
                break;
            case DOWN:
                newHead = new Point(head.x, (head.y + 1) % (SnakeBoard.BOARD_HEIGHT / SnakeBoard.TILE_SIZE));
                break;
            case LEFT:
                newHead = new Point((head.x - 1 + SnakeBoard.BOARD_WIDTH / SnakeBoard.TILE_SIZE) % (SnakeBoard.BOARD_WIDTH / SnakeBoard.TILE_SIZE), head.y);
                break;
            case RIGHT:
                newHead = new Point((head.x + 1) % (SnakeBoard.BOARD_WIDTH / SnakeBoard.TILE_SIZE), head.y);
                break;
            default:
                newHead = head;
        }

        body.addFirst(newHead);
        body.removeLast();
    }

    public void setDirection(Direction direction) {
       
        if ((this.direction == Direction.LEFT && direction != Direction.RIGHT)
                || (this.direction == Direction.RIGHT && direction != Direction.LEFT)
                || (this.direction == Direction.UP && direction != Direction.DOWN)
                || (this.direction == Direction.DOWN && direction != Direction.UP)) {
            this.direction = direction;
        }
    }

    public boolean collidesWith(Food food) {
        return body.getFirst().equals(food.getPosition());
    }

    public boolean checkCollision() {
        Point head = body.getFirst();
        for (int i = 1; i < body.size(); i++) {
            if (head.equals(body.get(i))) {
                return true;
            }
        }
        return false;
    }

    public void eat(Food food) {
        
        body.addLast(new Point(-1, -1));
    }
}

class Food {
    private Point position;

    public Food() {
        generateFood();
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(position.x * SnakeBoard.TILE_SIZE, position.y * SnakeBoard.TILE_SIZE, SnakeBoard.TILE_SIZE, SnakeBoard.TILE_SIZE);
    }

    public void generateFood() {
        Random random = new Random();
        int x = random.nextInt(SnakeBoard.BOARD_WIDTH / SnakeBoard.TILE_SIZE);
        int y = random.nextInt(SnakeBoard.BOARD_HEIGHT / SnakeBoard.TILE_SIZE);
        position = new Point(x, y);
    }

    public Point getPosition() {
        return position;
    }
}
