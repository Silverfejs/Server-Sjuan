package sjuan;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class control the logic of the game "Sjuan"
 * @author Sjuan
 *
 */
public class Controller {
	private Player player1;
	private Player player2;
	private Player player3;
	private Player player4;
	private Deck deck = new Deck();
	private int gameID;
	private Rules rules = new Rules(this);
	private HashMap<Integer, ArrayList <Card>> gameBoardList = new HashMap<Integer, ArrayList<Card>>();
	private HashMap<Integer, ArrayList <Player>> game = new HashMap<Integer, ArrayList<Player>>();
	private HashMap<Integer, ArrayList <Card>> passCardList = new HashMap<Integer, ArrayList<Card>>();

	/**
	 * Constructs a controller 
	 */
	public Controller(Server server, int gameID, 
			int clientID1, int  clientID2, int clientID3, int clientID4) {
		this.gameID = gameID;
		player1 = new Player(clientID1, gameID);
		player2 = new Player(clientID2, gameID);
		player3 = new Player(clientID3, gameID);
		player4 = new Player(clientID4, gameID);
		deal();
		ArrayList <Player> playerList = new ArrayList<Player>(4);
		ArrayList <Card> gameBoardCards = new ArrayList<Card>();
		ArrayList <Card> tempList = new ArrayList<Card>();
		playerList.add(player1);
		playerList.add(player2);
		playerList.add(player3);
		playerList.add(player4);
		game.put(gameID, playerList);
		gameBoardList.put(gameID, gameBoardCards);
		passCardList.put(gameID, tempList);

	}

	/**
	 * This method deals the deck to all players
	 */
	public void deal() { 
		while (deck.getAllCards()!=0) {
			player1.setPlayerCards(deck.dealCard());
			if (deck.getAllCards()>0)
				player2.setPlayerCards(deck.dealCard());
			if (deck.getAllCards()>0)
				player3.setPlayerCards(deck.dealCard());
			if (deck.getAllCards()>0)
				player4.setPlayerCards(deck.dealCard());
		}
	}

	/**
	 * this method add a card to the gameboard
	 * @param card takes in a card from a player to be set to the gameboard
	 */
	public void moveGameBoardCards(Card card, int gameID) {
		gameBoardList.get(gameID).add(card);
	}

	/**
	 * this method sets the gameboards cards that are played
	 * @param gameBoardCards takes in the cards that are out at game board
	 */
	public void setGameBoardCardsList(ArrayList<Card> gameBoardCards, int gameID) {
		gameBoardList.get(gameID).clear();
		gameBoardList.get(gameID).addAll(gameBoardCards);
	}

	/**
	 * this method add a card to the gameboard
	 * gameboardcards is for controller to know what card that are played to game board
	 * @return gameBoardCards returns an ArrayList of the cards at gameboard
	 */
	public ArrayList <Card> getGameBoardCards (int gameID) {
		return gameBoardList.get(gameID);
	}

	/**
	 * this method returns a boolean if a card is playable or not
	 * @param card takes in a card
	 * @return boolean returns a boolean if the card is playable or not
	 */
	public boolean checkIfCardIsPlayable(String cardName, int clientID, int gameID){
		if (clientID==game.get(gameID).get(0).getClientID()) {
			return rules.correct(player1.getCardByName(cardName), player1, gameID, clientID);
		}
		else if (clientID==game.get(gameID).get(1).getClientID()) {
			return rules.correct(player2.getCardByName(cardName), player2, gameID, clientID);
		}
		else if (clientID==game.get(gameID).get(2).getClientID()) {
			return rules.correct(player3.getCardByName(cardName), player3, gameID, clientID);
		}
		else if (clientID==game.get(gameID).get(3).getClientID()) {
			return rules.correct(player4.getCardByName(cardName), player4, gameID, clientID);
		}
		System.out.println("controller.checkIfCardISPlayable");

		return false;
	}

	/**
	 * this method checks if there are cards are able to play
	 * @return true if there are no cards to play, false if there is cards to play
	 */
	public boolean checkIfPassIsPossible(int clientID, int gameID) {
		if (clientID==player1.getClientID()) {
			for (Card card : player1.getPlayerCards()) {
				if (rules.checkPass(card, gameID)) {
					return false;
				}
			}
			return true; 
		}
		else if (clientID==player2.getClientID()) {
			for (Card card : player2.getPlayerCards()) {
				if (rules.checkPass(card, gameID)) {
					return false;
				}
			}
			return true; 
		}
		else if (clientID==player3.getClientID()) {
			for (Card card : player3.getPlayerCards()) {
				if (rules.checkPass(card, gameID)) {
					return false;
				}
			}
			return true; 
		}
		else if (clientID==player4.getClientID()) {
			for (Card card : player4.getPlayerCards()) {
				if (rules.checkPass(card, gameID)) {
					return false;
				}
			}
			return true; 
		}
		System.out.println("controller.checkIfpassIsPossible");
		return false;
	}

	/**
	 * this method finds out the player that have the starting card (h7) 
	 * and sets the client id for the player
	 */
	public void whoHaveHeartSeven () {
		for (Card card : player1.getPlayerCards())
			if (card.toString().equals("h7")) {
				player1.hasHeart7();
			}
		for (Card card : player2.getPlayerCards())
			if (card.toString().equals("h7")) {
				player2.hasHeart7();
			}
		for (Card card : player3.getPlayerCards())
			if (card.toString().equals("h7")) {
				player3.hasHeart7();
			}
		for (Card card : player4.getPlayerCards())
			if (card.toString().equals("h7")) {
				player4.hasHeart7();
			}
	}

	/**
	 * this method sets playerOrder
	 * @param player takes in a player
	 */
	public void setPlayerOrder(Player player) {
		if (player1==null) {
			player1 = player;
		}

		else if (player2==null) {
			player2 = player;
		}

		else if (player3==null) {
			player3 = player;

		}
		else if (player4==null) {
			player4 = player;
		}
	}

	/**
	 * checks that no player is null
	 * @return boolean returns if true or false
	 */
	public boolean playersExist() {
		if (player1!=null && player2!=null && player3!=null && player4!=null){
			deal();
			return true;
		}
		return false;

	}

	/**
	 * this method returns player1 in a game
	 * @param gameID takes in the gameID of a game
	 * @return player1 returns player1 of a game
	 */
	public Player getPlayer1(int gameID) {
		player1 = game.get(gameID).get(0);

		return player1;
	}

	/**
	 * this method returns player2 in a game
	 * @param gameID takes in the gameID of a game
	 * @return player2 returns player2 of a game
	 */
	public Player getPlayer2(int gameID) {
		player2 = game.get(gameID).get(1);

		return player2;
	}

	/**
	 * this method returns player3 in a game
	 * @param gameID takes in the gameID of a game
	 * @return player3 returns player3 of a game
	 */
	public Player getPlayer3(int gameID) {
		player3 = game.get(gameID).get(2);

		return player3;
	}

	/**
	 * this method returns player4 in a game
	 * @param gameID takes in the gameID of a game
	 * @return player4 returns player4 of a game
	 */
	public Player getPlayer4(int gameID) {
		player4 = game.get(gameID).get(3);
		return player4;
	}

	/**
	 * this method returns a controller of a game
	 * @param gameID takes in a gameID
	 * @return controller returns this controller
	 */
	public Controller getController(int gameID) {

		//det kan kanske behövas lite mer förklaring eller utökning här
		return this;
	}

	/**
	 * this method returns a gameID of a game
	 * @return gameID returns a Integer of a gameID
	 */
	public int getGameID() {
		return gameID;
	}

	/**
	 * this method returns the next player turn in a game as a clientID
	 * @param clientID takes in current players clientID
	 * @param gameID takes in a Integer of the gameID
	 * @return clientID returns a clientID of the next players clientID
	 */
	public int setNextPlayersTurn(int clientID, int gameID) {
		for (int i = 0; i <= game.get(gameID).size();i++){
			if (game.get(gameID).get(i).getClientID()==clientID) {
				if (i==3) {
					i = -1;
				}
				i++;
				return game.get(gameID).get(i).getClientID();
			}
		}
		System.out.println("controller.setNextPlayersTurn");
		return -1;
	}

	/**
	 * this method returns a player with help of a gameID and a clientID
	 * @param clientID takes in a Integer of a clientID
	 * @param gameID takes in a Integer of a gameID
	 * @return player returns a player
	 */
	public Player getPlayerByClientID(int gameID, int clientID) {
		for (Player player : game.get(gameID)) {
			if (player.getClientID()==clientID) {
				return player;
			}
		}
		System.out.println(clientID + "controller.getPlayerByClientID");
		return null;
	}

	/**
	 * this method returns a Integer of hand size of opponent1 from a clients perspective
	 * @param gameID takes in a Integer of a gameID
	 * @param clientID takes in a Integer of a clientID
	 * @return Integer returns a Integer of a opponents hand size of cards
	 */
	public int getOpponent1HandSize(int gameID, int clientID) {
		if (getPlayer1(gameID).getClientID()==clientID) {
			return getPlayer2(gameID).getPlayerCardSize();
		}
		else if (getPlayer2(gameID).getClientID()==clientID) {
			return getPlayer3(gameID).getPlayerCardSize();
		}
		else if (getPlayer3(gameID).getClientID()==clientID) {
			return getPlayer4(gameID).getPlayerCardSize();
		}
		else {
			return getPlayer1(gameID).getPlayerCardSize();
		}
	}

	/**
	 * this method returns a Integer of hand size of opponent1 from a clients perspective
	 * @param gameID takes in a Integer of a gameID
	 * @param clientID takes in a Integer of a clientID
	 * @return Integer returns a Integer of a opponents hand size of cards
	 */
	public int getOpponent2HandSize(int gameID, int clientID) {

		if (getPlayer1(gameID).getClientID()==clientID) {
			return getPlayer3(gameID).getPlayerCardSize();
		}
		else if (getPlayer2(gameID).getClientID()==clientID) {
			return getPlayer4(gameID).getPlayerCardSize();
		}
		else if (getPlayer3(gameID).getClientID()==clientID) {
			return getPlayer1(gameID).getPlayerCardSize();
		}
		else {
			return getPlayer2(gameID).getPlayerCardSize();
		}
	}

	/**
	 * this method returns a Integer of hand size of opponent1 from a clients perspective
	 * @param gameID takes in a Integer of a gameID
	 * @param clientID takes in a Integer of a clientID
	 * @return Integer returns a Integer of a opponents hand size of cards
	 */
	public int getOpponent3HandSize(int gameID, int clientID) {

		if (getPlayer1(gameID).getClientID()==clientID) {
			return getPlayer4(gameID).getPlayerCardSize();
		}
		else if (getPlayer2(gameID).getClientID()==clientID) {
			return getPlayer1(gameID).getPlayerCardSize();
		}
		else if (getPlayer3(gameID).getClientID()==clientID) {
			return getPlayer2(gameID).getPlayerCardSize();
		}
		else {
			return getPlayer3(gameID).getPlayerCardSize();
		}
	}

	/**
	 * this method removes a card from a players hand to be put in a 
	 * list that will be added to a passing player
	 * @param cardName takes in the name of desired card to give
	 * @param clientID takes in a Integer of the clients ID
	 * @param gameID takes in a Integer of a controllers ID of a game
	 */
	public void giveCard (String cardName, int clientID, int gameID) {
		for (Card card : getPlayerByClientID(gameID, clientID).getPlayerCards()) {
			if (card.toString().equals(cardName)) {
				getPlayerByClientID(gameID, clientID).getPlayerCards().remove(card);
				passCardList.get(gameID).add(card);
//				System.out.println(card.toString() + " är tilllagd i gebort listan");
				break;
			}
		}
	}

	/**
	 * Cards given to a player is stored in a ArrayList, 
	 * this is the method to move the cards in the list to a players hand
	 * @param clientID takes in a Integer of a clients ID
	 * @param gameID takes in a Integer of a controllers gameID
	 * @return
	 */
	public ArrayList<Card> addRecievedCardsToPassedPlayer(int clientID, int gameID) {
		ArrayList<Card> list = passCardList.get(gameID);
		for (int i = list.size()-1; i >= 0 ; i--) {
			getPlayerByClientID(gameID, clientID).getPlayerCards().add(list.remove(i));
		}
		passCardList.get(gameID).clear();
		return getPlayerByClientID(gameID, clientID).getPlayerCards();
	}

	public String playerWin(int gameID) {
		if (getPlayer1(gameID).getPlayerCardSize()==0){
			return "Klient: " + getPlayer1(gameID).getClientID() + ", har vunnit denna spelomgång!";
		}
		else if (getPlayer2(gameID).getPlayerCardSize()==0) {
			return "Klient: " + getPlayer2(gameID).getClientID() + ", har vunnit denna spelomgång!";
		}
		else if (getPlayer3(gameID).getPlayerCardSize()==0) {
			return "Klient: " + getPlayer3(gameID).getClientID() + ", har vunnit denna spelomgång!";
		}
		else if (getPlayer4(gameID).getPlayerCardSize()==0) {
			return "Klient: " + getPlayer4(gameID).getClientID() + ", har vunnit denna spelomgång!";
		}
		return null;
	}
}