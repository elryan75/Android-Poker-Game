package com.example.pokerprototype;

import java.util.ArrayList;

public class Bot {

    public static int[] takeDecision(Player player, Table table, int round, double highestBet) {


        //Get cards of the bot
        ArrayList<Card> cards = player.getHandCards();

        int[] decision = new int[2];

        //For round 1
        if (round == 1) {

            //Check if he has a pair
            if (cards.get(0).getRank() == cards.get(1).getRank()) {
                //Decision to bet
                decision[0] = 2;

                //Bet depending on strength + randomness
                decision[1] = (int) (cards.get(0).getRank() * 5 * Math.random() * 3);
            } else {
                if (highestBet / player.getBalance() > 45) {
                    //fold
                    decision[0] = 0;
                } else {
                    int strength = cards.get(0).getRank() + cards.get(1).getRank();
                    if (strength > 15) {
                        //Check/call
                        decision[0] = 1;
                    } else {
                        //Fold
                        decision[0] = 0;
                    }
                }
            }
        }

        //For other rounds
        else {
            ArrayList<Card> allCards = new ArrayList();
            allCards.addAll(table.getCards());
            allCards.addAll(player.getHandCards());
            double[] handStat = Probability.getStatistics(player.getDeck(), allCards);

            int chanceOfGoodCards = 0;

            for (int i = 2; i < handStat.length; i++) {
                chanceOfGoodCards += handStat[i];
            }

            if (chanceOfGoodCards > 80) {
                //Bet
                decision[0] = 2;
                decision[1] = 80;
            }
             else if (chanceOfGoodCards > 30) {
                //check
                decision[0] = 1;
            } else if (cards.get(0).getRank() == cards.get(1).getRank()) {
                //check
                decision[0] = 1;
            } else {
                 if (highestBet / player.getBalance() > 30) {
                     System.out.println("The ratio is " + highestBet / player.getBalance());
                     //fold
                     decision[0] = 0;
                 }
                 else {
                     //check
                     decision[0] = 1;
                 }
            }

        }

        return decision;
    }




}
