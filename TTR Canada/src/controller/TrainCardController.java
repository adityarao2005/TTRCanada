package controller;

import java.util.Collections;
import java.util.Stack;

import javax.swing.JOptionPane;

import model.CardColour;
import model.Player;
import model.TrainCard;
import view.CardPanel;

// BY RISANTH
public class TrainCardController {
	
	// Fields
	private TTRController controller;
	private Stack<TrainCard> deck;
	private Stack<TrainCard> dump;

	
	// Getters and Setters
	public TrainCardController() {
		controller = TTRController.getInstance();
	}

	public Stack<TrainCard> getDeck() {
		return deck;
	}

	public void setDeck(Stack<TrainCard> deck) {
		this.deck = deck;
	}

	public Stack<TrainCard> getDump() {
		return dump;
	}

	public void setDump(Stack<TrainCard> dump) {
		this.dump = dump;
	}

	// Creates the train card deck
	public void CreateCardDeck() {

		// Use a enhanced for loop to get the color out of the CardColor and use .values
		// to determine the color
		for (CardColour colour : CardColour.values()) {

			// Sets the default loop amount to 12
			int limit = 12;

			// If the card color is rainbow, the loop is increased to 14 times
			if (colour == CardColour.RAINBOW)
				limit = 14;

			// The loop will run to the limit amount and after will add the cards to the
			// deck
			for (int i = 0; i <= limit; i++) {
				TrainCard card = new TrainCard(colour);
				card.addMouseListener(controller);
				deck.add(card);
			}
		}

		// This will shuffle the card deck
		Collections.shuffle(deck);

	}

	// Flip the cards until we find a suitable river
	public void FlipCards() {
		// Perform this until we get a deck without 3 or more wildcards
		CardPanel panel = controller.getGameFrame().getCardPanel();

		do {

			// Flip the cards
			for (int i = 0; i < 5; i++) {
				TrainCard old = panel.setCardAt(i, deck.pop());

				// If there are old cards, ad them back to the dump deck
				if (old != null)
					controller.getDumpDeck().add(old);

				// Make sure the deck never gets empty
				preventEmptyTrainDeck();
			}

			// Runs the loop while there isn't 3 rainbow cards
		} while (CheckThreeRainbowCards());
	}

	// This methods checks that out of the drawn cards used for the river if 3 or
	// more of the cards are rainbow
	public boolean CheckThreeRainbowCards() {
		// Perform this until we get a deck without 3 or more wildcards
		CardPanel panel = controller.getGameFrame().getCardPanel();

		// Starts the rainbowcount at zero
		int rainbowCount = 0;

		// Checks if the card drawn is a rainbow card
		for (TrainCard card : panel.getRiver()) {
			if (card.getType() == CardColour.RAINBOW) {
				rainbowCount++;
			}
		}

		// Return if the rainbow count less then or equal to 3
		return rainbowCount > 2;
	}

	// This method prevents a empty train deck as it refills it from the dump deck
	public void preventEmptyTrainDeck() {

		// This method will refill the traindeck with dump cards and will shuffle them
		// as well
		if (deck.isEmpty()) {
			deck.addAll(dump);
			dump.clear();
			Collections.shuffle(deck);
		}
	}

	// Deals the train cards
	public void DealCards() {

		// Runs the loop 4 times because each player gets 4 cards each
		for (int round = 0; round < 4; round++) {

			// This represents the current player the loop is at and the other part
			// represents the total amount of players
			for (Player player : controller.getPlayers()) {

				// This takes the card off the deck and adds the card to the players deck
				TrainCard card = controller.getTrainCardDeck().pop();
				player.getTrainCards().add(card);

				// If deck is empty, take cards from discard pile and add to deck
				preventEmptyTrainDeck();

			}
		}
	}

	// This method flips over a new card into the old location of where the player
	// selected a card from in the river
	public void FlippingCard(TrainCard card, Player player) {
		CardPanel panel = controller.getGameFrame().getCardPanel();

		// Add the card which got clicked to the users collection of triancards
		player.getTrainCards().add(card);
		
		// Replaces the card in the panel
		panel.replaceCard(card, deck.pop());
		
		// This prevents a empty train deck
		preventEmptyTrainDeck();

		// This 3 rainbow cards are found it will flip the cards
		if (CheckThreeRainbowCards()) {

			JOptionPane.showMessageDialog(controller.getGameFrame(), "3 jokers found in river!");

			FlipCards();

		}

		// This gets the player panel and refresh the panel
		controller.getGameFrame().getPlayerPanel().refresh();
	}

	public void DealSingleCard(Player player) {
		// Pop the traincard from the tain card stack and add to the player
		TrainCard card = deck.pop();

		// This loop is for the AI
		if (!player.isAI())
			JOptionPane.showMessageDialog(controller.getGameFrame(), String.format("%s just picked up a %s card", player.getName(), card.getType()));
		else
			JOptionPane.showMessageDialog(controller.getGameFrame(), String.format("AI %s just picked up a card", player.getName()));

		// Adds the traincard to the player
		player.getTrainCards().add(card);

		// Prevents a empty train deck
		preventEmptyTrainDeck();

	}

	// Disables all the wild cards
	public void disableWildCards() {
		
		// This gets the frame and then the card panel
		CardPanel panel = controller.getGameFrame().getCardPanel();
		
		// Go through all the cards in the river
		for (int i = 0; i < 5; i++) {
			// If the card colour is rainbow, disable it
			if (panel.getCardAt(i).getType() == CardColour.RAINBOW)
				panel.getCardAt(i).setEnabled(false);
		}
	}
}