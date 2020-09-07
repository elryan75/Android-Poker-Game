package com.example.pokerprototype;

import java.util.ArrayList;
import java.util.Collections;

public class Hand {
    public static final int ROYALFLUSH    = 9999999;
    public static final int STRAIGHTFLUSH = 8000000;
    public static final int FOUROFAKIND   = 7000000;
    public static final int FULLHOUSE     = 6000000;
    public static final int FLUSH         = 5000000;
    public static final int STRAIGHT      = 4000000;
    public static final int THREEOFAKIND  = 3000000;
    public static final int TWOPAIRS      = 2000000;
    public static final int PAIR          = 1000000;

    public static int getScore(ArrayList<Card> cards){ //More efficient than the getScore() method in the comments below. Both work however, but I think this one is quicker since it does two time less calculations.
        int rf = Hand.isRoyalFlush(cards);
        if(rf>0) //Royal Flush
            return rf;
        int sf = Hand.isStraightFlush(cards);
        if(sf>0) //Straight Flush
            return sf;
        int foak = Hand.isFourOfAKind(cards);
        if(foak>0) //Four of a kind
            return foak;
        int fh = Hand.isFullHouse(cards);
        if(fh>0) //Full House
            return fh;
        int f = Hand.isFlush(cards);
        if(f>0) //Flush
            return f;
        int s = Hand.isStraight(cards);
        if(s>0) //Straight
            return s;
        int toak = Hand.isThreeOfAKind(cards);
        if(toak>0) //Three of a kind
            return toak;
        int tp = Hand.isTwoPairs(cards);
        if(tp>0) //Two pairs
            return tp;
        int p = Hand.isPair(cards);
        if(p>0) //Pair
            return p;
        else
            return Hand.scoreHighCard(cards);
    }

    /*public static int getScore(ArrayList<com.example.pokerprototype.Card> cards){
        if(com.example.pokerprototype.Hand.isRoyalFlush(cards)>0)
            return com.example.pokerprototype.Hand.isRoyalFlush(cards);
        else if(com.example.pokerprototype.Hand.isStraightFlush(cards)>0)
            return com.example.pokerprototype.Hand.isStraightFlush(cards);
        else if(com.example.pokerprototype.Hand.isFourOfAKind(cards)>0)
            return com.example.pokerprototype.Hand.isFourOfAKind(cards);
        else if(com.example.pokerprototype.Hand.isFullHouse(cards)>0)
            return com.example.pokerprototype.Hand.isFullHouse(cards);
        else if(com.example.pokerprototype.Hand.isFlush(cards)>0)
            return com.example.pokerprototype.Hand.isFlush(cards);
        else if(com.example.pokerprototype.Hand.isStraight(cards)>0)
            return com.example.pokerprototype.Hand.isStraight(cards);
        else if(com.example.pokerprototype.Hand.isThreeOfAKind(cards)>0)
            return com.example.pokerprototype.Hand.isThreeOfAKind(cards);
        else if(com.example.pokerprototype.Hand.isTwoPairs(cards)>0)
            return com.example.pokerprototype.Hand.isTwoPairs(cards);
        if(com.example.pokerprototype.Hand.isPair(cards)>0)
            return com.example.pokerprototype.Hand.isPair(cards);
        else
            return com.example.pokerprototype.Hand.scoreHighCard(cards);
    }*/

    public static int isRoyalFlush(ArrayList<Card> cards){ //In the cards ArrayList, returns 0 if there is no royal flush, and its score if there is a royal flush.
        int[] suitAndOccurence = Hand.mostCommonSuitAndOccurence(cards);
        if(cards.size()<5 || suitAndOccurence[1]<5)
            return 0;
        else{
            int suitNeeded = suitAndOccurence[0];
            if(Card.contains(new Card(suitNeeded, Card.TEN), cards)&&
                    Card.contains(new Card(suitNeeded, Card.JACK), cards)&&
                    Card.contains(new Card(suitNeeded, Card.QUEEN), cards)&&
                    Card.contains(new Card(suitNeeded, Card.KING), cards)&&
                    Card.contains(new Card(suitNeeded, Card.ACE), cards))
                return ROYALFLUSH;
            else
                return 0;
        }
    }

    public static int isStraightFlush(ArrayList<Card> cards){ //In the cards ArrayList, returns 0 if there is no straight flush, and its score if there is a straight flush.
        int[] suitAndOccurence = Hand.mostCommonSuitAndOccurence(cards);
        if(cards.size()<5 || suitAndOccurence[1]<5) //If there is less than 5 cards or less than 5 cards of the same suit.
            return 0;
        else{
            ArrayList<Card> icards = new ArrayList<>(); icards.addAll(cards); //Create a copy of cards called icards.
            int suit = suitAndOccurence[0]; //suit is the suit of the flush.
            Hand.sortByRank(icards);
            if(icards.get(icards.size()-1).equals(new Card(suit, Card.ACE))) //If there is an ace;
                icards.add(0, new Card(suit, 1)); //Adds a card with rank 1 at the beginning in case we have Ace, 2, 3, 4, 5 as flush.
            for(int i = icards.size()-1; i>=4; i--){
                int rankBestCard = icards.get(i).getRank(); //Since the list is sorted, rankBestCard is the rank of the highest card evaluated
                if (Card.contains(new Card(suit, rankBestCard), icards) //Checks if icards contains all four cards before the rankBestCard.
                        &&Card.contains(new Card(suit, rankBestCard-1), icards)
                        &&Card.contains(new Card(suit, rankBestCard-2), icards)
                        &&Card.contains(new Card(suit, rankBestCard-3), icards)
                        &&Card.contains(new Card(suit, rankBestCard-4), icards)) //If true;
                    return STRAIGHTFLUSH + rankBestCard; //then there is a Straight, so return STRAIGHTFLUSH + the rank of the strongest card of the straightFlush.
            }
            return 0; //If there is no straight, return 0.
        }
    }

    public static int isFourOfAKind(ArrayList<Card> cards){ //In the cards ArrayList, returns 0 if there is no 4 of a kind, and its score if there is a 4 of a kind.
        int[] valueAndOccurence = Hand.mostCommonValueAndOccurence(cards);
        if(valueAndOccurence[1]==4){ //If there is a card that is repeated 4 times.
            ArrayList<Card> icards = new ArrayList<>(); icards.addAll(cards); //Create icards to be a copy of ArrayList cards.
            Hand.sortByRank(icards); //Sorts icards from lowest rank to highest.
            for(int i=icards.size()-1; i>=3; i--){
                if(icards.get(i).getRank()==valueAndOccurence[0]){ //If rank is the rank of the 4 of a kind, then
                    icards.remove(i); //Remove the 4 cards that have this value
                    icards.remove(i-1);
                    icards.remove(i-2);
                    icards.remove(i-3);
                    int high = icards.get(icards.size()-1).getRank(); //Gets the kicker
                    return FOUROFAKIND + 14*valueAndOccurence[0] + high; //Score = FOUROFAKIND + 14*(rank of it) + kicker
                }
            }
            return 0;
        }
        else
            return 0;
    }

    public static int isFullHouse(ArrayList<Card> cards){ //In the cards ArrayList, returns 0 if there is no full house, and its score if there is a full house.
        if(cards.size()<5)
            return 0;
        else{
            int[] value3AndOccurence = Hand.mostCommonValueAndOccurence(cards); //Value and occurence of the strongest 3 of a kind.
            if(value3AndOccurence[1]==3){
                ArrayList<Card> icards = new ArrayList<>(); icards.addAll(cards); //Create a copy of cards called icards.
                Hand.removeThreeOfAKind(icards); //Removes the strongest 3 of a kind.
                int[] value2AndOccurence = Hand.mostCommonValueAndOccurence(icards); //Value and occurence of the strongest pair.
                if(value2AndOccurence[1]>=2){ //Has to be >= because maybe there were other three of a kind in cards.
                    return FULLHOUSE + 14*value3AndOccurence[0] + value2AndOccurence[0]; //Score = FULLHOUSE + 14*(rank of the 3 of a kind) + rank of the pair
                }
                else
                    return 0;
            }
            else
                return 0;
        }
    }

    public static int isFlush(ArrayList<Card> cards){ //In the cards ArrayList, returns 0 if there is no flush, and its score if there is a flush.
        int[] suitAndOccurence = Hand.mostCommonSuitAndOccurence(cards);
        if(suitAndOccurence[1] >= 5){ //If there is a suit that is repeated at least 5 times.
            Hand.sortByRank(cards); //Sorts the cards by rank
            ArrayList<Card> icards = new ArrayList<>();
            int count = 0;
            for(int i=cards.size()-1; i>=0 && count<5; i--){
                if(cards.get(i).getSuitInt()==suitAndOccurence[0]){ //From strongest to weakest ranked card, if that card is of the suit making the flush.
                    icards.add(cards.get(i)); //Adds that card to icards.
                    count++; //When count=5, it means we have the best 5 cards of that suit.
                }
            }
            return FLUSH + 14*14*14*14*icards.get(0).getRank() + 14*14*14*icards.get(1).getRank() + 14*14*icards.get(2).getRank() + 14*icards.get(3).getRank() + icards.get(4).getRank();
        }
        else
            return 0;
    }

    public static int isStraight(ArrayList<Card> cards){ //In the cards ArrayList, returns 0 if there is no straight, and its score if there is a straight.
        if(cards.size()<5) //If there is less than 5 cards.
            return 0;
        else{
            ArrayList<Card> icards = new ArrayList<>(); icards.addAll(cards); //Create a copy of cards called icards.
            Hand.removeCardsOfSameValue(icards); //icards becomes an ArrayList of cards in increasing order with no 2 cards of the same value.
            if(icards.get(icards.size()-1).getRank()==Card.ACE) //If there is an ace;
                icards.add(0, new Card(0, 1)); //Adds a card with rank 1 at the beginning in case we have Ace, 2, 3, 4, 5 as flush.
            for(int i = icards.size()-1; i>=4; i--){
                int valueHighCard = icards.get(i).getRank(); //Since array is sorted, the last card is the highest.
                if(icards.get(i-1).getRank()+1==valueHighCard //Checks is every card before the last one is decreasing only by 1 each time.
                        && icards.get(i-2).getRank()+2==valueHighCard
                        && icards.get(i-3).getRank()+3==valueHighCard
                        && icards.get(i-4).getRank()+4==valueHighCard) //If true;
                    return STRAIGHT + valueHighCard; //then there is a Straight, so return STRAIGHT + the rank of the strongest card of the straight.
            }
            return 0; //If there is no straight, return 0.
        }
    }

    public static int isThreeOfAKind(ArrayList<Card> cards){ //In the cards ArrayList, returns 0 if there are no 2-pairs, or the value of the 2 pairs if there are such.
        int[] valueAndOccurence = Hand.mostCommonValueAndOccurence(cards);
        if(valueAndOccurence[1]==3){ //If there is a card that is repeated at least 3 times.
            ArrayList<Card> icards = new ArrayList<>(); icards.addAll(cards); //Create icards to be a copy of ArrayList cards.
            Hand.sortByRank(icards); //Sorts icards from lowest rank to highest.
            for(int i=icards.size()-1; i>=2; i--){
                if(icards.get(i).getRank()==valueAndOccurence[0]){ //If rank is the rank of the 3 of a kind, then
                    icards.remove(i); //Remove the 3 cards with this value
                    icards.remove(i-1);
                    icards.remove(i-2);
                    int high = icards.get(icards.size()-1).getRank(); //Gets the strongest kicker
                    int low = icards.get(icards.size()-2).getRank(); //Gets the 2nd strongest kicker
                    return THREEOFAKIND + 14*14*valueAndOccurence[0] + 14*high + low; //Score = THREEOFAKIND + 14*14*(rank of 3 of a kind) + 14*strongestKicker + 2nd strongest kicker
                }
            }
            return 0;
        }
        else
            return 0;
    }

    public static int isTwoPairs(ArrayList<Card> cards){ //In the cards ArrayList, returns 0 if there are no 2-pairs, or the value of the 2 pairs if there are such.
        if(cards.size()>=4){ //If there are at least 4 cards.
            ArrayList<Card> icards = new ArrayList<>(); icards.addAll(cards); //Create icards to be a copy of ArrayList cards.
            Hand.sortByRank(icards);
            for(int i=icards.size()-1; i>2; i--){
                if(icards.get(i).isSameRank(icards.get(i-1))){
                    for(int j=i-2; j>0; j--){
                        if(icards.get(j).isSameRank(icards.get(j-1))){
                            int highPair = icards.get(i).getRank();
                            int lowPair = icards.get(j).getRank();
                            icards.remove(i);
                            icards.remove(i-1);
                            icards.remove(j);
                            icards.remove(j-1);
                            int rankLastCard = icards.get(icards.size()-1).getRank();
                            return TWOPAIRS + 14*14*highPair + 14*lowPair + rankLastCard;
                        }
                    }
                }
            }
            return 0;
        }
        else
            return 0;
    }

    public static int isPair(ArrayList<Card> cards){ //In the cards ArrayList, returns 0 if there is no pair, or the value of the pair if there is one.
        int[] valueAndOccurence = Hand.mostCommonValueAndOccurence(cards);
        if(valueAndOccurence[1]==2){ //If there is a pair
            if(cards.size()==2) //If there are only 2 cards to check, then the score is PAIR + rank of the pair
                return PAIR + valueAndOccurence[0];
            else{
                ArrayList<Card> cardsToCheck = new ArrayList<>();
                ArrayList<Card> icards = new ArrayList<>(); icards.addAll(cards); //Create icards to be a copy of ArrayList cards.
                int cardRemovalCount = 0;
                int valPair = valueAndOccurence[0]; //The rank of the pair.
                for(int i = 0; i<icards.size() && cardRemovalCount<2; i++){
                    if(icards.get(i).getRank() == valPair){
                        cardsToCheck.add(icards.get(i));
                        icards.remove(i);
                        cardRemovalCount++; //When reaches 2, no need to check for pair.
                        i--; //To ensure that no card is not checked.
                    }
                }
                Hand.sortByRank(icards); //Sorts the remaining cards in icards in order of increasing rank.
                while(cardRemovalCount<5){
                    cardsToCheck.add(icards.get(icards.size()-1));
                    icards.remove(icards.size()-1);
                    cardRemovalCount++;
                }
                return Hand.valuePair(cardsToCheck); //Returns the value of the pair.
            }
        }
        else
            return 0;
    }

    public static int valuePair(ArrayList<Card> cards){ //Returns the value of the pair.
        int value = PAIR;
        sortByRank(cards); //Sorts cards from lowest rank to highest.

        if (cards.get(0).isSameRank(cards.get(1)))
            value += 14*14*14*cards.get(0).getRank() + cards.get(2).getRank() + 14*cards.get(3).getRank() + 14*14*cards.get(4).getRank();
        else if (cards.get(1).isSameRank(cards.get(2)))
            value += 14*14*14*cards.get(1).getRank() + cards.get(0).getRank() + 14*cards.get(3).getRank() + 14*14*cards.get(4).getRank();
        else if (cards.get(2).isSameRank(cards.get(3)))
            value += 14*14*14*cards.get(2).getRank() + cards.get(0).getRank() + 14*cards.get(1).getRank() + 14*14*cards.get(4).getRank();
        else
            value += 14*14*14*cards.get(3).getRank() + cards.get(0).getRank() + 14*cards.get(1).getRank() + 14*14*cards.get(2).getRank();

        return value; //PAIR + 14^3*pairValue + 14^2*highestCard + 14^1*middleCard + 14^0*lowestCard
    }

    public static int scoreHighCard(ArrayList<Card> cards){ //Returns the score if there is no combination in the cards arrayList.
        ArrayList<Card> icards = new ArrayList<>(); icards.addAll(cards); //Create icards to be a copy of ArrayList cards.
        Hand.sortByRank(icards);
        if(icards.size()==2)
            return 14*icards.get(1).getRank() + icards.get(0).getRank();
        else{
            int strongest = icards.size()-1;
            return 14*14*14*14*icards.get(strongest).getRank() + 14*14*14*icards.get(strongest-1).getRank() + 14*14*icards.get(strongest-2).getRank() + 14*icards.get(strongest-3).getRank() + icards.get(strongest-4).getRank();
        }

    }

    //Apparently not supported here
    public static void sortByRank(ArrayList<Card> cards){ //Sorts an ArrayList of com.example.pokerprototype.Card elements by their rank.
        Collections.sort(cards,(o1, o2) ->{
            if(o1.getRank()>o2.getRank())
                return 1;
            else if(o1.getRank()<o2.getRank())
                return -1;
            else
                return 0;
        });
    }

    public static int[] mostCommonSuitAndOccurence(ArrayList<Card> cards){ //Returns an int array containing the most common suit and amount of cards of this suit.
        int[] occurence = new int[4];
        int highestOccurence = 0;
        int mostCommonSuit=Card.HEARTS;
        for(int i = 0; i<cards.size(); i++){
            occurence[cards.get(i).getSuitInt()]++;
            if(occurence[cards.get(i).getSuitInt()]>highestOccurence){
                highestOccurence = occurence[cards.get(i).getSuitInt()];
                mostCommonSuit = cards.get(i).getSuitInt();
            }
        }
        return new int[]{mostCommonSuit,highestOccurence};
    }

    public static int[] mostCommonValueAndOccurence(ArrayList<Card> cards){ //Returns an int array containing the most common card value and the occurence of it. If tie, returns the highest rank common value and occurence.
        int[] occurence = new int[14];
        int highestOccurence = 0;
        int mostCommonCard = 0;
        for(int i = 0; i<cards.size(); i++){
            occurence[cards.get(i).getRank()-1]++;
            //If a card is repeated more times than the previous most repeated card OR if a card is repeated the same time but has a higher rank;
            if(occurence[cards.get(i).getRank()-1]>highestOccurence  || (occurence[cards.get(i).getRank()-1]==highestOccurence && cards.get(i).getRank()>mostCommonCard)){
                highestOccurence = occurence[cards.get(i).getRank()-1];
                mostCommonCard = cards.get(i).getRank();
            }
        }
        return new int[]{mostCommonCard,highestOccurence};
    }

    public static void removeCardsOfSameValue(ArrayList<Card> cards){ //Sorts an ArrayList of cards and deletes the duplicates (of rank). ex: a a d c b --> a b c d
        Hand.sortByRank(cards); //Puts the cards in order of rank.
        for(int i = 1; i<cards.size(); i++){
            if(cards.get(i-1).isSameRank(cards.get(i))){
                cards.remove(i);
                i--; //In case there are more than 2 times the same card, will compare again.
            }
        }
    }

    public static void removeThreeOfAKind(ArrayList<Card> cards){ //Removes the strongest three of a kind if there is at least one.
        Hand.sortByRank(cards); //Puts the cards in order of rank.
        for(int i = cards.size()-1; i>=2; i--){
            //Checks if the card before i, the card at i, and the card after i have all the same rank. If yes, remove them all.
            if(cards.get(i).isSameRank(cards.get(i-1)) && cards.get(i-1).isSameRank(cards.get(i-2))){
                cards.remove(i);
                cards.remove(i-1);
                cards.remove(i-2);
                break;
            }
        }
    }
}
