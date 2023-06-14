package view;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import controller.TTRController;
import model.Player;

// BY VITHURSH
// Shows the score
public class ScorePanel extends JPanel {

	private JLabel[] scores = new JLabel[4];
	// Array to hold JLabel components representing player scores

	public ScorePanel() {
		setLayout(null);
		// Sets the layout manager of the ScorePanel to null, indicating custom positioning of components

		setBorder(new BevelBorder(BevelBorder.LOWERED, Color.GRAY, Color.BLACK));
		// Sets the border style of the ScorePanel using a BevelBorder with lower bevel, gray color, and black outline

		this.setBackground(new Color(255,233,0));
		// Changes the background color of the ScorePanel to yellow (RGB value: 255, 233, 0)

		JLabel title = new JLabel("SCORE PANEL");
		// Creates a new JLabel instance with the text "SCORE PANEL"

		title.setLocation(200, 100);
		// Sets the location (coordinates) of the 'title' JLabel to (200, 100)

		title.setSize(200, 40);
		// Sets the size of the 'title' JLabel to be 200 pixels wide and 40 pixels tall

		add(title);
		// Adds the 'title' JLabel to the ScorePanel

		for (int i = 0; i < scores.length; i++) {
			scores[i] = new JLabel(String.format("Player %d's Score: %10d", i + 1, 0));
			// Creates a new JLabel for each player score with a formatted string representing the player number and score

			scores[i].setLocation(200, 150 + i * 40);
			// Sets the location (coordinates) of the 'scores' JLabel for each player

			scores[i].setSize(200, 40);
			// Sets the size of the 'scores' JLabel for each player to be 200 pixels wide and 40 pixels tall

			add(scores[i]);
			// Adds the 'scores' JLabel for each player to the ScorePanel
		}
	}

	public JLabel[] getScores() {
		return scores;
		// Returns the 'scores' array containing the JLabel components representing player scores
	}

	public void setScores(JLabel[] scores) {
		this.scores = scores;
		// Sets the 'scores' array to the provided array of JLabel components representing player scores
	}

	public void refresh() {
		for (int i = 0; i < scores.length; i++) {
			scores[i].setText(String.format("Player %d Score: %10d", i + 1,
					TTRController.getInstance().getPlayers()[i].getScore()));
			// Updates the text of each 'scores' JLabel to reflect the current score of each player
		}
	}
}