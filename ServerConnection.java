package sjuan;

import java.io.*;
import java.net.*;
/**
 * this class handle the connection with a client to give response and request with a client
 * @author Tobbe
 *
 */
public class ServerConnection {
	private Server server;
	private ObjectOutputStream output;
	private ObjectInputStream input;

	/**
	 * constructs a server connection with a client
	 * @param server takes in a server
	 * @param socket takes in a socket 
	 * @throws IOException 
	 */
	public ServerConnection(Server server, Socket socket) throws IOException {
		this.server = server;
		output = new ObjectOutputStream(socket.getOutputStream());
		input = new ObjectInputStream(socket.getInputStream());
		Thread thread = new Thread(new RequestHandler());
		thread.start();
	}
	/**
	 * this method gets a response from a client
	 * @param response takes in a response from server
	 */
	public void newResponse(Response response) {
		try {
			output.writeObject(response);
			output.flush();
			output.reset();

		}catch (IOException e) {
			e.getStackTrace();
			System.out.println(e);
		}
	}
	/**
	 * this class handle a request from a Client
	 *
	 */
	private class RequestHandler implements Runnable {
		public void run() {
			Request request;
			try {
				while (true) {
					request = (Request)input.readObject();
					server.newRequest(ServerConnection.this, request);
				}
			} catch (Exception e1) {
				e1.getStackTrace();
				System.out.println(e1);
			}
		}
	}
}
