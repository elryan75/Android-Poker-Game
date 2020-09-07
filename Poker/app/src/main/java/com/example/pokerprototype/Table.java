package com.example.pokerprototype;

import com.example.pokerprototype.Card;

import java.util.ArrayList;

public class Table{
    private ArrayList<Card> cards;
    private ArrayList<Card> deck;
    private int pot;
    private int positionButton=0;
    private int positionBigBlind;
    private int positionSmallBlind;
    private int valueBigBlind = 50;
    private int valueSmallBlind = 25;

    public int getValueBigBlind() {
        return valueBigBlind;
    }

    public void setValueBigBlind(int valueBigBlind) {
        this.valueBigBlind = valueBigBlind;
        this.valueSmallBlind = this.valueBigBlind/2;
    }

    public void tableClear(){
        if(this.cards.size()!=0){
            this.cards.clear();
        }
    }

    public int getValueSmallBlind() {
        return valueSmallBlind;
    }

    public int getPositionBigBlind() {
        return positionBigBlind;
    }

    public void setPositionBigBlind(int positionBigBlind) {
        this.positionBigBlind = positionBigBlind;
    }

    public int getPositionSmallBlind() {
        return positionSmallBlind;
    }

    public void setPositionSmallBlind(int positionSmallBlind) {
        this.positionSmallBlind = positionSmallBlind;
    }

    public Table() {
        this.cards= new ArrayList();
        this.deck = new ArrayList();
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void updateDeck(ArrayList<Card> deck){
        this.deck = deck;
    }

    public ArrayList<Card> getDeck(){
        return this.deck;
    }

    public int getPot() {
        return pot;
    }

    public int getPositionButton() {
        return positionButton;
    }

    public Table (ArrayList<Card> cards){ //Test pour Simon
        this.cards= new ArrayList<Card>();
        for(int i=0; i< cards.size(); i++)
            this.cards.add(cards.get(i));
    }

    public Table(ArrayList<Card> cards, int pot, int positionButton) {
        this.cards= new ArrayList<Card>();
        for(int i=0; i< cards.size(); i++)
            this.cards.add(cards.get(i));

        this.pot = pot;
        this.positionButton = positionButton;
    }

    public void resetTable(){
        cards.clear();
        this.pot = 0;
        this.positionButton ++;
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }

    public void addCards(ArrayList<Card> cards) {
        this.cards.addAll(cards);
    }

    public void setPot(int pot) {
        this.pot = pot;
    }

    public void setPositionButton(int positionButton) {
        this.positionButton = positionButton;
    }
}