package sjuan;
import java.io.*;
import java.sql.Connection;
import java.sql.Statement;

/**
 * this class handles requests
 * @author Sjuan
 *
 */
public class Request implements Serializable {
	private static final long serialVersionUID = 1L;

	private String request, cardName;
	private String sql, userName, passWord;
	private int clientID, gameID, passCounter, nbrOfAI;
	private boolean humanPlayer;


	/**
	 * constructs a request
	 * @param request takes in a request
	 */

	public Request(String request, int clientID, int gameID, boolean humanPlayer, int nbrOfAI) {
		this.request = request;
		this.clientID = clientID;
		this.gameID = gameID;
		this.humanPlayer = humanPlayer;
		this.nbrOfAI = nbrOfAI;
	}

	/**
	 * constructs a request
	 * @param request takes in a request
	 * @param cardName takes in a name of a card
	 */

	public Request(String request, String cardName, int clientID, int gameID) {
		this.request = request;
		this.cardName = cardName;
		this.clientID = clientID;
		this.gameID = gameID;
	}

	/**
	 * constructs a request
	 * @param request takes in a request
	 * @param cardName takes in a cardName of a card
	 * @param clientID takes in the clients ID
	 */
	public Request(String request, String cardName, int clientID, int gameID, int passCounter) {
		this.request = request;
		this.cardName = cardName;
		this.clientID = clientID;
		this.gameID = gameID;
		this.passCounter = passCounter;

	}

	public Request(String request, String userName, String passWord){
		this.request = request;
		this.userName = userName;
		this.passWord = passWord;
	}

	/**
	 * this method returns a request
	 * @return request returns a request as a string
	 */
	public String getRequest() {
		return request;
	}

	/**
	 * this method returns a clientID
	 * @return clientID returns a requested clientID
	 */

	public int getClientID() {
		return clientID;
	}

	/**
	 * this method returns a cardName of a card
	 * @return cardName returns a cardName of a card
	 */
	public String getCardName() {
		return cardName;
	}

	/**
	 * this method returns a username
	 * @return username
	 */

	public String getUserName(){
		return userName;
	}

	/**
	 * this method returns a password
	 * @return password
	 */

	public String getPassWord(){
		return passWord;
	}

	/**
	 * this method returns a gameID
	 * @return gameID returns a Integer of a game
	 */
	public int getGameID() {
		return gameID;
	}

	/**
	 * this method returns a passCounter
	 * @return passCounter controlled how many that have give a card 
	 */
	public int getPassCounter() {
		return passCounter;
	}
	
	public boolean isHumanPlayer() {
		return humanPlayer;
	}

	public int getNbrOfAI() {
		return nbrOfAI;
	}
}

