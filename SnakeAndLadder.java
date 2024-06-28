import java.io.File; // Import File dari paket java.io
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Comparator;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;

public class SnakeAndLadder {
    private ArrayList<Player> players;
    private ArrayList<Snake> snakes;
    private ArrayList<Ladder> ladders;
    private int boardSize;
    private int status;
    private int playerInTurn;
    private int[] turnCounts;

    public SnakeAndLadder(int boardSize, int playerCount) {
        this.boardSize = boardSize;
        this.players = new ArrayList<>();
        this.snakes = new ArrayList<>();
        this.ladders = new ArrayList<>();
        this.status = 0;
        this.turnCounts = new int[playerCount];
    }

    public void initiateGame() {
        // Set the default ladders
        int[][] defaultLadders = {{2, 23}, {8, 34}, {20, 77}, {32, 68}, {41, 79}, {74, 88}, {82, 100}, {85, 95}};
        addLadders(defaultLadders);

        // Set the default snakes
        int[][] defaultSnakes = {{29, 9}, {38, 15}, {47, 5}, {53, 33}, {62, 37}, {86, 54}, {92, 70}, {97, 25}};
        addSnakes(defaultSnakes);
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setPlayerInTurn(int playerInTurn) {
        this.playerInTurn = playerInTurn;
    }

    public void addPlayer(Player p) {
        this.players.add(p);
    }

    public void addSnake(Snake s) {
        this.snakes.add(s);
    }

    public void addSnakes(int[][] s) {
        for (int[] snake : s) {
            Snake newSnake = new Snake(snake[0], snake[1]);
            this.snakes.add(newSnake);
        }
    }

    public void addLadder(Ladder l) {
        this.ladders.add(l);
    }

    public void addLadders(int[][] l) {
        for (int[] ladder : l) {
            Ladder newLadder = new Ladder(ladder[0], ladder[1]);
            this.ladders.add(newLadder);
        }
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getStatus() {
        return status;
    }

    public int getPlayerInTurn() {
        return playerInTurn;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Snake> getSnakes() {
        return snakes;
    }

    public ArrayList<Ladder> getLadders() {
        return ladders;
    }

    public void printBoardMap() {
        System.out.println("Board Map:");

        for (int row = 10; row >= 1; row--) {
            System.out.print(row + " | ");

            for (int col = 1; col <= 10; col++) {
                int position = (row - 1) * 10 + col;
                boolean playerHere = false;

                for (Player player : players) {
                    if (player.getPosition() == position) {
                        System.out.print("P" + players.indexOf(player) + " ");
                        playerHere = true;
                        break;
                    }
                }

                if (!playerHere) {
                    boolean snakeOrLadder = false;

                    for (Snake snake : snakes) {
                        if (snake.getHead() == position) {
                            System.out.print("S ");
                            snakeOrLadder = true;
                            break;
                        }
                    }

                    for (Ladder ladder : ladders) {
                        if (ladder.getBottom() == position) {
                            System.out.print("L ");
                            snakeOrLadder = true;
                            break;
                        }
                    }

                    if (!snakeOrLadder) {
                        System.out.print("- ");
                    }
                }
            }

            System.out.println();
        }

        System.out.print("   ");
        for (int col = 1; col <= 10; col++) {
            System.out.print(col + " ");
        }
        System.out.println();
    }

    public int getTurn() {
        if (this.status == 0) {
            return (int) (Math.random() * players.size());
        } else {
            return (playerInTurn + 1) % players.size();
        }
    }

    public void movePlayer(Player p, int x) {
        p.moveAround(x, this.boardSize);

        for (Ladder ladder : ladders) {
            if (p.getPosition() == ladder.getBottom()) {
                p.setPosition(ladder.getTop());
                System.out.println(p.getUserName() + " climbed up a ladder from " + ladder.getBottom() + " to " + ladder.getTop());
            }
        }

        for (Snake snake : snakes) {
            if (p.getPosition() == snake.getHead()) {
                p.setPosition(snake.getTail());
                System.out.println(p.getUserName() + " got bitten by a snake from " + snake.getHead() + " to " + snake.getTail());
            }
        }

        System.out.println(p.getUserName() + " is now at position " + p.getPosition());

        if (p.getPosition() == this.boardSize) {
            this.status = 2;
            System.out.println("Game over! " + p.getUserName() + " has won.");
        }
    }

    public void sortPlayersByPosition() {
        players.sort(Comparator.comparingInt(Player::getPosition).reversed());
    }

    public void displayPlayerPositions() {
        System.out.println("Current Player Positions:");
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            System.out.println((i + 1) + ". " + player.getUserName() + " - Position: " + player.getPosition());
        }
    }

    public void play() {
        Scanner sc = new Scanner(System.in);

        System.out.println("How many players are playing? :");
        int playerCount = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < playerCount; i++) {
            System.out.println("Player " + (i + 1) + ", please enter your Username : ");
            String username = sc.nextLine();
            players.add(new Player(username));
        }

        Collections.shuffle(players);
        initiateGame();

        do {
            int turnIndex = getTurn();
            System.out.println();
            System.out.println("Turn: " + (turnIndex + 1));

            setStatus(1);
            setPlayerInTurn(turnIndex);

            Player playerInTurn = players.get(turnIndex);
            System.out.println("Now, the player in turn is " + playerInTurn.getUserName());

            System.out.println(playerInTurn.getUserName() + ", press ENTER to roll the dice!");
            String input = sc.nextLine();
            int diceRoll = 0;
            if (input.isEmpty()) {
                playSound("dice_roll.wav");
                diceRoll = playerInTurn.rollDice();
            }

            System.out.println("The dice shows number " + diceRoll + "!!");
            movePlayer(playerInTurn, diceRoll);

            turnCounts[turnIndex]++;

            System.out.println(playerInTurn.getUserName() + " has completed " + turnCounts[turnIndex] + " turns.");

            printBoardMap();

            // Sort and display player positions
            sortPlayersByPosition();
            displayPlayerPositions();

        } while (getStatus() != 2);

        sc.close();
    }

    private void playSound(String soundFile) {
        try {
            File file = new File(soundFile);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SnakeAndLadder game = new SnakeAndLadder(100, 0); // 0 here is just a placeholder for player count
        game.play();
    }
}
