package com.example.pokerprototype;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Sampler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class pokergame extends AppCompatActivity {

    private TextView xButtn;
    private Button foldBttn;
    private Button checkBttn;
    private Button raiseBttn;
    private EditText moneyRaised;
    private Button confirmBttn;
    private TextView chipsPlayer;
    private TextView chipsBot1;
    private TextView chipsBot2;
    private TextView chipsBot3;
    private ImageView hand1;
    private ImageView hand2;
    private ImageView hand3;
    private TextView pot;
    private TextView betPlayer;
    private TextView betBot1;
    private TextView betBot2;
    private TextView betBot3;
    private int currentPlayer;
    private boolean isEndOfTurn;
    private TextView turnBot1;
    private TextView turnBot2;
    private TextView turnBot3;
    private TextView turnPlayer;
    private int numTurn = 0;
    //For statistics
    private TextView rFlush;
    private TextView sFlush;
    private TextView fourKind;
    private TextView fHouse;
    private TextView nFlush;
    private TextView straight;
    private TextView threeKind;
    private TextView twoPair;
    private TextView pair;
    private TextView nothing;
    private TableLayout Statistics;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pokergame);
        this.foldBttn = findViewById(R.id.foldBttn);
        this.checkBttn = findViewById(R.id.checkBttn);
        this.raiseBttn = findViewById(R.id.raiseBttn);
        this.moneyRaised = findViewById(R.id.moneyRaised);
        this.confirmBttn = findViewById(R.id.confirmBttn);
        this.chipsPlayer = findViewById(R.id.chipsPlayer);
        this.chipsBot1 = findViewById(R.id.chipsBot1);
        this.chipsBot2 = findViewById(R.id.chipsBot2);
        this.chipsBot3 = findViewById(R.id.chipsBot3);
        this.hand1 = findViewById(R.id.hand1);
        this.hand2 = findViewById(R.id.hand2);
        this.hand3 = findViewById(R.id.hand3);
        this.pot = findViewById(R.id.pot);
        this.betPlayer = findViewById(R.id.betPlayer);
        this.betBot1 = findViewById(R.id.betBot1);
        this.betBot2 = findViewById(R.id.betBot2);
        this.betBot3 = findViewById(R.id.betBot3);
        this.turnBot1=(findViewById(R.id.turnBot1));
        this.turnBot2=(findViewById(R.id.turnBot2));
        this.turnBot3=(findViewById(R.id.turnBot3));
        this.turnPlayer=(findViewById(R.id.turnPlayer));


        Thread thread = new Thread(){
            @Override
            public void run() {
                Table table = new Table();
                //Initialize Players
                Player[] players = new Player[4];
                for(int i=0;i<players.length;i++){ // For each player
                    //Create the real player and 3 bots
                    if(i==0){
                        //The first object is made for the user which is why the 'realPlayer' variable is set to true
                        players[i] = new Player(i, 1000, true);
                    }
                    else{
                        players[i] = new Player(i, 1000, false);
                    }
                    System.out.println(players[i]);
                }
                System.out.println("");
                System.out.println("");

                while (players[0].getBalance()>0) { //One execution of this loop represents one full round.
                    System.out.println("Start of turn num "+numTurn);


                    isEndOfTurn = false;
                    //Initialize deck
                    ArrayList<Card> deck = createDeck();
                    table.updateDeck(deck);
                    ArrayList<Player> playersInRound = new ArrayList<>();
                    for (int i = 0; i < players.length; i++) {
                        giveCard(table, players[i]);
                        playersInRound.add(players[i]);
                    } //At this point, every player has 2 cards (hence the deck has 44 cards)


                    resetTurn(players, playersInRound, table);
                    for (int i = 0; i<players.length; i++){
                        System.out.println("Line 139: bet amount player"+players[i].getNumber()+" "+ players[i].getBetAmount());
                    }
                    //The first turn
                    updateBalance(players[table.getPositionBigBlind()], -table.getValueBigBlind(), table); //Withdraws the value of the BigBlind from the person who has the BigBlind.
                    updateBalance(players[table.getPositionSmallBlind()], -table.getValueSmallBlind(), table); //Withdraws the value of the SmallBlind from the person who has the SmallBlind.
                    try{
                        Thread.sleep(3000);
                    }
                    catch (Exception e){

                    }
                    for (int i = 0; i<players.length; i++){
                        System.out.println("Line 147: bet amount player"+players[i].getNumber()+" "+ players[i].getBetAmount());
                    }

                    //Players that are left to take a decision in this turn
                    ArrayList<Player> playersToPlay = new ArrayList<>();

                    for(int turn=1;turn<=4;turn++) {
                        switch (turn) {
                            case 1:
                                System.out.println("TURN 1: ");
                                //Number of the player that starts the turn 1.
                                currentPlayer = (table.getPositionBigBlind() + 1) % 4;

                                //Put playersInRound in Players that have to play
                                for (int i = 0; i < playersInRound.size(); i++) {
                                    playersToPlay.add(playersInRound.get(i));
                                }


                                break;
                            case 2:
                                System.out.println("TURN 2: ");
                                //Preparing 2nd turn
                                currentPlayer = table.getPositionSmallBlind(); //Sets the starting player
                                playersToPlay.clear(); //Empties the arrayList
                                //Put playersInRound in Players that have to play
                                for (int i = 0; i < playersInRound.size(); i++) {
                                    playersToPlay.add(playersInRound.get(i));
                                }
                                //Display the 3 first cards on the table.
                                for (int i = 1; i <= 3; i++) {
                                    showTableCard(table, players, i);
                                }



                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        displayProbabilities(players[0], table);
                                    }
                                });

                                break;
                            case 3:
                                System.out.println("TURN 3: ");
                                currentPlayer = table.getPositionSmallBlind(); //Sets the starting player
                                playersToPlay.clear(); //Empties the arrayList
                                //Put playersInRound in Players that have to play
                                for (int i = 0; i < playersInRound.size(); i++) {
                                    playersToPlay.add(playersInRound.get(i));
                                }
                                //Display the 4th card on the table.
                                showTableCard(table, players, 4);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        displayProbabilities(players[0], table);
                                    }
                                });
                                break;
                            case 4:
                                System.out.println("TURN 4: ");
                                //Preparing last turn
                                currentPlayer = table.getPositionSmallBlind(); //Sets the starting player
                                playersToPlay.clear(); //Empties the arrayList
                                //Put playersInRound in Players that have to play
                                for (int i = 0; i < playersInRound.size(); i++) {
                                    playersToPlay.add(playersInRound.get(i));
                                }
                                //Display the 5th card on the table.
                                showTableCard(table, players, 5);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        displayProbabilities(players[0], table);
                                    }
                                });
                                break;
                        }
                        //While at least one player has to play this turn:
                        while (playersToPlay.size() > 0) {
                            if (playersInRound.size()==1){
                                break;
                            }
                            if (playersToPlay.contains(players[currentPlayer])) {

                                showTurn(currentPlayer);
                                Player thePlayer = players[currentPlayer];
                                thePlayer.setdidSomething(false);

                                System.out.println("HERE ARE THE PLAYERS TO PLAY:");
                                for(int i=0;i<playersToPlay.size();i++){
                                    System.out.print(playersToPlay.get(i).getNumber() + ", ");
                                }
                                System.out.println("");
                                System.out.println("HERE ARE THE PLAYERS IN ROUND: ");
                                for(int i=0;i<playersInRound.size();i++){
                                    System.out.print(playersInRound.get(i).getNumber() + ", ");
                                }
                                System.out.println("");

                                if (thePlayer.isRealPlayer()) { //If the player to play is real
                                    enableRealPlayerOptions(); //Shows the option the real player can choose from.
                                    //FoldButton
                                    foldBttn.setOnClickListener(new View.OnClickListener() { //If the real player clicks on the button FOLD.
                                        @Override
                                        public void onClick(View v) {
                                            playersToPlay.remove(thePlayer);
                                            playersInRound.remove(thePlayer);
                                            thePlayer.setdidSomething(true);
                                            disableRealPlayerOptions();
                                            hideHandCards(thePlayer);
                                        }
                                    });
                                    //CheckButton
                                    checkBttn.setOnClickListener(new View.OnClickListener() { //If the real player clicks on the button CHECK/CALL.
                                        @Override
                                        public void onClick(View v) {
                                            check(players, playersInRound, thePlayer, table);
                                            playersToPlay.remove(thePlayer);
                                            thePlayer.setdidSomething(true);
                                            disableRealPlayerOptions();
                                        }
                                    });
                                    //RaiseButton (related with the confirmButton)
                                    raiseBttn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            confirmBttn.setVisibility(View.VISIBLE);
                                            confirmBttn.setClickable(true);
                                            moneyRaised.setVisibility(View.VISIBLE);
                                            moneyRaised.setClickable(true);
                                            moneyRaised.setFocusable(true);
                                            int highestBet = 0;
                                            for (int i = 0; i < playersInRound.size(); i++) {
                                                if (players[playersInRound.get(i).getNumber()].getBetAmount() > highestBet) {
                                                    highestBet = players[playersInRound.get(i).getNumber()].getBetAmount();
                                                }
                                            }
                                            int minRaiseNeeded = highestBet - thePlayer.getBetAmount();
                                            //At this point, the highest bet is found.
                                            moneyRaised.setText("" + minRaiseNeeded);
                                        }
                                    });
                                    //ConfirmButton
                                    confirmBttn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            int highestBet = 0;
                                            for (int i = 0; i < playersInRound.size(); i++) {
                                                if (players[playersInRound.get(i).getNumber()].getBetAmount() > highestBet) {
                                                    highestBet = players[playersInRound.get(i).getNumber()].getBetAmount();
                                                }
                                            }
                                            int minRaiseNeeded = highestBet - thePlayer.getBetAmount();

                                            String sRaised = moneyRaised.getText().toString();

                                            int iRaised = Integer.parseInt(sRaised);

                                            if (iRaised > minRaiseNeeded) { //If the amount entered is greater than the actual highest bet, sets its bet to this amount and update balance.
                                                updateBalance(thePlayer, -iRaised, table);
                                            }
                                            else if (iRaised == minRaiseNeeded) { //If the amount entered is the as the call, sets its bet to this amount and update balance.
                                                check(players, playersInRound, thePlayer, table);
                                                playersToPlay.remove(thePlayer);
                                                thePlayer.setdidSomething(true);
                                                disableRealPlayerOptions();
                                                return; //Stops onClick method
                                            }
                                            else if (iRaised < minRaiseNeeded) { //If the amount entered is less than the actual highest bet, the player calls, then it raises by that amount.
                                                updateBalance(thePlayer, -(iRaised + highestBet), table);
                                            }
                                            playersToPlay.clear();
                                            for (int i = 0; i < playersInRound.size(); i++) {
                                                if (!players[playersInRound.get(i).getNumber()].isAllIn() && playersInRound.get(i) != thePlayer)
                                                    playersToPlay.add(playersInRound.get(i)); //This means that we add all players that are not all in, nor the currentPlayer, to playersToPlay.
                                            }
                                            thePlayer.setdidSomething(true);
                                            disableRealPlayerOptions();
                                        }
                                    });

                                    //Wait until the player makes a decision
                                    while (!thePlayer.didSomething()) {
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }
                                else { //If the player to play is a bot
                                    System.out.println("Tour du bot #" + currentPlayer);

                                    int decision = (int) (Math.random() * 3);
                                    //0=Fold
                                    if (decision == 0) {
                                        playersToPlay.remove(thePlayer);
                                        playersInRound.remove(thePlayer);
                                        thePlayer.setdidSomething(true);
                                        hideHandCards(thePlayer);
                                        System.out.println("Bot #" + currentPlayer + " has folded!");
                                    }
                                    //1=Check
                                    else if (decision == 1) {
                                        check(players, playersInRound, thePlayer, table);
                                        playersToPlay.remove(thePlayer);
                                        thePlayer.setdidSomething(true);
                                        System.out.println("Bot #" + currentPlayer + " has checked/called!");
                                    }
                                    //Raise
                                    else {
                                        int highestBet = 0;
                                        for (int i = 0; i < playersInRound.size(); i++) {
                                            if (players[playersInRound.get(i).getNumber()].getBetAmount() > highestBet) {
                                                highestBet = players[playersInRound.get(i).getNumber()].getBetAmount();
                                            }
                                        }
                                        int minRaiseNeeded = highestBet - thePlayer.getBetAmount();
                                        System.out.println("HighestBet: "+highestBet+"$. Player's bet amount: "+thePlayer.getBetAmount()+"$. MinRaiseNeeded: "+minRaiseNeeded+"$.");

                                        if (thePlayer.getBalance() > highestBet) {
                                            updateBalance(thePlayer, -10 - minRaiseNeeded, table);
                                            playersToPlay.clear();
                                            for (int i = 0; i < playersInRound.size(); i++) {
                                                if (!players[playersInRound.get(i).getNumber()].isAllIn() && playersInRound.get(i) != thePlayer) {
                                                    playersToPlay.add(playersInRound.get(i));
                                                }
                                            }
                                            System.out.println("Bot #" + currentPlayer + " has raised by 10!");
                                        } else {
                                            playersToPlay.remove(thePlayer);
                                            playersInRound.remove(thePlayer);
                                            thePlayer.setdidSomething(true);
                                            System.out.println("Bot #" + currentPlayer + " has folded!");
                                        }
                                    }

                                    try {
                                        Thread.sleep(1000); //Time that the bot actually makes a decision
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }


                                }

                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                System.out.println(playersToPlay);
                            }
                            else {
                                System.out.println("Player #" + currentPlayer + " was not in the playersToPlay");
                            }

                            currentPlayer = (currentPlayer + 1) % 4;

                        }
                        if (playersInRound.size() == 1) {
                            isEndOfTurn = true;
                            System.out.println("The winner is: "+players[playersInRound.get(0).getNumber()]);
                            showTextWinner(playersInRound.get(0).getNumber());
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            splitPot(players, playersInRound, table);
                            setTextTurn();
                            System.out.println("End of turn");
                            break;
                        }
                    }

                    if (!isEndOfTurn){
                        //Need to find a winner
                        //Display the cards of the players still in round
                        for (int i = 0; i<playersInRound.size(); i++){
                            displayHandCards(playersInRound.get(i));
                        }
                        ArrayList<Player> winners = Player.winnerOfRound(playersInRound, table);
                        if(winners.size()==1){
                            showTextWinner(playersInRound.get(0).getNumber());
                        }
                        else{
                            //Do showTextWinner for many winners
                        }
                        System.out.println("THE WINNER IS: " + winners);
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        setTextTurn();
                        splitPot(players, winners, table);
                        System.out.println("End of turn");
                    }
                    numTurn++;
                }

                //Player has no money, goes back to main menu.
                Intent otherActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(otherActivity);
                finish();
            }
        };

        thread.start();

        //Clicking on the X textbutton brings you back to the main page activity
        this.xButtn = findViewById(R.id.xBttn);
        xButtn.setOnClickListener((v) -> {
            Intent otherActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(otherActivity);
            finish();
            System.exit(1);
        });
    }



    public void check(Player[] players, ArrayList<Player> playersInRound, Player player, Table table){
        int highestBet = 0;

        for(int i=0;i<playersInRound.size();i++){
            if(players[playersInRound.get(i).getNumber()].getBetAmount()>highestBet){
                highestBet=players[playersInRound.get(i).getNumber()].getBetAmount();
            }
        }

        if(player.getBetAmount()<highestBet){
            updateBalance(player, -(highestBet-player.getBetAmount()), table);
        }

    }

    public void updateBalance(Player player, int amount, Table table){ //Make sure the amount is negative if you want to withdraw from the player, and positive if you want to add.
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int realAmount = amount;
                int oldBalance = player.getBalance();
                if(oldBalance+amount<0){ //Equivalent to all-in
                    player.setAllIn(true);
                    realAmount = (-oldBalance); //Ex: if the balance is 100$, then the realAmount becomes -100$.
                }
                player.setBalance(oldBalance+realAmount);
                switch (player.getNumber()){ //Sets the text of the appropriate player's chips to its new balance.
                    case 0: chipsPlayer.setText(player.getBalance()+"$"); break;
                    case 1: chipsBot1.setText(player.getBalance()+"$"); break;
                    case 2: chipsBot2.setText(player.getBalance()+"$"); break;
                    case 3: chipsBot3.setText(player.getBalance()+"$"); break;
                }
                //This below takes care of the pot and its text.
                int oldPotValue = table.getPot();
                table.setPot(oldPotValue-realAmount);
                pot.setText(table.getPot()+"$");
                //This below takes care of the bet amounts of each player.
                player.setBetAmount(player.getBetAmount()-realAmount);
                System.out.println("Player number: "+player.getNumber()+". Its betAmount is now "+player.getBetAmount());
                switch (player.getNumber()){
                    case 0: betPlayer.setText(player.getBetAmount()+"$"); break;
                    case 1: betBot1.setText(player.getBetAmount()+"$"); break;
                    case 2: betBot2.setText(player.getBetAmount()+"$"); break;
                    case 3: betBot3.setText(player.getBetAmount()+"$"); break;
                }
            }
        });
    }


    public void splitPot(Player[] players, ArrayList<Player> winners, Table table){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int amountToEach = table.getPot() / winners.size();
                for (int i = 0; i < winners.size(); i++) {
                    int playerNumber = winners.get(i).getNumber();
                    players[playerNumber].setBalance(players[playerNumber].getBalance() + amountToEach);

                    switch (playerNumber) { //Sets the text of the appropriate player's chips to its new balance.
                        case 0:
                            chipsPlayer.setText(players[playerNumber].getBalance() + "$");
                            break;
                        case 1:
                            chipsBot1.setText(players[playerNumber].getBalance() + "$");
                            break;
                        case 2:
                            chipsBot2.setText(players[playerNumber].getBalance() + "$");
                            break;
                        case 3:
                            chipsBot3.setText(players[playerNumber].getBalance() + "$");
                            break;
                    }
                }
                table.setPot(0); //Resets the tablePot
                pot.setText(table.getPot() + "$");
                //This below takes care of the bet amounts of each player.
                for (int i = 0; i < players.length; i++) {
                    players[i].setBetAmount(0);
                    switch (i) {
                        case 0:
                            betPlayer.setText(players[i].getBetAmount() + "$");
                            break;
                        case 1:
                            betBot1.setText(players[i].getBetAmount() + "$");
                            break;
                        case 2:
                            betBot2.setText(players[i].getBetAmount() + "$");
                            break;
                        case 3:
                            betBot3.setText(players[i].getBetAmount() + "$");
                            break;
                    }
                }
            }
        });
    }


    public void enableRealPlayerOptions(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                foldBttn.setVisibility(View.VISIBLE);
                foldBttn.setClickable(true);
                checkBttn.setVisibility(View.VISIBLE);
                checkBttn.setClickable(true);
                raiseBttn.setVisibility(View.VISIBLE);
                raiseBttn.setClickable(true);
            }
        });
    }

    public void disableRealPlayerOptions(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                foldBttn.setVisibility(View.INVISIBLE);
                foldBttn.setClickable(false);
                checkBttn.setVisibility(View.INVISIBLE);
                checkBttn.setClickable(false);
                raiseBttn.setVisibility(View.INVISIBLE);
                raiseBttn.setClickable(false);
                moneyRaised.setVisibility(View.INVISIBLE);
                moneyRaised.setClickable(false);
                confirmBttn.setVisibility(View.INVISIBLE);
                confirmBttn.setClickable(false);
            }
        });

    }

    public void makeBotHandVisible(){
        hand1.setVisibility(View.VISIBLE);
        hand2.setVisibility(View.VISIBLE);
        hand3.setVisibility(View.VISIBLE);
    }

    public void displayHandCards(Player player){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int playerNum = player.getNumber();
                ArrayList<Card> cards = player.getHandCards();
                String cardName = "";
                switch(playerNum) {
                    case 0:
                        cardName = "handCard";
                        break;
                    //Real guy
                    case 1:
                        cardName = "bot1HandCard";
                        hand1.setVisibility(View.INVISIBLE);
                        break;
                    //Bot
                    case 2:
                        cardName = "bot2HandCard";
                        hand2.setVisibility(View.INVISIBLE);
                        break;
                    //Bot
                    case 3:
                        cardName = "bot3HandCard";
                        hand3.setVisibility(View.INVISIBLE);
                        break;
                    //Bot
                }
                for (int i = 0; i < cards.size(); i++) {
                    Card card = cards.get(i);
                    int suit = card.getSuitInt();
                    int value = card.getRank();
                    String suitString = "";
                    switch (suit) {
                        case Card.HEARTS:
                            suitString = "h";
                            break;
                        case Card.SPADES:
                            suitString = "s";
                            break;
                        case Card.DIAMONDS:
                            suitString = "d";
                            break;
                        case Card.CLUBS:
                            suitString = "c";
                            break;
                    }
                    int n = i+1;
                    ImageView HandCard = (ImageView) findViewById(getResources().getIdentifier(cardName + n, "id", getPackageName()));
                    int imageResource = getResources().getIdentifier("@drawable/" + suitString + value, null, getPackageName());
                    HandCard.setImageResource(imageResource);
                }
            }
        });
    }

    public void hideHandCards(Player player){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int playerNum = player.getNumber();
                switch (playerNum){
                    case 0: //Case the real Player
                        int imageBackOfCards = getResources().getIdentifier("@drawable/card_back", null, getPackageName());
                        ImageView playerHandCard1 = findViewById(R.id.handCard1);
                        playerHandCard1.setImageResource(imageBackOfCards);
                        ImageView playerHandCard2 = findViewById(R.id.handCard2);
                        playerHandCard2.setImageResource(imageBackOfCards);
                        break;
                    case 1: //Case Bot1
                        hand1.setVisibility(View.INVISIBLE); //Makes the image hand invisible.
                        ImageView bot1HandCard1 = (ImageView) findViewById(getResources().getIdentifier("bot1HandCard1", "id", getPackageName())); //Identify where is the handCard1 of bot1
                        ImageView bot1HandCard2 = (ImageView) findViewById(getResources().getIdentifier("bot1HandCard2", "id", getPackageName())); //Identify where is the handCard2 of bot1
                        bot1HandCard1.setImageDrawable(null); //Set the image of handCard1 to null to make sure we don't see anything
                        bot1HandCard2.setImageDrawable(null); //Set the image of handCard2 to null to make sure we don't see anything
                        break;
                    case 2: //Case Bot2
                        hand2.setVisibility(View.INVISIBLE); //Makes the image hand invisible.
                        ImageView bot2HandCard1 = (ImageView) findViewById(getResources().getIdentifier("bot2HandCard1", "id", getPackageName())); //Identify where is the handCard1 of bot2
                        ImageView bot2HandCard2 = (ImageView) findViewById(getResources().getIdentifier("bot2HandCard2", "id", getPackageName())); //Identify where is the handCard2 of bot2
                        bot2HandCard1.setImageDrawable(null); //Set the image of handCard1 to null to make sure we don't see anything
                        bot2HandCard2.setImageDrawable(null); //Set the image of handCard2 to null to make sure we don't see anything
                        break;
                    case 3: //Case Bot3
                        hand3.setVisibility(View.INVISIBLE); //Makes the image hand invisible.
                        ImageView bot3HandCard1 = (ImageView) findViewById(getResources().getIdentifier("bot3HandCard1", "id", getPackageName())); //Identify where is the handCard1 of bot3
                        ImageView bot3HandCard2 = (ImageView) findViewById(getResources().getIdentifier("bot3HandCard2", "id", getPackageName())); //Identify where is the handCard2 of bot3
                        bot3HandCard1.setImageDrawable(null); //Set the image of handCard1 to null to make sure we don't see anything
                        bot3HandCard2.setImageDrawable(null); //Set the image of handCard2 to null to make sure we don't see anything
                        break;
                }
            }
        });

    }

    public static ArrayList<Card> createDeck(){
        ArrayList<Card> deck = new ArrayList<>(52); //Initializes the deck
        for(int i = Card.HEARTS; i<=Card.CLUBS; i++){
            for(int j = Card.TWO; j<=Card.ACE; j++){
                deck.add(new Card(i,j));
            }
        }
        return deck;
    }

    public static void giveCard(Table table, Player player){
        //Remove old cards
        player.clearHand();
        table.tableClear();
        System.out.println(table.getCards());
        System.out.println(player.getHandCards());

        ArrayList<Card> deck = table.getDeck();
        //Select two random integers
        int randomCardNumber1 = (int)(Math.random()*deck.size());

        //Create array of two random cards from deck
        ArrayList<Card> cards = new ArrayList<Card>();
        Card card1 = deck.get(randomCardNumber1);
        cards.add(card1);
        deck.remove(randomCardNumber1);
        int randomCardNumber2 = (int)(Math.random()*deck.size());
        Card card2 = deck.get(randomCardNumber2);
        cards.add(card2);
        deck.remove(randomCardNumber2);

        //Give individual deck to players
        player.updateDeck(deck);

        //Update cards in player class
        player.addHand(cards);

        //Give updated deck to table
        table.updateDeck(deck);
    }

    public void showTableCard(Table table, Player[] players, int position){
        Card card = addCard(table, players);
        int suit = card.getSuitInt();
        int value = card.getRank();
        String suitString = "";
        switch (suit) {
            case Card.HEARTS:
                suitString = "h";
                break;
            case Card.SPADES:
                suitString = "s";
                break;
            case Card.DIAMONDS:
                suitString = "d";
                break;
            case Card.CLUBS:
                suitString = "c";
                break;
        }
        String finalSuitString = suitString;
        runOnUiThread(new Runnable() { //Show cards of real player
            @Override
            public void run() {
                ImageView cardView = (ImageView) findViewById(getResources().getIdentifier("tableCard" + position, "id", getPackageName()));
                int imageResource = getResources().getIdentifier("@drawable/" + finalSuitString + value, null, getPackageName());
                cardView.setImageResource(imageResource);
            }
        });

    }

    public static Card addCard(Table table, Player[] players){
        ArrayList<Card> deck = table.getDeck();

        //Select a card and removes it from the table deck.
        int randomCardNumber = (int)(Math.random()*deck.size());
        Card card = deck.get(randomCardNumber);
        table.addCard(card);
        deck.remove(card);

        for (int i = 0; i<players.length; i++) {
            players[i].removeCardFromDeck(card);
        }
        table.updateDeck(deck);
        return card;
    }

    public void displayProbabilities(Player player, Table table){
        ArrayList<Card> allCards = new ArrayList();
        allCards.addAll(table.getCards());
        allCards.addAll(player.getHandCards());

        double[] handStat = Probability.getStatistics(player.getDeck(), allCards);
        String[] handName = {"(Highest Card): ", "Pair: ", "Two Pairs: ", "Three of a kind: ",
                "Straight: ", "Flush: ", "Full house: ", "Four of a kind: ", "Straight flush: ","Royal flush: "};

        String prob = "";

        for(int i=0;i<handStat.length;i++){
            prob = prob + handName[i] + " " + handStat[i] + "% \n";
        }

        System.out.println(prob);

        TextView test = (TextView) findViewById(R.id.probabilitiesTest);
        test.setText(prob);


    }

    public void resetTurn(Player[] players, ArrayList<Player> playersInRound, Table table){
        //Show cards of real player
        displayHandCards(playersInRound.get(0));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Sets to null the 5 images of the table cards
                for (int i = 1; i<=5; i++){
                    ImageView cardView = (ImageView) findViewById(getResources().getIdentifier("tableCard" + i, "id", getPackageName()));
                    cardView.setImageDrawable(null);
                }
                //Si on a le temps on peut faire super efficient avec for loop i et for loop j pis remplacer le numero bot et numero handCard genre ex: botiHandCardj
                //Bot1
                hand1.setVisibility(View.VISIBLE); //Makes the image hand visible.
                ImageView bot1HandCard1 = findViewById(getResources().getIdentifier("bot1HandCard1", "id", getPackageName())); //Identify where is the handCard1 of bot1
                ImageView bot1HandCard2 = findViewById(getResources().getIdentifier("bot1HandCard2", "id", getPackageName())); //Identify where is the handCard2 of bot1
                bot1HandCard1.setImageDrawable(null); //Set the image of handCard1 to null to make sure we don't see anything
                bot1HandCard2.setImageDrawable(null); //Set the image of handCard2 to null to make sure we don't see anything
                // Case Bot2
                hand2.setVisibility(View.VISIBLE); //Makes the image hand visible.
                ImageView bot2HandCard1 = findViewById(getResources().getIdentifier("bot2HandCard1", "id", getPackageName())); //Identify where is the handCard1 of bot2
                ImageView bot2HandCard2 = findViewById(getResources().getIdentifier("bot2HandCard2", "id", getPackageName())); //Identify where is the handCard2 of bot2
                bot2HandCard1.setImageDrawable(null); //Set the image of handCard1 to null to make sure we don't see anything
                bot2HandCard2.setImageDrawable(null); //Set the image of handCard2 to null to make sure we don't see anything
                //Case Bot3
                hand3.setVisibility(View.VISIBLE); //Makes the image hand visible.
                ImageView bot3HandCard1 = findViewById(getResources().getIdentifier("bot3HandCard1", "id", getPackageName())); //Identify where is the handCard1 of bot3
                ImageView bot3HandCard2 = findViewById(getResources().getIdentifier("bot3HandCard2", "id", getPackageName())); //Identify where is the handCard2 of bot3
                bot3HandCard1.setImageDrawable(null); //Set the image of handCard1 to null to make sure we don't see anything
                bot3HandCard2.setImageDrawable(null); //Set the image of handCard2 to null to make sure we don't see anything
            }
        });
        for(int i = 0; i<playersInRound.size(); i++){
            //Sets false because these players won't have them on the next round. (We need this because we set it true before, but it won't be true after the round)
            players[i].setStartsBigBlind(false);
            players[i].setStartsSmallBlind(false);
        }
        int oldPositionButton = table.getPositionButton();
        int newPositionButton = (oldPositionButton + 1) % 4;
        int newPositionSmallBlind = (newPositionButton + 1) % 4;
        int newPositionBigBlind = (newPositionButton + 2) % 4;
        table.setPositionButton(newPositionButton);
        table.setPositionBigBlind(newPositionBigBlind);
        table.setPositionSmallBlind(newPositionSmallBlind);
        players[newPositionBigBlind].setStartsBigBlind(true); //This player has the BigBlind for this round.
        players[newPositionSmallBlind].setStartsSmallBlind(true); //This player has the SmallBlind for this round.

        //Move the button and small and big blinds
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Button
                ImageView newPosButton = (ImageView) findViewById(getResources().getIdentifier("pos" + newPositionButton, "id", getPackageName())); //Identify where to set the button
                ImageView oldPosButton = (ImageView) findViewById(getResources().getIdentifier("pos" + oldPositionButton, "id", getPackageName())); //Identify where the button was.
                oldPosButton.setImageDrawable(null); //Set old position of button to null image because there must be no image there.
                int imageButton = getResources().getIdentifier("@drawable/dealer", null, getPackageName()); //Retrieves the image of the button in the resources
                newPosButton.setImageResource(imageButton); //Sets the button image to go to its new position.
                //SmallBlind
                ImageView newPosSmallBlind = (ImageView) findViewById(getResources().getIdentifier("pos" + newPositionSmallBlind, "id", getPackageName())); //Identify where to set the smallBlind.
                int imageSmallBlind = getResources().getIdentifier("@drawable/small_blind", null, getPackageName()); //Retrieves the image of the smallBlind in the resources
                newPosSmallBlind.setImageResource(imageSmallBlind); //Sets the smallBlind image to go to its new position.
                //BigBlind
                ImageView newPosBigBlind = (ImageView) findViewById(getResources().getIdentifier("pos" + newPositionBigBlind, "id", getPackageName())); //Identify where to set the bigBlind.
                int imageBigBlind = getResources().getIdentifier("@drawable/big_blind", null, getPackageName()); //Retrieves the image of the bigBlind in the resources
                newPosBigBlind.setImageResource(imageBigBlind); //Sets the bigBlind image to go to its new position.
            }
        });
        //Disable the real player's options at the start of every round.
        disableRealPlayerOptions();

    }

    public void showTurn(int currentPlayer){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (currentPlayer){
                    case 0:
                        turnPlayer.setVisibility(View.VISIBLE);
                        turnBot1.setVisibility(View.INVISIBLE);
                        turnBot2.setVisibility(View.INVISIBLE);
                        turnBot3.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        turnPlayer.setVisibility(View.INVISIBLE);
                        turnBot1.setVisibility(View.VISIBLE);
                        turnBot2.setVisibility(View.INVISIBLE);
                        turnBot3.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        turnPlayer.setVisibility(View.INVISIBLE);
                        turnBot1.setVisibility(View.INVISIBLE);
                        turnBot2.setVisibility(View.VISIBLE);
                        turnBot3.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        turnPlayer.setVisibility(View.INVISIBLE);
                        turnBot1.setVisibility(View.INVISIBLE);
                        turnBot2.setVisibility(View.INVISIBLE);
                        turnBot3.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    public void showTextWinner(int currentPlayer){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (currentPlayer){
                    case 0:
                        turnPlayer.setText("Winner");
                        turnPlayer.setVisibility(View.VISIBLE);
                        turnBot1.setVisibility(View.INVISIBLE);
                        turnBot2.setVisibility(View.INVISIBLE);
                        turnBot3.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        turnPlayer.setVisibility(View.INVISIBLE);
                        turnBot1.setText("Winner");
                        turnBot1.setVisibility(View.VISIBLE);
                        turnBot2.setVisibility(View.INVISIBLE);
                        turnBot3.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        turnPlayer.setVisibility(View.INVISIBLE);
                        turnBot1.setVisibility(View.INVISIBLE);
                        turnBot2.setText("Winner");
                        turnBot2.setVisibility(View.VISIBLE);
                        turnBot3.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        turnPlayer.setVisibility(View.INVISIBLE);
                        turnBot1.setVisibility(View.INVISIBLE);
                        turnBot2.setVisibility(View.INVISIBLE);
                        turnBot3.setText("Winner");
                        turnBot3.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    public void setTextTurn(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                turnPlayer.setVisibility(View.INVISIBLE);
                turnPlayer.setText("Your Turn!");
                turnBot1.setVisibility(View.INVISIBLE);
                turnBot1.setText("Bot1's Turn");
                turnBot2.setVisibility(View.INVISIBLE);
                turnBot2.setText("Bot2's Turn");
                turnBot3.setVisibility(View.INVISIBLE);
                turnBot3.setText("Bot3's Turn");

            }
        });
    }

}