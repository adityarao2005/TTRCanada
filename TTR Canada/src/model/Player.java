package model;

import java.io.Serializable;
import java.util.ArrayList;

import controller.TTRController;

// BY JOE
// Represents the person who is playing the game
public class Player implements Serializable {
	private static final long serialVersionUID = 1L;

	// Fields
	private PlayerColour colour;
	private String name;
	private int numberOfTrain = 45;
	private ArrayList<Ticket> tickets = new ArrayList<>();
	private ArrayList<TrainCard> trainCards = new ArrayList<>();
	private ArrayList<Route> claimedRoutes = new ArrayList<>();
	private int score = 0;
	private boolean isAI;

	// constructor
	public Player(PlayerColour colour, String name, boolean isAI) {
		this.colour = colour;
		this.name = name;
		this.isAI = isAI;
	}

	// getters and setters
	public PlayerColour getColour() {
		return colour;
	}

	public void setColour(PlayerColour colour) {
		this.colour = colour;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumberOfTrain() {
		return numberOfTrain;
	}

	public void setNumberOfTrain(int numberOfTrain) {
		this.numberOfTrain = numberOfTrain;
	}

	public ArrayList<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(ArrayList<Ticket> tickets) {
		this.tickets = tickets;
	}

	public ArrayList<TrainCard> getTrainCards() {
		return trainCards;
	}

	public void setTrainCards(ArrayList<TrainCard> trainCards) {
		this.trainCards = trainCards;
	}

	public ArrayList<Route> getClaimedRoutes() {
		return claimedRoutes;
	}

	public void setClaimedRoutes(ArrayList<Route> claimedRoutes) {
		this.claimedRoutes = claimedRoutes;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean isAI() {
		return isAI;
	}

	public void setAI(boolean isAI) {
		this.isAI = isAI;
	}

	// if number of trains gets to 2 or less,
	public boolean endGame() {
		// Returns whether we have reached the end of the game
		if (numberOfTrain <= TTRController.TRAIN_LIMIT) {
			return true;
		}
		return false;
	}

	// BY JOE

	// check how many cards with certain type does the player have
	public int checkTrainCard(CardColour type) {

		int numberOfColour = 0;

		for (TrainCard card : this.trainCards) {
			if (card.getType().equals(type)) {
				numberOfColour++;
			}
		}
		return numberOfColour;
	}

	// check the number of cards with the type that the player has the most (except
	// for rainbow)
	public int checkMaxTrainCard() {

		int numberOfColour = 0;

		for (CardColour type : CardColour.values()) {

			if (type != CardColour.RAINBOW) {
				numberOfColour = Math.max(numberOfColour, checkTrainCard(type));
			}

		}
		return numberOfColour;
	}

	@Override
	public String toString() {
		return "Player [colour=" + colour + ", name=" + name + ", numberOfTrain=" + numberOfTrain + ", tickets="
				+ tickets + ", trainCards=" + trainCards + ", claimedRoutes=" + claimedRoutes + ", score=" + score
				+ ", isAI=" + isAI + "]";
	}

}