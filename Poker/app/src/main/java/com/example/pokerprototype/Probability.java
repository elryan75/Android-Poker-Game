package com.example.pokerprototype;


import java.math.BigInteger;
import java.util.ArrayList;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ryans
 */
public class Probability {

    public static double round(double n){
        //Round the probability to two decimal places
        n = ((int)(n*100));
        n /= 100;
        return n;
    }

    public static int firstDigit(int num){
        if(num/10 == 0)
            return num;
        return firstDigit(num/10);
    }

    public static BigInteger factorial(BigInteger n) {
        BigInteger result = BigInteger.ONE;

        while (!n.equals(BigInteger.ZERO)) {
            result = result.multiply(n);
            n = n.subtract(BigInteger.ONE);
        }

        return result;
    }
    public static int totalCombinations(int n, int r){

        if(n == 0 || r ==0)
            return 1;

        BigInteger bigCardsLeftInDeck = BigInteger.valueOf(n);
        BigInteger bigCardsLeftOnTable= BigInteger.valueOf(r);

        BigInteger top = factorial(bigCardsLeftInDeck);

        BigInteger bottom = (factorial(bigCardsLeftOnTable)).multiply(factorial((bigCardsLeftInDeck.subtract(bigCardsLeftOnTable))));

        BigInteger totalCombinations = top.divide(bottom);

        return totalCombinations.intValue();
    }

    public static double[] getStatistics(ArrayList<Card> deck, ArrayList<Card> cards){
        int cardsLeftToBeShown = 5-(cards.size()-2);


        double[] handCount = findCombinations(deck, cards, cardsLeftToBeShown);
        int totalCombinations = totalCombinations(deck.size(), cardsLeftToBeShown);

        for(int i=0;i<handCount.length;i++){
            handCount[i] = handCount[i]/(double)totalCombinations * 100;
            handCount[i] = round(handCount[i]);
        }
        return handCount;
    }



    //Finds all possible combinations of r elements in an Card ArrayList
    public static double[] findCombinations(ArrayList<Card> deck, ArrayList<Card> cards, int r){

        //Create an array to store the amount of time each hands are in combinations
        double[] handCount = new double[10];


        //Create an array of same size to use in combinations method
        Boolean[] used = new Boolean[deck.size()];
        for(int i=0;i<used.length;i++)
            used[i]=false;

        //Call the recursive combinations method
        return combinations(deck, cards, used,0,0,r, handCount);
    }


    /*
    Method uses recursion to go through all indexes and find their combinations
    When a combination is found, The method verifies its best hand
    */
    static double[] combinations(ArrayList<Card> deck, ArrayList<Card> cards, Boolean[] used, int index, int size, int r, double[] handCount){

        //Check if we have enough elements in the combination, if yes, stop the recursion for that index
        if(size==r){
            ArrayList<Card> tempCards = new ArrayList();
            tempCards.addAll(cards);
            for(int i=0;i<deck.size();i++){
                if(used[i]==true)
                    tempCards.add(deck.get(i));
            }
            int score = Hand.getScore(tempCards);
            if(score<1000000){ //If no hand is made except highest card
                handCount[0]++;
            }
            else{
                handCount[firstDigit(score)]++;
            }
            return handCount;
        }
        //Check if we've been through all indexes in the array
        if(index>=deck.size())
            return handCount;

        //Use the first index
        used[index] = true;
        //Find combinations with this index
        combinations(deck, cards, used,index+1,size+1,r, handCount);

        //When all combinations have been found, set that index to false
        used[index] = false;
        //Find combinations with the index after
        combinations(deck, cards, used,index+1, size,r, handCount);

        return handCount;
    }
}

