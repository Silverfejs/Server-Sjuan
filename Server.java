package sjuan;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class handles the server communication with clients
 * @author Tobbe
 *
 */
public class Server {
	private int clientID;
	private Lobby lobby = new Lobby();
	private HashMap <Integer, ServerConnection> connectionsList = new HashMap <Integer, ServerConnection>() ;
	private ArrayList <Integer> readyClientsConnections = new ArrayList<Integer>();
	private ArrayList <Integer> ready2ClientsConnections = new ArrayList<Integer>();
	private ArrayList <Integer> ready3ClientsConnections = new ArrayList<Integer>();
	private ArrayList <Integer> ready4ClientsConnections = new ArrayList<Integer>();
	private DataBase databas = new DataBase();
	private HashMap <Integer, Controller> controllerList = new HashMap <Integer, Controller>();

	/**
	 * constructs a server 
	 * @param port takes in a portNumber
	 */
	public Server(int port) {
		try {
			new ConnectToServer(this, port);
		}
		catch (Exception e) { // IOException, ClassNotFoundException
			System.out.println(e);
			e.getStackTrace();
		}
	}

	/**
	 * this method store a clients connection in connectionsList
	 * @param connection takes in a connection from a client
	 * @param counter takes in a counter that sets the clients ID
	 */
	public void newClient(ServerConnection connection, int counter) {
		clientID = counter;
		connectionsList.put(counter, connection);
	}

	/**
	 * this method creates a response for a client to receive
	 * @param connection takes in connection from a client
	 * @param request takes in a request to decide what to do
	 */
	public synchronized void newRequest(ServerConnection connection, Request request) throws SQLException {
		//skickar vidare ett klientID till klienten
		if (request.getRequest().equals("clientID")) {
			connection.newResponse(new Response("clientID" , clientID, request.isHumanPlayer(), request.getNbrOfAI()));
		}
		//Anropas om en klient vill enbart spela mot AIspelare
		else if (request.getRequest().equals("newAIPlayer")) {
			readyClientsConnections.add(request.getClientID());
			if (readyClientsConnections.size()<4) {
				lobby.waitingRoom(request.getClientID(), this);
				connectionsList.get(request.getClientID()).newResponse(new Response
						("createAI", request.getClientID(), false, 3));
			}
			else {
				lobby.waitingRoom(request.getClientID(), this);
				connectionsList.get(readyClientsConnections.get(0)).newResponse(new Response
						("newGame", readyClientsConnections.get(0), 
								lobby.getGameID(request.getClientID())));
			}
		}
		//Anropas om en klient vill enbart spela mot en mänsklig spelare och 2 AIspelare
		else if (request.getRequest().equals("twoPlayerGame")) {
			ready2ClientsConnections.add(request.getClientID());
			if (ready2ClientsConnections.size()>=2 && ready2ClientsConnections.size()<4){
				connectionsList.get(request.getClientID()).newResponse(new Response
						("createAI", request.getClientID(), false, 2));
			}
			else if (ready2ClientsConnections.size()==4){
				for (int client : ready2ClientsConnections) {
					lobby.waitingRoom2(client, this);
				}
				connectionsList.get(ready2ClientsConnections.get(0)).newResponse(new Response
						("newGame2", ready2ClientsConnections.get(0), 
								lobby.getGameID(request.getClientID())));
			}
		}
		//Anropas om en klient vill enbart spela mot två mänskliga spelare och 1 AIspelare
		else if (request.getRequest().equals("threePlayerGame")) {
			ready3ClientsConnections.add(request.getClientID());
			if (ready3ClientsConnections.size()>=3 && ready3ClientsConnections.size()<4){
				connectionsList.get(request.getClientID()).newResponse(new Response
						("createAI", request.getClientID(), false, 1));
			}
			else if (ready3ClientsConnections.size()==4){
				for (int client : ready3ClientsConnections) {
					lobby.waitingRoom3(client, this);
				}
				connectionsList.get(ready3ClientsConnections.get(0)).newResponse(new Response
						("newGame3", ready3ClientsConnections.get(0), 
								lobby.getGameID(request.getClientID())));
			}
		}
		//Anropas om en klient vill enbart spela mmänskliga spelare 
		else if (request.getRequest().equals("fourPlayerGame")) {
			ready4ClientsConnections.add(request.getClientID());
			if (ready4ClientsConnections.size()==4) {
				for (int client : ready4ClientsConnections) {
					lobby.waitingRoom4(client, this);
				}
				connectionsList.get(ready4ClientsConnections.get(0)).newResponse(new Response
						("newGame4", ready4ClientsConnections.get(0), 
								lobby.getGameID(request.getClientID())));
			}
		}

		//skapar en användare i databasen
		else if (request.getRequest().equals("createUser")) {
			try {
				DataBase.connect(request.getUserName(), request.getPassWord());
				JOptionPane.showMessageDialog(null, "Välkommen till sjuan " + request.getUserName() + ":)");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			connection.newResponse(new Response("Login", logInDb(request.getUserName(), request.getPassWord()), request.getUserName()));
		}

		//loggar in en användare i databasen
		else if(request.getRequest().equals("Login")){
			connection.newResponse(new Response("Login", logInDb(request.getUserName(), request.getPassWord()), request.getUserName()));
		}
		//sätter ifall en klient är en AI eller mänsklig spelare
		else if (request.getRequest().equals("setPlayerHumanOrAI")) {
			//			System.out.println(request.getClientID() + " "+ request.isHumanPlayer());

		}
		// startar ett nytt spel
		else if (request.getRequest().equals("newGame")) {
			//			System.out.println("spelet startas");
			Controller controller = controllerList.get(request.getGameID());
			Player player1 = controller.getPlayer1(controller.getGameID());
			Player player2 = controller.getPlayer2(controller.getGameID());
			Player player3 = controller.getPlayer3(controller.getGameID());
			Player player4 = controller.getPlayer4(controller.getGameID());
			controller.whoHaveHeartSeven();

			connectionsList.get(readyClientsConnections.get(0)).newResponse(new Response("newGame", 
					player1, player2.getPlayerCardSize(),player3.getPlayerCardSize(), 
					player4.getPlayerCardSize(), readyClientsConnections.get(0),
					controller.getGameID(), player1.isHasHeart7()));

			connectionsList.get(readyClientsConnections.get(1)).newResponse(new Response("newGame", 
					player2, player3.getPlayerCardSize(), player4.getPlayerCardSize(),
					player1.getPlayerCardSize(), readyClientsConnections.get(1), 
					controller.getGameID(), player2.isHasHeart7()));

			connectionsList.get(readyClientsConnections.get(2)).newResponse(new Response("newGame", 
					player3, player4.getPlayerCardSize(), player1.getPlayerCardSize(), 
					player2.getPlayerCardSize(), readyClientsConnections.get(2),
					controller.getGameID(), player3.isHasHeart7()));

			connectionsList.get(readyClientsConnections.get(3)).newResponse(new Response("newGame", 
					player4, player1.getPlayerCardSize(), player2.getPlayerCardSize(), 
					player3.getPlayerCardSize(), readyClientsConnections.get(3), 
					controller.getGameID(), player4.isHasHeart7()));

			readyClientsConnections.clear();
		}

		// startar ett nytt spel
		else if (request.getRequest().equals("newGame2")) {
			//			System.out.println("spelet startas");
			Controller controller = controllerList.get(request.getGameID());
			Player player1 = controller.getPlayer1(controller.getGameID());
			Player player2 = controller.getPlayer2(controller.getGameID());
			Player player3 = controller.getPlayer3(controller.getGameID());
			Player player4 = controller.getPlayer4(controller.getGameID());
			controller.whoHaveHeartSeven();

			connectionsList.get(ready2ClientsConnections.get(0)).newResponse(new Response("newGame2", 
					player1, player2.getPlayerCardSize(),player3.getPlayerCardSize(), 
					player4.getPlayerCardSize(), ready2ClientsConnections.get(0),
					controller.getGameID(), player1.isHasHeart7()));

			connectionsList.get(ready2ClientsConnections.get(1)).newResponse(new Response("newGame2", 
					player2, player3.getPlayerCardSize(), player4.getPlayerCardSize(),
					player1.getPlayerCardSize(), ready2ClientsConnections.get(1), 
					controller.getGameID(), player2.isHasHeart7()));

			connectionsList.get(ready2ClientsConnections.get(2)).newResponse(new Response("newGame2", 
					player3, player4.getPlayerCardSize(), player1.getPlayerCardSize(), 
					player2.getPlayerCardSize(), ready2ClientsConnections.get(2),
					controller.getGameID(), player3.isHasHeart7()));

			connectionsList.get(ready2ClientsConnections.get(3)).newResponse(new Response("newGame2", 
					player4, player1.getPlayerCardSize(), player2.getPlayerCardSize(), 
					player3.getPlayerCardSize(), ready2ClientsConnections.get(3), 
					controller.getGameID(), player4.isHasHeart7()));

			ready2ClientsConnections.clear();
		}

		// startar ett nytt spel
		else if (request.getRequest().equals("newGame3")) {
			//			System.out.println("spelet startas");
			Controller controller = controllerList.get(request.getGameID());
			Player player1 = controller.getPlayer1(controller.getGameID());
			Player player2 = controller.getPlayer2(controller.getGameID());
			Player player3 = controller.getPlayer3(controller.getGameID());
			Player player4 = controller.getPlayer4(controller.getGameID());
			controller.whoHaveHeartSeven();

			connectionsList.get(ready3ClientsConnections.get(0)).newResponse(new Response("newGame3", 
					player1, player2.getPlayerCardSize(),player3.getPlayerCardSize(), 
					player4.getPlayerCardSize(), ready3ClientsConnections.get(0),
					controller.getGameID(), player1.isHasHeart7()));

			connectionsList.get(ready3ClientsConnections.get(1)).newResponse(new Response("newGame3", 
					player2, player3.getPlayerCardSize(), player4.getPlayerCardSize(),
					player1.getPlayerCardSize(), ready3ClientsConnections.get(1), 
					controller.getGameID(), player2.isHasHeart7()));

			connectionsList.get(ready3ClientsConnections.get(2)).newResponse(new Response("newGame3", 
					player3, player4.getPlayerCardSize(), player1.getPlayerCardSize(), 
					player2.getPlayerCardSize(), ready3ClientsConnections.get(2),
					controller.getGameID(), player3.isHasHeart7()));

			connectionsList.get(ready3ClientsConnections.get(3)).newResponse(new Response("newGame3", 
					player4, player1.getPlayerCardSize(), player2.getPlayerCardSize(), 
					player3.getPlayerCardSize(), ready3ClientsConnections.get(3), 
					controller.getGameID(), player4.isHasHeart7()));

			ready3ClientsConnections.clear();
		}

		// startar ett nytt spel
		else if (request.getRequest().equals("newGame4")) {
			//			System.out.println("spelet startas");
			Controller controller = controllerList.get(request.getGameID());
			Player player1 = controller.getPlayer1(controller.getGameID());
			Player player2 = controller.getPlayer2(controller.getGameID());
			Player player3 = controller.getPlayer3(controller.getGameID());
			Player player4 = controller.getPlayer4(controller.getGameID());
			controller.whoHaveHeartSeven();

			connectionsList.get(ready4ClientsConnections.get(0)).newResponse(new Response("newGame4", 
					player1, player2.getPlayerCardSize(),player3.getPlayerCardSize(), 
					player4.getPlayerCardSize(), ready4ClientsConnections.get(0),
					controller.getGameID(), player1.isHasHeart7()));

			connectionsList.get(ready4ClientsConnections.get(1)).newResponse(new Response("newGame4", 
					player2, player3.getPlayerCardSize(), player4.getPlayerCardSize(),
					player1.getPlayerCardSize(), ready4ClientsConnections.get(1), 
					controller.getGameID(), player2.isHasHeart7()));

			connectionsList.get(ready4ClientsConnections.get(2)).newResponse(new Response("newGame4", 
					player3, player4.getPlayerCardSize(), player1.getPlayerCardSize(), 
					player2.getPlayerCardSize(), ready4ClientsConnections.get(2),
					controller.getGameID(), player3.isHasHeart7()));

			connectionsList.get(ready4ClientsConnections.get(3)).newResponse(new Response("newGame4", 
					player4, player1.getPlayerCardSize(), player2.getPlayerCardSize(), 
					player3.getPlayerCardSize(), ready4ClientsConnections.get(3), 
					controller.getGameID(), player4.isHasHeart7()));

			ready4ClientsConnections.clear();
		}

		//kontrollerar ifall en klient kan passa eller inte
		else if(request.getRequest().equals("pass")) {
			if (controllerList.get(request.getGameID()).checkIfPassIsPossible(request.getClientID(), request.getGameID())) { 
				connectionsList.get(request.getClientID()).newResponse(new Response("pass", request.getClientID(), request.getGameID(), 
						request.getPassCounter(), controllerList.get(request.getGameID()).getPlayerByClientID(request.getGameID(), request.getClientID())));
				//				System.out.println(request.getClientID() + ": har passat. counter = " + request.getPassCounter());

			}
			else {
				connection.newResponse(new Response("passainte", request.getClientID()));
			}
		}
		// Anropar databasen
		//		else if(request.getRequest().equals("database")){
		//			connection.newResponse(new Response("database", databas.playedGames(request.getUserName())));
		//		}

		//kontrollerar och spelar ut ett kort om det går
		else if (request.getRequest().equals("playCard")) {
			Controller controller = controllerList.get(request.getGameID());
			if (controller.checkIfCardIsPlayable(request.getCardName(), request.getClientID(),
					request.getGameID())) { 
				//				System.out.println(request.getClientID() + ": har spelat: " + request.getCardName());
				String ifPlayerWin = controller.playerWin(request.getGameID());
				//				System.out.println(ifPlayerWin);

				int clientID1 = controller.getPlayer1(request.getGameID()).getClientID();
				int clientID2 = controller.getPlayer2(request.getGameID()).getClientID();
				int clientID3 = controller.getPlayer3(request.getGameID()).getClientID();
				int clientID4 = controller.getPlayer4(request.getGameID()).getClientID();
				connectionsList.get(request.getClientID()).newResponse(new Response("updatePlayerWithAI",
						controller.getPlayerByClientID(request.getGameID(), request.getClientID()),
						controller.getOpponent1HandSize(request.getGameID(), request.getClientID()),
						controller.getOpponent2HandSize(request.getGameID(), request.getClientID()), 
						controller.getOpponent3HandSize(request.getGameID(), request.getClientID()), 
						controller.getGameBoardCards(request.getGameID()), request.getClientID(), 
						request.getPassCounter(), ifPlayerWin));
				for (int i = 4;i>0;i--) {

					if (i==4) {
						connectionsList.get(clientID1).newResponse(new Response("update", 
								controller.getPlayerByClientID(request.getGameID(), clientID1),
								controller.getOpponent1HandSize(request.getGameID(), clientID1),
								controller.getOpponent2HandSize(request.getGameID(), clientID1), 
								controller.getOpponent3HandSize(request.getGameID(), clientID1), 
								controller.getGameBoardCards(request.getGameID()), clientID1, 
								request.getPassCounter(), ifPlayerWin));
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else if (i==3) {
						connectionsList.get(clientID2).newResponse(new Response("update", 
								controller.getPlayerByClientID(request.getGameID(), clientID2),
								controller.getOpponent1HandSize(request.getGameID(), clientID2),
								controller.getOpponent2HandSize(request.getGameID(), clientID2), 
								controller.getOpponent3HandSize(request.getGameID(), clientID2), 
								controller.getGameBoardCards(request.getGameID()), clientID2, 
								request.getPassCounter(), ifPlayerWin));
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else if (i==2){
						connectionsList.get(clientID3).newResponse(new Response("update", 
								controller.getPlayerByClientID(request.getGameID(), clientID3),
								controller.getOpponent1HandSize(request.getGameID(), clientID3),
								controller.getOpponent2HandSize(request.getGameID(), clientID3), 
								controller.getOpponent3HandSize(request.getGameID(), clientID3), 
								controller.getGameBoardCards(request.getGameID()), clientID3, 
								request.getPassCounter(), ifPlayerWin));
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else if (i==1) {
						connectionsList.get(clientID4).newResponse(new Response("update", 
								controller.getPlayerByClientID(request.getGameID(), clientID4),
								controller.getOpponent1HandSize(request.getGameID(), clientID4),
								controller.getOpponent2HandSize(request.getGameID(), clientID4), 
								controller.getOpponent3HandSize(request.getGameID(), clientID4), 
								controller.getGameBoardCards(request.getGameID()), clientID4, 
								request.getPassCounter(), ifPlayerWin));
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			else {
				connection.newResponse(new Response("dontPlayCard", request.getClientID()));
			}
		}

		// ger bort ett kort
		else if (request.getRequest().equals("giveACard")) {
			Controller controller = controllerList.get(request.getGameID());
			int clientID = controller.setNextPlayersTurn(request.getClientID(), 
					request.getGameID());
			String ifPlayerWin = controller.playerWin(request.getGameID());
			//			System.out.println(ifPlayerWin);

			connectionsList.get(clientID).newResponse(new Response("updateAndGiveCard", 
					controller.getPlayerByClientID(request.getGameID(), clientID),
					controller.getOpponent1HandSize(request.getGameID(), clientID),
					controller.getOpponent2HandSize(request.getGameID(), clientID), 
					controller.getOpponent3HandSize(request.getGameID(), clientID), 
					controller.getGameBoardCards(request.getGameID()), clientID, request.getPassCounter(), ifPlayerWin));
			//			System.out.println(clientID + " ska ge ett kort. counter = " + request.getPassCounter());
		}

		// ger bort ett kort
		else if (request.getRequest().equals("giveACardToAPlayer")) {
			//			System.out.println(request.getClientID() + " ger ett kort. passCount=" + request.getPassCounter());
			Controller controller = controllerList.get(request.getGameID());
			controller.giveCard(request.getCardName(), request.getClientID(), request.getGameID());

			String ifPlayerWin = controller.playerWin(request.getGameID());
			//			System.out.println(ifPlayerWin);

			int clientID = controller.setNextPlayersTurn(request.getClientID(), 
					request.getGameID());
			connectionsList.get(clientID).newResponse(new Response("wakePlayer", clientID, 
					request.getGameID(), request.getPassCounter(), 
					controller.getPlayerByClientID(request.getGameID(), clientID)));	
		}

		//tar emot kort
		else if (request.getRequest().equals("recieveCards")) {
			Controller controller = controllerList.get(request.getGameID());
			int clientID = controller.setNextPlayersTurn(request.getClientID(), 
					request.getGameID());
			if (controller.getPlayerByClientID(request.getGameID(), request.getClientID()).isHumanPlayer()==false) {
				controller.addRecievedCardsToPassedPlayer(request.getClientID(), request.getGameID());
				//				System.out.println(request.getClientID() + ": tar emot kort");

			}
			else if (controller.getPlayerByClientID(request.getGameID(), request.getClientID()).isHumanPlayer()==true) {
				controller.addRecievedCardsToPassedPlayer(clientID, request.getGameID());
				//				System.out.println(clientID + ": tar emot kort");
			}

			connectionsList.get(clientID).newResponse(new Response("wakePlayer", 
					controller.getPlayerByClientID(request.getGameID(), clientID),
					controller.getOpponent1HandSize(request.getGameID(), clientID),
					controller.getOpponent2HandSize(request.getGameID(), clientID), 
					controller.getOpponent3HandSize(request.getGameID(), clientID), 
					controller.getGameBoardCards(request.getGameID()), clientID, request.getPassCounter(), null));
		}

		// anropar nästa spelare
		else if (request.getRequest().equals("nextPlayer")) {
			connectionsList.get(controllerList.get(request.getGameID()).setNextPlayersTurn(request.getClientID(), 
					request.getGameID())).newResponse(new Response("wakePlayer",
							controllerList.get(request.getGameID()).setNextPlayersTurn
							(request.getClientID(), request.getGameID()), request.getPassCounter()));
			//			System.out.println(request.getClientID() + ": väcker nästa " );

		}
		// skickar spelupdatering
		else if (request.getRequest().equals("getGameConditions")) {
			Controller controller = controllerList.get(request.getGameID());
			connection.newResponse(new Response("updateAndPlayCard", request.getClientID(), request.getGameID(), 
					controller.getGameBoardCards(request.getGameID()), 
					controller.getPlayerByClientID(request.getGameID(), request.getClientID())));
		}
		//skickar speluppdatering
		else if (request.getRequest().equals("getHumanGameConditions")) {
			Controller controller = controllerList.get(request.getGameID());
			connection.newResponse(new Response("update", request.getClientID(), request.getGameID(), 
					controller.getGameBoardCards(request.getGameID()), 
					controller.getPlayerByClientID(request.getGameID(), request.getClientID())));
		}
		//skickar speluppdatering

		else if (request.getRequest().equals("getAllGameConditions")) {
			Controller controller = controllerList.get(request.getGameID());
			String ifPlayerWin = controller.playerWin(request.getGameID());
			//			System.out.println(ifPlayerWin);
			connection.newResponse(new Response("updateAll", controller.getPlayerByClientID(request.getGameID(), request.getClientID()),
					controller.getOpponent1HandSize(request.getGameID(), request.getClientID()),
					controller.getOpponent2HandSize(request.getGameID(), request.getClientID()), 
					controller.getOpponent3HandSize(request.getGameID(), request.getClientID()), 
					controller.getGameBoardCards(request.getGameID()), request.getClientID(), request.getPassCounter(), ifPlayerWin));
			//			System.out.println(request.getClientID() + ": uppdaterar spelet " );

		}
		else {
			connection.newResponse(new Response("clientsMissing", request.getClientID()));
		}
	}

	/**
	 * this method creates a new controller
	 * @param client1 takes in a client
	 * @param client2 takes in a client
	 * @param client3 takes in a client
	 * @param client4 takes in a client
	 * @param gameID takes in a gameID
	 */
	public void createGame(int client1, int client2, int client3, int client4, int gameID) {
		controllerList.put(gameID, new Controller(this, gameID, client1, client2, client3, client4));
	}

	/**
	 * this method returns a boolean
	 * @return true if the name and password is correct 
	 */

	public boolean logInDb(String userName, String passWord){
		return databas.logInDb(userName, passWord);
	}
}
