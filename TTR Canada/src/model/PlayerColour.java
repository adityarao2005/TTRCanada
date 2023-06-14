package model;

import java.awt.Color;

// DONE BY ADITYA RAO
// Represents the colour of the player
public enum PlayerColour {
	// Enums with Respective awt colour
	Blue(Color.CYAN), Green(Color.GREEN), Red(Color.RED), Yellow(Color.YELLOW);

	// Fields
	private Color color;
	
	// Constructor - accepts the awt colour correspondant
	PlayerColour(Color color) {
		this.color = color;
	}

	// Getters
	public Color getColor() {
		return color;
	}

}
