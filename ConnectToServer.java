package sjuan;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * this class handle clients connectiong to the server
 * @author Sjuan
 *
 */
public class ConnectToServer {
	private Server server;
	private int port;
	private int counter = 0;

	/**
	 * construct a connection to server
	 * @param server
	 * @param port
	 */
	public ConnectToServer(Server server, int port) {

		this.server = server;
		this.port = port;
			Thread thread = new Thread(new Connect());
			thread.start();
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
//		System.out.println(counter);
	}

	private class Connect implements Runnable {
		public void run() {
			ServerSocket serverSocket = null;
			Socket socket;
			try {
				serverSocket = new ServerSocket(port);
				while (true) {
					socket = serverSocket.accept();
					setCounter(getCounter() + 1);
//					System.out.println("New client" + getCounter());
					server.newClient(new ServerConnection(server,socket), counter);
				}
			} catch (IOException e1) {
				e1.getStackTrace();
				System.out.println(e1);
			}
			try {
				serverSocket.close();
			} catch (Exception e) {
 				e.getStackTrace();
			}
		}
	}
}
