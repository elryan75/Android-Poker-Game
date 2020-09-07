package com.example.pokerprototype;

import java.util.ArrayList;

public class Player{
    private int number;
    private int score;
    private int balance;
    private ArrayList<Card> handCards = new ArrayList<>();;
    private ArrayList<Card> deck;
    private Boolean realPlayer;
    private ArrayList<Card> playableCards;
    private boolean startsSmallBlind;
    private boolean startsBigBlind;
    private boolean toPlay;
    private boolean allIn;
    private boolean playerDidSomethingThisTurn;
    private int betAmount;
    private boolean played; //Is true when the player has played this turn
    private boolean folded;


    public Player(){}

    public Player(int balance, ArrayList<Card> handCards, Boolean realPlayer) {
        this.balance = balance;
        this.handCards= new ArrayList<>();
        for(int i=0; i< handCards.size(); i++)
            this.handCards.add(handCards.get(i));
        this.realPlayer = realPlayer;
    }

    public Player(int number, int balance, Boolean realPlayer) {
        this.number = number;
        this.balance = balance;
        this.realPlayer = realPlayer;
        this.betAmount = 0;
    }

    public void updateDeck(ArrayList<Card> deck){
        this.deck=deck;
    }

    public ArrayList<Card> getDeck(){
        return this.deck;
    }

    public void removeCardFromDeck(Card card){
        this.deck.remove(card);
    }

    public void addHand(ArrayList<Card> handCards){
        this.handCards = new ArrayList<>();
        for(int i=0; i< handCards.size(); i++)
            this.handCards.add(handCards.get(i));
    }

    public void clearHand(){
        if(handCards.size()!=0)
            this.handCards.clear();
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public ArrayList<Card> getPlayableCards() {
        return playableCards;
    }

    public void setPlayableCards(ArrayList<Card> tableCards) {
        this.playableCards = new ArrayList<>();
        this.playableCards.clear(); //Is it necessary?
        this.playableCards.addAll(this.handCards); //Adds the cards in this player's hand to his playable cards
        this.playableCards.addAll(tableCards); //Adds the cards of the table to the playable cards.
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public ArrayList<Card> getHandCards() {
        return handCards;
    }

    public boolean isRealPlayer() {
        return realPlayer;
    }

    public void setStartsSmallBlind(boolean startsSmallBlind){
        this.startsSmallBlind = startsSmallBlind;
    }

    public boolean hasSmallBlind(){
        return startsSmallBlind;
    }

    public void setStartsBigBlind(boolean startsBigBlind){
        this.startsBigBlind = startsBigBlind;
    }

    public boolean hasBigBlind(){
        return startsBigBlind;
    }

    public void setToPlay(boolean toPlay){
        this.toPlay = toPlay;
    }

    public boolean hasToPlay(){ //When this returns true, it means the player has to check this turn. When this returns false, it means the player has already checked this turn.
        return toPlay;
    }

    public boolean isAllIn() {
        return allIn;
    }

    public void setAllIn(boolean allIn) {
        this.allIn = allIn;
    }

    public void setdidSomething(boolean playerDidSomethingThisTurn){
        this.playerDidSomethingThisTurn = playerDidSomethingThisTurn;
    }
    public boolean didSomething(){
        return playerDidSomethingThisTurn;
    }

    public int getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(int betAmount) {
        this.betAmount = betAmount;
    }

    public boolean hasPlayed() {
        return played;
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }

    public boolean hasFolded() {
        return folded;
    }

    public void setFolded(boolean folded) {
        this.folded = folded;
    }

    @Override
    public String toString() {
        if(realPlayer)
            return "(YOU) com.example.pokerprototype.Player{" + "balance=" + balance + ", hand=" + handCards + '}';
        else
            return "(CPU) com.example.pokerprototype.Player{" + "balance=" + balance + ", hand=" + handCards + '}';
    }

    public static ArrayList<Player> winnerOfRound(Player[] players, Table table){ //Return an ArrayList of players containing the winner (or winners if 2 or more players have the same best hand)
        ArrayList<Player> winners = new ArrayList<>();
        if(players.length==1){ //Will never happen practically.
            winners.add(players[0]);
            return winners;
        }
        else{
            int maxScore = 0; //The score to beat.
            ArrayList<Card> tableCards = new ArrayList<>(); //The cards on the table.
            tableCards.addAll(table.getCards());
            for (Player player : players) { //For loop through all the players.
                player.setPlayableCards(tableCards); //Adds the cards on the table to the cards a player can use.
                if (Hand.getScore(player.getPlayableCards()) > maxScore) { //If the score of the player is better than the score to beat;
                    maxScore = Hand.getScore(player.getPlayableCards()); //The score to beat is now that one.
                    winners.clear(); //Remove all players that had the score to beat.
                    winners.add(player); //Adds the player to the winners list.
                }
                else if (Hand.getScore(player.getPlayableCards()) == maxScore) { //If the score of the player is the same as the score to beat;
                    winners.add(player); //Adds that player to the potential winners.
                }
            }
            return winners; //Return the list of the winners.
        }
    }

    public static ArrayList<Player> winnerOfRound(ArrayList<Player> players, Table table){ //Return an ArrayList of players containing the winner (or winners if 2 or more players have the same best hand)
        ArrayList<Player> winners = new ArrayList<>();
        if(players.size()==1){
            winners.add(players.get(0));
            return winners;
        }
        else{
            int maxScore = 0; //The score to beat.
            ArrayList<Card> tableCards = new ArrayList<>(); //The cards on the table.
            tableCards.addAll(table.getCards());
            for (int i = 0; i<players.size(); i++) { //For loop through all the players.
                players.get(i).setPlayableCards(tableCards); //Adds the cards on the table to the cards a player can use.
                if (Hand.getScore(players.get(i).getPlayableCards()) > maxScore) { //If the score of the player is better than the score to beat;
                    maxScore = Hand.getScore(players.get(i).getPlayableCards()); //The score to beat is now that one.
                    winners.clear(); //Remove all players that had the score to beat.
                    winners.add(players.get(i)); //Adds the player to the winners list.
                }
                else if (Hand.getScore(players.get(i).getPlayableCards()) == maxScore) { //If the score of the player is the same as the score to beat;
                    winners.add(players.get(i)); //Adds that player to the potential winners.
                }
            }
            return winners; //Return the list of the winners.
        }
    }

}