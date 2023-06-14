package view;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import model.CardColour;
import model.Player;
import model.Ticket;
import model.TrainCard;

// Player Panel
// DONE BY ADITYA RAO
// Represents the view component for the portion on the GameFrame which depicts the attributes of the player
public class PlayerPanel extends JPanel {

	// Fields
	// Constants
	private static final String NAME_HEADER = "NAME: ";
	private static final String COLOUR_HEADER = "COLOUR: ";
	private static final String NUMBER_TRAINS_HEADER = "NUMBER OF TRAINS: ";
	private static final String TRAINS_HEADER = "TRAIN CARDS: ";
	private static final String TITLE = "PLAYER PANEL";
	private static final String TICKETS_TITLE = "TICKETS: ";
	private static final String CLAIM_ROUTE_TEXT = "CLAIM ROUTE";
	private static final String NEXT_TURN_TEXT = "NEXT TURN";

	// Width of the entire window is around 1920
	// Width of the board panel is around 1400
	// Width of this panel will be around 1920 - 1400 = 520
	// Height we can say is the same height as the board panel which is 900
	private static final int WIDTH = 504;
	private static final int HEIGHT = 617;
	private static final int SPACING = 30;

	// Labels
	private JLabel nameLabel = new JLabel(NAME_HEADER + "Test");
	private JLabel colourLabel = new JLabel(COLOUR_HEADER + "Colour");
	private JLabel[] trainCardLabels = new JLabel[9];
	private JLabel numberTrainsLabel = new JLabel(NUMBER_TRAINS_HEADER);

	// Buttons
	private JButton claimRouteButton = new JButton(CLAIM_ROUTE_TEXT);
	private JButton nextTurnButton = new JButton(NEXT_TURN_TEXT);

	// Tickets
	private JList<Ticket> ticketsList = new JList<>();

	// Current player
	private Player current;

	// Constructor
	public PlayerPanel() {
		// Initialize the panel

		// not using layouts just yet
		setLayout(null);
		setBorder(new BevelBorder(BevelBorder.LOWERED, Color.GRAY, Color.BLACK));
		setBackground(new Color(255, 200, 200));

		// Sets the size
		setSize(WIDTH, HEIGHT);

		// Create a title label and add it to the panel
		JLabel title = new JLabel(TITLE);
		title.setSize(520, SPACING);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setVerticalAlignment(SwingConstants.CENTER);
		this.add(title);

		// Set the location, size, and font of the name label
		nameLabel.setLocation(0, SPACING);
		nameLabel.setSize(260, SPACING);
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		nameLabel.setVerticalAlignment(SwingConstants.CENTER);
		this.add(nameLabel);

		// Set the location, size, and font of the name label
		colourLabel.setLocation(260, SPACING);
		colourLabel.setSize(200, SPACING);
		colourLabel.setHorizontalAlignment(SwingConstants.CENTER);
		colourLabel.setVerticalAlignment(SwingConstants.CENTER);
		this.add(colourLabel);

		// create the ticket title and add it to the
		JLabel ticketLabel = new JLabel(TICKETS_TITLE);
		ticketLabel.setLocation(20, 2 * SPACING);
		ticketLabel.setSize(520, SPACING);
		ticketLabel.setHorizontalAlignment(SwingConstants.LEFT);
		ticketLabel.setVerticalAlignment(SwingConstants.CENTER);
		this.add(ticketLabel);

		// Work on the tickets list
		ticketsList.setLayoutOrientation(JList.VERTICAL);
		ticketsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ticketsList.setVisibleRowCount(-1);

		JScrollPane listScroller = new JScrollPane(ticketsList);
		listScroller.setSize(480, 150);
		listScroller.setLocation(10, 3 * SPACING);
		this.add(listScroller);

		// Set the location, size, and font of the train cards label
		JLabel trainCardsLabel = new JLabel(TRAINS_HEADER);
		trainCardsLabel.setLocation(10, 150 + 3 * SPACING);
		trainCardsLabel.setSize(260, SPACING);
		trainCardsLabel.setVerticalAlignment(SwingConstants.CENTER);
		this.add(trainCardsLabel);

		// Initialize trains array
		// For each colour
		for (int i = 0; i < trainCardLabels.length; i++) {
			// Get the colour
			CardColour colour = CardColour.values()[i];
			
			// Create the text labels			
			JLabel label = new JLabel(colour.name());
			label.setLocation(50, 150 + 4 * SPACING + SPACING * i);
			label.setBackground(colour.getColor());
			label.setOpaque(true);
			label.setSize(65, SPACING);
			label.setHorizontalTextPosition(SwingConstants.LEADING);
			this.add(label);
			
			// Add contrast to the card labels
			if (colour == CardColour.BLACK || colour == CardColour.BLUE)
				label.setForeground(Color.WHITE);
			

			// Set the initial text for the train labels
			trainCardLabels[i] = new JLabel("0");
			trainCardLabels[i].setLocation(130, 150 + 4 * SPACING + SPACING * i);
			trainCardLabels[i].setSize(130, SPACING);
			trainCardLabels[i].setHorizontalTextPosition(SwingConstants.LEADING);
			this.add(trainCardLabels[i]);
		}

		// Set the location, size, and font of the name label
		numberTrainsLabel.setLocation(260, 150 + 3 * SPACING);
		numberTrainsLabel.setSize(260, SPACING);
		numberTrainsLabel.setVerticalAlignment(SwingConstants.CENTER);
		this.add(numberTrainsLabel);

		// Setup the claim route button
		claimRouteButton.setLocation(260, 150 + 6 * SPACING);
		claimRouteButton.setSize(200, 2 * SPACING);
		claimRouteButton.setHorizontalAlignment(SwingConstants.CENTER);
		claimRouteButton.setVerticalAlignment(SwingConstants.CENTER);
		claimRouteButton.setEnabled(false);
		this.add(claimRouteButton);

		// Setup the next turn button
		nextTurnButton.setLocation(260, HEIGHT - 150);
		nextTurnButton.setSize(200, 4 * SPACING);
		nextTurnButton.setHorizontalAlignment(SwingConstants.CENTER);
		nextTurnButton.setVerticalAlignment(SwingConstants.CENTER);
		nextTurnButton.setEnabled(false);
		this.add(nextTurnButton);
	}

	// Getters and setters
	public Player getCurrent() {
		return current;
	}

	// THIS IS A MUST TO CALL WHEN CHANGING THE TURNS
	public void setCurrent(Player current) {
		this.current = current;
		refresh();
	}

	public JButton getClaimRouteButton() {
		return claimRouteButton;
	}

	public void setClaimRouteButton(JButton claimRouteButton) {
		this.claimRouteButton = claimRouteButton;
	}

	public JButton getNextTurnButton() {
		return nextTurnButton;
	}

	public void setNextTurnButton(JButton nextTurnButton) {
		this.nextTurnButton = nextTurnButton;
	}

	// Utility methods
	// Must call in order for the routes to be refreshed
	// Will do most of the UI work for this panel
	public void refresh() {
		// Get the current player
		Player current = getCurrent();

		// Setup the labels
		nameLabel.setText(NAME_HEADER + current.getName());
		colourLabel.setText(COLOUR_HEADER + current.getColour());
		colourLabel.setBackground(current.getColour().getColor());
		colourLabel.setOpaque(true);
		ticketsList.setListData(new Vector<>(current.getTickets()));
		numberTrainsLabel.setText(NUMBER_TRAINS_HEADER + current.getNumberOfTrain());

		// Set the train cards
		Map<CardColour, Integer> trainCards = new HashMap<>();
		for (CardColour colour : CardColour.values())
			trainCards.put(colour, 0);

		// Increment the traincards as we see them
		for (TrainCard card : current.getTrainCards())
			trainCards.replace(card.getType(), trainCards.get(card.getType()) + 1);

		// Update the train card labels
		for (int i = 0; i < trainCardLabels.length; i++) {
			// Update the string
			trainCardLabels[i].setText(trainCards.get(CardColour.values()[i]).toString());
		}
	}

}
