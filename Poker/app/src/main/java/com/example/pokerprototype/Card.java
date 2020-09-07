package com.example.pokerprototype;

import java.util.ArrayList;

public class Card{
    private int suit;
    private int rank;
    // The ranks
    public static final int ACE      = 14;
    public static final int KING     = 13;
    public static final int QUEEN    = 12;
    public static final int JACK     = 11;
    public static final int TEN      = 10;
    public static final int NINE     = 9;
    public static final int EIGHT    = 8;
    public static final int SEVEN    = 7;
    public static final int SIX      = 6;
    public static final int FIVE     = 5;
    public static final int FOUR     = 4;
    public static final int THREE    = 3;
    public static final int TWO      = 2;

    // The suits
    public static final int HEARTS   = 0;
    public static final int SPADES   = 1;
    public static final int DIAMONDS = 2;
    public static final int CLUBS    = 3;

    public Card(){} //Default Constructor.

    public Card(int suit, int rank){ //com.example.pokerprototype.Card constructor that accepts a suit and a valueOfCard as arguments.
        this.suit=suit;
        this.rank=rank;
    }

    public int getSuitInt() { //Returns the value of the suit as an int.
        return suit;
    }

    public String getSuitString(){ //Returns the value of the suit as a String.
        String suitS = null;
        switch(this.suit){
            case HEARTS:
                suitS = "Hearts";
                break;
            case SPADES:
                suitS = "Spades";
                break;
            case DIAMONDS:
                suitS = "Diamonds";
                break;
            case CLUBS:
                suitS = "Clubs";
                break;
        }
        return suitS;
    }

    public void setSuit(int suit) { //Sets the suit to a specified suit.
        this.suit = suit;
    }

    public int getRank() { //Returns the rank, of the card.
        return rank;
    }

    public void setValueOfCard(int rank) { //Sets the value of the card to a specified value.
        this.rank = rank;
    }

    @Override
    public String toString(){ //Returns a String with the rank of the card and its suit.
        return this.getRank()+" of "+this.getSuitString();
    }

    public boolean isSameSuit(Card card){ //Returns true if both cards are from the same suit.
        return this.getSuitInt()==card.getSuitInt();
    }

    public boolean isSameRank(Card card){ //Returns true if both cards have the same rank.
        return this.getRank()==card.getRank();
    }

    public boolean equals(Card card){ //Returns true if both cards have the same suit and same rank.
        return this.isSameSuit(card)&&this.isSameRank(card);
    }

    public static boolean contains(Card theCard, ArrayList<Card> cards){ //Returns true if theCard is in the cards ArrayList.
        for(int i = 0; i<cards.size(); i++){
            if(cards.get(i).equals(theCard))
                return true;
        }
        return false;
    }
}
