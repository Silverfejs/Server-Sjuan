package sjuan;
import java.sql.*;

import javax.swing.JOptionPane;

/**
 * This class handles the communication with the database.
 * @author Anna
 *
 */
public class DataBase {

	public static Connection connection;
	public static Statement statement;
	public static java.sql.PreparedStatement statement1, statement2;
	private static String sql = "";
	private static int colCount;
	private static String antalVinster, antalFörluster, antalSpeladeSpel;


	/**
	 * Metoden returnerar alla kolumner och rader som finns databasen.
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	public static String showResultSet(ResultSet resultSet) throws SQLException {	
		ResultSetMetaData meta = resultSet.getMetaData();
		sql = "";
		colCount = meta.getColumnCount();	

		for(int i=1; i<=colCount; i++)
			sql += meta.getColumnLabel(i) + ", ";	

		sql += "\n";

		while(resultSet.next()) {
			for(int i=1; i<=colCount; i++)
				sql += resultSet.getObject(i).toString() + ", ";

			sql += "\n";
		}
		System.out.println(sql);
		return sql;
	}



	/**
	 * Metoden upprättar anslutning till databasen.
	 * @throws SQLException
	 */
	public static void connect() throws SQLException {
		try {

			Class.forName("com.mysql.jdbc.Driver"); 
			connection = DriverManager.getConnection("jdbc:mysql://195.178.232.7:4040/ab4607","ab4607","prinsessan"); 
			statement = connection.createStatement(); 

		} catch(ClassNotFoundException e1) {
			System.out.println("Databas-driver hittades ej: "+e1);
		}
	}

	/**
	 * Metoden avslutar databasuppkopplingen
	 * @throws SQLException
	 */
	public static void disconnect() throws SQLException {
		statement.close();
		connection.close();
	}

	public static void main(String[] args) {
		try {
			connect();

			disconnect();
		} catch(SQLException e) {
			System.out.println(e);
		}
	}

	/**
	 * Metoden kontrollerar så att användaren är registrerad genom att kontrollera att användarnamnet och 
	 * lösenordet som matats in stämmer överens med det användarnamn och lösenord som finns lagrat i DB.
	 * @param userName
	 * @param passWord
	 * @return
	 */
	public static boolean logInDb(String userName, String passWord){
		try {
			Class.forName("com.mysql.jdbc.Driver");	//Hämtar database-drivern
			connection = DriverManager.getConnection ("jdbc:mysql://195.178.232.7:4040/ab4607", "ab4607", "prinsessan");
			statement = connection.createStatement();	

			ResultSet res = statement.executeQuery("SELECT AnvändarNamn FROM statistics where AnvändarNamn='" 
					+ userName + "'and Lösenord='" + passWord + "'");
			return res.next();

		}catch ( ClassNotFoundException e ) {
			JOptionPane.showMessageDialog(null,e.getMessage());
		}

		catch(SQLException e) {
			JOptionPane.showMessageDialog(null,"Sql Error");
		}
		return false;
	}


	/**
	 * Metoden lägger till ny användare genom att ta det inmatade användarnamnet och lösenordet och lägga till i DB.
	 * @param AnvändarNamn
	 * @param LösenOrd
	 * @return
	 * @throws SQLException
	 */
	public static String connect(String AnvändarNamn, String LösenOrd) throws SQLException {
		try {

			Class.forName("com.mysql.jdbc.Driver"); 
			connection = DriverManager.getConnection("jdbc:mysql://195.178.232.7:4040/ab4607","ab4607","prinsessan");  
			statement1 = connection.prepareStatement("INSERT INTO statistics(AnvändarNamn, LösenOrd) VALUES(?,?)"); 

			statement1.setString(1, AnvändarNamn);
			statement1.setString(2, LösenOrd);
			statement1.executeUpdate(); 
		} catch(ClassNotFoundException e1) {
			System.out.println("Databas-driver hittades ej: "+e1);
		}
		return AnvändarNamn;
	}


	/*
	 * Metoden gör så att det adderas 1 till vinster kolumnen där användarNamnet är knut (AnvändarNamnet ska bytas ut mot de inloggade
	 * namnet. Dvs samma instansvariabel userName som i databasklassen.
	 */
	public static String vunnaSpel(String userName){
		try
		{
			connection = DriverManager.getConnection("jdbc:mysql://195.178.232.7:4040/ab4607","ab4607","prinsessan");
			statement = connection.createStatement();


			String vinst = "UPDATE statistics " + "SET Vinster = Vinster+1 WHERE AnvändarNamn " + "'" + userName + "'";
			statement.executeUpdate(vinst);
			String selectQuery = "SELECT Vinster FROM statistics WHERE AnvändarNamn = " + "'" + userName + "'";	
			ResultSet resultSet = statement.executeQuery(selectQuery);	

			while (resultSet.next()){
				String statistic = resultSet.getString("Vinster");
				JOptionPane.showMessageDialog( null, "Vinster: " + statistic);
			}	


		}
		catch (SQLException e)
		{
			System.out.println("Connection String is not correct:Unable to connect to the given database");
		}
		return antalVinster;

	}


	/*
	 * Metoden gör så att det adderas 1 till förluster kolumnen där användarNamnet är knut (AnvändarNamnet ska bytas ut mot de inloggade
	 * namnet. Dvs samma instansvariabel userName som i databasklassen.
	 */
	public static String lostGames(String userName) throws SQLException{
		try
		{
			connection = DriverManager.getConnection("jdbc:mysql://195.178.232.7:4040/ab4607","ab4607","prinsessan");
			statement = connection.createStatement();


			String Förluster = "UPDATE statistics " + "SET Förluster = Förluster + 1 WHERE AnvändarNamn = " + "'" + userName + "'";
			statement.executeUpdate(Förluster);
			String selectQuery = "SELECT Förluster FROM statistics WHERE AnvändarNamn = " + "'" + userName + "'";	
			ResultSet resultSet = statement.executeQuery(selectQuery);	

			while (resultSet.next()){
				String hej = resultSet.getString("Förluster");
				//				JOptionPane.showMessageDialog( null, "Förluster: " + hej);
			}	

		}
		catch (SQLException e)
		{
			System.out.println("Connection String is not correct:Unable to connect to the given database");
		}

		return antalFörluster;

	}

	public static String playedGames(String userName){/*
	 * Metoden gör så att det vinster + förluster läggs till i speladespel kolumnen där användarNamnet är knut (AnvändarNamnet ska bytas ut mot de inloggade
	 * namnet. Dvs samma instansvariabel userName som i databasklassen.
	 */
		try
		{
			connection = DriverManager.getConnection("jdbc:mysql://195.178.232.7:4040/ab4607","ab4607","prinsessan");
			statement = connection.createStatement();


			String SpeladeSpel = "UPDATE statistics " + "SET SpeladeSpel = SpeladeSpel + 1 WHERE AnvändarNamn = " + "'" + userName + "'";
			statement.executeUpdate(SpeladeSpel);
			String selectQuery = "SELECT SpeladeSpel FROM statistics WHERE AnvändarNamn = " + "'" + userName + "'";	
			ResultSet resultSet = statement.executeQuery(selectQuery);	

			while (resultSet.next()){
				JOptionPane.showMessageDialog( null, "SpeladeSpel: " + resultSet.getString("SpeladeSpel"));
			}	

		}
		catch (SQLException e)
		{
			System.out.println("Connection String is not correct:Unable to connect to the given database");
		}
		return antalSpeladeSpel;

	}

}

