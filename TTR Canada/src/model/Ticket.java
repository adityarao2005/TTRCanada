package model;

import java.io.Serializable;
import java.util.Objects;

// BY RISANTH
// Start of the class
public class Ticket implements Serializable {
	private static final long serialVersionUID = 1L;

	// Fields
	private City startCity;
	private City destinationCity;
	private int pointWorth;
	private boolean completed;

	// Constructor method
	public Ticket(City startCity, City destinationCity, int pointWorth) {
		super();
		this.startCity = startCity;
		this.destinationCity = destinationCity;
		this.pointWorth = pointWorth;
	}

	// Getter and Setter methods
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

	public int getPointWorth() {
		return pointWorth;
	}

	public void setPointWorth(int pointWorth) {
		this.pointWorth = pointWorth;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;

	}

	// To String method
	@Override
	public String toString() {
		return "Ticket [startCity=" + startCity + ", destinationCity=" + destinationCity + ", pointWorth=" + pointWorth
				+ ", completed=" + completed + "]";
	}

	// Modified the hashcode method to remove duplicates when used in HashSet
	@Override
	public int hashCode() {
		return Objects.hash(completed, destinationCity, pointWorth, startCity);
	}

	// Checks whether both tickets are the same
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ticket other = (Ticket) obj;
		return completed == other.completed && Objects.equals(destinationCity, other.destinationCity)
				&& pointWorth == other.pointWorth && Objects.equals(startCity, other.startCity);
	}

}