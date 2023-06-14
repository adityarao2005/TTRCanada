package model;

import java.awt.Point;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

// BY VITHURSH
// Represents the Routes
public class Route extends JLabel {
	private static final long serialVersionUID = 1L;
	// The serialVersionUID is a unique identifier for Serializable classes.
	// It is used during deserialization to ensure that the sender and receiver
	// of a serialized object have loaded classes for that object that are compatible
	// with respect to serialization. It helps maintain compatibility between
	// different versions of the class.

	// Fields
	private City startCity;
	// Represents the starting city of the route.

	private City destinationCity;
	// Represents the destination city of the route.

	private int length;
	// Represents the length of the route.

	private CardColour trainType;
	// Represents the type of train cards required to claim the route.

	private Point point;
	// Represents the coordinates of the route on the game board.

	private boolean isDualRail = false;
	// Indicates whether the route is a dual rail route or not.

	private Player claimer;
	// Represents the player who has claimed the route.
	// If null, the route is unclaimed. Otherwise, it stores a reference to the claiming player.

	// Constructor
	public Route() {
		this.setSize(15, 15);
	}

	// Getters and Setters
	public City getStartCity() {
		return startCity;
	}

	public void setStartCity(City startCity) {
		this.startCity = startCity;
	}

	public City getDestinationCity() {
		return destinationCity;
	}

	public void setDestinationCity(City destinationCity) {
		this.destinationCity = destinationCity;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public CardColour getTrainType() {
		return trainType;
	}

	public void setTrainType(CardColour trainType) {
		this.trainType = trainType;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
		setLocation(point);
	}

	public boolean isDualRail() {
		return isDualRail;
	}

	public void setDualRail(boolean isDualRail) {
		this.isDualRail = isDualRail;
	}

	public Player getClaimer() {
		return claimer;
	}

	public void setClaimer(Player claimer) {
		this.claimer = claimer;

		// Set the icon
		if (claimer != null)
			this.setIcon(new ImageIcon(String.format("images/checkmark%s.png", claimer.getColour())));
	}

	// For checking double routes
	@Override
	public int hashCode() {
		return Objects.hash(startCity, destinationCity);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Route other = (Route) obj;
		return Objects.equals(claimer, other.claimer) && Objects.equals(destinationCity, other.destinationCity)
				&& isDualRail == other.isDualRail && length == other.length && Objects.equals(point, other.point)
				&& Objects.equals(startCity, other.startCity) && trainType == other.trainType;
	}

	// Check if this route is identical to another route for serialization
	public boolean isIdentical(Route route) {
		return Objects.equals(startCity, route.startCity) && Objects.equals(destinationCity, route.destinationCity)
				&& Objects.equals(length, route.length) && Objects.equals(trainType, route.trainType);
	}

	@Override
	public String toString() {
		return "Route [startCity=" + startCity + ", destinationCity=" + destinationCity + ", length=" + length
				+ ", trainType=" + (trainType == CardColour.RAINBOW ? "GRAY" : trainType) + "]";
	}

	// Gets the other city if found
	public City getOther(City city) {
		// If the current city is the start, return the destination
		if (city.equals(startCity))
			return destinationCity;
		// If the current city is destination, return the start
		else if (city.equals(destinationCity))
			return startCity;
		// Else return null, current is not in this city
		else
			return null;
	}
}