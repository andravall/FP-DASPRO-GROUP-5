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

public class Player {
    private String userName;
    private int position;

    public Player(String userName) {
        this.userName = userName;
        this.position = 0; // Start position
    }

    public String getUserName() {
        return userName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int rollDice() {
        // Simulate rolling a dice (1 to 6)
        return (int) (Math.random() * 6) + 1;
    }

    public void moveAround(int steps, int boardSize) {
        int newPosition = this.position + steps;
        if (newPosition <= boardSize) {
            this.position = newPosition;
        }
    }
}
