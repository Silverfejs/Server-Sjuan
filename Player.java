package sjuan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * This class handle a player
 * @author Tobbe
 *
 */
public class Player implements Serializable{

	private static final long serialVersionUID = 1L;
	private ArrayList<Card> playerCards;
//	private String name;
	private boolean humanPlayer = false, hasHeart7 = false;
	private int clientID, gameID;
	
	/**
	 * the constructor creates a player
	 */
	public Player(int clientID, int gameID) {
		playerCards = new ArrayList<Card>();
		this.clientID = clientID;
		this.setGameID(gameID);
	}

	/**
	 * This method sets the players cards
	 * @param card gets a card
	 */
	public void setPlayerCards(Card card) {
		playerCards.add(card);
	}

	/**
	 * this method sets a players hand of cards
	 * @param playerCards takes in a hand of cards
	 */
	public void setPlayerCards(ArrayList<Card> playerCards) {
		this.playerCards = playerCards;
	}

	/**
	 * this method translates players ArrayList of Cards to an Array of Strings
	 * @return
	 */
	public String[] getPlayerCardsToString() {
		String[] cards = new String[playerCards.size()];
		for (int i = 0; i < cards.length; i++){
			cards[i] = playerCards.get(i).toString();
		}
		return cards;
	}

	/**
	 * this method returns a player cards as in a ArrayList of Card
	 * @return playerCards returns a ArrayList of Card
	 */
	public ArrayList<Card> getPlayerCards() {
		return playerCards;
	}
	
	/**
	 * This method prints players cards
	 */
	public void printCards() {
		for (int i = 0; i < playerCards.size(); i++) {
			System.out.print(playerCards.get(i) + ", ");
		}
	}
	
	/**
	 * this method returns players size of a hand
	 * @return playerCards.size() returns players hand of cards 
	 */
	public int getPlayerCardSize() {
		return playerCards.size();
	}

	/**
	 * this method returns a card from a players hand using a String name of the card
	 * @param cardName takes in a name of a card as a String
	 * @return a card the matches the cardName
	 */
	public Card getCardByName(String cardName) {
		for (Card card : playerCards) {
			if (card.toString().equals(cardName)) {
				return card;
			}
		}
		return null;
	}

	/**
	 * this method returns clientID from a player
	 * @return clientID returns a Integer of a clientID
	 */
	public int getClientID() {
		return clientID;
	}

	/**
	 * this method sets a clientID for a player
	 * @param clientID takes in an ID from a client
	 */
	public void setClientID(int clientID) {
		this.clientID = clientID;
	}
	
	/**
	 * this method returns a gameID player is playing
	 * @return gameID returns a Integer of a gameID that player is playing
	 */
	public int getGameID() {
		return gameID;
	}
	
	/**
	 * this method sets a gameID to a player
	 * @param gameID takes in a Integer of a gameID
	 */
	public void setGameID(int gameID) {
		this.gameID = gameID;
	}
	
	/**
	 * this method returns a boolean if this player have hearts of seven in is hand of cards
	 * @return hasHeart7 returns a boolean if a player have card "h7"
	 */
	public boolean isHasHeart7() {
		return hasHeart7;
	}
	
	/**
	 * this method sets hasHeart7 to true
	 */
	public void hasHeart7() {
		this.hasHeart7 = true;
	}
	public boolean isHumanPlayer() {
		return humanPlayer;
	}
	public void setHumanPlayer(boolean humanPlayer) {
		this.humanPlayer = humanPlayer;
	}
}

