package model;

import java.awt.Color;

// DONE BY ADITYA RAO
// Represents the color of the card
public enum CardColour {
	// Enums with respective file location and awt colour correspondant
	BLACK("images/trainCardBlack.png", Color.BLACK), BLUE("images/trainCardBlue.png", Color.BLUE),
	GREEN("images/trainCardGreen.png", Color.GREEN), ORANGE("images/trainCardOrange.png", Color.ORANGE),
	PURPLE("images/trainCardPurple.png", new Color(255, 0, 255)), RED("images/trainCardRed.png", Color.RED),
	WHITE("images/trainCardWhite.png", Color.WHITE), YELLOW("images/trainCardYellow.png", Color.YELLOW),
	RAINBOW("images/trainCardRainbow.png", Color.GRAY);

	// Fields
	private String file;
	private Color color;

	// Constructor, accepts the file loc and the awt colour
	CardColour(String file, Color color) {
		this.file = file;
		this.color = color;
	}

	// Getters
	public String getFile() {
		return file;
	}

	public Color getColor() {
		return color;
	}

}
