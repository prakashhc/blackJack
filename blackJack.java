/**
 * Version 0.9
 * Created by Prakash Chatlani
 *
 *  About:
 *      - Blackjack is a card game with the goal of reaching 21. If a player goes over, then he "busts" and loses
 *      - In my version of Blackjack, it will be a one player game against the computer.
 *      - The player starts with $10 and can wager however much they want
 *
 *
 *  Future features:
 *      - add split feature (if a user starts with two of the same cards, then allow to split and play those individually)
 *      - 2-player mode by adding Player classes
 *
 */

import java.util.*;
public class blackJack {

    static List<String> deck = generateDeck();

    static int playerTotal; //total of the player's cards
    static int computerTotal; //total of the computer's cards
    static int realcomputerTotal; //total of the computer's BOTH cards (without revealing both)

    static boolean computerFlag;

    private static  final Map<String, Integer> cardValues;

    static int balance;

    public static int getWager() {
        return wager;
    }

    public static void setWager(int wager) {
        blackJack.wager = wager;
    }

    static int wager;

    public static int getBalance() {
        return balance;
    }

    public static void setBalance(int balance) {
        blackJack.balance = balance;
    }


    public static void main(String[] args){

        intro(); //introduction with menu (intro, play, quit)


    }

    /**
     * Introduction: features how to play and a menu
     */
    public static void intro(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to Prakash's Blackjack. Please pick an option (type 1, 2 or 3) below\nMenu:\n\t1. Instructions\n\t2. Play\n\t3. Quit");
        int selection = scan.nextInt();
        if (selection > 3 || selection < 1){
            intro();
        }

        switch (selection) {
            case 1 -> {
                System.out.println("Instructions: Blackjack is a card game in which the goal is to reach 21.\nIn this version, you will be playing against the dealer and will be dealt 2 cards at the beginning of each round. If you like your cards (feel like they will be closer to 21 than the dealer), then you can STAND, or if not, you can hit and get another card.\nIf you go over, you lose your wager and can start a new game.");
                intro();
            }
            case 2 -> {
                balance = 10;
                startGame();
            }
            case 3 -> System.exit(0);
        }
    }

    /**
     * startGame() - will give the dealer and the player each two cards.
     * Print statement reveals one of the dealer's cards
     * Cards should be set as the traditional 52-deck (Ace - King but no Joker)
     */
    public static void startGame(){

        Scanner scan = new Scanner(System.in);

        System.out.println("Balance: " + getBalance());

        System.out.println("How much would you lke to wager?"); //asks for wager and checks if balance is sufficient

        try {
            wager = scan.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter an integer.");
            wager = scan.nextInt();
        }

        System.out.println("Perfect! Your wager was " + getWager() + ".\nNow dealing cards...\n");
        setBalance(getBalance()-getWager()); //subtracts the wager from user's balance


        dealCards(); //deals 2 cards to the player and 2 to the computer


        gameOptions();


    }


    /**
     * dealCards() - deals two cards to the player
     * will use a print statement to show the 2 cards held by the player and will print out one of the computer's cards
     */
    private static void dealCards() {

        if (deck.isEmpty()) {
            deck = generateDeck();
            Collections.shuffle(deck);
        }

        Collections.shuffle(deck);

        // Deal 2 cards to the player
        String playerCard1 = deck.remove(0);
        String playerCard2 = deck.remove(0);

        // Deal 2 cards to the computer
        String computerCard1 = deck.remove(0);
        String computerCard2 = deck.remove(0);

        playerTotal = calculateTotal(playerCard1) + calculateTotal(playerCard2);
        computerTotal = calculateTotal(computerCard1);
        realcomputerTotal = calculateTotal(computerCard1) + calculateTotal(computerCard2);

        System.out.println("Player's cards: " + playerCard1 + ", " + playerCard2 + " - Total: " + playerTotal);
        System.out.println("Computer's cards: " + computerCard1 + " - Total: " + computerTotal + "\n");
        //System.out.println("Computer's REAL cards: " + computerCard1 + ", " + computerCard2 + " - Total: " + realcomputerTotal + "\n");


        //if either the user or the player has blackjack (21), then reveal here
        if(realcomputerTotal == 21){
            computerFlag = true;
            blackjack();
        }
        else if(playerTotal == 21){
            blackjack();
        }

    }


    private static List<String> generateDeck() {
        List<String> deck = new ArrayList<>();

        // Generate a standard 52-card deck
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};

        for (String suit : suits) {
            for (String rank : ranks) {
                deck.add(rank + " of " + suit);
            }
        }

        return deck;
    }


    private static int calculateTotal(String card) {
        // Assume the card is in the format "Rank of Suit"
        String[] parts = card.split(" ");
        String rank = parts[0];

        return cardValues.get(rank);
    }

    static {
        // Initialize card values hash map
        cardValues = new HashMap<>();

        //assigns the key to the value that the key holds - ex: card "2" will have a value of 2
        for (int i = 2; i <= 10; i++) {
            cardValues.put(Integer.toString(i), i);
        }

        //assigns the cardValues with other values
        cardValues.put("Jack", 10);
        cardValues.put("Queen", 10);
        cardValues.put("King", 10);
        cardValues.put("Ace", 11); // You may want to handle Ace differently based on the total value
    }


    /**
     * Player who gets Blackjack (21 on the first drawing)
     * if the computer wins, then check if the player has any more balance & either startGame or end game
     * if player wins with Blackjack, then the return is 2.5x the wager
     */


    private static void blackjack(){
        if (computerFlag){
            computerFlag = false;
            System.out.println("You lost! The computer got blackjack!");
            if(getBalance() > 0){
                startGame();
            }
            else{
                gameOver();
            }
        }
        System.out.println("You got Blackjack!\nYour wager has been multiplied by 3x and has been added to your balance.");
        setBalance(getBalance() + getWager()*3);
        startGame(); //starts a new game of Blackjack
    }



    private static void gameOver(){
        System.out.println("Game over! You lost.\nMenu:\n1. New Game\n2. Quit");
        Scanner scan = new Scanner(System.in);
        int selection = scan.nextInt();
        switch (selection) {
            case 1 -> intro();
            case 2 -> System.exit(0);
        }
    }


    /**
     * gameOptions() - allows the player to choose whether to double down, stand, or hit
     * double down - you will draw another card and then reveal yourself to the dealer
     * stand - you stand and reveal your cards to the dealer
     * hit - take another card and repeat options
     */
    private static void gameOptions(){
        Scanner scan = new Scanner(System.in);

        if(getBalance() >= getWager()){
            System.out.println("Would you like to:\n1. Double Down\n2. Hit\n3. Stand");
            int selection = scan.nextInt();
            switch (selection) {
                case 1 -> doubleDown();
                case 2 -> hit();
                case 3 -> stand();
            }
        }
        else{
            System.out.println("Would you like to:\n1. Hit\n2. Stand");
            int selection = scan.nextInt();
            switch (selection) {
                case 1 -> hit();
                case 2 -> stand();
            }
        }
    }

    /**
     * Draws a singular card, takes another "wager" from the user's balance, and then reveals both the user's and the computer's cards
     */
    private static void doubleDown(){

        if(deck.isEmpty()){
            deck = generateDeck();
            Collections.shuffle(deck);
        }

        setBalance(getBalance()-getWager());

        String extraCard = deck.remove(0);
        playerTotal += calculateTotal(extraCard);
        System.out.println("You drew a " + extraCard + ". Your total is " + playerTotal);

        if(deck.isEmpty()){
            deck = generateDeck();
            Collections.shuffle(deck);
        }


        if(playerTotal > 21){ //if bust, then player loses
            System.out.println("You lost!");
            if(getBalance() == 0){
                gameOver();
            }
            else startGame();
        }

        else if(realcomputerTotal > playerTotal && realcomputerTotal <= 21){
            //Computer's cards
            System.out.println("Computer's total: " + realcomputerTotal);
            System.out.println("You lost!");
            if(balance == 0){ //if the user is out of money
                gameOver();
            }
            startGame();
        }

        //case 2: player has a greater card-value but now the computer/dealer can draw more cards
        else if (playerTotal > realcomputerTotal) {
            //loops until either the computer gets more than the player OR until the computer busts
            while (realcomputerTotal < playerTotal) { //computer will draw another card and repeat process (while loop)
                String extraCard1 = deck.remove(0);
                realcomputerTotal += calculateTotal(extraCard1);
                if (realcomputerTotal > 21) { //if bust, then player wins
                    System.out.println("The computer bust. Computer's total: " + realcomputerTotal);
                    System.out.println("You win! Doubling your wager to your balance now...");
                    balance+=getWager()*4;
                    startGame();
                }
                else if(realcomputerTotal == playerTotal){
                    System.out.println("You tied with the computer. Refunding wager.");
                    balance += getWager();
                    startGame();
                }
            }

            if(realcomputerTotal > playerTotal && realcomputerTotal <= 21){
                //Computer's cards
                System.out.println("Computer's total: " + realcomputerTotal);
                System.out.println("You lost!");
                if(balance == 0){ //if the user is out of money
                    gameOver();
                }
                startGame();
            }
        }

        //case 3: if the player and computer got the same value
        else if(playerTotal == realcomputerTotal){
            System.out.println("You tied with the computer. Refunding wager.");
            balance += getWager()*2;
            startGame();
        }

        //if the player has 21 AND the computer also does not have 21
        else if(playerTotal == 21){
            System.out.println("You got 21!");
            balance += getWager()*4;
            startGame();
        }

        else{
            System.out.println("You won!");
            balance += getWager()*4;
            startGame();
        }
    }

    /**
     * Draws a singular card and adds to the user's pile and then goes back to menu options
     */
    private static void hit() {
        String extraCard = deck.remove(0);
        playerTotal += calculateTotal(extraCard);
        System.out.println("You drew a " + extraCard + ". Your total is " + playerTotal);
        if(playerTotal > 21){ //if bust, then player loses
            System.out.println("You lost!");
            if(getBalance() == 0){
                gameOver();
            }
            else startGame();
        }
        if(playerTotal == 21){
            System.out.println("You got 21! You win!");
            balance += getWager()*4;
            startGame();
        }
        gameOptions(); //displays the options (double down/hit/stand)
    }


    /**
     * --broken?
     * Reveals card and determines round's winner
     */
    private static void stand(){

        if(deck.isEmpty()){
            deck = generateDeck();
            Collections.shuffle(deck);
        }



        if(realcomputerTotal > playerTotal && realcomputerTotal <= 21){
            //Computer's cards
            System.out.println("Computer's total: " + realcomputerTotal);
            System.out.println("You lost!");
            if(balance == 0){ //if the user is out of money
                gameOver();
            }
            startGame();
        }

        else if (playerTotal > realcomputerTotal) {
            //loops until either the computer gets more than the player OR until the computer busts
            while (realcomputerTotal < playerTotal) { //computer will draw another card and repeat process (while loop)
                String extraCard = deck.remove(0);
                realcomputerTotal += calculateTotal(extraCard);
                if (realcomputerTotal > 21) { //if bust, then player wins
                    System.out.println("The computer bust. Computer's total: " + realcomputerTotal);
                    System.out.println("You win! Doubling your wager to your balance now...");
                    balance+=getWager()*2;
                    startGame();
                }
                else if(realcomputerTotal == playerTotal){
                    System.out.println("You tied with the computer. Refunding wager.");
                    balance += getWager();
                    startGame();
                }
            }

            if(realcomputerTotal > playerTotal && realcomputerTotal <= 21){
                //Computer's cards
                System.out.println("Computer's total: " + realcomputerTotal);
                System.out.println("You lost!");
                if(balance == 0){ //if the user is out of money
                    gameOver();
                }
                startGame();
            }
        }

        else if(playerTotal == realcomputerTotal){
            System.out.println("You tied with the computer. Refunding wager.");
            balance += getWager();
            startGame();
        }

        else if(playerTotal == 21){
            System.out.println("You got 21!");
            balance += getWager()*4;
            startGame();
        }

        else{
            System.out.println("You won!");
            balance += getWager()*2;
            startGame();
        }
    }
}


