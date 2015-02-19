package sjuan;

import java.io.Serializable;
import java.util.Comparator;

/**
 * This class handle a card
 * @author Tobbe
 *
 */
public class Card implements Serializable, Comparable<Card> {
	private int value, type;

	private transient final static String[] types = { "h", "s", "d", "c" };
	private transient final static String[] values  = { "1", "2", "3", "4", 
		"5", "6", "7", "8", "9", "10", "j", "q", "k" };

	/**
	 * the construktor creates a card
	 * @param type
	 * @param value
	 */
	public Card(int type, int value) {
		this.type=type;
		this.value=value;
	}
	/**
	 * this method returns a String of a Card Object
	 * @return returns a String of a Card Object
	 */
	public String toString() {
		return types[type] + values[value];
	}
	/**
	 * this method returns value of a Card
	 * @return value returns value of a Card
	 */
	public int getValue() {
		return value;
	}
	/**
	 * this method returns type of a Card
	 * @return type returns type of a Card
	 */
	public int getType() {
		return type;
	}

	@Override
	public int compareTo(Card card) {

		int value = ((Card) card).getValue(); 

		//ascending order
		return this.getValue() - value;
	}

	//This method creates a compatator to sort cards by value in descrending order
	
	public static Comparator<Card> CardValueAscComparator = new Comparator<Card>() {

		public int compare(Card card1, Card card2) {

			Integer cardValue1 = card1.getValue();
			Integer cardValue2 = card2.getValue();

			//descrending order
			return cardValue2.compareTo(cardValue1);

		}
	};

	//This method creates a compatator to sort cards by value in ascending order

	public static Comparator<Card> CardValueDescComparator = new Comparator<Card>() {

		public int compare(Card card1, Card card2) {

			Integer cardValue1 = card1.getValue();
			Integer cardValue2 = card2.getValue();

			//ascending order
			return cardValue1.compareTo(cardValue2);

		}
	};
	//This method creates a compatator to sort cards by type in ascending order

	public static Comparator<Card> CardTypeAscComparator = new Comparator<Card>() {

		public int compare(Card card1, Card card2) {

			Integer cardType1 = card1.getType();
			Integer cardType2 = card2.getType();

			return cardType2.compareTo(cardType1);

		}
	};
	//This method creates a compatator to sort cards by type in descending order

	public static Comparator<Card> CardTypedescComparator = new Comparator<Card>() {

		public int compare(Card card1, Card card2) {

			Integer cardType1 = card1.getType();
			Integer cardType2 = card2.getType();

			return cardType1.compareTo(cardType2);

		}
	};

}
