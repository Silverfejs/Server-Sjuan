package sjuan;
import java.io.*;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * this class handle responses
 * @author Sjuan
 *
 */
public class Response implements Serializable {

	private static final long serialVersionUID = 1L;
	private String request, sql, cardName, ifPlayerWin, userName;
	private ArrayList <Card> cards, gameBoardCards;
	private int opponentCards1, opponentCards2, opponentCards3, clientID, gameID, passCounter, nbrOfAI;
	private boolean hasHeart7, humanPlayer, logOk;

	/**
	 * constructs a response containing a string
	 * @param str takes in a string-Object
	 * 
	 */
	public Response(String request, int clientID) {
		this.request = request;
		this.clientID = clientID;
	}

	/**
	 * constructs a response containing a string and a boolean
	 * @param request takes in a string-Object
	 * @param logOk takes in a boolean
	 */
	public Response(String request, boolean logOk, String userName){
		this.request = request;
		this.logOk = logOk;
		this.userName = userName;
	}

	public Response (String request, String sql) {
		this.request = request;
		this.sql = sql;
	}

	public Response(String request, int clientID, boolean humanPlayer, int nbrOfAI) {
		this.request = request;
		this.clientID = clientID;
		this.humanPlayer = humanPlayer;
		this.nbrOfAI = nbrOfAI;
	}

	public Response(String request, int clientID, int gameID) {
		this.request = request;
		this.clientID = clientID;
		this.gameID = gameID;
	}

	public Response(String request, int clientID, int gameID, int count, Player player) {
		this.request = request;
		this.clientID = clientID;
		this.gameID = gameID;
		this.passCounter = count;
		this.cards = player.getPlayerCards();
	}

	public Response(String request, int clientID, int gameID, 
			ArrayList <Card> gameBoardCards, Player player) {
		this.request = request;
		this.clientID = clientID;
		this.gameID = gameID;
		this.gameBoardCards = gameBoardCards;
		this.cards = player.getPlayerCards();
	}
	/**
	 * constructs a response containing a request, cardName, list of a players cards
	 * and a list of game board cards 
	 * @param request
	 * @param cardName
	 * @param cards
	 * @param gameBoardCards
	 */
	public Response (String request, String cardName, Player player,
			ArrayList<Card> gameBoardCards, int clientID) {
		this.request = request;
		this.cardName = cardName;
		this.cards = player.getPlayerCards();
		this.gameBoardCards = gameBoardCards;
		this.clientID = clientID;

	}

	/**
	 * constructs a response containing four players hands of cards and a string-Object
	 * @param playerCardList takes in a cards of a player
	 * @param playerCardSize takes in a player cards size
	 * @param playerCardSize2 takes in a player cards size
	 * @param playerCardSize3 takes in a player cards size
	 */
	public Response(String request, Player player,
			int opponentCards1, int opponentCards2, int opponentCards3, 
			int clientID, int gameID, boolean hasHeart7) {
		this.request = request;
		this.clientID = clientID;
		this.gameID = gameID;
		this.cards = player.getPlayerCards();
		this.opponentCards1 = opponentCards1;
		this.opponentCards2 = opponentCards2;
		this.opponentCards3 = opponentCards3;
		this.hasHeart7 = hasHeart7;

	}

	public Response(String request, Player player,
			int opponentCards1, int opponentCards2, int opponentCards3, 
			ArrayList<Card> gameBoardCards, int clientID, int counter, String ifPlayerWin) {
		this.request = request;
		this.cards = player.getPlayerCards();
		this.opponentCards1 = opponentCards1;
		this.opponentCards2 = opponentCards2;
		this.opponentCards3 = opponentCards3;
		this.gameBoardCards = gameBoardCards;
		this.clientID = clientID;
		this.passCounter = counter;
		this.ifPlayerWin = ifPlayerWin;
	}


	/**
	 * this method returns a request
	 * @return request returns a request
	 */
	public String getRequest() {
		return request;
	}

	/**
	 * this method returns a players cards
	 * @return cards returns a players cards
	 */
	public ArrayList<Card> getCards() {
		return cards;
	}

	/**
	 * this method returns sql String
	 * @return sql returns a string of sql
	 */
	public String getSql(){
		return sql;
	}

	/**
	 * this method returns how many cards the one of the oppenent have
	 * @return opponentCards1 return an int of opponents cards size
	 */
	public int getOpponentCards1() {
		return opponentCards1;
	}

	/**
	 * this method returns how many cards the one of the oppenent have
	 * @return opponentCards2 return an int of opponents cards size
	 */
	public int getOpponentCards2() {
		return opponentCards2;
	}

	/**
	 * this method returns how many cards the one of the oppenent have
	 * @return opponentCards3 return an int of opponents cards size
	 */
	public int getOpponentCards3() {
		return opponentCards3;
	}

	/**
	 * this method returns ID for a client
	 * @return client returns Id for a client
	 */
	public int getClientID() {
		return clientID;
	}

	/**
	 * this method returns a String of a card name
	 * @return cardName returns a name of a card
	 */
	public String getCardName() {
		return cardName;
	}

	/**
	 * this methos returns the cards that is played at game board
	 * @return gameBoardCards return the cards that been played
	 */
	public ArrayList<Card> getGameBoardCards () {
		return gameBoardCards;
	}

	public boolean getLogOk(){
		return logOk;
	}

	/**
	 * this method returns a gameID of the game
	 * @return gameID returns a Integer of a gameID
	 */
	public int getGameID() {
		return gameID;
	}

	/**
	 * this method returns a boolean if a player have hearts of seven card
	 * @return hasheart7 returns a boolean if a player have card "h7"
	 */
	public boolean isHasHeart7() {
		return hasHeart7;
	}

	public int getPassCounter() {
		return passCounter;
	}

	public boolean isHumanPlayer() {
		return humanPlayer;
	}

	public String getIfPlayerWin() {
		return ifPlayerWin;
	}

	public int getNbrOfAI() {
		return nbrOfAI;
	}

	public String getUserName() {
		return userName;
	}
}