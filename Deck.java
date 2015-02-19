package sjuan;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class handle the deck of cards of the game "Sjuan"
 * @author Tobbe
 *
 */
public class Deck {
	private ArrayList<Card> cards;

	/**
	 * the constructor creates a deck of card and shuffle it
	 */
	public Deck() {
		cards = new ArrayList<Card>(52);
		int index1, index2;
		Random rand = new Random();
		Card temp;

		for (int type=0; type<=3; type++) {
			for (int value=0; value<=12; value++) {
				cards.add(new Card(type, value));
			}
		}      
		int size = cards.size() -1;

		// this is the part when the deck gets shuffled
		for (int i=0; i<100; i++) {
			index1 = rand.nextInt(size+1);
			index2 = rand.nextInt(size+1);

			temp = cards.get( index2 );
			cards.set( index2 , cards.get( index1 ) );
			cards.set( index1, temp );
		}
	}

	/**
	 * this method deals a card
	 * @return card returns a card and removes it from the deck
	 */
	public Card dealCard() {       
		return cards.remove( 0 );
	}
	
	/**
	 * this method returns the number of cards deck
	 * @return cards.size() returns the size of the deck
	 */
	public int getAllCards() {
		return cards.size();
	}
}