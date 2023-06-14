package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import model.TrainCard;

// BY JOE
// Card Panel that displays the train cards deck, ticket cards deck and 5 train cards in the river
public class CardPanel extends JPanel {

	// Fields
	private JLabel deckImage = new JLabel();
	private JLabel ticketImage = new JLabel();
	private TrainCard[] river = new TrainCard[5];
	private JPanel actualCardPanel = new JPanel();

	// contructor
	public CardPanel() {
		this.deckImage.setIcon(new ImageIcon("images/cardBack.png"));
		this.ticketImage.setIcon(new ImageIcon("images/ticketDeck.png"));
		deckImage.setSize(125, 75);
		ticketImage.setSize(125, 75);

		actualCardPanel.add(deckImage);
		actualCardPanel.add(ticketImage);

		deckImage.setEnabled(false);
		ticketImage.setEnabled(false);

		actualCardPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 50, 5));
		actualCardPanel.setSize(1400, 100);
		actualCardPanel.setBackground(Color.CYAN);
		setBorder(new BevelBorder(BevelBorder.LOWERED, Color.GRAY, Color.BLACK));

		setLayout(new BorderLayout());
		add(actualCardPanel, BorderLayout.CENTER);

		//
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(null);
		headerPanel.setPreferredSize(new Dimension(1400, 25));
		headerPanel.setBackground(Color.CYAN);

		JLabel deckLabel = new JLabel("TRAIN CARD DECK");
		deckLabel.setBounds(60, 0, 125, 30);

		headerPanel.add(deckLabel);

		JLabel ticketLabel = new JLabel("TICKET DECK");
		ticketLabel.setBounds(250, 0, 125, 30);


		headerPanel.add(ticketLabel);

		JLabel cardLabel = new JLabel("CARD PANEL");
		cardLabel.setBounds(850, 0, 125, 30);

		headerPanel.add(cardLabel);

		add(headerPanel, BorderLayout.NORTH);

	}

	// getters and setters
	public JLabel getDeckImage() {
		return deckImage;
	}

	public void setDeckImage(JLabel deckImage) {
		this.deckImage = deckImage;
	}

	public JLabel getTicketImage() {
		return ticketImage;
	}

	public void setTicketImage(JLabel ticketImage) {
		this.ticketImage = ticketImage;
	}

	public TrainCard getCardAt(int index) {
		return river[index];
	}

	public TrainCard setCardAt(int index, TrainCard newCard) {
		TrainCard oldCard = river[index];
		if (oldCard != null)
			actualCardPanel.remove(oldCard);

		actualCardPanel.add(newCard, 2 + index);
		river[index] = newCard;

		return oldCard;

	}

	public void replaceCard(TrainCard old, TrainCard newCard) {
		int index = -1;

		for (int i = 0; i < 5; i++) {
			if (river[i].equals(old)) {
				index = i;
				break;
			}
		}

		if (index == -1) {
			System.err.println("Error: Could not find old card");
			return;
		}

		setCardAt(index, newCard);
	}

	public TrainCard[] getRiver() {
		return river;
	}

	public void setRiver(TrainCard[] river) {
		this.river = river;
	}

}