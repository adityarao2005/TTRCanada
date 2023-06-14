package model;

import javax.swing.ImageIcon;
// Import Statements
import javax.swing.JLabel;

// BY RISANTH
// Start of the TrainCard class and extends JLabel
public class TrainCard extends JLabel {
	private static final long serialVersionUID = 1L;

	// Field
	private CardColour type;

	// Constructor
	public TrainCard(CardColour type) {
		super();
		this.type = type;
		setSize(125, 75);
		setIcon(new ImageIcon(type.getFile()));
	}

	// Getter and Setter methods
	public CardColour getType() {
		return type;
	}

	public void setType(CardColour type) {
		this.type = type;
	}

	// To String method
	@Override
	public String toString() {
		return "TrainCard [type=" + type + "]";
	}
	
	// Checks if the traincard is a rainbow card
	public boolean isRainbow() {
		return type == CardColour.RAINBOW;
	}

}