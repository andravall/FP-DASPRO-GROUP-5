/**
 * -----------------------------------------------------
 * ES234211 - Programming Fundamental
 * Genap - 2023/2024
 * Group Capstone Project: Snake and Ladder Game
 * -----------------------------------------------------
 * Class    : C
 * Group    : 5
 * Members  :
 * 1. 5026231159 - Mohammad Ferdinand Valliandra
 * 2. 5026231207 - Achmad Andi Miftakhul Huda
 * 3. 5026231225 - Harbima Razan Adhitya
 * ------------------------------------------------------
 */

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Meminta jumlah pemain
        System.out.println("How many players are playing? :");
        int playerCount = sc.nextInt();
        sc.nextLine(); // Menangkap newline character setelah nextInt()

        // Membuat daftar pemain
        ArrayList<Player> players = new ArrayList<>();

        // Meminta nama pengguna untuk setiap pemain
        for (int i = 0; i < playerCount; i++) {
            System.out.println("Player " + (i + 1) + ", please enter your Username : ");
            String username = sc.nextLine();
            players.add(new Player(username));
        }

        // Merandomisasi urutan pemain
        Collections.shuffle(players);

        // Initialize the game with board size and initial number of ladders/snakes
        SnakeAndLadder game = new SnakeAndLadder(100, 8);
        game.initiateGame();

        System.out.println("Do you want to add Ladders? (yes/no):");
        System.out.println("The default board has 8 Ladders");
        String addLaddersOption = sc.nextLine();

        if (addLaddersOption.equalsIgnoreCase("yes")) {
            System.out.println("How many ladders? :");
            int ladderCount = sc.nextInt();

            for (int i = 0; i < ladderCount; i++) {
                System.out.println("Enter start and end positions for ladder " + (i + 1) + ": ");
                int bottom = sc.nextInt();
                int top = sc.nextInt();
                game.addLadder(new Ladder(bottom, top));
            }
            sc.nextLine(); // Menangkap newline character setelah nextInt()
        }

        // Opsi untuk menambahkan ular
        System.out.println("Do you want to add Snakes? (yes/no):");
        System.out.println("The default board has 8 Snakes");
        String addSnakesOption = sc.nextLine();

        if (addSnakesOption.equalsIgnoreCase("yes")) {
            System.out.println("How many snakes? :");
            int snakeCount = sc.nextInt();

            for (int i = 0; i < snakeCount; i++) {
                System.out.println("Enter start and end positions for snake " + (i + 1) + ": ");
                int head = sc.nextInt();
                int tail = sc.nextInt();
                game.addSnake(new Snake(head, tail));
            }
            sc.nextLine(); // Menangkap newline character setelah nextInt()
        }

        // Menambahkan pemain ke dalam permainan
        for (Player player : players) {
            game.addPlayer(player);
        }

        // Menjalankan permainan
        do {
            // Menentukan giliran pertama
            int turnIndex = game.getTurn();
            System.out.println();
            System.out.println("Turn: " + (turnIndex + 1));

            game.setStatus(1);
            game.setPlayerInTurn(turnIndex);

            Player playerInTurn = game.getPlayers().get(turnIndex);
            System.out.println("Now, the player in turn is " + playerInTurn.getUserName());

            // Pemain dalam giliran melempar dadu
            System.out.println(playerInTurn.getUserName() + ", press ENTER to roll the dice!");
            String input = sc.nextLine();
            int diceRoll = 0;
            if (input.isEmpty()) {
                playSound("dice_roll.wav"); // Memutar suara dadu
                diceRoll = playerInTurn.rollDice();
            }

            System.out.println("The dice shows number " + diceRoll + "!!");
            game.movePlayer(playerInTurn, diceRoll);

            // Memeriksa apakah pemain menang
            if (playerInTurn.getPosition() == game.getBoardSize()) {
                playSound("winner.wav"); // Memutar suara kemenangan
                System.out.println("Congratulations " + playerInTurn.getUserName() + ", you won!");
                game.setStatus(2); // Mengakhiri permainan

                // Menunggu suara selesai diputar
                try {
                    Thread.sleep(5000); // Menunggu selama 5 detik, sesuaikan dengan durasi suara "winner.wav"
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Sort and display player positions
            game.sortPlayersByPosition();
            displayPlayerPositions(game.getPlayers());

        } while (game.getStatus() != 2);
    }

    // Metode untuk memutar suara
    public static void playSound(String soundFile) {
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

    // Method to display player positions
    public static void displayPlayerPositions(ArrayList<Player> players) {
        System.out.println("Current Player Positions:");
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            System.out.println((i + 1) + ". " + player.getUserName() + " - Position: " + player.getPosition());
        }
    }
}
